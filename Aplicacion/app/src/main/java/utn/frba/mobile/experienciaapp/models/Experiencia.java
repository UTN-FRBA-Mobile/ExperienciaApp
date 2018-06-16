package utn.frba.mobile.experienciaapp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import utn.frba.mobile.experienciaapp.lib.googlemaps.CustomMarkerData;
import utn.frba.mobile.experienciaapp.lib.ws.ResponseWS;
import utn.frba.mobile.experienciaapp.lib.ws.WSRetrofit;

public class Experiencia extends ModeloGenerico implements CustomMarkerData {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("nombre")
    @Expose
    private String nombre;
    @SerializedName("descripcion")
    @Expose
    private String descripcion;
    @SerializedName("longitud")
    @Expose
    private Double longitud;
    @SerializedName("latitud")
    @Expose
    private Double latitud;
    @SerializedName("precio")
    @Expose
    private String precio;
    @SerializedName("fecha_creacion")
    @Expose
    private String fechaCreacion;
    @SerializedName("visible")
    @Expose
    private String visible;
    @SerializedName("productor")
    @Expose
    private Productor productor;
    @SerializedName("duracion")
    @Expose
    private String duracion;
    @SerializedName("direccion")
    @Expose
    private String direccion;
    @SerializedName("imagenes")
    @Expose
    private ArrayList<String> imagenes;
    @SerializedName("calificacion")
    @Expose
    private Integer calificacion;

    //Metodos para WS

    public static Call<ResponseWS> GetDetalle(int id){
        return WSRetrofit.getInstance().getInformationOf(
                WSRetrofit.APARTADO,
                WSRetrofit.KEY,
                WSRetrofit.GET_DETALLE_EXPERIENCIA,
                Integer.toString(id)
        );
    }

    public static Call<ResponseWS> Filter(String intereses,
                                          String fecha_inicio,
                                          String fecha_fin,
                                          String precio_inicio,
                                          String precio_fin,
                                          String latitud,
                                          String longitud,
                                          String distancia){
        return WSRetrofit.getInstance().filterExperiencia(
                WSRetrofit.APARTADO,
                WSRetrofit.KEY,
                WSRetrofit.FILTER_EXPERIENCIAS,
                intereses,
                fecha_inicio,
                fecha_fin,
                precio_inicio,
                precio_fin,
                latitud,
                longitud,
                distancia
        );
    }

    public static List<Experiencia> addResponseToList(List<Experiencia> experiencias, ResponseWS responseWS){
        if(responseWS != null && !responseWS.getResult().isEmpty()){
            for(Object obj : responseWS.getResult()){
                if(obj instanceof Experiencia){
                    experiencias.add((Experiencia) obj);
                }
            }
        }

        return experiencias;
    }

    //GETTERS Y SETTERS
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getVisible() {
        return visible;
    }

    public void setVisible(String visible) {
        this.visible = visible;
    }

    public Productor getProductor() {
        return productor;
    }

    public void setProductor(Productor productor) {
        this.productor = productor;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getDuracion() {
        return duracion;
    }

    public void setDuracion(String duracion) {
        this.duracion = duracion;
    }

    public ArrayList<String> getImagenes() {
        return imagenes;
    }

    public void setImagenes(ArrayList<String> imagenes) {
        this.imagenes = imagenes;
    }

    public Integer getCalificacino() {
        return calificacion;
    }

    public void setCalificacino(Integer calificacion) {
        this.calificacion = calificacion;
    }
}
