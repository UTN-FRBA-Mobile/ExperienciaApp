package utn.frba.mobile.experienciaapp.experiencia;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import utn.frba.mobile.experienciaapp.models.Foto;
import utn.frba.mobile.experienciaapp.R;
import java.util.List;
import com.bumptech.glide.Glide;

public class FotoAdapter extends RecyclerView.Adapter<FotoAdapter.ModelViewHolder>{

    private List<Foto> items;

    private Context context;

    public FotoAdapter(List<Foto> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public FotoAdapter.ModelViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ModelViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.galeria_item, parent, false));
    }

    @Override
    public void onBindViewHolder(FotoAdapter.ModelViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    public class ModelViewHolder extends RecyclerView.ViewHolder{

        private TextView textView;
        private ImageView imageView;

        public ModelViewHolder(View itemView) {
            super(itemView);
            this.imageView = itemView.findViewById(R.id.image_view);
        }

        public void bind(Foto model) {
            //textView.setText(model.getTextId());
            //imageView.setImageResource(model.getImageId());
            Glide.with(context).load(model.getUrl()).into(imageView);
        }
    }

}