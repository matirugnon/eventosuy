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
import soap.PublicadorControlador;
import soap.PublicadorUsuario;
import soap.PublicadorRegistro;
import soap.DtEdicion;
import soap.DtRegistro;
import soap.DtRegistroArray;
import soap.StringArray;
import utils.SoapClientHelper;
import utils.SoapClientHelper;



@WebServlet(urlPatterns = {"/registrosEdicion", "/registrosEdicion/*"})
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
        // Support path-based parameter: /registrosEdicion/{edicion}
        if (edicion == null || edicion.trim().isEmpty()) {
            String pathInfo = request.getPathInfo();
            if (pathInfo != null && pathInfo.length() > 1) {
                // pathInfo starts with '/'
                try {
                    edicion = java.net.URLDecoder.decode(pathInfo.substring(1), "UTF-8");
                } catch (Exception ex) {
                    edicion = pathInfo.substring(1);
                }
            }
        }
        if (edicion == null || edicion.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/edicionesOrganizadas");
            return;
        }

        try {
            // Obtener clientes SOAP
            PublicadorControlador publicadorEv = SoapClientHelper.getPublicadorControlador();
            PublicadorUsuario publicadorUser = SoapClientHelper.getPublicadorUsuario();
            PublicadorRegistro publicadorReg = SoapClientHelper.getPublicadorRegistro();

            // Usar los tipos Dt que provee el SOAP
            DtEdicion dtEd = publicadorEv.consultarEdicion(edicion);

            if (dtEd == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Edición no encontrada");
                return;
            }

            // Verificar que el organizador de la edición coincide con el usuario en sesión
            if (dtEd.getOrganizador() == null || !dtEd.getOrganizador().equals(nickname)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Acceso denegado: No sos el organizador de esta edición");
                return;
            }

            // Obtener categorías para el sidebar (ordenadas alfabéticamente)
            Set<String> categoriasSet = new HashSet(publicadorEv.listarCategorias().getItem());
            List<String> categorias = new ArrayList<>(categoriasSet);
            Collections.sort(categorias);

            // Construir la lista de registros de la edición: iteramos asistentes y filtramos
            List<DtRegistro> registros = new ArrayList<>();
            StringArray asistentesArr = publicadorUser.listarAsistentes();
            if (asistentesArr != null && asistentesArr.getItem() != null) {
                for (String asist : asistentesArr.getItem()) {
                    try {
                        DtRegistroArray regsArray = publicadorReg.listarRegistrosPorAsistente(asist);
                        if (regsArray != null && regsArray.getItem() != null) {
                            for (DtRegistro r : regsArray.getItem()) {
                                if (r != null && edicion.equals(r.getNomEdicion())) {
                                    registros.add(r);
                                }
                            }
                        }
                    } catch (Exception ex) {
                        // Ignorar asistentes que causen error al consultar registros individuales
                        System.err.println("Warning: no se pudieron obtener registros para asistente " + asist + ": " + ex.getMessage());
                    }
                }
            }

            request.setAttribute("edicion", dtEd);
            request.setAttribute("registros", registros);
            request.setAttribute("categorias", categorias);

            // Pasar datos de sesión al JSP
            request.setAttribute("nickname", nickname);
            request.setAttribute("role", role);
            request.setAttribute("avatar", session.getAttribute("avatar"));
            request.setAttribute("nombre", session.getAttribute("nombre"));

            request.getRequestDispatcher("/WEB-INF/views/registrosEdicion.jsp").forward(request, response);

        } catch (Exception e) {
            throw new ServletException("Error cargando registros de la edición", e);
        }
    }
}

