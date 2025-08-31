
package logica.Controladores;

import java.util.List;
import java.util.Set;

import excepciones.CorreoInvalidoException;
import excepciones.ExisteInstitucionException;
import excepciones.FechaInvalidaException;
import excepciones.UsuarioNoExisteException;
import excepciones.UsuarioRepetidoException;
import logica.DataUsuario;
import logica.Usuario;
import logica.DatatypesYEnum.DTFecha;
import logica.DatatypesYEnum.DTUsuario;

/**
 * @author TProg2017
 *
 */
public interface IControladorUsuario {

	static IControladorUsuario getInstance() {
        return ControladorUsuario.getInstance();
    }

	public void altaUsuario(Usuario u);

	public void altaAsistente(String nick, String nombre, String correo, String apellido, DTFecha fechanac, String institucion)
			throws UsuarioRepetidoException, CorreoInvalidoException, FechaInvalidaException;

    public void altaOrganizador(String nick, String nombre, String correo, String descripcion, String link) throws UsuarioRepetidoException, CorreoInvalidoException;


    public boolean ExisteNickname(String nick);

    public boolean ExisteCorreo(String correo);


    public Set<String> listarEdicionesOrganizador(String nombreOrganizador);


    //public List<Usuario> listarUsuarios();


    public void AsociarInstitucion(String nick, String nombreInstitucion);

    public Set<String> listarOrganizadores();

    public Set<String> listarUsuarios();

    public Set<String> listarAsistentes();

    public boolean existeInstitucion(String nomInstitucion);

    public void altaInstitucion(String nombreInstitucion, String descripcion, String web)
    		throws ExisteInstitucionException;

    public Set<String> listarInstituciones();

    public DTUsuario getDTUsuario(String nombreU);
    public Set<String> obtenerRegistros(String nombreAsistente);


    public void modificarUsuario(String nick, DTUsuario datosUsuario) throws UsuarioNoExisteException;


	public boolean esFechaValida(int dia, int mes, int anio);


}





