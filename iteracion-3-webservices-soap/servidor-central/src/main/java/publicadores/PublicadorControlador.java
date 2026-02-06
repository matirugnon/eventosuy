package publicadores;

import jakarta.jws.WebMethod;

import jakarta.jws.WebService;
import jakarta.jws.soap.SOAPBinding;
import jakarta.jws.soap.SOAPBinding.Style;
import jakarta.xml.ws.Endpoint;

import logica.controladores.IControladorEvento;
import logica.controladores.IControladorUsuario;
import logica.controladores.IControladorRegistro;
import utils.Utils;

import logica.datatypesyenum.DTEvento;
import logica.datatypesyenum.DTFecha;
import logica.datatypesyenum.DTPatrocinio;
import logica.datatypesyenum.NivelPatrocinio;
import logica.datatypesyenum.EstadoEdicion;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Set;

import config.Config;
import excepciones.EventoNoExisteException;

import java.util.Arrays;
import java.util.HashSet;


@WebService
@SOAPBinding(style = Style.RPC)
public class PublicadorControlador {

    private final IControladorEvento ctrl = IControladorEvento.getInstance();
    @WebMethod
    public String hola() {
        return "Hola desde el Servidor Central";
    }

    @WebMethod
    public String obtenerEventos() {
        return String.join(", ", ctrl.listarEventos());
    }

