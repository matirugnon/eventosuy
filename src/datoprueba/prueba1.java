package datoprueba;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.Set;

import javax.swing.JOptionPane;

import org.junit.jupiter.api.Test;

import excepciones.CorreoInvalidoException;
import excepciones.EventoRepetidoException;
import excepciones.FechaInvalidaException;
import excepciones.UsuarioRepetidoException;
import logica.Controladores.IControladorEvento;
import logica.Controladores.IControladorRegistro;
import logica.Controladores.IControladorUsuario;
import logica.DatatypesYEnum.DTAsistente;
import logica.DatatypesYEnum.DTFecha;
import logica.DatatypesYEnum.DTPatrocinio;
import logica.DatatypesYEnum.DTUsuario;
import logica.DatatypesYEnum.NivelPatrocinio;
import utils.Utils;
class prueba1 {

	private IControladorUsuario contrU = IControladorUsuario.getInstance();
    private IControladorEvento contE =  IControladorEvento.getInstance();
    private IControladorRegistro contR = IControladorRegistro.getInstance();
	@Test
	public void altaUsuarios() {
		
		try {
			Utils.cargarDatos(contrU,contE,contR);
		} catch (UsuarioRepetidoException | CorreoInvalidoException | EventoRepetidoException | FechaInvalidaException e1) {
			e1.printStackTrace();
		}
		Set<String> o = contrU.listarUsuarios();
		Set <String> esperado = new HashSet<>();
		esperado.add("atorres");esperado.add("msilva");esperado.add("sofirod");esperado.add("vale23");
		esperado.add("luciag");esperado.add("andrearod");esperado.add("AnaG");esperado.add("JaviL");esperado.add("MariR");
		esperado.add("SofiM");esperado.add("miseventos");esperado.add("techcorp");esperado.add("imm");esperado.add("udelar");esperado.add("mec");
		assertEquals(o,esperado);
		
		o = contrU.listarAsistentes();
		esperado.clear();
		esperado.add("atorres");esperado.add("msilva");esperado.add("sofirod");esperado.add("vale23");
		esperado.add("luciag");esperado.add("andrearod");esperado.add("AnaG");esperado.add("JaviL");esperado.add("MariR");
		esperado.add("SofiM");
		assertEquals(o,esperado);
		
		o = contrU.listarOrganizadores();
		esperado.clear();
		esperado.add("miseventos");esperado.add("techcorp");esperado.add("imm");esperado.add("udelar");esperado.add("mec");
		assertEquals(o,esperado);
		
		o = contrU.listarInstituciones();
		esperado.clear();
		esperado.add("Facultad de Ingeniería");esperado.add("ORT Uruguay");esperado.add("Universidad Católica del Uruguay");esperado.add("Antel");esperado.add("Agencia Nacional de Investigación e Innovación (ANII)");
		assertEquals(o,esperado);
		
		o = contrU.listarEdicionesOrganizador("imm");
		esperado.clear();
		esperado.add("Montevideo Rock 2025");esperado.add("Maratón de Montevideo 2025");esperado.add("Maratón de Montevideo 2024");esperado.add("Maratón de Montevideo 2022");
		assertEquals(o,esperado);
		
		o = contE.listarEdiciones("Maratón de Montevideo");
		esperado.clear();
		esperado.add("Maratón de Montevideo 2025");esperado.add("Maratón de Montevideo 2024");esperado.add("Maratón de Montevideo 2022");
		assertEquals(o,esperado);
		
		o = contE.listarPatrocinios("Tecnología Punta del Este 2026");
		esperado.clear();
		esperado.add("TECHFING");esperado.add("TECHANII");
		assertEquals(o,esperado);
		
		boolean existe = contrU.existeInstitucion("hola");
		boolean espera = false;
		assertEquals(existe,espera);
		
		DTUsuario DTPruebaus = contrU.getDTUsuario("atorres");
		DTAsistente DTAsis = (DTAsistente) DTPruebaus;
		DTAsistente DTAsisesp = new DTAsistente("atorres", "Ana","atorres@gmail.com","Torres", new DTFecha(12,5,1990), "Facultad de Ingeniería");
		assertEquals(DTAsis.getApellido(),DTAsisesp.getApellido());
		assertEquals(DTAsis.getNombre(),DTAsisesp.getNombre());
		assertEquals(DTAsis.getCorreo(),DTAsisesp.getCorreo());
		assertEquals(DTAsis.getInstitucion(),DTAsisesp.getInstitucion());
		
		
		DTPatrocinio DTPat = contE.consultarTipoPatrocinioEdicion("Tecnología Punta del Este 2026", "TECHFING");
		DTPatrocinio DTPates = new DTPatrocinio(new DTFecha(21,8,2025),20000,"TECHFING",NivelPatrocinio.Oro,"Tecnología Punta del Este 2026","Facultad de Ingeniería");
		assertEquals(DTPat.getCodigo(),DTPates.getCodigo());
		assertEquals(DTPat.getEdicion(),DTPates.getEdicion());
		assertEquals(DTPat.getInstitucion(),DTPates.getInstitucion());
		assertEquals(DTPat.getMonto(),DTPates.getMonto());
		assertEquals(DTPat.getNivel(),DTPates.getNivel());
		
		espera = true;
		boolean obtenido = contE.costoSuperaAporte("Tecnología Punta del Este 2026", "Facultad de Ingeniería", "Estudiante", 10000, 10);
		assertEquals(espera,obtenido);
		
		espera = false;
		obtenido = contE.costoSuperaAporte("Tecnología Punta del Este 2026", "Facultad de Ingeniería", "Estudiante", 10000, 1);
		assertEquals(espera,obtenido);
		}
	
	
	
	
}