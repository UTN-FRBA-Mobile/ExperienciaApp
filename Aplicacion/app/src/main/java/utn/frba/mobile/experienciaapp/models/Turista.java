package utn.frba.mobile.experienciaapp.models;

import android.net.Uri;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import retrofit2.Call;
import utn.frba.mobile.experienciaapp.lib.ws.ResponseWS;
import utn.frba.mobile.experienciaapp.lib.ws.WSRetrofit;

public class Turista extends ModeloGenerico{
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("login_token")
    @Expose
    private String loginToken;
    @SerializedName("login_type")
    @Expose
    private String loginType;
    @SerializedName("firebase_token")
    @Expose
    private String firebaseToken;
    @SerializedName("last_longitud")
    @Expose
    private Double lastLongitud;
    @SerializedName("last_latitud")
    @Expose
    private Double lastLatitud;
    @SerializedName("last_geo_update")
    @Expose
    private String lastGeoUpdate;
    @SerializedName("last_request")
    @Expose
    private String lastRequest;

    private Uri imageUrl;

    public static Call<ResponseWS> SignIn(Turista turista){
        //TODO: Validacion de turista seteado
        String l_long = "";
        String l_lat = "";

        if(turista.getLastLongitud() != null)
            l_long = turista.getLastLongitud().toString();
        if(turista.getLastLatitud() != null)
            l_lat = turista.getLastLatitud().toString();

        return WSRetrofit.getInstance().signInTurista(
                WSRetrofit.APARTADO,
                WSRetrofit.KEY,
                WSRetrofit.SIGN_IN_TURISTA,
                turista.getEmail(),
                turista.firebaseToken,
                l_long,
                l_lat
        );


    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLoginToken() {
        return loginToken;
    }

    public void setLoginToken(String loginToken) {
        this.loginToken = loginToken;
    }

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }

    public String getFirebaseToken() {
        return firebaseToken;
    }

    public void setFirebaseToken(String firebaseToken) {
        this.firebaseToken = firebaseToken;
    }

    public Double getLastLongitud() {
        return lastLongitud;
    }

    public void setLastLongitud(Double lastLongitud) {
        this.lastLongitud = lastLongitud;
    }

    public Double getLastLatitud() {
        return lastLatitud;
    }

    public void setLastLatitud(Double lastLatitud) {
        this.lastLatitud = lastLatitud;
    }

    public String getLastGeoUpdate() {
        return lastGeoUpdate;
    }

    public void setLastGeoUpdate(String lastGeoUpdate) {
        this.lastGeoUpdate = lastGeoUpdate;
    }

    public String getLastRequest() {
        return lastRequest;
    }

    public void setLastRequest(String lastRequest) {
        this.lastRequest = lastRequest;
    }

    public Uri getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(Uri imageUrl) {
        this.imageUrl = imageUrl;
    }
}
