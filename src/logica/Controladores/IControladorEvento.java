package logica.Controladores;

import java.util.Set;

import logica.Evento;
import logica.DatatypesYEnum.DTEdicion;
import logica.DatatypesYEnum.DTFecha;
import logica.DatatypesYEnum.DTPatrocinio;
import logica.DatatypesYEnum.DTSeleccionEvento;

public interface IControladorEvento {

	public boolean existeEvento(String nomEvento);

	public void darAltaEvento(String nomEvento, String desc, DTFecha fechaAlta, String sigla, Set<String> cetegorias);

	public Set<String> listarEventos();

	public DTSeleccionEvento seleccionarEvento(String nomEvento);

	public DTEdicion consultarEdicion(String nomEdicion);

	public boolean existeEdicion(String nomEdicion);

	public void AltaEdicion(String nomEvento, String nickOrganizador, String nomEdicion, String sigla, String ciudad, String pais, DTFecha fechaIni, DTFecha fechaFin, DTFecha fechaAlta);

	public Set<String> listarEdiciones(String nomEvento);

	public DTPatrocinio consultarTipoPatrocinioEdicion(String nomEdicion, int codPatrocinio);
}
