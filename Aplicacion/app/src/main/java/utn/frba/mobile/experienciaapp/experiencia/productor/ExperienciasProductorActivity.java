package utn.frba.mobile.experienciaapp.experiencia.productor;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Marker;
import com.google.gson.Gson;

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
import utn.frba.mobile.experienciaapp.experiencia.ExperienciaListAdapter;
import utn.frba.mobile.experienciaapp.lib.animations.slidinguppanel.SlidingUpPanelLayout;
import utn.frba.mobile.experienciaapp.lib.googlemaps.GoogleMapsUtils;
import utn.frba.mobile.experienciaapp.lib.permisions.PermisionsUtils;
import utn.frba.mobile.experienciaapp.lib.utils.Alert;
import utn.frba.mobile.experienciaapp.lib.ws.ReciveResponseWS;
import utn.frba.mobile.experienciaapp.lib.ws.ResponseWS;
import utn.frba.mobile.experienciaapp.lib.ws.WSRetrofit;
import utn.frba.mobile.experienciaapp.models.Experiencia;
import utn.frba.mobile.experienciaapp.models.Productor;
import utn.frba.mobile.experienciaapp.models.PuntoInteres;

public class ExperienciasProductorActivity extends BaseActivityWithToolBar implements ReciveResponseWS,OnMapReadyCallback {

    private static final String TAG = "ExperienciasProdAct";
    private static final int GET_EXPERIENCIAS_OF_PRODUCTOR = 1;

    private SlidingUpPanelLayout mLayout;
    private GoogleMap mMap;
    private SimpleLocation simpleLocation;
    Alert loadingAlert = new Alert(this);

    Integer productor_id = 0;

    private List<Experiencia> experiencias = new ArrayList<>();
    public Map<Integer, Marker> markerList = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_experiencias_productor);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        AddBackButtonToToolBar();

        simpleLocation = GoogleMapsUtils.InitializeSimpleLocation(this);

        Intent intent = getIntent();
        productor_id = intent.getIntExtra("id", 0);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        SetMyLocationButton();

        loadingAlert.Loading();
    }

    @Override
    public void ReciveResponseWS(ResponseWS responseWS, int accion) {
        switch(accion) {
            case GET_EXPERIENCIAS_OF_PRODUCTOR: {
                if (responseWS != null && responseWS.getResult() != null && responseWS.getResult().size() > 0 && responseWS.getResult().get(0) instanceof Experiencia) {
                    experiencias.clear();
                    experiencias = Experiencia.addResponseToList(experiencias, responseWS);
                } else {
                    experiencias = new ArrayList<>();
                }

                if(mMap != null)
                {
                    SetListOfExperiencias(experiencias);
                    AddRefreshMap(experiencias);
                }

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
                Experiencia experiencia = (new Gson()).fromJson(marker.getSnippet(), Experiencia.class);
                Intent myIntent = new Intent(ExperienciasProductorActivity.this, ExperienciaDetailActivity.class);
                if(experiencia != null){
                    myIntent.putExtra("id",experiencia.getId()); //Optional parameters
                }
                ExperienciasProductorActivity.this.startActivity(myIntent);
            }
        });


        //Set Custom InfoWindow Adapter
        ExperienciaInfoWindowAdapter adapter = new ExperienciaInfoWindowAdapter(ExperienciasProductorActivity.this);
        mMap.setInfoWindowAdapter(adapter);

        Experiencia.GetAllOfProductor(productor_id).enqueue(WSRetrofit.ParseResponseWS(this,GET_EXPERIENCIAS_OF_PRODUCTOR));
    }

    @SuppressLint("MissingPermission")
    private void SetMyLocationButton() {
        ImageView myLocationButton = (ImageView) findViewById(R.id.myLocationButton);
        myLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (PermisionsUtils.canAccessLocation(ExperienciasProductorActivity.this)) {
                    if (!simpleLocation.hasLocationEnabled()) {
                        SimpleLocation.openSettings(ExperienciasProductorActivity.this);
                    } else {
                        final double latitude = simpleLocation.getLatitude();
                        final double longitude = simpleLocation.getLongitude();

                        if (mMap != null) {
                            mMap.setMyLocationEnabled(true);
                            GoogleMapsUtils.GoToLocationInMap(mMap, latitude, longitude, null);
                        }
                    }
                } else {
                    PermisionsUtils.requestLocationPermissions(ExperienciasProductorActivity.this);
                }
            }
        });
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

    private void SetListOfExperiencias(List<Experiencia> listExperiencias) {
        TextView t = (TextView) findViewById(R.id.listadoPuntosTV);
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
}
