package logica;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import logica.datatypesyenum.DTUsuario;

/**
 * Representa a un usuario en el sistema con nombre, apellido y cedula de identidad.
 * Mantiene ademas la informacion sobre seguidores y seguidos dentro de la plataforma.
 */
public abstract class Usuario {

    private String nickname;
    private String nombre;
    private String correo;
    private String password;
    private String avatar;
    private final Set<String> seguidores;
    private final Set<String> seguidos;

    public Usuario(String nick, String nombre, String correo, String password, String avatar) {
        this.nickname = nick;
        this.nombre = nombre;
        this.correo = correo;
        this.password = password;
        this.avatar = avatar;
        this.seguidores = new HashSet<>();
        this.seguidos = new HashSet<>();
    }

    // getters básicos
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
        return password;
    }

    public String getAvatar() {
        return avatar;
    }

    public abstract String getTipo();

    public Set<String> getSeguidores() {
        return Collections.unmodifiableSet(seguidores);
    }

    public Set<String> getSeguidos() {
        return Collections.unmodifiableSet(seguidos);
    }

    // setters básicos
    public void setNombre(String nom) {
        nombre = nom;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    // relaciones de seguimiento
    public boolean agregarSeguidor(String nickSeguidor) {
        return seguidores.add(nickSeguidor);
    }

    public boolean eliminarSeguidor(String nickSeguidor) {
        return seguidores.remove(nickSeguidor);
    }

    public boolean agregarSeguido(String nickSeguido) {
        return seguidos.add(nickSeguido);
    }

    public boolean eliminarSeguido(String nickSeguido) {
        return seguidos.remove(nickSeguido);
    }

    public DTUsuario getDTUsuario() {
        return new DTUsuario(
                nickname,
                nombre,
                correo,
                password,
                avatar,
                new java.util.ArrayList<>(seguidores),
                new java.util.ArrayList<>(seguidos));
    }
}
