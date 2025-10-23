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
import logica.controladores.IControladorRegistro;
import logica.datatypesyenum.DTTipoDeRegistro;
import logica.datatypesyenum.DTEdicion;
import excepciones.EdicionNoExisteException;
import utils.Utils;

@WebServlet("/consultaTipoRegistro")
public class ConsultaTipoRegistroServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	
    	request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
    	
        try {
            // Obtener parámetros
            String tipoRegistro = request.getParameter("tipo");
            String edicion = request.getParameter("edicion");
            
            if (tipoRegistro == null || edicion == null || 
                tipoRegistro.trim().isEmpty() || edicion.trim().isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/inicio");
                return;
            }

            // Limpiar espacios en blanco
            tipoRegistro = tipoRegistro.trim();
            edicion = edicion.trim();

            // Obtener controladores
            IControladorRegistro ctrlRegistro = IControladorRegistro.getInstance();
            IControladorEvento ctrlEvento = IControladorEvento.getInstance();

            // Obtener información del tipo de registro
            DTTipoDeRegistro tipoInfo = ctrlRegistro.consultaTipoDeRegistro(edicion, tipoRegistro);
            
            if (tipoInfo == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Tipo de registro no encontrado");
                return;
            }

            // Obtener información de la edición
            DTEdicion edicionInfo = ctrlEvento.consultarEdicion(edicion);
            
            // Obtener todas las categorías para el sidebar (ordenadas alfabéticamente)
            Set<String> categoriasSet = ctrlEvento.listarCategorias();
            List<String> categorias = new ArrayList<>(categoriasSet);
            Collections.sort(categorias);

            // Verificar si el usuario logueado es asistente
            String role = (String) request.getSession().getAttribute("role");
            String nickname = (String) request.getSession().getAttribute("usuario");
            boolean esAsistente = "asistente".equals(role);
            boolean yaRegistrado = false;
            
            if (esAsistente && nickname != null) {
                try {
                    yaRegistrado = ctrlRegistro.estaRegistrado(edicion, nickname);
                } catch (Exception e) {
                    // Si hay error verificando, asumimos que no está registrado
                    yaRegistrado = false;
                }
            }

            // Pasar los datos a la JSP
            request.setAttribute("tipoRegistro", tipoInfo);
            request.setAttribute("edicionInfo", edicionInfo);
            request.setAttribute("categorias", categorias);
            request.setAttribute("esAsistente", esAsistente);
            request.setAttribute("yaRegistrado", yaRegistrado);

            // Obtener el rol desde la sesión y pasarlo a la JSP
            request.setAttribute("role", role);
            request.setAttribute("nickname", nickname);
            request.setAttribute("avatar", request.getSession().getAttribute("avatar"));

            request.getRequestDispatcher("/WEB-INF/views/consultaTipoRegistro.jsp").forward(request, response);

        } catch (Exception e) {
            throw new ServletException("Error obteniendo información del tipo de registro", e);
        }
    }
}
