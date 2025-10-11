package logica.controladores;

import java.util.Map;
import java.util.HashSet;
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
import logica.Categoria;
import logica.Edicion;
import logica.Evento;
import logica.Institucion;
import logica.Organizador;

import logica.Patrocinio;
import logica.TipoDeRegistro;
import logica.Usuario;
import logica.datatypesyenum.DTEdicion;
import logica.datatypesyenum.DTEvento;
import logica.datatypesyenum.DTFecha;
import logica.datatypesyenum.DTPatrocinio;
import logica.datatypesyenum.DTSeleccionEvento;
import logica.datatypesyenum.EstadoEdicion;
import logica.datatypesyenum.NivelPatrocinio;
import logica.manejadores.ManejadorEventos;
import logica.manejadores.ManejadorUsuario;

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


	//altaEvento con excepcion

	// Método sin imagen (mantiene compatibilidad)
	public void darAltaEvento(String nomEvento, String desc, DTFecha fechaAlta, String sigla, Set<String> nomcategorias)
	        throws EventoRepetidoException, CategoriaNoSeleccionadaException, FechaInvalidaException {
		darAltaEvento(nomEvento, desc, fechaAlta, sigla, nomcategorias, null);
	}

	// Método con imagen (opcional)
	public void darAltaEvento(String nomEvento, String desc, DTFecha fechaAlta, String sigla, Set<String> nomcategorias, String imagen)
	        throws EventoRepetidoException, CategoriaNoSeleccionadaException, FechaInvalidaException {

	    // Verificamos si ya existe un evento con ese nombre
	    if (existeEvento(nomEvento)) {
	        throw new EventoRepetidoException(nomEvento);
	    }

	    if (nomcategorias == null || nomcategorias.isEmpty()) {
	        throw new CategoriaNoSeleccionadaException();
	    }

	    if (!esFechaValida(fechaAlta.getDia(), fechaAlta.getMes(), fechaAlta.getAnio())) {
            throw new FechaInvalidaException(fechaAlta.getDia(), fechaAlta.getMes(), fechaAlta.getAnio());
        }


	    // Si no existe, creamos el evento
	    Set<Categoria> categorias = manejadorE.getCategorias(nomcategorias);
	    Evento eve = new Evento(nomEvento, desc, fechaAlta, sigla, categorias, imagen);
	    manejadorE.addEvento(eve);
	}

	//listar
	public Set<String> listarEventos() {

		ManejadorEventos manejador = ManejadorEventos.getInstance();
		Set<String> eventos = manejador.listarEventos();

		return eventos;

	}

	public Set<String> listarEdiciones(){
		ManejadorEventos manejador = ManejadorEventos.getInstance();
		Map<String, Edicion> ediciones = manejador.getEdiciones();
		Set<String> listaEdiciones = ediciones.keySet();
		return listaEdiciones;
		}

    public DTSeleccionEvento seleccionarEvento(String nomEvento)
    		throws EventoNoExisteException {

        Evento eve = manejadorE.obtenerEvento(nomEvento);

        if (eve == null) {
            throw new EventoNoExisteException(nomEvento);
        }

        DTEvento dto = new DTEvento(eve);

        //no entiendo bien estas dos cosas todavia
        Set<String> nombresCategorias = new HashSet<>();
        if (eve.getCategorias() != null) {
            for (String c : eve.getCategorias()) {
                if (c != null && c != null) {
                    nombresCategorias.add(c);
                }
            }
        }


        Set<String> nombresEdiciones = new HashSet<>();
        Set<DTEdicion> edicionesCompletas = new HashSet<>();
        if (eve.getEdiciones() != null) {
            for (String ed : eve.getEdiciones()) {

                if (ed != "") {
                    nombresEdiciones.add(ed);
                    // Obtener la edición completa con su imagen
                    Edicion edicion = manejadorE.obtenerEdicion(ed);
                    if (edicion != null) {
                        edicionesCompletas.add(edicion.getDTEdicion());
                    }
                }
            }
        }

        return new DTSeleccionEvento(dto, nombresCategorias, nombresEdiciones, edicionesCompletas);
    }

    public DTEdicion consultarEdicion(String nomEdicion) {
    	Edicion edi = manejadorE.obtenerEdicion(nomEdicion);

    	DTEdicion dte = edi.getDTEdicion();
    	return dte;
    }//responsabilizar a evento por la creacion del DT


	//altaCategoria
	public boolean existeCategoria(String cat) {
		ManejadorEventos manejador = ManejadorEventos.getInstance();
		return manejador.existeCategoria(cat);
	}

	public void altaCategoria(String cat){
		ManejadorEventos manejador = ManejadorEventos.getInstance();
		Categoria nueva = new Categoria(cat);
		manejador.addCategoria(nueva);
	}



	public boolean existeEdicion(String nomEdicion) {

	    return manejadorE.existeEdicion(nomEdicion);
	}


	// Método sin imagen (mantiene compatibilidad)
	public void altaEdicion(String nomEvento, String nickOrganizador, String nomEdicion, String sigla, String ciudad, String pais, DTFecha fechaIni, DTFecha fechaFin, DTFecha fechaAlta)
			throws EdicionExistenteException, FechasIncompatiblesException{
		altaEdicion(nomEvento, nickOrganizador, nomEdicion, sigla, ciudad, pais, fechaIni, fechaFin, fechaAlta, null);
	}

	// Método con imagen (opcional)
	public void altaEdicion(String nomEvento, String nickOrganizador, String nomEdicion, String sigla, String ciudad, String pais, DTFecha fechaIni, DTFecha fechaFin, DTFecha fechaAlta, String imagen)
			throws EdicionExistenteException, FechasIncompatiblesException{

	        ManejadorEventos manEv = ManejadorEventos.getInstance();
	        Evento event = manEv.obtenerEvento(nomEvento);
	        if (event == null) {
	            throw new IllegalArgumentException("No existe el evento: " + nomEvento);
	        }

	        if (existeEdicion(nomEdicion)) {

			}

	        if (fechaFin.compareTo(fechaIni) < 0) { //error de fecha
	            throw new FechasIncompatiblesException();
	        }


	        ManejadorUsuario manUs = ManejadorUsuario.getinstance();
	        Usuario usu = manUs.obtenerUsuario(nickOrganizador);
	        if (usu == null) {
	            throw new IllegalArgumentException("No existe el usuario: " + nickOrganizador);
	        }
	        if (!(usu instanceof Organizador)) {
	            throw new IllegalArgumentException("El usuario '" + nickOrganizador + "' no es organizador.");
	        }
	        Organizador org = (Organizador) usu;

	        // Crear la edición con el evento asociado
            Edicion edi = new Edicion(nomEdicion, sigla, ciudad, pais, fechaIni, fechaFin, fechaAlta, org, event, imagen);


	        org.agregarEdicion(edi);
	        event.agregarEdicion(edi);
	        manEv.addEdicion(edi);
	}

	public Set<String> listarEdiciones(String nomEvento)
			throws EventoNoExisteException{

	    ManejadorEventos manEv = ManejadorEventos.getInstance();
	    Evento event = manEv.obtenerEvento(nomEvento);

	    if (event == null) {
	        throw new EventoNoExisteException(nomEvento);
	    }

	    Set<String> eds = event.getEdiciones();
	    Set<String> nombres = new java.util.HashSet<>();
	    if (eds != null) {
	        for (String ed : eds) {
	            if (ed != null) nombres.add(ed);
	        }
	    }
	    return nombres;
	}


	public DTPatrocinio consultarTipoPatrocinioEdicion(String nomEdicion, String codPatrocinio)
			throws EdicionNoExisteException, EdicionSinPatrociniosException, PatrocinioNoEncontradoException {

	    Edicion edicion = manejadorE.obtenerEdicion(nomEdicion);

	    if (edicion == null) {
	        throw new EdicionNoExisteException(nomEdicion);
	    }

	    Set<Patrocinio> pats = edicion.getPatrocinios();

	    if (pats == null || pats.isEmpty()) {
	        throw new EdicionSinPatrociniosException(nomEdicion);
	    }

	    for (Patrocinio p : pats) {
	        if (p != null && p.getCodigo().equals(codPatrocinio)) {
	            return new DTPatrocinio(
	                p.getFechaAlta(),
	                p.getMonto(),
	                p.getCodigo(),
	                p.getNivel(),
	                nomEdicion,
	                p.getNombreInstitucion(),
	                p.getCantidadTipoDeRegistro().getCantRegistros(),
	                p.getNombreTipoDeRegistro()
	            );
	        }
	    }


	    throw new PatrocinioNoEncontradoException(codPatrocinio, nomEdicion);
	}

	 public boolean existePatrocinio(String nomEdicion, String nomInstitucion) {

	    	Edicion edicion = manejadorE.obtenerEdicion(nomEdicion);
	    	return edicion.esPatrocinador(nomInstitucion);

	    }

	 public boolean costoSuperaAporte(String nomEdicion, String nomInstitucion, String nomTipoRegistro, double monto, int cantRegGrat) {
		 ManejadorEventos manejadorE = ManejadorEventos.getInstance();
		 Edicion edicion = manejadorE.obtenerEdicion(nomEdicion);
		 TipoDeRegistro tipo = edicion.getTipoDeRegistro(nomTipoRegistro);
		 double costo = tipo.getCosto();
		 return cantRegGrat * costo > 0.2 * monto;
	 }


	 public void altaPatrocinio(String nomEdicion, String nomInstitucion, NivelPatrocinio nivel, double aporte, String nomTipoRegistro, int cantRegistrosGratuitos, String codigo, DTFecha fechaAlta)
			 throws PatrocinioDuplicadoException {


		 Edicion edicion = manejadorE.obtenerEdicion(nomEdicion);


		 if (edicion == null) {
		        throw new IllegalArgumentException("No existe la edición: " + nomEdicion);
		 }

		 if (existePatrocinio(nomEdicion, nomInstitucion)) {
		        throw new PatrocinioDuplicadoException(nomInstitucion, nomEdicion);
		    }


		 TipoDeRegistro tipo = edicion.getTipoDeRegistro(nomTipoRegistro);

		 if (tipo == null) {
		        throw new IllegalArgumentException("No existe el tipo de registro: " + nomTipoRegistro);
		    }


		 ManejadorUsuario manejadorU = ManejadorUsuario.getinstance();
		 Institucion ins = manejadorU.obtenerInstitucion(nomInstitucion);
		 Patrocinio pat = edicion.altaPatrocinio(ins, nivel, aporte, tipo, cantRegistrosGratuitos, codigo, fechaAlta);
		 ins.agregarPatrocinio(pat);

	 }
	 
	
