package logica.datatypesyenum;

import java.io.Serializable;

public class DTTipoRegistro implements Serializable{
	private static final long serialVersionUID = 1L;

	
    private String nombre;
    private String descripcion;
    private float costo;
    private int cupo;

    public DTTipoRegistro() {}
    
    public DTTipoRegistro(String nom, String desc, float cost, int cup) {

        nombre = nom;
        descripcion = desc;
        costo = cost;
        cupo = cup;

    }

    public String getNombre() { return nombre; }
    public String getDescripcion() {return descripcion; }
    public float getCosto() { return costo; }
    public int getCupo() { return cupo; }

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public void setCosto(float costo) {
		this.costo = costo;
	}

	public void setCupo(int cupo) {
		this.cupo = cupo;
	} 
    
}