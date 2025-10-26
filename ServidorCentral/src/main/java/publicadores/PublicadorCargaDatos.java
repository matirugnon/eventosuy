package publicadores;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import excepciones.CategoriaNoSeleccionadaException;
import excepciones.CorreoInvalidoException;
import excepciones.EdicionExistenteException;
import excepciones.EdicionNoExisteException;
import excepciones.EventoRepetidoException;
import excepciones.ExisteInstitucionException;
import excepciones.FechaInvalidaException;
import excepciones.FechasIncompatiblesException;
import excepciones.NombreTipoRegistroDuplicadoException;
import excepciones.PatrocinioDuplicadoException;
import excepciones.SiglaRepetidaException;
import excepciones.UsuarioNoExisteException;
import excepciones.UsuarioRepetidoException;
import excepciones.UsuarioYaRegistradoEnEdicionException;
import jakarta.jws.WebMethod;
import jakarta.jws.WebService;
import jakarta.jws.soap.SOAPBinding;
import jakarta.jws.soap.SOAPBinding.ParameterStyle;
import jakarta.jws.soap.SOAPBinding.Style;
import jakarta.xml.ws.Endpoint;
import logica.controladores.IControladorEvento;
import logica.controladores.IControladorRegistro;
import logica.controladores.IControladorUsuario;
import utils.Utils;

@WebService
@SOAPBinding(style = Style.RPC, parameterStyle = ParameterStyle.WRAPPED)
public class PublicadorCargaDatos {
	 private static boolean datosCargados = false;
	 
	public static void main(String[] args) {
        String configPath = System.getProperty("user.home") + "/.eventosUy/servidor-central.properties";
        Properties props = new Properties();

        try {
            props.load(new FileInputStream(configPath));
        } catch (IOException e) {
            System.err.println("⚠️ Advertencia: No se encontró el archivo de configuración en: " + configPath);
            System.err.println("   Usando valores por defecto.");
        }

        String url = props.getProperty("servidor.cargaDatos.url", "http://localhost:9131/publicadorCargaDatos");
        System.out.println("Publicando PublicadorCargaDatos en: " + url);
        Endpoint.publish(url, new PublicadorRegistro());
    }
	
	@WebMethod
	public boolean hayDatosBasicos() {
	    try {
	        IControladorUsuario ctrlUsuario = IControladorUsuario.getInstance();
	        IControladorEvento ctrlEvento = IControladorEvento.getInstance();

	        boolean hayUsuarios = !ctrlUsuario.listarUsuarios().isEmpty();
	        boolean hayEventos = !ctrlEvento.obtenerDTEventos().isEmpty();

	        return hayUsuarios && hayEventos;
	    } catch (Exception e) {
	        return false;
	    }
	}
	
	@WebMethod
	public boolean datosPrecargados() {
		return datosCargados;
	}
	
	@WebMethod
    public void marcarDatosCargados() {
        datosCargados = true;
    }
	
	public boolean cargarDatos()
			throws UsuarioRepetidoException,
			CorreoInvalidoException, EventoRepetidoException, SiglaRepetidaException, FechaInvalidaException,
			ExisteInstitucionException, EdicionExistenteException, FechasIncompatiblesException,
			NombreTipoRegistroDuplicadoException, UsuarioNoExisteException, UsuarioYaRegistradoEnEdicionException, CategoriaNoSeleccionadaException, PatrocinioDuplicadoException, EdicionNoExisteException {
		
		 if (datosCargados) return false;
		 
		IControladorUsuario ctrlUsuario = IControladorUsuario.getInstance();
        IControladorEvento ctrlEvento = IControladorEvento.getInstance();
        IControladorRegistro ctrlRegistro = IControladorRegistro.getInstance();
        
  
		Utils.cargarDatos(ctrlUsuario,ctrlEvento,ctrlRegistro);
		marcarDatosCargados();
		
		return true;
	}

}
