package logica;

import logica.DatatypesYEnum.DTFecha;

public class Registro{

	DTFecha fechaRegistro;
	float costoRegistro;
	TipoDeRegistro tipo;


	public Registro(DTFecha fecha, float costo, TipoDeRegistro tipo) {
		fechaRegistro = fecha;
		costoRegistro = costo;
		this.tipo = tipo;
	}
	
	public String getNomTipo(){
	    return tipo.getNombre();
	}

}
