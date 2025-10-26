package servlets;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Set;



import java.util.HashSet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import soap.CorreoInvalidoException_Exception;
import soap.FechaInvalidaException_Exception;
import soap.PublicadorUsuario;
import soap.StringArray;
import soap.UsuarioRepetidoException_Exception;
import utils.SoapClientHelper;

@WebServlet("/signup")
@MultipartConfig(maxFileSize = 5 * 1024 * 1024) // 5MB max para imágenes
public class SignupServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        try {
            PublicadorUsuario publicador = SoapClientHelper.getPublicadorUsuario();
            StringArray institucionesArray = publicador.listarInstituciones();
            institucionesArray = institucionesArray != null ? institucionesArray : new StringArray();
            Set<String> instituciones = new HashSet<>(institucionesArray.getItem());

            request.setAttribute("instituciones", instituciones);
            request.getRequestDispatcher("/WEB-INF/views/altadeusuario.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "Error cargando formulario: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/altadeusuario.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        try {
            String rol = request.getParameter("rol");
            String nickname = request.getParameter("nickname");
            String nombre = request.getParameter("nombre");
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            String confirm = request.getParameter("confirm");

            String error = validarDatosBasicos(nickname, nombre, email, password, confirm);
            if (error != null) {
                mostrarFormularioConError(request, response, error);
                return;
            }

            PublicadorUsuario publicador = SoapClientHelper.getPublicadorUsuario();

            if ("asistente".equals(rol)) {
                crearAsistente(request, response, publicador, nickname, nombre, email, password);
            } else if ("organizador".equals(rol)) {
                crearOrganizador(request, response, publicador, nickname, nombre, email, password);
            } else {
                mostrarFormularioConError(request, response, "Rol no válido");
                return;
            }

            HttpSession session = request.getSession();
            session.setAttribute("datosMensaje", "El usuario '" + nickname + "' fue creado exitosamente");
            session.setAttribute("datosMensajeTipo", "info");
            response.sendRedirect(request.getContextPath() + "/inicio");

        } catch (UsuarioRepetidoException_Exception e) {
            mostrarFormularioConError(request, response, "❌ El nickname o email ya está en uso");
        } catch (CorreoInvalidoException_Exception e) {
            mostrarFormularioConError(request, response, "❌ El correo electrónico no es válido");
        } catch (FechaInvalidaException_Exception e) {
            mostrarFormularioConError(request, response, "❌ La fecha de nacimiento no es válida");
        } catch (Exception e) {
            mostrarFormularioConError(request, response, "❌ Error al crear usuario: " + e.getMessage());
        }
    }

    private void crearAsistente(HttpServletRequest request, HttpServletResponse response, PublicadorUsuario publicador,
            		String nickname, String nombre, String email, String password) 
            throws soap.UsuarioRepetidoException_Exception,CorreoInvalidoException_Exception,FechaInvalidaException_Exception,
            	IOException, ServletException {

        String apellido = request.getParameter("apellido");
        String fechaNacStr = request.getParameter("fechaNacimiento");
        String institucion = request.getParameter("institucion");

        if (apellido == null || apellido.trim().isEmpty()) {
            mostrarFormularioConError(request, response, "El apellido es requerido para asistentes");
            return;
        }

        if (fechaNacStr == null || fechaNacStr.trim().isEmpty()) {
            mostrarFormularioConError(request, response, "La fecha de nacimiento es requerida para asistentes");
            return;
        }

        // Parsear fecha
        LocalDate fecha;
        try{
        	fecha = parseFecha(fechaNacStr);
        } catch (soap.FechaInvalidaException_Exception e) {
            mostrarFormularioConError(request, response, "❌ La fecha de nacimiento no es válida");
            return;
        }
        int dia = fecha.getDayOfMonth();
        int mes = fecha.getMonthValue();
        int anio = fecha.getYear();

        String rutaImagen = procesarImagen(request);

        // Llamada SOAP para crear asistente - usar "" si no hay imagen
        try {
            publicador.altaAsistente(nickname, nombre, email, apellido, dia, mes, anio,
                    institucion != null ? institucion : "", 
                    password, 
                    rutaImagen != null ? rutaImagen : "");
        } catch (soap.UsuarioRepetidoException_Exception e) {
            mostrarFormularioConError(request, response, "⚠️ " + e.getMessage());
        } catch (soap.CorreoInvalidoException_Exception e) {
            mostrarFormularioConError(request, response, "⚠️ " + e.getMessage());
        } catch (soap.FechaInvalidaException_Exception e) {
            mostrarFormularioConError(request, response, "⚠️ " + e.getMessage());
        }
    }

