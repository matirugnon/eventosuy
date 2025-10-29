package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

// removed unused imports (use soap.DtEdicion instead)
import utils.SoapClientHelper;
import soap.PublicadorControlador;
import soap.PublicadorUsuario;
import soap.StringArray;
import soap.DtEdicion;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/edicionesOrganizadas")
public class EdicionesOrganizadasServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    
    // init not required: we obtain SOAP clients lazily in doGet
    
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
            //a partir de aca se puede usar el publicador
            PublicadorControlador publicadorControlador = SoapClientHelper.getPublicadorControlador();
            PublicadorUsuario publicadorUsuario = SoapClientHelper.getPublicadorUsuario();


            // Obtener ediciones organizadas por este usuario (nombres) y convertir a DtEdicion
            StringArray edicionesNombres = publicadorUsuario.listarEdicionesOrganizador(nickname);
            List<DtEdicion> edicionesOrganizadas = new ArrayList<>();
            if (edicionesNombres != null && edicionesNombres.getItem() != null) {
                for (String nom : edicionesNombres.getItem()) {
                    try {
                        DtEdicion d = publicadorControlador.consultarEdicion(nom);
                        if (d != null) edicionesOrganizadas.add(d);
                    } catch (Exception ex) {
                        // ignorar ediciones que no se puedan obtener
                        System.err.println("Error obteniendo DtEdicion para: " + nom + " -> " + ex.getMessage());
                    }
                }
            }

            // Verificar cuáles ediciones ya finalizaron
            LocalDate hoy = LocalDate.now();
            Map<String, Boolean> edicionesPasadas = new HashMap<>();
            for (DtEdicion edicion : edicionesOrganizadas) {
                if (edicion.getFechaFin() != null) {
                    LocalDate fechaFin = LocalDate.of(
                        edicion.getFechaFin().getAnio(),
                        edicion.getFechaFin().getMes(),
                        edicion.getFechaFin().getDia()
                    );
                    boolean esPasada = fechaFin.isBefore(hoy);
                    edicionesPasadas.put(edicion.getNombre(), esPasada);
                } else {
                    edicionesPasadas.put(edicion.getNombre(), false);
                }
            }

            // Obtener categorías para el sidebar (ordenadas alfabéticamente) vía SOAP
            StringArray categoriasArray = publicadorControlador.listarCategorias();
            List<String> categorias = new ArrayList<>();
            if (categoriasArray != null && categoriasArray.getItem() != null) {
                categorias.addAll(categoriasArray.getItem());
            }
            Collections.sort(categorias);
            
            request.setAttribute("edicionesOrganizadas", edicionesOrganizadas);
            request.setAttribute("edicionesPasadas", edicionesPasadas);
            request.setAttribute("categorias", categorias);
            request.setAttribute("nickname", nickname);
            
            request.getRequestDispatcher("/WEB-INF/views/edicionesOrganizadas.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error al cargar las ediciones organizadas: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
        }
    }
}

