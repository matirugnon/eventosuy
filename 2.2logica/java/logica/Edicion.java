package logica;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import java.util.Set;

import logica.DatatypesYEnum.DTEdicion;
import logica.DatatypesYEnum.DTFecha;
import logica.DatatypesYEnum.EstadoEdicion;
import logica.DatatypesYEnum.NivelPatrocinio;

public class Edicion{

	private DTFecha fechaInicioDtFecha;
	private DTFecha fechafinDtFecha;
	private DTFecha altaEdicionDtFecha;
	private String sigla;
	private String ciudad;
	private String pais;
	private String nombre;
	private Organizador organizador;
	private Evento evento; // Referencia al objeto Evento

	private Map<String, TipoDeRegistro> tiposDeRegistro;
	private Map<String, Patrocinio> patrocinios;

	private EstadoEdicion estado; // Nuevo atributo
	private String imagen;

	// Constructor sin imagen (mantiene compatibilidad)
	public Edicion(String nom, String s, String ciu, String p, DTFecha ini, DTFecha fin, DTFecha alta, Organizador org, Evento evento) {
		this(nom, s, ciu, p, ini, fin, alta, org, evento, null);
	}

	// Constructor con imagen (opcional)
	public Edicion(String nom, String s, String ciu, String p, DTFecha ini, DTFecha fin, DTFecha alta, Organizador org, Evento evento, String imagen) {
		nombre = nom;
		fechaInicioDtFecha = ini;
		fechafinDtFecha = fin;
		ciudad = ciu;
		pais = p;

		sigla = s;

		organizador = org;

		altaEdicionDtFecha = alta;
		this.evento = evento; // Inicializar la referencia al objeto Evento
		this.imagen = imagen;

		this.tiposDeRegistro = new HashMap<>();
		this.patrocinios = new HashMap<>();
		this.estado = EstadoEdicion.INGRESADA; // Estado por defecto

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

    public String getSigla()        { return sigla; }
    public String getImagen()       { return imagen; }
    
    public void setImagen(String imagen) { this.imagen = imagen; }

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
	            String nombreAsistente = reg.getAsistente();
	            String nombreTipo = reg.getNomTipo();

	            // Crear par (asistente, tipoRegistro)
	            Map.Entry<String, String> entry = new AbstractMap.SimpleImmutableEntry<>(nombreAsistente, nombreTipo);
	            registrosData.add(entry);
	        }
	    }

	    // Construir y devolver el DTEdicion
	    return new DTEdicion(
	        this.evento.getNombre(), // Obtener el nombre del evento desde el objeto Evento
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
	        patrocinios,
	        this.estado, // Incluir el estado
	        this.imagen  // Incluir la imagen
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
		TipoDeRegistro tipo, int cantRegistrosGratuitos, String codigo, DTFecha fechaAlta) {
		Patrocinio pat = new Patrocinio(fechaAlta, aporte, codigo, nivel, this, ins, cantRegistrosGratuitos, tipo);
		this.patrocinios.put(codigo, pat);
		return pat;
	}

	public EstadoEdicion getEstado() {
        return estado;
    }

    public void setEstado(EstadoEdicion estado) {
        this.estado = estado;
    }

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }

}
