package logica;

import java.util.HashMap;
import java.util.Map;

public class Institucion{

	private String nombre;
	private String descripcion;
	private String sitioweb;
	private String logo; // Nuevo campo para imagen del logo
	
	private Map<String, Patrocinio> patrocinios;

	public Institucion(String nom, String desc, String web) {
		this.nombre = nom;
		this.descripcion = desc;
		this.sitioweb = web;
		this.logo = null; // Por defecto sin logo
		this.patrocinios = new HashMap<>();
	}
	
	// Constructor con logo
	public Institucion(String nom, String desc, String web, String logo) {
		this.nombre = nom;
		this.descripcion = desc;
		this.sitioweb = web;
		this.logo = logo;
		this.patrocinios = new HashMap<>();
	}

	public String getNombre() {
		return nombre; }

	public String getDescripcion() {
		return this.descripcion;
	}
	
	public String getSitioWeb() {
		return this.sitioweb;
	}
	
	public String getLogo() {
		return this.logo;
	}
	
	public void setLogo(String logo) {
		this.logo = logo;
	}
	
	public void agregarPatrocinio(Patrocinio pat) {
		patrocinios.put(pat.getCodigo(), pat);
	}




}
