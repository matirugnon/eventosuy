package publicadores;

import config.Config;
import jakarta.jws.WebMethod;
import jakarta.jws.WebService;
import jakarta.jws.soap.SOAPBinding;
import jakarta.xml.ws.Endpoint;
import jakarta.jws.soap.SOAPBinding.Style;

@WebService
@SOAPBinding(style = Style.RPC)

public class PublicadorPrueba {

    @WebMethod
    public String hola() {
        return "Hola desde el Servidor Central";
    }

    public static void main(String[] args) {
        String hostProp = System.getProperty("publicadorPrueba.host");
        String portProp = System.getProperty("publicadorPrueba.port");
        String urlProp  = System.getProperty("publicadorPrueba.url");

        String host = (hostProp != null)
                ? hostProp
                : Config.getPublisherHost("publicadorPrueba");

        int port = (portProp != null)
                ? Integer.parseInt(portProp)
                : Config.getPublisherPort("publicadorPrueba");

        String path = (urlProp != null)
                ? urlProp
                : Config.getPublisherUrl("publicadorPrueba");

        String url = "http://" + host + ":" + port + path;

        System.out.println("Publicando servicio en: " + url);
        Endpoint.publish(url, new PublicadorPrueba());
    }

}
