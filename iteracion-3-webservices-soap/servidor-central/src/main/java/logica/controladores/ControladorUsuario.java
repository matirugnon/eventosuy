package logica.controladores;

import java.util.HashSet;
import java.util.Set;

import excepciones.CorreoInvalidoException;
import excepciones.ExisteInstitucionException;
import excepciones.FechaInvalidaException;
import excepciones.UsuarioNoExisteException;
import excepciones.UsuarioRepetidoException;
import logica.Asistente;
import logica.Institucion;
import logica.Organizador;
import logica.Usuario;
import logica.datatypesyenum.DTAsistente;
import logica.datatypesyenum.DTFecha;
import logica.datatypesyenum.DTInstitucion;
import logica.datatypesyenum.DTOrganizador;
import logica.datatypesyenum.DTUsuario;
import logica.manejadores.ManejadorUsuario;

public class ControladorUsuario implements IControladorUsuario {

	private ManejadorUsuario manejador;
	private static ControladorUsuario instancia = null;

    private ControladorUsuario() {
    	this.manejador = ManejadorUsuario.getinstance();
    }


    public static ControladorUsuario getInstance() {
        if (instancia == null)
            instancia = new ControladorUsuario();
        return instancia;
    }


    //metodo para agregar usuario luego de creado
    public void altaUsuario(Usuario usu) {
        manejador.addUsuario(usu);
    }

    //alta Asistente
    @Override
    public void altaAsistente(String nick, String nombre, String correo, String apellido, DTFecha fechanac, String institucion, String password)
            throws UsuarioRepetidoException, CorreoInvalidoException, FechaInvalidaException {

        // Validación básica del correo
        if (!esCorreoValido(correo)) {
            throw new CorreoInvalidoException(correo);
        }

        if (existeNickname(nick)) {
            throw new UsuarioRepetidoException("El usuario " + nick + " ya está registrado");
        }

        if (existeCorreo(correo)) {
            throw new UsuarioRepetidoException("El correo " + correo + " ya está registrado");
        }

        if (!esFechaValida(fechanac.getDia(), fechanac.getMes(), fechanac.getAnio())) {
            throw new FechaInvalidaException(fechanac.getDia(), fechanac.getMes(), fechanac.getAnio());
        }

        DTFecha hoy = new DTFecha(java.time.LocalDate.now().getDayOfMonth(),
                java.time.LocalDate.now().getMonthValue(),
                java.time.LocalDate.now().getYear());

        if (fechanac.compareTo(hoy) > 0) {
        	throw new FechaInvalidaException("La fecha de nacimiento no puede ser futura.");
        }

        // Si no se especifica avatar, asignar imagen por defecto
        String avatarPorDefecto = "/img/usSinFoto.webp";

        Usuario asis = new Asistente(nick, nombre, correo, apellido, fechanac, institucion, password, avatarPorDefecto);
        altaUsuario(asis);
    }

    //alta Asistente con avatar
    @Override
    public void altaAsistente(String nick, String nombre, String correo, String apellido, DTFecha fechanac, String institucion, String password, String avatar)
            throws UsuarioRepetidoException, CorreoInvalidoException, FechaInvalidaException {

        // Validación básica del correo
        if (!esCorreoValido(correo)) {
            throw new CorreoInvalidoException(correo);
        }

        if (existeNickname(nick)) {
            throw new UsuarioRepetidoException("El usuario " + nick + " ya está registrado");
        }

        if (existeCorreo(correo)) {
            throw new UsuarioRepetidoException("El correo " + correo + " ya está registrado");
        }

        if (!esFechaValida(fechanac.getDia(), fechanac.getMes(), fechanac.getAnio())) {
            throw new FechaInvalidaException(fechanac.getDia(), fechanac.getMes(), fechanac.getAnio());
        }

        DTFecha hoy = new DTFecha(java.time.LocalDate.now().getDayOfMonth(),
                java.time.LocalDate.now().getMonthValue(),
                java.time.LocalDate.now().getYear());

        if (fechanac.compareTo(hoy) > 0) {
        	throw new FechaInvalidaException("La fecha de nacimiento no puede ser futura.");
        }
        if (avatar == null || avatar.trim().isEmpty()) {
            // Asignar imagen por defecto cuando no se proporciona avatar
            avatar = "/img/usSinFoto.webp";
        }

        Usuario asis = new Asistente(nick, nombre, correo, apellido, fechanac, institucion, password, avatar);
        altaUsuario(asis);
    }

