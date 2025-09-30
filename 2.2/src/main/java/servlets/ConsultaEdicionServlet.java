package servlets;

import java.io.IOException;
import java.util.Set;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import logica.Controladores.IControladorEvento;
import logica.DatatypesYEnum.DTEdicion;
import logica.DatatypesYEnum.DTEvento;

@WebServlet("/consultaEdicion")
public class ConsultaEdicionServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            IControladorEvento ctrl = IControladorEvento.getInstance();

            // Obtener el nombre de la edición desde el parámetro
            String nombreEdicion = request.getParameter("edicion");
            
            if (nombreEdicion == null || nombreEdicion.trim().isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/inicio");
                return;
            }

            // Obtener información de la edición específica
            DTEdicion edicion = ctrl.consultarEdicion(nombreEdicion);
            
            if (edicion == null) {
                response.sendRedirect(request.getContextPath() + "/inicio");
                return;
            }
            
            // Obtener información del evento padre
            DTEvento eventoPadre = ctrl.obtenerEventoPorEdicion(nombreEdicion);
            
            // Obtener todas las categorías para el sidebar
            Set<String> categorias = ctrl.listarCategorias();

            // Pasar los datos a la JSP
            request.setAttribute("edicion", edicion);
            request.setAttribute("eventoPadre", eventoPadre);
            request.setAttribute("categorias", categorias);

            // Obtener el rol desde la sesión y pasarlo a la JSP
            String role = (String) request.getSession().getAttribute("role");
            request.setAttribute("role", role);
            request.setAttribute("nickname", request.getSession().getAttribute("usuario"));
            request.setAttribute("avatar", request.getSession().getAttribute("avatar"));

            request.getRequestDispatcher("/WEB-INF/views/consultaEdicion.jsp").forward(request, response);

        } catch (Exception e) {
            throw new ServletException("Error obteniendo información de la edición", e);
        }
    }
}