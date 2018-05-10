package utn.frba.mobile.experienciaapp.experiencia;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import utn.frba.mobile.experienciaapp.R;
import utn.frba.mobile.experienciaapp.models.Experiencia;

public class ExperienciaActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_experiencia);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        //https://gist.github.com/ccjeng/ff8ca25e0e92302639dadbe4a8533279
        //Only for test
        Experiencia experiencia = new Experiencia();
        experiencia.setNombre("Nombre de la experiencia");
        experiencia.setDescripcion("Descripcion de la experiencia");
        String jsonExperiencia = (new Gson()).toJson(experiencia);

        mMap = googleMap;

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled(true);



        // Add a marker in Sydney and move the camera
        LatLng latLng1 = new LatLng(-34, 151);
        //Marker marker1 = mMap.addMarker(new MarkerOptions().position(latLng1).title("Marker in Sydney Title").snippet("Population: 4,137,400"));
        //marker1.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));

        //Marker
        MarkerOptions markerOpt = new MarkerOptions();
        markerOpt.position(latLng1)
                .title("Titulo marcador")
                .snippet(jsonExperiencia);
                //.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher_foreground));

        //Set Custom InfoWindow Adapter
        CustomInfoWindowAdapter adapter = new CustomInfoWindowAdapter(ExperienciaActivity.this);
        mMap.setInfoWindowAdapter(adapter);

        LatLng latLng2 = new LatLng(-34.001, 151.001);
        Marker marker2 = mMap.addMarker(new MarkerOptions().position(latLng2).title("Marker in 2 Title"));
        marker2.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));

        Marker marker1 = mMap.addMarker(markerOpt);
        marker1.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
        marker1.showInfoWindow();

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng1, 17f));
        //Map.moveCamera(CameraUpdateFactory.newLatLng(sydney));//Agregar sin zoom

        //Obtener coordenadas de los toques en el mapa
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener(){
            public void onMapClick(LatLng point){
                Toast.makeText(ExperienciaActivity.this.getApplicationContext(),
                        point.latitude + ", " + point.longitude,
                        Toast.LENGTH_SHORT).show();
            }
        });

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {

                Intent myIntent = new Intent(ExperienciaActivity.this, ExperienciaDetailActivity.class);
                myIntent.putExtra("title", marker.getTitle()); //Optional parameters
                ExperienciaActivity.this.startActivity(myIntent);


                /*Toast.makeText(ExperienciaActivity.this.getApplicationContext(),
                        "Marcador elegido:"+marker.getTitle(),
                        Toast.LENGTH_SHORT).show();
                */
            }
        });
    }
}
