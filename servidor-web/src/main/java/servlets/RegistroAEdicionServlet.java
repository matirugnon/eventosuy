package servlets;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;



import soap.DTFecha;
import soap.DtEdicion;
import soap.DtEvento;
import soap.DtPatrocinio;

import soap.DtTipoDeRegistro;
import soap.EdicionNoExisteException_Exception;
import soap.PublicadorControlador;
import soap.PublicadorUsuario;
import soap.PublicadorRegistro;
import soap.StringArray;
import soap.UsuarioYaRegistradoEnEdicionException_Exception;
import utils.SoapClientHelper;




@WebServlet("/registroAedicion")
public class RegistroAEdicionServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	
    	request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        
        HttpSession session = request.getSession();
        String usuario = (String) session.getAttribute("usuario");
        String role = (String) session.getAttribute("role");
        
        // Verificar que el usuario est├⌐ logueado y sea un asistente
        if (usuario == null || !"asistente".equals(role)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        String ajaxHeader = request.getHeader("X-Requested-With");
        boolean esAjax = "XMLHttpRequest".equals(ajaxHeader) || request.getParameter("action") != null;

        if (esAjax) {
            handleAjaxRequest(request, response);
            return;
        }

        try {

            //obtener publicadores 
            PublicadorRegistro publicadorRegistro = SoapClientHelper.getPublicadorRegistro();
            PublicadorControlador publicadorControlador = SoapClientHelper.getPublicadorControlador();

            // Obtener todos los eventos con sus ediciones activas (wsimport genera un wrapper DtEventoArray)
            soap.DtEventoArray eventosArr = publicadorControlador.obtenerDTEventos();
            List<DtEvento> eventosList = new ArrayList<>();
            if (eventosArr != null && eventosArr.getItem() != null) {
                eventosList.addAll(eventosArr.getItem());
            }

            StringArray categoriasSet = publicadorControlador.listarCategorias();
            List<String> categorias = new ArrayList<>(categoriasSet.getItem());
            Collections.sort(categorias);
            request.setAttribute("categorias", categorias);
            
            // Para cada evento, obtener las ediciones activas con información completa
            for (DtEvento evento : eventosList) {
                StringArray nombresEdiciones = publicadorControlador.listarEdicionesActivasAceptadas(evento.getNombre());

                List<String> nombresEdicionesList = new ArrayList<>();
                if (nombresEdiciones != null && nombresEdiciones.getItem() != null) {
                    nombresEdicionesList.addAll(nombresEdiciones.getItem());
                }
                
                for (String nombreEdicion : nombresEdicionesList) {
                    DtEdicion edicion = publicadorControlador.consultarEdicion(nombreEdicion);
                    if (edicion != null) {
                        // Agregar tipos de registro a la edición
                        StringArray nombresTipos = publicadorRegistro.listarTipoRegistro(nombreEdicion);
                        List<DtTipoDeRegistro> tiposList = new ArrayList<>();
                        for (String nombreTipo : nombresTipos.getItem()) {
                            DtTipoDeRegistro tipo = publicadorRegistro.consultaTipoDeRegistro(nombreEdicion, nombreTipo);
                            if (tipo != null) {
                                tiposList.add(tipo);
                            }
                        }
                        // Los tipos ya están en edicion.getTiposDeRegistro(), pero necesitamos la info completa
                    }
                }
            }
            
            // Pasar los datos a la JSP
            request.setAttribute("eventos", eventosList);
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
            //obtener publicadores 
            PublicadorRegistro publicadorRegistro = SoapClientHelper.getPublicadorRegistro();
            PublicadorControlador publicadorControlador = SoapClientHelper.getPublicadorControlador();
            
            if ("getEdiciones".equals(action)) {
                String nombreEvento = request.getParameter("evento");
                if (nombreEvento != null) {
                    StringArray ediciones = publicadorControlador.listarEdicionesActivasAceptadas(nombreEvento);
                    List<String> edicionesList = new ArrayList<>();
                    if (ediciones != null && ediciones.getItem() != null) {
                        edicionesList.addAll(ediciones.getItem());
                    }

                    StringBuilder json = new StringBuilder("[");
                    boolean first = true;
                    for (String edicion : edicionesList) {
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
                    DtEdicion edicion = publicadorControlador.consultarEdicion(nombreEdicion);
                    if (edicion != null) {

                        // Obtener tipos de registro con información completa
                        StringArray nombresTipos = publicadorRegistro.listarTipoRegistro(nombreEdicion);
                        List<String> nombresTiposList = new ArrayList<>();
                        if (nombresTipos != null && nombresTipos.getItem() != null) {
                            nombresTiposList.addAll(nombresTipos.getItem());
                        }

                        StringBuilder tiposJson = new StringBuilder("[");
                        boolean first = true;
                        for (String nombreTipo : nombresTiposList) {
                            DtTipoDeRegistro tipo = publicadorRegistro.consultaTipoDeRegistro(nombreEdicion, nombreTipo);
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
                        DTFecha inicio = edicion.getFechaInicio();
                        DTFecha fin = edicion.getFechaFin();

                        String fechaInicioStr = inicio.getDia() + "/" + inicio.getMes() + "/" + inicio.getAnio();
                        String fechaFinStr = fin.getDia() + "/" + fin.getMes() + "/" + fin.getAnio();
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
    	
    	request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        
        HttpSession session = request.getSession();
        String usuario = (String) session.getAttribute("usuario");
        String role = (String) session.getAttribute("role");
        
        // Verificar que el usuario est├⌐ logueado y sea un asistente
        if (usuario == null || !"asistente".equals(role)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
    boolean isAjax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
    try {
            //obtener publicadores 
            PublicadorRegistro publicadorRegistro = SoapClientHelper.getPublicadorRegistro();
            PublicadorControlador publicadorControlador = SoapClientHelper.getPublicadorControlador();

            // Obtener parámetros del formulario
            String edicion = request.getParameter("edicion");
            String tipoRegistro = request.getParameter("tipoRegistro");
            String codigoPatrocinio = request.getParameter("codigoPatrocinio");
            
            String mensaje = "";
            String tipoMensaje = "error";
            
            if (edicion == null || edicion.trim().isEmpty()) {
                mensaje = "❌ Debe seleccionar una edición.";
            } else if (tipoRegistro == null || tipoRegistro.trim().isEmpty()) {
                mensaje = "❌ Debe seleccionar un tipo de registro.";
            } else {
                edicion = edicion.trim();
                tipoRegistro = tipoRegistro.trim();
                
                    // Verificar que la edición existe
                    DtEdicion dtEdicion = publicadorControlador.consultarEdicion(edicion);
                    if (dtEdicion == null) {
                        mensaje = "❌ La edición seleccionada no existe.";
                    } else {
                        // Verificar que el usuario no esté ya registrado
                        boolean yaRegistrado = publicadorRegistro.estaRegistrado(edicion, usuario);
                        if (yaRegistrado) {
                                    mensaje = "❌ Ya estás registrado en esta edición.";
                                } else {
                            // Verificar que el tipo de registro existe
                            DtTipoDeRegistro dtTipoRegistro = publicadorRegistro.consultaTipoDeRegistro(edicion, tipoRegistro);
                            if (dtTipoRegistro == null) {
                                mensaje = "❌ El tipo de registro seleccionado no existe.";
                            } else {
                                // Verificar cupo disponible
                                boolean cupoDisponible = !publicadorRegistro.alcanzoCupo(edicion, tipoRegistro);
                                if (!cupoDisponible) {
                                    mensaje = "❌ Ya se alcanzó el cupo para este tipo de registro.";
                                } else {
                                    double costoFinal = dtTipoRegistro.getCosto();
                                    
                                    // Procesar código de patrocinio si se proporcionó
                                    if (codigoPatrocinio != null && !codigoPatrocinio.trim().isEmpty()) {
                                        codigoPatrocinio = codigoPatrocinio.trim();
                                        
                                        try {
                                            // Verificar que el código existe en la edición
                                            boolean existeCodigo = publicadorControlador.existeCodigoPatrocinioEnEdicion(edicion, codigoPatrocinio);
                                            if (!existeCodigo) {
                                                mensaje = "❌ El código de patrocinio no es válido para esta edición.";
                                            } else {
                                                // Obtener información del patrocinio
                                                DtPatrocinio patrocinio = publicadorControlador.consultarTipoPatrocinioEdicion(edicion, codigoPatrocinio);
                                                
                                                if (patrocinio == null) {
                                                    mensaje = "❌ El código de patrocinio ingresado no es válido.";
                                                } else {
                                                    // Verificar que el patrocinio aplica al tipo de registro
                                                    if (!patrocinio.getTipoDeRegistro().equals(tipoRegistro)) {
                                                        mensaje = "❌ El código de patrocinio no es válido para este tipo de registro.";
                                                    } else {
                                                        // Verificar que aún quedan usos disponibles
                                                        if (patrocinio.getCantidadGratis() <= 0) {
                                                            mensaje = "❌ Ya se alcanzó la cantidad de usos para este código de patrocinio.";
                                                        } else {
                                                            // TODO: Verificar institución del usuario si es necesario
                                                            costoFinal = 0.0; // Registro gratuito con patrocinio
                                                        }
                                                    }
                                                }
                                            }
                                        } catch (Exception e) {
                                            // Las excepciones específicas del backend no se declaran en los stubs SOAP;
                                            // capturamos cualquier error y mostramos un mensaje genérico.
                                            mensaje = "❌ Error al validar el código de patrocinio: " + e.getMessage();
                                        }
                                    }
                                    
                                    // Si no hay errores, proceder con el registro
                                    if (mensaje.isEmpty()) {
                                        LocalDate fechaActual = LocalDate.now();
                                        DTFecha fechaRegistro = new DTFecha();
                                        fechaRegistro.setDia(fechaActual.getDayOfMonth());
                                        fechaRegistro.setMes(fechaActual.getMonthValue());
                                        fechaRegistro.setAnio(fechaActual.getYear());
                                        

                                        // Convertir DTFecha local a SOAP DTFecha antes de llamar al publicador
                                        soap.DTFecha soapFecha = new soap.DTFecha();
                                        soapFecha.setDia(fechaRegistro.getDia());
                                        soapFecha.setMes(fechaRegistro.getMes());
                                        soapFecha.setAnio(fechaRegistro.getAnio());

                                        String resultado;
                                        
                                        // Usar el método apropiado según si hay código de patrocinio o no
                                        if (codigoPatrocinio != null && !codigoPatrocinio.trim().isEmpty() && costoFinal == 0.0) {
                                            // Registro con patrocinio
                                            resultado = publicadorRegistro.altaRegistroConPatrocinio(edicion, usuario, tipoRegistro, soapFecha, codigoPatrocinio);
                                        } else {
                                            // Registro normal
                                            resultado = publicadorRegistro.altaRegistro(edicion, usuario, tipoRegistro, soapFecha, costoFinal);
                                        }

                                        if ("OK".equals(resultado)) {
                                        	String fechaStr = String.format("%02d/%02d/%d", fechaRegistro.getDia(), fechaRegistro.getMes(), fechaRegistro.getAnio());
                                            String exito = "Registro exitoso a la edición '" + edicion + "'. Fecha: " + fechaStr + " | Costo: $" + String.format("%.0f", costoFinal);
                                            if (isAjax) {
                                                response.setContentType("application/json");
                                                response.setCharacterEncoding("UTF-8");
                                                response.getWriter().write("{\"success\":true,\"message\":\"" + exito.replace("\"", "\\\"") + "\"}");
                                                return;
                                            } else {
                                                session.setAttribute("datosMensaje", exito);
                                                session.setAttribute("datosMensajeTipo", "info");
                                                response.sendRedirect(request.getContextPath() + "/registroAedicion");
                                                return;
                                            }
                                        } else {
                                            String errMsg = "❌ Error al procesar el registro: " + resultado;
                                            if (isAjax) {
                                                response.setContentType("application/json");
                                                response.setCharacterEncoding("UTF-8");
                                                response.getWriter().write("{\"success\":false,\"message\":\"" + errMsg.replace("\"", "\\\"") + "\"}");
                                                return;
                                            } else {
                                                session.setAttribute("datosMensaje", errMsg);
                                                session.setAttribute("datosMensajeTipo", "error");
                                                response.sendRedirect(request.getContextPath() + "/registroAedicion");
                                                System.out.println("DEBUG - HOLAAAA" + resultado);
                                                return;
                                            }
                                        }
                                    
                                    }
                                }
                            }
                        }
                    }
                
            }
            
            // Si llegamos aquí es porque hubo un error
            System.out.println("DEBUG - mensaje: '" + mensaje + "'");
            System.out.println("DEBUG - mensaje.isEmpty(): " + mensaje.isEmpty());
            if (!mensaje.isEmpty()) {
                System.out.println("DEBUG - Guardando mensaje en sesión: " + mensaje);
                if (isAjax) {
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter().write("{\"success\":false,\"message\":\"" + mensaje.replace("\"", "\\\"") + "\"}");
                    return;
                } else {
                    session.setAttribute("datosMensaje", mensaje);
                    session.setAttribute("datosMensajeTipo", "error");
                    response.sendRedirect(request.getContextPath() + "/registroAedicion");
                    return;
                }
            } else {
                System.out.println("DEBUG - mensaje está vacío, no se redirige con error");
            }
            
        } catch (Exception e) {
            session.setAttribute("datosMensaje", "❌ Error procesando registro: " + e.getMessage());
            session.setAttribute("datosMensajeTipo", "error");
            //response.sendRedirect(request.getContextPath() + "/registroAedicion");
        }
    }
}

