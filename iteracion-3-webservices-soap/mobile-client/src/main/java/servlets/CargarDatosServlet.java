package servlets;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import soap.PublicadorCargaDatos;
import utils.SoapClientHelper;

@WebServlet("/cargarDatos")
public class CargarDatosServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		cargarDatos(request, response);
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		cargarDatos(request, response);
	}

	private void cargarDatos(HttpServletRequest request, HttpServletResponse response) throws IOException {
		HttpSession session = request.getSession();
		
		// Determinar si viene desde login o desde inicio
		String referer = request.getHeader("referer");
		String redirectUrl = (referer != null && referer.contains("/login")) 
			? request.getContextPath() + "/login" 
			: request.getContextPath() + "/inicio";

		try {
			PublicadorCargaDatos publicadorDatos = SoapClientHelper.getPublicadorCargaDatos();
			boolean resultado = publicadorDatos.cargarDatos();
			
			if (resultado) {
				session.setAttribute("datosMensaje", "Los datos de ejemplo se cargaron correctamente.");
				session.setAttribute("datosMensajeTipo", "success");
			} else {
				session.setAttribute("datosMensaje", "Los datos ya estaban cargados o hubo un error.");
				session.setAttribute("datosMensajeTipo", "info");
			}
		} catch (Exception e) {
			session.setAttribute("datosMensaje", "Error al cargar los datos: " + e.getMessage());
			session.setAttribute("datosMensajeTipo", "error");
		}

		response.sendRedirect(redirectUrl);
	}
}

