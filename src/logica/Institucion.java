package logica;

public class Institucion{

	private String nombre;
	private String descripcion;
	private String sitioweb;

	public Institucion(String nom, String desc, String web) {
		this.nombre = nom;
		this.descripcion = desc;
		this.sitioweb = web;
	}

	public String getNombre() {
		return nombre;}

	public String getDescripcion() {
		return this.descripcion;
	}
	
	public String getSitioWeb() {
		return this.sitioweb;
	}






}
