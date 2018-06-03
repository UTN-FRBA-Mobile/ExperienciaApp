package utn.frba.mobile.experienciaapp.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import utn.frba.mobile.experienciaapp.models.Experiencia;
import utn.frba.mobile.experienciaapp.models.FechaExperiencia;
import utn.frba.mobile.experienciaapp.models.Reserva;
import utn.frba.mobile.experienciaapp.models.Turista;


public class AgendaService {

    private static AgendaService instance=null;

    private AgendaService(){
        //private constrcutor
    }

    public static AgendaService getInstance(){
        if(instance==null){
            instance=new AgendaService();
        }
        return instance;
    }

    public List<Reserva> getMisReservas(){
        List<Reserva> reservas=new ArrayList<Reserva>();
        reservas.add(createReserva());
        reservas.add(createReserva());
        reservas.add(createReserva());
        reservas.add(createReserva());
        return reservas;
    }

    private Reserva createReserva() {
        Reserva reserva=new Reserva();
        reserva.setCantidadPersonas(2);
        reserva.setCodigo("362178931908");
        reserva.setTotal(4444d);
        FechaExperiencia fechaExperiencia=new FechaExperiencia();
        fechaExperiencia.setFechaHora(new Date());
        fechaExperiencia.setExperiencia(createExperience());
        reserva.setFechaExperiencia(fechaExperiencia);


        return reserva;
    }

    private Experiencia createExperience(){
        Experiencia exp = new Experiencia();
        exp.setNombre("Pirámides de egipto");
        exp.setDescripcion("Las pirámides de Egipto son, de todos los vestigios legados por egipcios de la antigüedad, los más portentosos y emblemáticos monumentos de esta civilización, y en particular, las tres grandespirámides de Giza, las tumbas o cenotafios de los faraones Keops, Kefrén y Micerino, cuya construcción se remonta, para la gran mayoría de estudiosos, al periodo denominado Imperio Antiguo de Egipto. La Gran Pirámide de Giza, construida por Keops (Jufu), es una de lasSiete Maravillas del Mundo Antiguo, además de ser la única que aún perdura. Su visita guiada comienza con una fascinante introducción de cada una de las tres pirámides de Gizeh: la de Keops, la de Kefrén y la de Micerinos. Dispondrá de tiempo libre para entrar a una de las pirámides (coste adicional), aunque a su guía no le estará permitido entrar con usted." +
                "Un corto trayecto por carretera hacia el lado de la meseta más cercano a la ciudad le lleva a los pies de la esfinge, el enigmático símbolo de Egipto. También en Gizeh puede visitar el Museo de la Barca Solar (opcional), que alberga la magníficamente bien conservada barca funeraria de Keops.");
        exp.setFechaCreacion("09/05/2018");
        exp.setPrecio("1200");
        exp.setDireccion("Av Siempreviva 4530, El Cairo");
        exp.setDuracion("2 dias");

        ArrayList imagenes = new ArrayList();
        imagenes.add("https://sobrehistoria.com/wp-content/uploads/2016/03/las-piramides-de-egipto-portada-600x429.jpg");
        imagenes.add("https://sobrehistoria.com/wp-content/uploads/2016/03/las-piramides-de-egipto-giza-600x337.jpg");
        imagenes.add("https://sobrehistoria.com/wp-content/uploads/2016/03/las-piramides-de-egipto-pesado-del-corazon-600x350.jpg");
        imagenes.add("https://sobrehistoria.com/wp-content/uploads/2016/03/las-piramides-de-egipto-piramides.jpg");
        imagenes.add("https://sobrehistoria.com/wp-content/uploads/2016/03/las-piramides-de-egipto-ciudad-constructores-600x450.jpg");

        exp.setImagenes(imagenes);
        return exp;
    }
}
