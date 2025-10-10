package servlets;

import java.io.IOException;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import logica.Controladores.IControladorUsuario;
import logica.Controladores.IControladorEvento;
import logica.Controladores.IControladorRegistro;
import logica.DatatypesYEnum.DTUsuario;
import logica.DatatypesYEnum.DTAsistente;
import logica.DatatypesYEnum.DTOrganizador;
import logica.DatatypesYEnum.EstadoEdicion;
import logica.Edicion;
import utils.Utils;
import logica.DatatypesYEnum.DTEdicion;
import logica.Registro;
import logica.DatatypesYEnum.DTRegistro;

@WebServlet({"/perfilUsuario", "/miPerfil"})
public class PerfilUsuarioServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Determinar el nickname del usuario a consultar
            String nickname;
            String requestPath = request.getServletPath();
            
            if ("/miPerfil".equals(requestPath)) {
                // Para /miPerfil, verificar que el usuario esté logueado y usar su nickname
                if (request.getSession(false) == null || request.getSession(false).getAttribute("usuario") == null) {
                    response.sendRedirect(request.getContextPath() + "/login");
                    return;
                }
                nickname = (String) request.getSession().getAttribute("usuario");
            } else {
                // Para /perfilUsuario, obtener el nickname del parámetro
                nickname = request.getParameter("nickname");
                if (nickname == null || nickname.trim().isEmpty()) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Nickname de usuario requerido");
                    return;
                }
            }

            // Obtener los controladores
            IControladorUsuario ctrlUsuario = IControladorUsuario.getInstance();
            IControladorEvento ctrlEvento = IControladorEvento.getInstance();

            // Carga inicial de datos si hace falta
            Set<String> usuariosExistentes = ctrlUsuario.listarUsuarios();
            if (usuariosExistentes == null || usuariosExistentes.isEmpty()) {
                Utils.cargarDatos(
                    ctrlUsuario,
                    ctrlEvento,
                    IControladorRegistro.getInstance()
                );
            }

            // Verificar que el usuario existe
            if (!ctrlUsuario.ExisteNickname(nickname)) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Usuario no encontrado");
                return;
            }

            // Obtener el DTUsuario específico
            DTUsuario usuario = ctrlUsuario.getDTUsuario(nickname);
            
            if (usuario == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "No se pudo obtener la información del usuario");
                return;
            }

            // Determinar el tipo de usuario y obtener información específica
            String tipoUsuario;
            DTAsistente asistente = null;
            DTOrganizador organizador = null;
            
            if (usuario instanceof DTAsistente) {
                tipoUsuario = "Asistente";
                asistente = (DTAsistente) usuario;
            } else if (usuario instanceof DTOrganizador) {
                tipoUsuario = "Organizador";
                organizador = (DTOrganizador) usuario;
            } else {
                tipoUsuario = "Usuario";
            }

            // Obtener categorías para el sidebar
            Set<String> categorias = ctrlEvento.listarCategorias();

            // Manejar parámetros de navegación de regreso
            String from = request.getParameter("from");
            String edicionOrigen = request.getParameter("edicion");
            
            // Configurar información de regreso
            if ("edicion".equals(from) && edicionOrigen != null && !edicionOrigen.trim().isEmpty()) {
                request.setAttribute("backTo", "edicion");
                request.setAttribute("backLabel", "← Volver a la edición " + edicionOrigen);
                request.setAttribute("backUrl", request.getContextPath() + "/consultaEdicion?edicion=" + edicionOrigen);
            } else {
                request.setAttribute("backTo", "listado");
                request.setAttribute("backLabel", "← Volver al listado");
                request.setAttribute("backUrl", request.getContextPath() + "/listarUsuarios");
            }

            // Pasar los datos como atributos a la JSP
            request.setAttribute("usuario", usuario);
            request.setAttribute("tipoUsuario", tipoUsuario);
            request.setAttribute("asistente", asistente);
            request.setAttribute("organizador", organizador);
            request.setAttribute("categorias", categorias);

            // Pasar datos de sesión (nickname, avatar, nombre, role)
            request.setAttribute("nickname", request.getSession().getAttribute("usuario"));
            request.setAttribute("avatar", request.getSession().getAttribute("avatar"));
            request.setAttribute("role", request.getSession().getAttribute("role"));
            request.setAttribute("nombre", request.getSession().getAttribute("nombre"));

            // Obtener ediciones organizadas por el usuario
            Set<DTEdicion> edicionesAceptadas = ctrlEvento.listarEdicionesOrganizadasPorEstado(nickname, EstadoEdicion.ACEPTADA);
            request.setAttribute("edicionesAceptadas", edicionesAceptadas);

            // Si el organizador es quien inició sesión
            String sessionNickname = (String) request.getSession().getAttribute("usuario");
            if (sessionNickname != null && sessionNickname.equals(nickname)) {
                Set<DTEdicion> edicionesIngresadas = ctrlEvento.listarEdicionesOrganizadasPorEstado(sessionNickname, EstadoEdicion.INGRESADA);
                Set<DTEdicion> edicionesRechazadas = ctrlEvento.listarEdicionesOrganizadasPorEstado(sessionNickname, EstadoEdicion.RECHAZADA);
                request.setAttribute("edicionesIngresadas", edicionesIngresadas);
                request.setAttribute("edicionesRechazadas", edicionesRechazadas);
            }

            // Verificar si el usuario logueado es un asistente y está consultando su propio perfil
            if (tipoUsuario.equals("Asistente") && sessionNickname != null && sessionNickname.equals(nickname)) {
                IControladorRegistro ctrlRegistro = IControladorRegistro.getInstance();
                
                // Obtener registros del asistente - funcionalidad de MiPerfilServlet
                Set<DTRegistro> registros = new HashSet<>();
                try {
                    registros = ctrlRegistro.listarRegistrosPorAsistente(nickname);
                    if (registros == null) {
                        registros = new HashSet<>();
                    }
                } catch (Exception e) {
                    System.out.println("Error obteniendo registros: " + e.getMessage());
                    registros = new HashSet<>();
                }
                request.setAttribute("registros", registros);
                
                // También preparar los datos para perfilUsuario.jsp
                Set<Map<String, String>> registrosAsistente = new HashSet<>();
                for (DTRegistro registro : registros) {
                    String nomEdicion = registro.getnomEdicion();
                    DTEdicion edicion = ctrlEvento.consultarEdicion(nomEdicion);
                    String nomEvento = edicion.getEvento(); // Assuming DTEdicion has a getEvento() method

                    Map<String, String> registroData = new HashMap<>();
                    registroData.put("evento", nomEvento);
                    registroData.put("edicion", nomEdicion);
                    registroData.put("tipoDeRegistro", registro.getTipoDeRegistro());
                    registroData.put("fechaRegistro", registro.getFechaRegistro().toString());
                    registroData.put("costo", registro.getCosto().toString());

                    registrosAsistente.add(registroData);
                }
                request.setAttribute("registrosAsistente", registrosAsistente);
            } else if (tipoUsuario.equals("Asistente")) {
                // Para otros asistentes, solo preparar datos para perfilUsuario.jsp
                IControladorRegistro ctrlRegistro = IControladorRegistro.getInstance();
                Set<DTRegistro> registros = ctrlRegistro.listarRegistrosPorAsistente(nickname);
                Set<Map<String, String>> registrosAsistente = new HashSet<>();

                for (DTRegistro registro : registros) {
                    String nomEdicion = registro.getnomEdicion();
                    DTEdicion edicion = ctrlEvento.consultarEdicion(nomEdicion);
                    String nomEvento = edicion.getEvento(); // Assuming DTEdicion has a getEvento() method

                    Map<String, String> registroData = new HashMap<>();
                    registroData.put("evento", nomEvento);
                    registroData.put("edicion", nomEdicion);
                    registroData.put("tipoDeRegistro", registro.getTipoDeRegistro());
                    registroData.put("fechaRegistro", registro.getFechaRegistro().toString());
                    registroData.put("costo", registro.getCosto().toString());

                    registrosAsistente.add(registroData);
                }
                request.setAttribute("registrosAsistente", registrosAsistente);
            }
            
            // Determinar qué registros puede consultar el usuario logueado
            Set<String> registrosConsultables = new HashSet<>();
            String sessionRole = (String) request.getSession().getAttribute("role");
            
            if (sessionNickname != null && tipoUsuario.equals("Asistente")) {
                IControladorRegistro ctrlRegistro = IControladorRegistro.getInstance();
                Set<DTRegistro> registros = ctrlRegistro.listarRegistrosPorAsistente(nickname);
                
                for (DTRegistro registro : registros) {
                    // Verificar permisos para cada registro
                    boolean puedeConsultar = false;
                    
                    // Caso 1: Es el mismo asistente
                    if (nickname.equals(sessionNickname) && "asistente".equals(sessionRole)) {
                        puedeConsultar = true;
                    }
                    // Caso 2: Es el organizador específico de esta edición
                    else if ("organizador".equals(sessionRole)) {
                        try {
                            DTEdicion edicion = ctrlEvento.consultarEdicion(registro.getnomEdicion());
                            if (edicion != null && sessionNickname.equals(edicion.getOrganizador())) {
                                puedeConsultar = true;
                            }
                        } catch (Exception e) {
                            // Si hay error, no dar permisos
                        }
                    }
                    
                    if (puedeConsultar) {
                        // Crear una clave única para identificar el registro
                        String claveRegistro = nickname + "|" + registro.getnomEdicion() + "|" + registro.getTipoDeRegistro();
                        registrosConsultables.add(claveRegistro);
                    }
                }
            }
            request.setAttribute("registrosConsultables", registrosConsultables);
            
            // Para organizadores, obtener ediciones organizadas - funcionalidad de MiPerfilServlet
            if (tipoUsuario.equals("Organizador") && sessionNickname != null && sessionNickname.equals(nickname)) {
                Set<String> edicionesOrganizadas = new HashSet<>();
                try {
                    edicionesOrganizadas = ctrlUsuario.listarEdicionesOrganizador(nickname);
                } catch (Exception e) {
                    System.out.println("Error obteniendo ediciones: " + e.getMessage());
                }
                request.setAttribute("edicionesOrganizadas", edicionesOrganizadas);
            }

            // Redirigir al JSP correspondiente según la ruta
            if ("/miPerfil".equals(requestPath)) {
                request.getRequestDispatcher("/WEB-INF/views/miPerfil.jsp").forward(request, response);
            } else {
                request.getRequestDispatcher("/WEB-INF/views/perfilUsuario.jsp").forward(request, response);
            }

        } catch (Exception e) {
            throw new ServletException("Error al obtener el perfil del usuario: " + e.getMessage(), e);
        }
    }
}