package logica;

import logica.DatatypesYEnum.DTFecha;

public class Registro{

	private DTFecha fechaRegistro;
	private double costoRegistro;
	private TipoDeRegistro tipo;

	private String asistente;

	public Registro(DTFecha fecha, double costo, TipoDeRegistro tipo, String asis) {
		fechaRegistro = fecha;
		costoRegistro = costo;
		this.tipo = tipo;
		asistente = asis;

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
}
