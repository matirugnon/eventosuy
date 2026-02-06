package servlets.dto;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * DTO temporal para eventos obtenidos vía SOAP
 * Formato del array: [nombre, sigla, descripcion, dia, mes, anio, imagen, categorias_separadas_por_coma, estado]
 */
public class EventoDTO {
    private String nombre;
    private String sigla;
    private String descripcion;
    private int dia;
    private int mes;
    private int anio;
    private String imagen;
    private Set<String> categorias;
    private boolean finalizado;
    private String estado;

    public EventoDTO(String[] detalles) {
        if (detalles != null && detalles.length >= 8) {
            this.nombre = detalles[0];
            this.sigla = detalles[1];
            this.descripcion = detalles[2];
            this.dia = Integer.parseInt(detalles[3]);
            this.mes = Integer.parseInt(detalles[4]);
            this.anio = Integer.parseInt(detalles[5]);
            this.imagen = detalles[6].isEmpty() ? null : detalles[6];
            this.categorias = detalles[7].isEmpty() ? new HashSet<>() : 
                            new HashSet<>(Arrays.asList(detalles[7].split(",")));
            if (detalles.length > 8) {
                this.estado = detalles[8];
                this.finalizado = "FINALIZADO".equalsIgnoreCase(detalles[8]);
            } else {
                this.estado = "ACTIVO";
                this.finalizado = false;
            }
        }
    }

    public String getNombre() { return nombre; }
    public String getSigla() { return sigla; }
    public String getDescripcion() { return descripcion; }
    public String getImagen() { return imagen; }
    public Set<String> getCategorias() { return categorias; }
    public boolean isFinalizado() { return finalizado; }
    public String getEstado() { return estado; }
    
    public String getFechaEvento() {
        return dia + "/" + mes + "/" + anio;
    }
}

