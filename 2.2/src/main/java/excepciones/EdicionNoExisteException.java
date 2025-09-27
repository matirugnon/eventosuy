// Archivo: excepciones/EdicionNoExisteException.java

package excepciones;

/**
 * Excepción lanzada cuando se intenta acceder a una edición que no existe.
 */
public class EdicionNoExisteException extends Exception {

    public EdicionNoExisteException(String nombreEdicion) {
        super("No existe la edición: '" + nombreEdicion + "'.");
    }
}