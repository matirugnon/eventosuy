package logica;

import java.awt.Component;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import logica.DatatypesYEnum.DTFecha;

public class Edicion{

	private DTFecha fechaInicioDtFecha;
	private DTFecha fechafinDtFecha;
	private DTFecha altaEdicionDtFecha;

	private String ciudad;
	private String pais;
	private String nombre;
	private String sigla;
	private Organizador organizador;

	private Map<String, TipoDeRegistro> tiposDeRegistro;
	private Map<Integer,Patrocinio> patrocinios;

	public Edicion(String nom, DTFecha ini, DTFecha fin, String ciu, String p, String s) {

		nombre = nom;
		fechaInicioDtFecha = ini;
		fechafinDtFecha = fin;
		ciudad = ciu;
		pais = p;
		sigla = s;

		this.tiposDeRegistro = new HashMap<>();
		this.patrocinios = new HashMap<>();
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
    public String getSigla()         { return sigla; }



	public boolean existeTipoDeRegistro(String nombreTipo) {
		return tiposDeRegistro.containsKey(nombreTipo);
	}

	public void agregarTipoDeRegistro(TipoDeRegistro tipo, String nombre) {
		this.tiposDeRegistro.put(nombre, tipo);
	}

	public Organizador getOrganizador() {
		// TODO Auto-generated method stub
		return null;
	}

	public Map<String, TipoDeRegistro> getTiposdeRegistros() {
		// TODO Auto-generated method stub
		return tiposDeRegistro;
	}


}
