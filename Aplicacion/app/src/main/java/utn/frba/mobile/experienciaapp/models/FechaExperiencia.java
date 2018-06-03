package utn.frba.mobile.experienciaapp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FechaExperiencia {
    private static SimpleDateFormat dateFormat =new SimpleDateFormat("dd/MM/yyyy hh:mm");

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("disponibilidad")
    @Expose
    private Integer disponibilidad;
    @SerializedName("fecha_hora")
    @Expose
    private String fecha_hora;
    @SerializedName("experiencia")
    @Expose
    private Experiencia experiencia;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getDisponibilidad() {
        return disponibilidad;
    }

    public void setDisponibilidad(int disponibilidad) {
        this.disponibilidad = disponibilidad;
    }

    public String getFechaHora() {
        return fecha_hora;
    }

    public void setFechaHora(String fecha_hora) {
        this.fecha_hora = fecha_hora;
    }

    public void setFechaHora(Date fecha_hora) {
        this.fecha_hora = dateFormat.format(fecha_hora);
    }

    public Experiencia getExperiencia() {
        return experiencia;
    }

    public void setExperiencia(Experiencia experiencia) {
        this.experiencia = experiencia;
    }
}
