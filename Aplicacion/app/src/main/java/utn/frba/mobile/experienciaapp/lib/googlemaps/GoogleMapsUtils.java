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

    public static void GoToLocationInMap(GoogleMap mMap, double lat, double lng, float zoom){
        LatLng latLng1 = new LatLng(lat, lng);
        CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(latLng1, zoom);
        mMap.animateCamera(yourLocation);
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
        //.icon(BitmapDescriptorFactory.fromResource(drawableResource));

        return markerOpt;
    }

    public static MarkerOptions GetMarkerOptionsFor(double lat, double lng){
        MarkerOptions markerOpt = new MarkerOptions();
        LatLng latLng1 = new LatLng(lat, lng);
        markerOpt.position(latLng1);
        return markerOpt;
    }

    public static Marker AddMarkerOptionsToMap(GoogleMap mMap, MarkerOptions markerOpt,int drawableResource, boolean showInfoWindow){
        Marker marker = mMap.addMarker(markerOpt);
        if(drawableResource == 0) {
            marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        }else {
            marker.setIcon(BitmapDescriptorFactory.fromResource(drawableResource));
        }
        if(showInfoWindow)
            marker.showInfoWindow();

        return marker;
    }
}
