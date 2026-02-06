package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import soap.DtEdicion;
import soap.PublicadorControlador;
import soap.PublicadorRegistro;
import soap.StringArray;
import utils.SoapClientHelper;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@WebServlet("/altaTipoRegistro")
public class AltaTipoRegistroServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private PublicadorControlador publicadorCtrl; // SOAP para consultar edición/categorías
    private PublicadorRegistro publicadorReg; // SOAP para crear tipo de registro
    
    @Override
    public void init() throws ServletException {
        try {
            publicadorCtrl = SoapClientHelper.getPublicadorControlador();
            publicadorReg = SoapClientHelper.getPublicadorRegistro();            // PublicadorRegistro comparte el mismo service package; obtenerlo del Service
        } catch (Exception e) {
            throw new ServletException("Error al inicializar controladores/servicios", e);
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
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Acceso denegado: Solo organizadores pueden acceder a esta funciÃ³n");
            return;
        }
        
        String edicionNombre = request.getParameter("edicion");
        
        if (edicionNombre == null || edicionNombre.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/edicionesOrganizadas");
            return;
        }
        
        try {
            
            // Consultar la edición vía SOAP (fuente de verdad del servidor central)
            DtEdicion edicion = publicadorCtrl.consultarEdicion(edicionNombre);
            
            if (edicion == null) {
                System.out.println("DEBUG: Edicion no encontrada");
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "EdiciÃ³n no encontrada: " + edicionNombre);
                return;
            }

            if (edicion.getOrganizador() == null || !edicion.getOrganizador().equals(nickname)) {
                System.out.println("DEBUG: Edicion no pertenece al organizador en sesiÃ³n");
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "No tienes permisos para crear tipos de registro en esta ediciÃ³n: " + edicionNombre);
                return;
            }
            
            // Verificar que la ediciÃ³n no haya finalizado
            if (edicion.getFechaFin() != null) {
                LocalDate hoy = LocalDate.now();
                LocalDate fechaFin = LocalDate.of(
                    edicion.getFechaFin().getAnio(),
                    edicion.getFechaFin().getMes(),
                    edicion.getFechaFin().getDia()
                );
                
                if (fechaFin.isBefore(hoy)) {
                    session.setAttribute("datosMensaje", "âŒ No se puede dar de alta tipos de registro para una ediciÃ³n que ya finalizÃ³");
                    session.setAttribute("datosMensajeTipo", "error");
                    response.sendRedirect(request.getContextPath() + "/edicionesOrganizadas");
                    return;
                }
            }
            
            // Obtener categorías para el sidebar (ordenadas alfabéticamente) desde SOAP
            StringArray categoriasArray = publicadorCtrl.listarCategorias();
            List<String> categorias = new ArrayList<>();
            if (categoriasArray != null && categoriasArray.getItem() != null) {
                categorias.addAll(categoriasArray.getItem());
            }
            Collections.sort(categorias);
            
            request.setAttribute("edicion", edicion);
            request.setAttribute("categorias", categorias);
            
            // Pasar datos de sesiÃ³n al JSP
            request.setAttribute("nickname", nickname);
            request.setAttribute("avatar", session.getAttribute("avatar"));
            request.setAttribute("role", session.getAttribute("role"));
            request.setAttribute("nombre", session.getAttribute("nombre"));
            
            System.out.println("DEBUG: Redirigiendo a altaTipoRegistro.jsp");
            request.getRequestDispatcher("/WEB-INF/views/altaTipoRegistro.jsp").forward(request, response);
            
        } catch (Exception e) {
            System.err.println("ERROR en AltaTipoRegistroServlet.doGet: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "Error al cargar la informaciÃ³n de la ediciÃ³n: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
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
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Acceso denegado");
            return;
        }
        
        
            String edicionNombre = request.getParameter("edicionNombre");
            String nombre = request.getParameter("nombre");
            String descripcion = request.getParameter("descripcion");
            String costoStr = request.getParameter("costo");
            String cupoStr = request.getParameter("cupo");
            
            // Validaciones bÃ¡sicas
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
                request.setAttribute("error", "Costo y cupo deben ser valores numÃ©ricos vÃ¡lidos");
                doGet(request, response);
                return;
            }
            
            // Verificar pertenencia por organizador directo de la edición vía SOAP
            DtEdicion ed = publicadorCtrl.consultarEdicion(edicionNombre);
            if (ed == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "EdiciÃ³n no encontrada");
                return;
            }
            if (ed.getOrganizador() == null || !ed.getOrganizador().equals(nickname)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "No tienes permisos para crear tipos de registro en esta ediciÃ³n");
                return;
            }
            
            // Intentar crear el tipo de registro

            String res = publicadorReg.altaTipoDeRegistro(edicionNombre, nombre.trim(), descripcion.trim(), costo, cupo);
            if (res != null && !"OK".equals(res.trim())) {
                // Preparar atributos y mostrar el formulario con el mensaje de error
                request.setAttribute("error", res);

                // Reusar la DtEdicion ya consultada (ed) para el JSP
                request.setAttribute("edicion", ed);

                // Obtener categorías para el sidebar (igual que en doGet)
                StringArray categoriasArray2 = publicadorCtrl.listarCategorias();
                List<String> categorias2 = new ArrayList<>();
                if (categoriasArray2 != null && categoriasArray2.getItem() != null) {
                    categorias2.addAll(categoriasArray2.getItem());
                }
                Collections.sort(categorias2);
                request.setAttribute("categorias", categorias2);

                // Pasar datos de sesión para que el header/usuario se muestren correctamente
                request.setAttribute("nickname", nickname);
                request.setAttribute("avatar", session.getAttribute("avatar"));
                request.setAttribute("role", session.getAttribute("role"));
                request.setAttribute("nombre", session.getAttribute("nombre"));

                // Mantener los valores ingresados en el formulario usando param (ya están en request)
                request.getRequestDispatcher("/WEB-INF/views/altaTipoRegistro.jsp").forward(request, response);
                return;
            }

            // Redirigir con mensaje de éxito
            session.setAttribute("datosMensaje", "El tipo de registro '" + nombre.trim() + "' fue creado exitosamente para la edición '" + edicionNombre + "'");
            session.setAttribute("datosMensajeTipo", "info");
            response.sendRedirect(request.getContextPath() + "/edicionesOrganizadas");
    }
}

