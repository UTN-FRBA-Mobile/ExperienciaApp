package utn.frba.mobile.experienciaapp.Productor;


import utn.frba.mobile.experienciaapp.Productor.adapter.ExperienciaProductorAdapter;
import utn.frba.mobile.experienciaapp.R;
import utn.frba.mobile.experienciaapp.models.Experiencia;
import utn.frba.mobile.experienciaapp.service.ProductorService;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

public class ProductorExperienciasActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productor_experiencias);

        setTitle("Experiencias");

        RecyclerView recyclerView = findViewById(R.id.recycler_view);

        List<Experiencia> items = ProductorService.getInstance().getMockProductorExperiencias(1);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(new ExperienciaProductorAdapter(items));

    }
}
