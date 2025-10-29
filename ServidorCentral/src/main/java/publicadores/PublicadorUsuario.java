package publicadores;
import jakarta.xml.bind.annotation.XmlSeeAlso;
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
import logica.datatypesyenum.DTAsistente;
import logica.datatypesyenum.DTFecha;
import logica.datatypesyenum.DTInstitucion;
import logica.datatypesyenum.DTOrganizador;
import logica.datatypesyenum.DTUsuario;
import utils.Utils;


@WebService
@XmlSeeAlso({DTAsistente.class, DTOrganizador.class})
@SOAPBinding(style = Style.RPC, parameterStyle = ParameterStyle.WRAPPED)
public class PublicadorUsuario {
    private final IControladorUsuario ctrlUs = IControladorUsuario.getInstance();
    private final IControladorRegistro ctrlReg = IControladorRegistro.getInstance();
    private final IControladorEvento ctrlEv = IControladorEvento.getInstance();

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

    
    
    @WebMethod
    public boolean altaAsistenteSinAvatar(String nick, String nombre, String correo, String apellido,
                                 int diaNac, int mesNac, int anioNac, String institucion, String password)
    	throws UsuarioRepetidoException, CorreoInvalidoException, FechaInvalidaException {
            DTFecha fechanac = new DTFecha(diaNac, mesNac, anioNac);
            ctrlUs.altaAsistente(nick, nombre, correo, apellido, fechanac, institucion, password);
            return true;
    }
    
    
    @WebMethod
    public boolean altaAsistente(String nick, String nombre, String correo, String apellido,
                                 int diaNac, int mesNac, int anioNac, 
                                 String institucion, String password, String avatar)
            throws UsuarioRepetidoException, CorreoInvalidoException, FechaInvalidaException {

        String avatarfinal = (avatar == null || avatar.trim().isEmpty()) ? null : avatar;
        DTFecha fechanac = new DTFecha(diaNac, mesNac, anioNac);
        ctrlUs.altaAsistente(nick, nombre, correo, apellido, fechanac, institucion, password, avatarfinal);
        return true;
    }

    
    @WebMethod
    public boolean altaOrganizadorSinAvatar(String nick, String nombre, String correo, String descripcion,
                                   String link, String password)
            throws UsuarioRepetidoException, CorreoInvalidoException {
        ctrlUs.altaOrganizador(nick, nombre, correo, descripcion, link, password);
        return true;
    }

    
    @WebMethod
    public boolean altaOrganizador(String nick, String nombre, String correo, String descripcion,
                                   String link, String password, String avatar)
            throws UsuarioRepetidoException, CorreoInvalidoException {

        String avatarfinal = (avatar == null || avatar.trim().isEmpty()) ? null : avatar;
        ctrlUs.altaOrganizador(nick, nombre, correo, descripcion, link, password, avatarfinal);
        return true;
    }

    public boolean altaInstitucion(String nombreInstitucion, String descripcion, String web)
            throws ExisteInstitucionException {
        ctrlUs.altaInstitucion(nombreInstitucion, descripcion, web);
        return true;
    }
    //alta institucion con logo
    // Nota: no se pueden exponer dos operaciones SOAP con el mismo nombre y distinto signature;
    // por eso esta operación usa un operationName distinto en el WSDL.
    public boolean altaInstitucionConLogo(String nombreInstitucion, String descripcion, String web, String logo)
            throws ExisteInstitucionException {
        ctrlUs.altaInstitucion(nombreInstitucion, descripcion, web, logo);
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

    @WebMethod
    public boolean validarCredenciales(String identificador, String password) {
        return ctrlUs.validarCredenciales(identificador, password);
    }

    @WebMethod
    public String obtenerTipoUsuario(String identificador) throws UsuarioNoExisteException {
        DTUsuario dtUsuario = ctrlUs.getDTUsuario(identificador);
        if (dtUsuario instanceof DTAsistente) {
            return "asistente";
        } else if (dtUsuario instanceof DTOrganizador) {
            return "organizador";
        }
        return "desconocido";
    }

    @WebMethod
    public String obtenerAvatar(String identificador) throws UsuarioNoExisteException {
        String avatar = ctrlUs.obtenerAvatar(identificador);
        return avatar != null ? avatar : "";
    }

    @WebMethod
    public String obtenerEmail(String identificador) throws UsuarioNoExisteException {
        DTUsuario dtUsuario = ctrlUs.getDTUsuario(identificador);
        String email = dtUsuario.getCorreo();
        return email != null ? email : "";
    }
    @WebMethod
    public String obtenerNombre(String identificador) throws UsuarioNoExisteException {
        DTUsuario dtUsuario = ctrlUs.getDTUsuario(identificador);
        return dtUsuario.getNombre();
    }
    
    @WebMethod
    public String[] obtenerRegistrosDetallados(String nickname) throws UsuarioNoExisteException {
        Set<logica.datatypesyenum.DTRegistro> registros = ctrlReg.listarRegistrosPorAsistente(nickname);
        String[] resultado = new String[registros.size()];
        int i = 0;
        for (logica.datatypesyenum.DTRegistro reg : registros) {
            // Obtener información de la edición
            try {
                logica.datatypesyenum.DTEdicion edicion = ctrlEv.consultarEdicion(reg.getNomEdicion());
                // Formato: nombreEvento|nombreEdicion|siglaEdicion|estado|tipoRegistro|costo|fecha
                resultado[i] = edicion.getEvento() + "|" + 
                              edicion.getNombre() + "|" + 
                              edicion.getSigla() + "|" + 
                              edicion.getEstado() + "|" + 
                              reg.getTipoDeRegistro() + "|" + 
                              reg.getCosto() + "|" + 
                              reg.getFechaRegistro().toString();
            } catch (Exception e) {
                // Si no se puede obtener la edición, devolver info básica
                resultado[i] = "Desconocido|" + reg.getNomEdicion() + "|N/A|FINALIZADA|" + 
                              reg.getTipoDeRegistro() + "|" + reg.getCosto() + "|" + 
                              reg.getFechaRegistro().toString();
            }
            i++;
        }
        return resultado;
    }
    
    @WebMethod
    public boolean existeNickname(String nick) {
    	return ctrlUs.existeNickname(nick);
    }
    
    @WebMethod
    public boolean existeEmail(String email) {
    	return ctrlUs.existeCorreo(email);
    }
}
