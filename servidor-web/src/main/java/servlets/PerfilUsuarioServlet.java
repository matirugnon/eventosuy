package servlets;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import soap.PublicadorUsuario;
import utils.SoapClientHelper;

@WebServlet({"/perfilUsuario", "/miPerfil"})
public class PerfilUsuarioServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
    
        try {
            String requestPath = request.getServletPath();
            String nickname;
            HttpSession session = request.getSession(false);
            
            if ("/miPerfil".equals(requestPath)) {
                if (session == null || session.getAttribute("usuario") == null) {
                    response.sendRedirect(request.getContextPath() + "/login");
                    return;
                }
                nickname = (String) session.getAttribute("usuario");
            } else {
                nickname = request.getParameter("nickname");
                if (nickname == null || nickname.trim().isEmpty()) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Nickname no especificado");
                    return;
                }
            }
            
            PublicadorUsuario publicador = SoapClientHelper.getPublicadorUsuario();
            String tipoUsuario = publicador.obtenerTipoUsuario(nickname);
            
            if ("desconocido".equals(tipoUsuario)) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Usuario no encontrado");
                return;
            }
            
            String avatar = publicador.obtenerAvatar(nickname);
            String email = publicador.obtenerEmail(nickname);
            String nombre = publicador.obtenerNombre(nickname);
            
            // Crear un objeto simple para el JSP
            servlets.dto.UsuarioDTO usuario = new servlets.dto.UsuarioDTO();
            usuario.setNickname(nickname);
            usuario.setAvatar(avatar);
            usuario.setEmail(email);
            usuario.setNombre(nombre);
            
            request.setAttribute("usuario", usuario);
            request.setAttribute("nombre", nombre);
            request.setAttribute("nickname", nickname);
            request.setAttribute("tipoUsuario", tipoUsuario);
            request.setAttribute("avatar", avatar);
            
            // Obtener registros si es asistente
            if ("asistente".equals(tipoUsuario)) {
                try {
                    soap.StringArray registrosDetallados = publicador.obtenerRegistrosDetallados(nickname);
                    
                    // Separar registros por estado
                    java.util.List<servlets.dto.RegistroEdicionDTO> edicionesAceptadas = new java.util.ArrayList<>();
                    java.util.List<servlets.dto.RegistroEdicionDTO> edicionesIngresadas = new java.util.ArrayList<>();
                    java.util.List<servlets.dto.RegistroEdicionDTO> edicionesRechazadas = new java.util.ArrayList<>();
                    
                    if (registrosDetallados != null && registrosDetallados.getItem() != null) {
                        for (String regStr : registrosDetallados.getItem()) {
                            String[] parts = regStr.split("\\|");
                            if (parts.length >= 7) {
                                servlets.dto.RegistroEdicionDTO edicion = new servlets.dto.RegistroEdicionDTO();
                                edicion.setEvento(parts[0]);
                                edicion.setNombre(parts[1]);
                                edicion.setSigla(parts[2]);
                                String estado = parts[3];
                                edicion.setEstado(estado);
                                
                                // Clasificar por estado
                                if ("ACEPTADA".equals(estado)) {
                                    edicionesAceptadas.add(edicion);
                                } else if ("INGRESADA".equals(estado)) {
                                    edicionesIngresadas.add(edicion);
                                } else if ("RECHAZADA".equals(estado)) {
                                    edicionesRechazadas.add(edicion);
                                }
                            }
                        }
                    }
                    
                    request.setAttribute("edicionesAceptadas", edicionesAceptadas);
                    request.setAttribute("edicionesIngresadas", edicionesIngresadas);
                    request.setAttribute("edicionesRechazadas", edicionesRechazadas);
                } catch (Exception e) {
                    e.printStackTrace();
                    // Si falla, enviar listas vacías
                    request.setAttribute("edicionesAceptadas", new java.util.ArrayList<>());
                    request.setAttribute("edicionesIngresadas", new java.util.ArrayList<>());
                    request.setAttribute("edicionesRechazadas", new java.util.ArrayList<>());
                }
            }
            
            if (session != null) {
                request.setAttribute("sessionNickname", session.getAttribute("usuario"));
                request.setAttribute("sessionAvatar", session.getAttribute("avatar"));
                request.setAttribute("sessionRole", session.getAttribute("role"));
            }
            
            if ("/miPerfil".equals(requestPath)) {
                request.getRequestDispatcher("/WEB-INF/views/miPerfil.jsp").forward(request, response);
            } else {
                request.getRequestDispatcher("/WEB-INF/views/perfilUsuario.jsp").forward(request, response);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al consultar el perfil: " + e.getMessage());
        }
    }
}