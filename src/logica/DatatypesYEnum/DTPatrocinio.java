package logica.DatatypesYEnum;

import logica.DatatypesYEnum.DTFecha;
import logica.DatatypesYEnum.NivelPatrocinio;

public class DTPatrocinio{

    private DTFecha fechaAlta;
    private double monto;
    private String codigo;
    private NivelPatrocinio nivel;


    public DTPatrocinio(DTFecha alta, double m, String cod, NivelPatrocinio niv) {

        fechaAlta = alta;
        monto = m;
        codigo = cod;
        nivel = niv;
    }
    
    public DTFecha getFechaAlta() { return fechaAlta; }
    public double getMonto() { return monto; }
    public String getCodigo() { return codigo; }
    public NivelPatrocinio getNivel() { return nivel; }
    




}