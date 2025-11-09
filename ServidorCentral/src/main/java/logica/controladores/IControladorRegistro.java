package logica.controladores;

import java.util.Set;

import excepciones.EdicionNoExisteException;
import excepciones.NombreTipoRegistroDuplicadoException;
import excepciones.UsuarioNoExisteException;
import excepciones.UsuarioYaRegistradoEnEdicionException;
import logica.datatypesyenum.DTFecha;
import logica.datatypesyenum.DTRegistro;
import logica.datatypesyenum.DTTipoDeRegistro;


public interface IControladorRegistro {

	static IControladorRegistro getInstance() {
	    return ControladorRegistro.getInstance();
	}


	public boolean existeTipoDeRegistro(String nombreEd, String nombreTipo);

	public boolean altaTipoDeRegistro(String nombreEd, String nombreTipo, String descripcion, double costo, int cupo)
			throws NombreTipoRegistroDuplicadoException;

	public Set<String> listarTipoRegistro(String nombreEd)
			throws EdicionNoExisteException;

	public DTTipoDeRegistro consultaTipoDeRegistro(String nombreEd, String nombreReg);

	public boolean estaRegistrado(String nomEdicion, String nickAsistente) throws UsuarioNoExisteException;


	public boolean alcanzoCupo(String nomEdicion, String nomTipoRegistro);

	public void altaRegistro(String nomEdicion, String nickAsistente, String nomTipoRegistro, DTFecha fechaRegistro, double costo)
			throws UsuarioYaRegistradoEnEdicionException, UsuarioNoExisteException;

	public Set<String> obtenerNomsTipoRegistro(String nickusuario);

	public DTRegistro getRegistro(String usuario, String registro);

	// Método para listar registros por asistente
    public Set<DTRegistro> listarRegistrosPorAsistente(String nickname) throws UsuarioNoExisteException;
    
    // Método para registrar asistencia a edición
    public void registrarAsistencia(String nickAsistente, String nomEdicion, String nomTipoRegistro) 
    		throws UsuarioNoExisteException;
    
    public void altaRegistro(String nomEdicion, String nickAsistente, String nomTipoRegistro, DTFecha fechaRegistro, double costo, boolean patrocinado)
			throws UsuarioYaRegistradoEnEdicionException, UsuarioNoExisteException;
}
