package servlets;

import java.io.IOException;

import excepciones.CategoriaNoSeleccionadaException;
import excepciones.CorreoInvalidoException;
import excepciones.EdicionExistenteException;
import excepciones.EdicionNoExisteException;
import excepciones.EventoRepetidoException;
import excepciones.ExisteInstitucionException;
import excepciones.FechaInvalidaException;
import excepciones.FechasIncompatiblesException;
import excepciones.NombreTipoRegistroDuplicadoException;
import excepciones.PatrocinioDuplicadoException;
import excepciones.UsuarioNoExisteException;
import excepciones.UsuarioRepetidoException;
import excepciones.UsuarioYaRegistradoEnEdicionException;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import logica.controladores.IControladorEvento;
import logica.controladores.IControladorRegistro;
import logica.controladores.IControladorUsuario;
import utils.Utils;

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
		ServletContext context = getServletContext();
		HttpSession session = request.getSession();

		if (Utils.datosPrecargados(context)) {
			session.setAttribute("datosMensaje", "Los datos ya estaban cargados.");
			session.setAttribute("datosMensajeTipo", "info");
			response.sendRedirect(request.getContextPath() + "/inicio");
			return;
		}

		try {
			Utils.cargarDatos(
					IControladorUsuario.getInstance(),
					IControladorEvento.getInstance(),
					IControladorRegistro.getInstance()
			);
			Utils.marcarDatosCargados(context);
			session.setAttribute("datosMensaje", "Los datos de ejemplo se cargaron correctamente.");
			session.setAttribute("datosMensajeTipo", "success");
		} catch (UsuarioRepetidoException |
				CorreoInvalidoException |
				EventoRepetidoException |
				FechaInvalidaException |
				ExisteInstitucionException |
				EdicionExistenteException |
				FechasIncompatiblesException |
				NombreTipoRegistroDuplicadoException |
				UsuarioNoExisteException |
				UsuarioYaRegistradoEnEdicionException |
				CategoriaNoSeleccionadaException |
				PatrocinioDuplicadoException |
				EdicionNoExisteException e) {
			session.setAttribute("datosMensaje", "Ocurri\u00f3 un error al cargar los datos: " + e.getMessage());
			session.setAttribute("datosMensajeTipo", "error");
		}

		response.sendRedirect(request.getContextPath() + "/inicio");
	}
}
