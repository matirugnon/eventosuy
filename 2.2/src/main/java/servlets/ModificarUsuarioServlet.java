package servlets;

import java.io.IOException;
import java.util.Set;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

import logica.Controladores.IControladorUsuario;
import logica.Controladores.IControladorEvento;
import logica.DatatypesYEnum.DTUsuario;
import logica.DatatypesYEnum.DTAsistente;
import logica.DatatypesYEnum.DTOrganizador;
import logica.DatatypesYEnum.DTFecha;
import excepciones.UsuarioNoExisteException;
import excepciones.FechaInvalidaException;
import utils.Utils;

@WebServlet("/modificarUsuario")
@MultipartConfig(maxFileSize = 5 * 1024 * 1024) // 5MB max para imágenes
public class ModificarUsuarioServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Verificar que el usuario esté logueado
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("usuario") == null) {
                response.sendRedirect(request.getContextPath() + "/login");
                return;
            }

            String nickname = (String) session.getAttribute("usuario");
            String role = (String) session.getAttribute("role");
            String avatar = (String) session.getAttribute("avatar");

            // Obtener los controladores
            IControladorUsuario ctrlUsuario = IControladorUsuario.getInstance();
            IControladorEvento ctrlEvento = IControladorEvento.getInstance();

            // Carga inicial de datos si hace falta
            Set<String> usuariosExistentes = ctrlUsuario.listarUsuarios();
            if (usuariosExistentes == null || usuariosExistentes.isEmpty()) {
                Utils.cargarDatos(ctrlUsuario, ctrlEvento, 
                    logica.Controladores.IControladorRegistro.getInstance());
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

            // Determinar el tipo de usuario
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

            // Obtener instituciones disponibles para asistentes
            Set<String> instituciones = null;
            if ("Asistente".equals(tipoUsuario)) {
                instituciones = ctrlUsuario.listarInstituciones();
            }

            // Obtener categorías para el sidebar
            Set<String> categorias = ctrlEvento.listarCategorias();

            // Pasar los datos como atributos a la JSP
            request.setAttribute("usuario", usuario);
            request.setAttribute("tipoUsuario", tipoUsuario);
            request.setAttribute("asistente", asistente);
            request.setAttribute("organizador", organizador);
            request.setAttribute("instituciones", instituciones);
            request.setAttribute("categorias", categorias);

            // Pasar datos de sesión
            request.setAttribute("nickname", nickname);
            request.setAttribute("avatar", avatar);
            request.setAttribute("role", role);

            // Redirigir a la JSP
            request.getRequestDispatcher("/WEB-INF/views/modificarUsuario.jsp").forward(request, response);

        } catch (Exception e) {
            throw new ServletException("Error al cargar formulario de modificación: " + e.getMessage(), e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Verificar que el usuario esté logueado
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("usuario") == null) {
                response.sendRedirect(request.getContextPath() + "/login");
                return;
            }

            String nickname = (String) session.getAttribute("usuario");
            IControladorUsuario ctrlUsuario = IControladorUsuario.getInstance();

            // Obtener datos del formulario
            String nombre = request.getParameter("nombre");
            String password = request.getParameter("password");
            String confirm = request.getParameter("confirm");

            // Validaciones básicas
            String error = validarDatosBasicos(nombre, password, confirm);
            if (error != null) {
                mostrarFormularioConError(request, response, error);
                return;
            }

            // Obtener usuario actual
            DTUsuario usuarioActual = ctrlUsuario.getDTUsuario(nickname);
            if (usuarioActual == null) {
                mostrarFormularioConError(request, response, "Usuario no encontrado");
                return;
            }

            // Procesar imagen si existe
            String rutaImagen = procesarImagen(request);
            if (rutaImagen == null) {
                rutaImagen = usuarioActual.getAvatar(); // Mantener avatar actual si no se sube uno nuevo
            }

            // Crear DTUsuario actualizado según el tipo
            DTUsuario usuarioModificado = null;
            
            if (usuarioActual instanceof DTAsistente) {
                // Procesar datos específicos de asistente
                String apellido = request.getParameter("apellido");
                String fechaNacStr = request.getParameter("fechaNac");
                String institucion = request.getParameter("institucion");
                
                if (apellido == null || apellido.trim().isEmpty()) {
                    mostrarFormularioConError(request, response, "El apellido es requerido");
                    return;
                }
                
                DTFecha fechaNac = null;
                if (fechaNacStr != null && !fechaNacStr.trim().isEmpty()) {
                    fechaNac = convertirFecha(fechaNacStr);
                }
                
                DTAsistente asistenteActual = (DTAsistente) usuarioActual;
                usuarioModificado = new DTAsistente(
                    nickname,
                    nombre,
                    usuarioActual.getCorreo(),
                    password != null && !password.trim().isEmpty() ? password : usuarioActual.getPassword(),
                    apellido,
                    fechaNac != null ? fechaNac : asistenteActual.getFechaNacimiento(),
                    institucion != null && !institucion.trim().isEmpty() ? institucion : asistenteActual.getInstitucion(),
                    rutaImagen
                );
                
            } else if (usuarioActual instanceof DTOrganizador) {
                // Procesar datos específicos de organizador
                String descripcion = request.getParameter("descripcion");
                String sitioWeb = request.getParameter("web");
                
                DTOrganizador organizadorActual = (DTOrganizador) usuarioActual;
                usuarioModificado = new DTOrganizador(
                    nickname,
                    nombre,
                    usuarioActual.getCorreo(),
                    password != null && !password.trim().isEmpty() ? password : usuarioActual.getPassword(),
                    descripcion != null ? descripcion : organizadorActual.getDescripcion(),
                    sitioWeb != null ? sitioWeb : organizadorActual.getLink(),
                    rutaImagen
                );
            }

            if (usuarioModificado == null) {
                mostrarFormularioConError(request, response, "Error procesando datos del usuario");
                return;
            }

            // Modificar usuario en el sistema
            ctrlUsuario.modificarUsuario(nickname, usuarioModificado);

            // Actualizar datos en la sesión
            session.setAttribute("avatar", rutaImagen);

            // Redirigir al perfil con mensaje de éxito
            response.sendRedirect(request.getContextPath() + "/miPerfil?mensaje=Usuario modificado exitosamente");

        } catch (UsuarioNoExisteException e) {
            mostrarFormularioConError(request, response, "Usuario no encontrado");
        } catch (FechaInvalidaException e) {
            mostrarFormularioConError(request, response, "La fecha de nacimiento no es válida");
        } catch (Exception e) {
            mostrarFormularioConError(request, response, "Error al modificar usuario: " + e.getMessage());
        }
    }

    private DTFecha convertirFecha(String fechaStr) throws FechaInvalidaException {
        try {
            // Convertir fecha en formato DD/MM/YYYY
            String[] partes = fechaStr.split("/");
            if (partes.length == 3) {
                int dia = Integer.parseInt(partes[0]);
                int mes = Integer.parseInt(partes[1]);
                int anio = Integer.parseInt(partes[2]);
                return new DTFecha(dia, mes, anio);
            } else {
                throw new FechaInvalidaException("Formato de fecha inválido. Use DD/MM/YYYY");
            }
        } catch (NumberFormatException e) {
            throw new FechaInvalidaException("Formato de fecha inválido");
        }
    }

    private String procesarImagen(HttpServletRequest request) throws IOException, ServletException {
        Part imagenPart = request.getPart("imagen");
        if (imagenPart != null && imagenPart.getSize() > 0) {
            String contentType = imagenPart.getContentType();
            if (contentType != null && contentType.startsWith("image/")) {
                // Generar nombre único para la imagen
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
                throw new IllegalArgumentException("El archivo debe ser una imagen válida");
            }
        }
        return null;
    }

    private String validarDatosBasicos(String nombre, String password, String confirm) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return "El nombre es requerido";
        }
        if (nombre.length() > 60) {
            return "El nombre no puede tener más de 60 caracteres";
        }
        
        // Solo validar contraseña si se está intentando cambiar
        if (password != null && !password.trim().isEmpty()) {
            if (password.length() < 6) {
                return "La contraseña debe tener al menos 6 caracteres";
            }
            
            if (!password.equals(confirm)) {
                return "Las contraseñas no coinciden";
            }
        }
        
        return null; // Sin errores
    }

    private void mostrarFormularioConError(HttpServletRequest request, HttpServletResponse response, 
                                         String error) throws ServletException, IOException {
        
        try {
            // Recargar datos necesarios
            String nickname = (String) request.getSession().getAttribute("usuario");
            IControladorUsuario ctrlUsuario = IControladorUsuario.getInstance();
            IControladorEvento ctrlEvento = IControladorEvento.getInstance();
            
            DTUsuario usuario = ctrlUsuario.getDTUsuario(nickname);
            Set<String> instituciones = ctrlUsuario.listarInstituciones();
            Set<String> categorias = ctrlEvento.listarCategorias();
            
            request.setAttribute("usuario", usuario);
            request.setAttribute("instituciones", instituciones);
            request.setAttribute("categorias", categorias);
            request.setAttribute("nickname", nickname);
            request.setAttribute("avatar", request.getSession().getAttribute("avatar"));
            request.setAttribute("role", request.getSession().getAttribute("role"));
        } catch (Exception e) {
            // Si no se pueden cargar datos, continuar sin ellos
        }
        
        request.setAttribute("error", error);
        request.getRequestDispatcher("/WEB-INF/views/modificarUsuario.jsp").forward(request, response);
    }
}