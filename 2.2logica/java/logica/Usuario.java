package logica;

import logica.DatatypesYEnum.DTUsuario;

/**
 * Representa a un usuario en el sistema con nombre, apellido y cédula de identidad.
 * @author TProg2017
 *
 */
public abstract class Usuario {

	 private String nickname;
	 private String nombre;
	 private String correo;
     private String password;
     private String avatar; // Nuevo atributo

    public Usuario(String nick, String nombre, String correo, String password, String avatar) {
        this.nickname = nick;
        this.nombre = nombre;
        this.correo = correo;
        this.password = password;
        this.avatar = avatar; // Asignar el nuevo atributo
    }

    //getters
    public String getNickname() {	return nickname;	}
    public String getNombre() { return nombre; }
    public String getCorreo() { return correo; }
    public String getPassword() { return password; }
    public String getAvatar() {
        return avatar;
    }
    public abstract String getTipo();

    //setters



    public void setNombre(String nom) {
        nombre = nom;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public DTUsuario getDTUsuario() {
    	return new DTUsuario(nickname, nombre, correo, password, avatar); // Incluir avatar
    }



}
