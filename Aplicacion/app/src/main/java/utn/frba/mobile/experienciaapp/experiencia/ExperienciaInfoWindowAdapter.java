package utn.frba.mobile.experienciaapp.experiencia;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.gson.Gson;

import utn.frba.mobile.experienciaapp.R;
import utn.frba.mobile.experienciaapp.models.Experiencia;

public class ExperienciaInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private Activity context;

    public ExperienciaInfoWindowAdapter(Activity context){
        this.context = context;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        View view = context.getLayoutInflater().inflate(R.layout.customwindow, null);

        Gson gson = new Gson();
        Experiencia experiencia = gson.fromJson(marker.getSnippet(), Experiencia.class);

        TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
        TextView tvSubTitle = (TextView) view.findViewById(R.id.tv_subtitle);

        tvTitle.setText(experiencia.getNombre());
        tvSubTitle.setText(experiencia.getDescripcion());

      /*  Button button = (Button) view.findViewById(R.id.button_view_activity);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(CustomInfoWindowAdapter.this.context,
                        "Ver Actividad",
                        Toast.LENGTH_SHORT).show();
            }
        });
        */
        return view;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}
