package logica;

import logica.DatatypesYEnum.DTFecha;

public class Registro{

	DTFecha fechaRegistro;
	double costoRegistro;
	TipoDeRegistro tipo;
	Asistente asistente;

	public Registro(DTFecha fecha, double costo, TipoDeRegistro tipo, Asistente asistente) {
		fechaRegistro = fecha;
		costoRegistro = costo;
		this.tipo = tipo;
		this.asistente = asistente;
	}
	
	public String getNomTipo(){
	    return tipo.getNombre();
	}
	
	public TipoDeRegistro getTipoDeRegistro() {
		return tipo;
	}

}
