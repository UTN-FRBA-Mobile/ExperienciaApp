package utn.frba.mobile.experienciaapp.experiencia;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import im.delight.android.location.SimpleLocation;
import utn.frba.mobile.experienciaapp.R;
import utn.frba.mobile.experienciaapp.experiencia.filtros.FiltrosBehaviour;
import utn.frba.mobile.experienciaapp.lib.googlemaps.GoogleMapsUtils;
import utn.frba.mobile.experienciaapp.lib.animations.slidinguppanel.SlidingUpPanelLayout;
import utn.frba.mobile.experienciaapp.lib.permisions.PermisionsUtils;
import utn.frba.mobile.experienciaapp.lib.utils.Alert;
import utn.frba.mobile.experienciaapp.models.Experiencia;

public class BuscarExperienciaActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = "BuscarExperienciaAct";
    private static final float DEFAULT_ZOOM = 17f;

    private SlidingUpPanelLayout mLayout;
    private GoogleMap mMap;
    private SimpleLocation simpleLocation;
    //Filtros
    private ImageView favoritosIB,interesesIB,fechaHoraIB,presupuestoIB,distanciaIB;



    private static final String[] LOCATION_PERMS={
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    private void inicializarFiltros(){
        favoritosIB=findViewById(R.id.favoritosIB);
        interesesIB=findViewById(R.id.interesesIB);
        fechaHoraIB=findViewById(R.id.fechaHoraIB);
        presupuestoIB=findViewById(R.id.presupuestoIB);
        distanciaIB=findViewById(R.id.distanciaIB);
        FiltrosBehaviour filtrosBehaviour=new FiltrosBehaviour();
        filtrosBehaviour.setBehaviour(favoritosIB,interesesIB,fechaHoraIB,presupuestoIB,distanciaIB);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inicializarFiltros();
        setContentView(R.layout.activity_buscar_experiencia);
        setSupportActionBar((Toolbar) findViewById(R.id.main_toolbar));

        simpleLocation = new SimpleLocation(this);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

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
                            GoogleMapsUtils.AddMyLocationToMap(mMap,latitude,longitude,DEFAULT_ZOOM);
                        }
                    }
                }else{
                    PermisionsUtils.requestLocationPermissions(BuscarExperienciaActivity.this);
                }
            }
        });

        TextView t = (TextView) findViewById(R.id.listadoExperienciasTV);
        t.setText(Html.fromHtml(getString(R.string.listado_de_experiencias)));


        ListView lv = (ListView) findViewById(R.id.list);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(BuscarExperienciaActivity.this, "onItemClick", Toast.LENGTH_SHORT).show();
            }
        });

        List<String> your_array_list = Arrays.asList(
                "This",
                "Is",
                "An",
                "Example",
                "ListView",
                "That",
                "You",
                "Can",
                "Scroll",
                ".",
                "It",
                "Shows",
                "How",
                "Any",
                "Scrollable",
                "View",
                "Can",
                "Be",
                "Included",
                "As",
                "A",
                "Child",
                "Of",
                "SlidingUpPanelLayout"
        );

        // This is the array adapter, it takes the context of the activity as a
        // first parameter, the type of list view as a second parameter and your
        // array as a third parameter.
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                your_array_list );

        lv.setAdapter(arrayAdapter);

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

        ImageView interesesIB = (ImageView)findViewById(R.id.interesesIB);
        interesesIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Alert alert = new Alert(BuscarExperienciaActivity.this);
                alert.Show("Descripcion","Titulo");
            }
        });
    }

    public Map<String,Marker> markerList = new HashMap<>();

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Experiencia experiencia = new Experiencia();
        experiencia.setNombre("Nombre de la experiencia");
        experiencia.setDescripcion("Descripcion de la experiencia");
        experiencia.setLatitud(-34d);
        experiencia.setLongitud(151d);

        //Set Map
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled(true);

        //Obtener coordenadas de los toques en el mapa
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener(){
            public void onMapClick(LatLng latLng){
                Toast.makeText(BuscarExperienciaActivity.this.getApplicationContext(),latLng.latitude + "," + latLng.longitude,Toast.LENGTH_SHORT).show();
                Experiencia experiencia = new Experiencia();
                experiencia.setNombre("Experiencia " + latLng.latitude + "," + latLng.longitude);
                experiencia.setDescripcion("Descripcion de la experiencia " + latLng.latitude + "," + latLng.longitude);
                experiencia.setLatitud(latLng.latitude);
                experiencia.setLongitud(latLng.longitude);

                Marker marker = GoogleMapsUtils.AddMarkerOptionsToMap(mMap,GoogleMapsUtils.GetMarkerOptionsFor(experiencia),true);
                markerList.put(latLng.latitude + "," + latLng.longitude,marker);
            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                marker.remove();
                return true;
            }
        });

       /* mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                marker.hideInfoWindow();
            }
        });*/

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Intent myIntent = new Intent(BuscarExperienciaActivity.this, ExperienciaDetailActivity.class);
                myIntent.putExtra("id",123); //Optional parameters
                BuscarExperienciaActivity.this.startActivity(myIntent);
            }
        });

        //Set Custom InfoWindow Adapter
        ExperienciaInfoWindowAdapter adapter = new ExperienciaInfoWindowAdapter(BuscarExperienciaActivity.this);
        mMap.setInfoWindowAdapter(adapter);

        Marker marker = GoogleMapsUtils.AddMarkerOptionsToMap(mMap,GoogleMapsUtils.GetMarkerOptionsFor(experiencia),true);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), DEFAULT_ZOOM));
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

}
