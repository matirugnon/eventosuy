package excepciones;

public class EdicionYaArchivadaException extends Exception {
    
    private static final long serialVersionUID = 1L;
    
    public EdicionYaArchivadaException(String nombreEdicion) {
        super("La edición '" + nombreEdicion + "' ya se encuentra archivada.");
    }
}
