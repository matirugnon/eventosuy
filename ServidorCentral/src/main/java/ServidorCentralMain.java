import publicadores.PublicadorControlador;
import publicadores.PublicadorUsuario;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import jakarta.xml.ws.Endpoint;

/**
 * Punto de entrada principal del Servidor Central.
 * Inicia ambos servicios SOAP: PublicadorControlador y PublicadorUsuario.
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
        
        // Iniciar PublicadorControlador
        String urlControlador = props.getProperty("servidor.central.url", "http://localhost:9128/publicador");
        System.out.println("\n[1/2] Publicando PublicadorControlador en: " + urlControlador);
        Endpoint.publish(urlControlador, new PublicadorControlador());
        System.out.println("✓ PublicadorControlador iniciado correctamente");
        
        // Iniciar PublicadorUsuario
        String urlUsuario = props.getProperty("servidor.usuario.url", "http://localhost:9128/publicadorUsuario");
        System.out.println("\n[2/2] Publicando PublicadorUsuario en: " + urlUsuario);
        Endpoint.publish(urlUsuario, new PublicadorUsuario());
        System.out.println("✓ PublicadorUsuario iniciado correctamente");
        
        System.out.println("\n=== Servidor Central listo ===");
        System.out.println("PublicadorControlador: " + urlControlador + "?wsdl");
        System.out.println("PublicadorUsuario: " + urlUsuario + "?wsdl");
        System.out.println("\nPresiona Ctrl+C para detener el servidor.");
    }
}
