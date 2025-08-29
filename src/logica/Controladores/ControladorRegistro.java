package logica.Controladores;

import java.util.Set;

import logica.Edicion;
import logica.DatatypesYEnum.DTTipoRegistro;
import logica.manejadores.ManejadorEventos;
import logica.TipoDeRegistro;

public class ControladorRegistro implements IControladorRegistro {
    
    public DTTipoRegistro consultarTipoRegistroEdicion(String nomEdicion, String nomTipoRegistro) {
        ManejadorEventos me = ManejadorEventos.getInstance();
        Edicion ed = me.obtenerEdicion(nomEdicion);
        if (ed == null) {
            throw new IllegalArgumentException("No existe la edición: " + nomEdicion);
        }

        Set<TipoDeRegistro> tipos = ed.getTiposRegistro();
        if (tipos == null || tipos.isEmpty()) {
            throw new IllegalArgumentException("La edición '" + nomEdicion + "' no tiene tipos de registro.");
        }

        for (TipoDeRegistro tr : tipos) {
            if (tr != null && nomTipoRegistro.equals(tr.getNombre())) {
                return new DTTipoRegistro(
                    tr.getNombre(),
                    tr.getDescripcion(),
                    tr.getCosto(),
                    tr.getCupo()                
                 
               
                );
            }
        }

        throw new IllegalArgumentException("No existe el tipo de registro '" + nomTipoRegistro + "' en la edición '" + nomEdicion + "'.");
    }


    
    
    
}


