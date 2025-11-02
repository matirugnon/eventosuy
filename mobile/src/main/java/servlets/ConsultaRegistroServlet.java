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
import servlets.dto.EdicionDetalleDTO;
import soap.DtRegistro;
import soap.PublicadorControlador;
import soap.PublicadorRegistro;
import utils.SoapClientHelper;



@WebServlet("/consultaRegistro")
public class ConsultaRegistroServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	
    	request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
    	
        // Verificar que el usuario esté logueado y sea asistente
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        String role = (String) session.getAttribute("role");
        if (!"asistente".equals(role)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
    	
        try {
            // Obtener parÃ¡metros
            String asistente = request.getParameter("asistente");
            String edicion = request.getParameter("edicion");
            String tipoRegistro = request.getParameter("tipoRegistro");
            
            if (asistente == null || edicion == null || tipoRegistro == null ||
                asistente.trim().isEmpty() || edicion.trim().isEmpty() || tipoRegistro.trim().isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/inicio");
                return;
            }
            
            
            PublicadorControlador publicadorEv = SoapClientHelper.getPublicadorControlador();
            PublicadorRegistro publicadorReg = SoapClientHelper.getPublicadorRegistro();
         
            // Verificar que el usuario tenga permisos para ver este registro
            String usuarioSesion = (String) request.getSession().getAttribute("usuario");
            String rolSesion = (String) request.getSession().getAttribute("role");
            
            // Verificar permisos
            boolean tienePermisos = false;
            
            if (usuarioSesion != null) {
                // Caso 1: Es el mismo asistente que estÃ¡ registrado
                if (asistente.equals(usuarioSesion) && "asistente".equals(rolSesion)) {
                    tienePermisos = true;
                }
                // Caso 2: Es el organizador especÃ­fico de esta ediciÃ³n
                else if ("organizador".equals(rolSesion)) {
                    try {
                        EdicionDetalleDTO edicionInfo = new EdicionDetalleDTO(publicadorEv.obtenerDetalleCompletoEdicion(edicion));
                        // Solo permitir si el usuario logueado es exactamente el organizador de esta ediciÃ³n
                        if (edicionInfo != null && usuarioSesion.equals(edicionInfo.getOrganizador())) {
                            tienePermisos = true;
                        }
                    } catch (Exception e) {
                        // Si hay error obteniendo la ediciÃ³n, no dar permisos
                        tienePermisos = false;
                    }
                }
                // Caso 3: NingÃºn otro rol tiene permisos (visitantes, otros organizadores, etc.)
            }
            
            if (!tienePermisos) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "No tienes permisos para ver este registro");
                return;
            }

            // Obtener el registro especÃ­fico
            // Primero obtenemos todos los registros del asistente
            Set<DtRegistro> registros = new HashSet<>(publicadorReg.listarRegistrosPorAsistente(asistente).getItem());
            DtRegistro registro = null;
            
            // Buscar el registro especÃ­fico por ediciÃ³n y tipo de registro
            for (DtRegistro reg : registros) {
                if (reg.getNomEdicion().equals(edicion) && reg.getTipoDeRegistro().equals(tipoRegistro)) {
                    registro = reg;
                    break;
                }
            }
            
            if (registro == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Registro no encontrado");
                return;
            }

            // Obtener informaciÃ³n de la ediciÃ³n
            EdicionDetalleDTO edicionInfo = new EdicionDetalleDTO(publicadorEv.obtenerDetalleEvento(edicion));
            
            // Obtener todas las categorÃ­as para el sidebar (ordenadas alfabÃ©ticamente)
            Set<String> categoriasSet = new HashSet<>(publicadorEv.listarCategorias().getItem());
            List<String> categorias = new ArrayList<>(categoriasSet);
            Collections.sort(categorias);

            // Pasar los datos a la JSP
            request.setAttribute("registro", registro);
            request.setAttribute("edicionInfo", edicionInfo);
            request.setAttribute("categorias", categorias);

            // Obtener el rol desde la sesiÃ³n y pasarlo a la JSP (role ya fue definido al inicio del método)
            String nickname = (String) session.getAttribute("usuario");
            String avatar = (String) session.getAttribute("avatar");
            
            // Debug temporal
            System.out.println("DEBUG ConsultaRegistroServlet - role: " + role);
            System.out.println("DEBUG ConsultaRegistroServlet - nickname: " + nickname);
            System.out.println("DEBUG ConsultaRegistroServlet - avatar: " + avatar);
            
            request.setAttribute("role", role);
            request.setAttribute("nickname", nickname);
            request.setAttribute("avatar", avatar);

            request.getRequestDispatcher("/WEB-INF/views/consultaRegistro.jsp").forward(request, response);

        } catch (Exception e) {
            throw new ServletException("Error obteniendo informaciÃ³n del registro", e);
        }
    }
}

