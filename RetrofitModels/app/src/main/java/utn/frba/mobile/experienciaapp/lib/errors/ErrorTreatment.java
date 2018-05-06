package utn.frba.mobile.experienciaapp.lib.errors;

import com.crashlytics.android.Crashlytics;

public class ErrorTreatment {
    public static void TreatExeption(Exception e)
    {
        Crashlytics.logException(e);
        e.printStackTrace();
    }
}
