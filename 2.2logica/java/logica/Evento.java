package logica;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import logica.DatatypesYEnum.DTFecha;
import logica.DatatypesYEnum.EstadoEdicion;

public class Evento {

    private String nombre;
    private String sigla;
    private String descripcion;
    private DTFecha fechaEvento;
    private Set<String> categorias;
    private Map<String, Edicion> ediciones;
    private String imagen;

    // Constructor sin imagen (mantiene compatibilidad)
    public Evento(String nom, String desc, DTFecha fecha, String sig, Set<Categoria> categoriaObjetos) {
        this(nom, desc, fecha, sig, categoriaObjetos, null);
    }

    // Constructor con imagen (opcional)
    public Evento(String nom, String desc, DTFecha fecha, String sig, Set<Categoria> categoriaObjetos, String imagen) {
        this.nombre = nom;
        this.descripcion = desc;
        this.fechaEvento = fecha;
        this.sigla = sig;
        this.ediciones = new HashMap<>();
        this.imagen = imagen;

        this.categorias = new HashSet<>();
        for (Categoria cat : categoriaObjetos) {
            this.categorias.add(cat.getNombre()); // asumiendo que Categoria tiene getNombre()
        }

    }

    public String getNombre() { return nombre; }
    public String getSigla() { return sigla; }
    public String getDescripcion() { return descripcion; }
    public DTFecha getFechaEvento() { return fechaEvento; }
    public Set<String> getCategorias() { return new HashSet<>(categorias); }
    public Set<String> getEdiciones() { return ediciones.keySet(); }
    public String getImagen() { return imagen; }
    
    public void setImagen(String imagen) { this.imagen = imagen; }
   
    public void agregarEdicion(Edicion edicion) {
        ediciones.put(edicion.getNombre(), edicion);
    }
    public Set<String> obtenerEdicionesPorEstado(EstadoEdicion estado) {
    	 Set<String> res = new HashSet<>();
    	 for (Edicion ed : ediciones.values()) {
    	        if (ed.getEstado() == estado) {
    	            res.add(ed.getNombre());
    	        }
    	 }
    	 return res;
    }


    @Override
    public String toString() {
        return nombre;
    }

    public Edicion nuevaEdicion(String nombre, String sigla, String ciudad, String pais, DTFecha inicio, DTFecha fin, DTFecha alta, Organizador org) {

    	Edicion edicion = new Edicion(nombre, sigla, ciudad, pais, inicio, fin, alta, org, this);

    	ediciones.put(edicion.getNombre(), edicion);

		return edicion;
    }

}




