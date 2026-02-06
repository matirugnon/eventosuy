// Archivo: excepciones/EdicionNoExisteException.java

package excepciones;

import jakarta.xml.ws.WebFault;

/**
 * Excepción lanzada cuando se intenta acceder a una edición que no existe.
 */
@WebFault(name = "EdicionNoExisteFault")
public class EdicionNoExisteException extends Exception {

    public EdicionNoExisteException(String nombreEdicion) {
        super("No existe la edición: '" + nombreEdicion + "'.");
    }
}