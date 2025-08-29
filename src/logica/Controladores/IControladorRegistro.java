package logica.Controladores;

import logica.DatatypesYEnum.*;

public interface IControladorRegistro {
    
    public DTTipoRegistro consultarTipoRegistroEdicion(String nomEdicion, String nomTipoRegistro);
    
    public Set<String> obtenerRegistros(String nickUsuario);

}
