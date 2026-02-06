package logica.datatypesyenum;

import java.io.Serializable;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import jakarta.xml.bind.annotation.XmlTransient;


public class DTEdicion implements Serializable{
	private static final long serialVersionUID = 1L;

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
    
    public DTEdicion() {}
    
    
    
	
    public void setEvento(String evento) {
		this.evento = evento;
	}

	public void setVideo(String video) {
		this.video = video;
	}
	
	public String getVideo() {
		return video;
	}
	

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public void setFechaInicio(DTFecha fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public void setFechaFin(DTFecha fechaFin) {
		this.fechaFin = fechaFin;
	}

	public void setAltaEdicion(DTFecha altaEdicion) {
		this.altaEdicion = altaEdicion;
	}

	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}

	public void setPais(String pais) {
		this.pais = pais;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	public void setOrganizador(String organizador) {
		this.organizador = organizador;
	}

	public void setPatrocinios(Set<String> patrocinios) {
		this.patrocinios = patrocinios;
	}

	public void setTiposDeRegistro(Set<String> tiposDeRegistro) {
		this.tiposDeRegistro = tiposDeRegistro;
	}

	public void setRegistros(Set<Map.Entry<String, String>> registros) {
		this.registros = registros;
	}

	public void setEstado(EstadoEdicion estado) {
		this.estado = estado;
	}

	public void setImagen(String imagen) {
		this.imagen = imagen;
	}

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
	
	@XmlTransient
	public Set<Map.Entry<String, String>> getRegistros(){return registros; }
	
	public EstadoEdicion getEstado() { return estado; } // Nuevo getter
	public String getImagen() { return imagen; } // Getter para imagen



}