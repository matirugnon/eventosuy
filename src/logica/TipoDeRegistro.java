package logica;

import java.util.Set;

import logica.DatatypesYEnum.DTTipoDeRegistro;

public class TipoDeRegistro{

	private String nombre;
	private String descripcion;
	private double costo;
	private int cupo;

	private CantidadTipoDeRegistro cantidadTipo;
	private Set<Registro> registros;

	public TipoDeRegistro(String nom, String desc, double cost, int c) {

		nombre = nom;
		descripcion = desc;
		costo = cost;
		cupo = c;

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

	public CantidadTipoDeRegistro getCantidadTipoDeRegistro() {
		return this.cantidadTipo;
	}

	public Set<Registro> getRegistros() {
		return this.registros;
	}

	public DTTipoDeRegistro getDTTipoDeRegistro() {
		return new DTTipoDeRegistro(this.nombre, this.descripcion, this.costo,this.cupo);
	}


}
