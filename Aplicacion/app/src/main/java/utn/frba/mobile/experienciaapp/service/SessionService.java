package utn.frba.mobile.experienciaapp.service;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import utn.frba.mobile.experienciaapp.lib.ws.ReciveResponseWS;
import utn.frba.mobile.experienciaapp.lib.ws.ResponseWS;
import utn.frba.mobile.experienciaapp.lib.ws.WSRetrofit;
import utn.frba.mobile.experienciaapp.models.Turista;

public class SessionService {
    private static final String TAG = "SessionService";
    private static  SessionService instance=null;
    private Turista turistaLogueado=null;


    public static SessionService getInstance(){
        if (instance==null){
            instance=new SessionService();
        }
        return instance;
    }

    public boolean isSessionActive(AppCompatActivity activity){
        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
        FirebaseUser fuser=firebaseAuth.getCurrentUser();
        if(fuser!=null) {
            Turista turista = new Turista();
            turista.setId(fuser.getUid());
            turista.setEmail(fuser.getEmail());
            turista.setFirebaseToken(fuser.getIdToken(true).toString());
            if(turistaLogueado==null){
                try {
                    turistaLogueado=new ExecuteTask(activity).execute(turista).get();
                    //Esto esta mockeado porque aun no registre
                    turistaLogueado.setId(fuser.getUid());
                    turistaLogueado.setEmail(fuser.getEmail());
                    turistaLogueado.setImageUrl(fuser.getPhotoUrl());
                } catch (Exception e) {
                    throw new IllegalStateException(e);
                }

            }
            return true;
        }else{
            turistaLogueado=null;
        }
        return false;
    }

    public Turista getTurista(){
        return turistaLogueado;
    }



    private class ExecuteTask extends AsyncTask<Turista,Void,Turista> implements ReciveResponseWS {

        private Context context;
        public ExecuteTask(Context context) {
            this.context = context;
        }

        private Turista turistaLogueado;
        @Override
        protected Turista doInBackground(Turista... turistas) {
            Turista turista=turistas[0];
            try {
                WSRetrofit.ParseResponseWS(Turista.SignIn(turista).execute(),this, SIGN_IN_TEST);
            }catch (Exception e){
                throw new IllegalStateException(e);
            }
            return turistaLogueado;
        }


        @Override
        public void ReciveResponseWS(ResponseWS responseWS, int accion) {
            switch(accion){
                //TODO: DELETE SOLO PARA TEST
                case SIGN_IN_TEST:{
                    if(responseWS != null && responseWS.getResult() != null && responseWS.getResult().size() == 1 && responseWS.getResult().get(0) instanceof Turista){
                        turistaLogueado = (Turista) responseWS.getResult().get(0);
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

}
