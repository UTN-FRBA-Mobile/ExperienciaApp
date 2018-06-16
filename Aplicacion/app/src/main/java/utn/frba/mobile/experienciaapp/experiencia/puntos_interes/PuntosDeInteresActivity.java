package utn.frba.mobile.experienciaapp.experiencia.puntos_interes;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Marker;
import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import im.delight.android.location.SimpleLocation;
import utn.frba.mobile.experienciaapp.BaseActivityWithToolBar;
import utn.frba.mobile.experienciaapp.R;
import utn.frba.mobile.experienciaapp.experiencia.BuscarExperienciaActivity;
import utn.frba.mobile.experienciaapp.experiencia.ExperienciaDetailActivity;
import utn.frba.mobile.experienciaapp.experiencia.ExperienciaInfoWindowAdapter;
import utn.frba.mobile.experienciaapp.experiencia.ViewPagerAdapter;
import utn.frba.mobile.experienciaapp.lib.animations.slidinguppanel.SlidingUpPanelLayout;
import utn.frba.mobile.experienciaapp.lib.googlemaps.GoogleMapsUtils;
import utn.frba.mobile.experienciaapp.lib.permisions.PermisionsUtils;
import utn.frba.mobile.experienciaapp.lib.utils.Alert;
import utn.frba.mobile.experienciaapp.lib.ws.ReciveResponseWS;
import utn.frba.mobile.experienciaapp.lib.ws.ResponseWS;
import utn.frba.mobile.experienciaapp.lib.ws.WSRetrofit;
import utn.frba.mobile.experienciaapp.models.Experiencia;
import utn.frba.mobile.experienciaapp.models.PuntoInteres;

public class PuntosDeInteresActivity extends BaseActivityWithToolBar implements OnMapReadyCallback,ReciveResponseWS {

    private static final String TAG = "PuntosDeInteresAct";

    private static final int GET_PUNTOS_INTERES = 1;

    private SlidingUpPanelLayout mLayout;
    private GoogleMap mMap;
    private SimpleLocation simpleLocation;
    Alert loadingAlert = new Alert(this);

    public Map<Integer, Marker> markerList = new HashMap<>();
    private List<PuntoInteres> puntosDeInteres = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puntos_de_interes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        AddBackButtonToToolBar();
        SetMyLocationButton();
        simpleLocation = GoogleMapsUtils.InitializeSimpleLocation(this);

        Intent intent = getIntent();
        Integer id_experiencia = intent.getIntExtra("id", 0);

        PuntoInteres.GetPuntosDeInteresOf(id_experiencia).enqueue(WSRetrofit.ParseResponseWS(this,GET_PUNTOS_INTERES));

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        loadingAlert.Loading();
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        if(PermisionsUtils.canAccessLocation(this)) {
            mMap.setMyLocationEnabled(true);
        }

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                PuntoInteres puntoInteres = (new Gson()).fromJson(marker.getSnippet(), PuntoInteres.class);
                Alert puntoDetailAlert = new Alert(PuntosDeInteresActivity.this);

                LayoutInflater inflater = LayoutInflater.from(getBaseContext());
                View puntoInteresDetailView = inflater.inflate(R.layout.punto_interes_detail, null, false);

                TextView descripcionTV = puntoInteresDetailView.findViewById(R.id.descripcionTV);
                descripcionTV.setText(puntoInteres.getDescripcion());

                ViewPager viewPagerPhotoSlider = puntoInteresDetailView.findViewById(R.id.photoSlider);
                ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(PuntosDeInteresActivity.this, puntoInteres.getImagenes());
                viewPagerPhotoSlider.setAdapter(viewPagerAdapter);

