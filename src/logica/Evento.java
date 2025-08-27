package logica;

import java.util.Set;

import logica.DatatypesYEnum.DTFecha;

public class Evento {

    private String nombre;
    private String sigla;
    private String descripcion;
    private DTFecha fechaEvento;
    private Set<Categoria> categorias;  
    private Set<Edicion> ediciones;

    public Evento(String nom, String desc, DTFecha fecha, String sig, Set<Categoria> categorias) {
        this.nombre = nom;
        this.descripcion = desc;
        this.fechaEvento = fecha;
        this.sigla = sig;
        this.categorias = categorias;
    }

    public String getNombre() {
        return nombre;
    }

    public String getSigla() {
        return sigla;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public DTFecha getFechaEvento() {
        return fechaEvento;
    }

    public Set<Categoria> getCategorias() {
        return categorias;
    }

	public Set<Edicion> getEdiciones() {
		return ediciones;

	}
}



