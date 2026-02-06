package logica.controladores;

import java.time.LocalDate;
import java.util.Map;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import excepciones.CategoriaNoSeleccionadaException;
import excepciones.CostoSuperaAporteException;
import excepciones.EdicionExistenteException;
import excepciones.EdicionNoExisteException;
import excepciones.EdicionNoFinalizadaException;
import excepciones.EdicionSinPatrociniosException;
import excepciones.EdicionYaArchivadaException;
import excepciones.EventoNoExisteException;
import excepciones.EventoRepetidoException;
import excepciones.EventoYaFinalizadoException;
import excepciones.FechaInvalidaException;
import excepciones.FechasIncompatiblesException;
import excepciones.PatrocinioDuplicadoException;
import excepciones.PatrocinioNoEncontradoException;
import excepciones.SiglaRepetidaException;
import excepciones.CostoSuperaAporteException;
import logica.Categoria;
import logica.Edicion;
import logica.EdicionArchivada;
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
import logica.manejadores.ManejadorPersistencia;
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
	public boolean darAltaEvento(
		    String nomEvento,
		    String desc,
		    DTFecha fechaAlta,
		    String sigla,
		    Set<String> nomcategorias
		) throws EventoRepetidoException, SiglaRepetidaException, CategoriaNoSeleccionadaException, FechaInvalidaException {
		    return darAltaEvento(nomEvento, desc, fechaAlta, sigla, nomcategorias, null);
		}

	// Método con imagen (opcional)
	public boolean darAltaEvento(
		    String nomEvento,
		    String desc,
		    DTFecha fechaAlta,
		    String sigla,
		    Set<String> nomcategorias,
		    String imagen
		) throws EventoRepetidoException, SiglaRepetidaException, CategoriaNoSeleccionadaException, FechaInvalidaException {
		    
		    if (existeEvento(nomEvento)) {
		        throw new EventoRepetidoException(nomEvento);
		    }
		    
		    if (manejadorE.existeSigla(sigla)) {
		        throw new SiglaRepetidaException(sigla);
		    }
		    
		    if (nomcategorias == null || nomcategorias.isEmpty()) {
		        throw new CategoriaNoSeleccionadaException("Debe seleccionar al menos una categoría para el evento.");
		    }
		    
		    if (!esFechaValida(fechaAlta.getDia(), fechaAlta.getMes(), fechaAlta.getAnio())) {
		        throw new FechaInvalidaException(fechaAlta.getDia(), fechaAlta.getMes(), fechaAlta.getAnio());
		    }

		    // Asignar imagen por defecto cuando no se proporciona
		    if (imagen == null || imagen.trim().isEmpty()) {
		    	imagen = "/img/eventoSinImagen.png";
		    }

		    Set<Categoria> categorias = manejadorE.getCategorias(nomcategorias);
		    Evento eve = new Evento(nomEvento, desc, fechaAlta, sigla, categorias, imagen);
		    manejadorE.addEvento(eve);
		    return true;
		}

	//listar
	public Set<String> listarEventos() {

		ManejadorEventos manejador = ManejadorEventos.getInstance();
		Set<String> eventos = manejador.listarEventos();

		Set<String> activos = new HashSet<>();
		for (String nombre : eventos) {
			Evento evento = manejador.obtenerEvento(nombre);
			if (evento != null && !evento.estaFinalizado()) {
				activos.add(nombre);
			}
		}

		return activos;

	}

	public Set<String> listarEdiciones(){
		ManejadorEventos manejador = ManejadorEventos.getInstance();
		Map<String, Edicion> ediciones = manejador.getEdiciones();
		Set<String> listaEdiciones = ediciones.keySet();
		return listaEdiciones;
	}    public DTSeleccionEvento seleccionarEvento(String nomEvento)
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
                    // Obtener la edición completa
                    Edicion edicion = manejadorE.obtenerEdicion(ed);
                    // FILTRAR ediciones archivadas SOLO en consulta evento (listado público)
                    if (edicion != null && edicion.getEstado() != EstadoEdicion.ARCHIVADA) {
                        nombresEdiciones.add(ed);
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
	public boolean altaEdicion(String nomEvento, String nickOrganizador, String nomEdicion, String sigla,
	        String ciudad, String pais, DTFecha fechaIni, DTFecha fechaFin, DTFecha fechaAlta) throws EdicionExistenteException, SiglaRepetidaException, FechaInvalidaException, FechasIncompatiblesException, EventoNoExisteException{
	    return altaEdicion(nomEvento, nickOrganizador, nomEdicion, sigla, ciudad, pais, fechaIni, fechaFin, fechaAlta, null);
	}

	// Método con imagen (mantiene compatibilidad): solo imagen, sin video
	public boolean altaEdicion(String nomEvento, String nickOrganizador, String nomEdicion, String sigla,
	        String ciudad, String pais, DTFecha fechaIni, DTFecha fechaFin, DTFecha fechaAlta, String imagen) throws EdicionExistenteException, SiglaRepetidaException, FechaInvalidaException, FechasIncompatiblesException, EventoNoExisteException{
	    return altaEdicion(nomEvento, nickOrganizador, nomEdicion, sigla, ciudad, pais, fechaIni, fechaFin, fechaAlta, imagen, null);
	}

	// Método con imagen (opcional)
	public boolean altaEdicion(String nomEvento, String nickOrganizador, String nomEdicion, String sigla,
	        String ciudad, String pais, DTFecha fechaIni, DTFecha fechaFin, DTFecha fechaAlta, String imagen, String video)  throws EdicionExistenteException, SiglaRepetidaException, FechaInvalidaException, FechasIncompatiblesException, EventoNoExisteException{
	    
	        // Validaciones y lógica original
	        ManejadorEventos manEv = ManejadorEventos.getInstance();
	        Evento event = manEv.obtenerEvento(nomEvento);
	        if (event == null) {
				throw new EventoNoExisteException(nomEvento);
	        }

	        if (existeEdicion(nomEdicion)) {
	            throw new EdicionExistenteException(nomEdicion);// Edición ya existe
	        }

	        if (fechaFin.compareTo(fechaIni) < 0) {
	            throw new FechasIncompatiblesException();// Fechas inválidas
	        }

	        ManejadorUsuario manUs = ManejadorUsuario.getinstance();
	        Usuario usu = manUs.obtenerUsuario(nickOrganizador);
	        if (usu == null || !(usu instanceof Organizador)) {
	            return false; // Usuario no válido
	        }

	        Organizador org = (Organizador) usu;
	        Edicion edi = new Edicion(nomEdicion, sigla, ciudad, pais, fechaIni, fechaFin, fechaAlta, org, event, imagen, video);

	        org.agregarEdicion(edi);
	        event.agregarEdicion(edi);
	        manEv.addEdicion(edi);

	        return true; // Éxito
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

	public String obtenerEventoDeEdicion(String nombreEdicion) throws EdicionNoExisteException {
	    ManejadorEventos manEv = ManejadorEventos.getInstance();
	    Edicion edicion = manEv.obtenerEdicion(nombreEdicion);
	    
	    if (edicion == null) {
	        throw new EdicionNoExisteException(nombreEdicion);
	    }
	    
	    Evento evento = edicion.getEvento();
	    return evento != null ? evento.getNombre() : null;
	}

	public Set<String> listarEdicionesActivas(String nomEvento)
			throws EventoNoExisteException{

	    ManejadorEventos manEv = ManejadorEventos.getInstance();
	    Evento event = manEv.obtenerEvento(nomEvento);

	    if (event == null) {
	        throw new EventoNoExisteException(nomEvento);
	    }

	    Set<String> eds = event.getEdiciones();
	    Set<String> nombres = new java.util.HashSet<>();
	    LocalDate fechaActual = LocalDate.of(2025, 10, 16);

	    if (eds != null) {
	        for (String ed : eds) {
	            if (ed != null) {
	                // Verificar que la edición no haya finalizado
	                Edicion edicion = manejadorE.obtenerEdicion(ed);
	                if (edicion != null) {
	                    DTFecha fechaFin = edicion.getFechaFin();
	                    LocalDate fechaFinEdicion = LocalDate.of(fechaFin.getAnio(), fechaFin.getMes(), fechaFin.getDia());

	                    // Solo agregar la edición si no ha finalizado
	                    if (!fechaFinEdicion.isBefore(fechaActual)) {
	                        nombres.add(ed);
	                    }
	                }
	            }
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
			 throws PatrocinioDuplicadoException, CostoSuperaAporteException {


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

		 if( costoSuperaAporte(nomEdicion, nomInstitucion, nomTipoRegistro, aporte, cantRegistrosGratuitos)) {
			 throw new CostoSuperaAporteException();
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
				if (seleccion.getNombresEdiciones().contains(nomEdicion)) {
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

	public void finalizarEvento(String nomEvento) throws EventoNoExisteException, EventoYaFinalizadoException {
		Evento evento = manejadorE.obtenerEvento(nomEvento);
		if (evento == null) {
			throw new EventoNoExisteException(nomEvento);
		}
		if (evento.estaFinalizado()) {
			throw new EventoYaFinalizadoException(nomEvento);
		}
		evento.finalizar();
	}

	public boolean esEventoFinalizado(String nomEvento) throws EventoNoExisteException {
		Evento evento = manejadorE.obtenerEvento(nomEvento);
		if (evento == null) {
			throw new EventoNoExisteException(nomEvento);
		}
		return evento.estaFinalizado();
	}

	public DTEvento obtenerDTEvento(String nomEvento) throws EventoNoExisteException {
		Evento evento = manejadorE.obtenerEvento(nomEvento);
		if (evento == null) {
			throw new EventoNoExisteException(nomEvento);
		}
		return new DTEvento(evento);
	}
	
	/**
	 * Archiva una edición de evento. 
	 * Solo se pueden archivar ediciones que ya han finalizado y están en estado ACEPTADA.
	 * Los datos de la edición se persisten en disco usando JPA.
	 */
	public void archivarEdicion(String nomEdicion) throws EdicionNoExisteException, EdicionNoFinalizadaException, EdicionYaArchivadaException {
		Edicion edicion = manejadorE.obtenerEdicion(nomEdicion);
		
		if (edicion == null) {
			throw new EdicionNoExisteException(nomEdicion);
		}
		
		// Verificar que la edición esté en estado ACEPTADA
		if (edicion.getEstado() != EstadoEdicion.ACEPTADA) {
			throw new EdicionNoFinalizadaException(nomEdicion + " (debe estar en estado ACEPTADA)");
		}
		
		// Verificar que la edición ya haya finalizado
		LocalDate fechaActual = LocalDate.now();
		DTFecha fechaFin = edicion.getFechaFin();
		LocalDate fechaFinEdicion = LocalDate.of(fechaFin.getAnio(), fechaFin.getMes(), fechaFin.getDia());
		
		if (!fechaFinEdicion.isBefore(fechaActual)) {
			throw new EdicionNoFinalizadaException(nomEdicion);
		}
		
		// Crear la entidad de edición archivada y persistir
		int diaArchivado = fechaActual.getDayOfMonth();
		int mesArchivado = fechaActual.getMonthValue();
		int anioArchivado = fechaActual.getYear();
		
		EdicionArchivada edicionArchivada = new EdicionArchivada(edicion, diaArchivado, mesArchivado, anioArchivado);
		
		// Persistir en base de datos
		ManejadorPersistencia manejadorP = ManejadorPersistencia.getInstance();
		manejadorP.persistirEdicionArchivada(edicionArchivada);
		
		// Cambiar el estado de la edición a ARCHIVADA
		edicion.setEstado(EstadoEdicion.ARCHIVADA);
	}
	
	/**
	 * Lista las ediciones finalizadas y aceptadas de un organizador que pueden ser archivadas.
	 */
	public Set<DTEdicion> listarEdicionesArchivables(String nicknameOrganizador) {
		Set<DTEdicion> resultado = new HashSet<>();
		LocalDate fechaActual = LocalDate.now();
		
		// Obtener todas las ediciones del organizador en estado ACEPTADA
		Set<DTEdicion> edicionesAceptadas = listarEdicionesOrganizadasPorEstado(nicknameOrganizador, EstadoEdicion.ACEPTADA);
		ManejadorPersistencia manejadorP = ManejadorPersistencia.getInstance();
		
		for (DTEdicion edicion : edicionesAceptadas) {
			DTFecha fechaFin = edicion.getFechaFin();
			LocalDate fechaFinEdicion = LocalDate.of(fechaFin.getAnio(), fechaFin.getMes(), fechaFin.getDia());
			
			// Solo agregar si ya finalizó y no está archivada
			if (fechaFinEdicion.isBefore(fechaActual) && !manejadorP.estaArchivada(edicion.getNombre())) {
				resultado.add(edicion);
			}
		}
		
		return resultado;
	}
	
	/**
	 * Verifica si una edición está archivada.
	 */
	public boolean estaEdicionArchivada(String nomEdicion) {
		ManejadorPersistencia manejadorP = ManejadorPersistencia.getInstance();
		return manejadorP.estaArchivada(nomEdicion);
	}
	
	/**
	 * Lista las ediciones archivadas de un organizador.
	 */
	public Set<String> listarEdicionesArchivadasPorOrganizador(String nicknameOrganizador) {
		ManejadorPersistencia manejadorP = ManejadorPersistencia.getInstance();
		Set<String> resultado = new HashSet<>();
		
		for (EdicionArchivada ea : manejadorP.listarEdicionesArchivadasPorOrganizador(nicknameOrganizador)) {
			resultado.add(ea.getNombreEdicion());
		}
		
		return resultado;
	}
	
	public void incrementarVisitas(String nomEv) throws EventoNoExisteException {
		if (!existeEvento(nomEv)) {
			throw new EventoNoExisteException(nomEv);
		}
		ManejadorEventos manEv  = ManejadorEventos.getInstance();
		Evento eve = manEv.obtenerEvento(nomEv);
		eve.incrementarVisitas();
	}
	
	public List<DTEvento> obtenerMasVisitados() {
		ManejadorEventos manEv  = ManejadorEventos.getInstance();
		Set<DTEvento> dtEventos = manEv.getDTEventos();
	    return dtEventos.stream()
	    		.filter(ev -> ev.getVisitas() > 0)
	            .sorted(Comparator.comparingInt(DTEvento::getVisitas).reversed())
	            .limit(5)
	            .toList();
	}
	
	public void setVisitas(String nomEv, int visitas) {
		ManejadorEventos manEv  = ManejadorEventos.getInstance();
		Evento eve = manEv.obtenerEvento(nomEv);
		eve.setVisitas(visitas);
	}
}
