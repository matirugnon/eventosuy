package servlets.dto;

import soap.StringArray;
import java.util.List;

public class EdicionDetalleDTO {

    private String evento; 
    private String nombre;
    private String sigla;
    private String ciudad;
    private String pais;
    private int fechaInicioDia;
    private int fechaInicioMes;
    private int fechaInicioAnio;
    private int fechaFinDia;
    private int fechaFinMes;
    private int fechaFinAnio;
    private String organizador;
    private String imagen;
    private String estado;

    /**
     * Constructor que parsea el StringArray del SOAP.
     * Formato esperado: 
     * [0] nombre, [1] sigla, [2] ciudad, [3] pais,
     * [4] fechaInicioDia, [5] fechaInicioMes, [6] fechaInicioAnio,
     * [7] fechaFinDia, [8] fechaFinMes, [9] fechaFinAnio,
     * [10] organizador, [11] imagen, [12] estado, [13] evento
     */
    public EdicionDetalleDTO(StringArray detalleEdicionArray) {
        if (detalleEdicionArray != null && detalleEdicionArray.getItem() != null) {
            List<String> items = detalleEdicionArray.getItem();

            if (items.size() >= 14) { // ahora esperamos 14 elementos
                this.nombre = items.get(0);
                this.sigla = items.get(1);
                this.ciudad = items.get(2);
                this.pais = items.get(3);
                this.fechaInicioDia = Integer.parseInt(items.get(4));
                this.fechaInicioMes = Integer.parseInt(items.get(5));
                this.fechaInicioAnio = Integer.parseInt(items.get(6));
                this.fechaFinDia = Integer.parseInt(items.get(7));
                this.fechaFinMes = Integer.parseInt(items.get(8));
                this.fechaFinAnio = Integer.parseInt(items.get(9));
                this.organizador = items.get(10);
                this.imagen = items.get(11).isEmpty() ? null : items.get(11);
                this.estado = items.get(12);
                this.evento = items.get(13); // nuevo campo
            }
        }
    }

    // Getters
    public String getEvento() { return evento; }
    public String getNombre() { return nombre; }
    public String getSigla() { return sigla; }
    public String getCiudad() { return ciudad; }
    public String getPais() { return pais; }
    public int getFechaInicioDia() { return fechaInicioDia; }
    public int getFechaInicioMes() { return fechaInicioMes; }
    public int getFechaInicioAnio() { return fechaInicioAnio; }
    public int getFechaFinDia() { return fechaFinDia; }
    public int getFechaFinMes() { return fechaFinMes; }
    public int getFechaFinAnio() { return fechaFinAnio; }
    public String getOrganizador() { return organizador; }
    public String getImagen() { return imagen; }
    public String getEstado() { return estado; }

    // Setters
    public void setEvento(String evento) { this.evento = evento; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setSigla(String sigla) { this.sigla = sigla; }
    public void setCiudad(String ciudad) { this.ciudad = ciudad; }
    public void setPais(String pais) { this.pais = pais; }
    public void setFechaInicioDia(int fechaInicioDia) { this.fechaInicioDia = fechaInicioDia; }
    public void setFechaInicioMes(int fechaInicioMes) { this.fechaInicioMes = fechaInicioMes; }
    public void setFechaInicioAnio(int fechaInicioAnio) { this.fechaInicioAnio = fechaInicioAnio; }
    public void setFechaFinDia(int fechaFinDia) { this.fechaFinDia = fechaFinDia; }
    public void setFechaFinMes(int fechaFinMes) { this.fechaFinMes = fechaFinMes; }
    public void setFechaFinAnio(int fechaFinAnio) { this.fechaFinAnio = fechaFinAnio; }
    public void setOrganizador(String organizador) { this.organizador = organizador; }
    public void setImagen(String imagen) { this.imagen = imagen; }
    public void setEstado(String estado) { this.estado = estado; }
}
