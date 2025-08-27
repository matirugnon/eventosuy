package logica.manejadores;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import logica.Categoria;
import logica.Edicion;
import logica.Evento;
import logica.Usuario;

public class ManejadorEventos {

	//coleccion Eventos, ediciones y categorias mapeados por su nombre
	private Map<String, Evento> eventos;
	private Map<String, Edicion> ediciones;
	private Map<String, Categoria> categorias;

	private static ManejadorEventos instancia = null;

	private ManejadorEventos() {
    	eventos = new HashMap<String, Evento>();

    }

	//singleton
    public static ManejadorEventos getinstance() {
        if (instancia == null)
            instancia = new ManejadorEventos();
        return instancia;
    }

    //add
    public void addEvento(Evento e) {
        String nombreEvento = e.getNombre(); //implementar
        eventos.put(nombreEvento, e);
    }

    public void addEdicion(Edicion e) {
        String nombreEdicion = e.getNombre(); //implementar
        ediciones.put(nombreEdicion, e);
    }

    public void addCategoria(Categoria c) {
        categorias.put(c.getNombre(), c);
    }



    //obtain
    public Evento obtenerEvento(String nombreEvento) {
        return ((Evento) eventos.get(nombreEvento));
    }
    
    public Edicion obtenerEdicion(String nombreEdicion) {
        return  ediciones.get(nombreEdicion);
    }
   
    
    //getters
    public Map<String, Evento> getEventos(){
    	return eventos;
    }
    
    public Map<String, Edicion> getEdiciones(){
    	return ediciones;
    }

    //exists
    public boolean existeCategoria(String cat){
    	return categorias.containsKey(cat);

    }
    	
}