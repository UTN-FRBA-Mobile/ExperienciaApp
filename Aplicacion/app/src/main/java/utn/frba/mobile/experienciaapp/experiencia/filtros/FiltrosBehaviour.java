package utn.frba.mobile.experienciaapp.experiencia.filtros;

import android.widget.ImageView;

public class FiltrosBehaviour {

   private  ImageView favoritosIB,interesesIB,fechaHoraIB,presupuestoIB,distanciaIB;


    public void setBehaviour(ImageView favoritosIB,ImageView interesesIB,ImageView fechaHoraIB,ImageView presupuestoIB,ImageView distanciaIB){
        this.favoritosIB=favoritosIB;
        this.interesesIB=interesesIB;
        this.fechaHoraIB=fechaHoraIB;
        this.presupuestoIB=presupuestoIB;
        this.distanciaIB=distanciaIB;
    }
}
