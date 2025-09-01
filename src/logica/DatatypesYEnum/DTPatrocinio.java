package logica.DatatypesYEnum;

import logica.DatatypesYEnum.DTFecha;
import logica.DatatypesYEnum.NivelPatrocinio;

public class DTPatrocinio{

    private DTFecha fechaAlta;
    private double monto;
    private String codigo;
    private NivelPatrocinio nivel;
    private String edicion;
    private String institucion;
    private int cantidadGratis;
    private String tipoDeRegistro;


    public DTPatrocinio(DTFecha alta, double m, String cod, NivelPatrocinio niv,String ed, String ins, int cantGratis,String tipoDeRegistro) {

        fechaAlta = alta;
        monto = m;
        codigo = cod;
        nivel = niv;
        edicion = ed;
        institucion = ins;
        this.cantidadGratis = cantGratis;
        this.tipoDeRegistro = tipoDeRegistro;
    }

    public DTFecha getFechaAlta() { return fechaAlta; }
    public double getMonto() { return monto; }
    public String getCodigo() { return codigo; }
    public NivelPatrocinio getNivel() { return nivel; }

    public String getEdicion() {
    	return edicion;
    }
    public String getInstitucion() {
    	return institucion;
    }

    public int getCantidadGratis() {
    	return cantidadGratis;
    }

    public String getTipoDeRegistro() {
    	return tipoDeRegistro;
    }




}