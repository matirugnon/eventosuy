package servlets.dto;

import soap.StringArray;

/**
 * DTO para mapear los datos de una edición obtenidos desde SOAP.
 * El array SOAP tiene el formato:
 * [0] nombre, [1] imagen
 */
public class EdicionDTO {
    private String nombre;
    private String imagen;

    public EdicionDTO(StringArray soapArray) {
        if (soapArray == null || soapArray.getItem() == null || soapArray.getItem().size() < 2) {
            throw new IllegalArgumentException("StringArray inválido para EdicionDTO");
        }
        
        this.nombre = soapArray.getItem().get(0);
        this.imagen = soapArray.getItem().get(1);
        // Si la imagen está vacía, asignar null
        if (this.imagen != null && this.imagen.trim().isEmpty()) {
            this.imagen = null;
        }
    }

    public String getNombre() {
        return nombre;
    }

    public String getImagen() {
        return imagen;
    }
}
