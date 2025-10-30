package presentacion;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import jakarta.xml.ws.Endpoint;
import publicadores.PublicadorCargaDatos;
import publicadores.PublicadorControlador;
import publicadores.PublicadorRegistro;
import publicadores.PublicadorUsuario;

// Arranca (opcionalmente) los endpoints SOAP locales y luego muestra la GUI.
public class Main {
    public static void main(String[] args) {
        // Cargar configuración de URLs (opcional)
        String configPath = System.getProperty("user.home") + "/.eventosUy/servidor-central.properties";
        Properties props = new Properties();
        try {
            props.load(new FileInputStream(configPath));
            System.out.println("✓ Configuración cargada desde: " + configPath);
        } catch (IOException e) {
            System.err.println("⚠️ Advertencia: No se encontró el archivo de configuración en: " + configPath);
            System.err.println("   Usando valores por defecto.");
        }

        // Allow disabling publishing via system property: -Dpublish.soap=false
        boolean publishSoap = Boolean.parseBoolean(System.getProperty("publish.soap", "true"));
        if (publishSoap) {
            try {
                String urlCarga = props.getProperty("servidor.cargaDatos.url", "http://localhost:9128/publicadorCargaDatos");
                String urlControlador = props.getProperty("servidor.central.url", "http://localhost:9128/publicador");
                String urlRegistro = props.getProperty("servidor.registro.url", "http://localhost:9128/publicadorRegistro");
                String urlUsuario = props.getProperty("servidor.usuario.url", "http://localhost:9128/publicadorUsuario");

                System.out.println("Publicando servicios SOAP locales (para que la web pueda consultarlos):");
                System.out.println(" - PublicadorCargaDatos: " + urlCarga);
                System.out.println(" - PublicadorControlador: " + urlControlador);
                System.out.println(" - PublicadorRegistro: " + urlRegistro);
                System.out.println(" - PublicadorUsuario: " + urlUsuario);

                Endpoint.publish(urlCarga, new PublicadorCargaDatos());
                Endpoint.publish(urlControlador, new PublicadorControlador());
                Endpoint.publish(urlRegistro, new PublicadorRegistro());
                Endpoint.publish(urlUsuario, new PublicadorUsuario());

                System.out.println("✓ Servicios SOAP publicados localmente.");
            } catch (Exception ex) {
                System.err.println("No se pudieron publicar los servicios SOAP: " + ex.getMessage());
                System.err.println("Si ya hay un servidor SOAP corriendo en esas URLs, los servicios seguirán funcionando en ese servidor.");
            }
        } else {
            System.out.println("Avanzando sin publicar servicios SOAP locales (publish.soap=false).");
            System.out.println("Asegurate de que exista un servidor SOAP central activo para que la web funcione correctamente.");
        }

        javax.swing.SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}