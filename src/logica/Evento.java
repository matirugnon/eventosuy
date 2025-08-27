package logica;

import logica.DatatypesYEnum.DTFecha;

public class Evento{

	private String nombre;
	private String sigla;
	private String descripcion;
	private DTFecha fechaEvento;


	public Evento(String nom, String sig, String desc, DTFecha fecha) {

		nombre = nom;
		sigla = sig;
		descripcion = desc;
		fechaEvento = fecha;
	}

}



