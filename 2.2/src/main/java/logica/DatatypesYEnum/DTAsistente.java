package logica.DatatypesYEnum;

public class DTAsistente extends DTUsuario {

    private String apellido;
    private DTFecha fechaNacimiento;
    private String institucion;

    public DTAsistente(String nickname, String nombre, String correo, String password, String apellido, DTFecha fechaNacimiento, String institucion) {
        super(nickname, nombre, correo, password); // Actualizado para incluir password
        this.apellido = apellido;
        this.fechaNacimiento = fechaNacimiento;
        this.institucion = institucion;
    }

    // Getters
    public String getApellido() {
        return apellido;
    }

    public DTFecha getFechaNacimiento() {
        return fechaNacimiento;
    }

    public String getInstitucion() {
        return institucion;
    }
}
