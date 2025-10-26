package excepciones;

import jakarta.xml.ws.WebFault;

/**
 * Excepción utilizada para indicar la existencia de un usuario repetido en el sistema.
 * 
 * @author TProg2017
 *
 */
@WebFault(name = "UsuarioRepetidoFault")
@SuppressWarnings("serial")
public class UsuarioRepetidoException extends Exception {

    public UsuarioRepetidoException(String string) {
        super(string);
    }

}
