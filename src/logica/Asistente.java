package logica;

import java.util.HashSet;
import java.util.Set;

import logica.DatatypesYEnum.DTFecha;

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

	public Set<Registro> getRegistros() {
		// TODO Auto-generated method stub
		return new HashSet<>(registros);
	}

	public void agregarRegistro(Registro reg) {
        this.registros.add(reg);
    }

	public String getInstitucion() {
		return institucion;
	}




}
