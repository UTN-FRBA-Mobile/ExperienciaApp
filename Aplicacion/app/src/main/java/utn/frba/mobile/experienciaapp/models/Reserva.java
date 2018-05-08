package utn.frba.mobile.experienciaapp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Reserva {
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
}
