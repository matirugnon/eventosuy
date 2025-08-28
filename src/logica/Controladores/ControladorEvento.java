package logica.Controladores;

import java.util.Map;

import java.util.HashSet;

import java.util.Set;

import logica.Categoria;
import logica.Edicion;
import logica.Evento;



import logica.DatatypesYEnum.DTEdicion;
import logica.DatatypesYEnum.DTEvento;
import logica.DatatypesYEnum.DTFecha;
import logica.DatatypesYEnum.DTSeleccionEvento;
import logica.manejadores.ManejadorEventos;

public class ControladorEvento implements IControladorEvento {

	private ManejadorEventos manejadorE;
	private static ControladorEvento instancia = null;

	public ControladorEvento() {

    	//inicializo el manejador
    	this.manejadorE = ManejadorEventos.getInstance();
    }


	public static ControladorEvento getInstance() {
		if (instancia == null)
            instancia = new ControladorEvento();
        return instancia;
	}



	public boolean existeEvento(String nomEvento) {
		return manejadorE.existe(nomEvento);

	}

	public void darAltaEvento(String nomEvento, String desc, DTFecha fechaAlta, String sigla, Set<String> nomcategorias) {
	    Set<Categoria> categorias = manejadorE.getCategorias(nomcategorias);   // convierte los nombres de categorías en objetos Categoria
	    Evento e = new Evento(nomEvento, desc, fechaAlta, sigla, categorias); // crea el evento con todos los datos
	    manejadorE.addEvento(e);                                             // lo guarda en el manejador
	}

	//listar
	public Set<String> listarEventos() {
	    return manejadorE.listarEventos();
	}

	public Set<String> listarEdiciones(){
		ManejadorEventos manejador = ManejadorEventos.getInstance();
		Map<String, Edicion> ediciones = manejador.getEdiciones();
		Set<String> listaEdiciones = ediciones.keySet();
		return listaEdiciones;
		}

    public DTSeleccionEvento seleccionarEvento(String nomEvento) {
        Evento e = manejadorE.obtenerEvento(nomEvento);
        if (e == null) {
            return null;
        }


        DTEvento dto = new DTEvento(e);

        //no entiendo bien estas dos cosas todavia
        Set<String> nombresCategorias = new HashSet<>();
        if (e.getCategorias() != null) {
            for (Categoria c : e.getCategorias()) {
                if (c != null && c.getNombre() != null) {
                    nombresCategorias.add(c.getNombre());
                }
            }
        }


        Set<String> nombresEdiciones = new HashSet<>();
        if (e.getEdiciones() != null) {
            for (Edicion ed : e.getEdiciones()) {
                if (ed != null && ed.getNombre() != null) {
                    nombresEdiciones.add(ed.getNombre());
                }
            }
        }

        return new DTSeleccionEvento(dto, nombresCategorias, nombresEdiciones);
    }

    public DTEdicion consultarEdicion(String nomEdicion) {
    	Edicion e = manejadorE.obtenerEdicion(nomEdicion);
    	DTEdicion dte = new DTEdicion(e);
    	return dte;
    }


	//altaCategoria
	public boolean existeCategoria(String cat) {
		ManejadorEventos manejador = ManejadorEventos.getInstance();
		return manejador.existeCategoria(cat);
	}

	public void altaCategoria (String cat){
		ManejadorEventos manejador = ManejadorEventos.getInstance();
		Categoria nueva = new Categoria(cat);
		manejador.addCategoria(nueva);
	}

}