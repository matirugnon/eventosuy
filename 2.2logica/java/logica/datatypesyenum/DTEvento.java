package logica.datatypesyenum;

import java.util.Set;

import logica.Evento;

public class DTEvento {
    private final String nombre;
    private final String sigla;
    private final String descripcion;
    private final DTFecha fechaEvento;
    private final Set<String> categorias;
    private final String imagen;

    public DTEvento(String nombre, String sigla, String descripcion, DTFecha fechaEvento, Set<String> categorias) {
        this.nombre = nombre;
        this.sigla = sigla;
        this.descripcion = descripcion;
        this.fechaEvento = fechaEvento;
        this.categorias = categorias;
        this.imagen = null;
    }

    public DTEvento(String nombre, String sigla, String descripcion, DTFecha fechaEvento, Set<String> categorias, String imagen) {
        this.nombre = nombre;
        this.sigla = sigla;
        this.descripcion = descripcion;
        this.fechaEvento = fechaEvento;
        this.categorias = categorias;
        this.imagen = imagen;
    }

    // Si querés construir desde un Evento:
    public DTEvento(Evento event) {
        this.nombre = event.getNombre();
        this.sigla = event.getSigla();
        this.descripcion = event.getDescripcion();
        this.fechaEvento = event.getFechaEvento();
		this.categorias = event.getCategorias();
        this.imagen = event.getImagen();
    }

    public String getNombre() { return nombre; }
    public String getSigla() { return sigla; }
    public String getDescripcion() { return descripcion; }
    public DTFecha getFechaEvento() { return fechaEvento; }
    public Set<String> getCategorias() {return categorias; }
    public String getImagen() { return imagen; }
}