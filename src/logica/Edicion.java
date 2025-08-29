package logica;

import java.awt.Component;
import java.time.LocalDate;

import java.util.HashMap;
import java.util.Map;

import java.util.Set;

import logica.DatatypesYEnum.DTFecha;
import logica.TipoDeRegistro;

public class Edicion{

	private DTFecha fechaInicioDtFecha;
	private DTFecha fechafinDtFecha;
	private DTFecha altaEdicionDtFecha;
	private String sigla;
	private String ciudad;
	private String pais;
	private String nombre;
	private Organizador organizador;

	private Map<String, TipoDeRegistro> tiposDeRegistro;
	private Map<Integer,Patrocinio> patrocinios;

	public Edicion(String nom, String s, String ciu, String p, DTFecha ini, DTFecha fin, DTFecha alta, Organizador org) {


		nombre = nom;
		fechaInicioDtFecha = ini;
		fechafinDtFecha = fin;
		ciudad = ciu;
		pais = p;

		sigla = s;

		organizador = org;

		altaEdicionDtFecha = alta;

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

    public String getSigla()        { return sigla;}

    public void setOrganizador(Organizador o) {
        organizador = o;
    }

	public boolean existeTipoDeRegistro(String nombreTipo) {
		return tiposDeRegistro.containsKey(nombreTipo);
	}

	public void agregarTipoDeRegistro(TipoDeRegistro tipo, String nombre) {
		this.tiposDeRegistro.put(nombre, tipo);
	}

	public Organizador getOrganizador() {
		return organizador;
	}

	public Map<String, TipoDeRegistro> getTiposdeRegistros() {
		// TODO Auto-generated method stub
		return tiposDeRegistro;
	}

	public TipoDeRegistro getTipoDeRegistro(String nombreReg) {
		return this.tiposDeRegistro.get(nombreReg);
	}

	public Set<String> getNombresTiposDeRegistro(){
		return tiposDeRegistro.keySet();
	}


}
