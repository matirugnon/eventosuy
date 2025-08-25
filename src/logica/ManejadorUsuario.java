package logica;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

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

    public Usuario[] getUsuarios() {
        if (usuariosNick.isEmpty())
            return null;
        else {
            Collection<Usuario> usrs = usuariosNick.values();
            Object[] o = usrs.toArray();
            Usuario[] usuarios = new Usuario[o.length];
            for (int i = 0; i < o.length; i++) {
                usuarios[i] = (Usuario) o[i];
            }

            return usuarios;
        }
    }

}
