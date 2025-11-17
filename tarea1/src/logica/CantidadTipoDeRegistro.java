package logica;

public class CantidadTipoDeRegistro{

	private int cantRegistros;
	private TipoDeRegistro tipo;
	private Patrocinio patrocinio;
	
	public CantidadTipoDeRegistro(int cant, TipoDeRegistro t, Patrocinio pat) {
		cantRegistros = cant;
		tipo = t;
		patrocinio = pat;
		t.asociarcantidadDeTipoRegistro(this);
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
}
	
