package logica.Controladores;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import logica.Categoria;
import logica.Edicion;
import logica.Evento;
import logica.manejadores.ManejadorEventos;
import logica.manejadores.ManejadorUsuario;

public class ControladorEvento implements IControladorEvento {

	private ManejadorEventos manejadorE;

	public ControladorEvento() {

    	//inicializo el manejador
    	this.manejadorE = ManejadorEventos.getinstance();
    }

	//listar
	public Set<String> listarEventos(){
		ManejadorEventos manejador = ManejadorEventos.getinstance();
		Map<String, Evento> eventos = manejador.getEventos();
		Set<String> listaEventos = eventos.keySet();
		return listaEventos;
	}
	
	public Set<String> listarEdiciones(){
		ManejadorEventos manejador = ManejadorEventos.getinstance();
		Map<String, Edicion> ediciones = manejador.getEdiciones();
		Set<String> listaEdiciones = ediciones.keySet();
		return listaEdiciones;
	}
	
	//altaCategoria
	public boolean existeCategoria(String cat) {
		ManejadorEventos manejador = ManejadorEventos.getinstance();
		return manejador.existeCategoria(cat);
	}
	
	public void altaCategoria (String cat){
		ManejadorEventos manejador = ManejadorEventos.getinstance();
		Categoria nueva = new Categoria(cat);
		manejador.addCategoria(nueva);
	}
	
		
}