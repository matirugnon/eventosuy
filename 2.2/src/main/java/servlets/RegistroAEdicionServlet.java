package servlets;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Set;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import logica.controladores.IControladorEvento;
import logica.controladores.IControladorUsuario;
import logica.controladores.IControladorRegistro;
import logica.datatypesyenum.DTEdicion;
import logica.datatypesyenum.DTEvento;
import logica.datatypesyenum.DTFecha;
import logica.datatypesyenum.DTPatrocinio;
import logica.datatypesyenum.DTTipoDeRegistro;
import logica.datatypesyenum.EstadoEdicion;
import excepciones.EdicionNoExisteException;
import excepciones.EdicionSinPatrociniosException;
import excepciones.PatrocinioNoEncontradoException;
import excepciones.UsuarioNoExisteException;
import excepciones.UsuarioYaRegistradoEnEdicionException;
import utils.Utils;

@WebServlet("/registroAedicion")
public class RegistroAEdicionServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        String usuario = (String) session.getAttribute("usuario");
        String role = (String) session.getAttribute("role");
        
        // Verificar que el usuario esté logueado y sea un asistente
        if (usuario == null || !"asistente".equals(role)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        // Verificar si es una petición AJAX
        String ajaxHeader = request.getHeader("X-Requested-With");
        if ("XMLHttpRequest".equals(ajaxHeader) || request.getParameter("action") != null) {
            handleAjaxRequest(request, response);
            return;
        }
        
        try {
            IControladorEvento ctrlEvento = IControladorEvento.getInstance();
            IControladorUsuario ctrlUsuario = IControladorUsuario.getInstance();
            IControladorRegistro ctrlRegistro = IControladorRegistro.getInstance();
            
            // Carga inicial de datos si hace falta
            Set<String> usuariosExistentes = ctrlUsuario.listarUsuarios();
            if (usuariosExistentes == null || usuariosExistentes.isEmpty()) {
                Utils.cargarDatos(
                    ctrlUsuario,
                    ctrlEvento,
                    ctrlRegistro
                );
            }
            
            // Obtener todos los eventos con sus ediciones
            Set<DTEvento> eventos = ctrlEvento.obtenerDTEventos();
            Set<String> categorias = ctrlEvento.listarCategorias();
            
            // Para cada evento, obtener las ediciones con información completa
            for (DTEvento evento : eventos) {
                Set<String> nombresEdiciones = ctrlEvento.listarEdiciones(evento.getNombre());
                for (String nombreEdicion : nombresEdiciones) {
                    DTEdicion edicion = ctrlEvento.consultarEdicion(nombreEdicion);
                    if (edicion != null) {
                        // Agregar tipos de registro a la edición
                        Set<String> nombresTipos = ctrlRegistro.listarTipoRegistro(nombreEdicion);
                        // Los tipos ya están en edicion.getTiposDeRegistro(), pero necesitamos la info completa
                    }
                }
            }
            
            // Pasar los datos a la JSP
            request.setAttribute("eventos", eventos);
            request.setAttribute("categorias", categorias);
            request.setAttribute("role", role);
            request.setAttribute("nickname", usuario);
            request.setAttribute("avatar", session.getAttribute("avatar"));
            
            request.getRequestDispatcher("/WEB-INF/views/registroAedicion.jsp").forward(request, response);
            
        } catch (Exception e) {
            throw new ServletException("Error cargando formulario de registro", e);
        }
    }
    
    // Método para obtener datos de edición via AJAX
    private void handleAjaxRequest(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        try {
            IControladorEvento ctrlEvento = IControladorEvento.getInstance();
            IControladorRegistro ctrlRegistro = IControladorRegistro.getInstance();
            
            if ("getEdiciones".equals(action)) {
                String nombreEvento = request.getParameter("evento");
                if (nombreEvento != null) {
                    Set<String> ediciones = ctrlEvento.listarEdiciones(nombreEvento);
                    StringBuilder json = new StringBuilder("[");
                    boolean first = true;
                    for (String edicion : ediciones) {
                        if (!first) json.append(",");
                        json.append("{\"id\":\"").append(edicion).append("\",\"nombre\":\"").append(edicion).append("\"}");
                        first = false;
                    }
                    json.append("]");
                    response.getWriter().write(json.toString());
                }
            } else if ("getEdicionDetails".equals(action)) {
                String nombreEdicion = request.getParameter("edicion");
                if (nombreEdicion != null) {
                    DTEdicion edicion = ctrlEvento.consultarEdicion(nombreEdicion);
                    if (edicion != null) {
                        // Obtener tipos de registro con información completa
                        Set<String> nombresTipos = ctrlRegistro.listarTipoRegistro(nombreEdicion);
                        StringBuilder tiposJson = new StringBuilder("[");
                        boolean first = true;
                        for (String nombreTipo : nombresTipos) {
                            DTTipoDeRegistro tipo = ctrlRegistro.consultaTipoDeRegistro(nombreEdicion, nombreTipo);
                            if (tipo != null) {
                                if (!first) tiposJson.append(",");
                                tiposJson.append("{\"id\":\"").append(tipo.getNombre())
                                        .append("\",\"nombre\":\"").append(tipo.getNombre())
                                        .append("\",\"cupo\":").append(tipo.getCupo())
                                        .append(",\"costo\":").append(tipo.getCosto()).append("}");
                                first = false;
                            }
                        }
                        tiposJson.append("]");
                        
                        StringBuilder json = new StringBuilder("{");
                        json.append("\"nombre\":\"").append(edicion.getNombre()).append("\",");
                        json.append("\"fechas\":\"").append(edicion.getFechaInicio().toString())
                            .append(" - ").append(edicion.getFechaFin().toString()).append("\",");
                        json.append("\"ciudad\":\"").append(edicion.getCiudad()).append("\",");
                        json.append("\"pais\":\"").append(edicion.getPais()).append("\",");
                        json.append("\"tipos\":").append(tiposJson.toString());
                        json.append("}");
                        
                        response.getWriter().write(json.toString());
                    }
                }
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        String usuario = (String) session.getAttribute("usuario");
        String role = (String) session.getAttribute("role");
        
        // Verificar que el usuario esté logueado y sea un asistente
        if (usuario == null || !"asistente".equals(role)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        try {
            IControladorEvento ctrlEvento = IControladorEvento.getInstance();
            IControladorUsuario ctrlUsuario = IControladorUsuario.getInstance();
            IControladorRegistro ctrlRegistro = IControladorRegistro.getInstance();
            
            // Obtener parámetros del formulario
            String edicion = request.getParameter("edicion");
            String tipoRegistro = request.getParameter("tipoRegistro");
            String codigoPatrocinio = request.getParameter("codigoPatrocinio");
            
            String mensaje = "";
            String tipoMensaje = "error";
            
            if (edicion == null || edicion.trim().isEmpty()) {
                mensaje = "Debe seleccionar una edición.";
            } else if (tipoRegistro == null || tipoRegistro.trim().isEmpty()) {
                mensaje = "Debe seleccionar un tipo de registro.";
            } else {
                edicion = edicion.trim();
                tipoRegistro = tipoRegistro.trim();
                
                try {
                    // Verificar que la edición existe
                    DTEdicion dtEdicion = ctrlEvento.consultarEdicion(edicion);
                    if (dtEdicion == null) {
                        mensaje = "La edición seleccionada no existe.";
                    } else {
                        // Verificar que el usuario no esté ya registrado
                        boolean yaRegistrado = ctrlRegistro.estaRegistrado(edicion, usuario);
                        if (yaRegistrado) {
                            mensaje = "Ya estás registrado en esta edición.";
                        } else {
                            // Verificar que el tipo de registro existe
                            DTTipoDeRegistro dtTipoRegistro = ctrlRegistro.consultaTipoDeRegistro(edicion, tipoRegistro);
                            if (dtTipoRegistro == null) {
                                mensaje = "El tipo de registro seleccionado no existe.";
                            } else {
                                // Verificar cupo disponible
                                boolean cupoDisponible = !ctrlRegistro.alcanzoCupo(edicion, tipoRegistro);
                                if (!cupoDisponible) {
                                    mensaje = "Ya se alcanzó el cupo para este tipo de registro.";
                                } else {
                                    double costoFinal = dtTipoRegistro.getCosto();
                                    
                                    // Procesar código de patrocinio si se proporcionó
                                    if (codigoPatrocinio != null && !codigoPatrocinio.trim().isEmpty()) {
                                        codigoPatrocinio = codigoPatrocinio.trim();
                                        
                                        try {
                                            // Verificar que el código existe en la edición
                                            boolean existeCodigo = ctrlEvento.existeCodigoPatrocinioEnEdicion(edicion, codigoPatrocinio);
                                            if (!existeCodigo) {
                                                mensaje = "El código de patrocinio no es válido para esta edición.";
                                            } else {
                                                // Obtener información del patrocinio
                                                DTPatrocinio patrocinio = ctrlEvento.consultarTipoPatrocinioEdicion(edicion, codigoPatrocinio);
                                                
                                                // Verificar que el patrocinio aplica al tipo de registro
                                                if (!patrocinio.getTipoDeRegistro().equals(tipoRegistro)) {
                                                    mensaje = "El código de patrocinio no es válido para este tipo de registro.";
                                                } else {
                                                    // Verificar que aún quedan usos disponibles
                                                    if (patrocinio.getCantidadGratis() <= 0) {
                                                        mensaje = "Ya se alcanzó la cantidad de usos para este código de patrocinio.";
                                                    } else {
                                                        // TODO: Verificar institución del usuario si es necesario
                                                        costoFinal = 0.0; // Registro gratuito con patrocinio
                                                    }
                                                }
                                            }
                                        } catch (EdicionSinPatrociniosException e) {
                                            mensaje = "Esta edición no tiene patrocinios disponibles.";
                                        } catch (PatrocinioNoEncontradoException e) {
                                            mensaje = "El código de patrocinio no es válido.";
                                        }
                                    }
                                    
                                    // Si no hay errores, proceder con el registro
                                    if (mensaje.isEmpty()) {
                                        LocalDate fechaActual = LocalDate.now();
                                        DTFecha fechaRegistro = new DTFecha(
                                            fechaActual.getDayOfMonth(),
                                            fechaActual.getMonthValue(),
                                            fechaActual.getYear()
                                        );
                                        
                                        ctrlRegistro.altaRegistro(edicion, usuario, tipoRegistro, fechaRegistro, costoFinal);
                                        
                                        mensaje = "✅ Registro exitoso. Fecha: " + fechaRegistro.toString() + " | Costo: $" + String.format("%.0f", costoFinal);
                                        tipoMensaje = "success";
                                    }
                                }
                            }
                        }
                    }
                } catch (EdicionNoExisteException e) {
                    mensaje = "La edición seleccionada no existe.";
                } catch (UsuarioNoExisteException e) {
                    mensaje = "Error: usuario no encontrado.";
                } catch (UsuarioYaRegistradoEnEdicionException e) {
                    mensaje = "Ya estás registrado en esta edición.";
                } catch (Exception e) {
                    mensaje = "Error inesperado: " + e.getMessage();
                }
            }
            
            // Recargar datos para mostrar el formulario con el mensaje
            Set<DTEvento> eventos = ctrlEvento.obtenerDTEventos();
            Set<String> categorias = ctrlEvento.listarCategorias();
            
            request.setAttribute("eventos", eventos);
            request.setAttribute("categorias", categorias);
            request.setAttribute("role", role);
            request.setAttribute("nickname", usuario);
            request.setAttribute("avatar", session.getAttribute("avatar"));
            request.setAttribute("mensaje", mensaje);
            request.setAttribute("tipoMensaje", tipoMensaje);
            
            request.getRequestDispatcher("/WEB-INF/views/registroAedicion.jsp").forward(request, response);
            
        } catch (Exception e) {
            throw new ServletException("Error procesando registro", e);
        }
    }
}