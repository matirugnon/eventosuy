// Archivo: excepciones/ValorNumericoInvalidoException.java

package excepciones;

/**
 * Excepción lanzada cuando se ingresa un valor no numérico en un campo que debe ser número.
 * Por ejemplo: ingresar "abc" en los campos de costo o cupo.
 */
public class ValorNumericoInvalidoException extends Exception {

    public ValorNumericoInvalidoException(String nombreCampo, String valorIngresado) {
        super("El valor ingresado en '" + nombreCampo + "' no es válido: '" + valorIngresado + "'. Se espera un número.");
    }

    public ValorNumericoInvalidoException(String mensaje) {
        super(mensaje);
    }
}