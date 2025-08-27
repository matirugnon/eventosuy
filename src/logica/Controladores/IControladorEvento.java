package logica.Controladores;

import java.util.Set;

import logica.DatatypesYEnum.DTEdicion;
import logica.DatatypesYEnum.DTFecha;
import logica.DatatypesYEnum.DTSeleccionEvento;

public interface IControladorEvento {
	
	public boolean existeEvento(String nomEvento); 
	
	public void darAltaEvento(String nomEvento, String desc, DTFecha fechaAlta, String sigla, Set<String> cetegorias);
	
	public Set<String> listarEventos();
	
	public DTSeleccionEvento seleccionarEvento(String nomEvento);
	
	public DTEdicion consultarEdicion(String nomEdicion);
}
