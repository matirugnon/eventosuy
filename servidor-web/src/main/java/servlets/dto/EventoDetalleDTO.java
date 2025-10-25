package servlets.dto;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import soap.StringArray;

/**
 * DTO para mapear los datos de un evento obtenidos desde SOAP.
 * El array SOAP tiene el formato:
 * [0] nombre, [1] sigla, [2] descripcion, [3] dia, [4] mes, [5] anio, [6] imagen, [7] categorias_csv
 */
public class EventoDetalleDTO {
    private String nombre;
    private String sigla;
    private String descripcion;
    private String imagen;
    private int dia;
    private int mes;
    private int anio;
    private Set<String> categorias;

    public EventoDetalleDTO(StringArray soapArray) {
        if (soapArray == null || soapArray.getItem() == null || soapArray.getItem().isEmpty()) {
            throw new IllegalArgumentException("StringArray vacío o nulo");
        }
        
        List<String> items = soapArray.getItem();
        
        this.nombre = items.get(0);
        this.sigla = items.get(1);
        this.descripcion = items.get(2);
        this.dia = Integer.parseInt(items.get(3));
        this.mes = Integer.parseInt(items.get(4));
        this.anio = Integer.parseInt(items.get(5));
        this.imagen = items.get(6).isEmpty() ? null : items.get(6);
        
        // Parsear categorías separadas por coma
        String categoriasStr = items.get(7);
        if (categoriasStr != null && !categoriasStr.trim().isEmpty()) {
            this.categorias = new HashSet<>(Arrays.asList(categoriasStr.split(",")));
        } else {
            this.categorias = new HashSet<>();
        }
    }

    public String getNombre() {
        return nombre;
    }

    public String getSigla() {
        return sigla;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getImagen() {
        return imagen;
    }

    public int getDia() {
        return dia;
    }

    public int getMes() {
        return mes;
    }

    public int getAnio() {
        return anio;
    }

    public Set<String> getCategorias() {
        return categorias;
    }

    public String getFechaEvento() {
        return dia + "/" + mes + "/" + anio;
    }
}
