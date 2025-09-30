package logica.Controladores;

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
import logica.DatatypesYEnum.DTAsistente;
import logica.DatatypesYEnum.DTFecha;
import logica.DatatypesYEnum.DTOrganizador;
import logica.DatatypesYEnum.DTUsuario;
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
    public void altaUsuario(Usuario u) {
        manejador.addUsuario(u);
    }

    //alta Asistente
    @Override
    public void altaAsistente(String nick, String nombre, String correo, String apellido, DTFecha fechanac, String institucion, String password)
            throws UsuarioRepetidoException, CorreoInvalidoException, FechaInvalidaException {

        if (!correo.contains("@")) {
            throw new CorreoInvalidoException(correo);
        }

        if (ExisteNickname(nick)) {
            throw new UsuarioRepetidoException("El usuario " + nick + " ya está registrado");
        }

        if (ExisteCorreo(correo)) {
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



        Usuario a = new Asistente(nick, nombre, correo, apellido, fechanac, institucion, password, null); // Avatar puede ser null
        altaUsuario(a);
    }

    //alta Organizador
    @Override
    public void altaOrganizador(String nick, String nombre, String correo, String descripcion, String link, String password)
            throws UsuarioRepetidoException, CorreoInvalidoException {

        if (!correo.contains("@")) {
            throw new CorreoInvalidoException(correo);
        }

        if (ExisteNickname(nick)) {
            throw new UsuarioRepetidoException("El usuario " + nick + " ya está registrado");
        }

        if (ExisteCorreo(correo)) {
            throw new UsuarioRepetidoException("El correo " + correo + " ya está registrado");
        }

        Usuario o = new Organizador(nick, nombre, correo, descripcion, link, password, null); // Avatar puede ser null
        altaUsuario(o);
    }



    //-------------------------NUEVOS METODOS DEL CONTROLADOR---------------------------------------------------------------


    public boolean ExisteNickname(String nick) {

    	 ManejadorUsuario mu = ManejadorUsuario.getinstance();
         Usuario u = mu.obtenerUsuario(nick); // Ya soporta nickname o correo

         if (u != null) {return true;}

		return false;
    }

    public boolean ExisteCorreo(String correo) {

    	Set<String> usuarios = listarUsuarios();

    	for(String nick : usuarios) {

    		Usuario u = manejador.obtenerUsuario(nick);
    		if (u.getCorreo().equals(correo)) {
				return true;
			}

    	}

		return false;

   }

    public void AsociarInstitucion(String nick, String nombreInstitucion) {

    }

    public Set<String> listarUsuarios() {
        Set<Usuario> usuarios = manejador.getUsuarios();
        Set<String> nicks = new HashSet<>();
        for (Usuario u : usuarios) {
            nicks.add(u.getNickname()); // o getNickname(), según lo que qusieras mostrar
        }
        return nicks;
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
    	Institucion ins = new Institucion(nombreInstitucion,descripcion,web);

    	if (existeInstitucion(nombreInstitucion)) {
            throw new ExisteInstitucionException();
        }


    	manejador.addInstitucion(ins);
    }

    public Set<String> listarInstituciones(){
    	return manejador.getNombreInstituciones();
    }


	public DTUsuario getDTUsuario(String identificador)
			throws UsuarioNoExisteException{

		Usuario u = manejador.obtenerUsuario(identificador); // Soporta nickname o correo

		if (u == null) {
			throw new UsuarioNoExisteException(identificador);
		}

		if (u instanceof Organizador) {
			Organizador o = (Organizador) u;
			return o.getDTOrganizador();
		}else {

			Asistente a = (Asistente) u;
			return a.getDTAsistente();
		}


	}

	public Set<String> obtenerRegistros(String identificador) {

		Asistente a = (Asistente) manejador.obtenerUsuario(identificador); // Soporta nickname o correo


		Set<String> registros = a.getNomsTipo();

		return registros;
	}


	public Set<String> listarEdicionesOrganizador(String identificador) {

		Usuario u = manejador.obtenerUsuario(identificador); // Soporta nickname o correo

		Organizador o = (Organizador) u;

		Set<String> ediciones = o.getNombresEdiciones();

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
	        Asistente a = (Asistente) usuario;
	        DTAsistente dtA = (DTAsistente) datosUsuario;

	        a.setApellido(dtA.getApellido());

	        if(!esFechaValida(dtA.getFechaNacimiento().getDia(), dtA.getFechaNacimiento().getMes(), dtA.getFechaNacimiento().getAnio())) {
	        	throw new FechaInvalidaException(dtA.getFechaNacimiento().getDia(), dtA.getFechaNacimiento().getMes(), dtA.getFechaNacimiento().getAnio());
	        }

	        a.setFechaNacimiento(dtA.getFechaNacimiento());
	    }
	    else if (usuario instanceof Organizador && datosUsuario instanceof DTOrganizador) {
	        Organizador o = (Organizador) usuario;
	        DTOrganizador dtO = (DTOrganizador) datosUsuario;
	        o.setDescripcion(dtO.getDescripcion());
	        o.setLink(dtO.getLink());
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
	    return (anio % 4 == 0 && anio % 100 != 0) || (anio % 400 == 0);
	}





	@Override
	public Usuario obtenerUsuario(String identificador) {
		// TODO Auto-generated method stub
		return null;
	}













}