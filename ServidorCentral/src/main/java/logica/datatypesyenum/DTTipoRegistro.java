package logica.datatypesyenum;



public class DTTipoRegistro{

    private String nombre;
    private String descripcion;
    private float costo;
    private int cupo;

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
}