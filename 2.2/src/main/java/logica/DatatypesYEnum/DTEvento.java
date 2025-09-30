package logica.DatatypesYEnum;

import java.util.Set;

import logica.Evento;

public class DTEvento {
    private final String nombre;
    private final String sigla;
    private final String descripcion;
    private final DTFecha fechaEvento;
    private final Set<String> categorias;

    public DTEvento(String nombre, String sigla, String descripcion, DTFecha fechaEvento, Set<String> categorias) {
        this.nombre = nombre;
        this.sigla = sigla;
        this.descripcion = descripcion;
        this.fechaEvento = fechaEvento;
        this.categorias = categorias;
    }

    // Si querés construir desde un Evento:
    public DTEvento(Evento e) {
        this.nombre = e.getNombre();
        this.sigla = e.getSigla();
        this.descripcion = e.getDescripcion();
        this.fechaEvento = e.getFechaEvento();
		this.categorias = e.getCategorias();
    }

    public String getNombre() { return nombre; }
    public String getSigla() { return sigla; }
    public String getDescripcion() { return descripcion; }
    public DTFecha getFechaEvento() { return fechaEvento; }
    public Set<String> getCategorias() {return categorias;}
}