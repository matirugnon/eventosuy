package excepciones;

public class EventoYaFinalizadoException extends Exception {
    private static final long serialVersionUID = 1L;

    public EventoYaFinalizadoException(String nombreEvento) {
        super("El evento '" + nombreEvento + "' ya está finalizado.");
    }
}