public Set<DTEvento> obtenerDTEventos(){
	ManejadorEventos manejador = ManejadorEventos.getInstance();
	return manejador.getDTEventos();
}


	 public Set<String> listarPatrocinios(String nomEdicion){
		 ManejadorEventos manejadorE = ManejadorEventos.getInstance();
		 Edicion edicion = manejadorE.obtenerEdicion(nomEdicion);
		 return edicion.getCodigosPatrocinios();
	 }

	public DTEvento obtenerEventoPorEdicion(String nomEdicion){
		ManejadorEventos manejador = ManejadorEventos.getInstance();
		Set<DTEvento> eventos = manejador.getDTEventos();
		
		// Buscar el evento que contiene esta edición
		for (DTEvento evento : eventos) {
			try {
				DTSeleccionEvento seleccion = seleccionarEvento(evento.getNombre());
				if (seleccion.getEdiciones().contains(nomEdicion)) {
					return evento;
				}
			} catch (EventoNoExisteException e) {
				throw new IllegalStateException("No existe evento asociado a la edicion: " + nomEdicion, e);
			}
		}
		return null;
	}

	 public boolean existeCodigoPatrocinioEnEdicion(String edicion, String codigo) {

		 Edicion ed2 = manejadorE.obtenerEdicion(edicion);

		 for (String c : ed2.getCodigosPatrocinios()) {
			 if (c.contentEquals(codigo)) {
				return true;
			}
		 }

		return false;
	 }



	//metodos auxiliares para validar fechas
		public boolean esFechaValida(int dia, int mes, int anio) {
		    if (mes < 1 || mes > 12) return false;
		    if (dia < 1 || anio < 1) return false;

		    // Días por mes
		    int[] diasPorMes = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

		    // Ajustar febrero para año bisiesto
		    if (esBisiesto(anio)) {
		        diasPorMes[1] = 29;
		    }

		    return dia <= diasPorMes[mes - 1];
		}

		private boolean esBisiesto(int anio) {
		    return anio % 4 == 0 && anio % 100 != 0 || anio % 400 == 0;
		}


		public Set<String> listarCategorias() {
			Set<String> categorias = manejadorE.getNombresCategorias();
			System.out.println("Categorías en el manejador: " + categorias); // Depuración
			return categorias;
		}


	public void actualizarEstadoEdicion(String nomEdicion, EstadoEdicion nuevoEstado) throws EdicionNoExisteException {
		Edicion edi = manejadorE.obtenerEdicion(nomEdicion);
		if (edi == null) {
			throw new EdicionNoExisteException(nomEdicion);
		}
		edi.setEstado(nuevoEstado);
	}

	public Set<String> listarEdicionesPorEstado(EstadoEdicion estado) {
		ManejadorEventos manejador = ManejadorEventos.getInstance();
		Set<String> resultado = new HashSet<>();
		for (Edicion ed : manejador.obtenerEdicionesPorEstado(estado)) {
			resultado.add(ed.getNombre());
		}
		return resultado;
	}

	public Set<DTEdicion> listarEdicionesOrganizadasPorEstado(String nicknameOrganizador, EstadoEdicion estado) {
		ManejadorEventos manejador = ManejadorEventos.getInstance();
		Set<DTEdicion> resultado = new HashSet<>();
		for (Edicion ed : manejador.obtenerEdicionesPorEstado(estado)) {
			if (ed.getOrganizador().getNickname().equals(nicknameOrganizador)) {
				resultado.add(ed.getDTEdicion());
			}
		}
		return resultado;
	}
	
	public Set<String> listarEdicionesPorEstadoDeEvento(String nomEvento, EstadoEdicion estado){
		ManejadorEventos manejador = ManejadorEventos.getInstance();
		Evento eve = manejador.obtenerEvento(nomEvento);
		return eve.obtenerEdicionesPorEstado(estado);
	}

}
