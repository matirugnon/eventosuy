package logica.Controladores;

import excepciones.UsuarioNoExisteException;
import excepciones.UsuarioRepetidoException;
import logica.DataUsuario;
import logica.Usuario;
import logica.DatatypesYEnum.DTFecha;

/**
 * @author TProg2017
 *
 */
public interface IControladorUsuario {

    /**
     * Registra al usuario en el sistema.
     * @param n Nombre del usuario.
     * @param ap Apellido del usuario.
     * @param ci Cédula del usuario.
     * @throws UsuarioRepetidoException Si la cédula del usuario se encuentra registrada en el sistema.
     */

	public void altaUsuario(Usuario u);

	public void altaAsistente(String nick, String nombre, String correo, String apellido, DTFecha fechanac, String institucion) throws UsuarioRepetidoException;

    public void altaOrganizador(String nick, String nombre, String correo, String descripcion, String link) throws UsuarioRepetidoException;

    /**
     * Retorna la información de un usuario con la cédula indicada.
     * @param ci Cédula del usuario.
     * @return Información del usuario.
     * @throws UsuarioNoExisteException Si la cédula del usuario no está registrada en el sistema.
     */
    public abstract DataUsuario verInfoUsuario(String ci) throws UsuarioNoExisteException;

    /**
     * Retorna la información de todos los usuarios registrados en el sistema.
     * @return Información de los usuarios del sistema.
     * @throws UsuarioNoExisteException Si no existen usuarios registrados en el sistema.
     */
    public abstract DataUsuario[] getUsuarios() throws UsuarioNoExisteException;


    public boolean ExisteNickname(String nick);

    public void AsociarInstitucion(String nick, String nombreInstitucion);
}

