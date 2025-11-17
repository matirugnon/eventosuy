package logica.DatatypesYEnum;

public class DTUsuario {

	private String nickname;
	private String nombre;
	private String correo;

	public DTUsuario(String nickname, String nombre, String correo) {

		this.correo = correo;
		this.nickname = nickname;
		this.nombre = nombre;
	}

	public String getNickname() {
        return nickname;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCorreo() {
        return correo;
    }

}