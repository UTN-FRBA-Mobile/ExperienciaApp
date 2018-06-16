package utn.frba.mobile.experienciaapp.experiencia;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import im.delight.android.location.SimpleLocation;
import utn.frba.mobile.experienciaapp.BaseActivityWithToolBar;
import utn.frba.mobile.experienciaapp.R;
import utn.frba.mobile.experienciaapp.lib.errors.ErrorTreatment;
import utn.frba.mobile.experienciaapp.lib.googlemaps.GeocoderTask;
import utn.frba.mobile.experienciaapp.lib.googlemaps.GoogleMapsUtils;
import utn.frba.mobile.experienciaapp.lib.animations.slidinguppanel.SlidingUpPanelLayout;
import utn.frba.mobile.experienciaapp.lib.googlemaps.ReciveAdress;
import utn.frba.mobile.experienciaapp.lib.permisions.PermisionsUtils;
import utn.frba.mobile.experienciaapp.lib.utils.Alert;
import utn.frba.mobile.experienciaapp.lib.ws.ReciveResponseWS;
import utn.frba.mobile.experienciaapp.lib.ws.ResponseWS;
import utn.frba.mobile.experienciaapp.lib.ws.WSRetrofit;
import utn.frba.mobile.experienciaapp.models.Experiencia;
import utn.frba.mobile.experienciaapp.models.Interes;
import utn.frba.mobile.experienciaapp.models.Productor;

import static utn.frba.mobile.experienciaapp.lib.googlemaps.GoogleMapsUtils.FAR_ZOOM;

public class BuscarExperienciaActivity extends BaseActivityWithToolBar implements OnMapReadyCallback, ReciveResponseWS, ReciveAdress {

    //TODO: PAsar a preference store
    public static String RADIO_DISTANCIA = "10";
    public static LatLng INIT_LOCATION;

    private static final int GET_EXPERIENCIAS = 1;
    private static final int FILTER_EXPERIENCIAS = 2;
    private static final int GET_INTERESES = 3;

    private static final String MY_PREFERECES = "BuscarExperienciaActivity";

    private static final String TAG = "BuscarExperienciaAct";
    private SharedPreferences sharedPref;

    private SlidingUpPanelLayout mLayout;
    private GoogleMap mMap;
    private SimpleLocation simpleLocation;

    private List<Experiencia> experiencias = new ArrayList<>();
    private List<Interes> intereses = new ArrayList<>();

    public Map<Integer, Marker> markerList = new HashMap<>();

    public Alert distanciaFilterAlert;
    public View distanciaView;
    public Alert interesesFilterAlert;
    public View interesesView;
    public Set<Integer> interesesFiltrados = new HashSet<>();
    public Alert fechaHoraFilterAlert;
    public View fechaHoraView;
    public Alert presupuestoFilterAlert;
    public View presupuestoView;

    public SimpleDateFormat mySqlDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public SimpleDateFormat localDateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    public SimpleDateFormat localDateFormat = new SimpleDateFormat("dd/MM/yyyy");
    public SimpleDateFormat localTimeFormat = new SimpleDateFormat("HH:mm");

    public Marker myLocation;

    //Filtros
    private ImageView interesesIB, fechaHoraIB, presupuestoIB, distanciaIB,limpiarFiltrosIB;

