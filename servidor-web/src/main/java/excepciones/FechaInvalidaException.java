// Archivo: excepciones/FechaInvalidaException.java

package excepciones;

/**
 * Excepción lanzada cuando se proporciona una fecha inválida (ej. 30 de febrero).
 */
public class FechaInvalidaException extends Exception {

    public FechaInvalidaException(int dia, int mes, int anio) {
        super(String.format("La fecha %02d/%02d/%04d no es válida.", dia, mes, anio));
    }

    public FechaInvalidaException(String mensaje) {
        super(mensaje);
    }


}