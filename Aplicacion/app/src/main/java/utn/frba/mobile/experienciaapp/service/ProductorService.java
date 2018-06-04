package utn.frba.mobile.experienciaapp.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import utn.frba.mobile.experienciaapp.models.Experiencia;
import utn.frba.mobile.experienciaapp.models.Productor;

public class ProductorService {

    private static ProductorService instance=null;

    public static ProductorService getInstance(){
        if(instance==null){
            instance=new ProductorService();
        }
        return instance;
    }

    public Productor getMockProductor(Integer id) {
        Productor prod = new Productor();
        prod.setId(100);
        prod.setNombre("Roberto");
        prod.setApellido("Gómez Bolaños");
        prod.setCelular("+54 1512456154");
        prod.setDescripcion("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.\n Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo. Nemo enim ipsam voluptatem quia voluptas sit aspernatur aut odit aut fugit, sed quia consequuntur magni dolores eos qui ratione voluptatem sequi nesciunt. Neque porro quisquam est, qui dolorem ipsum quia dolor sit amet, consectetur, adipisci velit, sed quia non numquam eius modi tempora incidunt ut labore et dolore magnam aliquam quaerat voluptatem. Ut enim ad minima veniam, quis nostrum exercitationem ullam corporis suscipit laboriosam, nisi ut aliquid ex ea commodi consequatur? Quis autem vel eum iure reprehenderit qui in ea voluptate velit esse quam nihil molestiae consequatur, vel illum qui dolorem eum fugiat quo voluptas nulla pariatur?");
        prod.setDireccion("Av Presidente Preon 1243");
        prod.setDni("33562659");
        prod.setEmail("robertogomez@gmail.com");
        prod.setTelefono("43456754");
        return prod;
    }

    public List<Experiencia> getMockProductorExperiencias(Integer id)
    {

        List<Experiencia> lista = new ArrayList<Experiencia>();

        Experiencia exp = new Experiencia();

        exp.setNombre("Nombre1");
        exp.setDescripcion("Descripcion1");
        exp.setDireccion("Direccion 1");
        exp.setFechaCreacion("15/01/1990");
        lista.add(exp);

        exp = new Experiencia();
        exp.setNombre("Nombre2");
        exp.setDescripcion("Descripcion2");
        exp.setDireccion("Direccion 2");
        exp.setFechaCreacion("15/01/1991");
        lista.add(exp);

        exp = new Experiencia();
        exp.setNombre("Nombre3");
        exp.setDescripcion("Descripcion3");
        exp.setDireccion("Direccion 3");
        exp.setFechaCreacion("15/01/1993s");
        lista.add(exp);

        return  lista;





    }
}
