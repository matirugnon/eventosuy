// Archivo: excepciones/PatrocinioNoEncontradoException.java

package excepciones;

/**
 * Excepción lanzada cuando no se encuentra un patrocinio con el código especificado en una edición.
 */
public class PatrocinioNoEncontradoException extends Exception {

    /**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public PatrocinioNoEncontradoException(String codigo, String nombreEdicion) {
        super("No existe el patrocinio con código '" + codigo + "' en la edición '" + nombreEdicion + "'.");
    }
}