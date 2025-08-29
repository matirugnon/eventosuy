package logica;

import logica.DatatypesYEnum.DTFecha;

public class Registro{

	DTFecha fechaRegistro;
	float costoRegistro;
	TipoDeRegistro tipo;
	private String asistente;

	public Registro(DTFecha fecha, float costo, TipoDeRegistro tipo, String asis) {
		fechaRegistro = fecha;
		costoRegistro = costo;
		this.tipo = tipo;
		asistente = asis;
	}

	public String getNomTipo(){
	    return tipo.getNombre();
	}

	public String getAsistente() {
		// TODO Auto-generated method stub
		return asistente;
	}

}
