package utn.frba.mobile.experienciaapp;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;
import utn.frba.mobile.experienciaapp.experiencia.BuscarExperienciaActivity;
import utn.frba.mobile.experienciaapp.lib.ws.WSRetrofit;
import utn.frba.mobile.experienciaapp.login.LoginActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        try {
            Fabric.with(this, new Crashlytics());
            //throw new RuntimeException("This is a crash");//TO initialize crashlitycs on web
        }catch (java.lang.IllegalArgumentException e){
            //e.printStackTrace();
        }

        final Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();

                Intent i = new Intent(getBaseContext(), HomeActivity.class);
                startActivity(i);
            }
        }, 3000);//Time of splash
    }
}
