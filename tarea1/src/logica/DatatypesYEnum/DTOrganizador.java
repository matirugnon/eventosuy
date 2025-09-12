package logica.DatatypesYEnum;

public class DTOrganizador extends DTUsuario {

    private String descripcion;
    private String link;

    public DTOrganizador(String nickname, String nombre, String correo, String descripcion, String link ) {
        super(nickname, nombre, correo); // inicializa los campos heredados
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
