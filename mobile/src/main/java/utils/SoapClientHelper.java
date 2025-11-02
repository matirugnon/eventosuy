package utils;

import soap.PublicadorCargaDatos;
import soap.PublicadorCargaDatosService;
import soap.PublicadorControlador;
import soap.PublicadorControladorService;
import soap.PublicadorRegistro;
import soap.PublicadorRegistroService;
import soap.PublicadorUsuario;
import soap.PublicadorUsuarioService;

/**
 * Helper para obtener y reutilizar los clientes SOAP de los publicadores del servidor central.
 */
public final class SoapClientHelper {

    private static PublicadorControlador publicadorPort;
    private static PublicadorUsuario publicadorUsuarioPort;
    private static PublicadorRegistro publicadorRegistroPort;
    private static PublicadorCargaDatos publicadorCargaDatosPort;

    private SoapClientHelper() {
        // Helper de utilidades, no instanciable.
    }

    /**
     * Obtiene una instancia singleton del publicador controlador.
     */
    public static PublicadorControlador getPublicadorControlador() {
        if (publicadorPort == null) {
            try {
                PublicadorControladorService service = new PublicadorControladorService();
                publicadorPort = service.getPublicadorControladorPort();
                System.out.println("Conexion SOAP establecida con el PublicadorControlador");
            } catch (Exception e) {
                System.err.println("Error al conectar con el PublicadorControlador: " + e.getMessage());
                throw new RuntimeException("No se pudo conectar con el servidor SOAP", e);
            }
        }
        return publicadorPort;
    }

    /**
     * Obtiene una instancia singleton del publicador de usuarios.
     */
    public static PublicadorUsuario getPublicadorUsuario() {
        if (publicadorUsuarioPort == null) {
            try {
                PublicadorUsuarioService service = new PublicadorUsuarioService();
                publicadorUsuarioPort = service.getPublicadorUsuarioPort();
                System.out.println("Conexion SOAP establecida con el PublicadorUsuario");
            } catch (Exception e) {
                System.err.println("Error al conectar con el PublicadorUsuario: " + e.getMessage());
                throw new RuntimeException("No se pudo conectar con el servidor SOAP (usuario)", e);
            }
        }
        return publicadorUsuarioPort;
    }

    /**
     * Obtiene una instancia singleton del publicador de registros.
     */
    public static PublicadorRegistro getPublicadorRegistro() {
        if (publicadorRegistroPort == null) {
            try {
                PublicadorRegistroService service = new PublicadorRegistroService();
                publicadorRegistroPort = service.getPublicadorRegistroPort();
                System.out.println("Conexion SOAP establecida con el PublicadorRegistro");
            } catch (Exception e) {
                System.err.println("Error al conectar con el PublicadorRegistro: " + e.getMessage());
                throw new RuntimeException("No se pudo conectar con el servidor SOAP (registro)", e);
            }
        }
        return publicadorRegistroPort;
    }

    /**
     * Obtiene una instancia singleton del publicador de carga de datos.
     */
    public static PublicadorCargaDatos getPublicadorCargaDatos() {
        if (publicadorCargaDatosPort == null) {
            try {
                PublicadorCargaDatosService service = new PublicadorCargaDatosService();
                publicadorCargaDatosPort = service.getPublicadorCargaDatosPort();
                System.out.println("Conexion SOAP establecida con el PublicadorCargaDatos");
            } catch (Exception e) {
                System.err.println("Error al conectar con el PublicadorCargaDatos: " + e.getMessage());
                throw new RuntimeException("No se pudo conectar con el servidor SOAP (cargaDatos)", e);
            }
        }
        return publicadorCargaDatosPort;
    }

    /**
     * Reinicia las referencias a los clientes SOAP, útil si el servidor central se reinicia.
     */
    public static void resetConnection() {
        publicadorPort = null;
        publicadorUsuarioPort = null;
        publicadorRegistroPort = null;
        publicadorCargaDatosPort = null;
    }

    /**
     * Verifica si el servidor central está disponible realizando una llamada simple.
     */
    public static boolean isServerAvailable() {
        try {
            PublicadorControlador port = getPublicadorControlador();
            String response = port.hola();
            return response != null && !response.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }
}
