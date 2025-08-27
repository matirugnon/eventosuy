package logica.manejadores;

import java.util.HashMap;
import java.util.HashSet;
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
    public static ManejadorEventos getInstance() {
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

    	String nomCategoria = c.getNombre();
        categorias.put(nomCategoria, c);

    }



    //obtain
    
    public Edicion obtenerEdicion(String nombreEdicion) {
    	return ((Edicion) ediciones.get(nombreEdicion));
    }
    
    public Evento obtenerEvento(String nombreEvento) {
        return ((Evento) eventos.get(nombreEvento));
    }
    
    public Categoria obtenerCategoria(String nombreCategoria) {
    	return ((Categoria) categorias.get(nombreCategoria));
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
        // Copia defensiva para no exponer la vista interna del map
        return new HashSet<>(eventos.keySet());
    }
    
    

    //exists
    public boolean existeCategoria(String cat){
    	return categorias.containsKey(cat);

    }
    



}