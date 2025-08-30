package logica.Controladores;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import excepciones.UsuarioNoExisteException;
import excepciones.UsuarioRepetidoException;
import logica.Asistente;
import logica.DataUsuario;
import logica.Organizador;
import logica.Usuario;
import logica.DatatypesYEnum.DTFecha;
import logica.DatatypesYEnum.DTUsuario;
import logica.manejadores.ManejadorEventos;
import logica.manejadores.ManejadorUsuario;

/**
 * Controlador de usuarios.
 * @author TProg2017
 *
 */
public class ControladorUsuario implements IControladorUsuario {

	private ManejadorUsuario manejador;
	private static ControladorUsuario instancia = null;

    public ControladorUsuario() {

    	//inicializo el manejador
    	this.manejador = ManejadorUsuario.getinstance();
    }


    public static ControladorUsuario getInstance() {
        if (instancia == null)
            instancia = new ControladorUsuario();
        return instancia;
    }


    //metodo para agregar usuario luego de creado
    public void altaUsuario(Usuario u) {
        manejador.addUsuario(u);
    }

    //alta Asistente
    public void altaAsistente(String nick, String nombre, String correo, String apellido, DTFecha fechanac, String institucion) throws UsuarioRepetidoException {

        if(ExisteNickname(nick))
            throw new UsuarioRepetidoException("El usuario " + nick + " ya esta registrado");
        if(ExisteCorreo(correo))
        	throw new UsuarioRepetidoException("El correo " + correo + " ya esta registrado");


        Usuario a = new Asistente(nick, nombre, correo, apellido, fechanac, institucion);
        altaUsuario(a);
    }

    //alta Organizador
    public void altaOrganizador(String nick, String nombre, String correo, String descripcion, String link) throws UsuarioRepetidoException {

    	 if (ExisteNickname(nick))
             throw new UsuarioRepetidoException("El usuario " + nick + " ya esta registrado");
         if(ExisteCorreo(correo))
         	throw new UsuarioRepetidoException("El correo " + correo + " ya esta registrado");

        Usuario o = new Organizador(nick, nombre, correo, descripcion, link);
        altaUsuario(o);
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

    //-------------------------NUEVOS METODOS DEL CONTROLADOR---------------------------------------------------------------


    public boolean ExisteNickname(String nick) {

    	 ManejadorUsuario mu = ManejadorUsuario.getinstance();
         Usuario u = mu.obtenerUsuario(nick);

         if (u != null) {return true;}

		return false;
    }

    public boolean ExisteCorreo(String correo) {

    	//IMPLEMENTAR--------------------
		return false;

   }

    public void AsociarInstitucion(String nick, String nombreInstitucion) {

    }

    public Set<String> listarUsuarios() {
        Set<Usuario> usuarios = manejador.getUsuarios();
        Set<String> nicks = new HashSet<>();
        for (Usuario u : usuarios) {
            nicks.add(u.getNickname()); // o getNickname(), según lo que qusieras mostrar
        }
        return nicks;
    }


    public void modificarUsuario(String nick, DTUsuario datosUsuario) {

    }


    public Set<String> listarOrganizadores(){
        ManejadorUsuario manejadorU = ManejadorUsuario.getinstance();
        return manejadorU.obtenerNicksOrganizadores();
    }
    
    public Set<String> listarAsistentes(){
    	ManejadorUsuario manejadorU = ManejadorUsuario.getinstance();
    	return manejadorU.getNickAsistentes();
    }





















}
