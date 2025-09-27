package logica.DatatypesYEnum;

import logica.Evento;

public class DTEvento {
    private final String nombre;
    private final String sigla;
    private final String descripcion;
    private final DTFecha fechaEvento;

    public DTEvento(String nombre, String sigla, String descripcion, DTFecha fechaEvento) {
        this.nombre = nombre;
        this.sigla = sigla;
        this.descripcion = descripcion;
        this.fechaEvento = fechaEvento;
    }

    // Si querés construir desde un Evento:
    public DTEvento(Evento e) {
        this.nombre = e.getNombre();
        this.sigla = e.getSigla();
        this.descripcion = e.getDescripcion();
        this.fechaEvento = e.getFechaEvento();
    }

    public String getNombre() { return nombre; }
    public String getSigla() { return sigla; }
    public String getDescripcion() { return descripcion; }
    public DTFecha getFechaEvento() { return fechaEvento; }
}