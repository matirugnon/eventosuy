package logica.Controladores;

import logica.Edicion;
import logica.TipoDeRegistro;
import logica.manejadores.ManejadorEventos;

public class ControladorRegistro implements IControladorRegistro {
	
	
//Alta de Tipo de Registro
	public boolean existeTipoDeRegistro(String nombreEd,String nombreTipo) {
		ManejadorEventos manejador = ManejadorEventos.getinstance();
		Edicion ed = manejador.obtenerEdicion(nombreEd);
	return ed.existeTipoDeRegistro(nombreTipo);
	}
	
	public void altaTipoDeRegistro(String nombreEd, String nombreTipo,String descripcion, double costo, int cupo) {
		ManejadorEventos manejador = ManejadorEventos.getinstance();
		Edicion ed =manejador.obtenerEdicion(nombreEd);
		TipoDeRegistro tipo = new TipoDeRegistro(nombreTipo,descripcion,costo,cupo);
		ed.agregarTipoDeRegistro(tipo, nombreTipo);
	}
}