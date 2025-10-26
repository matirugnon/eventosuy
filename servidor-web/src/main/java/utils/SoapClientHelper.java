package utils;

import soap.PublicadorControlador;
import soap.PublicadorControladorService;
import soap.PublicadorUsuario;
import soap.PublicadorUsuarioService;

/**
 * Helper class para obtener el cliente SOAP del PublicadorControlador
 */
public class SoapClientHelper {
    
    private static PublicadorControlador publicadorPort = null;
    private static PublicadorUsuario publicadorUsuarioPort = null;
    
    /**
     * Obtiene una instancia del puerto del PublicadorControlador
     * Utiliza patrón Singleton para reutilizar la conexión
     */
    public static PublicadorControlador getPublicadorControlador() {
        if (publicadorPort == null) {
            try {
                PublicadorControladorService service = new PublicadorControladorService();
                publicadorPort = service.getPublicadorControladorPort();
                System.out.println("✓ Conexión SOAP establecida con el PublicadorControlador");
            } catch (Exception e) {
                System.err.println("❌ Error al conectar con el PublicadorControlador: " + e.getMessage());
                throw new RuntimeException("No se pudo conectar con el servidor SOAP", e);
            }
        }
        return publicadorPort;
    }

    /**
     * Obtiene una instancia del puerto del PublicadorUsuario (SOAP client)
     */
    public static PublicadorUsuario getPublicadorUsuario() {
        if (publicadorUsuarioPort == null) {
            try {
                PublicadorUsuarioService service = new PublicadorUsuarioService();
                publicadorUsuarioPort = service.getPublicadorUsuarioPort();
                System.out.println("✓ Conexión SOAP establecida con el PublicadorUsuario");
            } catch (Exception e) {
                System.err.println("❌ Error al conectar con el PublicadorUsuario: " + e.getMessage());
                throw new RuntimeException("No se pudo conectar con el servidor SOAP (usuario)", e);
            }
        }
        return publicadorUsuarioPort;
    }
    
    /**
     * Reinicia la conexión SOAP (útil si el servidor se reinicia)
     */
    public static void resetConnection() {
        publicadorPort = null;
        publicadorUsuarioPort = null;
    }
    
    /**
     * Verifica si el servidor SOAP está disponible
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
