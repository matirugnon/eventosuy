package logica;

public class CantidadTipoDeRegistro{

	private int cantRegistros;
	private TipoDeRegistro tipo;
	private Patrocinio patrocinio;
	
	public CantidadTipoDeRegistro(int cant, TipoDeRegistro tipoReg, Patrocinio pat) {
		cantRegistros = cant;
		tipo = tipoReg;
		patrocinio = pat;
		tipoReg.asociarcantidadDeTipoRegistro(this);
	}
	
	public int getCantRegistros() {
		return cantRegistros;
	}
	
	public TipoDeRegistro getTipoDeRegistro() {
		return tipo;
	}
	
	public String getNombreTipoDeRegistro() {
		return tipo.getNombre();
	}

	public Patrocinio getPatrocinio() {
		return patrocinio;
	}
	
	public int getCantidad() {
		return cantRegistros;
	}
	
	public void decrementar() {
		if (cantRegistros > 0) {
			cantRegistros--;
		}
	}
}
	
