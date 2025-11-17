package logica.datatypesyenum;

public class DTTipoDeRegistro {
	private String nombre;
	private String descripcion;
	private double costo;
	private int cupo;
	
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
}
