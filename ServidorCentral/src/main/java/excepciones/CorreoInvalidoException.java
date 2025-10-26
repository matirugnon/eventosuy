package excepciones;

import jakarta.xml.ws.WebFault;

/**
 * Excepción lanzada cuando un correo electrónico no contiene el símbolo '@'.
 */
@WebFault(name = "CorreoInvalidoFault")
public class CorreoInvalidoException extends Exception {

    public CorreoInvalidoException(String correo) {
        super("El correo electrónico '" + correo + "' no es válido: debe contener el símbolo '@'.");
    }

    public CorreoInvalidoException(String correo, Throwable cause) {
        super("El correo electrónico '" + correo + "' no es válido: debe contener el símbolo '@'.", cause);
    }
}