package logica.Controladores;

import logica.Categoria;
import logica.manejadores.ManejadorEventos;
import logica.manejadores.ManejadorUsuario;

public class ControladorEvento implements IControladorEvento {

	private ManejadorEventos manejadorE;

	public ControladorEvento() {

    	//inicializo el manejador
    	this.manejadorE = ManejadorEventos.getinstance();
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