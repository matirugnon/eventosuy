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
import logica.datatypesyenum.DTRegistro;
import logica.datatypesyenum.DTEdicion;
import excepciones.UsuarioNoExisteException;
import utils.Utils;

@WebServlet("/consultaRegistro")
public class ConsultaRegistroServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	
    	request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
    	
        try {
            // Obtener parámetros
            String asistente = request.getParameter("asistente");
            String edicion = request.getParameter("edicion");
            String tipoRegistro = request.getParameter("tipoRegistro");
            
            if (asistente == null || edicion == null || tipoRegistro == null ||
                asistente.trim().isEmpty() || edicion.trim().isEmpty() || tipoRegistro.trim().isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/inicio");
                return;
            }

            IControladorEvento ctrlEvento = IControladorEvento.getInstance();
            IControladorRegistro ctrlRegistro = IControladorRegistro.getInstance();

            // Verificar que el usuario tenga permisos para ver este registro
            String usuarioSesion = (String) request.getSession().getAttribute("usuario");
            String rolSesion = (String) request.getSession().getAttribute("role");
            
            // Verificar permisos
            boolean tienePermisos = false;
            
            if (usuarioSesion != null) {
                // Caso 1: Es el mismo asistente que está registrado
                if (asistente.equals(usuarioSesion) && "asistente".equals(rolSesion)) {
                    tienePermisos = true;
                }
                // Caso 2: Es el organizador específico de esta edición
                else if ("organizador".equals(rolSesion)) {
                    try {
                        DTEdicion edicionInfo = ctrlEvento.consultarEdicion(edicion);
                        // Solo permitir si el usuario logueado es exactamente el organizador de esta edición
                        if (edicionInfo != null && usuarioSesion.equals(edicionInfo.getOrganizador())) {
                            tienePermisos = true;
                        }
                    } catch (Exception e) {
                        // Si hay error obteniendo la edición, no dar permisos
                        tienePermisos = false;
                    }
                }
                // Caso 3: Ningún otro rol tiene permisos (visitantes, otros organizadores, etc.)
            }
            
            if (!tienePermisos) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "No tienes permisos para ver este registro");
                return;
            }

            // Obtener el registro específico
            // Primero obtenemos todos los registros del asistente
            Set<DTRegistro> registros = ctrlRegistro.listarRegistrosPorAsistente(asistente);
            DTRegistro registro = null;
            
            // Buscar el registro específico por edición y tipo de registro
            for (DTRegistro reg : registros) {
                if (reg.getnomEdicion().equals(edicion) && reg.getTipoDeRegistro().equals(tipoRegistro)) {
                    registro = reg;
                    break;
                }
            }
            
            if (registro == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Registro no encontrado");
                return;
            }

            // Obtener información de la edición
            DTEdicion edicionInfo = ctrlEvento.consultarEdicion(edicion);
            
            // Obtener todas las categorías para el sidebar (ordenadas alfabéticamente)
            Set<String> categoriasSet = ctrlEvento.listarCategorias();
            List<String> categorias = new ArrayList<>(categoriasSet);
            Collections.sort(categorias);

            // Pasar los datos a la JSP
            request.setAttribute("registro", registro);
            request.setAttribute("edicionInfo", edicionInfo);
            request.setAttribute("categorias", categorias);

            // Obtener el rol desde la sesión y pasarlo a la JSP
            String role = (String) request.getSession().getAttribute("role");
            String nickname = (String) request.getSession().getAttribute("usuario");
            String avatar = (String) request.getSession().getAttribute("avatar");
            
            // Debug temporal
            System.out.println("DEBUG ConsultaRegistroServlet - role: " + role);
            System.out.println("DEBUG ConsultaRegistroServlet - nickname: " + nickname);
            System.out.println("DEBUG ConsultaRegistroServlet - avatar: " + avatar);
            
            request.setAttribute("role", role);
            request.setAttribute("nickname", nickname);
            request.setAttribute("avatar", avatar);

            request.getRequestDispatcher("/WEB-INF/views/consultaRegistro.jsp").forward(request, response);

        } catch (Exception e) {
            throw new ServletException("Error obteniendo información del registro", e);
        }
    }
}
