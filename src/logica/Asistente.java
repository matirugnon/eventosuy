package logica;

import java.util.HashSet;
import java.util.Set;

import logica.DatatypesYEnum.DTAsistente;
import logica.DatatypesYEnum.DTFecha;
import logica.DatatypesYEnum.DTRegistro;
import logica.DatatypesYEnum.DTUsuario;

public class Asistente extends Usuario {

	//atributos
    private String apellido;
    private DTFecha fechaNacimiento;
    private Set<Registro> registros;
    private String institucion;

    public Asistente(String nickname, String nombre, String apellido, String correo, DTFecha fechaNacimiento, String ins) {
        super(nickname, nombre, correo);
        this.apellido = apellido;
        this.fechaNacimiento = fechaNacimiento;
        this.registros = new HashSet<>();
        this.institucion = ins;
    }

    public String getApellido() { return apellido; }
    public DTFecha getFechaNacimiento() { return fechaNacimiento; }

    @Override
    public String getTipo() {
        return "Asistente";
    }

	public void setApellido(String text) {
		apellido = text;

	}

	public void setFechaNac(String text) {
		// TODO Auto-generated method stub

	}


	public void agregarRegistro(Registro reg) {
        this.registros.add(reg);
    }

	public String getInstitucion() {
		return institucion;
	}


	public Set<String> getNomsTipo() {
	    Set<String> nombresTipos = new HashSet<>();
	    for (Registro r : registros) {
	        if (r != null) {
	            String nombreTipo = r.getNomTipo();
	            if (nombreTipo != null) {
	                nombresTipos.add(nombreTipo);
	            }
	        }
	    }
	    return nombresTipos;
	}

	public DTUsuario getDTAsistente() {
		return new DTAsistente(nickname, nombre,correo, apellido, fechaNacimiento, institucion);
	}

	public DTRegistro getRegistro(String nomTipoReg) {
	    if (registros == null) {
	        return null;
	    }

	    for (Registro r : this.registros) {
	        String nombreTR = r.getTipoDeRegistro().getNombre();
	        if (nombreTR != null && nombreTR.equals(nomTipoReg)) {

	            String asistente = this.getNombre();
	            DTFecha fecha = r.getFechaRegistro();
	            Double costo = r.getCosto();

	            return new DTRegistro(asistente, nombreTR, fecha, costo);
	        }
	    }


	    return null;
	}

	public Set<Registro> getRegistros() {

		Set<Registro> resultado = new HashSet<>();

		for(Registro r: registros) {

			resultado.add(r);

		}

		return resultado;
	}





}
