package logica.datatypesyenum;

public class DTAsistente extends DTUsuario {

    private String apellido;
    private DTFecha fechaNacimiento;
    private String institucion;

    public DTAsistente(String nickname, String nombre, String correo, String password, String apellido, DTFecha fechaNacimiento, String institucion, String avatar) {
        super(nickname, nombre, correo, password, avatar); // Actualizado para incluir avatar
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
