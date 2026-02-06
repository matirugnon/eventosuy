package servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import soap.UsuarioNoExisteException_Exception;
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

            DtUsuario dtUsuario = publicador.getDTUsuario(nickname);

            if (dtUsuario == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Usuario no encontrado");
                return;
            }

            String tipoUsuario;
            if (dtUsuario instanceof DtAsistente) {
                tipoUsuario = "Asistente";
            } else if (dtUsuario instanceof DtOrganizador) {
                tipoUsuario = "Organizador";
            } else {
                tipoUsuario = "Usuario";
            }

            request.setAttribute("usuario", dtUsuario);
            request.setAttribute("tipoUsuario", tipoUsuario);
            request.setAttribute("perfilNickname", nickname);

            boolean esPropietario = session != null && nickname.equals(session.getAttribute("usuario"));
            request.setAttribute("esPropietario", esPropietario);

            List<String> seguidores = new ArrayList<>();
            List<String> seguidos = new ArrayList<>();

            try {
                StringArray seguidoresArray = publicador.obtenerSeguidores(nickname);
                if (seguidoresArray != null && seguidoresArray.getItem() != null) {
                    seguidores = new ArrayList<>(seguidoresArray.getItem());
                }
            } catch (UsuarioNoExisteException_Exception ignored) {
                seguidores = new ArrayList<>();
            }

            try {
                StringArray seguidosArray = publicador.obtenerSeguidos(nickname);
                if (seguidosArray != null && seguidosArray.getItem() != null) {
                    seguidos = new ArrayList<>(seguidosArray.getItem());
                }
            } catch (UsuarioNoExisteException_Exception ignored) {
                seguidos = new ArrayList<>();
            }

            boolean puedeSeguir = session != null && session.getAttribute("usuario") != null && !esPropietario;
            boolean siguiendo = false;
            if (puedeSeguir) {
                try {
                    String usuarioActual = (String) session.getAttribute("usuario");
                    siguiendo = publicador.esSeguidor(usuarioActual, nickname);
                } catch (UsuarioNoExisteException_Exception ignored) {
                    siguiendo = false;
                }
            }

            request.setAttribute("seguidores", seguidores);
            request.setAttribute("seguidos", seguidos);
            request.setAttribute("seguidoresCount", seguidores.size());
            request.setAttribute("seguidosCount", seguidos.size());
            request.setAttribute("puedeSeguir", puedeSeguir);
            request.setAttribute("siguiendo", siguiendo);

            if (dtUsuario instanceof DtAsistente asistente) {
                request.setAttribute("asistente", asistente);

                try {
                    soap.DtRegistroArray registrosArray = publicadorRegistro.listarRegistrosPorAsistente(nickname);
                    List<DtRegistro> registros = new ArrayList<>();

                    if (registrosArray != null && registrosArray.getItem() != null) {
                        for (soap.DtRegistro r : registrosArray.getItem()) {
                            if (r == null) {
                                continue;
                            }
                            try {
                                DtRegistro reg = publicadorControlador.obtenerRegistro(r.getAsistente(), r.getNomEdicion(), r.getTipoDeRegistro());
                                if (reg != null) {
                                    registros.add(reg);
                                }
                            } catch (Exception ex) {
                                System.err.println("Error obteniendo registro desde controlador: " + ex.getMessage());
                            }
                        }
                    }

                    request.setAttribute("registros", registros);
                } catch (Exception e) {
                    e.printStackTrace();
                    request.setAttribute("registros", new ArrayList<>());
                }
            }

            if (dtUsuario instanceof DtOrganizador organizador) {
                request.setAttribute("organizador", organizador);

                try {
                    StringArray edicionesArray = publicador.listarEdicionesOrganizador(nickname);
                    String[] edicionesNicks = edicionesArray != null && edicionesArray.getItem() != null
                            ? edicionesArray.getItem().toArray(new String[0])
                            : new String[0];

                    String usuarioLogueado = null;
                    if (session != null && session.getAttribute("usuario") != null) {
                        usuarioLogueado = (String) session.getAttribute("usuario");
                    }

                    List<servlets.dto.RegistroEdicionDTO> edicionesAceptadas = new ArrayList<>();
                    List<servlets.dto.RegistroEdicionDTO> edicionesIngresadas = new ArrayList<>();
                    List<servlets.dto.RegistroEdicionDTO> edicionesRechazadas = new ArrayList<>();
                    List<servlets.dto.RegistroEdicionDTO> edicionesArchivadas = new ArrayList<>();
                    
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
                                    if (usuarioLogueado != null && usuarioLogueado.equals(nickname)) {
                                        if ("ACEPTADA".equals(estado)) {
                                            edicionesAceptadas.add(dto);
                                        } else if ("INGRESADA".equals(estado)) {
                                            edicionesIngresadas.add(dto);
                                        } else if ("RECHAZADA".equals(estado)) {
                                            edicionesRechazadas.add(dto);
                                        } else if ("ARCHIVADA".equals(estado)) {
                                            edicionesArchivadas.add(dto);
                                        }
                                    } else if ("ACEPTADA".equals(estado) || "ARCHIVADA".equals(estado)) {
                                        // Mostrar aceptadas y archivadas en perfiles públicos
                                        if ("ACEPTADA".equals(estado)) {
                                            edicionesAceptadas.add(dto);
                                        } else {
                                            edicionesArchivadas.add(dto);
                                        }
                                    }
                                }
                            } catch (Exception e) {
                                System.err.println("Error obteniendo edicion: " + edicionNombre);
                            }
                        }
                    }

                    request.setAttribute("edicionesAceptadas", edicionesAceptadas);
                    request.setAttribute("edicionesIngresadas", edicionesIngresadas);
                    request.setAttribute("edicionesRechazadas", edicionesRechazadas);
                    request.setAttribute("edicionesArchivadas", edicionesArchivadas);
                } catch (Exception e) {
                    e.printStackTrace();
                    request.setAttribute("edicionesAceptadas", new ArrayList<>());
                    request.setAttribute("edicionesIngresadas", new ArrayList<>());
                    request.setAttribute("edicionesRechazadas", new ArrayList<>());
                    request.setAttribute("edicionesArchivadas", new ArrayList<>());
                }
            }

            if (session != null) {
                if (session.getAttribute("perfilError") != null) {
                    request.setAttribute("perfilError", session.getAttribute("perfilError"));
                    session.removeAttribute("perfilError");
                }

                StringArray categoriasSet = publicadorControlador.listarCategorias();
                List<String> categorias = new ArrayList<>();
                if (categoriasSet != null && categoriasSet.getItem() != null) {
                    categorias.addAll(categoriasSet.getItem());
                    Collections.sort(categorias);
                }

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

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String usuarioActual = (String) session.getAttribute("usuario");
        String nickname = request.getParameter("nickname");
        String action = request.getParameter("action");

        if (nickname == null || nickname.trim().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Nickname no especificado");
            return;
        }

        if (action == null || action.trim().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Accion no especificada");
            return;
        }

        try {
            PublicadorUsuario publicador = SoapClientHelper.getPublicadorUsuario();
            if ("seguir".equals(action)) {
                publicador.seguirUsuario(usuarioActual, nickname);
            } else if ("dejar".equals(action)) {
                publicador.dejarSeguirUsuario(usuarioActual, nickname);
            }
        } catch (UsuarioNoExisteException_Exception e) {
            session.setAttribute("perfilError", e.getMessage());
        } catch (Exception e) {
            session.setAttribute("perfilError", "No se pudo actualizar la relacion de seguimiento.");
        }

        response.sendRedirect(request.getContextPath() + "/perfilUsuario?nickname=" + nickname);
    }
}
