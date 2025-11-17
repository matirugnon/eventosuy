package excepciones;

/**
 * Excepción que se lanza cuando se intenta dar de alta
 * una edición con un nombre que ya existe en la plataforma.
 */
public class EdicionExistenteException extends Exception {

    public EdicionExistenteException() {
        super("Ya existe una edición con ese nombre en la plataforma.");
    }

    public EdicionExistenteException(String mensaje) {
        super(mensaje);
    }
}
