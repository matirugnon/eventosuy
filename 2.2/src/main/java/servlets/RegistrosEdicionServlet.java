package servlets;

import java.io.IOException;
import java.util.Set;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import logica.controladores.IControladorEvento;
import logica.datatypesyenum.DTEdicion;

@WebServlet("/registrosEdicion")
public class RegistrosEdicionServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	
    	request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
    	
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String nickname = (String) session.getAttribute("usuario");
        String role = (String) session.getAttribute("role");

        if (!"organizador".equals(role)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Acceso denegado: Solo organizadores pueden consultar registros de sus ediciones");
            return;
        }

        String edicion = request.getParameter("edicion");
        if (edicion == null || edicion.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/edicionesOrganizadas");
            return;
        }

        try {
            IControladorEvento ctrlEvento = IControladorEvento.getInstance();
            DTEdicion dt = ctrlEvento.consultarEdicion(edicion);

            if (dt == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Edición no encontrada");
                return;
            }

            // Verificar que el organizador de la edición coincide con el usuario en sesión
            if (dt.getOrganizador() == null || !dt.getOrganizador().equals(nickname)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Acceso denegado: No sos el organizador de esta edición");
                return;
            }

            // Obtener categorías para el sidebar
            Set<String> categorias = ctrlEvento.listarCategorias();

            request.setAttribute("edicion", dt);
            request.setAttribute("categorias", categorias);
            
            // Pasar datos de sesión al JSP
            request.setAttribute("nickname", nickname);
            request.setAttribute("role", role);
            request.setAttribute("avatar", session.getAttribute("avatar"));
            request.setAttribute("nombre", session.getAttribute("nombre"));

            request.getRequestDispatcher("/WEB-INF/views/registrosEdicion.jsp").forward(request, response);

        } catch (Exception e) {
            throw new ServletException("Error cargando registros de la edición", e);
        }
    }
}
