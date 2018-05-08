package utn.frba.mobile.experienciaapp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InteresXTurista {
    @SerializedName("turista")
    @Expose
    private Turista turista;
    @SerializedName("interes")
    @Expose
    private Interes interes;

    public Turista getId() {
        return turista;
    }

    public void setId(Turista turista) {
        this.turista = turista;
    }

    public Interes getInteres() {
        return interes;
    }

    public void setInteres(Interes interes) {
        this.interes = interes;
    }
}
