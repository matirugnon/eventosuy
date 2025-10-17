package servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import excepciones.EventoNoExisteException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logica.controladores.IControladorEvento;
import logica.datatypesyenum.DTEdicion;
import logica.datatypesyenum.DTSeleccionEvento;
import logica.datatypesyenum.EstadoEdicion;

@WebServlet("/consultaEvento")
public class ConsultaEventoServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	
    	request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
    	
        try {
            IControladorEvento ctrl = IControladorEvento.getInstance();

            // Obtener el nombre del evento desde el parámetro
            String nombreEvento = request.getParameter("evento");

            if (nombreEvento == null || nombreEvento.trim().isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/inicio");
                return;
            }

            // Obtener información del evento específico
            DTSeleccionEvento eventoSeleccionado = ctrl.seleccionarEvento(nombreEvento);

            // Obtener solo las ediciones aceptadas del evento (objetos completos)
            Set<String> nombresEdicionesAceptadas = ctrl.listarEdicionesPorEstadoDeEvento(nombreEvento, EstadoEdicion.ACEPTADA);
            
            // Obtener los objetos DTEdicion completos para acceder a las imágenes
            java.util.Set<DTEdicion> edicionesAceptadas = new java.util.HashSet<>();
            for (String nombreEdicion : nombresEdicionesAceptadas) {
                DTEdicion edicion = ctrl.consultarEdicion(nombreEdicion);
                if (edicion != null) {
                    edicionesAceptadas.add(edicion);
                }
            }
            request.setAttribute("edicionesAceptadas", edicionesAceptadas);

            // Obtener todas las categorías para el sidebar (ordenadas alfabéticamente)
            Set<String> categoriasSet = ctrl.listarCategorias();
            List<String> categorias = new ArrayList<>(categoriasSet);
            Collections.sort(categorias);

            // Pasar los datos a la JSP
            request.setAttribute("eventoSeleccionado", eventoSeleccionado);
            request.setAttribute("categorias", categorias);

            // Obtener el rol desde la sesión y pasarlo a la JSP
            String role = (String) request.getSession().getAttribute("role");
            request.setAttribute("role", role);
            request.setAttribute("nickname", request.getSession().getAttribute("usuario"));
            request.setAttribute("avatar", request.getSession().getAttribute("avatar"));
            request.setAttribute("imagen", request.getSession().getAttribute("imagen"));

            request.getRequestDispatcher("/WEB-INF/views/consultaEvento.jsp").forward(request, response);

        } catch (EventoNoExisteException e) {
            // Si el evento no existe, redirigir al inicio
            response.sendRedirect(request.getContextPath() + "/inicio");
        } catch (Exception e) {
            throw new ServletException("Error obteniendo información del evento", e);
        }
    }
}