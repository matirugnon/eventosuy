package logica;

import java.awt.Component;
import java.time.LocalDate;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import java.util.Set;

import logica.DatatypesYEnum.DTEdicion;
import logica.DatatypesYEnum.DTFecha;
import logica.DatatypesYEnum.DTRegistro;
import logica.DatatypesYEnum.NivelPatrocinio;
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
	private Map<String,Patrocinio> patrocinios;

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

	public Set<Patrocinio> getPatrocinios() {
		return (Set<Patrocinio>) new HashSet<>(patrocinios.values());
	}

	public Set<String> getCodigosPatrocinios() {
		return patrocinios.keySet();
	}

	public DTEdicion getDTEdicion() {

	    String nicknameOrganizador = organizador != null ? organizador.getNickname() : "Sin organizador";

	    // Obtener los nombres de los tipos de registro
	    Set<String> patrocinios = this.getCodigosPatrocinios();
	    Set<String> nombresTiposDeRegistro = this.getNombresTiposDeRegistro();

	    Set<Map.Entry<String, String>> registrosData = new HashSet<>();

	    for (TipoDeRegistro tipo : tiposDeRegistro.values()) {
	        Set<Registro> registros = tipo.getRegistros();
	        for (Registro reg : registros) {
	            String nombreAsistente = reg.getAsistente();        // ✅ Ya es String
	            String nombreTipo = reg.getNomTipo();          // "Estudiante", "Profesional", etc.

	            // Crear par (asistente, tipoRegistro)
	            Map.Entry<String, String> entry = new AbstractMap.SimpleImmutableEntry<>(nombreAsistente, nombreTipo);
	            registrosData.add(entry);
	        }
	    }




	    Set<DTRegistro> registrosDT = null;

	    // Construir y devolver el DTEdicion
	    return new DTEdicion(
	        this.nombre,
	        this.sigla,
	        this.fechaInicioDtFecha,
	        this.fechafinDtFecha,
	        this.altaEdicionDtFecha,
	        this.ciudad,
	        this.pais,
	        nicknameOrganizador,
	        nombresTiposDeRegistro,
	        registrosData,
	        patrocinios
	    );
	}

	public boolean esPatrocinador(String nomInstitucion) {
		for (Patrocinio pat : patrocinios.values()) {
			if (pat.getNombreInstitucion().equals(nomInstitucion)) {
				return true;
			}
		}
		return false;
	}


	public Patrocinio altaPatrocinio(Institucion ins, NivelPatrocinio nivel, double aporte,
		TipoDeRegistro tipo, int cantRegistrosGratuitos,String codigo, DTFecha fechaAlta) {
		Patrocinio pat = new Patrocinio(fechaAlta, aporte, codigo, nivel, this, ins, cantRegistrosGratuitos, tipo);
		this.patrocinios.put(codigo, pat);
		return pat;
	}

}