    //alta Organizador
    @Override
    public void altaOrganizador(String nick, String nombre, String correo, String descripcion, String link, String password)
            throws UsuarioRepetidoException, CorreoInvalidoException {

        // Validación básica del correo
        if (!esCorreoValido(correo)) {
            throw new CorreoInvalidoException(correo);
        }

        if (existeNickname(nick)) {
            throw new UsuarioRepetidoException("El usuario " + nick + " ya está registrado");
        }

        if (existeCorreo(correo)) {
            throw new UsuarioRepetidoException("El correo " + correo + " ya está registrado");
        }
        // Si no se especifica avatar, asignar imagen por defecto
        String avatarPorDefecto = "/img/usSinFoto.webp";

        Usuario org = new Organizador(nick, nombre, correo, descripcion, link, password, avatarPorDefecto);
        altaUsuario(org);
    }

    //alta Organizador con avatar
    @Override
    public void altaOrganizador(String nick, String nombre, String correo, String descripcion, String link, String password, String avatar)
            throws UsuarioRepetidoException, CorreoInvalidoException {

        // Validación básica del correo
        if (!esCorreoValido(correo)) {
            throw new CorreoInvalidoException(correo);
        }

        if (existeNickname(nick)) {
            throw new UsuarioRepetidoException("El usuario " + nick + " ya está registrado");
        }

        if (existeCorreo(correo)) {
            throw new UsuarioRepetidoException("El correo " + correo + " ya está registrado");
        }
        if (avatar == null || avatar.trim().isEmpty()) {
            // Asignar imagen por defecto cuando no se proporciona avatar
            avatar = "/img/usSinFoto.webp";
        }

        Usuario org = new Organizador(nick, nombre, correo, descripcion, link, password, avatar);
        altaUsuario(org);
    }



    //-------------------------NUEVOS METODOS DEL CONTROLADOR---------------------------------------------------------------


    public boolean existeNickname(String nick) {

    	 ManejadorUsuario manUs = ManejadorUsuario.getinstance();
         Usuario usr = manUs.obtenerUsuario(nick); // Ya soporta nickname o correo

         if (usr != null) {return true; }

		return false;
    }

    public boolean existeCorreo(String correo) {

    	Set<String> usuarios = listarUsuarios();

    	for (String nick : usuarios) {

    		Usuario usr = manejador.obtenerUsuario(nick);
    		if (usr.getCorreo().equals(correo)) {
				return true;
			}

    	}

		return false;

   }

    public void asociarInstitucion(String nick, String nombreInstitucion) {

    }

    


    public Set<String> listarUsuarios() {
        Set<Usuario> usuarios = manejador.getUsuarios();
        
        Set<String> nicks = new HashSet<>();
        for (Usuario u : usuarios) {
            nicks.add(u.getNickname());
        }
        
        return nicks;
    }

    public Set<DTUsuario> listarUsuariosDT() {
        Set<Usuario> usuarios = manejador.getUsuarios();
        Set<DTUsuario> dtUsuarios = new HashSet<>();
        for (Usuario u : usuarios) {
            if (u instanceof Organizador) {
                dtUsuarios.add(((Organizador) u).getDTOrganizador());
            } else if (u instanceof Asistente) {
                dtUsuarios.add(((Asistente) u).getDTAsistente());
            }
        }
        return dtUsuarios;
    }

    public Set<String> listarOrganizadores(){
        return manejador.obtenerNicksOrganizadores();
    }

    public Set<String> listarAsistentes(){
    	return manejador.getNickAsistentes();
    }

    public boolean existeInstitucion(String nombre) {
        return manejador.existeInstitucion(nombre);
    }

    public void altaInstitucion(String nombreInstitucion, String descripcion, String web) throws ExisteInstitucionException {
    	Institucion ins = new Institucion(nombreInstitucion, descripcion, web);

    	if (existeInstitucion(nombreInstitucion)) {
            throw new ExisteInstitucionException();
        }

    	manejador.addInstitucion(ins);
    }
    
    public void altaInstitucion(String nombreInstitucion, String descripcion, String web, String logo) throws ExisteInstitucionException {
    	Institucion ins = new Institucion(nombreInstitucion, descripcion, web, logo);

    	if (existeInstitucion(nombreInstitucion)) {
            throw new ExisteInstitucionException();
        }

    	manejador.addInstitucion(ins);
    }

