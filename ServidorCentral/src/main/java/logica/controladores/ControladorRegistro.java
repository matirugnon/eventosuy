package logica.controladores;

import java.util.Set;
import java.util.HashSet;

import excepciones.EdicionNoExisteException;
import excepciones.NombreTipoRegistroDuplicadoException;
import excepciones.UsuarioNoExisteException;
import excepciones.UsuarioYaRegistradoEnEdicionException;
import logica.Asistente;
import logica.Edicion;
import logica.Patrocinio;
import logica.Registro;
import logica.manejadores.ManejadorEventos;
import logica.manejadores.ManejadorUsuario;
import logica.TipoDeRegistro;
import logica.Usuario;
import logica.datatypesyenum.DTFecha;
import logica.datatypesyenum.DTRegistro;
import logica.datatypesyenum.DTTipoDeRegistro;

public class ControladorRegistro implements IControladorRegistro {



	private static ControladorRegistro instancia = null;

	private ControladorRegistro() {

    }

	public static ControladorRegistro getInstance() {
		if (instancia == null)
            instancia = new ControladorRegistro();
        return instancia;
	}


//Alta de Tipo de Registro
	public boolean existeTipoDeRegistro(String nombreEd, String nombreTipo) {
		ManejadorEventos manejador = ManejadorEventos.getInstance();
		Edicion edicion = manejador.obtenerEdicion(nombreEd);
	return edicion.existeTipoDeRegistro(nombreTipo);
	}

	public boolean altaTipoDeRegistro(String nombreEd, String nombreTipo, String descripcion, double costo, int cupo) throws NombreTipoRegistroDuplicadoException {

		if (existeTipoDeRegistro(nombreEd, nombreTipo)) {
			 throw new NombreTipoRegistroDuplicadoException(nombreTipo);
		}

		ManejadorEventos manejador = ManejadorEventos.getInstance();
		Edicion edicion =manejador.obtenerEdicion(nombreEd);
		TipoDeRegistro tipo = new TipoDeRegistro(nombreTipo, descripcion, costo, cupo, edicion);
		edicion.agregarTipoDeRegistro(tipo, nombreTipo);
		return true;
	}

	public Set<String> listarTipoRegistro(String nombreEd)
			throws EdicionNoExisteException{
		ManejadorEventos manejador = ManejadorEventos.getInstance();
		Edicion edicion = manejador.obtenerEdicion(nombreEd);

		if (edicion == null) {
			throw new EdicionNoExisteException(nombreEd);
		}

		return edicion.getNombresTiposDeRegistro();
	}

	public DTTipoDeRegistro consultaTipoDeRegistro(String nombreEd, String nombreReg) {
		ManejadorEventos manejador = ManejadorEventos.getInstance();
		Edicion edicion = manejador.obtenerEdicion(nombreEd);
		TipoDeRegistro reg = edicion.getTipoDeRegistro(nombreReg);
		return reg.getDTTipoDeRegistro();
	}


	public Set<String> obtenerNomsTipoRegistro(String nickusuario) {
	    ManejadorUsuario manUs = ManejadorUsuario.getinstance();
	    Usuario usr = manUs.obtenerUsuario(nickusuario);

	    // Caso 1: no existe
	    if (usr == null) {
	        return null; // o Collections.emptySet() si preferís no usar null
	    }

	    // Caso 2: si es Asistente, casteamos y devolvemos sus tipos
	    if (usr instanceof Asistente) {
	        Asistente asist = (Asistente) usr;
	        return asist.getNomsTipo();
	    }

	    // Caso 3: existe pero no es Asistente
	    return null; // o Collections.emptySet()
	}

	public boolean estaRegistrado(String nomEdicion, String nickAsistente) throws UsuarioNoExisteException {
		ManejadorUsuario manUs = ManejadorUsuario.getinstance();
		Usuario usr = manUs.obtenerUsuario(nickAsistente);
		if (usr instanceof Asistente asist) {
			for (Registro reg: asist.getRegistros()) {
				if (reg.getTipoDeRegistro().getNombreEdicion().equals(nomEdicion)) {
					return true;
				}
			}
			return false;
		} else {
			 throw new UsuarioNoExisteException("El usuario " + nickAsistente + " no es asistente");
		}
	}

