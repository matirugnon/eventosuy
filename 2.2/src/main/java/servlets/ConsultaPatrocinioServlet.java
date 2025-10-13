package servlets;

import java.io.IOException;
import java.util.Set;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import logica.controladores.IControladorEvento;
import logica.controladores.IControladorUsuario;
import logica.datatypesyenum.DTPatrocinio;
import logica.datatypesyenum.DTEdicion;
import logica.datatypesyenum.DTInstitucion;
import excepciones.EdicionNoExisteException;
import excepciones.EdicionSinPatrociniosException;
import excepciones.PatrocinioNoEncontradoException;
import utils.Utils;

@WebServlet("/consultaPatrocinio")
public class ConsultaPatrocinioServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Obtener parámetros
            String codigo = request.getParameter("codigo");
            String edicion = request.getParameter("edicion");

            if (codigo == null || edicion == null || codigo.trim().isEmpty() || edicion.trim().isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/inicio");
                return;
            }

            // Limpiar espacios en blanco
            codigo = codigo.trim();
            edicion = edicion.trim();

            if (!Utils.asegurarDatosCargados(request, response)) {
                return;
            }

            // Obtener controladores
            IControladorEvento ctrlEvento = IControladorEvento.getInstance();
            IControladorUsuario ctrlUsuario = IControladorUsuario.getInstance();

            // Obtener información del patrocinio
            DTPatrocinio patrocinio = ctrlEvento.consultarTipoPatrocinioEdicion(edicion, codigo);

            if (patrocinio == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Patrocinio no encontrado");
                return;
            }

            // Obtener información de la institución (incluyendo logo)
            DTInstitucion institucion = ctrlUsuario.getInstitucion(patrocinio.getInstitucion());

            // Obtener información de la edición
            DTEdicion edicionInfo = ctrlEvento.consultarEdicion(edicion);

            // Obtener todas las categorías para el sidebar
            Set<String> categorias = ctrlEvento.listarCategorias();

            // Pasar los datos a la JSP
            request.setAttribute("patrocinio", patrocinio);
            request.setAttribute("institucion", institucion);
            request.setAttribute("edicionInfo", edicionInfo);
            request.setAttribute("categorias", categorias);

            // Obtener el rol desde la sesión y pasarlo a la JSP
            String role = (String) request.getSession().getAttribute("role");
            request.setAttribute("role", role);
            request.setAttribute("nickname", request.getSession().getAttribute("usuario"));
            request.setAttribute("avatar", request.getSession().getAttribute("avatar"));

            request.getRequestDispatcher("/WEB-INF/views/consultaPatrocinio.jsp").forward(request, response);

        } catch (EdicionNoExisteException | EdicionSinPatrociniosException | PatrocinioNoEncontradoException e) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Patrocinio no encontrado: " + e.getMessage());
        } catch (Exception e) {
            throw new ServletException("Error obteniendo información del patrocinio", e);
        }
    }
}
