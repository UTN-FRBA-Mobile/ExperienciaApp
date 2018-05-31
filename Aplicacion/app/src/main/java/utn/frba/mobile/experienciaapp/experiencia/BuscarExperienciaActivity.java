package utn.frba.mobile.experienciaapp.experiencia;

import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBufferResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import im.delight.android.location.SimpleLocation;
import utn.frba.mobile.experienciaapp.BaseActivityWithToolBar;
import utn.frba.mobile.experienciaapp.R;
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
import utn.frba.mobile.experienciaapp.models.Productor;

public class BuscarExperienciaActivity extends BaseActivityWithToolBar implements OnMapReadyCallback,ReciveResponseWS,ReciveAdress {

    //TODO: PAsar a preference store
    public static String RADIO_DISTANCIA = "10";
    public static LatLng INIT_LOCATION;

    private static final int GET_EXPERIENCIAS = 1;
    private static final int FILTER_EXPERIENCIAS = 2;

    private static final String TAG = "BuscarExperienciaAct";

    private SlidingUpPanelLayout mLayout;
    private GoogleMap mMap;
    private SimpleLocation simpleLocation;
    private List<Experiencia> experiencias = new ArrayList<>();

    public Alert distanciaFilterAlert;
    public View distanciaView;

    public Marker myLocation;

