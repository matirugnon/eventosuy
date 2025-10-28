package servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
import soap.DtEdicion;
//nuevos imports (copiar este bloque)
import soap.PublicadorControlador;
import soap.StringArray;
import utils.SoapClientHelper;


@WebServlet("/consultaEdicion")
public class ConsultaEdicionServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	
    	request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        
        try {

            PublicadorControlador publicador = SoapClientHelper.getPublicadorControlador();

            // Obtener el nombre de la ediciÃ³n desde el parÃ¡metro
            String nombreEdicion = request.getParameter("edicion");

            
            if (nombreEdicion == null || nombreEdicion.trim().isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/inicio");
                return;
            }

            // Obtener información de la edición específica
            DtEdicion edicionDt = publicador.consultarEdicion(nombreEdicion);

            if (edicionDt == null) {
                response.sendRedirect(request.getContextPath() + "/inicio");
                return;
            }
            
            // Obtener información del evento padre
            String nombreEventoPadre = publicador.obtenerEventoDeEdicion(nombreEdicion);
            
            // Obtener información del organizador
            String nicknameOrganizador = edicionDt.getOrganizador();
            DTUsuario organizador = null; 
            String avatarOrganizador = "/img/usSinFoto.webp"; // Avatar por defecto
            
            if (nicknameOrganizador != null && !nicknameOrganizador.trim().isEmpty()) {
                try {
                    soap.PublicadorUsuario publicadorUsuario = SoapClientHelper.getPublicadorUsuario();
                    avatarOrganizador = publicadorUsuario.obtenerAvatar(nicknameOrganizador);
                    if (avatarOrganizador == null || avatarOrganizador.trim().isEmpty()) {
                        avatarOrganizador = "/img/usSinFoto.webp";
                    }
                } catch (Exception e) {
                    System.err.println("Error obteniendo avatar del organizador: " + e.getMessage());
                    avatarOrganizador = "/img/usSinFoto.webp";
                }
            }

            // Obtener todas las categorías para el sidebar (ordenadas alfabéticamente)
            StringArray categoriasSet = publicador.listarCategorias();
            List<String> categorias = new ArrayList<>(categoriasSet.getItem());
            Collections.sort(categorias);

            // Pasar los datos a la JSP
            request.setAttribute("edicion", edicionDt);
            request.setAttribute("eventoPadre", nombreEventoPadre);
            request.setAttribute("organizador", organizador);
            request.setAttribute("avatarOrganizador", avatarOrganizador);
            request.setAttribute("categorias", categorias);

            // Obtener el rol desde la sesiÃ³n y pasarlo a la JSP
            String role = (String) request.getSession().getAttribute("role");
            request.setAttribute("role", role);
            request.setAttribute("nickname", request.getSession().getAttribute("usuario"));
            request.setAttribute("avatar", request.getSession().getAttribute("avatar"));

            request.getRequestDispatcher("/WEB-INF/views/consultaEdicion.jsp").forward(request, response);

        } catch (Exception e) {
            throw new ServletException("Error obteniendo informaciÃ³n de la ediciÃ³n", e);
        }
    }
}

