package excepciones;

/**
 * Excepción lanzada cuando un correo electrónico no contiene el símbolo '@'.
 */
public class CorreoInvalidoException extends Exception {

    public CorreoInvalidoException(String correo) {
        super("El correo electrónico '" + correo + "' no es válido: debe contener el símbolo '@'.");
    }

    public CorreoInvalidoException(String correo, Throwable cause) {
        super("El correo electrónico '" + correo + "' no es válido: debe contener el símbolo '@'.", cause);
    }
}