	public boolean alcanzoCupo(String nomEdicion, String nomTipoRegistro) {
		ManejadorEventos manejador = ManejadorEventos.getInstance();
		Edicion edicion = manejador.obtenerEdicion(nomEdicion);
		TipoDeRegistro tipo = edicion.getTipoDeRegistro(nomTipoRegistro);
		return tipo.alcanzoCupo();
	}

	public void altaRegistro(String nomEdicion, String nickAsistente, String nomTipoRegistro, DTFecha fechaRegistro, double costo)
			throws UsuarioYaRegistradoEnEdicionException, UsuarioNoExisteException{


		if (estaRegistrado(nomEdicion, nickAsistente)) {
			throw new UsuarioYaRegistradoEnEdicionException(nickAsistente, nomEdicion);
		}

		ManejadorEventos manejador = ManejadorEventos.getInstance();
		Edicion edicion = manejador.obtenerEdicion(nomEdicion);
		TipoDeRegistro tipo = edicion.getTipoDeRegistro(nomTipoRegistro);
		ManejadorUsuario manUs = ManejadorUsuario.getinstance();
		Usuario usuario = manUs.obtenerUsuario(nickAsistente);
		Asistente asist = (Asistente) usuario;
		Registro reg = tipo.altaRegistro(fechaRegistro, costo, nickAsistente);
		asist.agregarRegistro(reg);

	}
	
	public void altaRegistro(String nomEdicion, String nickAsistente, String nomTipoRegistro, DTFecha fechaRegistro, double costo, boolean patrocinado)
			throws UsuarioYaRegistradoEnEdicionException, UsuarioNoExisteException{


		if (estaRegistrado(nomEdicion, nickAsistente)) {
			throw new UsuarioYaRegistradoEnEdicionException(nickAsistente, nomEdicion);
		}

		ManejadorEventos manejador = ManejadorEventos.getInstance();
		Edicion edicion = manejador.obtenerEdicion(nomEdicion);
		TipoDeRegistro tipo = edicion.getTipoDeRegistro(nomTipoRegistro);
		ManejadorUsuario manUs = ManejadorUsuario.getinstance();
		Usuario usuario = manUs.obtenerUsuario(nickAsistente);
		Asistente asist = (Asistente) usuario;
		Registro reg = tipo.altaRegistro(fechaRegistro, costo, nickAsistente, patrocinado);
		asist.agregarRegistro(reg);

	}
	
	// Nuevo método para registrar con código de patrocinio
	public void altaRegistroConPatrocinio(String nomEdicion, String nickAsistente, String nomTipoRegistro, DTFecha fechaRegistro, String codigoPatrocinio)
			throws UsuarioYaRegistradoEnEdicionException, UsuarioNoExisteException{

		if (estaRegistrado(nomEdicion, nickAsistente)) {
			throw new UsuarioYaRegistradoEnEdicionException(nickAsistente, nomEdicion);
		}

		ManejadorEventos manejador = ManejadorEventos.getInstance();
		Edicion edicion = manejador.obtenerEdicion(nomEdicion);
		
		// Buscar el patrocinio por código
		Patrocinio patrocinio = edicion.buscarPatrocinioPorCodigo(codigoPatrocinio);
		if (patrocinio == null) {
			throw new UsuarioNoExisteException("Código de patrocinio no válido: " + codigoPatrocinio);
		}
		
		// Validar que haya usos disponibles
		if (patrocinio.getCantidadGratis() <= 0) {
			throw new UsuarioNoExisteException("El código de patrocinio ya no tiene usos disponibles");
		}
		
		// Validar que el patrocinio aplique al tipo de registro
		if (!patrocinio.getNombreTipoDeRegistro().equals(nomTipoRegistro)) {
			throw new UsuarioNoExisteException("El código de patrocinio no aplica a este tipo de registro");
		}
		
		// Crear el registro con costo 0 y marcado como patrocinado
		TipoDeRegistro tipo = edicion.getTipoDeRegistro(nomTipoRegistro);
		ManejadorUsuario manUs = ManejadorUsuario.getinstance();
		Usuario usuario = manUs.obtenerUsuario(nickAsistente);
		Asistente asist = (Asistente) usuario;
		Registro reg = tipo.altaRegistro(fechaRegistro, 0.0, nickAsistente, true);
		asist.agregarRegistro(reg);
		
		// Decrementar el contador de usos del patrocinio
		patrocinio.decrementarCantidadGratis();
	}
	
	
	

