package logica.manejadores;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import logica.Asistente;
import logica.Institucion;
import logica.Usuario;

/**
 * Clase que conserva la colección global de los usuarios del sistema.
 * Los usuarios se identifican por su NICKNAME
 * Se implementa en base al patrón Singleton.
 */

//Adaptacion del modelo presentado de demoswing

public class ManejadorUsuario {

	//hasmap que linkea un nickname con un usuario
    private Map<String, Usuario> usuariosNick;

    private Map<String, Institucion> instituciones;

    private static ManejadorUsuario instancia = null;

    private ManejadorUsuario() {
    	usuariosNick = new HashMap<String, Usuario>();
    	instituciones = new HashMap<>();
    }

    //singleton
    public static ManejadorUsuario getinstance() {
        if (instancia == null)
            instancia = new ManejadorUsuario();
        return instancia;
    }

    public void addUsuario(Usuario usu) {
        String nick = usu.getNickname(); //implementar
        usuariosNick.put(nick, usu);
    }

    public Usuario obtenerUsuario(String nick) {
        return ((Usuario) usuariosNick.get(nick));
    }

    public Set<Usuario> getUsuarios() {
        return new HashSet<>(usuariosNick.values());
    }

    public Set<String> obtenerNicksOrganizadores() {
        Set<String> res = new HashSet<>();
        for (Usuario u : usuariosNick.values()) {
            if (u instanceof logica.Organizador) {   // ajustá el paquete si difiere
                res.add(u.getNickname());
            }
        }
        return res;  // si no hay, vuelve vacío
    }

    public Set<String> getNickAsistentes(){
    	Set<String> nickAsistentes = new HashSet<>();
    	for (Usuario us : usuariosNick.values()) {
    		if (us instanceof Asistente as) {
    			nickAsistentes.add(as.getNickname());
    		}
    	}
    	return nickAsistentes;
    }

    public void addInstitucion(Institucion ins) {
    	String nombre = ins.getNombre();
    	instituciones.put(nombre, ins);
    }

    public Institucion obtenerInstitucion(String nombreInstitucion) {
    	return instituciones.get(nombreInstitucion);
    }

    public boolean existeInstitucion(String nombreInstitucion) {
    	return instituciones.containsKey(nombreInstitucion);
    }

    public Set<String> getNombreInstituciones(){
    	return instituciones.keySet();
    }
}



