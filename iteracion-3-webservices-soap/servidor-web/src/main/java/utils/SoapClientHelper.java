package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

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

    private static final Properties props = new Properties();

    // Cargamos la config UNA vez (mismo archivo que usa tomcat.sh / servidor central)
    static {
        String configPath = System.getProperty("user.home") + "/config/config.properties";
        try (FileInputStream fis = new FileInputStream(configPath)) {
            props.load(fis);
            System.out.println("✓ SoapClientHelper: config cargada desde " + configPath);
        } catch (IOException e) {
            System.err.println("⚠️ SoapClientHelper: no se pudo leer " + configPath +
                               " -> usando defaults localhost:9115");
        }
    }

    private static String get(String key, String def) {
        String v = props.getProperty(key);
        return (v == null || v.isBlank()) ? def : v.trim();
    }

    private static PublicadorControlador publicadorPort;
    private static PublicadorUsuario publicadorUsuarioPort;
    private static PublicadorRegistro publicadorRegistroPort;
    private static PublicadorCargaDatos publicadorCargaDatosPort;

    private SoapClientHelper() {
        // Helper de utilidades, no instanciable.
    }

    // =========================
    //  PublicadorControlador
    // =========================
    public static PublicadorControlador getPublicadorControlador() {
        if (publicadorPort == null) {
            try {
                String host = get("publicadorControlador.host", "localhost");
                String port = get("publicadorControlador.port", "9115");
                String path = get("publicadorControlador.url", "/publicador");

                String base = "http://" + host + ":" + port + path;
                URL wsdlUrl = new URL(base + "?wsdl");

                System.out.println("SoapClientHelper -> WSDL Controlador: " + wsdlUrl);

                // Usamos el constructor CON URL → ignora el hardcode de 9128
                PublicadorControladorService service = new PublicadorControladorService(wsdlUrl);
                publicadorPort = service.getPublicadorControladorPort();
                System.out.println("Conexión SOAP establecida con el PublicadorControlador");
            } catch (Exception e) {
                System.err.println("Error al conectar con el PublicadorControlador: " + e.getMessage());
                throw new RuntimeException("No se pudo conectar con el servidor SOAP (controlador)", e);
            }
        }
        return publicadorPort;
    }

    // =========================
    //  PublicadorUsuario
    // =========================
    public static PublicadorUsuario getPublicadorUsuario() {
        if (publicadorUsuarioPort == null) {
            try {
                String host = get("publicadorUsuario.host", "localhost");
                String port = get("publicadorUsuario.port", "9115");
                String path = get("publicadorUsuario.url", "/publicadorUsuario");

                String base = "http://" + host + ":" + port + path;
                URL wsdlUrl = new URL(base + "?wsdl");

                System.out.println("SoapClientHelper -> WSDL Usuario: " + wsdlUrl);

                PublicadorUsuarioService service = new PublicadorUsuarioService(wsdlUrl);
                publicadorUsuarioPort = service.getPublicadorUsuarioPort();
                System.out.println("Conexión SOAP establecida con el PublicadorUsuario");
            } catch (Exception e) {
                System.err.println("Error al conectar con el PublicadorUsuario: " + e.getMessage());
                throw new RuntimeException("No se pudo conectar con el servidor SOAP (usuario)", e);
            }
        }
        return publicadorUsuarioPort;
    }

    // =========================
    //  PublicadorRegistro
    // =========================
    public static PublicadorRegistro getPublicadorRegistro() {
        if (publicadorRegistroPort == null) {
            try {
                String host = get("publicadorRegistro.host", "localhost");
                String port = get("publicadorRegistro.port", "9115");
                String path = get("publicadorRegistro.url", "/publicadorRegistro");

                String base = "http://" + host + ":" + port + path;
                URL wsdlUrl = new URL(base + "?wsdl");

                System.out.println("SoapClientHelper -> WSDL Registro: " + wsdlUrl);

                PublicadorRegistroService service = new PublicadorRegistroService(wsdlUrl);
                publicadorRegistroPort = service.getPublicadorRegistroPort();
                System.out.println("Conexión SOAP establecida con el PublicadorRegistro");
            } catch (Exception e) {
                System.err.println("Error al conectar con el PublicadorRegistro: " + e.getMessage());
                throw new RuntimeException("No se pudo conectar con el servidor SOAP (registro)", e);
            }
        }
        return publicadorRegistroPort;
    }

    // =========================
    //  PublicadorCargaDatos
    // =========================
    public static PublicadorCargaDatos getPublicadorCargaDatos() {
        if (publicadorCargaDatosPort == null) {
            try {
                String host = get("publicadorCargaDatos.host", "localhost");
                String port = get("publicadorCargaDatos.port", "9115");
                String path = get("publicadorCargaDatos.url", "/publicadorCargaDatos");

                String base = "http://" + host + ":" + port + path;
                URL wsdlUrl = new URL(base + "?wsdl");

                System.out.println("SoapClientHelper -> WSDL CargaDatos: " + wsdlUrl);

                PublicadorCargaDatosService service = new PublicadorCargaDatosService(wsdlUrl);
                publicadorCargaDatosPort = service.getPublicadorCargaDatosPort();
                System.out.println("Conexión SOAP establecida con el PublicadorCargaDatos");
            } catch (Exception e) {
                System.err.println("Error al conectar con el PublicadorCargaDatos: " + e.getMessage());
                throw new RuntimeException("No se pudo conectar con el servidor SOAP (cargaDatos)", e);
            }
        }
        return publicadorCargaDatosPort;
    }

    /** Reinicia las referencias a los clientes SOAP, útil si el servidor central se reinicia. */
    public static void resetConnection() {
        publicadorPort = null;
        publicadorUsuarioPort = null;
        publicadorRegistroPort = null;
        publicadorCargaDatosPort = null;
    }

    /** Verifica si el servidor central está disponible realizando una llamada simple. */
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

