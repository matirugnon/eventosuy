package servlets;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import soap.PublicadorControlador;
import utils.SoapClientHelper;

/**
 * Servlet para archivar ediciones de eventos.
 * Solo organizadores pueden archivar sus propias ediciones finalizadas.
 */
@WebServlet("/archivarEdicion")
public class ArchivarEdicionServlet extends HttpServlet {
    
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String usuarioSesion = (String) request.getSession().getAttribute("usuario");
        String role = (String) request.getSession().getAttribute("role");

        // Verificar que el usuario esté logueado y sea organizador
        if (usuarioSesion == null || !"organizador".equals(role)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            String nombreEdicion = request.getParameter("edicion");
            
            if (nombreEdicion == null || nombreEdicion.trim().isEmpty()) {
                request.getSession().setAttribute("error", "Nombre de edición inválido.");
                response.sendRedirect(request.getContextPath() + "/edicionesOrganizadas");
                return;
            }

            PublicadorControlador publicador = SoapClientHelper.getPublicadorControlador();
            
            // Verificar que la edición pertenece al organizador logueado
            soap.DtEdicion edicion = publicador.consultarEdicion(nombreEdicion);
            if (edicion == null) {
                request.getSession().setAttribute("error", "La edición no existe.");
                response.sendRedirect(request.getContextPath() + "/edicionesOrganizadas");
                return;
            }
            
            if (!usuarioSesion.equals(edicion.getOrganizador())) {
                request.getSession().setAttribute("error", "No tienes permisos para archivar esta edición.");
                response.sendRedirect(request.getContextPath() + "/edicionesOrganizadas");
                return;
            }
            
            // Intentar archivar la edición en el servidor central vía SOAP
            String resultado = publicador.archivarEdicion(nombreEdicion);

            if ("OK".equals(resultado)) {
                request.getSession().setAttribute("success", "Edición archivada exitosamente.");
            } else {
                request.getSession().setAttribute("error", resultado);
            }
            
            response.sendRedirect(request.getContextPath() + "/edicionesOrganizadas");

        } catch (Exception e) {
            request.getSession().setAttribute("error", "Error al archivar la edición: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/edicionesOrganizadas");
        }
    }
}
