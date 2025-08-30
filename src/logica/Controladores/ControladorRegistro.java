package logica.Controladores;

import java.util.HashSet;
import java.util.Set;

import excepciones.UsuarioNoExisteException;
import logica.Asistente;
import logica.Edicion;
import logica.Registro;
import logica.DatatypesYEnum.DTTipoRegistro;
import logica.manejadores.ManejadorEventos;
import logica.manejadores.ManejadorUsuario;
import logica.TipoDeRegistro;
import logica.Usuario;
import logica.TipoDeRegistro;
import logica.DatatypesYEnum.DTFecha;
import logica.DatatypesYEnum.DTTipoDeRegistro;
import logica.manejadores.ManejadorEventos;

public class ControladorRegistro implements IControladorRegistro {



	private static ControladorRegistro instancia = null;

	public ControladorRegistro() {

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

	public void altaTipoDeRegistro(String nombreEd, String nombreTipo,String descripcion, double costo, int cupo) {
		ManejadorEventos manejador = ManejadorEventos.getInstance();
		Edicion ed =manejador.obtenerEdicion(nombreEd);
		TipoDeRegistro tipo = new TipoDeRegistro(nombreTipo,descripcion,costo,cupo, ed);
		ed.agregarTipoDeRegistro(tipo, nombreTipo);
	}

	public Set<String> listarTipoRegistro(String nombreEd) {
		ManejadorEventos manejador = ManejadorEventos.getInstance();
		Edicion ed = manejador.obtenerEdicion(nombreEd);
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

	public void altaRegistro(String nomEdicion, String nickAsistente, String nomTipoRegistro,DTFecha fechaRegistro, double costo) {
		ManejadorEventos manejador = ManejadorEventos.getInstance();
		Edicion ed = manejador.obtenerEdicion(nomEdicion);
		TipoDeRegistro tp = ed.getTipoDeRegistro(nomTipoRegistro);
		ManejadorUsuario mu = ManejadorUsuario.getinstance();
		Usuario us = mu.obtenerUsuario(nickAsistente);
		Asistente as = (Asistente) us;
		Registro reg = tp.altaRegistro(fechaRegistro, costo, nickAsistente);
		as.agregarRegistro(reg);
	}
}

