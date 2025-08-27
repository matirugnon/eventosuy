package logica.Controladores;

import logica.manejadores.ManejadorEventos;
import logica.manejadores.ManejadorUsuario;

public class ControladorEvento implements IControladorEvento {

	private ManejadorEventos manejadorE;

	public ControladorEvento() {

    	//inicializo el manejador
    	this.manejadorE = ManejadorEventos.getinstance();
    }


}