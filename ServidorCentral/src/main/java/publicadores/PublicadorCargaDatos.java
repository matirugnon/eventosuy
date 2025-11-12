package publicadores;

import excepciones.CategoriaNoSeleccionadaException;
import excepciones.CorreoInvalidoException;
import excepciones.EdicionExistenteException;
import excepciones.EdicionNoExisteException;
import excepciones.EventoNoExisteException;
import excepciones.EventoRepetidoException;
import excepciones.EventoYaFinalizadoException;
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

import config.Config;

@WebService
@SOAPBinding(style = Style.RPC, parameterStyle = ParameterStyle.WRAPPED)
public class PublicadorCargaDatos {

    private static boolean datosCargados = false;

    public static void main(String[] args) {
        // 1) Ver si vienen valores por -D desde el script
        String hostProp = System.getProperty("publicadorCargaDatos.host");
        String portProp = System.getProperty("publicadorCargaDatos.port");
        String urlProp  = System.getProperty("publicadorCargaDatos.url");

        // 2) Si no vienen por -D, usar config.Config (~/config/config.properties)
        String host = (hostProp != null)
                ? hostProp
                : Config.getPublisherHost("publicadorCargaDatos");

        int port = (portProp != null)
                ? Integer.parseInt(portProp)
                : Config.getPublisherPort("publicadorCargaDatos");

        String path = (urlProp != null)
                ? urlProp
                : Config.getPublisherUrl("publicadorCargaDatos");

        String url = "http://" + host + ":" + port + path;

        System.out.println("Publicando PublicadorCargaDatos en: " + url);
        Endpoint.publish(url, new PublicadorCargaDatos());
    }

    @WebMethod
    public boolean hayDatosBasicos() {
        try {
            IControladorUsuario ctrlUsuario = IControladorUsuario.getInstance();
            IControladorEvento  ctrlEvento  = IControladorEvento.getInstance();

            boolean hayUsuarios = !ctrlUsuario.listarUsuarios().isEmpty();
            boolean hayEventos  = !ctrlEvento.obtenerDTEventos().isEmpty();

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
                   CorreoInvalidoException,
                   EventoRepetidoException,
                   SiglaRepetidaException,
                   EventoNoExisteException,
                   FechaInvalidaException,
                   ExisteInstitucionException,
                   EdicionExistenteException,
                   FechasIncompatiblesException,
                   NombreTipoRegistroDuplicadoException,
                   UsuarioNoExisteException,
                   UsuarioYaRegistradoEnEdicionException,
                   CategoriaNoSeleccionadaException,
                   PatrocinioDuplicadoException,
                   EdicionNoExisteException,
                   EventoYaFinalizadoException {

        if (datosCargados) return false;

        IControladorUsuario  ctrlUsuario  = IControladorUsuario.getInstance();
        IControladorEvento   ctrlEvento   = IControladorEvento.getInstance();
        IControladorRegistro ctrlRegistro = IControladorRegistro.getInstance();

        Utils.cargarDatos(ctrlUsuario, ctrlEvento, ctrlRegistro);
        marcarDatosCargados();

        return true;
    }

}
