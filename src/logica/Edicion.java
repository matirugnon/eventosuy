package logica;

import java.time.LocalDate;

import logica.DatatypesYEnum.DTFecha;

public class Edicion{

	private DTFecha fechaInicioDtFecha;
	private DTFecha fechafinDtFecha;
	private DTFecha altaEdicionDtFecha;

	private String ciudad;
	private String pais;

	public Edicion(DTFecha ini, DTFecha fin, String ciu, String p) {

		fechaInicioDtFecha = ini;
		fechafinDtFecha = fin;
		ciudad = ciu;
		pais = p;

		//no me acuerdo como poner localdate :)
	}




}
