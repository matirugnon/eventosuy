package logica.Controladores;

import java.util.Map;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import logica.Categoria;
import logica.Edicion;
import logica.Evento;
import logica.Organizador;
import logica.Usuario;
import logica.DatatypesYEnum.DTEdicion;
import logica.DatatypesYEnum.DTEvento;
import logica.DatatypesYEnum.DTFecha;
import logica.DatatypesYEnum.DTSeleccionEvento;
import logica.manejadores.ManejadorEventos;
import logica.manejadores.ManejadorUsuario;

public class ControladorEvento implements IControladorEvento {

	private ManejadorEventos manejadorE;
	private static ControladorEvento instancia = null;

	public ControladorEvento() {

    	//inicializo el manejador
    	this.manejadorE = ManejadorEventos.getInstance();
    	manejadorE.inicializarCategoriasDefault();
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

	    Set<Categoria> categorias = manejadorE.getCategorias(nomcategorias);
	    // convierte los nombres de categorías en objetos Categoria
	    Evento e = new Evento(nomEvento, desc, fechaAlta, sigla, categorias); // crea el evento con todos los datos
	    manejadorE.addEvento(e);                                             // lo guarda en el manejador
	}

	//listar
	public Set<Evento> listarEventos() {
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
            for (String c : e.getCategorias()) {
                if (c != null && c != null) {
                    nombresCategorias.add(c);
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


	public void AltaEdicion(String nombre, String sigla, String ciudad, String pais, DTFecha inicio, DTFecha fin, DTFecha alta, String nombreEvento, String nombreOrg) {


		Evento ev = manejadorE.obtenerEvento(nombreEvento);

		ManejadorUsuario mu = ManejadorUsuario.getinstance();

		Organizador org = (Organizador) mu.obtenerUsuario(nombreOrg); //castee a organzizador

		Edicion ed = ev.NuevaEdicion(nombre,sigla,ciudad,pais,inicio,fin,alta,org);

		org.agregarEdicion(ed);

	}

	public boolean existeEdicion(String nombre) {
		// IMPLEMENTAR
		return false;
	}


	public Set<Organizador> listarOrganizadores() {
	    Set<Organizador> organizadores = new HashSet<>();

	    // Obtener instancia del controlador de usuarios
	    ControladorUsuario ctrlUsuario = ControladorUsuario.getInstance();
	    List<Usuario> todosLosUsuarios = ctrlUsuario.listarUsuarios(); // Ya tienes este método

	    // Filtrar los que son Organizador
	    for (Usuario u : todosLosUsuarios) {
	        if (u instanceof Organizador) {
	            organizadores.add((Organizador) u);
	        }
	    }

	    return organizadores;
	}

}