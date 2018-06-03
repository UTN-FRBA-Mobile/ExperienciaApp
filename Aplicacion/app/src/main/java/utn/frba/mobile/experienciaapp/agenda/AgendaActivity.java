package utn.frba.mobile.experienciaapp.agenda;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import utn.frba.mobile.experienciaapp.R;
import utn.frba.mobile.experienciaapp.agenda.adapter.RecycleCardAdapter;
import utn.frba.mobile.experienciaapp.agenda.adapter.ViewPageAdapter;
import utn.frba.mobile.experienciaapp.models.Experiencia;
import utn.frba.mobile.experienciaapp.models.FechaExperiencia;
import utn.frba.mobile.experienciaapp.models.Reserva;
import utn.frba.mobile.experienciaapp.service.AgendaService;


public class AgendaActivity extends AppCompatActivity {
    private static final String TAG = "AgendaActivity";

    private RecycleCardAdapter recycleCardAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView cardViewRV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agenda);
        initComponenteReference();
        //populateExperiencias();



    }

    private void initComponenteReference(){
        this.layoutManager=new GridLayoutManager(this,1);
        this.cardViewRV=findViewById(R.id.cardViewRV);
        this.cardViewRV.setHasFixedSize(true);
        this.cardViewRV.setLayoutManager(this.layoutManager);
        this.recycleCardAdapter=new RecycleCardAdapter(this,AgendaService.getInstance().getMisReservas());
        this.cardViewRV.setAdapter(recycleCardAdapter);
    }

    private void popularData(){

    }


}
