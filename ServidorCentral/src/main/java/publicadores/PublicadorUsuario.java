package publicadores;
import jakarta.xml.ws.Endpoint;



import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Set;

import excepciones.CorreoInvalidoException;
import excepciones.ExisteInstitucionException;
import excepciones.FechaInvalidaException;
import excepciones.UsuarioNoExisteException;
import excepciones.UsuarioRepetidoException;
import jakarta.jws.WebMethod;
import jakarta.jws.WebService;
import jakarta.jws.soap.SOAPBinding;
import jakarta.jws.soap.SOAPBinding.ParameterStyle;
import jakarta.jws.soap.SOAPBinding.Style;
import jakarta.xml.ws.Endpoint;
import logica.controladores.IControladorEvento;
import logica.controladores.IControladorRegistro;
import logica.controladores.IControladorUsuario;
import logica.datatypesyenum.DTFecha;
import logica.datatypesyenum.DTInstitucion;
import logica.datatypesyenum.DTUsuario;
import utils.Utils;


@WebService
@SOAPBinding(style = Style.RPC, parameterStyle = ParameterStyle.WRAPPED)
public class PublicadorUsuario {
    private final IControladorUsuario ctrlUs = IControladorUsuario.getInstance();

    public static void main(String[] args) {
        String configPath = System.getProperty("user.home") + "/.eventosUy/servidor-central.properties";
        Properties props = new Properties();

        try {
            props.load(new FileInputStream(configPath));
        } catch (IOException e) {
            System.err.println("⚠️ Advertencia: No se encontró el archivo de configuración en: " + configPath);
            System.err.println("   Usando valores por defecto.");
        }

        String url = props.getProperty("servidor.usuario.url", "http://localhost:9128/publicadorUsuario");
        System.out.println("Publicando PublicadorUsuario en: " + url);
        Endpoint.publish(url, new PublicadorUsuario());
    }

    // ------------------- Métodos de alta -------------------
    @WebMethod
    public boolean altaAsistente(String nick, String nombre, String correo, String apellido,
                                 DTFecha fechanac, String institucion, String password, String avatar)
            throws UsuarioRepetidoException, CorreoInvalidoException, FechaInvalidaException {
        ctrlUs.altaAsistente(nick, nombre, correo, apellido, fechanac, institucion, password, avatar);
        return true;
    }

    @WebMethod
    public boolean altaOrganizador(String nick, String nombre, String correo, String descripcion,
                                   String link, String password, String avatar)
            throws UsuarioRepetidoException, CorreoInvalidoException {
        ctrlUs.altaOrganizador(nick, nombre, correo, descripcion, link, password, avatar);
        return true;
    }

    @WebMethod
    public boolean altaInstitucion(String nombreInstitucion, String descripcion, String web)
            throws ExisteInstitucionException {
        ctrlUs.altaInstitucion(nombreInstitucion, descripcion, web);
        return true;
    }

    @WebMethod
    public String[] listarUsuarios() {
        return ctrlUs.listarUsuarios().toArray(new String[0]);
    }

    @WebMethod
    public DTUsuario[] listarUsuariosDT() {
        return ctrlUs.listarUsuariosDT().toArray(new DTUsuario[0]);
    }

    @WebMethod
    public String[] listarAsistentes() {
        return ctrlUs.listarAsistentes().toArray(new String[0]);
    }

    @WebMethod
    public String[] listarOrganizadores() {
        return ctrlUs.listarOrganizadores().toArray(new String[0]);
    }

    @WebMethod
    public String[] listarInstituciones() {
        return ctrlUs.listarInstituciones().toArray(new String[0]);
    }

    @WebMethod
    public DTInstitucion getInstitucion(String nombreInstitucion) {
        return ctrlUs.getInstitucion(nombreInstitucion);
    }

    @WebMethod
    public DTUsuario getDTUsuario(String identificador) throws UsuarioNoExisteException {
        return ctrlUs.getDTUsuario(identificador);
    }

    @WebMethod
    public String[] obtenerRegistros(String identificador) {
        return ctrlUs.obtenerRegistros(identificador).toArray(new String[0]);
    }

    @WebMethod
    public String[] listarEdicionesOrganizador(String identificador) {
        return ctrlUs.listarEdicionesOrganizador(identificador).toArray(new String[0]);
    }

    @WebMethod
    public boolean modificarUsuario(String nick, DTUsuario datosUsuario)
            throws UsuarioNoExisteException, FechaInvalidaException {
        ctrlUs.modificarUsuario(nick, datosUsuario);
        return true;
    }


    @WebMethod
    public String cargarDatosPrueba() {
    	try {
    		IControladorUsuario ctrlUsuario = IControladorUsuario.getInstance();
    		IControladorEvento ctrlEvento = IControladorEvento.getInstance();
    		IControladorRegistro ctrlRegistro = IControladorRegistro.getInstance();

    		Utils.cargarDatos(ctrlUsuario, ctrlEvento, ctrlRegistro);
    		return "OK";
    	} catch (Exception e) {
    		return e.getMessage();
    	}
    }
}
