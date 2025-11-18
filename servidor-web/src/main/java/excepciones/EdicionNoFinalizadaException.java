package excepciones;

public class EdicionNoFinalizadaException extends Exception {
    
    private static final long serialVersionUID = 1L;
    
    public EdicionNoFinalizadaException(String nombreEdicion) {
        super("La edición '" + nombreEdicion + "' no puede ser archivada porque aún no ha finalizado.");
    }
}
