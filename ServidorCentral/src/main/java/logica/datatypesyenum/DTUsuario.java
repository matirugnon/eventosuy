package logica.datatypesyenum;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSeeAlso;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso({DTAsistente.class, DTOrganizador.class})
public class DTUsuario implements Serializable {
    private static final long serialVersionUID = 1L;

    private String nickname;
    private String nombre;
    private String correo;
    private String password;
    private String avatar;

    @XmlElementWrapper(name = "seguidores")
    @XmlElement(name = "seguidor")
    private List<String> seguidores;

    @XmlElementWrapper(name = "seguidos")
    @XmlElement(name = "seguido")
    private List<String> seguidos;

    public DTUsuario() {
        this.seguidores = new ArrayList<>();
        this.seguidos = new ArrayList<>();
    }

    public DTUsuario(String nickname, String nombre, String correo, String password, String avatar) {
        this(nickname, nombre, correo, password, avatar, new ArrayList<>(), new ArrayList<>());
    }

    public DTUsuario(String nickname, String nombre, String correo, String password, String avatar, List<String> seguidores, List<String> seguidos) {
        this.nickname = nickname;
        this.nombre = nombre;
        this.correo = correo;
        this.password = password;
        this.avatar = avatar;
        this.seguidores = seguidores != null ? new ArrayList<>(seguidores) : new ArrayList<>();
        this.seguidos = seguidos != null ? new ArrayList<>(seguidos) : new ArrayList<>();
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
        return password;
    }

    public String getAvatar() {
        return avatar;
    }

    public List<String> getSeguidores() {
        return new ArrayList<>(seguidores);
    }

    public List<String> getSeguidos() {
        return new ArrayList<>(seguidos);
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

    public void setSeguidores(List<String> seguidores) {
        this.seguidores = seguidores != null ? new ArrayList<>(seguidores) : new ArrayList<>();
    }

    public void setSeguidos(List<String> seguidos) {
        this.seguidos = seguidos != null ? new ArrayList<>(seguidos) : new ArrayList<>();
    }
}
