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

    private Map<String, Usuario> usuariosNick;
    private Map<String, String> correosUsuarios; // Nuevo mapa para asociar correos con nicks
    private Map<String, Institucion> instituciones;

    private static ManejadorUsuario instancia = null;

    private ManejadorUsuario() {
        usuariosNick = new HashMap<>();
        correosUsuarios = new HashMap<>(); // Inicializar el nuevo mapa
        instituciones = new HashMap<>();
    }

    //singleton
    public static ManejadorUsuario getinstance() {
        if (instancia == null)
            instancia = new ManejadorUsuario();
        return instancia;
    }

    public void addUsuario(Usuario usu) {
        String nick = usu.getNickname();
        String correo = usu.getCorreo(); // Suponiendo que Usuario tiene un método getCorreo()
        usuariosNick.put(nick, usu);
        correosUsuarios.put(correo, nick); // Asociar el correo con el nick
    }

    /**
     * Método para obtener un usuario por su nickname o correo.
     * @param identificador Puede ser un nickname o un correo.
     * @return El usuario correspondiente, o null si no se encuentra.
     */
    public Usuario obtenerUsuario(String identificador) {
        if (identificador.contains("@")) { // Si contiene '@', se asume que es un correo
            String nick = correosUsuarios.get(identificador);
            return usuariosNick.get(nick);
        }
        return usuariosNick.get(identificador); // Si no, se asume que es un nickname
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
    
    /**
     * Método para validar las credenciales de un usuario.
     * @param nickname El nickname del usuario.
     * @param password La contraseña del usuario.
     * @return true si las credenciales son correctas, false en caso contrario.
     */
    public boolean validarCredenciales(String nickname, String password) {
        Usuario usuario = usuariosNick.get(nickname);
        if (usuario != null) {
            return usuario.getPassword().equals(password); // Compara la contraseña
        }
        return false; // Usuario no encontrado
    }
}