package utn.frba.mobile.experienciaapp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Experiencia {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("nombre")
    @Expose
    private String nombre;
    @SerializedName("descripcion")
    @Expose
    private String descripcion;
    @SerializedName("longitud")
    @Expose
    private Object longitud;
    @SerializedName("latitud")
    @Expose
    private Object latitud;
    @SerializedName("precio")
    @Expose
    private String precio;
    @SerializedName("fecha_creacion")
    @Expose
    private String fechaCreacion;
    @SerializedName("visible")
    @Expose
    private String visible;
    @SerializedName("productor")
    @Expose
    private Productor productor;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Object getLongitud() {
        return longitud;
    }

    public void setLongitud(Object longitud) {
        this.longitud = longitud;
    }

    public Object getLatitud() {
        return latitud;
    }

    public void setLatitud(Object latitud) {
        this.latitud = latitud;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getVisible() {
        return visible;
    }

    public void setVisible(String visible) {
        this.visible = visible;
    }

    public Productor getProductor() {
        return productor;
    }

    public void setProductor(Productor productor) {
        this.productor = productor;
    }

}