                puntoDetailAlert.ShowView(puntoInteresDetailView,puntoInteres.getNombre(),"Cerrar");
            }
        });

        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                View view = PuntosDeInteresActivity.this.getLayoutInflater().inflate(R.layout.experiencia_custommapwindow, null);

                Gson gson = new Gson();
                final PuntoInteres puntoInteres = gson.fromJson(marker.getSnippet(), PuntoInteres.class);

                TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
                TextView tvSubTitle = (TextView) view.findViewById(R.id.tv_subtitle);
                LinearLayout calificaciones_ll = (LinearLayout) view.findViewById(R.id.calificaciones_ll);

                tvTitle.setText(puntoInteres.getNombre());
                tvSubTitle.setText(puntoInteres.getDescripcion());
                calificaciones_ll.setVisibility(View.GONE);

                return view;
            }

            @Override
            public View getInfoContents(Marker marker) {
                return null;
            }
        });

    }

    @Override
    public void ReciveResponseWS(ResponseWS responseWS, int accion) {
        switch(accion) {
            case GET_PUNTOS_INTERES: {
                if (responseWS != null && responseWS.getResult() != null && responseWS.getResult().size() > 0 && responseWS.getResult().get(0) instanceof PuntoInteres) {
                    puntosDeInteres.clear();
                    puntosDeInteres = PuntoInteres.addResponseToList(puntosDeInteres, responseWS);
                } else {
                    puntosDeInteres = new ArrayList<>();
                }

                SetListOfExperiencias(puntosDeInteres);
                AddRefreshMap(puntosDeInteres);

                if(loadingAlert.IsLoading()){
                    loadingAlert.DismissLoading();
                }

                break;
            }

            default:{
                Log.d(TAG,"ReciveResponseWS accion no identificada: " + accion);
            }
        }
    }

    @SuppressLint("MissingPermission")
    private void SetMyLocationButton() {
        ImageView myLocationButton = (ImageView) findViewById(R.id.myLocationButton);
        myLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (PermisionsUtils.canAccessLocation(PuntosDeInteresActivity.this)) {
                    if (!simpleLocation.hasLocationEnabled()) {
                        SimpleLocation.openSettings(PuntosDeInteresActivity.this);
                    } else {
                        final double latitude = simpleLocation.getLatitude();
                        final double longitude = simpleLocation.getLongitude();

                        if (mMap != null) {
                            mMap.setMyLocationEnabled(true);
                            GoogleMapsUtils.GoToLocationInMap(mMap, latitude, longitude, null);
                        }
                    }
                } else {
                    PermisionsUtils.requestLocationPermissions(PuntosDeInteresActivity.this);
                }
            }
        });
    }

    private void AddRefreshMap(List<PuntoInteres> puntosDeInteresList) {
        for (Map.Entry<Integer, Marker> markerEntry : markerList.entrySet()) {
            markerEntry.getValue().remove();
        }

        if (!puntosDeInteresList.isEmpty()) {
            for (PuntoInteres punto : puntosDeInteresList) {
                Marker marker = GoogleMapsUtils.AddMarkerOptionsToMap(
                        mMap,
                        GoogleMapsUtils.GetMarkerOptionsFor(punto),
                        R.drawable.ic_punto_interes,
                        false
                );
                markerList.put(punto.getId(), marker);
            }
        }
    }

    private void SetListOfExperiencias(List<PuntoInteres> listExperiencias) {
        TextView t = (TextView) findViewById(R.id.listadoPuntosTV);
        t.setText(Html.fromHtml(getString(R.string.listado_de_puntos)));

        ListView lv = (ListView) findViewById(R.id.list);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PuntoInteres experiencia = (PuntoInteres) ((ListView) parent).getAdapter().getItem(position);
                if (experiencia != null && experiencia.getLongitud() != null && experiencia.getLatitud() != null) {
                    GoogleMapsUtils.GoToLocationInMap(mMap, experiencia.getLatitud(), experiencia.getLongitud(), null);
                    mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                    markerList.get(experiencia.getId()).showInfoWindow();
                }
            }
        });

        PuntosDeInteresListAdapter experienciaListAdapterAdapter = new PuntosDeInteresListAdapter(this, listExperiencias);

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
}
