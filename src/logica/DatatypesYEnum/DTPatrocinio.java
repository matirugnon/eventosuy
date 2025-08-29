package logica.DatatypesYEnum;

import logica.DatatypesYEnum.DTFecha;
import logica.DatatypesYEnum.NivelPatrocinio;

public class DTPatrocinio{

    private DTFecha fechaAlta;
    private float monto;
    private int codigo;
    private NivelPatrocinio nivel;


    public DTPatrocinio(DTFecha alta, float m, int cod, NivelPatrocinio niv) {

        fechaAlta = alta;
        monto = m;
        codigo = cod;
        nivel = niv;
    }
    
    public DTFecha getFechaAlta() { return fechaAlta; }
    public float getMonto() { return monto; }
    public int getCodigo() { return codigo; }
    public NivelPatrocinio getNivel() { return nivel; }
    




}