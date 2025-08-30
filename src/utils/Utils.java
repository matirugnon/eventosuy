package utils;

import java.util.List;
import java.util.Set;

import excepciones.UsuarioRepetidoException;
import logica.Controladores.ControladorEvento;
import logica.Controladores.ControladorRegistro;
import logica.Controladores.ControladorUsuario;
import logica.DatatypesYEnum.DTFecha;

public class Utils {

public static void cargarDatos(ControladorUsuario ctrlUsuario, ControladorEvento ctrlEvento, ControladorRegistro ctrlRegistro  ) throws UsuarioRepetidoException{

		// Instituciones
		ctrlUsuario.altaInstitucion("Facultad de Ingeniería", "Facultad de Ingeniería de la Universidad de la República", "https://www.fing.edu.uy");
		ctrlUsuario.altaInstitucion("ORT Uruguay", "Universidad privada enfocada en tecnología y gestión", "https://ort.edu.uy");
		ctrlUsuario.altaInstitucion("Universidad Católica del Uruguay", "Institución de educación superior privada", "https://ucu.edu.uy");
		ctrlUsuario.altaInstitucion("Antel", "Empresa estatal de telecomunicaciones", "https://antel.com.uy");
		ctrlUsuario.altaInstitucion("Agencia Nacional de Investigación e Innovación (ANII)", "Fomenta la investigación y la innovación en Uruguay", "https://anii.org.uy");

		// Usuarios

		ctrlUsuario.altaAsistente("atorres", "Ana", "atorres@gmail.com", "Torres", new DTFecha(1990,5,12), "Facultad de Ingeniería");
		ctrlUsuario.altaAsistente("msilva", "Martin", "martin.silva@fing.edu.uy", "Silva", new DTFecha(1987,8,21), "Facultad de Ingeniería");
		ctrlUsuario.altaAsistente("sofirod", "Sofia", "srodriguez@outlook.com", "Rodriguez", new DTFecha(1995,2,3), "Universidad Católica del Uruguay");
		ctrlUsuario.altaAsistente("vale23", "Valentina", "valentina.costa@mail.com", "Costa", new DTFecha(1992,12,1), "");
		ctrlUsuario.altaAsistente("luciag", "Lucía", "lucia.garcia@mail.com", "García", new DTFecha(1993,11,9), "");
		ctrlUsuario.altaAsistente("andrearod", "Andrea", "andrea.rod@mail.com", "Rodríguez", new DTFecha(2000,6,10), "Agencia Nacional de Investigación e Innovación (ANII)");
		ctrlUsuario.altaAsistente("AnaG", "Ana", "ana.gomez@hotmail.com", "Gómez", new DTFecha(1998,3,15), "");
		ctrlUsuario.altaAsistente("JaviL", "Javier", "javier.lopez@outlook.com", "López", new DTFecha(1995,7,22), "");
		ctrlUsuario.altaAsistente("MariR", "María", "maria.rodriguez@gmail.com", "Rodríguez", new DTFecha(2000,11,10), "");
		ctrlUsuario.altaAsistente("SofiM", "Sofía", "sofia.martinez@yahoo.com", "Martínez", new DTFecha(1997,2,5), "");


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
		ctrlEvento.darAltaEvento("Conferencia de Tecnología", "Evento sobre innovación tecnológica", new DTFecha(2025,1,10), "CONFTEC", Set.of("Tecnología", "Innovación"));
		ctrlEvento.darAltaEvento("Feria del Libro", "Encuentro anual de literatura", new DTFecha(2025,2,1), "FERLIB", Set.of("Literatura", "Cultura"));
		ctrlEvento.darAltaEvento("Montevideo Rock", "Festival de rock con artistas nacionales e internacionales", new DTFecha(2023,3,15), "MONROCK", Set.of("Cultura", "Música"));
		ctrlEvento.darAltaEvento("Maratón de Montevideo", "Competencia deportiva anual en la capital", new DTFecha(2022,1,1), "MARATON", Set.of("Deporte", "Salud"));
		ctrlEvento.darAltaEvento("Montevideo Comics", "Convención de historietas, cine y cultura geek", new DTFecha(2024,4,10), "COMICS", Set.of("Cultura", "Entretenimiento"));
		ctrlEvento.darAltaEvento("Expointer Uruguay", "Exposición internacional agropecuaria y ganadera", new DTFecha(2024,12,12), "EXPOAGRO", Set.of("Agro", "Negocios"));
		ctrlEvento.darAltaEvento("Montevideo Fashion Week", "Pasarela de moda uruguaya e internacional", new DTFecha(2025,7,20), "MFASHION", Set.of("Cultura", "Moda"));



		// Ediciones

		ctrlEvento.AltaEdicion("Montevideo Rock", "imm", "Montevideo Rock 2025", "MONROCK25", "Montevideo", "Uruguay",
                new DTFecha(2025,11,20), new DTFecha(2025,11,22), new DTFecha(2025,3,12));

		ctrlEvento.AltaEdicion("Maratón de Montevideo", "imm", "Maratón de Montevideo 2025", "MARATON25", "Montevideo", "Uruguay",
		                new DTFecha(2025,9,14), new DTFecha(2025,9,14), new DTFecha(2025,2,5));

		ctrlEvento.AltaEdicion("Maratón de Montevideo", "imm", "Maratón de Montevideo 2024", "MARATON24", "Montevideo", "Uruguay",
		                new DTFecha(2024,9,14), new DTFecha(2024,9,14), new DTFecha(2024,4,21));

		ctrlEvento.AltaEdicion("Maratón de Montevideo", "imm", "Maratón de Montevideo 2022", "MARATON22", "Montevideo", "Uruguay",
		                new DTFecha(2022,9,14), new DTFecha(2022,9,14), new DTFecha(2022,5,21));

		ctrlEvento.AltaEdicion("Montevideo Comics", "miseventos", "Montevideo Comics 2024", "COMICS24", "Montevideo", "Uruguay",
		                new DTFecha(2024,7,18), new DTFecha(2024,7,21), new DTFecha(2024,6,20));

		ctrlEvento.AltaEdicion("Montevideo Comics", "miseventos", "Montevideo Comics 2025", "COMICS25", "Montevideo", "Uruguay",
		                new DTFecha(2025,8,4), new DTFecha(2025,8,6), new DTFecha(2025,7,4));

		ctrlEvento.AltaEdicion("Expointer Uruguay", "miseventos", "Expointer Uruguay 2025", "EXPOAGRO25", "Durazno", "Uruguay",
		                new DTFecha(2025,9,11), new DTFecha(2025,9,17), new DTFecha(2025,2,1));

		ctrlEvento.AltaEdicion("Conferencia de Tecnología", "udelar", "Tecnología Punta del Este 2026", "CONFTECH26", "Punta del Este", "Uruguay",
		                new DTFecha(2026,4,6), new DTFecha(2026,4,10), new DTFecha(2025,8,1));

		ctrlEvento.AltaEdicion("Conferencia de Tecnología", "techcorp", "Mobile World Congress 2025", "MWC", "Barcelona", "España",
		                new DTFecha(2025,12,12), new DTFecha(2025,12,15), new DTFecha(2025,8,21));

		ctrlEvento.AltaEdicion("Conferencia de Tecnología", "techcorp", "Web Summit 2026", "WS26", "Lisboa", "Portugal",
						new DTFecha(2026,1,13), new DTFecha(2026,2,1), new DTFecha(2025,6,4));



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


		// Registros



	}


}
