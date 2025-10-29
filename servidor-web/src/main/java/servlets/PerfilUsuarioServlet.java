package servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import soap.PublicadorUsuario;
import soap.PublicadorControlador;
import soap.PublicadorRegistro;
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
            PublicadorRegistro publicadorRegistro = SoapClientHelper.getPublicadorRegistro();

            
            
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
                    // Obtener registros desde PublicadorRegistro (SOAP)
                    soap.DtRegistroArray registrosArray = publicadorRegistro.listarRegistrosPorAsistente(nickname);

                    java.util.List<DtRegistro> registros = new java.util.ArrayList<>();

                    if (registrosArray != null && registrosArray.getItem() != null) {
                        // Cada DtRegistro en el arreglo SOAP contiene los campos asistente|edicion|tipoRegistro
                        for (soap.DtRegistro r : registrosArray.getItem()) {
                            if (r == null) continue;
                            try {
                                // Pedir al controlador la representación completa del registro
                                DtRegistro reg = publicadorControlador.obtenerRegistro(r.getAsistente(), r.getNomEdicion(), r.getTipoDeRegistro());
                                if (reg != null) registros.add(reg);
                            } catch (Exception ex) {
                                System.err.println("Error obteniendo registro desde controlador: " + ex.getMessage());
                            }
                        }
                    }

                    request.setAttribute("registros", registros);
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

                    // Determinar si el usuario logueado es el mismo que el del perfil
                    String usuarioLogueado = null;
                    if (session != null && session.getAttribute("usuario") != null) {
                        usuarioLogueado = (String) session.getAttribute("usuario");
                    }

                    java.util.List<servlets.dto.RegistroEdicionDTO> edicionesAceptadas = new java.util.ArrayList<>();
                    java.util.List<servlets.dto.RegistroEdicionDTO> edicionesIngresadas = new java.util.ArrayList<>();
                    java.util.List<servlets.dto.RegistroEdicionDTO> edicionesRechazadas = new java.util.ArrayList<>();

                    if (edicionesNicks != null && edicionesNicks.length > 0) {
                        for (String edicionNombre : edicionesNicks) {
                            try {
                                DtEdicion edicion = publicadorControlador.consultarEdicion(edicionNombre);
                                if (edicion != null) {
                                    servlets.dto.RegistroEdicionDTO dto = new servlets.dto.RegistroEdicionDTO();
                                    dto.setEvento(edicion.getEvento());
                                    dto.setNombre(edicion.getNombre());
                                    dto.setSigla(edicion.getSigla());
                                    dto.setEstado(edicion.getEstado().value());

                                    String estado = edicion.getEstado().value();
                                    // Si el usuario logueado es el organizador, mostrar todas las ediciones
                                    if (usuarioLogueado != null && usuarioLogueado.equals(nickname)) {
                                        if ("ACEPTADA".equals(estado)) {
                                            edicionesAceptadas.add(dto);
                                        } else if ("INGRESADA".equals(estado)) {
                                            edicionesIngresadas.add(dto);
                                        } else if ("RECHAZADA".equals(estado)) {
                                            edicionesRechazadas.add(dto);
                                        }
                                    } else {
                                        // Si es otro usuario, mostrar solo las ediciones ACEPTADA
                                        if ("ACEPTADA".equals(estado)) {
                                            edicionesAceptadas.add(dto);
                                        }
                                    }
                                }
                            } catch (Exception e) {
                                System.err.println("Error obteniendo edición: " + edicionNombre);
                            }
                        }
                    }

                    request.setAttribute("edicionesAceptadas", edicionesAceptadas);
                    request.setAttribute("edicionesIngresadas", edicionesIngresadas);
                    request.setAttribute("edicionesRechazadas", edicionesRechazadas);
                } catch (Exception e) {
                    e.printStackTrace();
                    request.setAttribute("edicionesAceptadas", new java.util.ArrayList<>());
                    request.setAttribute("edicionesIngresadas", new java.util.ArrayList<>());
                    request.setAttribute("edicionesRechazadas", new java.util.ArrayList<>());
                }
            }
            
            if (session != null) {

                StringArray categoriasSet = publicadorControlador.listarCategorias();
                List<String> categorias = new ArrayList<>(categoriasSet.getItem());
                Collections.sort(categorias);


                request.setAttribute("sessionNickname", session.getAttribute("usuario"));
                request.setAttribute("sessionAvatar", session.getAttribute("avatar"));
                request.setAttribute("sessionRole", session.getAttribute("role"));
                request.setAttribute("categorias", categorias);
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