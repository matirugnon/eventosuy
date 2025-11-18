package excepciones;

import jakarta.xml.ws.WebFault;

/**
 * Excepción que se lanza cuando se intenta dar de alta
 * una edición con un nombre que ya existe en la plataforma.
 */
@WebFault(name = "CostoSuperaAporteFault")
public class CostoSuperaAporteException extends Exception {

      public CostoSuperaAporteException() {
        super("El costo supera el aporte minimo que es del 20% del costo de los registros gratuitos.");
    }

    public CostoSuperaAporteException(String mensaje) {
        super(mensaje);
    }
    
}
