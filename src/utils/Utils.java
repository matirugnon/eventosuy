package utils;

import java.util.List;
import java.util.Set;

import excepciones.CorreoInvalidoException;
import excepciones.EventoRepetidoException;
import excepciones.UsuarioRepetidoException;
import logica.Controladores.ControladorEvento;
import logica.Controladores.ControladorRegistro;
import logica.Controladores.ControladorUsuario;
import logica.DatatypesYEnum.DTFecha;
import logica.DatatypesYEnum.NivelPatrocinio;

public class Utils {

public static void cargarDatos(ControladorUsuario ctrlUsuario, ControladorEvento ctrlEvento, ControladorRegistro ctrlRegistro  )
		throws UsuarioRepetidoException, CorreoInvalidoException, EventoRepetidoException{

		// Instituciones
		ctrlUsuario.altaInstitucion("Facultad de Ingeniería", "Facultad de Ingeniería de la Universidad de la República", "https://www.fing.edu.uy");
		ctrlUsuario.altaInstitucion("ORT Uruguay", "Universidad privada enfocada en tecnología y gestión", "https://ort.edu.uy");
		ctrlUsuario.altaInstitucion("Universidad Católica del Uruguay", "Institución de educación superior privada", "https://ucu.edu.uy");
		ctrlUsuario.altaInstitucion("Antel", "Empresa estatal de telecomunicaciones", "https://antel.com.uy");
		ctrlUsuario.altaInstitucion("Agencia Nacional de Investigación e Innovación (ANII)", "Fomenta la investigación y la innovación en Uruguay", "https://anii.org.uy");

		// Usuarios

		ctrlUsuario.altaAsistente("atorres", "Ana", "atorres@gmail.com", "Torres", new DTFecha(12,5,1990), "Facultad de Ingeniería");
		ctrlUsuario.altaAsistente("msilva", "Martin", "martin.silva@fing.edu.uy", "Silva", new DTFecha(21,8,1987), "Facultad de Ingeniería");
		ctrlUsuario.altaAsistente("sofirod", "Sofia", "srodriguez@outlook.com", "Rodriguez", new DTFecha(3,2,1995), "Universidad Católica del Uruguay");
		ctrlUsuario.altaAsistente("vale23", "Valentina", "valentina.costa@mail.com", "Costa", new DTFecha(1,12,1992), "");
		ctrlUsuario.altaAsistente("luciag", "Lucía", "lucia.garcia@mail.com", "García", new DTFecha(9,11,1993), "");
		ctrlUsuario.altaAsistente("andrearod", "Andrea", "andrea.rod@mail.com", "Rodríguez", new DTFecha(10,6,2000), "Agencia Nacional de Investigación e Innovación (ANII)");
		ctrlUsuario.altaAsistente("AnaG", "Ana", "ana.gomez@hotmail.com", "Gómez", new DTFecha(15,3,1998), "");
		ctrlUsuario.altaAsistente("JaviL", "Javier", "javier.lopez@outlook.com", "López", new DTFecha(22,7,1995), "");
		ctrlUsuario.altaAsistente("MariR", "María", "maria.rodriguez@gmail.com", "Rodríguez", new DTFecha(10,11,2000), "");
		ctrlUsuario.altaAsistente("SofiM", "Sofía", "sofia.martinez@yahoo.com", "Martínez", new DTFecha(5,2,1997), "");


		ctrlUsuario.altaOrganizador("miseventos", "MisEventos", "contacto@miseventos.com", "Empresa de organización de eventos.", "https://miseventos.com");
		ctrlUsuario.altaOrganizador("techcorp", "Corporación Tecnológica", "info@techcorp.com", "Empresa líder en tecnologías de la información.", "");
		ctrlUsuario.altaOrganizador("imm", "Intendencia de Montevideo", "contacto@imm.gub.uy", "Gobierno departamental de Montevideo.", "https://montevideo.gub.uy");
		ctrlUsuario.altaOrganizador("udelar", "Universidad de la República", "contacto@udelar.edu.uy", "Universidad pública de Uruguay.", "https://udelar.edu.uy");
		ctrlUsuario.altaOrganizador("mec", "Ministerio de Educación y Cultura", "mec@mec.gub.uy", "Institución pública promotora de cultura.", "https://mec.gub.uy");



		// Categorias
		ctrlEvento.altaCategoria("Tecnología");
		ctrlEvento.altaCategoria("Innovación");
		ctrlEvento.altaCategoria("Literatura");
		ctrlEvento.altaCategoria("Cultura");
		ctrlEvento.altaCategoria("Música");
		ctrlEvento.altaCategoria("Deporte");
		ctrlEvento.altaCategoria("Salud");
		ctrlEvento.altaCategoria("Entretenimiento");
		ctrlEvento.altaCategoria("Agro");
		ctrlEvento.altaCategoria("Negocios");
		ctrlEvento.altaCategoria("Moda");
		ctrlEvento.altaCategoria("Investigación");

		// Eventos
		ctrlEvento.darAltaEvento("Conferencia de Tecnología", "Evento sobre innovación tecnológica", new DTFecha(1,10,2025), "CONFTEC", Set.of("Tecnología", "Innovación"));
		ctrlEvento.darAltaEvento("Feria del Libro", "Encuentro anual de literatura", new DTFecha(1,2,2025), "FERLIB", Set.of("Literatura", "Cultura"));
		ctrlEvento.darAltaEvento("Montevideo Rock", "Festival de rock con artistas nacionales e internacionales", new DTFecha(15,3,2023), "MONROCK", Set.of("Cultura", "Música"));
		ctrlEvento.darAltaEvento("Maratón de Montevideo", "Competencia deportiva anual en la capital", new DTFecha(1,1,2022), "MARATON", Set.of("Deporte", "Salud"));
		ctrlEvento.darAltaEvento("Montevideo Comics", "Convención de historietas, cine y cultura geek", new DTFecha(10,4,2024), "COMICS", Set.of("Cultura", "Entretenimiento"));
		ctrlEvento.darAltaEvento("Expointer Uruguay", "Exposición internacional agropecuaria y ganadera", new DTFecha(12,12,2024), "EXPOAGRO", Set.of("Agro", "Negocios"));
		ctrlEvento.darAltaEvento("Montevideo Fashion Week", "Pasarela de moda uruguaya e internacional", new DTFecha(20,7,2025), "MFASHION", Set.of("Cultura", "Moda"));



		// Ediciones

		ctrlEvento.AltaEdicion("Montevideo Rock", "imm", "Montevideo Rock 2025", "MONROCK25", "Montevideo", "Uruguay",
                new DTFecha(2025,11,20), new DTFecha(22,11,2025), new DTFecha(12,3,2025));

		ctrlEvento.AltaEdicion("Maratón de Montevideo", "imm", "Maratón de Montevideo 2025", "MARATON25", "Montevideo", "Uruguay",
		                new DTFecha(2025,9,14), new DTFecha(14,9,2025), new DTFecha(5,2,2025));

		ctrlEvento.AltaEdicion("Maratón de Montevideo", "imm", "Maratón de Montevideo 2024", "MARATON24", "Montevideo", "Uruguay",
		                new DTFecha(14,9,2024), new DTFecha(14,9,2024), new DTFecha(21,4,2024));

		ctrlEvento.AltaEdicion("Maratón de Montevideo", "imm", "Maratón de Montevideo 2022", "MARATON22", "Montevideo", "Uruguay",
		                new DTFecha(14,9,2022), new DTFecha(14,9,2022), new DTFecha(21,5,2022));

		ctrlEvento.AltaEdicion("Montevideo Comics", "miseventos", "Montevideo Comics 2024", "COMICS24", "Montevideo", "Uruguay",
		                new DTFecha(18,7,2024), new DTFecha(21,7,2024), new DTFecha(20,6,2024));

		ctrlEvento.AltaEdicion("Montevideo Comics", "miseventos", "Montevideo Comics 2025", "COMICS25", "Montevideo", "Uruguay",
		                new DTFecha(4,8,2025), new DTFecha(6,8,2025), new DTFecha(4,7,2025));

		ctrlEvento.AltaEdicion("Expointer Uruguay", "miseventos", "Expointer Uruguay 2025", "EXPOAGRO25", "Durazno", "Uruguay",
		                new DTFecha(11,9,2025), new DTFecha(17,9,2025), new DTFecha(1,2,2025));

		ctrlEvento.AltaEdicion("Conferencia de Tecnología", "udelar", "Tecnología Punta del Este 2026", "CONFTECH26", "Punta del Este", "Uruguay",
		                new DTFecha(6,4,2026), new DTFecha(10,4,2026), new DTFecha(1,8,2025));

		ctrlEvento.AltaEdicion("Conferencia de Tecnología", "techcorp", "Mobile World Congress 2025", "MWC", "Barcelona", "España",
		                new DTFecha(12,12,2025), new DTFecha(15,12,2025), new DTFecha(21,8,2025));

		ctrlEvento.AltaEdicion("Conferencia de Tecnología", "techcorp", "Web Summit 2026", "WS26", "Lisboa", "Portugal",
						new DTFecha(13,1,2026), new DTFecha(1,2,2026), new DTFecha(4,6,2025));



		// TipoRegistro

		// Montevideo Rock 2025
		ctrlRegistro.altaTipoDeRegistro("Montevideo Rock 2025", "General", "Acceso general a Montevideo Rock (2 días)", 1500, 2000);
		ctrlRegistro.altaTipoDeRegistro("Montevideo Rock 2025", "VIP", "Incluye backstage + acceso preferencial", 4000, 200);

		// Maratón de Montevideo 2025
		ctrlRegistro.altaTipoDeRegistro("Maratón de Montevideo 2025", "Corredor 42K", "Inscripción a la maratón completa", 1200, 499);
		ctrlRegistro.altaTipoDeRegistro("Maratón de Montevideo 2025", "Corredor 21K", "Inscripción a la media maratón", 800, 700);
		ctrlRegistro.altaTipoDeRegistro("Maratón de Montevideo 2025", "Corredor 10K", "Inscripción a la carrera 10K", 500, 1000);

		// Maratón de Montevideo 2024
		ctrlRegistro.altaTipoDeRegistro("Maratón de Montevideo 2024", "Corredor 42K", "Inscripción a la maratón completa", 1000, 300);
		ctrlRegistro.altaTipoDeRegistro("Maratón de Montevideo 2024", "Corredor 21K", "Inscripción a la media maratón", 500, 500);

		// Maratón de Montevideo 2022
		ctrlRegistro.altaTipoDeRegistro("Maratón de Montevideo 2022", "Corredor 42K", "Inscripción a la maratón completa", 1100, 450);
		ctrlRegistro.altaTipoDeRegistro("Maratón de Montevideo 2022", "Corredor 21K", "Inscripción a la media maratón", 900, 750);
		ctrlRegistro.altaTipoDeRegistro("Maratón de Montevideo 2022", "Corredor 10K", "Inscripción a la carrera 10K", 650, 1400);

		// Montevideo Comics 2024
		ctrlRegistro.altaTipoDeRegistro("Montevideo Comics 2024", "General", "Entrada para los 4 días de Montevideo Comics", 600, 1500);
		ctrlRegistro.altaTipoDeRegistro("Montevideo Comics 2024", "Cosplayer", "Entrada especial con acreditación para concurso cosplay", 300, 50);

		// Montevideo Comics 2025
		ctrlRegistro.altaTipoDeRegistro("Montevideo Comics 2025", "General", "Entrada para los 4 días de Montevideo Comics", 800, 1000);
		ctrlRegistro.altaTipoDeRegistro("Montevideo Comics 2025", "Cosplayer", "Entrada especial con acreditación para concurso cosplay", 500, 100);

		// Expointer Uruguay 2025
		ctrlRegistro.altaTipoDeRegistro("Expointer Uruguay 2025", "General", "Acceso a la exposición agropecuaria", 300, 5000);
		ctrlRegistro.altaTipoDeRegistro("Expointer Uruguay 2025", "Empresarial", "Acceso para empresas + networking", 2000, 5);

		// Tecnología Punta del Este 2026
		ctrlRegistro.altaTipoDeRegistro("Tecnología Punta del Este 2026", "Full", "Acceso ilimitado + Cena de gala", 1800, 300);
		ctrlRegistro.altaTipoDeRegistro("Tecnología Punta del Este 2026", "General", "Acceso general", 1500, 500);
		ctrlRegistro.altaTipoDeRegistro("Tecnología Punta del Este 2026", "Estudiante", "Acceso para estudiantes", 1000, 50);

		// Mobile World Congress 2025
		ctrlRegistro.altaTipoDeRegistro("Mobile World Congress 2025", "Full", "Acceso ilimitado + Cena de gala", 750, 550);
		ctrlRegistro.altaTipoDeRegistro("Mobile World Congress 2025", "General", "Acceso general", 500, 400);
		ctrlRegistro.altaTipoDeRegistro("Mobile World Congress 2025", "Estudiante", "Acceso para estudiantes", 250, 400);

		// Web Summit 2026
		ctrlRegistro.altaTipoDeRegistro("Web Summit 2026", "Full", "Acceso ilimitado + Cena de gala", 900, 30);
		ctrlRegistro.altaTipoDeRegistro("Web Summit 2026", "General", "Acceso general", 650, 5);
		ctrlRegistro.altaTipoDeRegistro("Web Summit 2026", "Estudiante", "Acceso para estudiantes", 300, 1);


		//Patrocinios
		ctrlEvento.altaPatrocinio("Tecnología Punta del Este 2026", "Facultad de Ingeniería",NivelPatrocinio.Oro, 20000, "Estudiante", 4, "TECHFING", new DTFecha(21,8,2025));
		ctrlEvento.altaPatrocinio("Tecnología Punta del Este 2026", "Agencia Nacional de Investigación e Innovación (ANII)",NivelPatrocinio.Plata, 10000, "General", 1, "TECHANII", new DTFecha(20,8,2025));
		ctrlEvento.altaPatrocinio("Maratón de Montevideo 2025", "Antel",NivelPatrocinio.Platino, 25000, "Corredor 10K", 10, "CORREANTEL", new DTFecha(4,3,2025));
		ctrlEvento.altaPatrocinio("Expointer Uruguay 2025", "Universidad Católica del Uruguay",NivelPatrocinio.Bronce, 15000, "General", 10, "EXPOCAT", new DTFecha(5,5,2025));

		// Registros
		ctrlRegistro.altaRegistro("Montevideo Rock 2025", "sofirod", "VIP", new DTFecha(14,5,2025), 4000);
		ctrlRegistro.altaRegistro("Maratón de Montevideo 2024", "sofirod", "Corredor 21K", new DTFecha(30,7,2024), 500);
		ctrlRegistro.altaRegistro("Web Summit 2026", "andrearod", "Estudiante", new DTFecha(21,8,2025), 300);
		ctrlRegistro.altaRegistro("Maratón de Montevideo 2025", "sofirod", "Corredor 42K", new DTFecha(3,3,2025), 1200);
		ctrlRegistro.altaRegistro("Mobile World Congress 2025", "vale23", "Full", new DTFecha(22,8,2025), 750);
		ctrlRegistro.altaRegistro("Maratón de Montevideo 2025", "AnaG", "Corredor 10K", new DTFecha(9,4,2025), 500);
		ctrlRegistro.altaRegistro("Maratón de Montevideo 2025", "JaviL", "Corredor 21K", new DTFecha(10,4,2025), 800);
		ctrlRegistro.altaRegistro("Montevideo Comics 2025", "MariR", "Cosplayer", new DTFecha(3,8,2025), 500);
		ctrlRegistro.altaRegistro("Montevideo Comics 2024", "SofiM", "General", new DTFecha(16,7,2024), 600);

	}


}
