package utils;

import java.util.Set;

import excepciones.CategoriaNoSeleccionadaException;
import excepciones.CorreoInvalidoException;
import excepciones.EdicionExistenteException;
import excepciones.EdicionNoExisteException;
import excepciones.EventoRepetidoException;
import excepciones.EventoYaFinalizadoException;
import excepciones.EventoNoExisteException;
import excepciones.ExisteInstitucionException;
import excepciones.FechaInvalidaException;
import excepciones.FechasIncompatiblesException;
import excepciones.NombreTipoRegistroDuplicadoException;
import excepciones.PatrocinioDuplicadoException;
import excepciones.SiglaRepetidaException;
import excepciones.UsuarioNoExisteException;
import excepciones.UsuarioRepetidoException;
import excepciones.UsuarioYaRegistradoEnEdicionException;
import excepciones.UsuarioNoPerteneceException;
import logica.controladores.IControladorEvento;
import logica.controladores.IControladorRegistro;
import logica.controladores.IControladorUsuario;
import logica.datatypesyenum.DTFecha;
import logica.datatypesyenum.EstadoEdicion;
import logica.datatypesyenum.NivelPatrocinio;


public class Utils {


public static void cargarDatos(IControladorUsuario ctrlUsuario, IControladorEvento ctrlEvento, IControladorRegistro ctrlRegistro  )
		throws UsuarioRepetidoException,
				CorreoInvalidoException, EventoRepetidoException, SiglaRepetidaException, FechaInvalidaException, EventoNoExisteException,
				ExisteInstitucionException, EdicionExistenteException, FechasIncompatiblesException,
				NombreTipoRegistroDuplicadoException, UsuarioNoExisteException, UsuarioYaRegistradoEnEdicionException, CategoriaNoSeleccionadaException, PatrocinioDuplicadoException, EdicionNoExisteException,
				EventoYaFinalizadoException, UsuarioNoPerteneceException
				

{


		// Instituciones
        if (!ctrlUsuario.existeInstitucion("Facultad de Ingeniería"))
            ctrlUsuario.altaInstitucion("Facultad de Ingeniería", "Facultad de Ingeniería de la Universidad de la República", "https://www.fing.edu.uy");
        if (!ctrlUsuario.existeInstitucion("ORT Uruguay"))
            ctrlUsuario.altaInstitucion("ORT Uruguay", "Universidad privada enfocada en tecnología y gestión", "https://ort.edu.uy");
        if (!ctrlUsuario.existeInstitucion("Universidad Católica del Uruguay"))
            ctrlUsuario.altaInstitucion("Universidad Católica del Uruguay", "Institución de educación superior privada", "https://ucu.edu.uy");
        if (!ctrlUsuario.existeInstitucion("Antel"))
            ctrlUsuario.altaInstitucion("Antel", "Empresa estatal de telecomunicaciones", "https://antel.com.uy");
        if (!ctrlUsuario.existeInstitucion("Agencia Nacional de Investigación e Innovación (ANII)"))
            ctrlUsuario.altaInstitucion("Agencia Nacional de Investigación e Innovación (ANII)", "Fomenta la investigación y la innovación en Uruguay", "https://anii.org.uy");

		// Usuarios - Asistentes
        if (!ctrlUsuario.existeNickname("atorres"))
            ctrlUsuario.altaAsistente("atorres", "Ana", "atorres@gmail.com", "Torres", new DTFecha(12,5,1990), "Facultad de Ingeniería", "123.torres", "/img/IMG-US01.jpg" );
        if (!ctrlUsuario.existeNickname("msilva"))
            ctrlUsuario.altaAsistente("msilva", "Martin", "martin.silva@fing.edu.uy", "Silva", new DTFecha(21,8,1987), "Facultad de Ingeniería", "msilva2005", "/img/usSinFoto.webp");
        if (!ctrlUsuario.existeNickname("sofirod"))
            ctrlUsuario.altaAsistente("sofirod", "Sofia", "srodriguez@outlook.com", "Rodriguez", new DTFecha(3,2,1995), "Universidad Católica del Uruguay", "srod.abc1","/img/IMG-US03.jpeg");
        if (!ctrlUsuario.existeNickname("vale23"))
            ctrlUsuario.altaAsistente("vale23", "Valentina", "valentina.costa@mail.com", "Costa", new DTFecha(1,12,1992), "", "valen11c", "/img/IMG-US07.jpeg");
        if (!ctrlUsuario.existeNickname("luciag"))
            ctrlUsuario.altaAsistente("luciag", "Lucía", "lucia.garcia@mail.com", "García", new DTFecha(9,11,1993), "", "garcia.22l", "/img/IMG-US08.jpeg");
        if (!ctrlUsuario.existeNickname("andrearod"))
            ctrlUsuario.altaAsistente("andrearod", "Andrea", "andrea.rod@mail.com", "Rodríguez", new DTFecha(10,6,2000), "Agencia Nacional de Investigación e Innovación (ANII)", "rod77and", "/img/IMG-US09.jpeg");
        if (!ctrlUsuario.existeNickname("AnaG"))
            ctrlUsuario.altaAsistente("AnaG", "Ana", "ana.gomez@hotmail.com", "Gómez", new DTFecha(15,3,1998), "", "gomez88a", "/img/IMG-US12.png");
        if (!ctrlUsuario.existeNickname("JaviL"))
            ctrlUsuario.altaAsistente("JaviL", "Javier", "javier.lopez@outlook.com", "López", new DTFecha(22,7,1995), "", "jl99lopez", "/img/IMG-US13.jpeg");
        if (!ctrlUsuario.existeNickname("MariR"))
            ctrlUsuario.altaAsistente("MariR", "María", "maria.rodriguez@gmail.com", "Rodríguez", new DTFecha(10,11,2000), "", "maria55r", "/img/IMG-US14.jpeg");
        if (!ctrlUsuario.existeNickname("SofiM"))
            ctrlUsuario.altaAsistente("SofiM", "Sofía", "sofia.martinez@yahoo.com", "Martínez", new DTFecha(5,2,1997), "", "smarti99z", "/img/IMG-US15.jpeg");

        // Usuarios - Organizadores
        if (!ctrlUsuario.existeNickname("miseventos"))
            ctrlUsuario.altaOrganizador("miseventos", "MisEventos", "contacto@miseventos.com", "Empresa de organización de eventos.", "https://miseventos.com", "22miseventos", "/img/IMG-US04.jpeg");
        if (!ctrlUsuario.existeNickname("techcorp"))
            ctrlUsuario.altaOrganizador("techcorp", "Corporación Tecnológica", "info@techcorp.com", "Empresa líder en tecnologías de la información.", "", "tech25corp", "/img/usSinFoto.webp");
        if (!ctrlUsuario.existeNickname("imm"))
            ctrlUsuario.altaOrganizador("imm", "Intendencia de Montevideo", "contacto@imm.gub.uy", "Gobierno departamental de Montevideo.", "https://montevideo.gub.uy", "imm2025", "/img/IMG-US06.png");
        if (!ctrlUsuario.existeNickname("udelar"))
            ctrlUsuario.altaOrganizador("udelar", "Universidad de la República", "contacto@udelar.edu.uy", "Universidad pública de Uruguay.", "https://udelar.edu.uy", "25udelar", "/img/usSinFoto.webp");
        if (!ctrlUsuario.existeNickname("mec"))
            ctrlUsuario.altaOrganizador("mec", "Ministerio de Educación y Cultura", "mec@mec.gub.uy", "Institución pública promotora de cultura.", "https://mec.gub.uy", "mec2025ok", "/img/IMG-US11.png");

        // Asignar avatares a usuarios específicos


        
        //seguidores
        ctrlUsuario.seguirUsuario("atorres", "sofirod");
        ctrlUsuario.seguirUsuario("atorres", "imm");
        ctrlUsuario.seguirUsuario("sofirod", "imm");
        ctrlUsuario.seguirUsuario("sofirod", "atorres");
        ctrlUsuario.seguirUsuario("udelar", "techcorp");
        ctrlUsuario.seguirUsuario("udelar", "mec");
        ctrlUsuario.seguirUsuario("techcorp", "sofirod");
        
        
        // Categorias
        if (!ctrlEvento.existeCategoria("Tecnología"))
            ctrlEvento.altaCategoria("Tecnología");
        if (!ctrlEvento.existeCategoria("Innovación"))
            ctrlEvento.altaCategoria("Innovación");
        if (!ctrlEvento.existeCategoria("Literatura"))
            ctrlEvento.altaCategoria("Literatura");
        if (!ctrlEvento.existeCategoria("Cultura"))
            ctrlEvento.altaCategoria("Cultura");
        if (!ctrlEvento.existeCategoria("Música"))
            ctrlEvento.altaCategoria("Música");
        if (!ctrlEvento.existeCategoria("Deporte"))
            ctrlEvento.altaCategoria("Deporte");
        if (!ctrlEvento.existeCategoria("Salud"))
            ctrlEvento.altaCategoria("Salud");
        if (!ctrlEvento.existeCategoria("Entretenimiento"))
            ctrlEvento.altaCategoria("Entretenimiento");
        if (!ctrlEvento.existeCategoria("Agro"))
            ctrlEvento.altaCategoria("Agro");
        if (!ctrlEvento.existeCategoria("Negocios"))
            ctrlEvento.altaCategoria("Negocios");
        if (!ctrlEvento.existeCategoria("Moda"))
            ctrlEvento.altaCategoria("Moda");
        if (!ctrlEvento.existeCategoria("Investigación"))
            ctrlEvento.altaCategoria("Investigación");

        // Eventos
        if (!ctrlEvento.existeEvento("Conferencia de Tecnología"))
            ctrlEvento.darAltaEvento("Conferencia de Tecnología", "Evento sobre innovación tecnológica", new DTFecha(1,10,2025), "CONFTEC", Set.of("Tecnología", "Innovación"));
        if (!ctrlEvento.existeEvento("Feria del Libro"))
            ctrlEvento.darAltaEvento("Feria del Libro", "Encuentro anual de literatura", new DTFecha(1,2,2025), "FERLIB", Set.of("Literatura", "Cultura"), "/img/IMG-EV02.jpeg");
        if (!ctrlEvento.existeEvento("Montevideo Rock"))
            ctrlEvento.darAltaEvento("Montevideo Rock", "Festival de rock con artistas nacionales e internacionales", new DTFecha(15,3,2023), "MONROCK", Set.of("Cultura", "Música"), "/img/IMG-EV03.jpeg");
        if (!ctrlEvento.existeEvento("Maratón de Montevideo"))
            ctrlEvento.darAltaEvento("Maratón de Montevideo", "Competencia deportiva anual en la capital", new DTFecha(1,1,2022), "MARATON", Set.of("Deporte", "Salud"), "/img/IMG-EV04.png");
        if (!ctrlEvento.existeEvento("Montevideo Comics"))
            ctrlEvento.darAltaEvento("Montevideo Comics", "Convención de historietas, cine y cultura geek", new DTFecha(10,4,2024), "COMICS", Set.of("Cultura", "Entretenimiento"), "/img/IMG-EV05.png");
        if (!ctrlEvento.existeEvento("Expointer Uruguay"))
            ctrlEvento.darAltaEvento("Expointer Uruguay", "Exposición internacional agropecuaria y ganadera", new DTFecha(12,12,2024), "EXPOAGRO", Set.of("Agro", "Negocios"), "/img/IMG-EV06.png");
        if (!ctrlEvento.existeEvento("Montevideo Fashion Week"))
            ctrlEvento.darAltaEvento("Montevideo Fashion Week", "Pasarela de moda uruguaya e internacional", new DTFecha(20,7,2025), "MFASHION", Set.of("Cultura", "Moda"));
        if (!ctrlEvento.existeEvento("Global"))
            ctrlEvento.darAltaEvento("Global", "Aventureros en grupo", new DTFecha(01,01,2025), "GBL", Set.of("Cultura"), "/img/IMG-EV08.jpeg");
        
        //visitas
        ctrlEvento.setVisitas("Conferencia de Tecnología", 2);
        ctrlEvento.setVisitas("Feria del Libro", 10);
        ctrlEvento.setVisitas("Montevideo Rock", 25);
        ctrlEvento.setVisitas("Maratón de Montevideo", 13);
        ctrlEvento.setVisitas("Montevideo Comics", 5);
        ctrlEvento.setVisitas("Expointer Uruguay", 10);
        ctrlEvento.setVisitas("Montevideo Fashion Week", 8);
        ctrlEvento.setVisitas("Global", 20);
        
        //Finalizar evento
      
        
        
        // Ediciones
        if (!ctrlEvento.existeEdicion("Montevideo Rock 2025"))
            ctrlEvento.altaEdicion("Montevideo Rock", "imm", "Montevideo Rock 2025", "MONROCK25", "Montevideo", "Uruguay",
                    new DTFecha(20,11,2025), new DTFecha(22,11,2025), new DTFecha(12,3,2025), "/img/IMG-EDEV01.jpeg", "https://www.youtube.com/watch?v=YFbRrUX04tU");

        if (!ctrlEvento.existeEdicion("Maratón de Montevideo 2025"))
            ctrlEvento.altaEdicion("Maratón de Montevideo", "imm", "Maratón de Montevideo 2025", "MARATON25", "Montevideo", "Uruguay",
                            new DTFecha(14,9,2025), new DTFecha(14,9,2025), new DTFecha(5,2,2025), "/img/IMG-EDEV02.png", "https://www.youtube.com/watch?v=Pg7Jw787MgE");

        if (!ctrlEvento.existeEdicion("Maratón de Montevideo 2024"))
            ctrlEvento.altaEdicion("Maratón de Montevideo", "imm", "Maratón de Montevideo 2024", "MARATON24", "Montevideo", "Uruguay",
                            new DTFecha(14,9,2024), new DTFecha(14,9,2024), new DTFecha(21,4,2024), "/img/IMG-EDEV03.jpeg", "https://www.youtube.com/watch?v=hxDn4EEMank");

        if (!ctrlEvento.existeEdicion("Maratón de Montevideo 2022"))
            ctrlEvento.altaEdicion("Maratón de Montevideo", "imm", "Maratón de Montevideo 2022", "MARATON22", "Montevideo", "Uruguay",
                            new DTFecha(14,9,2022), new DTFecha(14,9,2022), new DTFecha(21,5,2022), "/img/IMG-EDEV04.jpeg");

        if (!ctrlEvento.existeEdicion("Montevideo Comics 2024"))
            ctrlEvento.altaEdicion("Montevideo Comics", "miseventos", "Montevideo Comics 2024", "COMICS24", "Montevideo", "Uruguay",
                            new DTFecha(18,7,2024), new DTFecha(21,7,2024), new DTFecha(20,6,2024), "/img/IMG-EDEV05.jpeg", "https://www.youtube.com/watch?v=4n0itnXxCMg");

        if (!ctrlEvento.existeEdicion("Montevideo Comics 2025"))
            ctrlEvento.altaEdicion("Montevideo Comics", "miseventos", "Montevideo Comics 2025", "COMICS25", "Montevideo", "Uruguay",
                            new DTFecha(4,8,2025), new DTFecha(6,8,2025), new DTFecha(4,7,2025), "/img/IMG-EDEV06.jpeg", "https://www.youtube.com/watch?v=jRJt4i7G-SY");

        if (!ctrlEvento.existeEdicion("Expointer Uruguay 2025"))
            ctrlEvento.altaEdicion("Expointer Uruguay", "miseventos", "Expointer Uruguay 2025", "EXPOAGRO25", "Durazno", "Uruguay",
                            new DTFecha(11,9,2025), new DTFecha(17,9,2025), new DTFecha(1,2,2025), "/img/IMG-EDEV07.jpeg", "https://www.youtube.com/watch?v=NFjb-JujCCY");

        if (!ctrlEvento.existeEdicion("Tecnología Punta del Este 2026"))
            ctrlEvento.altaEdicion("Conferencia de Tecnología", "udelar", "Tecnología Punta del Este 2026", "TECH26", "Punta del Este", "Uruguay",
                            new DTFecha(6,4,2026), new DTFecha(10,4,2026), new DTFecha(1,8,2025), "/img/IMG-EDEV08.jpeg", "https://www.youtube.com/watch?v=IPukuYb9xWw");

        if (!ctrlEvento.existeEdicion("Mobile World Congress 2025"))
            ctrlEvento.altaEdicion("Conferencia de Tecnología", "techcorp", "Mobile World Congress 2025", "MWC25", "Barcelona", "España",
                            new DTFecha(12,12,2025), new DTFecha(15,12,2025), new DTFecha(21,8,2025), "/img/eventoSinImagen.png", "https://www.youtube.com/watch?v=zNVbgEJfgz8");

        if (!ctrlEvento.existeEdicion("Web Summit 2026"))
            ctrlEvento.altaEdicion("Conferencia de Tecnología", "techcorp", "Web Summit 2026", "WS26", "Lisboa", "Portugal",
                            new DTFecha(13,1,2026), new DTFecha(1,2,2026), new DTFecha(4,6,2025));

            ctrlEvento.altaEdicion("Montevideo Fashion Week", "mec",  "Montevideo Fashion Week 2026", "MFW26", "Nueva York", "Estados Unidos", new DTFecha(16,2,2026), new DTFecha(20,2,2026),new DTFecha(2,10,2025), "/img/IMG-EDEV11.jpeg");
            
            ctrlEvento.altaEdicion("Global", "miseventos",  "Descubre la Magia de Machu Picchu", "MAPI25", "Cusco", "Perú", new DTFecha(10,11,2025), new DTFecha(30,11,2025),new DTFecha(7,8,2025), "/img/IMG-EDEV12.jpeg", "https://www.youtube.com/watch?v=cnMa-Sm9H4k");
            
        

		// Estados definidos para mostrar en los perfiles
		ctrlEvento.actualizarEstadoEdicion("Montevideo Rock 2025", EstadoEdicion.ACEPTADA);
        ctrlEvento.actualizarEstadoEdicion("Maratón de Montevideo 2025", EstadoEdicion.ACEPTADA);
		ctrlEvento.actualizarEstadoEdicion("Maratón de Montevideo 2024", EstadoEdicion.ACEPTADA);
		ctrlEvento.actualizarEstadoEdicion("Maratón de Montevideo 2022", EstadoEdicion.RECHAZADA);
		ctrlEvento.actualizarEstadoEdicion("Montevideo Comics 2024", EstadoEdicion.ACEPTADA);
        ctrlEvento.actualizarEstadoEdicion("Montevideo Comics 2025", EstadoEdicion.ACEPTADA);
		ctrlEvento.actualizarEstadoEdicion("Expointer Uruguay 2025", EstadoEdicion.INGRESADA);
		ctrlEvento.actualizarEstadoEdicion("Tecnología Punta del Este 2026", EstadoEdicion.ACEPTADA);
        ctrlEvento.actualizarEstadoEdicion("Mobile World Congress 2025", EstadoEdicion.ACEPTADA);
		ctrlEvento.actualizarEstadoEdicion("Web Summit 2026", EstadoEdicion.ACEPTADA);
        ctrlEvento.actualizarEstadoEdicion("Montevideo Fashion Week 2026", EstadoEdicion.INGRESADA);
        ctrlEvento.actualizarEstadoEdicion("Descubre la Magia de Machu Picchu", EstadoEdicion.ACEPTADA);

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

		// Montevideo Fashion Week 2026
		ctrlRegistro.altaTipoDeRegistro("Montevideo Fashion Week 2026", "Full", "Acceso a todos los eventos de la semana", 450, 50);
		ctrlRegistro.altaTipoDeRegistro("Montevideo Fashion Week 2026", "Visitante", "Acceso parcial a los eventos de la semana", 150, 25);

		ctrlRegistro.altaTipoDeRegistro("Descubre la Magia de Machu Picchu", "plus50", "Viaje para personas con más de 50 años", 250, 10);
		ctrlRegistro.altaTipoDeRegistro("Descubre la Magia de Machu Picchu", "Mayores", "Viaje para personas con más de 1 8 años", 300, 20);

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
		
		ctrlRegistro.altaRegistroConPatrocinio("Tecnología Punta del Este 2026", "msilva", "Estudiante", new DTFecha(1,10,2025), "TECHFING");
		ctrlRegistro.altaRegistroConPatrocinio("Tecnología Punta del Este 2026", "andrearod", "General", new DTFecha(6,10,2025), "TECHANII");
        
        ctrlRegistro.altaRegistro("Tecnología Punta del Este 2026", "MariR", "General", new DTFecha(10,10,2025), 1500);
        ctrlRegistro.altaRegistro("Descubre la Magia de Machu Picchu", "atorres", "Mayores", new DTFecha(7,11,2025), 300);
        ctrlRegistro.altaRegistro("Descubre la Magia de Machu Picchu", "msilva", "Mayores", new DTFecha(10,8,2025), 300);
        ctrlRegistro.altaRegistro("Descubre la Magia de Machu Picchu", "AnaG", "plus50", new DTFecha(30,9,2025), 250);
        ctrlEvento.finalizarEvento("Global");
        
        
        //inicia 20/11/2025
        //ctrlRegistro.registrarAsistencia("sofirod", "Montevideo Rock 2025", "VIP");
        
        ctrlRegistro.registrarAsistencia("sofirod", "Maratón de Montevideo 2025", "Corredor 42K");
        ctrlRegistro.registrarAsistencia("AnaG", "Maratón de Montevideo 2025", "Corredor 10K");
        ctrlRegistro.registrarAsistencia("SofiM", "Montevideo Comics 2024", "General");
        ctrlRegistro.registrarAsistencia("atorres", "Descubre la Magia de Machu Picchu", "Mayores");
        ctrlRegistro.registrarAsistencia("AnaG", "Descubre la Magia de Machu Picchu", "plus50");
        
	}

	// Método para verificar si los datos ya fueron precargados
	public static boolean datosPrecargados(jakarta.servlet.ServletContext context) {
		Boolean datosCargados = (Boolean) context.getAttribute("datosPrecargados");
		return datosCargados != null && datosCargados;
	}

	// Método para marcar que los datos fueron cargados
	public static void marcarDatosCargados(jakarta.servlet.ServletContext context) {
		context.setAttribute("datosPrecargados", true);
	}

	// Método para verificar si hay datos básicos cargados (usuarios, eventos, etc.)
	public static boolean hayDatosBasicos() {
		try {
			IControladorUsuario ctrlUsuario = IControladorUsuario.getInstance();
			IControladorEvento ctrlEvento = IControladorEvento.getInstance();

			// Verificar si hay al menos un usuario y un evento
			boolean hayUsuarios = !ctrlUsuario.listarUsuarios().isEmpty();
			boolean hayEventos = !ctrlEvento.obtenerDTEventos().isEmpty();

			return hayUsuarios && hayEventos;
		} catch (Exception e) {
			return false;
		}
	}

}
