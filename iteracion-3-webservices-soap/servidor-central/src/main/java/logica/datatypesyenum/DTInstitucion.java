package logica.datatypesyenum;

import java.io.Serializable;

public class DTInstitucion implements Serializable{
	private static final long serialVersionUID = 1L;

    private String nombre;
    private String descripcion;
    private String sitioWeb;
    private String logo; // Nuevo campo para imagen del logo
    
    public DTInstitucion() {}
    
    public DTInstitucion(String nombre, String descripcion, String sitioWeb, String logo) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.sitioWeb = sitioWeb;
        this.logo = logo;
    }
    
    // Constructor sin logo (para compatibilidad)
    public DTInstitucion(String nombre, String descripcion, String sitioWeb) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.sitioWeb = sitioWeb;
        this.logo = null;
    }
    
    // Getters
    public String getNombre() {
        return nombre;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    public String getSitioWeb() {
        return sitioWeb;
    }
    
    public String getLogo() {
        return logo;
    }
    
    // Método auxiliar para verificar si tiene logo
    public boolean tieneLogo() {
        return logo != null && !logo.trim().isEmpty();
    }

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public void setSitioWeb(String sitioWeb) {
		this.sitioWeb = sitioWeb;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}
    
}