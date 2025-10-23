package datoprueba;

import static org.junit.jupiter.api.Assertions.*;

import java.util.AbstractMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


import org.junit.jupiter.api.Test;

import excepciones.CategoriaNoSeleccionadaException;
import excepciones.CorreoInvalidoException;
import excepciones.EdicionNoExisteException;
import excepciones.EdicionSinPatrociniosException;
import excepciones.EventoNoExisteException;
import excepciones.EventoRepetidoException;
import excepciones.FechaInvalidaException;
import excepciones.PatrocinioDuplicadoException;
import excepciones.PatrocinioNoEncontradoException;
import excepciones.UsuarioNoExisteException;
import excepciones.UsuarioRepetidoException;
import logica.controladores.IControladorEvento;
import logica.controladores.IControladorRegistro;
import logica.controladores.IControladorUsuario;
import logica.datatypesyenum.DTAsistente;
import logica.datatypesyenum.DTEdicion;
import logica.datatypesyenum.DTEvento;
import logica.datatypesyenum.DTFecha;
import logica.datatypesyenum.DTInstitucion;
import logica.datatypesyenum.DTOrganizador;
import logica.datatypesyenum.DTPatrocinio;
import logica.datatypesyenum.DTRegistro;
import logica.datatypesyenum.DTSeleccionEvento;
import logica.datatypesyenum.DTTipoDeRegistro;
import logica.datatypesyenum.DTUsuario;
import logica.datatypesyenum.EstadoEdicion;
import logica.datatypesyenum.NivelPatrocinio;
import utils.Utils;
class prueba1 {

	private IControladorUsuario contrU = IControladorUsuario.getInstance();
    private IControladorEvento contE =  IControladorEvento.getInstance();
    private IControladorRegistro contR = IControladorRegistro.getInstance();

