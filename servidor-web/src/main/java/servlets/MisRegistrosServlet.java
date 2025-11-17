package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import soap.DtRegistro;
import soap.PublicadorControlador;
import soap.PublicadorRegistro;
import utils.SoapClientHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.LinkedHashSet;
import java.util.stream.Collectors;

@WebServlet("/misRegistros")
public class MisRegistrosServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private PublicadorRegistro publicadorReg;
	private PublicadorControlador publicadorEv;
    
    @Override
    public void init() throws ServletException {
        try {
        	 publicadorReg = SoapClientHelper.getPublicadorRegistro();
        	 publicadorEv = SoapClientHelper.getPublicadorControlador();
            
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
        
        // Verificar que sea asistente
        if (!"asistente".equals(role)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Acceso denegado: Solo asistentes pueden acceder a esta funciÃ³n");
            return;
        }
        
        try {
            // Obtener todos los registros del asistente
            Set<DtRegistro> misRegistros = new HashSet<>(publicadorReg.listarRegistrosPorAsistente(nickname).getItem());
            
            // Agrupar por ediciÃ³n para mostrar una lista limpia
            Set<String> edicionesRegistradas = misRegistros.stream()
                .map(DtRegistro::getNomEdicion)
                .collect(Collectors.toCollection(LinkedHashSet::new));
            
            // Obtener categorÃ­as para el sidebar (ordenadas alfabÃ©ticamente)
            Set<String> categoriasSet = new HashSet<>(publicadorEv.listarCategorias().getItem());
            List<String> categorias = new ArrayList<>(categoriasSet);
            Collections.sort(categorias);
            
            request.setAttribute("edicionesRegistradas", edicionesRegistradas);
            request.setAttribute("misRegistros", misRegistros);
            request.setAttribute("categorias", categorias);
            
            // Pasar informaciÃ³n de la sesiÃ³n al JSP
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

