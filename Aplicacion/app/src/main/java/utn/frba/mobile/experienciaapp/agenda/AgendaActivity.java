package utn.frba.mobile.experienciaapp.agenda;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import utn.frba.mobile.experienciaapp.R;
import utn.frba.mobile.experienciaapp.agenda.adapter.RecycleCardAdapter;
import utn.frba.mobile.experienciaapp.agenda.adapter.ViewPageAdapter;
import utn.frba.mobile.experienciaapp.lib.ws.ReciveResponseWS;
import utn.frba.mobile.experienciaapp.lib.ws.ResponseWS;
import utn.frba.mobile.experienciaapp.lib.ws.WSRetrofit;
import utn.frba.mobile.experienciaapp.models.Experiencia;
import utn.frba.mobile.experienciaapp.models.FechaExperiencia;
import utn.frba.mobile.experienciaapp.models.Reserva;
import utn.frba.mobile.experienciaapp.models.Turista;
import utn.frba.mobile.experienciaapp.service.AgendaService;
import utn.frba.mobile.experienciaapp.service.SessionService;


public class AgendaActivity extends AppCompatActivity  {
    private static final String TAG = "AgendaActivity";


    private RecycleCardAdapter recycleCardAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView cardViewRV;
    private Turista turistaLogueado;
    private SessionService sessionService;

    private List<Reserva> reservas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agenda);
        sessionService=SessionService.getInstance();
        if(sessionService.isSessionActive(this)){
            turistaLogueado=sessionService.getTurista();
//            Reserva.GetReservasOf(turistaLogueado.getId(),turistaLogueado.getLoginToken()).enqueue(WSRetrofit.ParseResponseWS(this,GET_RESERVAS));
            try {
                reservas = new ObtenerReservasTask(this).execute(turistaLogueado).get();
            }catch (Exception e){
                throw new IllegalStateException(e);
            }

        }else{
            Toast.makeText(getApplicationContext(),"Debe iniciar session.",Toast.LENGTH_LONG).show();

        }
        initComponenteReference();
    }

    private void initComponenteReference(){
        this.layoutManager=new GridLayoutManager(this,1);
        this.cardViewRV=findViewById(R.id.cardViewRV);
        this.cardViewRV.setHasFixedSize(true);
        this.cardViewRV.setLayoutManager(this.layoutManager);
        this.recycleCardAdapter=new RecycleCardAdapter(this,reservas);
        this.cardViewRV.setAdapter(recycleCardAdapter);

    }

    private void popularData(){

    }




    private class ObtenerReservasTask extends AsyncTask<Turista,Void,List<Reserva>> implements ReciveResponseWS {

        private Context context;
        private List<Reserva> lista=new ArrayList<Reserva>();
        public ObtenerReservasTask(Context context) {
            this.context = context;
        }


        @Override
        protected List<Reserva> doInBackground(Turista... turistas) {
            Turista turistaLogueado=turistas[0];
            try {
                WSRetrofit.ParseResponseWS( Reserva.GetReservasOf(turistaLogueado.getId(),turistaLogueado.getLoginToken()).execute(),this, GET_RESERVAS);

            }catch (Exception e){
                throw new IllegalStateException(e);
            }
            return lista;
        }


        @Override
        public void ReciveResponseWS(ResponseWS responseWS, int accion) {
            switch(accion){
                case GET_RESERVAS:{
                    if(responseWS != null && responseWS.getResult() != null && responseWS.getResult() instanceof ArrayList){
                        lista.clear();
                        lista = Reserva.addResponseToList(lista,responseWS);
                    }else{
                        Toast.makeText(getApplicationContext(),"No tiene reservas agendadas.",Toast.LENGTH_LONG).show();
                        lista = new ArrayList<>();
                    }
                    break;
                }



                default:{
                    Log.d(TAG,"ReciveResponseWS accion no identificada: " + accion);
                }
            }
        }
    }
}
