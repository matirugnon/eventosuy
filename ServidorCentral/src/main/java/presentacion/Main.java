package presentacion;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import jakarta.xml.ws.Endpoint;
import publicadores.PublicadorCargaDatos;
import publicadores.PublicadorControlador;
import publicadores.PublicadorRegistro;
import publicadores.PublicadorUsuario;
import config.Config;

public class Main {
    public static void main(String[] args) {

        // Allow disabling publishing via system property: -Dpublish.soap=false
        boolean publishSoap = Boolean.parseBoolean(System.getProperty("publish.soap", "true"));

        if (publishSoap) {
            try {
                // ===== PublicadorCargaDatos =====
                String hostCarga = System.getProperty("publicadorCargaDatos.host");
                if (hostCarga == null) hostCarga = Config.getPublisherHost("publicadorCargaDatos");

                String portCarga = System.getProperty("publicadorCargaDatos.port");
                if (portCarga == null) portCarga = String.valueOf(Config.getPublisherPort("publicadorCargaDatos"));

                String pathCarga = System.getProperty("publicadorCargaDatos.url");
                if (pathCarga == null) pathCarga = Config.getPublisherUrl("publicadorCargaDatos");

                String urlCarga = "http://" + hostCarga + ":" + portCarga + pathCarga;

                // ===== PublicadorControlador =====
                String hostCtrl = System.getProperty("publicadorControlador.host");
                if (hostCtrl == null) hostCtrl = Config.getPublisherHost("publicadorControlador");

                String portCtrl = System.getProperty("publicadorControlador.port");
                if (portCtrl == null) portCtrl = String.valueOf(Config.getPublisherPort("publicadorControlador"));

                String pathCtrl = System.getProperty("publicadorControlador.url");
                if (pathCtrl == null) pathCtrl = Config.getPublisherUrl("publicadorControlador");

                String urlControlador = "http://" + hostCtrl + ":" + portCtrl + pathCtrl;

                // ===== PublicadorRegistro =====
                String hostReg = System.getProperty("publicadorRegistro.host");
                if (hostReg == null) hostReg = Config.getPublisherHost("publicadorRegistro");

                String portReg = System.getProperty("publicadorRegistro.port");
                if (portReg == null) portReg = String.valueOf(Config.getPublisherPort("publicadorRegistro"));

                String pathReg = System.getProperty("publicadorRegistro.url");
                if (pathReg == null) pathReg = Config.getPublisherUrl("publicadorRegistro");

                String urlRegistro = "http://" + hostReg + ":" + portReg + pathReg;

                // ===== PublicadorUsuario =====
                String hostUsr = System.getProperty("publicadorUsuario.host");
                if (hostUsr == null) hostUsr = Config.getPublisherHost("publicadorUsuario");

                String portUsr = System.getProperty("publicadorUsuario.port");
                if (portUsr == null) portUsr = String.valueOf(Config.getPublisherPort("publicadorUsuario"));

                String pathUsr = System.getProperty("publicadorUsuario.url");
                if (pathUsr == null) pathUsr = Config.getPublisherUrl("publicadorUsuario");

                String urlUsuario = "http://" + hostUsr + ":" + portUsr + pathUsr;

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