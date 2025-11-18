package logica.datatypesyenum;

import java.io.Serializable;

public class DTPatrocinio implements Serializable{
	private static final long serialVersionUID = 1L;
	
    private DTFecha fechaAlta;
    private double monto;
    private String codigo;
    private NivelPatrocinio nivel;
    private String edicion;
    private String institucion;
    private int cantidadGratis;
    private String tipoDeRegistro;

    public DTPatrocinio() {}
    
    public DTPatrocinio(DTFecha alta, double mont, String cod, NivelPatrocinio niv, String edi, String ins, int cantGratis, String tipoDeRegistro) {

        fechaAlta = alta;
        monto = mont;
        codigo = cod;
        nivel = niv;
        edicion = edi;
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

	public void setFechaAlta(DTFecha fechaAlta) {
		this.fechaAlta = fechaAlta;
	}

	public void setMonto(double monto) {
		this.monto = monto;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public void setNivel(NivelPatrocinio nivel) {
		this.nivel = nivel;
	}

	public void setEdicion(String edicion) {
		this.edicion = edicion;
	}

	public void setInstitucion(String institucion) {
		this.institucion = institucion;
	}

	public void setCantidadGratis(int cantidadGratis) {
		this.cantidadGratis = cantidadGratis;
	}

	public void setTipoDeRegistro(String tipoDeRegistro) {
		this.tipoDeRegistro = tipoDeRegistro;
	}




}