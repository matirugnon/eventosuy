package logica.DatatypesYEnum;

public class DTInstitucion {
    private String nombre;
    private String descripcion;
    private String sitioWeb;
    private String logo; // Nuevo campo para imagen del logo
    
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
}