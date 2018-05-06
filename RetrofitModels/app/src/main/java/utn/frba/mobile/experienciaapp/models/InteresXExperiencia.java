package utn.frba.mobile.experienciaapp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InteresXExperiencia {
    @SerializedName("experiencia")
    @Expose
    private Experiencia experiencia;
    @SerializedName("interes")
    @Expose
    private Interes interes;

    public Experiencia getId() {
        return experiencia;
    }

    public void setId(Experiencia experiencia) {
        this.experiencia = experiencia;
    }

    public Interes getInteres() {
        return interes;
    }

    public void setInteres(Interes interes) {
        this.interes = interes;
    }
}
