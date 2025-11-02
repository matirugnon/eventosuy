package excepciones;

public class NombreTipoRegistroDuplicadoException extends Exception {
    private static final long serialVersionUID = 1L;

    public NombreTipoRegistroDuplicadoException(String nombre) {
        super("Ya existe un tipo de registro con el nombre: " + nombre);
    }
}
