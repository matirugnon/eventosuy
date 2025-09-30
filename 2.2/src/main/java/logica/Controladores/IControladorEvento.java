package logica.Controladores;

import java.util.Set;

import excepciones.CategoriaNoSeleccionadaException;
import excepciones.EdicionExistenteException;
import excepciones.EdicionNoExisteException;
import excepciones.EdicionSinPatrociniosException;
import excepciones.EventoNoExisteException;
import excepciones.EventoRepetidoException;
import excepciones.FechaInvalidaException;
import excepciones.FechasIncompatiblesException;
import excepciones.PatrocinioDuplicadoException;
import excepciones.PatrocinioNoEncontradoException;
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
			throws EventoRepetidoException,CategoriaNoSeleccionadaException,FechaInvalidaException;

	public Set<String> listarEventos();

	public Set<String> listarEdiciones();

	public DTSeleccionEvento seleccionarEvento(String nomEvento)
			throws EventoNoExisteException;

	public DTEdicion consultarEdicion(String nomEdicion);

	public boolean existeCategoria(String cat);

	public void altaCategoria (String cat);

	public boolean existeEdicion(String nomEdicion);

	public void AltaEdicion(String nomEvento, String nickOrganizador, String nomEdicion, String sigla, String ciudad, String pais, DTFecha fechaIni, DTFecha fechaFin, DTFecha fechaAlta)
			throws EdicionExistenteException, FechasIncompatiblesException;

	public Set<String> listarEdiciones(String nomEvento)
			throws EventoNoExisteException;

	public DTPatrocinio consultarTipoPatrocinioEdicion(String nomEdicion, String codPatrocinio)
			throws EdicionNoExisteException,EdicionSinPatrociniosException, PatrocinioNoEncontradoException;

	public boolean existePatrocinio(String nomEdicion, String nomInstitucion);

	public boolean costoSuperaAporte(String nomEdicion, String nomInstitucion,String nomTipoRegistro, double monto, int cantRegGrat);

	 public void altaPatrocinio(String nomEdicion, String nomInstitucion, NivelPatrocinio nivel, double aporte,  String nomTipoRegistro, int cantRegistrosGratuitos,String codigo, DTFecha fechaAlta)
			throws PatrocinioDuplicadoException;

	 public Set<String> listarPatrocinios(String nomEdicion);

	 public boolean existeCodigoPatrocinioEnEdicion(String edicion, String codigo);

	public boolean esFechaValida(int dia, int mes, int anio);
	public Set<String> listarCategorias();
}
