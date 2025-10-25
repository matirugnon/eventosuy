package logica.datatypesyenum;

import java.io.Serializable;

public class DTUsuario implements Serializable {
	private static final long serialVersionUID = 1L;

	
	private String nickname;
	private String nombre;
	private String correo;
	private String password; // Nuevo atributo
	private String avatar; // Nuevo atributo

	public DTUsuario() {}
	
	public DTUsuario(String nickname, String nombre, String correo, String password, String avatar) {

		this.correo = correo;
		this.nickname = nickname;
		this.nombre = nombre;
		this.password = password;
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

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
    
    
}