	public DTRegistro getRegistro(String nombreUsuario, String nombreTipoRegistro) {

	    Usuario usu = ManejadorUsuario.getinstance().obtenerUsuario(nombreUsuario);
	    if (usu == null) {
	        throw new IllegalArgumentException("No existe el usuario: " + nombreUsuario);
	    }


	    if (!(usu instanceof Asistente)) {
	        throw new IllegalArgumentException("El usuario " + nombreUsuario + " no es un asistente.");
	    }

	    Asistente asist = (Asistente) usu;


	    for (String tr : asist.getNomsTipo()) {

	        if (tr != null && tr.equals(nombreTipoRegistro)) {
	            String asistente = asist.getNickname();
	            DTRegistro dreg = asist.getRegistro(nombreTipoRegistro);

	            String edicion = dreg.getNomEdicion();

	            DTFecha fecha = dreg.getFechaRegistro();

	            Double costo = dreg.getCosto();
	            
	            boolean asistio = dreg.isAsistio();
	            
	            boolean patrocinado = dreg.getPatrocinado();

	            return new DTRegistro(asistente, tr, fecha, costo, edicion, asistio, patrocinado);
	        }
	    }


	    return null;
	}

	@Override
	public Set<DTRegistro> listarRegistrosPorAsistente(String nickAsistente) throws UsuarioNoExisteException {
	    ManejadorUsuario manUs = ManejadorUsuario.getinstance();
	    Usuario usu = manUs.obtenerUsuario(nickAsistente);

	    if (usu instanceof Asistente asist) {
	        Set<DTRegistro> registros = new HashSet<>();
	        for (Registro reg : asist.getRegistros()) {
	            DTRegistro dtRegistro = new DTRegistro(
	                nickAsistente,
	                reg.getTipoDeRegistro().getNombre(),
	                reg.getFechaRegistro(),
	                reg.getCosto(),
	                reg.getTipoDeRegistro().getNombreEdicion(),
	                reg.getAsistio(),
	                reg.getPatrocinado()
	            );
	            registros.add(dtRegistro);
	        }
	        return registros;
	    } else {
	        throw new UsuarioNoExisteException("El usuario " + nickAsistente + " no es asistente");
	    }
	}

	@Override
	public void registrarAsistencia(String nickAsistente, String nomEdicion, String nomTipoRegistro) 
	        throws UsuarioNoExisteException {
	    System.out.println("=== DEBUG registrarAsistencia ===");
	    System.out.println("Asistente: " + nickAsistente);
	    System.out.println("Edición: " + nomEdicion);
	    System.out.println("Tipo Registro: " + nomTipoRegistro);
	    
	    ManejadorUsuario manUs = ManejadorUsuario.getinstance();
	    Usuario usu = manUs.obtenerUsuario(nickAsistente);

	    if (usu instanceof Asistente asist) {
	        System.out.println("Total registros del asistente: " + asist.getRegistros().size());
	        
	        // Debug: ver todos los registros
	        for (Registro r : asist.getRegistros()) {
	            System.out.println("  Registro: edicion=" + r.getNomEdicion() + 
	                             ", tipo=" + r.getTipoDeRegistro().getNombre() + 
	                             ", asistio=" + r.getAsistio());
	        }
	        
	        Registro registro = asist.obtenerRegistro(nomEdicion, nomTipoRegistro);
	        if (registro != null) {
	            System.out.println("Registro encontrado, marcando asistencia...");
	            registro.registrarAsistencia();
	            System.out.println("Asistencia marcada: " + registro.getAsistio());
	        } else {
	            System.out.println("ERROR: No se encontró el registro!");
	        }
	    } else {
	        throw new UsuarioNoExisteException("El usuario " + nickAsistente + " no es asistente");
	    }
	}
}

