package logica;

public class Organizador extends Usuario {

    private String descripcion;
    private String link;

    public Organizador(String nickname, String nombre, String correo, String descripcion, String link) {
        super(nickname, nombre, correo);
        this.descripcion = descripcion;
        this.link = link;
    }

    public String getDescripcion() { return descripcion; }
    public String getLink() { return link; }

    @Override
    public String getTipo() {
        return "Organizador";
    }

	public void setDescripcion(String text) {
		// TODO Auto-generated method stub

	}

	public void setLink(String text) {
		// TODO Auto-generated method stub

	}
}
