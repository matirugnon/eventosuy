package logica;

/**
 * Representa a un usuario en el sistema con nombre, apellido y cédula de identidad.
 * @author TProg2017
 *
 */
public abstract class Usuario {

	 protected String nickname;
	 protected String nombre;
	 protected String correo;

    public Usuario(String nick, String nombre, String correo) {
        this.nickname = nick;
        this.nombre = nombre;
        this.correo = correo;
    }

    //getters
    public String getNickname() {	return nickname;	}
    public String getNombre() { return nombre; }
    public String getCorreo() { return correo; }

    public abstract String getTipo();

    //setters

    public void setNickname(String nick) {
        nickname = nick;
    }

    public void setNombre(String n) {
        nombre = n;
    }

    public void setCorreo(String corr) {
    	correo = corr;
    }

}
