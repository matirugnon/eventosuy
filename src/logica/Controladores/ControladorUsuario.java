package logica.Controladores;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import excepciones.CorreoInvalidoException;
import excepciones.FechaInvalidaException;
import excepciones.UsuarioRepetidoException;
import logica.Asistente;
import logica.Institucion;
import logica.Organizador;
import logica.Usuario;
import logica.DatatypesYEnum.DTFecha;
import logica.DatatypesYEnum.DTUsuario;
import logica.manejadores.ManejadorUsuario;

public class ControladorUsuario implements IControladorUsuario {

	private ManejadorUsuario manejador;
	private static ControladorUsuario instancia = null;

    private ControladorUsuario() {

    	//inicializo el manejador
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
    public void altaAsistente(String nick, String nombre, String correo, String apellido, DTFecha fechanac, String institucion)
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



        Usuario a = new Asistente(nick, nombre, correo, apellido, fechanac, institucion);
        altaUsuario(a);
    }

    //alta Organizador
    public void altaOrganizador(String nick, String nombre, String correo, String descripcion, String link)
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

        Usuario o = new Organizador(nick, nombre, correo, descripcion, link);
        altaUsuario(o);
    }



    //-------------------------NUEVOS METODOS DEL CONTROLADOR---------------------------------------------------------------


    public boolean ExisteNickname(String nick) {

    	 ManejadorUsuario mu = ManejadorUsuario.getinstance();
         Usuario u = mu.obtenerUsuario(nick);

         if (u != null) {return true;}

		return false;
    }

    public boolean ExisteCorreo(String correo) {

    	ManejadorUsuario mu = ManejadorUsuario.getinstance();
    	Set<String> usuarios = listarUsuarios();

    	for(String nick : usuarios) {

    		Usuario u = mu.obtenerUsuario(nick);
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


    public void modificarUsuario(String nick, DTUsuario datosUsuario) {

    }


    public Set<String> listarOrganizadores(){
        ManejadorUsuario manejadorU = ManejadorUsuario.getinstance();
        return manejadorU.obtenerNicksOrganizadores();
    }

    public Set<String> listarAsistentes(){
    	ManejadorUsuario manejadorU = ManejadorUsuario.getinstance();
    	return manejadorU.getNickAsistentes();
    }

    public boolean existeInstitucion(String nomInstitucion) {
    	ManejadorUsuario manejadorU = ManejadorUsuario.getinstance();
    	return manejadorU.existeInstitucion(nomInstitucion);
    }

    public void altaInstitucion(String nombreInstitucion, String descripcion, String web) {
    	ManejadorUsuario manejadorU = ManejadorUsuario.getinstance();
    	Institucion ins = new Institucion(nombreInstitucion,descripcion,web);
    	manejadorU.addInstitucion(ins);
    }

    public Set<String> listarInstituciones(){
    	ManejadorUsuario manejadorU = ManejadorUsuario.getinstance();
    	return manejadorU.getNombreInstituciones();
    }


	public DTUsuario getDTUsuario(String nombreU) {

		ManejadorUsuario mu = ManejadorUsuario.getinstance();
		Usuario u = mu.obtenerUsuario(nombreU);

		if (u instanceof Organizador) {
			Organizador o = (Organizador) u;
			return o.getDTOrganizador();
		}else {

			Asistente a = (Asistente) u;
			return a.getDTAsistente();
		}


	}

	public Set<String> obtenerRegistros(String nombreAsistente) {

		ManejadorUsuario mu = ManejadorUsuario.getinstance();
		Asistente a = (Asistente) mu.obtenerUsuario(nombreAsistente);


		Set<String> registros = a.getNomsTipo();

		return registros;
	}


	public Set<String> listarEdiciones(String nombreOrganizador) {

		Usuario u = manejador.obtenerUsuario(nombreOrganizador);

		Organizador o = (Organizador) u;

		Set<String> ediciones = o.getNombresEdiciones();

		return ediciones;
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













}
