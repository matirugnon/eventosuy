package logica.DatatypesYEnum;

public class DTOrganizador extends DTUsuario {

    private String descripcion;
    private String link;

    public DTOrganizador(String nickname, String nombre, String correo, String password, String descripcion, String link, String avatar) {
        super(nickname, nombre, correo, password, avatar); // Actualizado para incluir password
        this.descripcion = descripcion;
        this.link = link;
    }

    // Getters
    public String getDescripcion() {
        return descripcion;
    }

    public String getLink() {
        return link;
    }
}
