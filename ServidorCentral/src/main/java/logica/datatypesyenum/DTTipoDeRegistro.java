package logica.datatypesyenum;

import java.io.Serializable;

public class DTTipoDeRegistro implements Serializable {
	private static final long serialVersionUID = 1L;

	
	private String nombre;
	private String descripcion;
	private double costo;
	private int cupo;
	
	public DTTipoDeRegistro() {}
	
	public DTTipoDeRegistro(String nom, String desc, double cost, int cup) {
		this.nombre = nom;
		this.descripcion = desc;
		this.costo = cost;
		this.cupo = cup;
	}
	
	public String getNombre() {
		return this.nombre;
	}
	
	public String getDescripcion() {
		return this.descripcion;
	}
	
	public double getCosto() {
		return this.costo;
	}
	
	public int getCupo() {
		return this.cupo;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public void setCosto(double costo) {
		this.costo = costo;
	}

	public void setCupo(int cupo) {
		this.cupo = cupo;
	}
	
	
}
