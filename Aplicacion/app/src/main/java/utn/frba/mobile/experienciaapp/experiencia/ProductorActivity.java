package utn.frba.mobile.experienciaapp.experiencia;

import android.content.Intent;
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

    Productor productor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productor);
        AddBackButtonToToolBar();

        Intent intent = getIntent();
        Integer id = intent.getIntExtra("id", 0);
        Productor.GetPerfil(id).enqueue(WSRetrofit.ParseResponseWS(this,GET_PERFIL));


        productor = getMockProductor(id);
        setTitle(productor.getNombre() + " " + productor.getApellido());

        setLayoutTexts(productor);

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

    private Productor getMockProductor(Integer id){
        Productor prod = new Productor();
        prod.setId(100);
        prod.setNombre("Roberto");
        prod.setApellido("Gómez Bolaños");
        prod.setCelular("+54 1512456154");
        prod.setDescripcion("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.\n Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo. Nemo enim ipsam voluptatem quia voluptas sit aspernatur aut odit aut fugit, sed quia consequuntur magni dolores eos qui ratione voluptatem sequi nesciunt. Neque porro quisquam est, qui dolorem ipsum quia dolor sit amet, consectetur, adipisci velit, sed quia non numquam eius modi tempora incidunt ut labore et dolore magnam aliquam quaerat voluptatem. Ut enim ad minima veniam, quis nostrum exercitationem ullam corporis suscipit laboriosam, nisi ut aliquid ex ea commodi consequatur? Quis autem vel eum iure reprehenderit qui in ea voluptate velit esse quam nihil molestiae consequatur, vel illum qui dolorem eum fugiat quo voluptas nulla pariatur?");
        prod.setDireccion("Av Presidente Preon 1243");
        prod.setDni("33562659");
        prod.setEmail("robertogomez@gmail.com");
        prod.setTelefono("43456754");
        return prod;
    }

    public void verFotos(View view){
        Toast.makeText(ProductorActivity.this.getApplicationContext(),"Mostrar Fotos", Toast.LENGTH_SHORT).show();
    }

    public void verExperiencias(View view){
        Toast.makeText(ProductorActivity.this.getApplicationContext(),"Mostrar Experiencias", Toast.LENGTH_SHORT).show();
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
