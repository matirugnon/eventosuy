package logica;

import logica.DatatypesYEnum.DTFecha;

public class Asistente extends Usuario {
    private String apellido;
    private DTFecha fechaNacimiento;

    public Asistente(String nickname, String nombre, String apellido, String correo, DTFecha fechaNacimiento) {
        super(nickname, nombre, correo);
        this.apellido = apellido;
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getApellido() { return apellido; }
    public DTFecha getFechaNacimiento() { return fechaNacimiento; }

    @Override
    public String getTipo() {
        return "Asistente";
    }
}
