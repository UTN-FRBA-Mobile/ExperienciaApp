package utn.frba.mobile.experienciaapp.experiencia.filtros;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;

import utn.frba.mobile.experienciaapp.R;
public class FavoritosFiltro extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favoritos_filtro);

        DisplayMetrics dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width=(int)(dm.widthPixels*0.8);
        int height=(int)(dm.heightPixels*0.6);
        getWindow().setLayout(width,height);
    }
}
