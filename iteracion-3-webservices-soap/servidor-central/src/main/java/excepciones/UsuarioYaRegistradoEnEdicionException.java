package excepciones;

import jakarta.xml.ws.WebFault;

@WebFault(name = "UsuarioYaRegistradoEnEdicionFault")
public class UsuarioYaRegistradoEnEdicionException extends Exception {
    private static final long serialVersionUID = 1L;

    public UsuarioYaRegistradoEnEdicionException(String nombreUsuario, String nombreEdicion) {
        super("El usuario '" + nombreUsuario + "' ya está registrado en la edición '" + nombreEdicion + "'.");
    }
}
