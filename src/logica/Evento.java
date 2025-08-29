package logica;

import java.util.HashSet;
import java.util.Set;
import logica.DatatypesYEnum.DTFecha;

public class Evento {

    private String nombre;
    private String sigla;
    private String descripcion;
    private DTFecha fechaEvento;
    private Set<String> categorias;
    private Set<Edicion> ediciones;


    public Evento(String nom, String desc, DTFecha fecha, String sig, Set<Categoria> categoriaObjetos) {
        this.nombre = nom;
        this.descripcion = desc;
        this.fechaEvento = fecha;
        this.sigla = sig;
        this.ediciones = new HashSet<>();


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
    public Set<Edicion> getEdiciones() { return ediciones; }

    public void agregarEdicion(Edicion ed) {
        ediciones.add(ed);
    }
    


    @Override
    public String toString() {
        return nombre;
    }

    public Edicion NuevaEdicion(String nombre, String sigla, String ciudad, String pais, DTFecha inicio, DTFecha fin, DTFecha alta, Organizador org) {

    	Edicion edicion = new Edicion(nombre, sigla, ciudad, pais, inicio, fin, alta,org);

    	ediciones.add(edicion);

		return edicion;
    }

}




