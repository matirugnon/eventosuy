package logica.Controladores;

import java.util.Set;

import excepciones.EdicionExistenteException;
import excepciones.EventoRepetidoException;
import excepciones.FechaInvalidaException;
import excepciones.FechasIncompatiblesException;
import logica.Evento;
import logica.DatatypesYEnum.DTEdicion;
import logica.DatatypesYEnum.DTFecha;
import logica.DatatypesYEnum.DTPatrocinio;
import logica.DatatypesYEnum.DTSeleccionEvento;
import logica.DatatypesYEnum.NivelPatrocinio;

public interface IControladorEvento {

	static IControladorEvento getInstance() {
	    return ControladorEvento.getInstance();
	}

	public boolean existeEvento(String nomEvento);

	public void darAltaEvento(String nomEvento, String desc, DTFecha fechaAlta, String sigla, Set<String> nomcategorias)
			throws EventoRepetidoException;

	public Set<String> listarEventos();

	public Set<String> listarEdiciones();

	public DTSeleccionEvento seleccionarEvento(String nomEvento);

	public DTEdicion consultarEdicion(String nomEdicion);

	public boolean existeCategoria(String cat);

	public void altaCategoria (String cat);

	public boolean existeEdicion(String nomEdicion);

	public void AltaEdicion(String nomEvento, String nickOrganizador, String nomEdicion, String sigla, String ciudad, String pais, DTFecha fechaIni, DTFecha fechaFin, DTFecha fechaAlta)
			throws EdicionExistenteException, FechasIncompatiblesException;

	public Set<String> listarEdiciones(String nomEvento);

	public DTPatrocinio consultarTipoPatrocinioEdicion(String nomEdicion, String codPatrocinio);

	public boolean existePatrocinio(String nomEdicion, String nomInstitucion);

	public boolean costoSuperaAporte(String nomEdicion, String nomInstitucion,String nomTipoRegistro, double monto, int cantRegGrat);

	 public void altaPatrocinio(String nomEdicion, String nomInstitucion, NivelPatrocinio nivel, double aporte,
			 String nomTipoRegistro, int cantRegistrosGratuitos,String codigo, DTFecha fechaAlta);

	 public Set<String> listarPatrocinios(String nomEdicion);

}
