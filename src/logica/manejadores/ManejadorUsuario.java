package logica.manejadores;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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

    private static ManejadorUsuario instancia = null;

    private ManejadorUsuario() {
    	usuariosNick = new HashMap<String, Usuario>();

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

}
