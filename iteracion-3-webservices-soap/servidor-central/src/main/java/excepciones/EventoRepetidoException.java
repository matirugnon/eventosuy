// Archivo: excepciones/EventoRepetidoException.java

package excepciones;

import jakarta.xml.ws.WebFault;

/**
 * Excepción lanzada cuando se intenta registrar un evento con un nombre que ya existe.
 */
@WebFault(name = "EventoRepetidoFault")
public class EventoRepetidoException extends Exception {

    public EventoRepetidoException(String nombreEvento) {
        super("Ya existe un evento con el nombre: '" + nombreEvento + "'.");
    }

    public EventoRepetidoException(String nombreEvento, Throwable cause) {
        super("Ya existe un evento con el nombre: '" + nombreEvento + "'.", cause);
    }
}