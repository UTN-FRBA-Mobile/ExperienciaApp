package utn.frba.mobile.experienciaapp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import utn.frba.mobile.experienciaapp.lib.googlemaps.CustomMarkerData;
import utn.frba.mobile.experienciaapp.lib.ws.ResponseWS;
import utn.frba.mobile.experienciaapp.lib.ws.WSRetrofit;

public class PuntoInteres extends ModeloGenerico implements CustomMarkerData {
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
    @SerializedName("imagenes")
    @Expose
    private ArrayList<String> imagenes;

    public static Call<ResponseWS> GetPuntosDeInteresOf(int experiencia_id){
        return WSRetrofit.getInstance().getInformationOf(
                WSRetrofit.APARTADO,
                WSRetrofit.KEY,
                WSRetrofit.GET_PUNTOS_INTERES_OF_EXPERIENCIA,
                Integer.toString(experiencia_id)
        );
    }
    public static List<PuntoInteres> addResponseToList(List<PuntoInteres> puntosDeInteres, ResponseWS responseWS){
        if(responseWS != null && !responseWS.getResult().isEmpty()){
            for(Object obj : responseWS.getResult()){
                if(obj instanceof PuntoInteres){
                    puntosDeInteres.add((PuntoInteres) obj);
                }
            }
        }

        return puntosDeInteres;
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

    public ArrayList<String> getImagenes() {
        return imagenes;
    }

    public void setImagenes(ArrayList<String> imagenes) {
        this.imagenes = imagenes;
    }
}