    //PreferenceValues
    boolean flagMiPosicionFiltro,flagAlertInicio = false;
    String key_last_lat,key_last_long,key_distancia_filtro,key_intereses_filtro,key_fecha_hora_inicio_filtro,key_fecha_hora_fin_filtro,key_presupuesto_inicio_filtro,key_presupuesto_fin_filtro = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //experiencias = MockExperiences();//TODO: Borrar, solo para testing
        //intereses = MockIntereses();//TODO: Borrar, solo para testing
        Interes.GetIntereses().enqueue(WSRetrofit.ParseResponseWS(BuscarExperienciaActivity.this, GET_INTERESES));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_experiencia);
        setSupportActionBar((Toolbar) findViewById(R.id.main_toolbar));

        sharedPref = getSharedPreferences(MY_PREFERECES,Context.MODE_PRIVATE);

        AddBackButtonToToolBar();
        simpleLocation = GoogleMapsUtils.InitializeSimpleLocation(this);
        inicializarFiltros();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        SetMyLocationButton();

        SetListOfExperiencias(experiencias);

        if (PermisionsUtils.canAccessLocation(BuscarExperienciaActivity.this) && simpleLocation.hasLocationEnabled()) {
            final double latitude = simpleLocation.getLatitude();
            final double longitude = simpleLocation.getLongitude();

            INIT_LOCATION = new LatLng(latitude, longitude);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        RefreshPreferenceValues();
        if (!flagAlertInicio){
            ShowPedirUbicacionAlert(true);
        }else if(!key_last_lat.equals("") && !key_last_long.equals("") && !key_distancia_filtro.equals("")){
            Experiencia.Filter(key_intereses_filtro, key_fecha_hora_inicio_filtro, key_fecha_hora_fin_filtro, key_presupuesto_inicio_filtro, key_presupuesto_fin_filtro, key_last_lat, key_last_long, key_distancia_filtro).enqueue(WSRetrofit.ParseResponseWS(BuscarExperienciaActivity.this, FILTER_EXPERIENCIAS));
        }else {
            ShowPedirUbicacionAlert(true);
        }
    }

    public void RefreshPreferenceValues(){
        //Inicializar valores de filtros
        flagAlertInicio = sharedPref.getBoolean(getString(R.string.key_alert_inicio_buscar_experiencia), false);
        flagMiPosicionFiltro = sharedPref.getBoolean(getString(R.string.key_mi_posicion_filtro_value), false);

        key_last_lat = sharedPref.getString(getString(R.string.key_last_lat), "");
        key_last_long = sharedPref.getString(getString(R.string.key_last_long), "");
        key_distancia_filtro = sharedPref.getString(getString(R.string.key_distancia_filtro), "");
        key_intereses_filtro = sharedPref.getString(getString(R.string.key_intereses_filtro), "");
        key_fecha_hora_inicio_filtro = sharedPref.getString(getString(R.string.key_fecha_hora_inicio_filtro), "");
        key_fecha_hora_fin_filtro = sharedPref.getString(getString(R.string.key_fecha_hora_fin_filtro), "");
        key_presupuesto_inicio_filtro = sharedPref.getString(getString(R.string.key_presupuesto_inicio_filtro), "");
        key_presupuesto_fin_filtro = sharedPref.getString(getString(R.string.key_presupuesto_fin_filtro), "");
    }

    public void ShowPedirUbicacionAlert(boolean inicio){
        final Alert alertInicio = new Alert(this);
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertInicio.Dismiss();
                distanciaIB.performClick();

                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putBoolean(getString(R.string.key_alert_inicio_buscar_experiencia), true);
                editor.apply();
            }
        };

        if(inicio) {
            alertInicio.ShowConfirmation("Indicanos donde te gustaria encontrar nuevas experiencias, puedes usar distintas combinaciones de filtros.", "Nuevas Experiencias", onClickListener, false);
        }else{
            alertInicio.ShowConfirmation("Para filtrar experiencias tenes que seleccionar antes una ubicación, y así combinar filtros.", "Nuevas Experiencias", onClickListener, false);
        }
    }

    @Override
    protected void onDestroy() {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(getString(R.string.key_alert_inicio_buscar_experiencia), false);
        editor.commit();
        super.onDestroy();
    }

    private void SetListOfExperiencias(List<Experiencia> listExperiencias) {
        TextView t = (TextView) findViewById(R.id.listadoExperienciasTV);
        t.setText(Html.fromHtml(getString(R.string.listado_de_experiencias)));

        ListView lv = (ListView) findViewById(R.id.list);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Experiencia experiencia = (Experiencia) ((ListView) parent).getAdapter().getItem(position);
                if (experiencia != null && experiencia.getLongitud() != null && experiencia.getLatitud() != null) {
                    GoogleMapsUtils.GoToLocationInMap(mMap, experiencia.getLatitud(), experiencia.getLongitud(), null);
                    mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                    markerList.get(experiencia.getId()).showInfoWindow();
                }
            }
        });

        ExperienciaListAdapter experienciaListAdapterAdapter = new ExperienciaListAdapter(this, listExperiencias);

        lv.setAdapter(experienciaListAdapterAdapter);

        mLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        mLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                Log.i(TAG, "onPanelSlide, offset " + slideOffset);
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                Log.i(TAG, "onPanelStateChanged " + newState);
            }
        });
        mLayout.setFadeOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        });
    }

    private void SetAutocompleteUbicacionFromFilter() {
        // Construct a GeoDataClient for the Google Places API for Android.
        GeoDataClient mGeoDataClient = Places.getGeoDataClient(this, null);

        if (distanciaView != null) {
            // Retrieve the AutoCompleteTextView that will display Place suggestions.
            AutoCompleteTextView mAutocompleteView = (AutoCompleteTextView) distanciaView.findViewById(R.id.autoCompleteUbicacionATV);

            LatLngBounds latLngBounds = null;
            if (INIT_LOCATION != null) {
                //Circle circle = new Circle(5000, INIT_LOCATION);
                //latLngBounds = circle.getBounds();
                latLngBounds = new LatLngBounds(
                        INIT_LOCATION,
                        INIT_LOCATION
                );

            }


            // Set up the adapter that will retrieve suggestions from the Places Geo Data Client.
            PlaceAutocompleteAdapter mAdapter = new PlaceAutocompleteAdapter(
                    this,
                    mGeoDataClient,
                    latLngBounds,
                    null);
            mAutocompleteView.setAdapter(mAdapter);
        }
    }

    private void AddRefreshMap(List<Experiencia> experienciaList) {
        for (Map.Entry<Integer, Marker> markerEntry : markerList.entrySet()) {
            markerEntry.getValue().remove();
        }

        if (!experienciaList.isEmpty()) {
            for (Experiencia experiencia : experienciaList) {
                Marker marker = GoogleMapsUtils.AddMarkerOptionsToMap(
                        mMap,
                        GoogleMapsUtils.GetMarkerOptionsFor(experiencia),
                        R.drawable.ic_experience_point,
                        false
                );
                markerList.put(experiencia.getId(), marker);
            }
        }
    }

    @SuppressLint("MissingPermission")
    private void SetMyLocationButton() {
        ImageView myLocationButton = (ImageView) findViewById(R.id.myLocationButton);
        myLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (PermisionsUtils.canAccessLocation(BuscarExperienciaActivity.this)) {
                    if (!simpleLocation.hasLocationEnabled()) {
                        SimpleLocation.openSettings(BuscarExperienciaActivity.this);
                    } else {
                        final double latitude = simpleLocation.getLatitude();
                        final double longitude = simpleLocation.getLongitude();

                        if (mMap != null) {
                            /*
                            simpleLocation.setListener(new SimpleLocation.Listener() {

                                public void onPositionChanged() {
                                    final double latitude = simpleLocation.getLatitude();
                                    final double longitude = simpleLocation.getLongitude();
                                    Toast.makeText(BuscarExperienciaActivity.this, latitude + "," + longitude, Toast.LENGTH_SHORT).show();
                                    myLocation.remove();
                                    myLocation = GoogleMapsUtils.AddMarkerOptionsToMap(mMap, GoogleMapsUtils.GetMarkerOptionsFor(latitude, longitude), R.drawable.ic_user_point, false);
                                    GoogleMapsUtils.GoToLocationInMap(mMap, latitude, longitude, null);
                                }

                            });
                            if (myLocation != null)
                                myLocation.remove();
                            myLocation = GoogleMapsUtils.AddMarkerOptionsToMap(mMap, GoogleMapsUtils.GetMarkerOptionsFor(latitude, longitude), R.drawable.ic_user_point, false);
                            */
                            mMap.setMyLocationEnabled(true);
                            GoogleMapsUtils.GoToLocationInMap(mMap, latitude, longitude, null);
                        }
                    }
                } else {
                    PermisionsUtils.requestLocationPermissions(BuscarExperienciaActivity.this);
                }
            }
        });
    }

    @SuppressLint("MissingPermission")
    private void inicializarFiltros() {
        interesesIB = (ImageView) findViewById(R.id.interesesIB);
        fechaHoraIB = (ImageView) findViewById(R.id.fechaHoraIB);
        presupuestoIB = (ImageView) findViewById(R.id.presupuestoIB);
        distanciaIB = (ImageView) findViewById(R.id.distanciaIB);
        limpiarFiltrosIB = (ImageView) findViewById(R.id.limpiarFiltrosIB);

        limpiarFiltrosIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Alert limpiarFiltrosAlert = new Alert(BuscarExperienciaActivity.this);

                View.OnClickListener aceptOnClickListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString(getString(R.string.key_presupuesto_inicio_filtro), "");
                        editor.putString(getString(R.string.key_presupuesto_fin_filtro), "");
                        editor.putString(getString(R.string.key_intereses_filtro), "");
                        editor.putString(getString(R.string.key_fecha_hora_inicio_filtro), "");
                        editor.putString(getString(R.string.key_fecha_hora_fin_filtro), "");
                        editor.apply();
                        RefreshPreferenceValues();
                        limpiarFiltrosAlert.Dismiss();
                    }
                };

                limpiarFiltrosAlert.ShowConfirmation("¿Desea eliminar las preferenias de los filtros?","Limpiar Filtros",aceptOnClickListener,true);
            }
        });

        presupuestoIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presupuestoFilterAlert = new Alert(BuscarExperienciaActivity.this);
                LinearLayout ll_content = new LinearLayout(BuscarExperienciaActivity.this);
                ll_content.setOrientation(LinearLayout.VERTICAL);
                ll_content.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);

                LayoutInflater inflater = LayoutInflater.from(getBaseContext());
                presupuestoView = inflater.inflate(R.layout.filtro_prespuesto, ll_content, false);

                EditText precioInicioET = presupuestoView.findViewById(R.id.precioInicioET);
                EditText precioFinET = presupuestoView.findViewById(R.id.precioFinET);
                precioInicioET.setText(key_presupuesto_inicio_filtro);
                precioFinET.setText(key_presupuesto_fin_filtro);

                View.OnClickListener aceptOnclick = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText precioInicioET = presupuestoView.findViewById(R.id.precioInicioET);
                        EditText precioFinET = presupuestoView.findViewById(R.id.precioFinET);
                        String precioInicio = precioInicioET.getText().toString();
                        String precioFin = precioFinET.getText().toString();

                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString(getString(R.string.key_presupuesto_inicio_filtro), precioInicio);
                        editor.putString(getString(R.string.key_presupuesto_fin_filtro), precioFin);
                        editor.apply();
                        RefreshPreferenceValues();

                        if(key_last_lat.equals("") || key_last_long.equals("") || key_distancia_filtro.equals("")){
                            ShowPedirUbicacionAlert(false);
                            presupuestoFilterAlert.Dismiss();
                        }else {
                            presupuestoFilterAlert.Loading();
                            Experiencia.Filter(key_intereses_filtro, key_fecha_hora_inicio_filtro, key_fecha_hora_fin_filtro, precioInicio, precioFin, key_last_lat, key_last_long, key_distancia_filtro).enqueue(WSRetrofit.ParseResponseWS(BuscarExperienciaActivity.this, FILTER_EXPERIENCIAS));
                        }
                    }
                };

                presupuestoView.findViewById(R.id.limpiarFiltroBT).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText precioInicioET = presupuestoView.findViewById(R.id.precioInicioET);
                        EditText precioFinET = presupuestoView.findViewById(R.id.precioFinET);
                        precioInicioET.setText("");
                        precioFinET.setText("");
                    }
                });

                presupuestoFilterAlert.ShowFilterView(presupuestoView, "Presupuesto", aceptOnclick, false);
            }
        });

        fechaHoraIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                fechaHoraFilterAlert = new Alert(BuscarExperienciaActivity.this);

                LinearLayout ll_content = new LinearLayout(BuscarExperienciaActivity.this);
                ll_content.setOrientation(LinearLayout.VERTICAL);
                ll_content.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);

                LayoutInflater inflater = LayoutInflater.from(getBaseContext());
                fechaHoraView = inflater.inflate(R.layout.filtro_fechahora, ll_content, false);

                final EditText fechaInicioET = fechaHoraView.findViewById(R.id.fechaInicioET);
                fechaInicioET.setText(localDateFormat.format(calendar.getTime()));
                fechaInicioET.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final CalendarView calendarView = new CalendarView(BuscarExperienciaActivity.this);
                        calendarView.setDate(Calendar.getInstance().getTime().getTime());
                        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                            @Override
                            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                                EditText fechaInicioET = fechaHoraView.findViewById(R.id.fechaInicioET);
                                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(year, month, dayOfMonth, 0, 0);
                                fechaInicioET.setText(format.format(calendar.getTime()));
                            }
                        });
                        final Alert alertCalendar = new Alert(BuscarExperienciaActivity.this);

                        View.OnClickListener onClickListener = new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alertCalendar.Dismiss();
                            }
                        };

                        alertCalendar.ShowFilterView(calendarView, "Fecha Inicio", onClickListener, false);
                    }
                });

                final EditText horaInicioET = fechaHoraView.findViewById(R.id.horaInicioET);
                horaInicioET.setText(localTimeFormat.format(calendar.getTime()));
                horaInicioET.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Alert alertTimePicker = new Alert(BuscarExperienciaActivity.this);
                        final TimePicker timePicker = new TimePicker(BuscarExperienciaActivity.this);
                        timePicker.setIs24HourView(true);
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {//6.0
                            timePicker.setHour(Calendar.getInstance().get(Calendar.HOUR));
                            timePicker.setMinute(Calendar.getInstance().get(Calendar.MINUTE));
                        } else {
                            timePicker.setCurrentHour(Calendar.getInstance().get(Calendar.HOUR));
                            timePicker.setCurrentMinute(Calendar.getInstance().get(Calendar.MINUTE));
                        }

                        View.OnClickListener onClickListener = new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                EditText horaInicioET = fechaHoraView.findViewById(R.id.horaInicioET);
                                SimpleDateFormat format = new SimpleDateFormat("HH:mm");
                                Calendar calendar = Calendar.getInstance();

                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {//6.0
                                    calendar.set(Calendar.HOUR_OF_DAY, timePicker.getHour());
                                    calendar.set(Calendar.MINUTE, timePicker.getMinute());
                                } else {
                                    calendar.set(Calendar.HOUR_OF_DAY, timePicker.getCurrentHour());
                                    calendar.set(Calendar.MINUTE, timePicker.getCurrentMinute());
                                }
                                horaInicioET.setText(format.format(calendar.getTime()));
                                alertTimePicker.Dismiss();
                            }
                        };

                        alertTimePicker.ShowFilterView(timePicker, "Hora Inicio", onClickListener, false);
                    }
                });

                final EditText fechaFinET = fechaHoraView.findViewById(R.id.fechaFinET);
                fechaFinET.setText(localDateFormat.format(calendar.getTime()));
                fechaFinET.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final CalendarView calendarView = new CalendarView(BuscarExperienciaActivity.this);
                        calendarView.setDate(Calendar.getInstance().getTime().getTime());
                        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                            @Override
                            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                                EditText fechaFinET = fechaHoraView.findViewById(R.id.fechaFinET);
                                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(year, month, dayOfMonth, 0, 0);
                                fechaFinET.setText(format.format(calendar.getTime()));
                            }
                        });
                        final Alert alertCalendar = new Alert(BuscarExperienciaActivity.this);

                        View.OnClickListener onClickListener = new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alertCalendar.Dismiss();
                            }
                        };

                        alertCalendar.ShowFilterView(calendarView, "Fecha Fin", onClickListener, false);
                    }
                });

                Calendar calendarAux = calendar;
                calendarAux.add(Calendar.HOUR, 3);
                final EditText horaFinET = fechaHoraView.findViewById(R.id.horaFinET);
                horaFinET.setText(localTimeFormat.format(calendarAux.getTime()));
                horaFinET.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Alert alertTimePicker = new Alert(BuscarExperienciaActivity.this);
                        final TimePicker timePicker = new TimePicker(BuscarExperienciaActivity.this);
                        timePicker.setIs24HourView(true);
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {//6.0
                            timePicker.setHour(Calendar.getInstance().get(Calendar.HOUR));
                            timePicker.setMinute(Calendar.getInstance().get(Calendar.MINUTE));
                        } else {
                            timePicker.setCurrentHour(Calendar.getInstance().get(Calendar.HOUR));
                            timePicker.setCurrentMinute(Calendar.getInstance().get(Calendar.MINUTE));
                        }

                        View.OnClickListener onClickListener = new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                EditText horaFinET = fechaHoraView.findViewById(R.id.horaFinET);
                                SimpleDateFormat format = new SimpleDateFormat("HH:mm");
                                Calendar calendar = Calendar.getInstance();

                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {//6.0
                                    calendar.set(Calendar.HOUR_OF_DAY, timePicker.getHour());
                                    calendar.set(Calendar.MINUTE, timePicker.getMinute());
                                } else {
                                    calendar.set(Calendar.HOUR_OF_DAY, timePicker.getCurrentHour());
                                    calendar.set(Calendar.MINUTE, timePicker.getCurrentMinute());
                                }
                                horaFinET.setText(format.format(calendar.getTime()));
                                alertTimePicker.Dismiss();
                            }
                        };

                        alertTimePicker.ShowFilterView(timePicker, "Hora Fin", onClickListener, false);
                    }
                });

                View.OnClickListener aceptOnclick = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText fechaInicioET = fechaHoraView.findViewById(R.id.fechaInicioET);
                        EditText horaInicioET = fechaHoraView.findViewById(R.id.horaInicioET);
                        EditText fechaFinET = fechaHoraView.findViewById(R.id.fechaFinET);
                        EditText horaFinET = fechaHoraView.findViewById(R.id.horaFinET);

                        String fechaInicio = fechaInicioET.getText().toString();
                        String horaInicio = horaInicioET.getText().toString();
                        String fechaFin = fechaFinET.getText().toString();
                        String horaFin = horaFinET.getText().toString();

                        String fechaFiltroInicio = "";
                        String fechaFiltroFin = "";

                        try {
                            fechaFiltroInicio = mySqlDateTimeFormat.format(localDateTimeFormat.parse(fechaInicio + "" + horaInicio));
                            fechaFiltroFin = mySqlDateTimeFormat.format(localDateTimeFormat.parse(fechaFin + "" + horaFin));
                        } catch (ParseException e) {
                            ErrorTreatment.TreatExeption(e);
                        }

                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString(getString(R.string.key_fecha_hora_inicio_filtro), fechaFiltroInicio);
                        editor.putString(getString(R.string.key_fecha_hora_fin_filtro), fechaFiltroFin);
                        editor.apply();
                        RefreshPreferenceValues();

                        if(key_last_lat.equals("") || key_last_long.equals("") || key_distancia_filtro.equals("")){
                            ShowPedirUbicacionAlert(false);
                            fechaHoraFilterAlert.Dismiss();
                        }else {
                            fechaHoraFilterAlert.Loading();
                            Experiencia.Filter(key_intereses_filtro, fechaFiltroInicio, fechaFiltroFin, key_presupuesto_inicio_filtro, key_presupuesto_fin_filtro, key_last_lat, key_last_long, key_distancia_filtro).enqueue(WSRetrofit.ParseResponseWS(BuscarExperienciaActivity.this, FILTER_EXPERIENCIAS));
                        }
                    }
                };

                fechaHoraView.findViewById(R.id.limpiarFiltroBT).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fechaInicioET.setText("");
                        horaInicioET.setText("");
                        fechaFinET.setText("");
                        horaFinET.setText("");
                    }
                });

                fechaHoraFilterAlert.ShowFilterView(fechaHoraView, "Fecha y Hora", aceptOnclick, false);
            }
        });

        interesesIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interesesFilterAlert = new Alert(BuscarExperienciaActivity.this);

                LinearLayout ll_content = new LinearLayout(BuscarExperienciaActivity.this);
                ll_content.setOrientation(LinearLayout.VERTICAL);
                ll_content.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);

                LayoutInflater inflater = LayoutInflater.from(getBaseContext());
                interesesView = inflater.inflate(R.layout.filtro_intereses, ll_content, false);

                final LinearLayout ll_intereses = (LinearLayout) interesesView.findViewById(R.id.ll_intereses);

                if (!intereses.isEmpty()) {
                    for (Interes interes : intereses) {
                        Switch interesSw = new Switch(BuscarExperienciaActivity.this);
                        interesSw.setText(interes.getNombre()+" "+interes.getId());
                        interesSw.setLabelFor(interes.getId());
                        interesSw.setPadding(0, 0, 0, 5);
                        if(Interes.IsInCadena(interes.getId(),sharedPref.getString(getString(R.string.key_intereses_filtro),""))){
                            interesesFiltrados.add(interes.getId());
                            interesSw.setChecked(true);
                        }
                        interesSw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                if(isChecked){
                                    interesesFiltrados.add(buttonView.getLabelFor());
                                }else{
                                    interesesFiltrados.remove(new Integer(buttonView.getLabelFor()));
                                }
                            }
                        });
                        ll_intereses.addView(interesSw);
                    }
                }

                View.OnClickListener aceptOnclick = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String cadentaDeIntereses = "0";
                        if(interesesFiltrados != null)
                            for(Integer id_interes : interesesFiltrados)
                                cadentaDeIntereses += "," + id_interes.toString();

                        if(cadentaDeIntereses.equals("0"))
                            cadentaDeIntereses = "";

                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString(getString(R.string.key_intereses_filtro), cadentaDeIntereses);
                        editor.apply();
                        RefreshPreferenceValues();

                        if(key_last_lat.equals("") || key_last_long.equals("") || key_distancia_filtro.equals("")){
                            ShowPedirUbicacionAlert(false);
                            interesesFilterAlert.Dismiss();
                        }else {
                            interesesFilterAlert.Loading();
                            Experiencia.Filter(cadentaDeIntereses, "", "", "", "", key_last_lat, key_last_long, key_distancia_filtro).enqueue(WSRetrofit.ParseResponseWS(BuscarExperienciaActivity.this, FILTER_EXPERIENCIAS));
                        }
                    }
                };

                interesesView.findViewById(R.id.limpiarFiltroBT).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for(int i=0;i<ll_intereses.getChildCount();i++){
                            View view = ll_intereses.getChildAt(i);
                            if(view instanceof Switch){
                                ((Switch) view).setChecked(false);
                            }
                        }
                    }
                });

                interesesFilterAlert.ShowFilterView(interesesView, "Intereses", aceptOnclick, true);
            }
        });

        distanciaIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                distanciaFilterAlert = new Alert(BuscarExperienciaActivity.this);

                LinearLayout ll_content = new LinearLayout(BuscarExperienciaActivity.this);
                ll_content.setOrientation(LinearLayout.VERTICAL);
                ll_content.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);

                LayoutInflater inflater = LayoutInflater.from(getBaseContext());
                distanciaView = inflater.inflate(R.layout.filtro_distancia, ll_content, false);
                SetAutocompleteUbicacionFromFilter();

                CheckBox miUbicacionB = (CheckBox) distanciaView.findViewById(R.id.miUbicacionB);
                miUbicacionB.setChecked(flagMiPosicionFiltro);

                View.OnClickListener aceptOnclick = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText distanciaET = (EditText) distanciaView.findViewById(R.id.distanciaET);
                        CheckBox miUbicacionB = (CheckBox) distanciaView.findViewById(R.id.miUbicacionB);
                        AutoCompleteTextView autoCompleteUbicacionATV = (AutoCompleteTextView) distanciaView.findViewById(R.id.autoCompleteUbicacionATV);
                        String location = autoCompleteUbicacionATV.getText().toString();
                        if (miUbicacionB.isChecked()) {
                            if (!PermisionsUtils.canAccessLocation(BuscarExperienciaActivity.this)) {
                                PermisionsUtils.requestLocationPermissions(BuscarExperienciaActivity.this);
                            }

                            if (!simpleLocation.hasLocationEnabled()) {
                                SimpleLocation.openSettings(BuscarExperienciaActivity.this);
                            }

                            if (PermisionsUtils.canAccessLocation(BuscarExperienciaActivity.this) && simpleLocation.hasLocationEnabled()) {
                                final double latitude = simpleLocation.getLatitude();
                                final double longitude = simpleLocation.getLongitude();

                                RADIO_DISTANCIA = distanciaET.getText().toString();

                                distanciaFilterAlert.Loading();

                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.putString(getString(R.string.key_last_lat), Double.toString(latitude));
                                editor.putString(getString(R.string.key_last_long), Double.toString(longitude));
                                editor.putString(getString(R.string.key_distancia_filtro), RADIO_DISTANCIA);
                                editor.putBoolean(getString(R.string.key_mi_posicion_filtro_value), true);
                                editor.apply();
                                RefreshPreferenceValues();

                                Experiencia.Filter(sharedPref.getString(getString(R.string.key_intereses_filtro), ""), "", "", "", "", Double.toString(latitude), Double.toString(longitude), RADIO_DISTANCIA).enqueue(WSRetrofit.ParseResponseWS(BuscarExperienciaActivity.this, FILTER_EXPERIENCIAS));
                                mMap.setMyLocationEnabled(true);
                                GoogleMapsUtils.GoToLocationInMap(mMap,latitude,longitude,null);
                            }
                        }else if(location!=null && !location.equals("")){
                            new GeocoderTask(BuscarExperienciaActivity.this).execute(location);
                        }
                    }
                };

                distanciaFilterAlert.ShowFilterView(distanciaView,"Ubicación",aceptOnclick,false);
            }
        });
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        /*
        Experiencia experiencia = new Experiencia();
        experiencia.setNombre("Nombre de la experiencia");
        experiencia.setDescripcion("Descripcion de la experiencia");
        experiencia.setLatitud(-34d);
        experiencia.setLongitud(151d);
        */

        //Set Map
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        if(PermisionsUtils.canAccessLocation(this)) {
            mMap.setMyLocationEnabled(true);
        }

        //Obtener coordenadas de los toques en el mapa
        /*
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener(){
            public void onMapClick(LatLng latLng){
                Toast.makeText(BuscarExperienciaActivity.this.getApplicationContext(),latLng.latitude + "," + latLng.longitude,Toast.LENGTH_SHORT).show();
                Experiencia experiencia = new Experiencia();
                experiencia.setNombre("Experiencia " + latLng.latitude + "," + latLng.longitude);
                experiencia.setDescripcion("Descripcion de la experiencia " + latLng.latitude + "," + latLng.longitude);
                experiencia.setLatitud(latLng.latitude);
                experiencia.setLongitud(latLng.longitude);

                //Marker marker = GoogleMapsUtils.AddMarkerOptionsToMap(mMap,GoogleMapsUtils.GetMarkerOptionsFor(experiencia),R.drawable.ic_experience_point,true);
                //markerList.put(latLng.latitude + "," + latLng.longitude,marker);
            }
        });
        */

        /*
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                marker.remove();
                return true;
            }
        });
        */


        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Experiencia experiencia = (new Gson()).fromJson(marker.getSnippet(), Experiencia.class);
                Intent myIntent = new Intent(BuscarExperienciaActivity.this, ExperienciaDetailActivity.class);
                if(experiencia != null){
                    myIntent.putExtra("id",experiencia.getId()); //Optional parameters
                }
                BuscarExperienciaActivity.this.startActivity(myIntent);
            }
        });


        //Set Custom InfoWindow Adapter
        ExperienciaInfoWindowAdapter adapter = new ExperienciaInfoWindowAdapter(BuscarExperienciaActivity.this);
        mMap.setInfoWindowAdapter(adapter);


    }

    @Override
    protected void onResume() {
        super.onResume();

        if (PermisionsUtils.canAccessLocation(BuscarExperienciaActivity.this) && simpleLocation.hasLocationEnabled()) {
            // make the device update its location
            simpleLocation.beginUpdates();
        }
    }

    @Override
    protected void onPause() {
        if (PermisionsUtils.canAccessLocation(BuscarExperienciaActivity.this) && simpleLocation.hasLocationEnabled()) {
            // stop location updates (saves battery)
            simpleLocation.endUpdates();
        }

        super.onPause();
    }

    @Override
    public void ReciveResponseWS(ResponseWS responseWS,int accion) {

        switch(accion){
            case GET_EXPERIENCIAS:{
                if(responseWS != null && responseWS.getResult() != null && responseWS.getResult().size() > 0 && responseWS.getResult().get(0) instanceof Experiencia){
                    experiencias.clear();
                    experiencias = Experiencia.addResponseToList(experiencias,responseWS);
                }else{
                    experiencias = new ArrayList<>();
                }
                break;
            }

            case GET_INTERESES:{
                if(responseWS != null && responseWS.getResult() != null && responseWS.getResult().size() > 0 && responseWS.getResult().get(0) instanceof Interes){
                    intereses.clear();
                    intereses = Interes.addResponseToList(intereses,responseWS);
                }else{
                    intereses = new ArrayList<>();
                }
                break;
            }

            case FILTER_EXPERIENCIAS:{
                if(responseWS != null && responseWS.getResult() != null && responseWS.getResult().size() > 0 && responseWS.getResult().get(0) instanceof Experiencia){
                    experiencias.clear();
                    experiencias = Experiencia.addResponseToList(experiencias,responseWS);
                }else{
                    experiencias = new ArrayList<>();
                }

                SetListOfExperiencias(experiencias);
                AddRefreshMap(experiencias);
                if(distanciaFilterAlert != null && distanciaFilterAlert.IsActive()) {
                    distanciaFilterAlert.DismissLoading();
                    distanciaFilterAlert.Dismiss();
                }
                if(interesesFilterAlert != null && interesesFilterAlert.IsActive()) {
                    interesesFilterAlert.DismissLoading();
                    interesesFilterAlert.Dismiss();
                    GoogleMapsUtils.GoToLocationInMap(mMap,Double.parseDouble(key_last_lat),Double.parseDouble(key_last_long),FAR_ZOOM);
                }
                if(fechaHoraFilterAlert != null && fechaHoraFilterAlert.IsActive()) {
                    fechaHoraFilterAlert.DismissLoading();
                    fechaHoraFilterAlert.Dismiss();
                    GoogleMapsUtils.GoToLocationInMap(mMap,Double.parseDouble(key_last_lat),Double.parseDouble(key_last_long),FAR_ZOOM);
                }
                if(presupuestoFilterAlert != null && presupuestoFilterAlert.IsActive()) {
                    presupuestoFilterAlert.DismissLoading();
                    presupuestoFilterAlert.Dismiss();
                    GoogleMapsUtils.GoToLocationInMap(mMap,Double.parseDouble(key_last_lat),Double.parseDouble(key_last_long),FAR_ZOOM);
                }

                if(distanciaFilterAlert == null && interesesFilterAlert == null && fechaHoraFilterAlert == null && presupuestoFilterAlert == null && mMap != null){
                    GoogleMapsUtils.GoToLocationInMap(mMap,Double.parseDouble(key_last_lat),Double.parseDouble(key_last_long),FAR_ZOOM);
                }

                if(experiencias.isEmpty())
                    new Alert(BuscarExperienciaActivity.this).Show("No se encontraron experiencias con esa combinación de filtros.","Mala Suerte =(");

                break;
            }

            default:{
                Log.d(TAG,"ReciveResponseWS accion no identificada: " + accion);
            }
        }

    }

    @Override
    public void ReciveAdress(Address address) {
        if(distanciaFilterAlert.IsActive()) {
            distanciaFilterAlert.DismissLoading();
            distanciaFilterAlert.Dismiss();
        }
        double latitude = address.getLatitude();
        double longitude = address.getLongitude();

        sharedPref.edit().putString(getString(R.string.key_last_lat), Double.toString(latitude));
        sharedPref.edit().putString(getString(R.string.key_last_long), Double.toString(longitude));
        sharedPref.edit().putString(getString(R.string.key_distancia_filtro), RADIO_DISTANCIA);
        sharedPref.edit().apply();
        RefreshPreferenceValues();

        Experiencia.Filter("","","","","",Double.toString(latitude),Double.toString(longitude),RADIO_DISTANCIA).enqueue(WSRetrofit.ParseResponseWS(this,FILTER_EXPERIENCIAS));
        GoogleMapsUtils.GoToLocationInMap(mMap,latitude,longitude,null);
    }
}
