package servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import servlets.dto.EdicionDetalleDTO;
import soap.PublicadorControlador;
import utils.SoapClientHelper;



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
        	PublicadorControlador publicadorEv = SoapClientHelper.getPublicadorControlador();
            EdicionDetalleDTO dt =  new EdicionDetalleDTO(publicadorEv.obtenerDetalleCompletoEdicion(edicion));

            if (dt == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "EdiciÃ³n no encontrada");
                return;
            }
         

            // Verificar que el organizador de la ediciÃ³n coincide con el usuario en sesiÃ³n
            if (dt.getOrganizador() == null || !dt.getOrganizador().equals(nickname)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Acceso denegado: No sos el organizador de esta ediciÃ³n");
                return;
            }

            // Obtener categorÃ­as para el sidebar (ordenadas alfabÃ©ticamente)
            Set<String> categoriasSet = new HashSet(publicadorEv.listarCategorias().getItem());
            List<String> categorias = new ArrayList<>(categoriasSet);
            Collections.sort(categorias);

            request.setAttribute("edicion", dt);
            request.setAttribute("categorias", categorias);
            
            // Pasar datos de sesiÃ³n al JSP
            request.setAttribute("nickname", nickname);
            request.setAttribute("role", role);
            request.setAttribute("avatar", session.getAttribute("avatar"));
            request.setAttribute("nombre", session.getAttribute("nombre"));

            request.getRequestDispatcher("/WEB-INF/views/registrosEdicion.jsp").forward(request, response);

        } catch (Exception e) {
            throw new ServletException("Error cargando registros de la ediciÃ³n", e);
        }
    }
}

