package utn.frba.mobile.experienciaapp.experiencia.puntos_interes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

import utn.frba.mobile.experienciaapp.R;
import utn.frba.mobile.experienciaapp.lib.utils.DownloadImageTask;
import utn.frba.mobile.experienciaapp.models.PuntoInteres;

public class PuntosDeInteresListAdapter extends ArrayAdapter<PuntoInteres> {
    private Context context;
    private LayoutInflater layoutInflater;
    private List<PuntoInteres> puntosDeInteres;

    public PuntosDeInteresListAdapter(Context context, List<PuntoInteres> experiencias) {
        super(context,-1,experiencias);
        this.context = context;
        this.puntosDeInteres = experiencias;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(this.getContext())
                    .inflate(R.layout.punto_interes_item_of_list, parent, false);
        }

        PuntoInteres puntoInteresItem = getItem(position);
        if (puntoInteresItem!= null) {
            ImageView tumbIV = (ImageView) convertView.findViewById(R.id.tumbIV);
            TextView nombreTV = (TextView) convertView.findViewById(R.id.nombreTV);
            TextView descripcionTV = (TextView) convertView.findViewById(R.id.descripcionTV);

            if(puntoInteresItem.getImagenes() != null && puntoInteresItem.getImagenes().size() > 0) {
                new DownloadImageTask(tumbIV).execute(puntosDeInteres.get(position).getImagenes().get(0));
            }else{
                //TODO Set Default Image
            }

            nombreTV.setText(puntoInteresItem.getNombre());
            descripcionTV.setText(puntoInteresItem.getDescripcion());
        }

        return convertView;
    }
}