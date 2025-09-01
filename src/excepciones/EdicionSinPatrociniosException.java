// Archivo: excepciones/EdicionSinPatrociniosException.java

package excepciones;

/**
 * Excepción lanzada cuando una edición no tiene patrocinios registrados.
 */
public class EdicionSinPatrociniosException extends Exception {

    /**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public EdicionSinPatrociniosException(String nombreEdicion) {
        super("La edición '" + nombreEdicion + "' no tiene patrocinios registrados.");
    }
}