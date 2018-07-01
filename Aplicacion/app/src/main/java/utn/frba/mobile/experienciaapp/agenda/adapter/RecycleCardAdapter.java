package utn.frba.mobile.experienciaapp.agenda.adapter;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.concurrent.ExecutionException;

import utn.frba.mobile.experienciaapp.R;
import utn.frba.mobile.experienciaapp.experiencia.ExperienciaDetailActivity;
import utn.frba.mobile.experienciaapp.experiencia.ProductorActivity;
import utn.frba.mobile.experienciaapp.lib.ws.ReciveResponseWS;
import utn.frba.mobile.experienciaapp.lib.ws.ResponseWS;
import utn.frba.mobile.experienciaapp.lib.ws.WSRetrofit;
import utn.frba.mobile.experienciaapp.models.Experiencia;
import utn.frba.mobile.experienciaapp.models.FechaExperiencia;
import utn.frba.mobile.experienciaapp.models.Reserva;
import utn.frba.mobile.experienciaapp.models.Turista;

public class RecycleCardAdapter extends RecyclerView.Adapter<RecycleCardAdapter.CardViewHolder> {
    private static final String TAG = "RecycleCardAdapter";

    private List<Reserva> reservas;
    private Context context;

    public RecycleCardAdapter(Context context, List<Reserva> reservas){
        this.reservas=reservas;
        this.context=context;
    }


    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_agenda_reservas,parent,false);
        CardViewHolder cardViewHolder=new CardViewHolder(view);
        return cardViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final CardViewHolder holder, final int position) {
        Reserva reserva=reservas.get(position);
        holder.position=position;
        FechaExperiencia fechaExperiencia=reserva.getFechaExperiencia();
        Experiencia experiencia=fechaExperiencia.getExperiencia();
        ViewPageAdapter viewPageAdapter=new ViewPageAdapter(this.context,experiencia.getImagenes());
        holder.imagenIV.setAdapter(viewPageAdapter);
        holder.tituloTV.setText(experiencia.getNombre());
        holder.direccion.setText(experiencia.getDireccion());
        holder.fecha.setText(fechaExperiencia.getFechaHora());
        holder.precio.setText(reserva.getTotal().toString());
        holder.mensajeTV.setVisibility(reservas==null || reservas.isEmpty()? View.VISIBLE:View.INVISIBLE);
        holder.eliminarReservaB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EliminarReservaData data=new EliminarReservaData();
                try {
                    boolean eliminado=new BorrarReservaTask(context).execute(data).get();
                    if(eliminado){
                        reservas.remove(position);
                        notifyDataSetChanged();
                        Toast.makeText(v.getContext(),"Reserva eliminada", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(v.getContext(),"No se pudo eliminar la reserva.", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    throw new IllegalStateException(e);
                }
            }
        });
//        StringBuilder infoExp=new StringBuilder();
//        infoExp.append("Precio: $").append(reserva.getTotal()).append("\nDuración: ").append(experiencia.getDuracion());
//        holder.infoTV.setText(infoExp.toString());
    }




    @Override
    public int getItemCount() {
        return this.reservas.size();
    }

    public static  class CardViewHolder extends RecyclerView.ViewHolder {

        private int position;
        private ViewPager imagenIV;
        private TextView tituloTV,infoTV,direccion,precio,fecha,mensajeTV;
        private Button eliminarReservaB;

        public CardViewHolder(View itemView) {
            super(itemView);
            initComponenteReference();
        }

        private void initComponenteReference() {
            this.imagenIV = itemView.findViewById(R.id.imagenIV);
            this.tituloTV = itemView.findViewById(R.id.tituloTV);
           // this.infoTV = itemView.findViewById(R.id.infoTV);
            this.direccion = itemView.findViewById(R.id.direccion);
            this.precio = itemView.findViewById(R.id.precio);
            this.fecha = itemView.findViewById(R.id.fecha);
            this.mensajeTV=itemView.findViewById(R.id.mensajeTV);
            this.eliminarReservaB=itemView.findViewById(R.id.eliminarReservaB);
        }
    }

    private class EliminarReservaData{
        private Turista turista;
        private Reserva reserva;

        public Turista getTurista() {
            return turista;
        }

        public void setTurista(Turista turista) {
            this.turista = turista;
        }

        public Reserva getReserva() {
            return reserva;
        }

        public void setReserva(Reserva reserva) {
            this.reserva = reserva;
        }
    }

    private class BorrarReservaTask extends AsyncTask<EliminarReservaData,Void,Boolean> implements ReciveResponseWS {

        private Context context;
        private boolean fueEliminado=false;
        public BorrarReservaTask(Context context) {
            this.context = context;
        }


        @Override
        protected Boolean doInBackground(EliminarReservaData... datas) {
            EliminarReservaData data=datas[0];
            try {
                WSRetrofit.ParseResponseWS(Reserva.BorrarReservaTurista(data.getTurista().getId(),data.getTurista().getLoginToken(),data.getReserva().getId()).execute(),this, ELIMINAR_RESERVA);

            }catch (Exception e){
                throw new IllegalStateException(e);
            }
            return fueEliminado;
        }


        @Override
        public void ReciveResponseWS(ResponseWS responseWS, int accion) {
            switch(accion){
                //TODO: DELETE SOLO PARA TEST
                case ELIMINAR_RESERVA:{
                    if(responseWS != null && responseWS.getResult() != null && responseWS.getResult().size() == 1 && responseWS.getResult().get(0) instanceof Turista){
                        fueEliminado=true;
                    }else{
                        //Do somthing
                        fueEliminado=false;
                        Log.w(TAG,"Reserva no satisfactoria, "+responseWS.getMsg());
                    }
                    break;
                }


                default:{
                    fueEliminado=false;
                    Log.d(TAG,"ReciveResponseWS accion no identificada: " + accion);
                }
            }
        }
    }
}
