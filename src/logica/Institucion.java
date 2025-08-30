package logica;

import java.util.HashMap;
import java.util.Map;

public class Institucion{

	private String nombre;
	private String descripcion;
	private String sitioweb;
	
	private Map<String,Patrocinio> patrocinios;

	public Institucion(String nom, String desc, String web) {
		this.nombre = nom;
		this.descripcion = desc;
		this.sitioweb = web;
		this.patrocinios = new HashMap<>();
	}

	public String getNombre() {
		return nombre;}

	public String getDescripcion() {
		return this.descripcion;
	}
	
	public String getSitioWeb() {
		return this.sitioweb;
	}
	
	public void agregarPatrocinio(Patrocinio pat) {
		patrocinios.put(pat.getCodigo(), pat);
	}




}
