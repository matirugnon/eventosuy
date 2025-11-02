package logica.datatypesyenum;

import java.util.Map;
import java.util.Map.Entry;
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
    private String imagen; // Imagen de la edición
    private String video; // Video de la edición

    public DTEdicion(String evento, String nombre, String sig, DTFecha fechaInicio, DTFecha fechaFin, DTFecha altaEdicion, String ciudad, String pais, String org, Set<String> tdr, Set<Map.Entry<String, String>> reg, Set<String> patro, EstadoEdicion estado) {
        this(evento, nombre, sig, fechaInicio, fechaFin, altaEdicion, ciudad, pais, org, tdr, reg, patro, estado, null, null);
    }

    public DTEdicion(String evento, String nombre, String sig, DTFecha fechaInicio, DTFecha fechaFin, DTFecha altaEdicion, String ciudad, String pais, String org, Set<String> tdr, Set<Map.Entry<String, String>> reg, Set<String> patro, EstadoEdicion estado, String imagen) {
        this(evento, nombre, sig, fechaInicio, fechaFin, altaEdicion, ciudad, pais, org, tdr, reg, patro, estado, imagen, null);
    }

    public DTEdicion(String evento, String nombre, String sig, DTFecha fechaInicio, DTFecha fechaFin, DTFecha altaEdicion, String ciudad, String pais, String org, Set<String> tdr, Set<Map.Entry<String, String>> reg, Set<String> patro, EstadoEdicion estado, String imagen, String video) {
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
        this.imagen = imagen;
        this.video = video;
    }

 


	public DTEdicion(String evento2, String nombre2, Object sig, DTFecha fechaInicio2, DTFecha fechaFin2,
			DTFecha altaEdicion2, String ciudad2, String pais2, String org, Set<String> tiporeg,
			Set<Entry<String, String>> regEsperados, Set<String> patro, Object estado2) {
		// TODO Auto-generated constructor stub
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
	public String getSigla() {return sigla; }
	public String getOrganizador() {return organizador; }
	public Set<String> getPatrocinios() {return patrocinios; }
	public Set<String> getTiposDeRegistro(){return tiposDeRegistro; }
	public Set<Map.Entry<String, String>> getRegistros(){return registros; }
	public EstadoEdicion getEstado() { return estado; } // Nuevo getter
	public String getImagen() { return imagen; } // Getter para imagen
    public String getVideo() { return video; } // Getter para video



}