package logica;

import logica.DatatypesYEnum.DTFecha;
import logica.DatatypesYEnum.NivelPatrocinio;

public class Patrocinio{

	private DTFecha fechaAlta;
	private double monto;
	private String codigo;
	private NivelPatrocinio nivel;
	private CantidadTipoDeRegistro cantTipo;
	private Edicion edicion;
	private Institucion institucion;

	public Patrocinio(DTFecha alta, double m, String cod, NivelPatrocinio niv, Edicion ed, Institucion ins, int cantGrat, TipoDeRegistro tipo) {

		fechaAlta = alta;
		monto = m;
		codigo = cod;
		nivel = niv;
		edicion = ed;
		institucion = ins;
		cantTipo = new CantidadTipoDeRegistro(cantGrat, tipo, this);

	}

	  public DTFecha getFechaAlta() { return fechaAlta; }
	    public double getMonto() { return monto; }
	    public String getCodigo() { return codigo; }
	    public NivelPatrocinio getNivel() { return nivel; }

	   public CantidadTipoDeRegistro getCantidadTipoDeRegistro() {
		   return cantTipo;
	   }

	   public String getNombreTipoDeRegistro() {
		   return cantTipo.getNombreTipoDeRegistro();
	   }

	   public String getNombreInstitucion() {
		   return institucion.getNombre();
	   }

}
