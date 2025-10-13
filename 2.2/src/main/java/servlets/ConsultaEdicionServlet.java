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
import logica.datatypesyenum.DTEdicion;
import logica.datatypesyenum.DTEvento;
import logica.datatypesyenum.DTUsuario;
import logica.Usuario;
import utils.Utils;

@WebServlet("/consultaEdicion")
public class ConsultaEdicionServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            IControladorEvento ctrl = IControladorEvento.getInstance();
            IControladorUsuario ctrlUsuario = IControladorUsuario.getInstance();

            // Obtener el nombre de la edición desde el parámetro
            String nombreEdicion = request.getParameter("edicion");
            
            if (nombreEdicion == null || nombreEdicion.trim().isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/inicio");
                return;
            }

            if (!Utils.asegurarDatosCargados(request, response)) {
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
            
            // Obtener información del organizador
            String nicknameOrganizador = edicion.getOrganizador();
            DTUsuario organizador = null;
            String avatarOrganizador = "/img/avatar-default.png"; // Avatar por defecto
            
            if (nicknameOrganizador != null && !nicknameOrganizador.trim().isEmpty()) {
                try {
                    organizador = ctrlUsuario.getDTUsuario(nicknameOrganizador);
                    if (organizador != null) {
                        // Obtener el avatar del organizador
                        Usuario usuarioOrganizador = ctrlUsuario.obtenerUsuario(nicknameOrganizador);
                        if (usuarioOrganizador != null && usuarioOrganizador.getAvatar() != null) {
                            avatarOrganizador = usuarioOrganizador.getAvatar();
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Error obteniendo información del organizador: " + e.getMessage());
                }
            }
            
            // Obtener todas las categorías para el sidebar
            Set<String> categorias = ctrl.listarCategorias();

            // Pasar los datos a la JSP
            request.setAttribute("edicion", edicion);
            request.setAttribute("eventoPadre", eventoPadre);
            request.setAttribute("organizador", organizador);
            request.setAttribute("avatarOrganizador", avatarOrganizador);
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