    public Set<String> listarInstituciones(){
    	return manejador.getNombreInstituciones();
    }
    
    public DTInstitucion getInstitucion(String nombreInstitucion) {
    	Institucion ins = manejador.obtenerInstitucion(nombreInstitucion);
    	if (ins == null) {
    		return null;
    	}
    	return new DTInstitucion(ins.getNombre(), ins.getDescripcion(), ins.getSitioWeb(), ins.getLogo());
    }


	public DTUsuario getDTUsuario(String identificador)
			throws UsuarioNoExisteException{

		Usuario usr = manejador.obtenerUsuario(identificador); // Soporta nickname o correo

		if (usr == null) {
			throw new UsuarioNoExisteException(identificador);
		}

		if (usr instanceof Organizador) {
			Organizador org = (Organizador) usr;
			return org.getDTOrganizador();
		}else {

			Asistente asis = (Asistente) usr;
			return asis.getDTAsistente();
		}


	}

	public Set<String> obtenerRegistros(String identificador) {

		Asistente asis = (Asistente) manejador.obtenerUsuario(identificador); // Soporta nickname o correo


		Set<String> registros = asis.getNomsTipo();

		return registros;
	}


	public Set<String> listarEdicionesOrganizador(String identificador) {

		Usuario usr = manejador.obtenerUsuario(identificador); // Soporta nickname o correo

		Organizador org = (Organizador) usr;

		Set<String> ediciones = org.getNombresEdiciones();

		return ediciones;
	}

	//-----------------------------------------------MODIFICAR USUARIO__________________________________________________


	public void modificarUsuario(String nick, DTUsuario datosUsuario)
			throws UsuarioNoExisteException, FechaInvalidaException {

	    Usuario usuario = manejador.obtenerUsuario(nick);

	    if (usuario == null) {
	        throw new UsuarioNoExisteException("No existe un usuario con nickname: " + nick);
	    }

	    // Actualizamos los campos editables
	    usuario.setNombre(datosUsuario.getNombre());
	    usuario.setAvatar(datosUsuario.getAvatar()); // Actualizar avatar

	    if (usuario instanceof Asistente && datosUsuario instanceof DTAsistente) {
	        Asistente asis = (Asistente) usuario;
	        DTAsistente dtA = (DTAsistente) datosUsuario;

	        asis.setApellido(dtA.getApellido());
            asis.setInstitucion(dtA.getInstitucion());

	        if (!esFechaValida(dtA.getFechaNacimiento().getDia(), dtA.getFechaNacimiento().getMes(), dtA.getFechaNacimiento().getAnio())) {
	        	throw new FechaInvalidaException(dtA.getFechaNacimiento().getDia(), dtA.getFechaNacimiento().getMes(), dtA.getFechaNacimiento().getAnio());
	        }

	        asis.setFechaNacimiento(dtA.getFechaNacimiento());
	    }else if (usuario instanceof Organizador && datosUsuario instanceof DTOrganizador) {
	        Organizador org = (Organizador) usuario;
	        DTOrganizador dtO = (DTOrganizador) datosUsuario;
	        org.setDescripcion(dtO.getDescripcion());
	        org.setLink(dtO.getLink());
	    }
	}




	//metodos auxiliares para validar fechas
	public boolean esFechaValida(int dia, int mes, int anio) {
	    if (mes < 1 || mes > 12) return false;
	    if (dia < 1 || anio < 1) return false;

	    // Días por mes
	    int[] diasPorMes = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

	    // Ajustar febrero para año bisiesto
	    if (esBisiesto(anio)) {
	        diasPorMes[1] = 29;
	    }

	    return dia <= diasPorMes[mes - 1];
	}

	private boolean esBisiesto(int anio) {
	    return anio % 4 == 0 && anio % 100 != 0 || anio % 400 == 0;
	}





	@Override
	public Usuario obtenerUsuario(String identificador) {
		return manejador.obtenerUsuario(identificador);
	}

