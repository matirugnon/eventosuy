
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

	static IControladorUsuario getInstance() {
        return ControladorUsuario.getInstance();
    }

	public void altaUsuario(Usuario u);

	public void altaAsistente(String nick, String nombre, String correo, String apellido, DTFecha fechanac, String institucion) throws UsuarioRepetidoException;

    public void altaOrganizador(String nick, String nombre, String correo, String descripcion, String link) throws UsuarioRepetidoException;


    //funcion que ya venia con ejemplo
    public abstract DataUsuario verInfoUsuario(String ci) throws UsuarioNoExisteException;


    public boolean ExisteNickname(String nick);

    public boolean ExisteCorreo(String correo);



    //public List<Usuario> listarUsuarios();


    public void AsociarInstitucion(String nick, String nombreInstitucion);

    public Set<String> listarOrganizadores();

    public Set<String> listarUsuarios();

    public Set<String> listarAsistentes();

    public boolean existeInstitucion(String nomInstitucion);

    public void altaInstitucion(String nombreInstitucion, String descripcion, String web);

    public Set<String> listarInstituciones();


}





