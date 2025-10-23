package publicadores;

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
        String url = "http://localhost:9128/publicador";
        System.out.println("Publicando servicio en: " + url);
        Endpoint.publish(url, new PublicadorPrueba());
    }
}
