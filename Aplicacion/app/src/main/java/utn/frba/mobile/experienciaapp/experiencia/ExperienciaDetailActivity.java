package utn.frba.mobile.experienciaapp.experiencia;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import utn.frba.mobile.experienciaapp.BaseActivityWithToolBar;
import utn.frba.mobile.experienciaapp.R;
import utn.frba.mobile.experienciaapp.experiencia.puntos_interes.PuntosDeInteresActivity;
import utn.frba.mobile.experienciaapp.lib.utils.Alert;
import utn.frba.mobile.experienciaapp.lib.ws.ReciveResponseWS;
import utn.frba.mobile.experienciaapp.lib.ws.ResponseWS;
import utn.frba.mobile.experienciaapp.lib.ws.WSRetrofit;
import utn.frba.mobile.experienciaapp.models.Experiencia;
import utn.frba.mobile.experienciaapp.models.FechaExperiencia;

public class ExperienciaDetailActivity extends BaseActivityWithToolBar implements ReciveResponseWS{

    ViewPager viewPagerPhotoSlider;
    Experiencia experiencia;

    private static final String TAG = "ExperienciaDetailAct";
    private static final int GET_DETALLE = 3;

    public Alert reservaModalAlert;
    public View reservaModalView;
    public Spinner spinnerDates;

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

    public void verFotos(View view){
        //Toast.makeText(ProductorActivity.this.getApplicationContext(),"Mostrar Fotos", Toast.LENGTH_SHORT).show();
        Intent myIntent = new Intent(ExperienciaDetailActivity.this, GaleriaActivity.class);
        myIntent.putExtra("fotos",experiencia.getImagenes());
        ExperienciaDetailActivity.this.startActivity(myIntent);

    }

    public void openReserveModal(View view){

        reservaModalAlert = new Alert(ExperienciaDetailActivity.this);

        LinearLayout modal_content = new LinearLayout(ExperienciaDetailActivity.this);
        modal_content.setOrientation(LinearLayout.VERTICAL);
        modal_content.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);

        LayoutInflater inflater = LayoutInflater.from(getBaseContext());


        reservaModalView = inflater.inflate(R.layout.reserva_modal, modal_content, false);

        spinnerDates = (Spinner) reservaModalView.findViewById(R.id.fechasDisponibles);
        List<String> horarios = new ArrayList<>();

        for(FechaExperiencia fecha: experiencia.getFechasExperiencia()){
            horarios.add(fecha.getFechaHora());
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(ExperienciaDetailActivity.this, android.R.layout.simple_spinner_item, horarios);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDates.setAdapter(dataAdapter);



        View.OnClickListener aceptOnclick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText cantPersonasEditText = (EditText) reservaModalView.findViewById(R.id.cantPersonas);
                Spinner horarioSpinner = (Spinner) reservaModalView.findViewById(R.id.fechasDisponibles);

                if(cantPersonasEditText != null && cantPersonasEditText.getText() != null &&
                        cantPersonasEditText.getText().toString() != null && !cantPersonasEditText.getText().toString().isEmpty() &&
                        horarioSpinner != null && horarioSpinner.getSelectedItem() != null && horarioSpinner.getSelectedItem().toString() != null &&
                        !horarioSpinner.getSelectedItem().toString().isEmpty()
                        ){

                    String cantPersonas = cantPersonasEditText.getText().toString();
                    String horario = horarioSpinner.getSelectedItem().toString();

                    Toast.makeText(ExperienciaDetailActivity.this.getApplicationContext(),"Reserva exitosa para " + cantPersonas + " persona(s) a las " + horario + "." ,Toast.LENGTH_SHORT).show();
                    reservaModalAlert.Dismiss();
                } else {
                    Toast.makeText(ExperienciaDetailActivity.this.getApplicationContext(),"Complete los datos requeridos" ,Toast.LENGTH_SHORT).show();
                }




            }

        };

        reservaModalAlert.ShowFilterView(reservaModalView, "Reservar", aceptOnclick, false);

    }

    @Override
    public void ReciveResponseWS(ResponseWS responseWS, int accion) {
        switch(accion){
            case GET_DETALLE:{
                if(responseWS != null && responseWS.getResult() != null && responseWS.getResult().size() == 1 && responseWS.getResult().get(0) instanceof Experiencia) {
                    experiencia = (Experiencia) responseWS.getResult().get(0);

                    //----------  TODO: remover fechas mockeadas
                    ArrayList<FechaExperiencia> horarios = new ArrayList<FechaExperiencia>();
                    horarios.add(new FechaExperiencia(1, 1, "9:00 AM", experiencia));
                    horarios.add(new FechaExperiencia(2, 1, "11:00 AM", experiencia));
                    horarios.add(new FechaExperiencia(3, 1, "3:00 PM", experiencia));
                    experiencia.setFechasExperiencia(horarios);
                    // ----------------

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