    private void crearOrganizador(HttpServletRequest request, HttpServletResponse response, PublicadorUsuario publicador,
            String nickname, String nombre, String email, String password) throws UsuarioRepetidoException_Exception,CorreoInvalidoException_Exception, IOException, ServletException {

        String descripcion = request.getParameter("descripcion");
        String sitioWeb = request.getParameter("web");

        String rutaImagen = procesarImagen(request);

        try {
            if (rutaImagen != null) {
                publicador.altaOrganizador(nickname, nombre, email, descripcion, sitioWeb, password, rutaImagen);
            } else {
                publicador.altaOrganizadorSinAvatar(nickname, nombre, email, descripcion, sitioWeb, password);
            }
        } catch (UsuarioRepetidoException_Exception e) {
        	 mostrarFormularioConError(request, response, e.getMessage());
        } catch (CorreoInvalidoException_Exception e) {
        	 mostrarFormularioConError(request, response, e.getMessage());
        }
    }

    private LocalDate parseFecha(String fechaStr) throws FechaInvalidaException_Exception {
        try {
            return LocalDate.parse(fechaStr, DateTimeFormatter.ISO_LOCAL_DATE);
        } catch (DateTimeParseException e) {
            throw new FechaInvalidaException_Exception("Formato de fecha inválido",null);
        }
    }

    private String procesarImagen(HttpServletRequest request) throws IOException, ServletException {
        Part imagenPart = request.getPart("imagen");
        if (imagenPart != null && imagenPart.getSize() > 0) {
            String contentType = imagenPart.getContentType();
            if (contentType != null && contentType.startsWith("image/")) {
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
                java.io.File uploadDir = new java.io.File(uploadPath);
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }

                String rutaCompleta = uploadPath + java.io.File.separator + nombreArchivo;
                imagenPart.write(rutaCompleta);
                return "/uploads/usuarios/" + nombreArchivo;
            } else {
                throw new IllegalArgumentException("El archivo debe ser una imagen válida");
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
            return "El nickname no puede tener más de 30 caracteres";
        }

        if (nombre == null || nombre.trim().isEmpty()) {
            return "El nombre es requerido";
        }
        if (nombre.length() > 60) {
            return "El nombre no puede tener más de 60 caracteres";
        }

        if (email == null || email.trim().isEmpty()) {
            return "El correo electrónico es requerido";
        }

        if (password == null || password.length() < 6) {
            return "La contraseña debe tener al menos 6 caracteres";
        }

        if (!password.equals(confirm)) {
            return "Las contraseñas no coinciden";
        }

        return null; // Sin errores
    }

    private void mostrarFormularioConError(HttpServletRequest request, HttpServletResponse response,
            String error) throws ServletException, IOException {
        try {
            PublicadorUsuario publicador = SoapClientHelper.getPublicadorUsuario();
            StringArray institucionesArray = publicador.listarInstituciones();
            institucionesArray = institucionesArray != null ? institucionesArray : new StringArray();
            Set<String> instituciones = new HashSet<>(institucionesArray.getItem());
            request.setAttribute("instituciones", instituciones);
        } catch (Exception e) {
            // continuar sin instituciones si falla
        }

        request.setAttribute("error", error);
        request.getRequestDispatcher("/WEB-INF/views/altadeusuario.jsp").forward(request, response);
    }
}

