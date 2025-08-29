package logica.Controladores;

import java.util.Set;

import logica.DatatypesYEnum.*;

public interface IControladorRegistro {

    
    
    
    public Set<String> obtenerRegistros(String nickUsuario);


	public boolean existeTipoDeRegistro(String nombreEd,String nombreTipo);

	public void altaTipoDeRegistro(String nombreEd, String nombreTipo,String descripcion, double costo, int cupo);

	public Set<String> listarTipoRegistro(String nombreEd);

	public DTTipoDeRegistro consultaTipoDeRegistro(String nombreEd, String nombreReg);


}
