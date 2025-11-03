package servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logica.datatypesyenum.DTUsuario;

//nuevos imports (copiar este bloque)
import soap.DtEdicion;
import soap.DTFecha;
import soap.PublicadorControlador;
import soap.StringArray;
import utils.SoapClientHelper;

@WebServlet("/consultaEdicion")
public class ConsultaEdicionServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");

		try {

			PublicadorControlador publicador = SoapClientHelper.getPublicadorControlador();

			// Obtener el nombre de la ediciÃ³n desde el parÃ¡metro
			String nombreEdicion = request.getParameter("edicion");

			if (nombreEdicion == null || nombreEdicion.trim().isEmpty()) {
				response.sendRedirect(request.getContextPath() + "/inicio");
				return;
			}

			// Obtener información de la edición específica
			DtEdicion edicionDt = publicador.consultarEdicion(nombreEdicion);

			if (edicionDt == null) {
				response.sendRedirect(request.getContextPath() + "/inicio");
				return;
			}

			// Obtener información del evento padre
			String nombreEventoPadre = publicador.obtenerEventoDeEdicion(nombreEdicion);

			// Obtener información del organizador
			String nicknameOrganizador = edicionDt.getOrganizador();
			DTUsuario organizador = null;
			String avatarOrganizador = "/img/usSinFoto.webp"; // Avatar por defecto

			if (nicknameOrganizador != null && !nicknameOrganizador.trim().isEmpty()) {
				try {
					soap.PublicadorUsuario publicadorUsuario = SoapClientHelper.getPublicadorUsuario();
					avatarOrganizador = publicadorUsuario.obtenerAvatar(nicknameOrganizador);
					if (avatarOrganizador == null || avatarOrganizador.trim().isEmpty()) {
						avatarOrganizador = "/img/usSinFoto.webp";
					}
				} catch (Exception e) {
					System.err.println("Error obteniendo avatar del organizador: " + e.getMessage());
					avatarOrganizador = "/img/usSinFoto.webp";
				}
			}

			// Obtener todas las categorías para el sidebar (ordenadas alfabéticamente)
			StringArray categoriasSet = publicador.listarCategorias();
			List<String> categorias = new ArrayList<>(categoriasSet.getItem());
			Collections.sort(categorias);

			// Obtener tipos de registro de la edición
			List<String> tiposDeRegistro = new ArrayList<>();
			try {
				soap.PublicadorRegistro publicadorRegistro = utils.SoapClientHelper.getPublicadorRegistro();
				soap.StringArray tiposArray = publicadorRegistro.listarTipoRegistro(nombreEdicion);
				if (tiposArray != null && tiposArray.getItem() != null) {
					tiposDeRegistro.addAll(tiposArray.getItem());
				}
			} catch (Exception e) {
				System.err.println("Error obteniendo tipos de registro: " + e.getMessage());
			}
			
			
			// obtener el rol y pasar nuevos datos de caso de uso
			String usuarioSesion = (String) request.getSession().getAttribute("usuario");
			String role = (String) request.getSession().getAttribute("role");
			String url = null;

			if ("asistente".equals(role)) {
				soap.PublicadorRegistro publicadorRegistro = utils.SoapClientHelper.getPublicadorRegistro();
				boolean registrado = publicadorRegistro.estaRegistrado(edicionDt.getNombre(), usuarioSesion);
				if (registrado) {
					String nombreCodificadoX264 = URLEncoder.encode(edicionDt.getNombre(), StandardCharsets.UTF_8);
					url = "/web/consultaRegistro?asistente=" + usuarioSesion + "&edicion=" + nombreCodificadoX264;
				} else {
					String nombreCodificadoX264 = URLEncoder.encode(edicionDt.getNombre(), StandardCharsets.UTF_8);
					String eventoCodificadoX264 = URLEncoder.encode(edicionDt.getEvento(), StandardCharsets.UTF_8);
					System.out.print(edicionDt.getEvento());
					url = "/web/registroAedicion?edicion=" + nombreCodificadoX264  + "&evento=" + eventoCodificadoX264;
				}
			} else if ("organizador".equals(role)) {
				if (usuarioSesion != null && usuarioSesion.equals(edicionDt.getOrganizador())) {
					String nombreCodificadoX264 = URLEncoder.encode(edicionDt.getNombre(), StandardCharsets.UTF_8);
					url = "/web/registrosEdicion?edicion=" + nombreCodificadoX264;
				}
			}

			// obtener la fecha fin de la edición
			DTFecha fechaFinDt = edicionDt.getFechaFin();
			LocalDate fechaFin = LocalDate.of(fechaFinDt.getAnio(), fechaFinDt.getMes(), fechaFinDt.getDia());
			LocalDate hoy = LocalDate.now();
			boolean finalizado = fechaFin.isBefore(hoy);

			// pasar al JSP
			request.setAttribute("finalizado", finalizado);
			request.setAttribute("urlBoton", url);

			// Pasar los datos a la JSP
			request.setAttribute("edicion", edicionDt);
			request.setAttribute("eventoPadre", nombreEventoPadre);
			request.setAttribute("organizador", organizador);
			request.setAttribute("avatarOrganizador", avatarOrganizador);
			request.setAttribute("categorias", categorias);
			request.setAttribute("tiposDeRegistro", tiposDeRegistro);

			// Obtener el rol desde la sesión y pasarlo a la JSP
			request.setAttribute("role", role);
			request.setAttribute("nickname", request.getSession().getAttribute("usuario"));
			request.setAttribute("avatar", request.getSession().getAttribute("avatar"));

			request.getRequestDispatcher("/WEB-INF/views/consultaEdicion.jsp").forward(request, response);

		} catch (Exception e) {
			throw new ServletException("Error obteniendo informaciÃ³n de la ediciÃ³n", e);
		}
	}
}
