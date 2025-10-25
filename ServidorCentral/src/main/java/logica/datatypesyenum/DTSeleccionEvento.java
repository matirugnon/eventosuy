package logica.datatypesyenum;

import java.io.Serializable;
import java.util.Set;

public class DTSeleccionEvento implements Serializable {
	private static final long serialVersionUID = 1L;

	
    private DTEvento evento;
    private Set<String> nombresCategorias;
    private Set<String> nombresEdiciones;
    private Set<DTEdicion> edicionesCompletas;

    public DTSeleccionEvento() {}
    
    public DTSeleccionEvento(DTEvento evento, Set<String> categorias, Set<String> ediciones) {
        this.evento = evento;
        this.nombresCategorias = categorias;
        this.nombresEdiciones = ediciones;
        this.edicionesCompletas = null;
    }

    public DTSeleccionEvento(DTEvento evento, Set<String> categorias, Set<String> ediciones, Set<DTEdicion> edicionesCompletas) {
        this.evento = evento;
        this.nombresCategorias = categorias;
        this.nombresEdiciones = ediciones;
        this.edicionesCompletas = edicionesCompletas;
    }

    public DTEvento getEvento() { return evento; }
    public Set<String> getCategorias() { return nombresCategorias; }
    public Set<String> getEdiciones() { return nombresEdiciones; }
    public Set<DTEdicion> getEdicionesCompletas() { return edicionesCompletas; }
    public String getNombre() {
    	return evento.getNombre();
    }
    public String getSigla() {
    	return evento.getSigla();
    }
    public String getDescripcion() {
    	return evento.getDescripcion();
    }
    
    public DTFecha getFechaEvento() {
    	return evento.getFechaEvento();
    }

	public Set<String> getNombresCategorias() {
		return nombresCategorias;
	}

	public void setNombresCategorias(Set<String> nombresCategorias) {
		this.nombresCategorias = nombresCategorias;
	}

	public Set<String> getNombresEdiciones() {
		return nombresEdiciones;
	}

	public void setNombresEdiciones(Set<String> nombresEdiciones) {
		this.nombresEdiciones = nombresEdiciones;
	}

	public void setEvento(DTEvento evento) {
		this.evento = evento;
	}

	public void setEdicionesCompletas(Set<DTEdicion> edicionesCompletas) {
		this.edicionesCompletas = edicionesCompletas;
	}
    
    
}
