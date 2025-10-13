package servlets;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Set;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import logica.controladores.IControladorUsuario;
import logica.datatypesyenum.DTFecha;
import excepciones.UsuarioRepetidoException;
import excepciones.CorreoInvalidoException;
import excepciones.FechaInvalidaException;
import utils.Utils;

@WebServlet("/signup")
@MultipartConfig(maxFileSize = 5 * 1024 * 1024) // 5MB max para imÃ¡genes
public class SignupServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        if (!Utils.asegurarDatosCargados(request, response)) {
            return;
        }

        try {
            // Obtener lista de instituciones para el dropdown
            IControladorUsuario ctrl = IControladorUsuario.getInstance();
            Set<String> instituciones = ctrl.listarInstituciones();
            
            request.setAttribute("instituciones", instituciones);
            request.getRequestDispatcher("/WEB-INF/views/altadeusuario.jsp").forward(request, response);
            
        } catch (Exception e) {
            request.setAttribute("error", "Error cargando formulario: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/altadeusuario.jsp").forward(request, response);
        }
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        if (!Utils.asegurarDatosCargados(request, response)) {
            return;
        }

        try {
            // Obtener parÃ¡metros del formulario
            String rol = request.getParameter("rol");
            String nickname = request.getParameter("nickname");
            String nombre = request.getParameter("nombre");
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            String confirm = request.getParameter("confirm");
            
            // Validaciones bÃ¡sicas
            String error = validarDatosBasicos(nickname, nombre, email, password, confirm);
            if (error != null) {
                mostrarFormularioConError(request, response, error);
                return;
            }
            
            IControladorUsuario ctrl = IControladorUsuario.getInstance();
            
            // Procesar segÃºn el rol
            if ("asistente".equals(rol)) {
                crearAsistente(request, ctrl, nickname, nombre, email, password);
            } else if ("organizador".equals(rol)) {
                crearOrganizador(request, ctrl, nickname, nombre, email, password);
            } else {
                mostrarFormularioConError(request, response, "Rol no vÃ¡lido");
                return;
            }
            
            // Ã‰xito - redirigir al inicio
            response.sendRedirect(request.getContextPath() + "/inicio?mensaje=Usuario creado exitosamente");
            
        } catch (UsuarioRepetidoException e) {
            mostrarFormularioConError(request, response, "El nickname o email ya estÃ¡ en uso");
        } catch (CorreoInvalidoException e) {
            mostrarFormularioConError(request, response, "El correo electrÃ³nico no es vÃ¡lido");
        } catch (FechaInvalidaException e) {
            mostrarFormularioConError(request, response, "La fecha de nacimiento no es vÃ¡lida");
        } catch (IllegalArgumentException e) {
            mostrarFormularioConError(request, response, e.getMessage());
        } catch (Exception e) {
            mostrarFormularioConError(request, response, "Error al crear usuario: " + e.getMessage());
        }
    }
    
    private void crearAsistente(HttpServletRequest request, IControladorUsuario ctrl, 
                              String nickname, String nombre, String email, String password) 
                              throws Exception {
        
        String apellido = request.getParameter("apellido");
        String fechaNacStr = request.getParameter("fechaNacimiento");
        String institucion = request.getParameter("institucion");
        
        if (apellido == null || apellido.trim().isEmpty()) {
            throw new IllegalArgumentException("El apellido es requerido para asistentes");
        }
        
        if (fechaNacStr == null || fechaNacStr.trim().isEmpty()) {
            throw new IllegalArgumentException("La fecha de nacimiento es requerida para asistentes");
        }
        
        // Convertir fecha
        DTFecha fechaNac = convertirFecha(fechaNacStr);
        
        // Procesar imagen si existe
        String rutaImagen = procesarImagen(request);
        
        // Crear asistente
        if (rutaImagen != null) {
            // Si hay imagen, usar el mÃ©todo con avatar
            if (institucion != null && !institucion.trim().isEmpty()) {
                ctrl.altaAsistente(nickname, nombre, email, apellido, fechaNac, institucion, password, rutaImagen);
            } else {
                ctrl.altaAsistente(nickname, nombre, email, apellido, fechaNac, "", password, rutaImagen);
            }
        } else {
            // Si no hay imagen, usar el mÃ©todo original
            if (institucion != null && !institucion.trim().isEmpty()) {
                ctrl.altaAsistente(nickname, nombre, email, apellido, fechaNac, institucion, password);
            } else {
                ctrl.altaAsistente(nickname, nombre, email, apellido, fechaNac, "", password);
            }
        }
    }
    
    private void crearOrganizador(HttpServletRequest request, IControladorUsuario ctrl,
                                String nickname, String nombre, String email, String password) 
                                throws Exception {
        
        String descripcion = request.getParameter("descripcion");
        String sitioWeb = request.getParameter("web");
        
        // Procesar imagen si existe
        String rutaImagen = procesarImagen(request);
        
        // Crear organizador
        if (rutaImagen != null) {
            // Si hay imagen, usar el mÃ©todo con avatar
            ctrl.altaOrganizador(nickname, nombre, email, descripcion, sitioWeb, password, rutaImagen);
        } else {
            // Si no hay imagen, usar el mÃ©todo original
            ctrl.altaOrganizador(nickname, nombre, email, descripcion, sitioWeb, password);
        }
    }
    
    private DTFecha convertirFecha(String fechaStr) throws FechaInvalidaException {
        try {
            LocalDate fecha = LocalDate.parse(fechaStr, DateTimeFormatter.ISO_LOCAL_DATE);
            return new DTFecha(fecha.getDayOfMonth(), fecha.getMonthValue(), fecha.getYear());
        } catch (DateTimeParseException e) {
            throw new FechaInvalidaException("Formato de fecha invÃ¡lido");
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
                
                String nombreArchivo = "user_" + System.currentTimeMillis() + extension;
                String uploadPath = getServletContext().getRealPath("/uploads/usuarios");
                
                // Crear directorio si no existe
                java.io.File uploadDir = new java.io.File(uploadPath);
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }
                
                // Guardar archivo
                String rutaCompleta = uploadPath + java.io.File.separator + nombreArchivo;
                imagenPart.write(rutaCompleta);
                
                // Retornar ruta relativa para guardar en la base de datos
                return "/uploads/usuarios/" + nombreArchivo;
            } else {
                throw new IllegalArgumentException("El archivo debe ser una imagen vÃ¡lida");
            }
        }
        return null;
    }
    
    private String validarDatosBasicos(String nickname, String nombre, String email, 
                                     String password, String confirm) {
        
        if (nickname == null || nickname.trim().isEmpty()) {
            return "El nickname es requerido";
        }
        if (nickname.length() > 30) {
            return "El nickname no puede tener mÃ¡s de 30 caracteres";
        }
        
        if (nombre == null || nombre.trim().isEmpty()) {
            return "El nombre es requerido";
        }
        if (nombre.length() > 60) {
            return "El nombre no puede tener mÃ¡s de 60 caracteres";
        }
        
        if (email == null || email.trim().isEmpty()) {
            return "El correo electrÃ³nico es requerido";
        }
        
        if (password == null || password.length() < 6) {
            return "La contraseÃ±a debe tener al menos 6 caracteres";
        }
        
        if (!password.equals(confirm)) {
            return "Las contraseÃ±as no coinciden";
        }
        
        return null; // Sin errores
    }
    
    private void mostrarFormularioConError(HttpServletRequest request, HttpServletResponse response, 
                                         String error) throws ServletException, IOException {
        
        try {
            // Recargar instituciones
            IControladorUsuario ctrl = IControladorUsuario.getInstance();
            Set<String> instituciones = ctrl.listarInstituciones();
            request.setAttribute("instituciones", instituciones);
        } catch (Exception e) {
            // Si no se pueden cargar instituciones, continuar sin ellas
        }
        
        request.setAttribute("error", error);
        request.getRequestDispatcher("/WEB-INF/views/altadeusuario.jsp").forward(request, response);
    }
}
