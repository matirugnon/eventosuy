package logica.datatypesyenum;

import java.io.Serializable;

public class DTRegistro implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String asistente;
	private String tipoDeRegistro;
	private DTFecha fechaRegistro;
	private Double costo;
	private String nomEdicion;
	private boolean asistio;
	private boolean patrocinado;

	public DTRegistro() {}
	
	public DTRegistro(String asis, String tdr, DTFecha fecha, Double cos, String edicion){
		asistente = asis;
		tipoDeRegistro = tdr;
		fechaRegistro = fecha;
		costo = cos;
		this.nomEdicion = edicion;
		this.asistio = false;
		this.patrocinado = false;
	}
	
	public DTRegistro(String asis, String tdr, DTFecha fecha, Double cos, String edicion, boolean asistio){
		asistente = asis;
		tipoDeRegistro = tdr;
		fechaRegistro = fecha;
		costo = cos;
		this.nomEdicion = edicion;
		this.asistio = asistio;
		this.patrocinado = false;
	}
	//con patrocinado
	public DTRegistro(String asis, String tdr, DTFecha fecha, Double cos, String edicion, boolean asistio, boolean patrocinado){
		asistente = asis;
		tipoDeRegistro = tdr;
		fechaRegistro = fecha;
		costo = cos;
		this.nomEdicion = edicion;
		this.asistio = asistio;
		this.patrocinado = patrocinado;
	}

	public String getAsistente() {
		return asistente;
	}

	public String getTipoDeRegistro() {
		return tipoDeRegistro;

	}

	public DTFecha getFechaRegistro() {
		return fechaRegistro;

	}

	public Double getCosto() {
		return costo;

	}
	
	public boolean getPatrocinado() {
		return patrocinado;
	}
	
	public void setPatrocinado(boolean patrocinado) {
		this.patrocinado = patrocinado;
	}

	


	@Override
	public String toString() {
	    return getNomEdicion() + " / " + getTipoDeRegistro();
	}

	public String getNomEdicion() {
		return nomEdicion;
	}

	public void setNomEdicion(String nomEdicion) {
		this.nomEdicion = nomEdicion;
	}


	public void setAsistente(String asistente) {
		this.asistente = asistente;
	}

	public void setTipoDeRegistro(String tipoDeRegistro) {
		this.tipoDeRegistro = tipoDeRegistro;
	}

	public void setFechaRegistro(DTFecha fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	public void setCosto(Double costo) {
		this.costo = costo;
	}

	public boolean isAsistio() {
		return asistio;
	}

	public void setAsistio(boolean asistio) {
		this.asistio = asistio;
	}


}
