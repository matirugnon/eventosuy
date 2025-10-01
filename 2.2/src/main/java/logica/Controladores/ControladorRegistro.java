package logica.Controladores;

import java.util.Set;
import java.util.HashSet;

import excepciones.EdicionNoExisteException;
import excepciones.NombreTipoRegistroDuplicadoException;
import excepciones.UsuarioNoExisteException;
import excepciones.UsuarioYaRegistradoEnEdicionException;
import logica.Asistente;
import logica.Edicion;
import logica.Registro;
import logica.manejadores.ManejadorEventos;
import logica.manejadores.ManejadorUsuario;
import logica.TipoDeRegistro;
import logica.Usuario;
import logica.DatatypesYEnum.DTFecha;
import logica.DatatypesYEnum.DTRegistro;
import logica.DatatypesYEnum.DTTipoDeRegistro;

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
	public boolean existeTipoDeRegistro(String nombreEd,String nombreTipo) {
		ManejadorEventos manejador = ManejadorEventos.getInstance();
		Edicion ed = manejador.obtenerEdicion(nombreEd);
	return ed.existeTipoDeRegistro(nombreTipo);
	}

	public void altaTipoDeRegistro(String nombreEd, String nombreTipo,String descripcion, double costo, int cupo) throws NombreTipoRegistroDuplicadoException {

		if (existeTipoDeRegistro(nombreEd, nombreTipo)) {
			 throw new NombreTipoRegistroDuplicadoException(nombreEd);
		}

		ManejadorEventos manejador = ManejadorEventos.getInstance();
		Edicion ed =manejador.obtenerEdicion(nombreEd);
		TipoDeRegistro tipo = new TipoDeRegistro(nombreTipo,descripcion,costo,cupo, ed);
		ed.agregarTipoDeRegistro(tipo, nombreTipo);
	}

	public Set<String> listarTipoRegistro(String nombreEd)
			throws EdicionNoExisteException{
		ManejadorEventos manejador = ManejadorEventos.getInstance();
		Edicion ed = manejador.obtenerEdicion(nombreEd);

		if (ed == null) {
			throw new EdicionNoExisteException(nombreEd);
		}

		return ed.getNombresTiposDeRegistro();
	}

	public DTTipoDeRegistro consultaTipoDeRegistro(String nombreEd, String nombreReg) {
		ManejadorEventos manejador = ManejadorEventos.getInstance();
		Edicion ed = manejador.obtenerEdicion(nombreEd);
		TipoDeRegistro reg = ed.getTipoDeRegistro(nombreReg);
		return reg.getDTTipoDeRegistro();
	}


	public Set<String> obtenerNomsTipoRegistro(String nickusuario) {
	    ManejadorUsuario mu = ManejadorUsuario.getinstance();
	    Usuario u = mu.obtenerUsuario(nickusuario);

	    // Caso 1: no existe
	    if (u == null) {
	        return null; // o Collections.emptySet() si preferís no usar null
	    }

	    // Caso 2: si es Asistente, casteamos y devolvemos sus tipos
	    if (u instanceof Asistente) {
	        Asistente a = (Asistente) u;
	        return a.getNomsTipo();
	    }

	    // Caso 3: existe pero no es Asistente
	    return null; // o Collections.emptySet()
	}

	public boolean estaRegistrado(String nomEdicion, String nickAsistente) throws UsuarioNoExisteException {
		ManejadorUsuario mu = ManejadorUsuario.getinstance();
		Usuario us = mu.obtenerUsuario(nickAsistente);
		if (us instanceof Asistente as) {
			for (Registro reg: as.getRegistros()) {
				if(reg.getTipoDeRegistro().getNombreEdicion().equals(nomEdicion)) {
					return true;
				}
			}
			return false;
		}
		else {
			 throw new UsuarioNoExisteException("El usuario " + nickAsistente + " no es asistente");
		}
	}

	public boolean alcanzoCupo(String nomEdicion, String nomTipoRegistro) {
		ManejadorEventos manejador = ManejadorEventos.getInstance();
		Edicion ed = manejador.obtenerEdicion(nomEdicion);
		TipoDeRegistro tp = ed.getTipoDeRegistro(nomTipoRegistro);
		return tp.alcanzoCupo();
	}

	public void altaRegistro(String nomEdicion, String nickAsistente, String nomTipoRegistro,DTFecha fechaRegistro, double costo)
			throws UsuarioYaRegistradoEnEdicionException, UsuarioNoExisteException{


		if(estaRegistrado(nomEdicion, nickAsistente)) {
			throw new UsuarioYaRegistradoEnEdicionException(nickAsistente, nomEdicion);
		}

		ManejadorEventos manejador = ManejadorEventos.getInstance();
		Edicion ed = manejador.obtenerEdicion(nomEdicion);
		TipoDeRegistro tp = ed.getTipoDeRegistro(nomTipoRegistro);
		ManejadorUsuario mu = ManejadorUsuario.getinstance();
		Usuario us = mu.obtenerUsuario(nickAsistente);
		Asistente as = (Asistente) us;
		Registro reg = tp.altaRegistro(fechaRegistro, costo, nickAsistente);
		as.agregarRegistro(reg);

	}

	public DTRegistro getRegistro(String nombreUsuario, String nombreTipoRegistro) {

	    Usuario u = ManejadorUsuario.getinstance().obtenerUsuario(nombreUsuario);
	    if (u == null) {
	        throw new IllegalArgumentException("No existe el usuario: " + nombreUsuario);
	    }


	    if (!(u instanceof Asistente)) {
	        throw new IllegalArgumentException("El usuario " + nombreUsuario + " no es un asistente.");
	    }

	    Asistente as = (Asistente) u;


	    for (String tr : as.getNomsTipo()) {

	        if (tr != null && tr.equals(nombreTipoRegistro)) {
	            String asistente = as.getNickname();
	            DTRegistro dreg = as.getRegistro(nombreTipoRegistro);

	            String edicion = dreg.getnomEdicion();

	            DTFecha fecha = dreg.getFechaRegistro();

	            Double costo = dreg.getCosto();

	            return new DTRegistro(asistente, tr, fecha, costo, edicion);
	        }
	    }


	    return null;
	}

	@Override
	public Set<DTRegistro> listarRegistrosPorAsistente(String nickAsistente) throws UsuarioNoExisteException {
	    ManejadorUsuario mu = ManejadorUsuario.getinstance();
	    Usuario us = mu.obtenerUsuario(nickAsistente);

	    if (us instanceof Asistente as) {
	        Set<DTRegistro> registros = new HashSet<>();
	        for (Registro reg : as.getRegistros()) {
	            DTRegistro dtRegistro = new DTRegistro(
	                nickAsistente,
	                reg.getTipoDeRegistro().getNombre(),
	                reg.getFechaRegistro(),
	                reg.getCosto(),
	                reg.getTipoDeRegistro().getNombreEdicion()
	            );
	            registros.add(dtRegistro);
	        }
	        return registros;
	    } else {
	        throw new UsuarioNoExisteException("El usuario " + nickAsistente + " no es asistente");
	    }
	}
}

