package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import logica.Controladores.IControladorEvento;
import logica.Controladores.IControladorRegistro;
import logica.DatatypesYEnum.DTEdicion;
import logica.DatatypesYEnum.EstadoEdicion;
import excepciones.NombreTipoRegistroDuplicadoException;

import java.io.IOException;
import java.util.Set;

@WebServlet("/altaTipoRegistro")
public class AltaTipoRegistroServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private IControladorEvento ctrlEvento;
    private IControladorRegistro ctrlRegistro;
    
    @Override
    public void init() throws ServletException {
        try {
            ctrlEvento = IControladorEvento.getInstance();
            ctrlRegistro = IControladorRegistro.getInstance();
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
        
        String edicionNombre = request.getParameter("edicion");
        
        if (edicionNombre == null || edicionNombre.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/edicionesOrganizadas");
            return;
        }
        
        try {
            System.out.println("DEBUG: Iniciando doGet para usuario: " + nickname + ", edicion: " + edicionNombre);
            
            // Verificar que el organizador tiene ediciones organizadas y que esta edición le pertenece
            Set<DTEdicion> edicionesOrganizadas = ctrlEvento.listarEdicionesOrganizadasPorEstado(nickname, EstadoEdicion.ACEPTADA);
            System.out.println("DEBUG: Ediciones organizadas obtenidas: " + (edicionesOrganizadas != null ? edicionesOrganizadas.size() : "null"));
            
            DTEdicion edicion = edicionesOrganizadas.stream()
                .filter(ed -> ed.getNombre().equals(edicionNombre))
                .findFirst()
                .orElse(null);
                
            System.out.println("DEBUG: Edicion encontrada: " + (edicion != null ? edicion.getNombre() : "null"));
            
            if (edicion == null) {
                System.out.println("DEBUG: Edicion no encontrada o no pertenece al organizador");
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "No tienes permisos para crear tipos de registro en esta edición: " + edicionNombre);
                return;
            }
            
            // Obtener categorías para el sidebar
            Set<String> categorias = ctrlEvento.listarCategorias();
            System.out.println("DEBUG: Categorias obtenidas: " + (categorias != null ? categorias.size() : "null"));
            
            request.setAttribute("edicion", edicion);
            request.setAttribute("categorias", categorias);
            
            System.out.println("DEBUG: Redirigiendo a altaTipoRegistro.jsp");
            request.getRequestDispatcher("/WEB-INF/views/altaTipoRegistro.jsp").forward(request, response);
            
        } catch (Exception e) {
            System.err.println("ERROR en AltaTipoRegistroServlet.doGet: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "Error al cargar la información de la edición: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
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
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Acceso denegado");
            return;
        }
        
        try {
            String edicionNombre = request.getParameter("edicionNombre");
            String nombre = request.getParameter("nombre");
            String descripcion = request.getParameter("descripcion");
            String costoStr = request.getParameter("costo");
            String cupoStr = request.getParameter("cupo");
            
            // Validaciones básicas
            if (edicionNombre == null || nombre == null || descripcion == null || 
                costoStr == null || cupoStr == null ||
                edicionNombre.trim().isEmpty() || nombre.trim().isEmpty() || 
                descripcion.trim().isEmpty() || costoStr.trim().isEmpty() || cupoStr.trim().isEmpty()) {
                
                request.setAttribute("error", "Todos los campos son obligatorios");
                doGet(request, response);
                return;
            }
            
            double costo;
            int cupo;
            
            try {
                costo = Double.parseDouble(costoStr.trim());
                cupo = Integer.parseInt(cupoStr.trim());
                
                if (costo < 0) {
                    request.setAttribute("error", "El costo no puede ser negativo");
                    doGet(request, response);
                    return;
                }
                
                if (cupo <= 0) {
                    request.setAttribute("error", "El cupo debe ser mayor a cero");
                    doGet(request, response);
                    return;
                }
                
            } catch (NumberFormatException e) {
                request.setAttribute("error", "Costo y cupo deben ser valores numéricos válidos");
                doGet(request, response);
                return;
            }
            
            // Verificar que el organizador puede crear tipos de registro en esta edición
            Set<DTEdicion> edicionesOrganizadas = ctrlEvento.listarEdicionesOrganizadasPorEstado(nickname, EstadoEdicion.ACEPTADA);
            
            boolean esPropia = edicionesOrganizadas.stream()
                .anyMatch(ed -> ed.getNombre().equals(edicionNombre));
            
            if (!esPropia) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "No tienes permisos para crear tipos de registro en esta edición");
                return;
            }
            
            // Intentar crear el tipo de registro
            System.out.println("DEBUG: Creando tipo de registro - Edicion: " + edicionNombre + ", Nombre: " + nombre.trim() + ", Costo: " + costo + ", Cupo: " + cupo);
            ctrlRegistro.altaTipoDeRegistro(edicionNombre, nombre.trim(), descripcion.trim(), costo, cupo);
            System.out.println("DEBUG: Tipo de registro creado exitosamente");
            
            request.setAttribute("success", "Tipo de registro '" + nombre.trim() + "' creado exitosamente");
            request.setAttribute("nombreTipoRegistro", nombre.trim());
            
            doGet(request, response);
            
        } catch (NombreTipoRegistroDuplicadoException e) {
            request.setAttribute("error", "Ya existe un tipo de registro con ese nombre para esta edición. Por favor, elige otro nombre.");
            doGet(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error al crear el tipo de registro: " + e.getMessage());
            doGet(request, response);
        }
    }
}