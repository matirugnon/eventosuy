package logica;

import jakarta.persistence.*;
import java.io.Serializable;

/**
 * Entidad JPA que representa una edición archivada.
 * Se persiste en HSQLDB usando EclipseLink.
 */
@Entity
@Table(name = "EDICIONES_ARCHIVADAS")
public class EdicionArchivada implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @Column(name = "NOMBRE_EDICION", nullable = false, length = 255)
    private String nombreEdicion;
    
    @Column(name = "NOMBRE_EVENTO", nullable = false, length = 255)
    private String nombreEvento;
    
    @Column(name = "SIGLA", nullable = false, length = 50)
    private String sigla;
    
    @Column(name = "CIUDAD", nullable = false, length = 100)
    private String ciudad;
    
    @Column(name = "PAIS", nullable = false, length = 100)
    private String pais;
    
    @Column(name = "FECHA_INICIO_DIA", nullable = false)
    private int fechaInicioDia;
    
    @Column(name = "FECHA_INICIO_MES", nullable = false)
    private int fechaInicioMes;
    
    @Column(name = "FECHA_INICIO_ANIO", nullable = false)
    private int fechaInicioAnio;
    
    @Column(name = "FECHA_FIN_DIA", nullable = false)
    private int fechaFinDia;
    
    @Column(name = "FECHA_FIN_MES", nullable = false)
    private int fechaFinMes;
    
    @Column(name = "FECHA_FIN_ANIO", nullable = false)
    private int fechaFinAnio;
    
    @Column(name = "FECHA_ALTA_DIA", nullable = false)
    private int fechaAltaDia;
    
    @Column(name = "FECHA_ALTA_MES", nullable = false)
    private int fechaAltaMes;
    
    @Column(name = "FECHA_ALTA_ANIO", nullable = false)
    private int fechaAltaAnio;
    
    @Column(name = "ORGANIZADOR", nullable = false, length = 100)
    private String organizador;
    
    @Column(name = "IMAGEN", length = 500)
    private String imagen;
    
    @Column(name = "VIDEO", length = 500)
    private String video;
    
    @Column(name = "FECHA_ARCHIVADO_DIA", nullable = false)
    private int fechaArchivadoDia;
    
    @Column(name = "FECHA_ARCHIVADO_MES", nullable = false)
    private int fechaArchivadoMes;
    
    @Column(name = "FECHA_ARCHIVADO_ANIO", nullable = false)
    private int fechaArchivadoAnio;
    
    // Constructor vacío requerido por JPA
    public EdicionArchivada() {
    }
    
    // Constructor completo
    public EdicionArchivada(Edicion edicion, int diaArchivado, int mesArchivado, int anioArchivado) {
        this.nombreEdicion = edicion.getNombre();
        this.nombreEvento = edicion.getEvento().getNombre();
        this.sigla = edicion.getSigla();
        this.ciudad = edicion.getCiudad();
        this.pais = edicion.getPais();
        
        this.fechaInicioDia = edicion.getFechaInicio().getDia();
        this.fechaInicioMes = edicion.getFechaInicio().getMes();
        this.fechaInicioAnio = edicion.getFechaInicio().getAnio();
        
        this.fechaFinDia = edicion.getFechaFin().getDia();
        this.fechaFinMes = edicion.getFechaFin().getMes();
        this.fechaFinAnio = edicion.getFechaFin().getAnio();
        
        this.fechaAltaDia = edicion.getAltaEdicion().getDia();
        this.fechaAltaMes = edicion.getAltaEdicion().getMes();
        this.fechaAltaAnio = edicion.getAltaEdicion().getAnio();
        
        this.organizador = edicion.getOrganizador().getNickname();
        this.imagen = edicion.getImagen();
        this.video = edicion.getVideo();
        
        this.fechaArchivadoDia = diaArchivado;
        this.fechaArchivadoMes = mesArchivado;
        this.fechaArchivadoAnio = anioArchivado;
    }
    
    // Getters y setters
    public String getNombreEdicion() {
        return nombreEdicion;
    }
    
    public void setNombreEdicion(String nombreEdicion) {
        this.nombreEdicion = nombreEdicion;
    }
    
    public String getNombreEvento() {
        return nombreEvento;
    }
    
    public void setNombreEvento(String nombreEvento) {
        this.nombreEvento = nombreEvento;
    }
    
    public String getSigla() {
        return sigla;
    }
    
    public void setSigla(String sigla) {
        this.sigla = sigla;
    }
    
    public String getCiudad() {
        return ciudad;
    }
    
    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }
    
    public String getPais() {
        return pais;
    }
    
    public void setPais(String pais) {
        this.pais = pais;
    }
    
    public int getFechaInicioDia() {
        return fechaInicioDia;
    }
    
    public void setFechaInicioDia(int fechaInicioDia) {
        this.fechaInicioDia = fechaInicioDia;
    }
    
    public int getFechaInicioMes() {
        return fechaInicioMes;
    }
    
    public void setFechaInicioMes(int fechaInicioMes) {
        this.fechaInicioMes = fechaInicioMes;
    }
    
    public int getFechaInicioAnio() {
        return fechaInicioAnio;
    }
    
    public void setFechaInicioAnio(int fechaInicioAnio) {
        this.fechaInicioAnio = fechaInicioAnio;
    }
    
    public int getFechaFinDia() {
        return fechaFinDia;
    }
    
    public void setFechaFinDia(int fechaFinDia) {
        this.fechaFinDia = fechaFinDia;
    }
    
    public int getFechaFinMes() {
        return fechaFinMes;
    }
    
    public void setFechaFinMes(int fechaFinMes) {
        this.fechaFinMes = fechaFinMes;
    }
    
    public int getFechaFinAnio() {
        return fechaFinAnio;
    }
    
    public void setFechaFinAnio(int fechaFinAnio) {
        this.fechaFinAnio = fechaFinAnio;
    }
    
    public int getFechaAltaDia() {
        return fechaAltaDia;
    }
    
    public void setFechaAltaDia(int fechaAltaDia) {
        this.fechaAltaDia = fechaAltaDia;
    }
    
    public int getFechaAltaMes() {
        return fechaAltaMes;
    }
    
    public void setFechaAltaMes(int fechaAltaMes) {
        this.fechaAltaMes = fechaAltaMes;
    }
    
    public int getFechaAltaAnio() {
        return fechaAltaAnio;
    }
    
    public void setFechaAltaAnio(int fechaAltaAnio) {
        this.fechaAltaAnio = fechaAltaAnio;
    }
    
    public String getOrganizador() {
        return organizador;
    }
    
    public void setOrganizador(String organizador) {
        this.organizador = organizador;
    }
    
    public String getImagen() {
        return imagen;
    }
    
    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
    
    public String getVideo() {
        return video;
    }
    
    public void setVideo(String video) {
        this.video = video;
    }
    
    public int getFechaArchivadoDia() {
        return fechaArchivadoDia;
    }
    
    public void setFechaArchivadoDia(int fechaArchivadoDia) {
        this.fechaArchivadoDia = fechaArchivadoDia;
    }
    
    public int getFechaArchivadoMes() {
        return fechaArchivadoMes;
    }
    
    public void setFechaArchivadoMes(int fechaArchivadoMes) {
        this.fechaArchivadoMes = fechaArchivadoMes;
    }
    
    public int getFechaArchivadoAnio() {
        return fechaArchivadoAnio;
    }
    
    public void setFechaArchivadoAnio(int fechaArchivadoAnio) {
        this.fechaArchivadoAnio = fechaArchivadoAnio;
    }
    
    @Override
    public String toString() {
        return "EdicionArchivada{" +
                "nombreEdicion='" + nombreEdicion + '\'' +
                ", nombreEvento='" + nombreEvento + '\'' +
                ", organizador='" + organizador + '\'' +
                ", fechaArchivado=" + fechaArchivadoDia + "/" + fechaArchivadoMes + "/" + fechaArchivadoAnio +
                '}';
    }
}
