package utn.frba.mobile.experienciaapp.lib.googlemaps;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import utn.frba.mobile.experienciaapp.R;
import utn.frba.mobile.experienciaapp.models.Experiencia;

public class GoogleMapsUtils {

    public static Marker AddMyLocationToMap(GoogleMap mMap,double lat, double lng,float zoom){
        MarkerOptions markerOpt = new MarkerOptions();

        LatLng latLng1 = new LatLng(lat, lng);

        markerOpt.position(latLng1);
                //.icon(BitmapDescriptorFactory.fromResource(R.drawable.experience_point));

        Marker marker = mMap.addMarker(markerOpt);
        marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

        CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(latLng1, zoom);
        mMap.animateCamera(yourLocation);

        return marker;
    }

    public static MarkerOptions GetMarkerOptionsFor(Experiencia experiencia){
        MarkerOptions markerOpt = new MarkerOptions();

        if(experiencia == null){
            return null;
        }

        LatLng latLng1 = new LatLng(experiencia.getLatitud(), experiencia.getLongitud());

        markerOpt.position(latLng1)
                .title(experiencia.getNombre())
                .snippet((new Gson()).toJson(experiencia));
                //.icon(BitmapDescriptorFactory.fromResource(R.drawable.experience_point));

        return markerOpt;
    }

    public static Marker AddMarkerOptionsToMap(GoogleMap mMap, MarkerOptions markerOpt, boolean showInfoWindow){
        Marker marker = mMap.addMarker(markerOpt);
        marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        if(showInfoWindow)
            marker.showInfoWindow();

        return marker;
    }
}
