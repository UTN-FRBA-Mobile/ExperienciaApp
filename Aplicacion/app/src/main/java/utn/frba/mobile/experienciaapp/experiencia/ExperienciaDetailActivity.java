package utn.frba.mobile.experienciaapp.experiencia;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import utn.frba.mobile.experienciaapp.Productor.ProductorActivity;
import utn.frba.mobile.experienciaapp.R;
import utn.frba.mobile.experienciaapp.models.Experiencia;
import utn.frba.mobile.experienciaapp.models.Productor;

public class ExperienciaDetailActivity extends AppCompatActivity {

    ViewPager viewPagerPhotoSlider;
    Experiencia experiencia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_experiencia_detail);

        Intent intent = getIntent();
        Integer id = intent.getIntExtra("id", 0);

        experiencia = this.getMockExperiencia(id);
        setTitle(experiencia.getNombre());

        viewPagerPhotoSlider = (ViewPager) findViewById(R.id.photoSlider);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this, experiencia.getImagenes());
        viewPagerPhotoSlider.setAdapter(viewPagerAdapter);

        this.setLayoutTexts(experiencia);

    }

    private void setLayoutTexts(Experiencia exp){
        setTxtTextView(R.id.fecha, exp.getFechaCreacion());
        setTxtTextView(R.id.hora, exp.getDuracion() + " hs");
        setTxtTextView(R.id.precio, exp.getPrecio());
        setTxtTextView(R.id.descripcion, exp.getDescripcion());
        setTxtTextView(R.id.direccion, exp.getDireccion());
        setTxtTextView(R.id.productor, exp.getProductor().getNombre() + " " + exp.getProductor().getApellido());
    }

    private void setTxtTextView(int id, String text){
        TextView myTextView= (TextView) findViewById(id);
        myTextView.setText(text);
    }

    private Experiencia getMockExperiencia(Integer id){
        Experiencia exp = new Experiencia();
        exp.setNombre("Pirámides de egipto");
        exp.setDescripcion("Las pirámides de Egipto son, de todos los vestigios legados por egipcios de la antigüedad, los más portentosos y emblemáticos monumentos de esta civilización, y en particular, las tres grandespirámides de Giza, las tumbas o cenotafios de los faraones Keops, Kefrén y Micerino, cuya construcción se remonta, para la gran mayoría de estudiosos, al periodo denominado Imperio Antiguo de Egipto. La Gran Pirámide de Giza, construida por Keops (Jufu), es una de lasSiete Maravillas del Mundo Antiguo, además de ser la única que aún perdura. Su visita guiada comienza con una fascinante introducción de cada una de las tres pirámides de Gizeh: la de Keops, la de Kefrén y la de Micerinos. Dispondrá de tiempo libre para entrar a una de las pirámides (coste adicional), aunque a su guía no le estará permitido entrar con usted." +
                "Un corto trayecto por carretera hacia el lado de la meseta más cercano a la ciudad le lleva a los pies de la esfinge, el enigmático símbolo de Egipto. También en Gizeh puede visitar el Museo de la Barca Solar (opcional), que alberga la magníficamente bien conservada barca funeraria de Keops.");
        exp.setFechaCreacion("09/05/2018");
        exp.setPrecio("1200");
        exp.setDireccion("Av Siempreviva 4530, El Cairo");
        exp.setDuracion("2");

        ArrayList imagenes = new ArrayList();
        imagenes.add("https://sobrehistoria.com/wp-content/uploads/2016/03/las-piramides-de-egipto-portada-600x429.jpg");
        imagenes.add("https://sobrehistoria.com/wp-content/uploads/2016/03/las-piramides-de-egipto-giza-600x337.jpg");
        imagenes.add("https://sobrehistoria.com/wp-content/uploads/2016/03/las-piramides-de-egipto-pesado-del-corazon-600x350.jpg");
        imagenes.add("https://sobrehistoria.com/wp-content/uploads/2016/03/las-piramides-de-egipto-piramides.jpg");
        imagenes.add("https://sobrehistoria.com/wp-content/uploads/2016/03/las-piramides-de-egipto-ciudad-constructores-600x450.jpg");

        exp.setImagenes(imagenes);

        Productor productor = new Productor();
        productor.setId(100);
        productor.setNombre("Roberto");
        productor.setApellido("Gómez Bolaños");

        exp.setProductor(productor);

        return exp;
    }

    public void viewItinerary(View view){
        Toast.makeText(ExperienciaDetailActivity.this.getApplicationContext(),
                "Mostrar Ininerario",
                Toast.LENGTH_SHORT).show();
    }

    public void verProductor(View view){
        Intent myIntent = new Intent(ExperienciaDetailActivity.this, ProductorActivity.class);
        myIntent.putExtra("id",experiencia.getProductor().getId()); //Optional parameters
        ExperienciaDetailActivity.this.startActivity(myIntent);
    }
}