package logica.manejadores;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import logica.Categoria;
import logica.Edicion;
import logica.Evento;
import logica.datatypesyenum.DTEvento;
import logica.datatypesyenum.EstadoEdicion;

public class ManejadorEventos {

	//coleccion Eventos, ediciones y categorias mapeados por su nombre
	private Map<String, Evento> eventos;
	private Map<String, Edicion> ediciones;
	private Map<String, Categoria> categorias;

	private static ManejadorEventos instancia = null;

	private ManejadorEventos() {
    	eventos = new HashMap<String, Evento>();
    	categorias = new HashMap<>();
    	ediciones = new HashMap<>();
    }

	//singleton
    public static ManejadorEventos getInstance() {
        if (instancia == null)
            instancia = new ManejadorEventos();
        return instancia;
    }

    //add
    public void addEvento(Evento evento) {
        String nombreEvento = evento.getNombre(); //implementar
        eventos.put(nombreEvento, evento);
    }

    public void addEdicion(Edicion edicion) {
        String nombreEdicion = edicion.getNombre(); //implementar
        ediciones.put(nombreEdicion, edicion);
    }

    public void addCategoria(Categoria cat) {

    	String nomCategoria = cat.getNombre();
        categorias.put(nomCategoria, cat);

    }



    //obtain


    public Evento obtenerEvento(String nombreEvento) {
        return (Evento) eventos.get(nombreEvento);
    }


    public Edicion obtenerEdicion(String nombreEdicion) {
        return  ediciones.get(nombreEdicion);
    }




    //getters


    public Map<String, Edicion> getEdiciones(){
    	return ediciones;
    }

    public Categoria obtenerCategoria(String nombreCategoria) {
    	return (Categoria) categorias.get(nombreCategoria);
    }

    public boolean existe(String nomEvento) {
        return this.obtenerEvento(nomEvento) != null;
    }

    public Set<Categoria> getCategorias(Set<String> categ) {
        Set<Categoria> result = new HashSet<>();

        for (String nombre : categ) {
            Categoria cat = this.obtenerCategoria(nombre);
            if (cat != null) {   // por si no existe
                result.add(cat);
            }
        }

        return result;
    }


    public Set<String> listarEventos() {

        return new HashSet<>(eventos.keySet());
    }

    public Set<String> getNombresCategorias() {

		return categorias.keySet();
	}



    public Set<DTEvento> getDTEventos() {
		Set<DTEvento> res = new HashSet<>();
		for (Evento e : eventos.values()) {
			res.add(new DTEvento(e));
		}
		return res;
	}
    //exists
    public boolean existeCategoria(String cat){
    	return categorias.containsKey(cat);

    }


    public boolean existeEdicion(String nomEdicion) {
        return ediciones.containsKey(nomEdicion);
    }

    public Set<Edicion> obtenerEdicionesPorEstado(EstadoEdicion estado) {
        Set<Edicion> resultado = new HashSet<>();
        for (Edicion ed : ediciones.values()) {
            if (ed.getEstado() == estado) {
                resultado.add(ed);
            }
        }
        return resultado;
    }

}