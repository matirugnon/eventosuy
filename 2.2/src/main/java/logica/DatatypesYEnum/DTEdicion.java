package logica.DatatypesYEnum;

import java.util.Map;
import java.util.Set;


public class DTEdicion {
    private String evento; // Nueva propiedad
    private  String nombre;
    private  DTFecha fechaInicio;
    private  DTFecha fechaFin;
    private  DTFecha altaEdicion;
    private  String ciudad;
    private  String pais;
    private  String sigla;
    private  String organizador;
    private  Set<String> patrocinios;
    private  Set<String> tiposDeRegistro;
    private Set<Map.Entry<String, String>> registros;
    private EstadoEdicion estado; // Nuevo atributo

    public DTEdicion(String evento, String nombre, String sig, DTFecha fechaInicio, DTFecha fechaFin, DTFecha altaEdicion, String ciudad, String pais, String org, Set<String> tdr, Set<Map.Entry<String, String>> reg, Set<String> patro, EstadoEdicion estado) {
        this.evento = evento;
        this.nombre = nombre;
        this.sigla = sig;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.altaEdicion = altaEdicion;
        this.ciudad = ciudad;
        this.pais = pais;
        this.organizador = org;
        this.tiposDeRegistro = tdr;
        this.registros = reg != null ? reg : Set.of(); // evita null
        this.patrocinios = patro;
        this.estado = estado;
    }

    public String getEvento() {
        return evento;
    }

    public String getNombre() { return nombre; }
    public DTFecha getFechaInicio() { return fechaInicio; }
    public DTFecha getFechaFin() { return fechaFin; }
    public DTFecha getAltaEdicion() { return altaEdicion; }
    public String getCiudad() { return ciudad; }
    public String getPais() { return pais; }
	public String getSigla() {return sigla;}
	public String getOrganizador() {return organizador;}
	public Set<String> getPatrocinios() {return patrocinios;}
	public Set<String> getTiposDeRegistro(){return tiposDeRegistro;}
	public Set<Map.Entry<String, String>> getRegistros(){return registros;}
	public EstadoEdicion getEstado() { return estado; } // Nuevo getter



}