package logica;

import java.time.LocalDate;
import java.util.HashSet;
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
	private Organizador orga;
	private Set<TipoDeRegistro> tiposderegistro;
	private Set<Patrocinio> patrocinios;

	public Edicion(String nom, String s, String ciu, String p, DTFecha ini, DTFecha fin, DTFecha fechaAlta) {

		nombre = nom;
		fechaInicioDtFecha = ini;
		fechafinDtFecha = fin;
		ciudad = ciu;
		pais = p;
		altaEdicionDtFecha = fechaAlta;
		sigla = s;
		orga = null;
		tiposderegistro = new HashSet<>();
		patrocinios = new HashSet<>();
		

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
        orga = o;
    }
    
    public Set<TipoDeRegistro> getTiposRegistro() {
        return tiposderegistro;
    }
	
    public Set<Patrocinio> getPatrocinios(){
        return patrocinios;
    }





}
