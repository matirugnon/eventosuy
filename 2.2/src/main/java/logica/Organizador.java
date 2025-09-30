package logica;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import java.util.Set;

import logica.DatatypesYEnum.DTOrganizador;
import logica.DatatypesYEnum.DTUsuario;

public class Organizador extends Usuario {

    private String descripcion;
    private String link;
    private Map<String, Edicion> ediciones;


    public Organizador(String nickname, String nombre, String correo, String descripcion, String link, String password, String avatar) {
        super(nickname, nombre, correo, password, avatar); // Actualizado para incluir avatar
        this.descripcion = descripcion;
        this.link = link;

        this.ediciones = new HashMap<>();

    }

    public String getDescripcion() { return descripcion; }
    public String getLink() { return link; }

    @Override
    public String getTipo() {
        return "organizador";
    }

	public void setDescripcion(String desc) {
		this.descripcion = desc;
	}

	public void setLink(String link) {
		this.link = link;
	}


	public void agregarEdicion(Edicion edicion) {
	    this.ediciones.put(edicion.getNombre(), edicion);
	}

	// Obtener una edición por nombre
	public Edicion obtenerEdicion(String nombre) {
	    return this.ediciones.get(nombre);
	}

	// Obtener todas las ediciones como Set<Edicion> (para mostrar en listas, etc.)
	public Set<Edicion> getEdiciones() {

	    return new HashSet<>(ediciones.values()); // devuelve copia del conjunto
	}

	public Set<String> getNombresEdiciones() {
	    return ediciones.keySet(); // devuelve copia del conjunto
	}


	// Eliminar una edición
	public void removerEdicion(String nombre) {
	    this.ediciones.remove(nombre);
	}

	// Verificar si tiene una edición
	public boolean tieneEdicion(String nombre) {
	    return this.ediciones.containsKey(nombre);
	}

	public DTUsuario getDTOrganizador() {
		return new DTOrganizador(nickname, nombre, correo, password, descripcion, link, avatar); // Incluir avatar
	}


}
