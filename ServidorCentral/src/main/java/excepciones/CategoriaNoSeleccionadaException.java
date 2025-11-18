// Archivo: excepciones/CategoriaNoSeleccionadaException.java

package excepciones;

import jakarta.xml.ws.WebFault;

/**
 * Excepción lanzada cuando no se selecciona ninguna categoría al dar de alta un evento.
 */
@WebFault(name = "CategoriaNoSeleccionadaFault")
public class CategoriaNoSeleccionadaException extends Exception {

    public CategoriaNoSeleccionadaException() {
        super("Debe seleccionar al menos una categoría para el evento.");
    }

    public CategoriaNoSeleccionadaException(String mensaje) {
        super(mensaje);
    }
}