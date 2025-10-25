package logica.datatypesyenum;

import java.io.Serializable;
import java.util.Set;

import logica.Evento;

public class DTEvento implements Serializable{
	private static final long serialVersionUID = 1L;
	
    private String nombre;
    private String sigla;
    private String descripcion;
    private DTFecha fechaEvento;
    private Set<String> categorias;
    private String imagen;

    public DTEvento() {}
    
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

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public void setFechaEvento(DTFecha fechaEvento) {
		this.fechaEvento = fechaEvento;
	}

	public void setCategorias(Set<String> categorias) {
		this.categorias = categorias;
	}

	public void setImagen(String imagen) {
		this.imagen = imagen;
	}
    
}