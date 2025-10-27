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
 * Helper class para obtener el cliente SOAP del PublicadorControlador
 */
public class SoapClientHelper {
    
    private static PublicadorControlador publicadorPort = null;
    private static PublicadorUsuario publicadorUsuarioPort = null;
    private static PublicadorRegistro publicadorRegistroPort = null;
    private static PublicadorCargaDatos publicadorCargaDatosPort = null;
    
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
    
    public static PublicadorRegistro getPublicadorRegistro() {
        if (publicadorRegistroPort == null) {
            try {
                PublicadorRegistroService service = new PublicadorRegistroService();
                publicadorRegistroPort = service.getPublicadorRegistroPort();
                System.out.println("✓ Conexión SOAP establecida con el PublicadorRegistro");
            } catch (Exception e) {
                System.err.println("❌ Error al conectar con el PublicadorRegistro: " + e.getMessage());
                throw new RuntimeException("No se pudo conectar con el servidor SOAP (registro)", e);
            }
        }
        return publicadorRegistroPort;
    }
    
    public static PublicadorCargaDatos getPublicadorCargaDatos() {
        if (publicadorCargaDatosPort == null) {
            try {
                PublicadorCargaDatosService service = new PublicadorCargaDatosService();
                publicadorCargaDatosPort = service.getPublicadorCargaDatosPort();
                System.out.println("✓ Conexión SOAP establecida con el PublicadorCargaDatos");
            } catch (Exception e) {
                System.err.println("❌ Error al conectar con el PublicadorCargaDatos: " + e.getMessage());
                throw new RuntimeException("No se pudo conectar con el servidor SOAP (cargaDatos)", e);
            }
        }
        return publicadorCargaDatosPort;
    }
    
    /**
     * Reinicia la conexión SOAP (útil si el servidor se reinicia)
     */
    public static void resetConnection() {
        publicadorPort = null;
        publicadorUsuarioPort = null;
        publicadorRegistroPort = null;
        publicadorCargaDatosPort = null;
