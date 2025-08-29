
package logica.Controladores;

import java.util.List;
import java.util.Set;

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

	public void altaUsuario(Usuario u);

	public void altaAsistente(String nick, String nombre, String correo, String apellido, DTFecha fechanac, String institucion) throws UsuarioRepetidoException;

    public void altaOrganizador(String nick, String nombre, String correo, String descripcion, String link) throws UsuarioRepetidoException;


    //funcion que ya venia con ejemplo
    public abstract DataUsuario verInfoUsuario(String ci) throws UsuarioNoExisteException;


    public boolean ExisteNickname(String nick);

    public boolean ExisteCorreo(String correo);

    public void AsociarInstitucion(String nick, String nombreInstitucion);

    public List<Usuario> listarUsuarios();

    public Set<String> listarOrganizadores();
}





