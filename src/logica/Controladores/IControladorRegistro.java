package logica.Controladores;

import java.util.Set;

import excepciones.UsuarioNoExisteException;
import logica.DatatypesYEnum.*;

public interface IControladorRegistro {

    
    
    
    public Set<String> obtenerRegistros(String nickUsuario);


	public boolean existeTipoDeRegistro(String nombreEd,String nombreTipo);

	public void altaTipoDeRegistro(String nombreEd, String nombreTipo,String descripcion, double costo, int cupo);

	public Set<String> listarTipoRegistro(String nombreEd);

	public DTTipoDeRegistro consultaTipoDeRegistro(String nombreEd, String nombreReg);
	
	public boolean estaRegistrado(String nomEdicion, String nickAsistente) throws UsuarioNoExisteException;

	public boolean alcanzoCupo(String nomEdicion, String nomTipoRegistro);
	
	public void altaRegistro(String nomEdicion, String nickAsistente, String nomTipoRegistro,DTFecha fechaRegistro, double costo);

}
