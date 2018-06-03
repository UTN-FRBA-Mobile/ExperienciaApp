package utn.frba.mobile.experienciaapp.agenda;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import utn.frba.mobile.experienciaapp.R;
import utn.frba.mobile.experienciaapp.agenda.adapter.ViewPageAdapter;
import utn.frba.mobile.experienciaapp.models.Experiencia;
import utn.frba.mobile.experienciaapp.models.FechaExperiencia;
import utn.frba.mobile.experienciaapp.models.Reserva;
import utn.frba.mobile.experienciaapp.service.AgendaService;


public class AgendaActivity extends AppCompatActivity {
    private static final String TAG = "AgendaActivity";

    private ViewPager imagenIV;
    private TextView tituloTV;
    private TextView infoTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agenda);
        initComponenteReference();
        populateExperiencias();
    }

    private void initComponenteReference(){
        this.imagenIV=findViewById(R.id.imagenIV);
        this.tituloTV=findViewById(R.id.tituloTV);
        this.infoTV=findViewById(R.id.infoTV);

    }

    private void populateExperiencias(){
       List<Reserva> reservas= AgendaService.getInstance().getMisReservas();
        Reserva reserva=reservas.get(0);
        FechaExperiencia fechaExperiencia=reserva.getFechaExperiencia();
        Experiencia experiencia=fechaExperiencia.getExperiencia();
        ViewPageAdapter viewPageAdapter=new ViewPageAdapter(this,experiencia.getImagenes());
        this.imagenIV.setAdapter(viewPageAdapter);
        this.tituloTV.setText(experiencia.getNombre()+" ("+experiencia.getDireccion()+")");
        StringBuilder infoExp=new StringBuilder();
        infoExp.append("Precio: $").append(reserva.getTotal()).append("\nDuración: ").append(experiencia.getDuracion());
        this.infoTV.setText(infoExp.toString());

    }


}
