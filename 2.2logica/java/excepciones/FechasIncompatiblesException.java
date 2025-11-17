package excepciones;

/**
 * Excepción que se lanza cuando la fecha de fin de una edición
 * es anterior a la fecha de inicio.
 */
public class FechasIncompatiblesException extends Exception {

    public FechasIncompatiblesException() {
        super("La fecha de fin no puede ser anterior a la fecha de inicio.");
    }

    public FechasIncompatiblesException(String mensaje) {
        super(mensaje);
    }
}
