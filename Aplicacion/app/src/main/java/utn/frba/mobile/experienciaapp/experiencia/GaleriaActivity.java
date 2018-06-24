package utn.frba.mobile.experienciaapp.experiencia;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import utn.frba.mobile.experienciaapp.R;
import android.support.v7.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import android.support.v7.widget.LinearLayoutManager;
import utn.frba.mobile.experienciaapp.models.Foto;
import android.content.Intent;

public class GaleriaActivity extends AppCompatActivity {

    private ArrayList<String> imagenes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galeria);
        Intent intent = getIntent();
        imagenes = intent.getStringArrayListExtra("fotos");
        RecyclerView recyclerView = findViewById(R.id.recycler_view);

        List<Foto> items = new ArrayList<>();

        for (int i = 0; i < imagenes.size(); i++) {
            items.add(new Foto(imagenes.get(i)));
    }
        /*
        items.add(new Foto(android.R.drawable.ic_dialog_alert, android.R.string.paste));
        items.add(new Foto(android.R.drawable.ic_dialog_dialer, android.R.string.copy));
        items.add(new Foto(android.R.drawable.ic_dialog_email, android.R.string.copyUrl));
        items.add(new Foto(android.R.drawable.ic_dialog_map, android.R.string.cancel));
        items.add(new Foto(android.R.drawable.ic_dialog_info, android.R.string.cut));
        items.add(new Foto(android.R.drawable.ic_dialog_info, android.R.string.cut));
        items.add(new Foto(android.R.drawable.ic_dialog_info, android.R.string.cut));
        items.add(new Foto(android.R.drawable.ic_dialog_info, android.R.string.cut));
        items.add(new Foto(android.R.drawable.ic_dialog_info, android.R.string.cut));
        items.add(new Foto(android.R.drawable.ic_dialog_info, android.R.string.cut));
        items.add(new Foto(android.R.drawable.ic_dialog_info, android.R.string.cut));
        items.add(new Foto(android.R.drawable.ic_dialog_info, android.R.string.cut));
        items.add(new Foto(android.R.drawable.ic_dialog_info, android.R.string.cut));
        items.add(new Foto(android.R.drawable.ic_dialog_alert, android.R.string.paste));
        items.add(new Foto(android.R.drawable.ic_dialog_dialer, android.R.string.copy));
        items.add(new Foto(android.R.drawable.ic_dialog_email, android.R.string.copyUrl));
        items.add(new Foto(android.R.drawable.ic_dialog_alert, android.R.string.paste));
        items.add(new Foto(android.R.drawable.ic_dialog_dialer, android.R.string.copy));
        items.add(new Foto(android.R.drawable.ic_dialog_email, android.R.string.copyUrl));
        */


        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(new FotoAdapter(items,this));
    }
}
