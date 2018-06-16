package utn.frba.mobile.experienciaapp.experiencia;

import android.content.Intent;
import android.media.Rating;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import utn.frba.mobile.experienciaapp.BaseActivityWithToolBar;
import utn.frba.mobile.experienciaapp.R;
import utn.frba.mobile.experienciaapp.experiencia.productor.ExperienciasProductorActivity;
import utn.frba.mobile.experienciaapp.experiencia.puntos_interes.PuntosDeInteresActivity;
import utn.frba.mobile.experienciaapp.lib.utils.Alert;
import utn.frba.mobile.experienciaapp.lib.ws.ReciveResponseWS;
import utn.frba.mobile.experienciaapp.lib.ws.ResponseWS;
import utn.frba.mobile.experienciaapp.lib.ws.WSRetrofit;
import utn.frba.mobile.experienciaapp.models.Experiencia;
import utn.frba.mobile.experienciaapp.models.Productor;

public class ProductorActivity extends BaseActivityWithToolBar implements ReciveResponseWS {

    private static final String TAG = "ProductorActivity";
    private static final int GET_PERFIL = 1;

    Alert loadingAlert = new Alert(this);
    ViewPager viewPagerPhotoSlider;
    RatingBar calificacionBar;

    Productor productor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productor);
        AddBackButtonToToolBar();

        Intent intent = getIntent();
        Integer id = intent.getIntExtra("id", 0);
        Productor.GetPerfil(id).enqueue(WSRetrofit.ParseResponseWS(this,GET_PERFIL));

        loadingAlert.Loading();
    }

    private void setLayoutTexts(Productor p){
        setTxtTextView(R.id.nombre, productor.getNombre() + " " + productor.getApellido());
        setTxtTextView(R.id.direccion, p.getDireccion());
        setTxtTextView(R.id.telefono, p.getTelefono());
        setTxtTextView(R.id.celular, p.getCelular());
        setTxtTextView(R.id.email, p.getEmail());
        setTxtTextView(R.id.descripcion, p.getDescripcion());
        setTxtTextView(R.id.dni, p.getDni());

        RatingBar ratingBar = (RatingBar) findViewById(R.id.calificacion);
        ratingBar.setNumStars(3);

    }

    private void setTxtTextView(int id, String text){
        TextView myTextView= (TextView) findViewById(id);
        myTextView.setText(text);
    }

    public void verFotos(View view){
        Toast.makeText(ProductorActivity.this.getApplicationContext(),"Mostrar Fotos", Toast.LENGTH_SHORT).show();
    }

    public void verExperiencias(View view){
        Intent myIntent = new Intent(ProductorActivity.this, ExperienciasProductorActivity.class);
        if(productor != null){
            myIntent.putExtra("id",productor.getId()); //Optional parameters
        }
        ProductorActivity.this.startActivity(myIntent);
    }

    @Override
    public void ReciveResponseWS(ResponseWS responseWS, int accion) {
        switch(accion){
            case GET_PERFIL:{
                if(responseWS != null && responseWS.getResult() != null && responseWS.getResult().size() == 1 && responseWS.getResult().get(0) instanceof Productor) {
                    productor = (Productor) responseWS.getResult().get(0);
                    setTitle(productor.getNombre() + " " + productor.getApellido());
                    setLayoutTexts(productor);
                    viewPagerPhotoSlider = (ViewPager) findViewById(R.id.photoSlider);
                    ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this, productor.getImagenes());
                    viewPagerPhotoSlider.setAdapter(viewPagerAdapter);

                    calificacionBar = (RatingBar) findViewById(R.id.calificacion);
                    calificacionBar.setMax(5);
                    calificacionBar.setNumStars(productor.getCalificacino());

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
