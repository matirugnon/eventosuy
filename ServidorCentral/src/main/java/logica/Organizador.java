package logica;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import logica.datatypesyenum.DTOrganizador;
import logica.datatypesyenum.DTUsuario;

public class Organizador extends Usuario {

    private String descripcion;
    private String link;
    private Map<String, Edicion> ediciones;

    public Organizador(String nickname, String nombre, String correo, String descripcion, String link, String password, String avatar) {
        super(nickname, nombre, correo, password, avatar);
        this.descripcion = descripcion;
        this.link = link;
        this.ediciones = new HashMap<>();
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getLink() {
        return link;
    }

    @Override
    public String getTipo() {
        return "organizador";
    }

    public void setDescripcion(String desc) {
        this.descripcion = desc;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void agregarEdicion(Edicion edicion) {
        this.ediciones.put(edicion.getNombre(), edicion);
    }

    public Edicion obtenerEdicion(String nombre) {
        return this.ediciones.get(nombre);
    }

    public Set<Edicion> getEdiciones() {
        return new HashSet<>(ediciones.values());
    }

    public Set<String> getNombresEdiciones() {
        return ediciones.keySet();
    }

    public void removerEdicion(String nombre) {
        this.ediciones.remove(nombre);
    }

    public boolean tieneEdicion(String nombre) {
        return this.ediciones.containsKey(nombre);
    }

    public DTUsuario getDTOrganizador() {
        DTOrganizador dto = new DTOrganizador(
                getNickname(),
                getNombre(),
                getCorreo(),
                getPassword(),
                descripcion,
                link,
                getAvatar());
        dto.setSeguidores(new ArrayList<>(getSeguidores()));
        dto.setSeguidos(new ArrayList<>(getSeguidos()));
        return dto;
    }
}
