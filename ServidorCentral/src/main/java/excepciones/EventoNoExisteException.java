// Archivo: excepciones/EventoNoExisteException.java

package excepciones;

/**
 * Excepción lanzada cuando se intenta acceder a un evento que no existe.
 */
public class EventoNoExisteException extends Exception {


	private static final long serialVersionUID = 1L;

	public EventoNoExisteException(String nombreEvento) {
        super("No existe un evento con el nombre: '" + nombreEvento + "'.");
    }

    public EventoNoExisteException(String nombreEvento, Throwable cause) {
        super("No existe un evento con el nombre: '" + nombreEvento + "'.", cause);
    }
}