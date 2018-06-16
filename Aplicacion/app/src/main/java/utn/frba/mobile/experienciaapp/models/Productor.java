package utn.frba.mobile.experienciaapp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import retrofit2.Call;
import utn.frba.mobile.experienciaapp.lib.ws.ResponseWS;
import utn.frba.mobile.experienciaapp.lib.ws.WSRetrofit;

public class Productor {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("nombre")
    @Expose
    private String nombre;
    @SerializedName("apellido")
    @Expose
    private String apellido;
    @SerializedName("dni")
    @Expose
    private String dni;
    @SerializedName("direccion")
    @Expose
    private String direccion;
    @SerializedName("telefono")
    @Expose
    private String telefono;
    @SerializedName("celular")
    @Expose
    private String celular;
    @SerializedName("descripcion")
    @Expose
    private String descripcion;
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
    @SerializedName("habilitado")
    @Expose
    private String habilitado;

    //Metodos para WS
    public static Call<ResponseWS> GetPerfil(int id){
        return WSRetrofit.getInstance().getInformationOf(
                WSRetrofit.APARTADO,
                WSRetrofit.KEY,
                WSRetrofit.GET_DETALLE_EXPERIENCIA,
                Integer.toString(id)
        );
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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

    public String getHabilitado() {
        return habilitado;
    }

    public void setHabilitado(String habilitado) {
        this.habilitado = habilitado;
    }

}