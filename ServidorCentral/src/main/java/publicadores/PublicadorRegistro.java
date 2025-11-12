package publicadores;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Set;

import config.Config;
import excepciones.EdicionNoExisteException;
import excepciones.UsuarioNoExisteException;
import excepciones.UsuarioYaRegistradoEnEdicionException;
import jakarta.jws.WebMethod;
import jakarta.jws.WebService;
import jakarta.jws.soap.SOAPBinding;
import jakarta.jws.soap.SOAPBinding.ParameterStyle;
import jakarta.jws.soap.SOAPBinding.Style;
import jakarta.xml.ws.Endpoint;
import logica.controladores.IControladorRegistro;
import logica.datatypesyenum.DTFecha;
import logica.datatypesyenum.DTRegistro;
import logica.datatypesyenum.DTTipoDeRegistro;

@WebService
@SOAPBinding(style = Style.RPC, parameterStyle = ParameterStyle.WRAPPED)
public class PublicadorRegistro {
	private final IControladorRegistro ctrlReg = IControladorRegistro.getInstance();
	
	public static void main(String[] args) {
	    String hostProp = System.getProperty("publicadorRegistro.host");
	    String portProp = System.getProperty("publicadorRegistro.port");
	    String urlProp  = System.getProperty("publicadorRegistro.url");

	    String host = (hostProp != null)
	            ? hostProp
	            : Config.getPublisherHost("publicadorRegistro");

	    int port = (portProp != null)
	            ? Integer.parseInt(portProp)
	            : Config.getPublisherPort("publicadorRegistro");

	    String path = (urlProp != null)
	            ? urlProp
	            : Config.getPublisherUrl("publicadorRegistro");

	    String url = "http://" + host + ":" + port + path;

	    System.out.println("Publicando PublicadorRegistro en: " + url);
	    Endpoint.publish(url, new PublicadorRegistro());
	}

	
	@WebMethod
	public String altaTipoDeRegistro(String nombreEd, String nombreTipo, String descripcion, double costo, int cupo){
		try {
			ctrlReg.altaTipoDeRegistro(nombreEd, nombreTipo, descripcion, costo, cupo);
			return "OK";
		} catch (Exception e) {
			return e.getMessage();
		}
	}
	
	@WebMethod
	public String[] listarTipoRegistro(String nombreEd)
			throws EdicionNoExisteException{
		return ctrlReg.listarTipoRegistro(nombreEd).toArray(new String[0]);
	}
	
	@WebMethod
	public DTTipoDeRegistro consultaTipoDeRegistro(String nombreEd, String nombreReg) {
		return ctrlReg.consultaTipoDeRegistro(nombreEd, nombreReg);
	}
	
	@WebMethod
	public String[] obtenerNomsTipoRegistro(String nickusuario) {
		Set<String> tipos = ctrlReg.obtenerNomsTipoRegistro(nickusuario);

	    if (tipos == null || tipos.isEmpty()) {
	        return new String[0]; // Evita problemas con null
	    }

	    return tipos.toArray(new String[0]);
	}
	@WebMethod
	public String altaRegistro(String nomEdicion, String nickAsistente, String nomTipoRegistro, DTFecha fechaRegistro, double costo) throws UsuarioYaRegistradoEnEdicionException, UsuarioNoExisteException {
		try {
			ctrlReg.altaRegistro(nomEdicion, nickAsistente, nomTipoRegistro, fechaRegistro, costo);
			return "OK";
		} catch (Exception e) {
			// Devolver el mensaje de la excepción para que el cliente SOAP lo muestre
			return e.getMessage();
		}
	}
	
	@WebMethod
	public String altaRegistroConPatrocinio(String nomEdicion, String nickAsistente, String nomTipoRegistro, DTFecha fechaRegistro, String codigoPatrocinio) throws UsuarioYaRegistradoEnEdicionException, UsuarioNoExisteException {
		try {
			ctrlReg.altaRegistroConPatrocinio(nomEdicion, nickAsistente, nomTipoRegistro, fechaRegistro, codigoPatrocinio);
			return "OK";
		} catch (Exception e) {
			// Devolver el mensaje de la excepción para que el cliente SOAP lo muestre
			return e.getMessage();
		}
	}
	
	@WebMethod
	public DTRegistro getRegistro(String nombreUsuario, String nombreTipoRegistro) {
		try{
			DTRegistro dtReg = ctrlReg.getRegistro(nombreUsuario, nombreTipoRegistro);
			if (dtReg == null) {
			return new DTRegistro();
			} else return dtReg;
		} catch (Exception e) {
			System.err.println("Error al obtener registro: " + e.getMessage());
	        return new DTRegistro();
		}
		
	}
	
	@WebMethod
	public DTRegistro[] listarRegistrosPorAsistente(String nickAsistente) throws UsuarioNoExisteException {
		return ctrlReg.listarRegistrosPorAsistente(nickAsistente).toArray(new DTRegistro[0]);
	}

	@WebMethod
	public boolean estaRegistrado(String nombreEdicion, String nickAsistente) throws UsuarioNoExisteException {
		return ctrlReg.estaRegistrado(nombreEdicion, nickAsistente);
	}

	@WebMethod
	public boolean alcanzoCupo(String nombreEdicion, String nombreTipoRegistro) throws EdicionNoExisteException {
		return ctrlReg.alcanzoCupo(nombreEdicion, nombreTipoRegistro);
	}

	@WebMethod
	public void registrarAsistencia(String nickAsistente, String nomEdicion, String nomTipoRegistro) 
			throws UsuarioNoExisteException {
		ctrlReg.registrarAsistencia(nickAsistente, nomEdicion, nomTipoRegistro);
	}
}
