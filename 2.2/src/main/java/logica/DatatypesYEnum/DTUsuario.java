package logica.DatatypesYEnum;

public class DTUsuario {

	private String nickname;
	private String nombre;
	private String correo;
	private String password; // Nuevo atributo
	private String avatar; // Nuevo atributo

	public DTUsuario(String nickname, String nombre, String correo, String password, String avatar) {

		this.correo = correo;
		this.nickname = nickname;
		this.nombre = nombre;
		this.password = password; // Asignar el nuevo atributo
		this.avatar = avatar; // Asignar el nuevo atributo
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

    public String getPassword() {
        return password; // Getter para password
    }

    public String getAvatar() {
        return avatar; // Getter para avatar
    }
}