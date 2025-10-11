package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import logica.controladores.IControladorEvento;
import logica.controladores.IControladorRegistro;
import logica.datatypesyenum.DTRegistro;

import java.io.IOException;
import java.util.Set;
import java.util.LinkedHashSet;
import java.util.stream.Collectors;

@WebServlet("/misRegistros")
public class MisRegistrosServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private IControladorRegistro ctrlRegistro;
    private IControladorEvento ctrlEvento;
    
    @Override
    public void init() throws ServletException {
        try {
            ctrlRegistro = IControladorRegistro.getInstance();
            ctrlEvento = IControladorEvento.getInstance();
        } catch (Exception e) {
            throw new ServletException("Error al inicializar controladores", e);
        }
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }
        
        String nickname = (String) session.getAttribute("usuario");
        String role = (String) session.getAttribute("role");
        
        // Verificar que sea asistente
        if (!"asistente".equals(role)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Acceso denegado: Solo asistentes pueden acceder a esta función");
            return;
        }
        
        try {
            // Obtener todos los registros del asistente
            Set<DTRegistro> misRegistros = ctrlRegistro.listarRegistrosPorAsistente(nickname);
            
            // Agrupar por edición para mostrar una lista limpia
            Set<String> edicionesRegistradas = misRegistros.stream()
                .map(DTRegistro::getnomEdicion)
                .collect(Collectors.toCollection(LinkedHashSet::new));
            
            // Obtener categorías para el sidebar
            Set<String> categorias = ctrlEvento.listarCategorias();
            
            request.setAttribute("edicionesRegistradas", edicionesRegistradas);
            request.setAttribute("misRegistros", misRegistros);
            request.setAttribute("categorias", categorias);
            
            // Pasar información de la sesión al JSP
            request.setAttribute("nickname", nickname);
            request.setAttribute("role", role);
            request.setAttribute("avatar", request.getSession().getAttribute("avatar"));
            
            request.getRequestDispatcher("/WEB-INF/views/misRegistros.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error al cargar mis registros: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
        }
    }
}