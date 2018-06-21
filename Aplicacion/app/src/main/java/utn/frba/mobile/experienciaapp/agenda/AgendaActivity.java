package utn.frba.mobile.experienciaapp.agenda;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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


public class AgendaActivity extends AppCompatActivity implements ReciveResponseWS {
    private static final String TAG = "AgendaActivity";

    public static final int GET_RESERVAS = 1;
    public static final int SIGN_IN_TEST = 2; //TODO: DELETE SOLO PARA TEST

    private RecycleCardAdapter recycleCardAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView cardViewRV;

    private List<Reserva> reservas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agenda);
        initComponenteReference();
        //populateExperiencias();

        /*
        Turista turistaRegisterTest = new Turista();
        turistaRegisterTest.setEmail("ejemplo@gerger.com");
        turistaRegisterTest.setFirebaseToken("wegnwegwe8u9u443lklk");
        Turista.SignIn(turistaRegisterTest).enqueue(WSRetrofit.ParseResponseWS(this,SIGN_IN_TEST));
        */

        Turista turista_test = new Turista();
        turista_test.setId(2);
        turista_test.setLoginToken("bcff836d782a4ef5bfec6946804d5cdf");
        Reserva.GetReservasOf(turista_test.getId(),turista_test.getLoginToken()).enqueue(WSRetrofit.ParseResponseWS(this,GET_RESERVAS));
    }

    private void initComponenteReference(){
        this.layoutManager=new GridLayoutManager(this,1);
        this.cardViewRV=findViewById(R.id.cardViewRV);
        List<Reserva> reservas=AgendaService.getInstance().getMisReservas();
        this.cardViewRV.setHasFixedSize(true);
        this.cardViewRV.setLayoutManager(this.layoutManager);
        this.recycleCardAdapter=new RecycleCardAdapter(this,reservas);
        this.cardViewRV.setAdapter(recycleCardAdapter);

    }

    private void popularData(){

    }


    @Override
    public void ReciveResponseWS(ResponseWS responseWS, int accion) {
        switch(accion){
            case GET_RESERVAS:{
                if(responseWS != null && responseWS.getResult() != null && responseWS.getResult().size() > 0 && responseWS.getResult().get(0) instanceof Reserva){
                    reservas.clear();
                    reservas = Reserva.addResponseToList(reservas,responseWS);
                }else{
                    reservas = new ArrayList<>();
                }
                break;
            }

            //TODO: DELETE SOLO PARA TEST
            case SIGN_IN_TEST:{
                if(responseWS != null && responseWS.getResult() != null && responseWS.getResult().size() == 1 && responseWS.getResult().get(0) instanceof Turista){
                    //turistaCreado = responseWS.getResult().get(0)
                }else{
                    //Do somthing
                }
                break;
            }

            default:{
                Log.d(TAG,"ReciveResponseWS accion no identificada: " + accion);
            }
        }
    }
}
