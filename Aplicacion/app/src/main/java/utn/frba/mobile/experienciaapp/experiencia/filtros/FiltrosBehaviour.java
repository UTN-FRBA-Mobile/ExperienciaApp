package utn.frba.mobile.experienciaapp.experiencia.filtros;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import utn.frba.mobile.experienciaapp.experiencia.BuscarExperienciaActivity;
import utn.frba.mobile.experienciaapp.lib.utils.Alert;

public class FiltrosBehaviour {

   private  ImageView favoritosIB,interesesIB,fechaHoraIB,presupuestoIB,distanciaIB;
   private AppCompatActivity activity;

    public void setBehaviour(AppCompatActivity activity, ImageView favoritosIB, ImageView interesesIB, ImageView fechaHoraIB, ImageView presupuestoIB, ImageView distanciaIB){
        this.favoritosIB=favoritosIB;
        this.interesesIB=interesesIB;
        this.fechaHoraIB=fechaHoraIB;
        this.presupuestoIB=presupuestoIB;
        this.distanciaIB=distanciaIB;
        this.activity=activity;
        favoritosIBOnClick();
        interesesIBOnClick();
    }

    private void interesesIBOnClick(){
        interesesIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Alert alert = new Alert(activity);
                alert.Show("Descripcion","Titulo");
            }
        });
    }

    private void favoritosIBOnClick(){
        favoritosIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.startActivity(new Intent(activity,FavoritosFiltro.class));
            }
        });
    }


}
