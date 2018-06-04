package utn.frba.mobile.experienciaapp.Productor.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import utn.frba.mobile.experienciaapp.R;

import java.util.List;

import utn.frba.mobile.experienciaapp.models.Experiencia;

public class ExperienciaProductorAdapter extends RecyclerView.Adapter<ExperienciaProductorAdapter.ModelViewHolder>{

    private List<Experiencia> items;

    public ExperienciaProductorAdapter(List<Experiencia> items) {
        this.items = items;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public ExperienciaProductorAdapter.ModelViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ModelViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.productor_experiencia_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ExperienciaProductorAdapter.ModelViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    public class ModelViewHolder extends RecyclerView.ViewHolder{

        private TextView nombre;
        private TextView descripcion;
        private TextView fecha;

        public ModelViewHolder(View itemView) {
            super(itemView);
            this.nombre = itemView.findViewById(R.id.nombre);
            this.descripcion = itemView.findViewById(R.id.descripcion);
            this.fecha = itemView.findViewById(R.id.fecha);
        }

        public void bind(Experiencia model) {
            nombre.setText(model.getNombre());
            descripcion.setText(model.getDescripcion());
            fecha.setText(model.getFechaCreacion());
        }
    }



}
