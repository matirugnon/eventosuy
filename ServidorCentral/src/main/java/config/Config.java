package config;

import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

/**
 * Carga centralizada de las propiedades usadas para publicar y consumir SOAP.
 * Proporciona defaults seguros para correr en localhost si no existe config externa.
 */
public class Config {
    private static final Properties props = new Properties();

    static {
        // Defaults para correr todo en localhost:9128
        setDefault("publicadorControlador");
        setDefault("publicadorUsuario");
        setDefault("publicadorRegistro");
        setDefault("publicadorCargaDatos");

        boolean loaded = false;
        String home = System.getProperty("user.home", "");
        loaded = loadIfExists(Path.of(home, "config", "config.properties"));

        if (!loaded) {
            String userDir = System.getProperty("user.dir", "");
            loaded = loadIfExists(Path.of(userDir, "config", "config.properties"));
        }

        if (!loaded) {
            try (InputStream is = Config.class.getClassLoader().getResourceAsStream("config/config.properties")) {
                if (is != null) {
                    props.load(is);
                    loaded = true;
                }
            } catch (Exception ignored) {
                // Ya tenemos defaults cargados.
            }
        }

        if (!loaded) {
            System.out.println("[Config] No se encontro config.properties; usando valores por defecto localhost:9128");
        } else {
            System.out.println("[Config] Configuracion cargada correctamente");
        }
    }

    private static void setDefault(String publisher) {
        props.setProperty(publisher + ".host", props.getProperty(publisher + ".host", "localhost"));
        props.setProperty(publisher + ".port", props.getProperty(publisher + ".port", "9128"));
        String defaultPath;
        if ("publicadorControlador".equals(publisher)) {
            defaultPath = "/publicador";
        } else {
            defaultPath = "/" + publisher;
        }
        props.setProperty(publisher + ".url", props.getProperty(publisher + ".url", defaultPath));
    }

    private static boolean loadIfExists(Path path) {
        try {
            if (Files.exists(path)) {
                try (InputStream fis = new FileInputStream(path.toFile())) {
                    props.load(fis);
                }
                return true;
            }
        } catch (Exception ex) {
            System.out.println("[Config] Error leyendo " + path + ": " + ex.getMessage());
        }
        return false;
    }

    public static String getPublisherHost(String publisher) {
        return props.getProperty(publisher + ".host", "localhost");
    }

    public static int getPublisherPort(String publisher) {
        return Integer.parseInt(props.getProperty(publisher + ".port", "9128"));
    }

    public static String getPublisherUrl(String publisher) {
        return props.getProperty(publisher + ".url", defaultPathFor(publisher));
    }

    public static String get(String clave) {
        return props.getProperty(clave);
    }

    private static String defaultPathFor(String publisher) {
        if ("publicadorControlador".equals(publisher)) {
            return "/publicador";
        }
        return "/" + publisher;
    }
}
