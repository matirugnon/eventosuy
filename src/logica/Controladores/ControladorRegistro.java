package logica.Controladores;

import java.util.Set;

import logica.Edicion;

import logica.DatatypesYEnum.DTTipoRegistro;
import logica.manejadores.ManejadorEventos;
import logica.TipoDeRegistro;


import logica.TipoDeRegistro;
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
		TipoDeRegistro tipo = new TipoDeRegistro(nombreTipo,descripcion,costo,cupo);
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
}

