package excepciones;

/**
 * Excepción que se lanza cuando se intenta dar de alta
 * una institución que ya existe en el sistema.
 */
public class ExisteInstitucionException extends Exception {

    public ExisteInstitucionException() {
        super("La institución ya existe en el sistema.");
    }

    public ExisteInstitucionException(String mensaje) {
        super(mensaje);
    }
}