    //Filtros
    private ImageView favoritosIB,interesesIB,fechaHoraIB,presupuestoIB,distanciaIB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //experiencias = MockExperiences();//TODO: Borrar, solo para testing

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_experiencia);
        setSupportActionBar((Toolbar) findViewById(R.id.main_toolbar));

        AddBackButtonToToolBar();
        simpleLocation = GoogleMapsUtils.InitializeSimpleLocation(this);
        inicializarFiltros();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        SetMyLocationButton();

        SetListOfExperiencias(experiencias);

        if(PermisionsUtils.canAccessLocation(BuscarExperienciaActivity.this) && simpleLocation.hasLocationEnabled()){
            final double latitude = simpleLocation.getLatitude();
            final double longitude = simpleLocation.getLongitude();

            INIT_LOCATION = new LatLng(latitude,longitude);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        final Alert alertInicio = new Alert(this);
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertInicio.Dismiss();
                distanciaIB.performClick();
            }
        };

        alertInicio.ShowConfirmation("Indicanos donde te gustaria encontrar nuevas experiencias.","Nuevas Experiencias",onClickListener,false);
    }

    public Map<Integer,Marker> markerList = new HashMap<>();

    private void SetListOfExperiencias(List<Experiencia> listExperiencias) {
        TextView t = (TextView) findViewById(R.id.listadoExperienciasTV);
        t.setText(Html.fromHtml(getString(R.string.listado_de_experiencias)));

        ListView lv = (ListView) findViewById(R.id.list);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Experiencia experiencia = (Experiencia) ((ListView) parent).getAdapter().getItem(position);
                if(experiencia != null && experiencia.getLongitud() != null && experiencia.getLatitud() != null){
                    GoogleMapsUtils.GoToLocationInMap(mMap,experiencia.getLatitud(),experiencia.getLongitud() ,null);
                    mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                }
                Toast.makeText(BuscarExperienciaActivity.this, "onItemClick", Toast.LENGTH_SHORT).show();
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

    private void SetAutocompleteUbicacionFromFilter(){
        // Construct a GeoDataClient for the Google Places API for Android.
        GeoDataClient mGeoDataClient = Places.getGeoDataClient(this, null);

        // Retrieve the AutoCompleteTextView that will display Place suggestions.
        AutoCompleteTextView mAutocompleteView = (AutoCompleteTextView)distanciaView.findViewById(R.id.autoCompleteUbicacionATV);

        LatLngBounds latLngBounds = null;
        if(INIT_LOCATION != null){
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

    private void AddRefreshMap(List<Experiencia> experienciaList){
        if(!experienciaList.isEmpty()){
            for(Experiencia experiencia : experienciaList){
                Marker marker = GoogleMapsUtils.AddMarkerOptionsToMap(
                        mMap,
                        GoogleMapsUtils.GetMarkerOptionsFor(experiencia),
                        R.drawable.ic_experience_point,
                        false
                );
                markerList.put(experiencia.getId(),marker);
            }

            //GoogleMapsUtils.GoToLocationInMap(mMap,experiencias.get(0).getLatitud(),experiencias.get(0).getLongitud(),null);
        }
    }

    private void SetMyLocationButton(){
        ImageView myLocationButton = (ImageView) findViewById(R.id.myLocationButton);
        myLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(PermisionsUtils.canAccessLocation(BuscarExperienciaActivity.this)){
                    if (!simpleLocation.hasLocationEnabled()) {
                        SimpleLocation.openSettings(BuscarExperienciaActivity.this);
                    }else{
                        final double latitude = simpleLocation.getLatitude();
                        final double longitude = simpleLocation.getLongitude();

                        if(mMap != null){
                            simpleLocation.setListener(new SimpleLocation.Listener() {

                                public void onPositionChanged() {
                                    final double latitude = simpleLocation.getLatitude();
                                    final double longitude = simpleLocation.getLongitude();
                                    Toast.makeText(BuscarExperienciaActivity.this, latitude+","+longitude, Toast.LENGTH_SHORT).show();
                                    myLocation.remove();
                                    myLocation = GoogleMapsUtils.AddMarkerOptionsToMap(mMap,GoogleMapsUtils.GetMarkerOptionsFor(latitude,longitude),R.drawable.ic_user_point,false);
                                    GoogleMapsUtils.GoToLocationInMap(mMap,latitude,longitude,null);
                                }

                            });
                            if(myLocation != null)
                                myLocation.remove();

                            myLocation = GoogleMapsUtils.AddMarkerOptionsToMap(mMap,GoogleMapsUtils.GetMarkerOptionsFor(latitude,longitude),R.drawable.ic_user_point,false);
                            GoogleMapsUtils.GoToLocationInMap(mMap,latitude,longitude,null);
                        }
                    }
                }else{
                    PermisionsUtils.requestLocationPermissions(BuscarExperienciaActivity.this);
                }
            }
        });
    }

    private void inicializarFiltros(){
        favoritosIB=(ImageView)findViewById(R.id.favoritosIB);
        interesesIB=(ImageView)findViewById(R.id.interesesIB);
        fechaHoraIB=(ImageView)findViewById(R.id.fechaHoraIB);
        presupuestoIB=(ImageView)findViewById(R.id.presupuestoIB);
        distanciaIB=(ImageView)findViewById(R.id.distanciaIB);

        interesesIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Alert alert = new Alert(BuscarExperienciaActivity.this);
                alert.Show("Descripcion","Titulo");
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
                distanciaView = inflater.inflate(R.layout.filtro_distancia, ll_content,false);
                SetAutocompleteUbicacionFromFilter();

                Button miUbicacionB = (Button) distanciaView.findViewById(R.id.miUbicacionB);
                miUbicacionB.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //View vParent = (View) v.getParent();

                        if(!PermisionsUtils.canAccessLocation(BuscarExperienciaActivity.this)){
                            PermisionsUtils.requestLocationPermissions(BuscarExperienciaActivity.this);
                        }

                        if(!simpleLocation.hasLocationEnabled()){
                            SimpleLocation.openSettings(BuscarExperienciaActivity.this);
                        }

                        if(PermisionsUtils.canAccessLocation(BuscarExperienciaActivity.this) && simpleLocation.hasLocationEnabled()) {
                            final double latitude = simpleLocation.getLatitude();
                            final double longitude = simpleLocation.getLongitude();

                            EditText distanciaET = (EditText) distanciaView.findViewById(R.id.distanciaET);
                            RADIO_DISTANCIA = distanciaET.getText().toString();

                            distanciaFilterAlert.Loading();
                            Experiencia.Filter("","","","","",Double.toString(latitude),Double.toString(longitude),RADIO_DISTANCIA).enqueue(WSRetrofit.ParseResponseWS(BuscarExperienciaActivity.this,FILTER_EXPERIENCIAS));
                            GoogleMapsUtils.GoToLocationInMap(mMap,latitude,longitude,null);
                        }
                    }
                });


                ImageButton buscarUbicacionIB = (ImageButton) distanciaView.findViewById(R.id.buscarUbicacionIB);
                buscarUbicacionIB.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //View vParent = (View) v.getParent();

                        EditText distanciaET = (EditText) distanciaView.findViewById(R.id.distanciaET);
                        RADIO_DISTANCIA = distanciaET.getText().toString();

                        AutoCompleteTextView autoCompleteUbicacionATV = (AutoCompleteTextView) distanciaView.findViewById(R.id.autoCompleteUbicacionATV);
                        String location = autoCompleteUbicacionATV.getText().toString();
                        if(location!=null && !location.equals("")){
                            new GeocoderTask(BuscarExperienciaActivity.this).execute(location);
                        }
                    }
                });

                distanciaFilterAlert.ShowView(distanciaView,"Ubicación","Volver");
            }
        });
    }

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
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

    }

    //TODO: BOrrar
    private List<Experiencia> MockExperiences(){
        List<Experiencia> experinecias = new ArrayList<>();

        for(int i=0;i<10;i++) {
            double v = i / 1000d;
            Experiencia exp = new Experiencia();
            exp.setNombre("Pirámides de egipto");
            exp.setDescripcion("Las pirámides de Egipto son, de todos los vestigios legados por egipcios de la antigüedad, los más portentosos y emblemáticos monumentos de esta civilización, y en particular, las tres grandespirámides de Giza, las tumbas o cenotafios de los faraones Keops, Kefrén y Micerino, cuya construcción se remonta, para la gran mayoría de estudiosos, al periodo denominado Imperio Antiguo de Egipto. La Gran Pirámide de Giza, construida por Keops (Jufu), es una de lasSiete Maravillas del Mundo Antiguo, además de ser la única que aún perdura. Su visita guiada comienza con una fascinante introducción de cada una de las tres pirámides de Gizeh: la de Keops, la de Kefrén y la de Micerinos. Dispondrá de tiempo libre para entrar a una de las pirámides (coste adicional), aunque a su guía no le estará permitido entrar con usted." +
                    "Un corto trayecto por carretera hacia el lado de la meseta más cercano a la ciudad le lleva a los pies de la esfinge, el enigmático símbolo de Egipto. También en Gizeh puede visitar el Museo de la Barca Solar (opcional), que alberga la magníficamente bien conservada barca funeraria de Keops.");
            exp.setFechaCreacion("09/05/2018");
            exp.setPrecio("1200");
            exp.setDireccion("Av Siempreviva 4530, El Cairo");
            exp.setDuracion("2");
            exp.setLatitud(-34 + v);
            exp.setLongitud(151 + v);

            ArrayList imagenes = new ArrayList();
            imagenes.add("https://sobrehistoria.com/wp-content/uploads/2016/03/las-piramides-de-egipto-portada-600x429.jpg");
            imagenes.add("https://sobrehistoria.com/wp-content/uploads/2016/03/las-piramides-de-egipto-giza-600x337.jpg");
            imagenes.add("https://sobrehistoria.com/wp-content/uploads/2016/03/las-piramides-de-egipto-pesado-del-corazon-600x350.jpg");
            imagenes.add("https://sobrehistoria.com/wp-content/uploads/2016/03/las-piramides-de-egipto-piramides.jpg");
            imagenes.add("https://sobrehistoria.com/wp-content/uploads/2016/03/las-piramides-de-egipto-ciudad-constructores-600x450.jpg");

            exp.setImagenes(imagenes);

            Productor productor = new Productor();
            productor.setId(100);
            productor.setNombre("Roberto");
            productor.setApellido("Gómez Bolaños");

            exp.setProductor(productor);

            experinecias.add(exp);
        }

        return experinecias;
    }

    @Override
    public void ReciveResponseWS(ResponseWS responseWS,int accion) {

        switch(accion){
            case GET_EXPERIENCIAS:{
                if(responseWS.getResult() != null && responseWS.getResult().size() > 0 && responseWS.getResult().get(0) instanceof Experiencia){
                    experiencias.clear();
                    experiencias = Experiencia.addResponseToList(experiencias,responseWS);
                }else{
                    experiencias = new ArrayList<>();
                }
                break;
            }

            case FILTER_EXPERIENCIAS:{
                if(responseWS.getResult() != null && responseWS.getResult().size() > 0 && responseWS.getResult().get(0) instanceof Experiencia){
                    experiencias.clear();
                    experiencias = Experiencia.addResponseToList(experiencias,responseWS);
                }else{
                    experiencias = new ArrayList<>();
                }

                SetListOfExperiencias(experiencias);
                AddRefreshMap(experiencias);
                if(distanciaFilterAlert.IsActive()) {
                    distanciaFilterAlert.DismissLoading();
                    distanciaFilterAlert.Dismiss();
                }
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
        Experiencia.Filter("","","","","",Double.toString(latitude),Double.toString(longitude),RADIO_DISTANCIA).enqueue(WSRetrofit.ParseResponseWS(this,FILTER_EXPERIENCIAS));
        GoogleMapsUtils.GoToLocationInMap(mMap,latitude,longitude,null);
    }
}
