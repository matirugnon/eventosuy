// Archivo: excepciones/SiglaRepetidaException.java

package excepciones;

import jakarta.xml.ws.WebFault;

/**
 * Excepción lanzada cuando se intenta registrar un evento o edición con una sigla que ya existe.
 */
@WebFault(name = "UsuarioNoPerteneceFault")
public class UsuarioNoPerteneceException extends Exception {

    public UsuarioNoPerteneceException(String string) {
        super(string);
    }
}
