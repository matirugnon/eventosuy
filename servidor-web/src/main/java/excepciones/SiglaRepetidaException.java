// Archivo: excepciones/SiglaRepetidaException.java

package excepciones;

import jakarta.xml.ws.WebFault;

/**
 * Excepción lanzada cuando se intenta registrar un evento o edición con una sigla que ya existe.
 */
@WebFault(name = "SiglaRepetidaFault")
public class SiglaRepetidaException extends Exception {

    public SiglaRepetidaException(String sigla) {
        super("Ya existe un evento o edición con la sigla: '" + sigla + "'. Por favor, elija otra sigla.");
    }

    public SiglaRepetidaException(String sigla, Throwable cause) {
        super("Ya existe un evento o edición con la sigla: '" + sigla + "'. Por favor, elija otra sigla.", cause);
    }
}
