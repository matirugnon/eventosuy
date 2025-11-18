// Archivo: excepciones/PatrocinioDuplicadoException.java

package excepciones;

import jakarta.xml.ws.WebFault;

/**
 * Excepción lanzada cuando se intenta registrar un patrocinio que ya existe
 * entre una institución y una edición.
 */
@WebFault(name = "PatrocinioDuplicadoFault")
public class PatrocinioDuplicadoException extends Exception {

    public PatrocinioDuplicadoException(String nomInstitucion, String nomEdicion) {
        super("La institución '" + nomInstitucion + "' ya es patrocinadora de la edición '" + nomEdicion + "'.");
    }

    public PatrocinioDuplicadoException(String mensaje) {
        super(mensaje);
    }
}