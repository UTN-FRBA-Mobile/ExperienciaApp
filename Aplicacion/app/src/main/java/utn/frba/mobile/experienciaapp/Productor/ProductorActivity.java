package utn.frba.mobile.experienciaapp.Productor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.net.Uri;

import utn.frba.mobile.experienciaapp.R;
import utn.frba.mobile.experienciaapp.models.Productor;
import utn.frba.mobile.experienciaapp.service.ProductorService;

public class ProductorActivity extends AppCompatActivity {

    Productor productor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productor);

        Intent intent = getIntent();
        Integer id = intent.getIntExtra("id", 0);

        //productor = getMockProductor(id);

        productor = ProductorService.getInstance().getMockProductor(id);
        setTitle(productor.getNombre() + " " + productor.getApellido());

        setLayoutTexts(productor);

    }

    private void setLayoutTexts(Productor p) {
        setTxtTextView(R.id.nombre, productor.getNombre() + " " + productor.getApellido());
        setTxtTextView(R.id.direccion, p.getDireccion());
        setTxtTextView(R.id.telefono, p.getTelefono());
        setTxtTextView(R.id.celular, p.getCelular());
        setTxtTextView(R.id.email, p.getEmail());
        setTxtTextView(R.id.descripcion, p.getDescripcion());
        setTxtTextView(R.id.dni, p.getDni());

        RatingBar ratingBar = (RatingBar) findViewById(R.id.calificacion);
        ratingBar.setNumStars(3);

    }

    private void setTxtTextView(int id, String text) {
        TextView myTextView = (TextView) findViewById(id);
        myTextView.setText(text);
    }


    public void verFotos(View view) {
        //Toast.makeText(ProductorActivity.this.getApplicationContext(), "Mostrar Fotos", Toast.LENGTH_SHORT).show();
        Intent myIntent = new Intent(ProductorActivity.this, ProductorGaleriaActivity.class);
        ProductorActivity.this.startActivity(myIntent);
    }

    public void verExperiencias(View view) {
        //Toast.makeText(ProductorActivity.this.getApplicationContext(), "Mostrar Experiencias", Toast.LENGTH_SHORT).show();
        Intent myIntent = new Intent(ProductorActivity.this, ProductorExperienciasActivity.class);
        ProductorActivity.this.startActivity(myIntent);
    }

    public void llamarTelefono(View view) {
        TextView textView = (TextView) view;
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:" + textView.getText()));
        startActivity(callIntent);
    }

    public void llamarCelular(View view) {
        TextView textView = (TextView) view;
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:" + textView.getText()));
        startActivity(callIntent);
    }

    public void enviarEmail(View view) {
        TextView textView = (TextView) view;
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        String[] to = {textView.getText().toString()};
        intent.putExtra(Intent.EXTRA_EMAIL, to);
        intent.putExtra(Intent.EXTRA_SUBJECT, "Consulta de Actvidad");
        intent.putExtra(Intent.EXTRA_TEXT, " ");
        //intent.setType("message/rfc822");
        Intent chooser = Intent.createChooser(intent, "Send Email...");
        startActivity(chooser);
    }

}