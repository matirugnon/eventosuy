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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
            
            // Verificar cuáles ediciones ya finalizaron
            LocalDate hoy = LocalDate.now();
            Map<String, Boolean> edicionesPasadas = new HashMap<>();
            
            for (DTEdicion edicion : edicionesOrganizadas) {
                if (edicion.getFechaFin() != null) {
                    LocalDate fechaFin = LocalDate.of(
                        edicion.getFechaFin().getAnio(),
                        edicion.getFechaFin().getMes(),
                        edicion.getFechaFin().getDia()
                    );
                    // La edición está finalizada si la fecha de fin es anterior a hoy
                    boolean esPasada = fechaFin.isBefore(hoy);
                    edicionesPasadas.put(edicion.getNombre(), esPasada);
                } else {
                    edicionesPasadas.put(edicion.getNombre(), false);
                }
            }
            
            // Obtener categorías para el sidebar (ordenadas alfabéticamente)
            Set<String> categoriasSet = ctrlEvento.listarCategorias();
            List<String> categorias = new ArrayList<>(categoriasSet);
            Collections.sort(categorias);
            
            request.setAttribute("edicionesOrganizadas", edicionesOrganizadas);
            request.setAttribute("edicionesPasadas", edicionesPasadas);
            request.setAttribute("categorias", categorias);
            
            request.getRequestDispatcher("/WEB-INF/views/edicionesOrganizadas.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error al cargar las ediciones organizadas: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
        }
    }
}