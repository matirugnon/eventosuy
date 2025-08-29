package logica.Controladores;



import java.util.HashSet;
import java.util.Set;

import logica.Categoria;
import logica.Edicion;
import logica.Evento;
import logica.Organizador;
import logica.Patrocinio;
import logica.Usuario;
import logica.DatatypesYEnum.DTEdicion;
import logica.DatatypesYEnum.DTEvento;
import logica.DatatypesYEnum.DTFecha;
import logica.DatatypesYEnum.DTPatrocinio;
import logica.DatatypesYEnum.DTSeleccionEvento;

import logica.manejadores.ManejadorEventos;
import logica.manejadores.ManejadorUsuario;

public class ControladorEvento implements IControladorEvento {

	private ManejadorEventos manejadorE;

	public ControladorEvento() {

    	//inicializo el manejador
    	this.manejadorE = ManejadorEventos.getInstance();
    }
	
	
	
	public boolean existeEvento(String nomEvento) {
		return manejadorE.existe(nomEvento);
		
	}
	
	public void darAltaEvento(String nomEvento, String desc, DTFecha fechaAlta, String sigla, Set<String> nomcategorias) {
	    Set<Categoria> categorias = manejadorE.getCategorias(nomcategorias);   // convierte los nombres de categorías en objetos Categoria
	    Evento e = new Evento(nomEvento, desc, fechaAlta, sigla, categorias); // crea el evento con todos los datos
	    manejadorE.addEvento(e);                                             // lo guarda en el manejador
	}
	
	public Set<String> listarEventos() {
	    return manejadorE.listarEventos();
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
		
	
	public boolean existeEdicion(String nomEdicion) {
	    
	    return manejadorE.existeEdicion(nomEdicion);
	}
	
	
	public void AltaEdicion(String nomEvento, String nickOrganizador, String nomEdicion, String sigla, String ciudad, String pais, DTFecha fechaIni, DTFecha fechaFin, DTFecha fechaAlta) {

	        ManejadorEventos me = ManejadorEventos.getInstance();
	        Evento ev = me.obtenerEvento(nomEvento);
	        if (ev == null) {
	            throw new IllegalArgumentException("No existe el evento: " + nomEvento);
	        }
	        

	        ManejadorUsuario mu = ManejadorUsuario.getinstance();
	        Usuario u = mu.obtenerUsuario(nickOrganizador);
	        if (u == null) {
	            throw new IllegalArgumentException("No existe el usuario: " + nickOrganizador);
	        }
	        if (!(u instanceof Organizador)) {
	            throw new IllegalArgumentException("El usuario '" + nickOrganizador + "' no es organizador.");
	        }
	        Organizador org = (Organizador) u;

	        Edicion ed = new Edicion(nomEdicion, sigla, ciudad, pais, fechaIni, fechaFin, fechaAlta);
	        ed.setOrganizador(org);

	        org.agregarEdicion(ed);
	        ev.agregarEdicion(ed);
	        me.addEdicion(ed);
	}
	
	public Set<String> listarEdiciones(String nomEvento){
	    ManejadorEventos me = ManejadorEventos.getInstance();
	    Evento e = me.obtenerEvento(nomEvento);
	    if (e == null) {
	        throw new IllegalArgumentException("No existe el evento: " + nomEvento);
	    }

	    Set<Edicion> eds = e.getEdiciones();
	    Set<String> nombres = new java.util.HashSet<>();
	    if (eds != null) {
	        for (Edicion ed : eds) {
	            if (ed != null) nombres.add(ed.getNombre());
	        }
	    }
	    return nombres;
	}

	
	public DTPatrocinio consultarTipoPatrocinioEdicion(String nomEdicion, int codPatrocinio) {
	    ManejadorEventos me = ManejadorEventos.getInstance();
	    Edicion ed = me.obtenerEdicion(nomEdicion);
	    if (ed == null) {
	        throw new IllegalArgumentException("No existe la edición: " + nomEdicion);
	    }

	    Set<Patrocinio> pats = ed.getPatrocinios();
	    if (pats == null || pats.isEmpty()) {
	        throw new IllegalArgumentException("La edición '" + nomEdicion + "' no tiene patrocinios.");
	    }

	    for (Patrocinio p : pats) {
	        if (p != null && p.getCodigo() == codPatrocinio) {
	            
	            return new DTPatrocinio(
	                p.getFechaAlta(),
	                p.getMonto(),
	                p.getCodigo(),
	                p.getNivel()
	            );
	        }
	    }

	    throw new IllegalArgumentException(
	        "No existe el patrocinio con código " + codPatrocinio + " en la edición '" + nomEdicion + "'."
	    );
	}

}