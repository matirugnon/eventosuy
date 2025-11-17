package logica.DatatypesYEnum;

import java.util.Set;

public class DTSeleccionEvento {
    private DTEvento evento;
    private Set<String> nombresCategorias;
    private Set<String> nombresEdiciones;

    public DTSeleccionEvento(DTEvento evento, Set<String> categorias, Set<String> ediciones) {
        this.evento = evento;
        this.nombresCategorias = categorias;
        this.nombresEdiciones = ediciones;
    }

    public DTEvento getEvento() { return evento; }
    public Set<String> getCategorias() { return nombresCategorias; }
    public Set<String> getEdiciones() { return nombresEdiciones; }
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
}
