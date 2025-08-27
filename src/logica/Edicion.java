package logica;

import java.time.LocalDate;

import logica.DatatypesYEnum.DTFecha;

public class Edicion{

	private DTFecha fechaInicioDtFecha;
	private DTFecha fechafinDtFecha;
	private DTFecha altaEdicionDtFecha;

	private String ciudad;
	private String pais;
	private String nombre;

	public Edicion(String nom, DTFecha ini, DTFecha fin, String ciu, String p) {

		nombre = nom;
		fechaInicioDtFecha = ini;
		fechafinDtFecha = fin;
		ciudad = ciu;
		pais = p;

		//no me acuerdo como poner localdate :)
	}

	public String getNombre() {
		return nombre;
	}
	
	public DTFecha getFechaInicio() { return fechaInicioDtFecha; }
    public DTFecha getFechaFin()    { return fechafinDtFecha; }
    public DTFecha getAltaEdicion() { return altaEdicionDtFecha; }
    public String getCiudad()       { return ciudad; }
    public String getPais()         { return pais; }
	





}
