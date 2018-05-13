package utn.frba.mobile.experienciaapp.lib.permisions;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;

public class PermisionsUtils {
    private static final String[] INITIAL_PERMS={
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_CONTACTS
    };
    private static final String[] CAMERA_PERMS={
            Manifest.permission.CAMERA
    };
    private static final String[] CONTACTS_PERMS={
            Manifest.permission.READ_CONTACTS
    };
    private static final String[] LOCATION_PERMS={
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    private static final int INITIAL_REQUEST = 1337;
    private static final int CAMERA_REQUEST = INITIAL_REQUEST+1;
    private static final int CONTACTS_REQUEST = INITIAL_REQUEST+2;
    private static final int LOCATION_REQUEST = INITIAL_REQUEST+3;

    public static void requestCameraPermissions(Activity activity){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M){//6.0
            activity.requestPermissions(CAMERA_PERMS, CAMERA_REQUEST);
        }
    }

    public static void requestContactsPermissions(Activity activity){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M){//6.0
            activity.requestPermissions(CONTACTS_PERMS, CONTACTS_REQUEST);
        }
    }

    public static void requestLocationPermissions(Activity activity){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M){//6.0
            activity.requestPermissions(LOCATION_PERMS, LOCATION_REQUEST);
        }
    }

    public static boolean canAccessLocation(Activity act) {
        return(hasPermission(act,Manifest.permission.ACCESS_FINE_LOCATION));
    }

    public static boolean canAccessCamera(Activity act) {
        return(hasPermission(act,Manifest.permission.CAMERA));
    }

    public static boolean canAccessContacts(Activity act) {
        return(hasPermission(act,Manifest.permission.READ_CONTACTS));
    }

    public static boolean hasPermission(Activity act,String perm) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {//6.0
            return (PackageManager.PERMISSION_GRANTED == act.checkSelfPermission(perm));
        }

        return true;
    }
}
