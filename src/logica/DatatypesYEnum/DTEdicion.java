package logica.DatatypesYEnum;

import logica.Edicion;

public class DTEdicion {
    private final String nombre;
    private final DTFecha fechaInicio;
    private final DTFecha fechaFin;
    private final DTFecha altaEdicion;
    private final String ciudad;
    private final String pais;

    public DTEdicion(String nombre,
                     DTFecha fechaInicio,
                     DTFecha fechaFin,
                     DTFecha altaEdicion,
                     String ciudad,
                     String pais) {
        this.nombre = nombre;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.altaEdicion = altaEdicion;
        this.ciudad = ciudad;
        this.pais = pais;
    }


    public DTEdicion(Edicion e) {
        this(e.getNombre(),
             e.getFechaInicio(),
             e.getFechaFin(),
             e.getAltaEdicion(),
             e.getCiudad(),
             e.getPais());
    }

    public String getNombre() { return nombre; }
    public DTFecha getFechaInicio() { return fechaInicio; }
    public DTFecha getFechaFin() { return fechaFin; }
    public DTFecha getAltaEdicion() { return altaEdicion; }
    public String getCiudad() { return ciudad; }
    public String getPais() { return pais; }
}