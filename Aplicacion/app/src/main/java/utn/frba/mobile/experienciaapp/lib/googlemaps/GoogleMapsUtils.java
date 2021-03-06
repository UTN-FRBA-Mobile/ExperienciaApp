package utn.frba.mobile.experienciaapp.lib.googlemaps;

import android.app.Activity;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import im.delight.android.location.SimpleLocation;
import com.google.gson.Gson;

import utn.frba.mobile.experienciaapp.R;
import utn.frba.mobile.experienciaapp.models.Experiencia;

public class GoogleMapsUtils {

    public static final float DEFAULT_ZOOM = 15f;
    public static final float FAR_ZOOM = 10f;

    public static SimpleLocation InitializeSimpleLocation(Activity activity){
        boolean requireFineGranularity = false;
        boolean passiveMode = false;
        long updateIntervalInMilliseconds = 1;
        //boolean requireNewLocation = false;
        SimpleLocation simpleLocation = new SimpleLocation(activity,requireFineGranularity,passiveMode,updateIntervalInMilliseconds);

        return simpleLocation;
    }

    public static void GoToLocationInMap(GoogleMap mMap, double lat, double lng, Float zoom){

        if(zoom == null){
            zoom = DEFAULT_ZOOM;
        }

        LatLng latLng1 = new LatLng(lat, lng);
        CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(latLng1, zoom);
        mMap.animateCamera(yourLocation);
    }

    public static MarkerOptions GetMarkerOptionsFor(CustomMarkerData customMarkerData){
        MarkerOptions markerOpt = new MarkerOptions();

        if(customMarkerData == null){
            return null;
        }

        LatLng latLng1 = new LatLng(customMarkerData.getLatitud(), customMarkerData.getLongitud());

        markerOpt.position(latLng1)
                .title(customMarkerData.getNombre())
                .snippet((new Gson()).toJson(customMarkerData));

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
