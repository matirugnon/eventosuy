import publicadores.PublicadorCargaDatos;
import publicadores.PublicadorControlador;
import publicadores.PublicadorRegistro;
import publicadores.PublicadorUsuario;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import jakarta.xml.ws.Endpoint;

/**
 * Punto de entrada principal del Servidor Central.
 * Inicia todos los servicios SOAP.
 */
public class ServidorCentralMain {

    public static void main(String[] args) {
        System.out.println("=== Iniciando Servidor Central ===");

        // Cargar configuración
        String configPath = System.getProperty("user.home") + "/.eventosUy/servidor-central.properties";
        Properties props = new Properties();

        try {
            props.load(new FileInputStream(configPath));
            System.out.println("✓ Configuración cargada desde: " + configPath);
        } catch (IOException e) {
            System.err.println("⚠️ Advertencia: No se encontró el archivo de configuración en: " + configPath);
            System.err.println("   Usando valores por defecto.");
        }

        // PublicadorCargaDatos
        String urlCarga = props.getProperty("servidor.cargaDatos.url", "http://localhost:9128/publicadorCargaDatos");
        System.out.println("\n[1/4] Publicando PublicadorCargaDatos en: " + urlCarga);
        Endpoint.publish(urlCarga, new PublicadorCargaDatos());
        System.out.println("✓ PublicadorCargaDatos iniciado");

        // PublicadorControlador
        String urlControlador = props.getProperty("servidor.central.url", "http://localhost:9128/publicador");
        System.out.println("\n[2/4] Publicando PublicadorControlador en: " + urlControlador);
        Endpoint.publish(urlControlador, new PublicadorControlador());
        System.out.println("✓ PublicadorControlador iniciado");

        // PublicadorRegistro
        String urlRegistro = props.getProperty("servidor.registro.url", "http://localhost:9128/publicadorRegistro");
        System.out.println("\n[3/4] Publicando PublicadorRegistro en: " + urlRegistro);
        Endpoint.publish(urlRegistro, new PublicadorRegistro());
        System.out.println("✓ PublicadorRegistro iniciado");

        // PublicadorUsuario
        String urlUsuario = props.getProperty("servidor.usuario.url", "http://localhost:9128/publicadorUsuario");
        System.out.println("\n[4/4] Publicando PublicadorUsuario en: " + urlUsuario);
        Endpoint.publish(urlUsuario, new PublicadorUsuario());
        System.out.println("✓ PublicadorUsuario iniciado");

        System.out.println("\n=== Servidor Central listo ===");
        System.out.println("PublicadorCargaDatos: " + urlCarga + "?wsdl");
        System.out.println("PublicadorControlador: " + urlControlador + "?wsdl");
        System.out.println("PublicadorRegistro: " + urlRegistro + "?wsdl");
        System.out.println("PublicadorUsuario: " + urlUsuario + "?wsdl");
        System.out.println("\nPresiona Ctrl+C para detener el servidor.");
    }
}

