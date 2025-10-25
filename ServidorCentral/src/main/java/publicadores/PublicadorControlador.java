package publicadores;

import jakarta.jws.WebMethod;

import jakarta.jws.WebService;
import jakarta.jws.soap.SOAPBinding;
import jakarta.jws.soap.SOAPBinding.Style;
import jakarta.xml.ws.Endpoint;
import logica.controladores.ControladorEvento;
import logica.controladores.IControladorEvento;
import logica.controladores.IControladorUsuario;
import logica.controladores.IControladorRegistro;
import utils.Utils;
import logica.datatypesyenum.DTEvento;
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
    public String altaEdicionDeEvento(
        String nickOrganizador,
        String nombreEvento,
        String nombreEdicion,
        String sigla,
        String ciudad,
        String pais,
        DTFecha fechaInicio,
        DTFecha fechaFin,
        DTFecha fechaAlta, 
        String rutaImagen

    ) {

        String imagenFinal = (rutaImagen == null || rutaImagen.trim().isEmpty()) ? null : rutaImagen;

        try {
            ctrl.altaEdicion(
                nickOrganizador,
                nombreEvento,
                nombreEdicion,
                sigla,
                ciudad,
                pais,
                fechaInicio,
                fechaFin,
                fechaAlta,
                imagenFinal
            );
            return "OK"; // Éxito

        } catch (Exception e) {
            // Manejar excepción si es necesario
            return e.getMessage();
        }
    }

    @WebMethod
    public String darAltaEvento(
        String nombreEvento,
        String descripcion,
        DTFecha fechaAlta,
        String sigla,
        String[] categorias,
        String rutaImagen
    ) {

    	Set<String> categoriasSet;
        if (categorias == null || categorias.length == 0) {
            categoriasSet = new HashSet<>();
        } else {
            categoriasSet = new HashSet<>(Arrays.asList(categorias));
        }

        // Convertir cadena vacía a null para imagen opcional
        String imagenFinal = (rutaImagen == null || rutaImagen.trim().isEmpty()) ? null : rutaImagen;

        try {
            ctrl.darAltaEvento(nombreEvento, descripcion, fechaAlta, sigla, categoriasSet, imagenFinal);
            return "OK"; // Éxito
        } catch (Exception e) {
            // Retornar el mensaje de error específico
            return e.getMessage();
        }
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
    public String[] listarEdicionesDeEvento(String nombreEvento) {
        try {
            Set<String> ediciones = ctrl.listarEdiciones(nombreEvento);
            return ediciones.toArray(new String[0]);
        } catch (Exception e) {
            return new String[0]; // Retornar arreglo vacío en caso de error
        }
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

    /**
     * Obtiene el detalle de un evento por su nombre.
     * Formato: [nombre, sigla, descripcion, dia, mes, anio, imagen, categorias_separadas_por_coma]
     */
    @WebMethod
    public String[] obtenerDetalleEvento(String nombreEvento) {
        try {
            Set<logica.datatypesyenum.DTEvento> eventos = ctrl.obtenerDTEventos();
            logica.datatypesyenum.DTEvento evento = null;
            
            for (logica.datatypesyenum.DTEvento e : eventos) {
                if (e.getNombre().equals(nombreEvento)) {
                    evento = e;
                    break;
                }
            }
            
            if (evento == null) {
                return new String[0];
            }
            
            String[] detalle = new String[8];
            detalle[0] = evento.getNombre();
            detalle[1] = evento.getSigla();
            detalle[2] = evento.getDescripcion();
            detalle[3] = String.valueOf(evento.getFechaEvento().getDia());
            detalle[4] = String.valueOf(evento.getFechaEvento().getMes());
            detalle[5] = String.valueOf(evento.getFechaEvento().getAnio());
            detalle[6] = evento.getImagen() != null ? evento.getImagen() : "";
            detalle[7] = evento.getCategorias() != null ? String.join(",", evento.getCategorias()) : "";
            
            return detalle;
        } catch (Exception e) {
            return new String[0];
        }
    }


    /**
     * Carga datos de prueba en los controladores del servidor (vía Publicador).
     * Retorna "OK" si se cargaron correctamente o el mensaje de la excepción en caso contrario.
     */
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