    @Test
	public void altaUsuarios() throws UsuarioRepetidoException, CorreoInvalidoException, EventoRepetidoException, FechaInvalidaException, excepciones.ExisteInstitucionException, excepciones.EdicionExistenteException, excepciones.FechasIncompatiblesException, excepciones.NombreTipoRegistroDuplicadoException, UsuarioNoExisteException, excepciones.UsuarioYaRegistradoEnEdicionException, CategoriaNoSeleccionadaException, PatrocinioDuplicadoException, EventoNoExisteException, EdicionNoExisteException, EdicionSinPatrociniosException, PatrocinioNoEncontradoException {


		Utils.cargarDatos(contrU,contE,contR);


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

		o = contE.listarEdiciones();
		esperado.clear();
		esperado.add( "Montevideo Rock 2025");esperado.add("Maratón de Montevideo 2025");esperado.add("Maratón de Montevideo 2024");esperado.add("Maratón de Montevideo 2022");
		esperado.add("Montevideo Comics 2024");esperado.add("Montevideo Comics 2025");esperado.add("Expointer Uruguay 2025");esperado.add("Tecnología Punta del Este 2026");
		esperado.add("Mobile World Congress 2025");esperado.add("Web Summit 2026");esperado.add("Montevideo Fashion Week 2026");
		assertEquals(o,esperado);

		o = contE.listarEventos();
		esperado.clear();
		esperado.add("Conferencia de Tecnología");esperado.add("Feria del Libro");esperado.add("Montevideo Rock");
		esperado.add("Maratón de Montevideo");esperado.add("Montevideo Comics");esperado.add("Expointer Uruguay");esperado.add("Montevideo Fashion Week");
		assertEquals(o,esperado);

		o = contE.listarPatrocinios("Tecnología Punta del Este 2026");
		esperado.clear();
		esperado.add("TECHFING");esperado.add("TECHANII");
		assertEquals(o,esperado);

		o = contR.listarTipoRegistro("Montevideo Rock 2025");
		esperado.clear();
		esperado.add("General");esperado.add("VIP");
		assertEquals(o,esperado);


		o = contrU.obtenerRegistros("JaviL");
		esperado.clear();
		esperado.add("Corredor 21K");
		assertEquals(o,esperado);

		o = contR.obtenerNomsTipoRegistro("MariR");
		esperado.clear();
		esperado.add("Cosplayer");
		assertEquals(o,esperado);


		boolean existe = contrU.existeInstitucion("hola");
		boolean espera = false;
		assertEquals(existe,espera);

		DTUsuario DTPruebaus = contrU.getDTUsuario("atorres");
		DTAsistente DTAsis = (DTAsistente) DTPruebaus;
		DTAsistente DTAsisesp = new DTAsistente("atorres", "Ana","atorres@gmail.com","123.torres","Torres", new DTFecha(12,5,1990), "Facultad de Ingeniería", null);
		assertEquals(DTAsis.getApellido(),DTAsisesp.getApellido());
		assertEquals(DTAsis.getNombre(),DTAsisesp.getNombre());
		assertEquals(DTAsis.getCorreo(),DTAsisesp.getCorreo());
		assertEquals(DTAsis.getInstitucion(),DTAsisesp.getInstitucion());
		assertEquals(DTAsis.getPassword(),DTAsisesp.getPassword());


		DTPatrocinio DTPat = contE.consultarTipoPatrocinioEdicion("Tecnología Punta del Este 2026", "TECHFING");
		DTPatrocinio DTPates = new DTPatrocinio(new DTFecha(21,8,2025),20000,"TECHFING",NivelPatrocinio.Oro,"Tecnología Punta del Este 2026","Facultad de Ingeniería",4,"Estudiante");
		assertEquals(DTPat.getCodigo(),DTPates.getCodigo());
		assertEquals(DTPat.getEdicion(),DTPates.getEdicion());
		assertEquals(DTPat.getInstitucion(),DTPates.getInstitucion());
		assertEquals(DTPat.getMonto(),DTPates.getMonto());
		assertEquals(DTPat.getNivel(),DTPates.getNivel());

		assertEquals(DTPat.getTipoDeRegistro(),DTPates.getTipoDeRegistro());
		assertEquals(DTPat.getCantidadGratis(),DTPates.getCantidadGratis());


		DTTipoDeRegistro DTTipo = contR.consultaTipoDeRegistro("Maratón de Montevideo 2024", "Corredor 42K");
		DTTipoDeRegistro DTTipoes = new DTTipoDeRegistro("Corredor 42K", "Inscripción a la maratón completa",1000,300);
		assertEquals(DTTipo.getCosto(),DTTipoes.getCosto());
		assertEquals(DTTipo.getCupo(),DTTipoes.getCupo());
		assertEquals(DTTipo.getDescripcion(),DTTipoes.getDescripcion());
		assertEquals(DTTipo.getNombre(),DTTipoes.getNombre());

		espera = true;
		boolean obtenido = contE.costoSuperaAporte("Tecnología Punta del Este 2026", "Facultad de Ingeniería", "Estudiante", 10000, 10);
		assertEquals(espera,obtenido);

		espera = false;
		obtenido = contE.costoSuperaAporte("Tecnología Punta del Este 2026", "Facultad de Ingeniería", "Estudiante", 10000, 1);
		assertEquals(espera,obtenido);

		DTRegistro DTRegObt = contR.getRegistro("SofiM","General");
		DTRegistro  DTReges = new DTRegistro("SofiM", "General", new DTFecha(16,7,2024), 600.0, "Montevideo Comics 2024");
		assertEquals(DTRegObt.getCosto(),DTReges.getCosto());
		assertEquals(DTRegObt.getnomEdicion(),DTReges.getnomEdicion());
		assertEquals(DTRegObt.getTipoDeRegistro(),DTReges.getTipoDeRegistro());
		assertEquals(DTRegObt.getAsistente(),DTReges.getAsistente());


		obtenido = contR.alcanzoCupo("Montevideo Rock 2025","VIP");
		espera = false;
		assertEquals(espera,obtenido);


		obtenido = contE.existePatrocinio("Tecnología Punta del Este 2026", "Universidad Católica del Uruguay");
		espera = false;
		assertEquals(espera,obtenido);

		obtenido = contE.existeCodigoPatrocinioEnEdicion("Maratón de Montevideo 2025", "CORREANTEL");
		espera = true;
		assertEquals(espera,obtenido);

		obtenido = contE.existeCategoria("salu");
		espera = false;
		assertEquals(espera,obtenido);


		DTSeleccionEvento DTSel = contE.seleccionarEvento("Montevideo Rock");
		Set<String> edEs = new HashSet<>();
		edEs.add("Montevideo Rock 2025");
		DTEvento DTEventes = new DTEvento("Montevideo Rock","MONROCK","Festival de rock con artistas nacionales e internacionales",new DTFecha(15,3,2023), edEs);
		DTSeleccionEvento DTSelesp = new DTSeleccionEvento(DTEventes,Set.of("Cultura", "Música"),edEs);
		assertEquals(DTSelesp.getCategorias(),DTSel.getCategorias());
		assertEquals(DTSelesp.getDescripcion(),DTSel.getDescripcion());
		assertEquals(DTSelesp.getEdiciones(),DTSel.getEdiciones());
		assertEquals(DTSelesp.getNombre(),DTSel.getNombre());
		assertEquals(DTSelesp.getSigla(),DTSel.getSigla());


		DTEdicion DTed = contE.consultarEdicion("Web Summit 2026");
		Set<String> patro = new HashSet<>();
		Set<String> tiporeg = new HashSet<>();
		Set<Map.Entry<String, String>> regEsperados = new HashSet<>();
		regEsperados.add(new AbstractMap.SimpleImmutableEntry<>("andrearod", "Estudiante"));
		tiporeg.add("Full");tiporeg.add("General");tiporeg.add("Estudiante");
		DTEdicion DTedEsp = new DTEdicion("Conferencia de Tecnología","Web Summit 2026", "WS26", new DTFecha(13,1,2026), new DTFecha(1,2,2026), new DTFecha(4,6,2025),  "Lisboa", "Portugal", "techcorp", tiporeg, regEsperados,patro,EstadoEdicion.ACEPTADA);
		assertEquals(DTed.getCiudad(),DTedEsp.getCiudad());
		assertEquals(DTed.getNombre(),DTedEsp.getNombre());
		assertEquals(DTed.getOrganizador(),DTedEsp.getOrganizador());
		assertEquals(DTed.getPais(),DTedEsp.getPais());
		assertEquals(DTed.getPatrocinios(),DTedEsp.getPatrocinios());
		assertEquals(DTed.getRegistros(),DTedEsp.getRegistros());
		assertEquals(DTed.getSigla(),DTedEsp.getSigla());
		assertEquals(DTed.getTiposDeRegistro(),DTedEsp.getTiposDeRegistro());


		DTOrganizador orgMod = new DTOrganizador("udelar","hola","hola@gmail.com","contrasenia","hola, buenas tarde","hola.edu.uy", null);
		contrU.modificarUsuario("udelar", orgMod);
		DTOrganizador obt = (DTOrganizador) contrU.getDTUsuario("udelar");
		assertEquals(obt.getNombre(),"hola");
		assertEquals(obt.getDescripcion(),"hola, buenas tarde");
		assertEquals(obt.getLink(),"hola.edu.uy");


		DTAsistente asMod = new DTAsistente("atorres", "chau",  "chau@gmail.com","contrasenia", "chaucha", new DTFecha(28,9,1891), "Facultad de Ingeniería", null);
		contrU.modificarUsuario("atorres", asMod);
		DTAsistente asObt = (DTAsistente) contrU.getDTUsuario("atorres");
		assertEquals(asObt.getNombre(),"chau");
		assertEquals(asObt.getApellido(),"chaucha");

		Set<DTEvento> dtEventos = contE.obtenerDTEventos();
		 DTEvento dtEvento = dtEventos.stream()
			        .filter(e -> e.getNombre().equals("Montevideo Rock"))
			        .findFirst()
			        .orElse(null);
		 assertNotNull(dtEvento, "Debe existir el evento Montevideo Rock");
		    assertEquals("MONROCK", dtEvento.getSigla());
		    assertEquals("Festival de rock con artistas nacionales e internacionales", dtEvento.getDescripcion());
		 
		    dtEvento = contE.obtenerEventoPorEdicion("Montevideo Rock 2025");
		    assertEquals("Montevideo Rock", dtEvento.getNombre());
		 
		    Set<String> categoriasObt = contE.listarCategorias();
		    Set<String> categoriasEsp = Set.of(
		    		"Tecnología", "Innovación", "Literatura", "Cultura", "Música",
		    	    "Deporte", "Salud", "Entretenimiento", "Agro", "Negocios",
		    	    "Moda", "Investigación"
		    	);
		    assertEquals(categoriasObt,categoriasEsp);
		    
		    o = contE.listarEdicionesPorEstado(EstadoEdicion.RECHAZADA);
		    esperado = Set.of("Maratón de Montevideo 2022");
		    assertEquals(o,esperado);
		    
		    contrU.altaAsistente("prueba1", "prueba", "prueba@gmail.com", "prueba", new DTFecha(28,9,1891), "Facultad de Ingeniería", "contrasenia");
		    asObt = (DTAsistente) contrU.getDTUsuario("prueba1");
		    assertEquals(asObt.getNombre(),"prueba");
		    
		    contrU.altaOrganizador("prueba2", "prueba", "prueba2@gmail.com", "prueba", "hola.com.uy", "contrasenia");
		    obt = (DTOrganizador) contrU.getDTUsuario("prueba2");
		    assertEquals(asObt.getNombre(),"prueba");
		    
		    DTInstitucion dtInst = contrU.getInstitucion("Facultad de Ingeniería");
		    DTInstitucion dtInstEsp= new DTInstitucion(
		        "Facultad de Ingeniería", 
		        "Facultad de Ingeniería de la Universidad de la República",
		        "https://www.fing.edu.uy"
		    );
		    assertEquals(dtInstEsp.getNombre(), dtInst.getNombre());
		    assertEquals(dtInstEsp.getDescripcion(), dtInst.getDescripcion());
		    assertEquals(dtInstEsp.getSitioWeb(), dtInst.getSitioWeb());
		    
		    
		    
		    contrU.altaInstitucion("pruebaInst", "prueba", "hola.com.es");
		    dtInst = contrU.getInstitucion("pruebaInst");
		   dtInstEsp= new DTInstitucion(
				   "pruebaInst", 
				   "prueba",
				   "hola.com.es"
			    );
			    assertEquals(dtInstEsp.getNombre(), dtInst.getNombre());
			    
			    Set<DTEdicion> dtEd = contE.listarEdicionesOrganizadasPorEstado("miseventos", EstadoEdicion.INGRESADA);    
			    Set<String> nombresEsperados = Set.of("Expointer Uruguay 2025");
			    Set<String> nombresObtenidos = new HashSet<>();
			    for (DTEdicion ed : dtEd) {
			        nombresObtenidos.add(ed.getNombre());
			    }
			    assertEquals(nombresEsperados, nombresObtenidos);
    
			    o = contE.listarEdicionesPorEstadoDeEvento("Expointer Uruguay", EstadoEdicion.INGRESADA);
			    esperado = Set.of("Expointer Uruguay 2025");
			    assertEquals(o,esperado);
    
			    
			    Set<DTUsuario> dtUsuarios = contrU.listarUsuariosDT();
    
			    Set<String> nickEsperados = Set.of(
			    		"vale23", "prueba1", "JaviL", "prueba2", "imm", "mec", "atorres",
			    	    "SofiM", "AnaG", "sofirod", "MariR", "udelar", "msilva",
			    	    "andrearod", "miseventos", "techcorp", "luciag"
			        );
			    Set<String> nickObtenidos = new HashSet<>();
			    for (DTUsuario dt : dtUsuarios) {
			        nickObtenidos.add(dt.getNickname());
			    }
			    assertEquals(nickEsperados, nickObtenidos);
			    
			   
			  
			    Set<DTRegistro> registrosObtenidos = contR.listarRegistrosPorAsistente("SofiM");
			    Set<String> tiposEsperados = Set.of("General");
			    Set<String> tiposObt = new HashSet<>();
			    for (DTRegistro dt : registrosObtenidos) {
			        tiposObt.add(dt.getTipoDeRegistro());
			    }
			    assertEquals(tiposEsperados, tiposObt);
			   
    }
}