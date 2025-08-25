package logica;

import excepciones.UsuarioNoExisteException;
import excepciones.UsuarioRepetidoException;

/**
 * Controlador de usuarios.
 * @author TProg2017
 *
 */
public class ControladorUsuario implements IControladorUsuario {

    public ControladorUsuario() {
    }

    public void altaUsuario(String nick, String nombre, String correo) throws UsuarioRepetidoException {

        ManejadorUsuario mu = ManejadorUsuario.getinstance();

        Usuario u = mu.obtenerUsuario(nick);

        if (u != null)
            throw new UsuarioRepetidoException("El usuario " + nick + " ya esta registrado");

        u = new Usuario(nick, nombre, correo);
        mu.addUsuario(u);
    }


    //metodo del controlador para mostrar la data del user

    public DataUsuario verInfoUsuario(String nick) throws UsuarioNoExisteException {
        ManejadorUsuario mu = ManejadorUsuario.getinstance();

        Usuario u = mu.obtenerUsuario(nick);

        if (u != null)
            return new DataUsuario(u.getNombre(), u.getNickname(), u.getCorreo());
        else
            throw new UsuarioNoExisteException("El usuario " + nick + " no existe");

    }


    public DataUsuario[] getUsuarios() throws UsuarioNoExisteException {

        ManejadorUsuario mu = ManejadorUsuario.getinstance();
        Usuario[] usrs = mu.getUsuarios();

        if (usrs != null) {
            DataUsuario[] du = new DataUsuario[usrs.length];
            Usuario usuario;

            // Para separar lógica de presentación, no se deben devolver los Usuario,
            // sino los DataUsuario
            for (int i = 0; i < usrs.length; i++) {
                usuario = usrs[i];
                du[i] = new DataUsuario(usuario.getNombre(), usuario.getNickname(), usuario.getCorreo());
            }

            return du;
        } else
            throw new UsuarioNoExisteException("No existen usuarios registrados");

    }
}
