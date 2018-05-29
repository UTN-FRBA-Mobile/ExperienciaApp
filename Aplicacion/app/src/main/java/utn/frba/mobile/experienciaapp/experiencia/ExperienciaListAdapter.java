package utn.frba.mobile.experienciaapp.experiencia;

import android.content.Context;
import android.database.DataSetObserver;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;


import java.util.List;

import utn.frba.mobile.experienciaapp.R;
import utn.frba.mobile.experienciaapp.lib.utils.DownloadImageTask;
import utn.frba.mobile.experienciaapp.models.Experiencia;

public class ExperienciaListAdapter extends ArrayAdapter<Experiencia> {
    private Context context;
    private LayoutInflater layoutInflater;
    private List<Experiencia> experiencias;

    public ExperienciaListAdapter(Context context, List<Experiencia> experiencias) {
        super(context,-1,experiencias);
        this.context = context;
        this.experiencias = experiencias;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(this.getContext())
                    .inflate(R.layout.experiencia_item_of_list, parent, false);

            //viewHolder = new ViewHolder();
            //viewHolder.itemView = (TextView) convertView.findViewById(R.id.ItemView);

            //convertView.setTag(viewHolder);
        } else {
            //viewHolder = (ViewHolder) convertView.getTag();
        }

        Experiencia experienciaItem = getItem(position);
        if (experienciaItem!= null) {
            ImageView tumbIV = (ImageView) convertView.findViewById(R.id.tumbIV);
            ImageView star1IV = (ImageView) convertView.findViewById(R.id.star1IV);
            ImageView star2IV = (ImageView) convertView.findViewById(R.id.star2IV);
            ImageView star3IV = (ImageView) convertView.findViewById(R.id.star3IV);
            ImageView favTV = (ImageView) convertView.findViewById(R.id.favTV);
            TextView nombreTV = (TextView) convertView.findViewById(R.id.nombreTV);
            TextView descripcionTV = (TextView) convertView.findViewById(R.id.descripcionTV);
            TextView precioTV = (TextView) convertView.findViewById(R.id.precioTV);

            if(experienciaItem.getImagenes() != null && experienciaItem.getImagenes().size() > 0) {
                new DownloadImageTask(tumbIV).execute(experiencias.get(position).getImagenes().get(0));
            }else{
                //TODO Set Default Image
            }

            nombreTV.setText(experienciaItem.getNombre());
            descripcionTV.setText(experienciaItem.getDescripcion());
            precioTV.setText("$" + experienciaItem.getPrecio() + " por persona");
        }

        return convertView;
    }
}
