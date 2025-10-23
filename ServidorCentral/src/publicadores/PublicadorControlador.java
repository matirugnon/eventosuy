package publicadores;

import jakarta.jws.WebMethod;
import jakarta.jws.WebService;
import jakarta.jws.soap.SOAPBinding;
import jakarta.jws.soap.SOAPBinding.Style;
import jakarta.xml.ws.Endpoint;
import logica.controladores.ControladorEvento;
import logica.datatypesyenum.DTFecha;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

@WebService
@SOAPBinding(style = Style.RPC)
public class PublicadorControlador {

    private final ControladorEvento ctrl = new ControladorEvento();

    @WebMethod
    public String hola() {
        return "Hola desde el Servidor Central";
    }

    @WebMethod
    public String obtenerEventos() {
        return String.join(", ", ctrl.listarEventos());
    }

    public static void main(String[] args) {
        // Ruta del archivo de configuración: ~/.eventosUy/servidor-central.properties
        String configPath = System.getProperty("user.home") + "/.eventosUy/servidor-central.properties";
        Properties props = new Properties();

        try {
            props.load(new FileInputStream(configPath));
        } catch (IOException e) {
            System.err.println("⚠️ Advertencia: No se encontró el archivo de configuración en: " + configPath);
            System.err.println("   Usando valores por defecto.");
        }

        String url = props.getProperty("servidor.central.url", "http://localhost:9128/publicador");
        System.out.println("Publicando Servidor Central en: " + url);
        Endpoint.publish(url, new PublicadorControlador());
    }

    @WebMethod
    public boolean altaEdicionDeEvento(
        String nickOrganizador,
        String nombreEvento,
        String nombreEdicion,
        String sigla,
        String ciudad,
        String pais,
        DTFecha fechaInicio,
        DTFecha fechaFin,
        DTFecha fechaAlta
    ) {
        return ctrl.altaEdicion(
			nickOrganizador,
			nombreEvento,
			nombreEdicion,
			sigla,
			ciudad,
			pais,
			fechaInicio,
			fechaFin,
			fechaAlta
		);
    }



}