package logica.datatypesyenum;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import logica.Evento;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;

public class DTEvento implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String nombre;
    private String sigla;
    private String descripcion;
    private DTFecha fechaEvento;
    private List<String> categorias; // ahora es List
    private String imagen;

    public DTEvento() {
        this.categorias = new ArrayList<>();
    }

    public DTEvento(String nombre, String sigla, String descripcion, DTFecha fechaEvento, List<String> categorias) {
        this(nombre, sigla, descripcion, fechaEvento, categorias, null);
    }

    public DTEvento(String nombre, String sigla, String descripcion, DTFecha fechaEvento, List<String> categorias, String imagen) {
        this.nombre = nombre;
        this.sigla = sigla;
        this.descripcion = descripcion;
        this.fechaEvento = fechaEvento;
        this.categorias = categorias != null ? new ArrayList<>(categorias) : new ArrayList<>();
        this.imagen = imagen;
    }

    public DTEvento(Evento event) {
        this.nombre = event.getNombre();
        this.sigla = event.getSigla();
        this.descripcion = event.getDescripcion();
        this.fechaEvento = event.getFechaEvento();
        this.categorias = new ArrayList<>(event.getCategorias()); // convertir Set a List
        this.imagen = event.getImagen();
    }

    // Getters normales
    public String getNombre() { return nombre; }
    public String getSigla() { return sigla; }
    public String getDescripcion() { return descripcion; }
    public DTFecha getFechaEvento() { return fechaEvento; }
    public String getImagen() { return imagen; }

    // Setters
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setSigla(String sigla) { this.sigla = sigla; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public void setFechaEvento(DTFecha fechaEvento) { this.fechaEvento = fechaEvento; }
    public void setImagen(String imagen) { this.imagen = imagen; }

    // Getter/Setter para la lista (JAXB-friendly)
    @XmlElementWrapper(name = "categorias")
    @XmlElement(name = "categoria")
    public List<String> getCategorias() {
        return categorias != null ? categorias : new ArrayList<>();
    }

    public void setCategorias(List<String> categorias) {
        this.categorias = categorias != null ? new ArrayList<>(categorias) : new ArrayList<>();
    }
}

