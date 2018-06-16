package utn.frba.mobile.experienciaapp.models;

import android.view.MotionEvent;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import utn.frba.mobile.experienciaapp.lib.ws.ResponseWS;
import utn.frba.mobile.experienciaapp.lib.ws.WSRetrofit;

public class Interes extends ModeloGenerico{
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("nombre")
    @Expose
    private String nombre;

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

    //Metodos para WS
    public static Call<ResponseWS> GetIntereses(){
        return WSRetrofit.getInstance().getGeneralInformation(
                WSRetrofit.APARTADO,
                WSRetrofit.KEY,
                WSRetrofit.GET_INTERESES
        );
    }

    public static List<Interes> addResponseToList(List<Interes> intereses, ResponseWS responseWS){
        if(responseWS != null && !responseWS.getResult().isEmpty()){
            for(Object obj : responseWS.getResult()){
                if(obj instanceof Interes){
                    intereses.add((Interes) obj);
                }
            }
        }

        return intereses;
    }

    public static boolean IsInCadena(int id_interes, String cadena){
        List<String> intereses = Arrays.asList(cadena.split(","));
        if(!intereses.isEmpty()){
            return intereses.contains(Integer.toString(id_interes));
        }

        return false;
    }
}
