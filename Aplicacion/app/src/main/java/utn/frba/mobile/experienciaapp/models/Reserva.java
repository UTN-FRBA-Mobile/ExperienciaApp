package utn.frba.mobile.experienciaapp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import retrofit2.Call;
import utn.frba.mobile.experienciaapp.lib.ws.ResponseWS;
import utn.frba.mobile.experienciaapp.lib.ws.WSRetrofit;

public class Reserva extends ModeloGenerico{
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("cantidad_personas")
    @Expose
    private Integer cantidadPersonas;
    @SerializedName("codigo")
    @Expose
    private String codigo;
    @SerializedName("total")
    @Expose
    private Double total;
    @SerializedName("turista")
    @Expose
    private Turista turista;
    @SerializedName("fecha_experiencia")
    @Expose
    private FechaExperiencia fechaExperiencia;

    public static Call<ResponseWS> GetReservasOf(int turista_id,String token){
        return WSRetrofit.getInstance().getInformationOf(
                WSRetrofit.APARTADO,
                WSRetrofit.KEY,
                WSRetrofit.GET_RESERVAS_OF_TURISTA,
                turista_id,
                token
        );

    }

    public static Call<ResponseWS> ReservarExperiencia(int id_turista,String login_token,int id_fecha_disponible,String cantidad,String total){
        return WSRetrofit.getInstance().reservarExperiencia(
                WSRetrofit.APARTADO,
                WSRetrofit.KEY,
                WSRetrofit.RESERVAR_EXPERIENCIA,
                Integer.toString(id_turista),
                login_token,
                Integer.toString(id_fecha_disponible),
                cantidad,
                total
        );
    }

    public static Call<ResponseWS> BorrarReservaTurista(int id_turista,String login_token,int id_reserva){
        return WSRetrofit.getInstance().borrarReservaTurista(
                WSRetrofit.APARTADO,
                WSRetrofit.KEY,
                WSRetrofit.BORRAR_RESERVA_TURISTA,
                login_token,
                Integer.toString(id_turista),
                Integer.toString(id_reserva)
        );
    }

    public static List<Reserva> addResponseToList(List<Reserva> reservas, ResponseWS responseWS){
        if(responseWS != null && !responseWS.getResult().isEmpty()){
            for(Object obj : responseWS.getResult()){
                if(obj instanceof Experiencia){
                    reservas.add((Reserva) obj);
                }
            }
        }

        return reservas;
    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCantidadPersonas() {
        return cantidadPersonas;
    }

    public void setCantidadPersonas(Integer cantidad_personas) {
        this.cantidadPersonas = cantidad_personas;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Turista getTurista() {
        return turista;
    }

    public void setTurista(Turista turista) {
        this.turista = turista;
    }

    public FechaExperiencia getFechaExperiencia() {
        return fechaExperiencia;
    }

    public void setFechaExperiencia(FechaExperiencia fechaExperiencia) {
        this.fechaExperiencia = fechaExperiencia;
    }
}
