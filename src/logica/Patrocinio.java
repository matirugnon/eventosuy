package logica;

import logica.DatatypesYEnum.DTFecha;
import logica.DatatypesYEnum.NivelPatrocinio;

public class Patrocinio{

	private DTFecha fechaAlta;
	private float monto;
	private int codigo;
	private NivelPatrocinio nivel;


	public Patrocinio(DTFecha alta, float m, int cod, NivelPatrocinio niv) {

		fechaAlta = alta;
		monto = m;
		codigo = cod;
		nivel = niv;
	}




}
