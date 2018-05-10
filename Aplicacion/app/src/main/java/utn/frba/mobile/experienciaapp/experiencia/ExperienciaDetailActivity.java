package utn.frba.mobile.experienciaapp.experiencia;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import utn.frba.mobile.experienciaapp.R;

public class ExperienciaDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_experiencia_detail);

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        setTitle(title);
    }

    public void viewItinerary(View view){
        Toast.makeText(ExperienciaDetailActivity.this.getApplicationContext(),
                "Mostrar Ininerario",
                Toast.LENGTH_SHORT).show();
    }
}
