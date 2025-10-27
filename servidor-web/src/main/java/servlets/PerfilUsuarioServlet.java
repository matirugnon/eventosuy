package servlets;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import soap.PublicadorUsuario;
import soap.PublicadorControlador;
import soap.DtUsuario;
import soap.DtAsistente;
import soap.DtOrganizador;
import soap.DtRegistro;
import soap.DtEdicion;
import soap.StringArray;
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
            PublicadorControlador publicadorControlador = SoapClientHelper.getPublicadorControlador();
            
            // Obtener DTUsuario completo desde SOAP
            DtUsuario dtUsuario = publicador.getDTUsuario(nickname);
            
            if (dtUsuario == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Usuario no encontrado");
                return;
            }
            
            // Determinar tipo de usuario
            String tipoUsuario;
            if (dtUsuario instanceof DtAsistente) {
                tipoUsuario = "Asistente";
            } else if (dtUsuario instanceof DtOrganizador) {
                tipoUsuario = "Organizador";
            } else {
                tipoUsuario = "Usuario";
            }
            
            // Enviar DTUsuario completo al JSP
            request.setAttribute("usuario", dtUsuario);
            request.setAttribute("tipoUsuario", tipoUsuario);
            
            // Si es Asistente, obtener sus registros
            if (dtUsuario instanceof DtAsistente) {
                DtAsistente asistente = (DtAsistente) dtUsuario;
                request.setAttribute("asistente", asistente);
                
                try {
                    // Obtener registros desde PublicadorUsuario
                    StringArray registrosArray = publicador.obtenerRegistros(nickname);
                    String[] registrosNicks = registrosArray != null ? registrosArray.getItem().toArray(new String[0]) : new String[0];
                    
                    if (registrosNicks != null && registrosNicks.length > 0) {
                        java.util.List<DtRegistro> registros = new java.util.ArrayList<>();
                        
                        // Para cada registro, obtener DTRegistro completo
                        for (String regNick : registrosNicks) {
                            // regNick tiene formato: "asistente|edicion|tipoRegistro"
                            String[] parts = regNick.split("\\|");
                            if (parts.length >= 3) {
                                try {
                                    DtRegistro reg = publicadorControlador.obtenerRegistro(parts[0], parts[1], parts[2]);
                                    if (reg != null) {
                                        registros.add(reg);
                                    }
                                } catch (Exception e) {
                                    System.err.println("Error obteniendo registro: " + regNick);
                                }
                            }
                        }
                        
                        request.setAttribute("registros", registros);
                    } else {
                        request.setAttribute("registros", new java.util.ArrayList<>());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    request.setAttribute("registros", new java.util.ArrayList<>());
                }
            }
            
            // Si es Organizador, obtener sus ediciones
            if (dtUsuario instanceof DtOrganizador) {
                DtOrganizador organizador = (DtOrganizador) dtUsuario;
                request.setAttribute("organizador", organizador);
                
                try {
                    // Obtener ediciones desde PublicadorUsuario
                    StringArray edicionesArray = publicador.listarEdicionesOrganizador(nickname);
                    String[] edicionesNicks = edicionesArray != null ? edicionesArray.getItem().toArray(new String[0]) : new String[0];
                    
                    if (edicionesNicks != null && edicionesNicks.length > 0) {
                        java.util.List<servlets.dto.RegistroEdicionDTO> edicionesAceptadas = new java.util.ArrayList<>();
                        java.util.List<servlets.dto.RegistroEdicionDTO> edicionesIngresadas = new java.util.ArrayList<>();
                        java.util.List<servlets.dto.RegistroEdicionDTO> edicionesRechazadas = new java.util.ArrayList<>();
                        
                        // Para cada edición, consultar y clasificar por estado
                        for (String edicionNombre : edicionesNicks) {
                            try {
                                DtEdicion edicion = publicadorControlador.consultarEdicion(edicionNombre);
                                if (edicion != null) {
                                    servlets.dto.RegistroEdicionDTO dto = new servlets.dto.RegistroEdicionDTO();
                                    dto.setEvento(edicion.getEvento());
                                    dto.setNombre(edicion.getNombre());
                                    dto.setSigla(edicion.getSigla());
                                    dto.setEstado(edicion.getEstado().value());
                                    
                                    // Clasificar por estado
                                    String estado = edicion.getEstado().value();
                                    if ("ACEPTADA".equals(estado)) {
                                        edicionesAceptadas.add(dto);
                                    } else if ("INGRESADA".equals(estado)) {
                                        edicionesIngresadas.add(dto);
                                    } else if ("RECHAZADA".equals(estado)) {
                                        edicionesRechazadas.add(dto);
                                    }
                                }
                            } catch (Exception e) {
                                System.err.println("Error obteniendo edición: " + edicionNombre);
                            }
                        }
                        
                        request.setAttribute("edicionesAceptadas", edicionesAceptadas);
                        request.setAttribute("edicionesIngresadas", edicionesIngresadas);
                        request.setAttribute("edicionesRechazadas", edicionesRechazadas);
                    } else {
                        request.setAttribute("edicionesAceptadas", new java.util.ArrayList<>());
                        request.setAttribute("edicionesIngresadas", new java.util.ArrayList<>());
                        request.setAttribute("edicionesRechazadas", new java.util.ArrayList<>());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
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