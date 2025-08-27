package logica.Controladores;

import excepciones.UsuarioNoExisteException;
import excepciones.UsuarioRepetidoException;
import logica.Asistente;
import logica.DataUsuario;
import logica.Organizador;
import logica.Usuario;
import logica.DatatypesYEnum.DTFecha;
import logica.manejadores.ManejadorUsuario;

/**
 * Controlador de usuarios.
 * @author TProg2017
 *
 */
public class ControladorUsuario implements IControladorUsuario {

	private ManejadorUsuario manejador;

    public ControladorUsuario() {

    	//inicializo el manejador
    	this.manejador = ManejadorUsuario.getinstance();
    }


    //metodo para agregar usuario luego de creado
    public void altaUsuario(Usuario u) {
        manejador.addUsuario(u);
    }

    //alta Asistente
    public void altaAsistente(String nick, String nombre, String correo, String apellido, DTFecha fechanac) throws UsuarioRepetidoException {

        ManejadorUsuario mu = ManejadorUsuario.getinstance();
        Usuario u = mu.obtenerUsuario(nick);

        if (u != null)
            throw new UsuarioRepetidoException("El usuario " + nick + " ya esta registrado");

        u = new Asistente(nick, nombre, correo, apellido, fechanac);
        altaUsuario(u);
    }

    //alta Organizador
    public void altaOrganizador(String nick, String nombre, String correo, String descripcion, String link) throws UsuarioRepetidoException {

        ManejadorUsuario mu = ManejadorUsuario.getinstance();

        Usuario u = mu.obtenerUsuario(nick);

        if (u != null)
            throw new UsuarioRepetidoException("El usuario " + nick + " ya esta registrado");

        u = new Organizador(nick, nombre, correo, descripcion, link);
        altaUsuario(u);
    }




    //metodo del controlador para mostrar la data del user, no se si sirve pero lo dejo porque estaba en la demoswing

    public DataUsuario verInfoUsuario(String nick) throws UsuarioNoExisteException {
        ManejadorUsuario mu = ManejadorUsuario.getinstance();

        Usuario u = mu.obtenerUsuario(nick);

        if (u != null)
            return new DataUsuario(u.getNombre(), u.getNickname(), u.getCorreo());
        else
            throw new UsuarioNoExisteException("El usuario " + nick + " no existe");

    }


    //lo mismo, no se si sirve

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