    public static void main(String[] args) {
        // 1) Intentar leer host/port/url desde -D si el script los pasa
        String hostProp = System.getProperty("publicadorControlador.host");
        String portProp = System.getProperty("publicadorControlador.port");
        String urlProp  = System.getProperty("publicadorControlador.url");

        // 2) Si no vienen por -D, usar config.Config (~/config/config.properties)
        String host = (hostProp != null)
                ? hostProp
                : Config.getPublisherHost("publicadorControlador");

        int port = (portProp != null)
                ? Integer.parseInt(portProp)
                : Config.getPublisherPort("publicadorControlador");

        String path = (urlProp != null)
                ? urlProp
                : Config.getPublisherUrl("publicadorControlador");

        String url = "http://" + host + ":" + port + path;

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
        String rutaImagen,
        String url

    ) {

        String imagenFinal = (rutaImagen == null || rutaImagen.trim().isEmpty()) ? null : rutaImagen;

        try {
            ctrl.altaEdicion(
                nombreEvento,
                nickOrganizador,
                nombreEdicion,
                sigla,
                ciudad,
                pais,
                fechaInicio,
                fechaFin,
                fechaAlta,
                imagenFinal,
                url
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
    public String[] listarEdicionesAceptadasDeEvento(String nombreEvento) {
        try {
            Set<String> ediciones = ctrl.listarEdicionesPorEstadoDeEvento(nombreEvento, EstadoEdicion.ACEPTADA);
            return ediciones.toArray(new String[0]);
        } catch (Exception e) {
            return new String[0]; // Retornar arreglo vacío en caso de error
        }
    }

    /**
     * Obtiene los detalles de una edición específica.
     * Retorna un array con: [0] nombre, [1] imagen (o cadena vacía si no tiene)
     */
    

    
    
    
    @WebMethod
    public String[] obtenerDetalleEdicion(String nombreEdicion) {
        try {
            logica.datatypesyenum.DTEdicion edicion = ctrl.consultarEdicion(nombreEdicion);
            if (edicion == null) {
                return new String[0];
            }
            
            String[] detalle = new String[2];
            detalle[0] = edicion.getNombre();
            detalle[1] = edicion.getImagen() != null ? edicion.getImagen() : "";
            
            return detalle;
        } catch (Exception e) {
            return new String[0];
        }
    }

    /**
     * Obtiene el detalle completo de una edición para la página de consulta.
     * Retorna un array con: [0] nombre, [1] sigla, [2] ciudad, [3] pais,
     * [4] fechaInicioDia, [5] fechaInicioMes, [6] fechaInicioAnio,
     * [7] fechaFinDia, [8] fechaFinMes, [9] fechaFinAnio,
     * [10] organizador, [11] imagen (o cadena vacía si no tiene), [12] estado,
     * [13] evento
     */
    @WebMethod
    public String[] obtenerDetalleCompletoEdicion(String nombreEdicion) {
        try {
            logica.datatypesyenum.DTEdicion edicion = ctrl.consultarEdicion(nombreEdicion);
            if (edicion == null) {
                return new String[0];
            }
            
            // Necesitamos 14 posiciones: índices 0..13
            String[] detalle = new String[14];
            detalle[0] = edicion.getNombre();
            detalle[1] = edicion.getSigla();
            detalle[2] = edicion.getCiudad();
            detalle[3] = edicion.getPais();
            detalle[4] = String.valueOf(edicion.getFechaInicio().getDia());
            detalle[5] = String.valueOf(edicion.getFechaInicio().getMes());
            detalle[6] = String.valueOf(edicion.getFechaInicio().getAnio());
            detalle[7] = String.valueOf(edicion.getFechaFin().getDia());
            detalle[8] = String.valueOf(edicion.getFechaFin().getMes());
            detalle[9] = String.valueOf(edicion.getFechaFin().getAnio());
            detalle[10] = edicion.getOrganizador();
            detalle[11] = edicion.getImagen() != null ? edicion.getImagen() : "";
            detalle[12] = edicion.getEstado() != null ? edicion.getEstado().toString() : "";
            detalle[13] = edicion.getEvento();
            return detalle;
        } catch (Exception e) {
            return new String[0];
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
    
  
    
    public DTEvento[] obtenerDTEventos(){
    	return ctrl.obtenerDTEventos().toArray(new DTEvento[0]);
    }
    
    public DTEvento obtenerEventoPorEdicion(String nomEdicion){
    	return ctrl.obtenerEventoPorEdicion(nomEdicion);
    }
    
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

     @WebMethod
    public String obtenerEventoDeEdicion(String nombreEdicion) {
        try {
            String nombreEvento = ctrl.obtenerEventoDeEdicion(nombreEdicion);
            return nombreEvento;
        } catch (Exception e) {
            return "";
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
    
    /**
     * Consulta una edición y devuelve el DTEdicion completo
     */
    @WebMethod
    public logica.datatypesyenum.DTEdicion consultarEdicion(String nombreEdicion) {
        try {
            return ctrl.consultarEdicion(nombreEdicion);
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * Obtiene un registro específico filtrando por asistente, edición y tipo
     */
    @WebMethod
    public logica.datatypesyenum.DTRegistro obtenerRegistro(String asistente, String edicion, String tipoRegistro) {
        try {
            IControladorRegistro ctrlReg = IControladorRegistro.getInstance();
            Set<logica.datatypesyenum.DTRegistro> registros = ctrlReg.listarRegistrosPorAsistente(asistente);
            
            // Filtrar por edición y tipo de registro
            for (logica.datatypesyenum.DTRegistro reg : registros) {
                if (reg.getNomEdicion().equals(edicion) && reg.getTipoDeRegistro().equals(tipoRegistro)) {
                    return reg;
                }
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }
    @WebMethod
    public logica.datatypesyenum.DTPatrocinio consultarTipoPatrocinioEdicion(String nombreEdicion, String codigoPatrocinio) {
        try {
            return ctrl.consultarTipoPatrocinioEdicion(nombreEdicion, codigoPatrocinio);
        } catch (Exception e) {
            return null;
        }
    }

    @WebMethod
    public boolean existeCodigoPatrocinioEnEdicion(String nombreEdicion, String codigoPatrocinio) {
        try {
            return ctrl.existeCodigoPatrocinioEnEdicion(nombreEdicion, codigoPatrocinio);
        } catch (Exception e) {
            return false;
        }
    }

    @WebMethod
    public String[] listarEdicionesActivasDeEvento(String nombreEvento) {
        try {
            Set<String> ediciones = ctrl.listarEdicionesActivas(nombreEvento);
            return ediciones.toArray(new String[0]);
        } catch (Exception e) {
            return new String[0]; // Retornar arreglo vacío en caso de error
        }
    }

    public String[] listarEdicionesActivasAceptadas(String nombreEvento) {
        try {
            Set<String> ediciones = ctrl.listarEdicionesPorEstadoDeEvento(nombreEvento, EstadoEdicion.ACEPTADA);
            Set<String> edicionesActivas = ctrl.listarEdicionesActivas(nombreEvento);
            
            ediciones.retainAll(edicionesActivas); // Intersecar ambos conjuntos
            return ediciones.toArray(new String[0]);
        } catch (Exception e) {
            return new String[0]; // Retornar arreglo vacío en caso de error
        }
    }





    @WebMethod
    public String finalizarEvento(String nombreEvento) {
        try {
            ctrl.finalizarEvento(nombreEvento);
            return "OK";
        } catch (Exception e) {
            String mensaje = e.getMessage();
            return (mensaje != null && !mensaje.isBlank()) ? mensaje : "No se pudo finalizar el evento.";
        }
    }

    @WebMethod
    public boolean esEventoFinalizado(String nombreEvento) {
        try {
            return ctrl.esEventoFinalizado(nombreEvento);
        } catch (Exception e) {
            return false;
        }
    }

    @WebMethod
    public String altaPatrocinio(String edicion, String institucion, NivelPatrocinio nivel, double aporte,
                                 String tipoRegistro, int registrosGratuitos, String codigo, DTFecha fechaAlta) {
        try {
            ctrl.altaPatrocinio(edicion, institucion, nivel, aporte, tipoRegistro, registrosGratuitos, codigo, fechaAlta);
            return "OK";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @WebMethod
    public DTEvento obtenerDTEvento(String nombreEvento) {
        try {
            return ctrl.obtenerDTEvento(nombreEvento);
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * Archiva una edición de evento.
     * Solo se pueden archivar ediciones que ya han finalizado.
     */
    @WebMethod
    public String archivarEdicion(String nombreEdicion) {
        try {
            ctrl.archivarEdicion(nombreEdicion);
            return "OK";
        } catch (Exception e) {
            return e.getMessage();
        }
    }
    
    /**
     * Lista las ediciones que pueden ser archivadas por un organizador.
     * Retorna ediciones finalizadas y aceptadas que aún no están archivadas.
     */
    @WebMethod
    public String[] listarEdicionesArchivables(String nicknameOrganizador) {
        try {
            Set<logica.datatypesyenum.DTEdicion> ediciones = ctrl.listarEdicionesArchivables(nicknameOrganizador);
            return ediciones.stream()
                .map(e -> e.getNombre())
                .toArray(String[]::new);
        } catch (Exception e) {
            return new String[0];
        }
    }
    
    /**
     * Verifica si una edición está archivada.
     */
    @WebMethod
    public boolean estaEdicionArchivada(String nombreEdicion) {
        try {
            return ctrl.estaEdicionArchivada(nombreEdicion);
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Lista las ediciones archivadas de un organizador.
     */
    @WebMethod
    public String[] listarEdicionesArchivadasPorOrganizador(String nicknameOrganizador) {
        try {
            Set<String> ediciones = ctrl.listarEdicionesArchivadasPorOrganizador(nicknameOrganizador);
            return ediciones.toArray(new String[0]);
        } catch (Exception e) {
            return new String[0];
        }
    }
    
    @WebMethod
    public boolean incrementarVisitas(String nomEvento) throws EventoNoExisteException{
    	ctrl.incrementarVisitas(nomEvento);
    	return true;
    	
    }
    
    /**
     * Lista las ediciones por estado.
     * @param estado El estado de la edición (ACEPTADA, FINALIZADA, etc.)
     * @return Array con los nombres de las ediciones
     */
    @WebMethod
    public String[] listarEdicionesPorEstado(EstadoEdicion estado) {
        try {
            Set<String> resultado = ctrl.listarEdicionesPorEstado(estado);
            return resultado.toArray(new String[0]);
        } catch (Exception e) {
            return new String[0];
        }
    }

  
    


}
