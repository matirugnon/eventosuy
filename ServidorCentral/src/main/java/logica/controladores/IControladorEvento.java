package logica.controladores;



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

import excepciones.SiglaRepetidaException;

import logica.datatypesyenum.DTEdicion;
import logica.datatypesyenum.DTEvento;
import logica.datatypesyenum.DTFecha;
import logica.datatypesyenum.DTPatrocinio;
import logica.datatypesyenum.DTSeleccionEvento;
import logica.datatypesyenum.EstadoEdicion;
import logica.datatypesyenum.NivelPatrocinio;



public interface IControladorEvento {



	static IControladorEvento getInstance() {



	    return ControladorEvento.getInstance();



	}



	public boolean existeEvento(String nomEvento);



	public boolean darAltaEvento(String nomEvento, String desc, DTFecha fechaAlta, String sigla, Set<String> nomcategorias)
			throws EventoRepetidoException, SiglaRepetidaException, CategoriaNoSeleccionadaException, FechaInvalidaException;



	public Set<String> listarEventos();



	public Set<String> listarEdiciones();

	public Set<String> listarEdicionesActivas(String nomEvento)
			throws EventoNoExisteException;

	public DTSeleccionEvento seleccionarEvento(String nomEvento)



			throws EventoNoExisteException;



	public DTEdicion consultarEdicion(String nomEdicion);



	public boolean existeCategoria(String cat);



	public void altaCategoria(String cat);



	public boolean existeEdicion(String nomEdicion);



	public boolean altaEdicion(String nomEvento, String nickOrganizador, String nomEdicion, String sigla,
	        String ciudad, String pais, DTFecha fechaIni, DTFecha fechaFin, DTFecha fechaAlta) 
			throws EdicionExistenteException, SiglaRepetidaException, FechaInvalidaException, FechasIncompatiblesException, EventoNoExisteException;



	public Set<String> listarEdiciones(String nomEvento)
			throws EventoNoExisteException;



	public DTPatrocinio consultarTipoPatrocinioEdicion(String nomEdicion, String codPatrocinio)



			throws EdicionNoExisteException, EdicionSinPatrociniosException, PatrocinioNoEncontradoException;



	public boolean existePatrocinio(String nomEdicion, String nomInstitucion);



	public boolean costoSuperaAporte(String nomEdicion, String nomInstitucion, String nomTipoRegistro, double monto, int cantRegGrat);



	 public void altaPatrocinio(String nomEdicion, String nomInstitucion, NivelPatrocinio nivel, double aporte,  String nomTipoRegistro, int cantRegistrosGratuitos, String codigo, DTFecha fechaAlta)



			throws PatrocinioDuplicadoException;



	 public Set<String> listarPatrocinios(String nomEdicion);



	 public boolean existeCodigoPatrocinioEnEdicion(String edicion, String codigo);



	public boolean esFechaValida(int dia, int mes, int anio);



	public Set<String> listarCategorias();



	public Set<DTEvento> obtenerDTEventos();



	public DTEvento obtenerEventoPorEdicion(String nomEdicion);
	
	public String obtenerEventoDeEdicion(String nombreEdicion) throws EdicionNoExisteException;



	public void actualizarEstadoEdicion(String nomEdicion, EstadoEdicion nuevoEstado) throws EdicionNoExisteException;



	public Set<String> listarEdicionesPorEstado(EstadoEdicion estado);



	public Set<DTEdicion> listarEdicionesOrganizadasPorEstado(String nicknameOrganizador, EstadoEdicion estado);



	public boolean altaEdicion(String string, String string2, String string3, String string4, String string5,
			String string6, DTFecha dtFecha, DTFecha dtFecha2, DTFecha dtFecha3, String string7) throws EdicionExistenteException, SiglaRepetidaException, FechaInvalidaException, FechasIncompatiblesException, EventoNoExisteException;






	public boolean darAltaEvento(String string, String string2, DTFecha dtFecha, String string3, Set<String> nomCategorias,
			String string4) throws EventoRepetidoException, SiglaRepetidaException, CategoriaNoSeleccionadaException, FechaInvalidaException;


	public Set<String> listarEdicionesPorEstadoDeEvento(String nomEvento, EstadoEdicion estado);


}



