package logica.DatatypesYEnum;

public class DTRegistro {

	private String asistente;
	private String tipoDeRegistro;
	private DTFecha fechaRegistro;
	private Double costo;

	public DTRegistro(String as, String tdr, DTFecha fecha, Double cos){
		asistente = as;
		tipoDeRegistro = tdr;
		fechaRegistro = fecha;
		costo = cos;
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



}