	/**
	 * Valida si un correo electrónico tiene un formato básico válido
	 * @param correo El correo a validar
	 * @return true si el correo es válido, false en caso contrario
	 */
	private boolean esCorreoValido(String correo) {
		if (correo == null) {
			return false;
		}
		
		// Eliminar espacios en blanco
		correo = correo.trim();
		
		// Verificar que no esté vacío
		if (correo.isEmpty()) {
			return false;
		}
		
		// Patrón simple: letras, números, punto, guión bajo, más, guión + @ + dominio con punto + extensión
		String patron = "^[a-zA-Z0-9._+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
		
		return correo.matches(patron);
	}















    @Override
    public void seguirUsuario(String seguidor, String seguido) throws UsuarioNoExisteException {
        if (seguidor == null || seguido == null) {
            throw new IllegalArgumentException("Los usuarios no pueden ser nulos");
        }
        if (seguidor.equals(seguido)) {
            return;
        }

        Usuario usuarioSeguidor = manejador.obtenerUsuario(seguidor);
        if (usuarioSeguidor == null) {
            throw new UsuarioNoExisteException(seguidor);
        }
        Usuario usuarioSeguido = manejador.obtenerUsuario(seguido);
        if (usuarioSeguido == null) {
            throw new UsuarioNoExisteException(seguido);
        }

        if (usuarioSeguidor.agregarSeguido(seguido)) {
            usuarioSeguido.agregarSeguidor(seguidor);
        } else if (!usuarioSeguido.getSeguidores().contains(seguidor)) {
            usuarioSeguido.agregarSeguidor(seguidor);
        }
    }

    @Override
    public void dejarSeguirUsuario(String seguidor, String seguido) throws UsuarioNoExisteException {
        if (seguidor == null || seguido == null) {
            throw new IllegalArgumentException("Los usuarios no pueden ser nulos");
        }
        if (seguidor.equals(seguido)) {
            return;
        }

        Usuario usuarioSeguidor = manejador.obtenerUsuario(seguidor);
        if (usuarioSeguidor == null) {
            throw new UsuarioNoExisteException(seguidor);
        }
        Usuario usuarioSeguido = manejador.obtenerUsuario(seguido);
        if (usuarioSeguido == null) {
            throw new UsuarioNoExisteException(seguido);
        }

        if (usuarioSeguidor.eliminarSeguido(seguido)) {
            usuarioSeguido.eliminarSeguidor(seguidor);
        } else if (usuarioSeguido.getSeguidores().contains(seguidor)) {
            usuarioSeguido.eliminarSeguidor(seguidor);
        }
    }

    @Override
    public Set<String> obtenerSeguidores(String nickname) throws UsuarioNoExisteException {
        if (nickname == null) {
            throw new IllegalArgumentException("El nickname no puede ser nulo");
        }
        Usuario usuario = manejador.obtenerUsuario(nickname);
        if (usuario == null) {
            throw new UsuarioNoExisteException(nickname);
        }
        return new HashSet<>(usuario.getSeguidores());
    }

    @Override
    public Set<String> obtenerSeguidos(String nickname) throws UsuarioNoExisteException {
        if (nickname == null) {
            throw new IllegalArgumentException("El nickname no puede ser nulo");
        }
        Usuario usuario = manejador.obtenerUsuario(nickname);
        if (usuario == null) {
            throw new UsuarioNoExisteException(nickname);
        }
        return new HashSet<>(usuario.getSeguidos());
    }

    @Override
    public boolean esSeguidor(String seguidor, String seguido) throws UsuarioNoExisteException {
        if (seguidor == null || seguido == null) {
            throw new IllegalArgumentException("Los usuarios no pueden ser nulos");
        }
        Usuario usuarioSeguido = manejador.obtenerUsuario(seguido);
        if (usuarioSeguido == null) {
            throw new UsuarioNoExisteException(seguido);
        }
        return usuarioSeguido.getSeguidores().contains(seguidor);
    }

	public boolean validarCredenciales(String identificador, String password) {
		if (identificador == null || identificador.isEmpty() || password == null || password.isEmpty()) {
			return false;
		}
		Usuario usuario = manejador.obtenerUsuario(identificador);
		if (usuario == null) {
			return false;
		}
		return usuario.getPassword().equals(password);
	}

	public String obtenerAvatar(String identificador) throws UsuarioNoExisteException {
		Usuario usuario = manejador.obtenerUsuario(identificador);
		if (usuario == null) {
			throw new UsuarioNoExisteException(identificador);
		}
		return usuario.getAvatar();
	}
}
