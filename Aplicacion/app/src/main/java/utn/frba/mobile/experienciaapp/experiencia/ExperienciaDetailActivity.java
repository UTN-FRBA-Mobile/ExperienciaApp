package utn.frba.mobile.experienciaapp.experiencia;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import utn.frba.mobile.experienciaapp.BaseActivityWithToolBar;
import utn.frba.mobile.experienciaapp.R;
import utn.frba.mobile.experienciaapp.experiencia.puntos_interes.PuntosDeInteresActivity;
import utn.frba.mobile.experienciaapp.lib.utils.Alert;
import utn.frba.mobile.experienciaapp.lib.ws.ReciveResponseWS;
import utn.frba.mobile.experienciaapp.lib.ws.ResponseWS;
import utn.frba.mobile.experienciaapp.lib.ws.WSRetrofit;
import utn.frba.mobile.experienciaapp.models.Experiencia;
import utn.frba.mobile.experienciaapp.models.Interes;
import utn.frba.mobile.experienciaapp.models.Productor;

public class ExperienciaDetailActivity extends BaseActivityWithToolBar implements ReciveResponseWS{

    ViewPager viewPagerPhotoSlider;
    Experiencia experiencia;

    private static final String TAG = "ExperienciaDetailAct";
    private static final int GET_DETALLE = 3;

    Alert loadingAlert = new Alert(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_experiencia_detail);
        AddBackButtonToToolBar();

        Intent intent = getIntent();
        Integer id = intent.getIntExtra("id", 0);
        Experiencia.GetDetalle(id).enqueue(WSRetrofit.ParseResponseWS(this,GET_DETALLE));

        loadingAlert.Loading();
    }

    private void setLayoutTexts(Experiencia exp){
        setTxtTextView(R.id.fecha, exp.getFechaCreacion());
        setTxtTextView(R.id.hora, exp.getDuracion() + " hs");
        setTxtTextView(R.id.precio, exp.getPrecio());
        setTxtTextView(R.id.descripcion, exp.getDescripcion());
        setTxtTextView(R.id.direccion, exp.getDireccion());
        setTxtTextView(R.id.productor, exp.getProductor().getNombre() + " " + exp.getProductor().getApellido());
    }

    private void setTxtTextView(int id, String text){
        TextView myTextView= (TextView) findViewById(id);
        myTextView.setText(text);
    }

    public void viewItinerary(View view){
        //Toast.makeText(ExperienciaDetailActivity.this.getApplicationContext(),"Mostrar Ininerario",Toast.LENGTH_SHORT).show();
        Intent myIntent = new Intent(ExperienciaDetailActivity.this, PuntosDeInteresActivity.class);
        if(experiencia != null){
            myIntent.putExtra("id",experiencia.getId()); //Optional parameters
        }
        ExperienciaDetailActivity.this.startActivity(myIntent);
    }

    public void verProductor(View view){
        Intent myIntent = new Intent(ExperienciaDetailActivity.this, ProductorActivity.class);
        myIntent.putExtra("id",experiencia.getProductor().getId()); //Optional parameters
        ExperienciaDetailActivity.this.startActivity(myIntent);
    }

    @Override
    public void ReciveResponseWS(ResponseWS responseWS, int accion) {
        switch(accion){
            case GET_DETALLE:{
                if(responseWS != null && responseWS.getResult() != null && responseWS.getResult().size() == 1 && responseWS.getResult().get(0) instanceof Experiencia) {
                    experiencia = (Experiencia) responseWS.getResult().get(0);
                    setTitle(experiencia.getNombre());

                    viewPagerPhotoSlider = (ViewPager) findViewById(R.id.photoSlider);
                    ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this, experiencia.getImagenes());
                    viewPagerPhotoSlider.setAdapter(viewPagerAdapter);

                    RatingBar calificacion = (RatingBar) findViewById(R.id.calificacion);

                    calificacion.setMax(5);
                    calificacion.setNumStars(experiencia.getCalificacino());

                    this.setLayoutTexts(experiencia);
                }else{
                    new Alert(this).Show("Ocurrio un error al consultar el WS.","Error");
                }

                if(loadingAlert.IsLoading()){
                    loadingAlert.DismissLoading();
                }
                break;
            }

            default:{
                Log.d(TAG,"ReciveResponseWS accion no identificada: " + accion);
            }
        }
    }
}