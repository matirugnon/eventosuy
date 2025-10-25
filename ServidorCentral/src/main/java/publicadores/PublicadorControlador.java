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
import java.util.Set;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

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

    @WebMethod
    public boolean darAltaEvento(
        String nombreEvento,
        String descripcion,
        DTFecha fechaAlta,
        String sigla,
        String[] categorias
    ) {

    	Set<String> categoriasSet;
        if (categorias == null || categorias.length == 0) {
            categoriasSet = new HashSet<>();
        } else {
            categoriasSet = new HashSet<>(Arrays.asList(categorias));

        }

        return ctrl.darAltaEvento(nombreEvento, descripcion, fechaAlta, sigla, categoriasSet);
    }

    @WebMethod
    public String[] listarEventos() {
        Set<String> eventos = ctrl.listarEventos();
        return eventos.toArray(new String[0]);
    }

    @WebMethod
    public String[] listarCategorias() {
        Set<String> categorias = ctrl.listarCategorias();
        return categorias.toArray(new String[0]);
    }

    @WebMethod
    public boolean altaCategoria(String nombreCategoria) {
        if (nombreCategoria == null || nombreCategoria.isBlank()) {
            return false;
        }
        if (!ctrl.existeCategoria(nombreCategoria)) {
            ctrl.altaCategoria(nombreCategoria);
        }
        return true;
    }


}
