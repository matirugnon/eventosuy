// Archivo: excepciones/PatrocinioDuplicadoException.java

package excepciones;

/**
 * Excepción lanzada cuando se intenta registrar un patrocinio que ya existe
 * entre una institución y una edición.
 */
public class PatrocinioDuplicadoException extends Exception {

    public PatrocinioDuplicadoException(String nomInstitucion, String nomEdicion) {
        super("La institución '" + nomInstitucion + "' ya es patrocinadora de la edición '" + nomEdicion + "'.");
    }

    public PatrocinioDuplicadoException(String mensaje) {
        super(mensaje);
    }
}