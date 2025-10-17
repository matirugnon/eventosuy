package servlets;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.time.LocalDate;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

import logica.controladores.IControladorEvento;
import logica.datatypesyenum.DTFecha;
import excepciones.EdicionExistenteException;
import excepciones.FechasIncompatiblesException;
import utils.Utils;

@WebServlet("/altaEdicion")
@MultipartConfig(maxFileSize = 5 * 1024 * 1024) // 5MB max para imÃ¡genes
public class AltaEdicionServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	
    	request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        
        try {
            // Verificar que el usuario estÃ© logueado y sea organizador
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("usuario") == null) {
                response.sendRedirect(request.getContextPath() + "/login");
                return;
            }

            String role = (String) session.getAttribute("role");
            if (!"organizador".equals(role)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Solo los organizadores pueden crear ediciones");
                return;
            }

            String nickname = (String) session.getAttribute("usuario");
            String avatar = (String) session.getAttribute("avatar");

            // Obtener parametros del formulario

            IControladorEvento ctrlEvento = IControladorEvento.getInstance();

            // Obtener eventos disponibles y categorias
            Set<String> eventos = ctrlEvento.listarEventos();
            Set<String> categoriasSet = ctrlEvento.listarCategorias();
            List<String> categorias = new ArrayList<>(categoriasSet);
            Collections.sort(categorias);

            // Pasar datos a la JSP
            request.setAttribute("eventos", eventos);
            request.setAttribute("categorias", categorias);
            request.setAttribute("nickname", nickname);
            request.setAttribute("avatar", avatar);
            request.setAttribute("role", role);

            request.getRequestDispatcher("/WEB-INF/views/altaEdicion.jsp").forward(request, response);

        } catch (Exception e) {
            throw new ServletException("Error al cargar formulario de alta de edición: " + e.getMessage(), e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	
    	request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
    	
        try {
            // Verificar que el usuario estÃ© logueado y sea organizador
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("usuario") == null) {
                response.sendRedirect(request.getContextPath() + "/login");
                return;
            }

            String role = (String) session.getAttribute("role");
            if (!"organizador".equals(role)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Solo los organizadores pueden crear ediciones");
                return;
            }

            String nickOrganizador = (String) session.getAttribute("usuario");

            String evento = request.getParameter("evento");
            String nombre = request.getParameter("nombre");
            String sigla = request.getParameter("sigla");
            String ciudad = request.getParameter("ciudad");
            String pais = request.getParameter("pais");
            String fechaInicioStr = request.getParameter("fechaInicio");
            String fechaFinStr = request.getParameter("fechaFin");

            // Validaciones bÃ¡sicas
            String error = validarDatos(evento, nombre, sigla, ciudad, pais, fechaInicioStr, fechaFinStr);
            if (error != null) {
                mostrarFormularioConError(request, response, error);
                return;
            }

            // Procesar imagen si existe
            String rutaImagen = procesarImagen(request);

            // Obtener controlador
            IControladorEvento ctrlEvento = IControladorEvento.getInstance();

            // Convertir fechas
            DTFecha fechaInicio = convertirFecha(fechaInicioStr);
            DTFecha fechaFin = convertirFecha(fechaFinStr);
            
            // Crear fecha actual para el alta
            LocalDate hoy = LocalDate.now();
            DTFecha fechaAlta = new DTFecha(hoy.getDayOfMonth(), hoy.getMonthValue(), hoy.getYear());

            // Crear ediciÃ³n (el mÃ©todo original no soporta imagen, se omite por ahora)
            ctrlEvento.altaEdicion(evento, nickOrganizador, nombre, sigla, ciudad, pais, fechaInicio, fechaFin, fechaAlta, rutaImagen);
            
            // Redirigir con mensaje de éxito usando sesión
            session.setAttribute("datosMensaje", "La edición '" + nombre + "' fue creada exitosamente");
            session.setAttribute("datosMensajeTipo", "info");
            response.sendRedirect(request.getContextPath() + "/inicio");

        } catch (EdicionExistenteException e) {
            mostrarFormularioConError(request, response, "❌ Ya existe una edición con ese nombre");
        } catch (FechasIncompatiblesException e) {
            mostrarFormularioConError(request, response, "❌ Las fechas de inicio y fin no son compatibles");
        } catch (Exception e) {
            mostrarFormularioConError(request, response, "❌ Error al crear edición: " + e.getMessage());
        }
    }

    private DTFecha convertirFecha(String fechaStr) throws IllegalArgumentException {
        try {
            // La fecha viene en formato YYYY-MM-DD del input type="date"
            String[] partes = fechaStr.split("-");
            if (partes.length == 3) {
                int anio = Integer.parseInt(partes[0]);
                int mes = Integer.parseInt(partes[1]);
                int dia = Integer.parseInt(partes[2]);
                return new DTFecha(dia, mes, anio);
            } else {
                throw new IllegalArgumentException("Formato de fecha invalido");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Formato de fecha invalido");
        }
    }

    private String procesarImagen(HttpServletRequest request) throws IOException, ServletException {
        Part imagenPart = request.getPart("imagen");
        if (imagenPart != null && imagenPart.getSize() > 0) {
            String contentType = imagenPart.getContentType();
            if (contentType != null && contentType.startsWith("image/")) {
                // Generar nombre Ãºnico para la imagen
                String extension = "";
                if (contentType.equals("image/jpeg") || contentType.equals("image/jpg")) {
                    extension = ".jpg";
                } else if (contentType.equals("image/png")) {
                    extension = ".png";
                } else if (contentType.equals("image/gif")) {
                    extension = ".gif";
                } else {
                    throw new IllegalArgumentException("Tipo de imagen no soportado");
                }
                
                String nombreArchivo = "edicion_" + System.currentTimeMillis() + extension;
                String uploadPath = getServletContext().getRealPath("/uploads/ediciones");
                
                // Crear directorio si no existe
                java.io.File uploadDir = new java.io.File(uploadPath);
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }
                
                // Guardar archivo
                String rutaCompleta = uploadPath + java.io.File.separator + nombreArchivo;
                imagenPart.write(rutaCompleta);
                
                // Retornar ruta relativa para guardar en la base de datos
                return "/uploads/ediciones/" + nombreArchivo;
            } else {
                throw new IllegalArgumentException("El archivo debe ser una imagen valida");
            }
        }
        return null; // Sin imagen
    }

    private String validarDatos(String evento, String nombre, String sigla, String ciudad, String pais, String fechaInicio, String fechaFin) {
        if (evento == null || evento.trim().isEmpty()) {
            return "Debe seleccionar un evento";
        }
        
        if (nombre == null || nombre.trim().isEmpty()) {
            return "El nombre de la edición es requerido";
        }
        if (nombre.length() > 140) {
            return "El nombre no puede tener más de 140 caracteres";
        }
        
        if (sigla == null || sigla.trim().isEmpty()) {
            return "La sigla de la edición es requerida";
        }
        if (sigla.length() > 20) {
            return "La sigla no puede tener más de 20 caracteres";
        }
        
        if (ciudad == null || ciudad.trim().isEmpty()) {
            return "La ciudad es requerida";
        }
        if (ciudad.length() > 60) {
            return "La ciudad no puede tener más de 60 caracteres";
        }
        
        if (pais == null || pais.trim().isEmpty()) {
            return "El paÃ­s es requerido";
        }
        if (pais.length() > 60) {
            return "El paí­s no puede tener más de 60 caracteres";
        }
        
        if (fechaInicio == null || fechaInicio.trim().isEmpty()) {
            return "La fecha de inicio es requerida";
        }
        
        if (fechaFin == null || fechaFin.trim().isEmpty()) {
            return "La fecha de fin es requerida";
        }
        
        // Validar que fecha fin sea posterior a fecha inicio
        try {
            LocalDate inicio = LocalDate.parse(fechaInicio);
            LocalDate fin = LocalDate.parse(fechaFin);
            
            if (fin.isBefore(inicio)) {
                return "La fecha de fin debe ser posterior a la fecha de inicio";
            }
        } catch (Exception e) {
            return "Formato de fecha invalido";
        }
        
        return null; // Sin errores
    }

    private void mostrarFormularioConError(HttpServletRequest request, HttpServletResponse response, 
                                         String error) throws ServletException, IOException {
        
        try {
            // Recargar datos necesarios
            IControladorEvento ctrlEvento = IControladorEvento.getInstance();
            Set<String> eventos = ctrlEvento.listarEventos();
            Set<String> categoriasSet = ctrlEvento.listarCategorias();
            List<String> categorias = new ArrayList<>(categoriasSet);
            Collections.sort(categorias);
            
            request.setAttribute("eventos", eventos);
            request.setAttribute("categorias", categorias);
            request.setAttribute("nickname", request.getSession().getAttribute("usuario"));
            request.setAttribute("avatar", request.getSession().getAttribute("avatar"));
            request.setAttribute("role", request.getSession().getAttribute("role"));
        } catch (Exception e) {
            // Si no se pueden cargar datos, continuar sin ellos
        }
        
        request.setAttribute("error", error);
        request.getRequestDispatcher("/WEB-INF/views/altaEdicion.jsp").forward(request, response);
    }
}
