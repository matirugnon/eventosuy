package logica;

import logica.datatypesyenum.DTFecha;

public class Registro{

	private DTFecha fechaRegistro;
	private double costoRegistro;
	private TipoDeRegistro tipo;

	private String asistente;
	private boolean asistio;
	boolean patrocinado;

	//para la gui
	public Registro(DTFecha fecha, double costo, TipoDeRegistro tipo, String asis) {
		fechaRegistro = fecha;
		costoRegistro = costo;
		this.tipo = tipo;
		asistente = asis;
		this.asistio = false; // Por defecto no ha asistido
		this.patrocinado = false;
	}
	
	//para la web
	public Registro(DTFecha fecha, double costo, TipoDeRegistro tipo, String asis, boolean patrocinado) {
		fechaRegistro = fecha;
		costoRegistro = costo;
		this.tipo = tipo;
		asistente = asis;
		this.asistio = false; // Por defecto no ha asistido
		this.patrocinado = patrocinado;
	}

	public String getNomTipo(){
	    return tipo.getNombre();
	}

	public TipoDeRegistro getTipoDeRegistro() {
		return tipo;
	}

	public String getAsistente() {
		// TODO Auto-generated method stub
		return asistente;
	}

	public DTFecha getFechaRegistro() {
	    return fechaRegistro;
	}

	public double getCosto() {
	    return costoRegistro;
	}

	public String getNomEdicion() {
	    return tipo.getNombreEdicion();
	}

	public boolean getAsistio() {
		return asistio;
	}

	public void setAsistio(boolean asistio) {
		this.asistio = asistio;
	}

	public void registrarAsistencia() {
		this.asistio = true;
	}
	
	public boolean getPatrocinado() {
		return patrocinado;
	}
	
	public void setPatrocinado(boolean patrocinado) {
		this.patrocinado = patrocinado;
	}
}
