package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import logica.controladores.IControladorEvento;
import logica.controladores.IControladorUsuario;
import logica.datatypesyenum.DTEdicion;
import logica.datatypesyenum.EstadoEdicion;

import java.io.IOException;
import java.util.Set;

@WebServlet("/edicionesOrganizadas")
public class EdicionesOrganizadasServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private IControladorEvento ctrlEvento;
    private IControladorUsuario ctrlUsuario;
    
    @Override
    public void init() throws ServletException {
        try {
            ctrlEvento = IControladorEvento.getInstance();
            ctrlUsuario = IControladorUsuario.getInstance();
        } catch (Exception e) {
            throw new ServletException("Error al inicializar controladores", e);
        }
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
    	request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
    	
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }
        
        String nickname = (String) session.getAttribute("usuario");
        String role = (String) session.getAttribute("role");
        
        // Verificar que sea organizador
        if (!"organizador".equals(role)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Acceso denegado: Solo organizadores pueden acceder a esta función");
            return;
        }
        
        try {
            // Obtener ediciones aceptadas organizadas por este usuario
            Set<DTEdicion> edicionesOrganizadas = ctrlEvento.listarEdicionesOrganizadasPorEstado(nickname, EstadoEdicion.ACEPTADA);
            
            // Obtener categorías para el sidebar
            Set<String> categorias = ctrlEvento.listarCategorias();
            
            request.setAttribute("edicionesOrganizadas", edicionesOrganizadas);
            request.setAttribute("categorias", categorias);
            
            request.getRequestDispatcher("/WEB-INF/views/edicionesOrganizadas.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error al cargar las ediciones organizadas: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
        }
    }
}