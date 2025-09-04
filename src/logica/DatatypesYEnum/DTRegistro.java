package logica.DatatypesYEnum;

public class DTRegistro {

	private String asistente;
	private String tipoDeRegistro;
	private DTFecha fechaRegistro;
	private Double costo;
	private String nomEdicion;

	public DTRegistro(String as, String tdr, DTFecha fecha, Double cos, String edicion){
		asistente = as;
		tipoDeRegistro = tdr;
		fechaRegistro = fecha;
		costo = cos;
		this.nomEdicion = edicion;
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

	public String getnomEdicion() {
	    return nomEdicion;
	}


	@Override
	public String toString() {
	    return getnomEdicion() + " / " + getTipoDeRegistro();
	}


}
