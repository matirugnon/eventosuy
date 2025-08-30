package logica.DatatypesYEnum;

public class DTAsistente extends DTUsuario{

	private String apellido;
    private DTFecha fechaNacimiento;
    private String institucion;

	public DTAsistente(String nickname, String nombre, String correo, String ap, DTFecha fechaNacimiento, String institucion ) {
		super(nickname, nombre, correo);

		apellido = ap;
		this.fechaNacimiento = fechaNacimiento;
		this.institucion =institucion;
	}

}
