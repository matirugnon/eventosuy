package servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import soap.DTFecha;
import soap.DtAsistente;
import soap.DtOrganizador;
import soap.DtUsuario;
import soap.FechaInvalidaException_Exception;
import soap.PublicadorControlador;
import soap.PublicadorUsuario;
import soap.UsuarioNoExisteException_Exception;
import utils.SoapClientHelper;



@WebServlet("/modificarUsuario")
@MultipartConfig(maxFileSize = 5 * 1024 * 1024) // 5MB max para imÃ¡genes
public class ModificarUsuarioServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	
    	request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
    	
        try {
            // Verificar que el usuario estÃ© logueado
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("usuario") == null) {
                response.sendRedirect(request.getContextPath() + "/login");
                return;
            }

            String nickname = (String) session.getAttribute("usuario");
            String role = (String) session.getAttribute("role");
            String avatar = (String) session.getAttribute("avatar");

            // Obtener los controladores
            PublicadorUsuario publicadorUs = SoapClientHelper.getPublicadorUsuario();
            
            PublicadorControlador publicadorEv = SoapClientHelper.getPublicadorControlador();
    
            // Verificar que el usuario existe
            if (!publicadorUs.existeNickname(nickname)) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Usuario no encontrado");
                return;
            }

            // Obtener el DTUsuario especÃ­fico
            DtUsuario usuario = publicadorUs.getDTUsuario(nickname);
            
            if (usuario == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "No se pudo obtener la informaciÃ³n del usuario");
                return;
            }

            // Determinar el tipo de usuario
            String tipoUsuario;
            DtAsistente asistente = null;
            DtOrganizador organizador = null;
            
            if (usuario instanceof DtAsistente) {
                tipoUsuario = "Asistente";
                asistente = (DtAsistente) usuario;
            } else if (usuario instanceof DtOrganizador) {
                tipoUsuario = "Organizador";
                organizador = (DtOrganizador) usuario;
            } else {
                tipoUsuario = "Usuario";
            }

            // Obtener instituciones disponibles para asistentes
            Set<String> instituciones = null;
            if ("Asistente".equals(tipoUsuario)) {
                instituciones = new HashSet<>(publicadorUs.listarInstituciones().getItem());
            }

            // Obtener categorÃ­as para el sidebar (ordenadas alfabéticamente)
            Set<String> categoriasSet = new HashSet<>(publicadorEv.listarCategorias().getItem());
            List<String> categorias = new ArrayList<>(categoriasSet);
            Collections.sort(categorias);

            // Pasar los datos como atributos a la JSP
            request.setAttribute("usuario", usuario);
            request.setAttribute("tipoUsuario", tipoUsuario);
            request.setAttribute("asistente", asistente);
            request.setAttribute("organizador", organizador);
            request.setAttribute("instituciones", instituciones);
            request.setAttribute("categorias", categorias);

            // Pasar datos de sesiÃ³n
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
    	
    	request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
    	
        try {
            // Verificar que el usuario estÃ© logueado
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("usuario") == null) {
                response.sendRedirect(request.getContextPath() + "/login");
                return;
            }

            String nickname = (String) session.getAttribute("usuario");
            
            PublicadorUsuario publicadorUs = SoapClientHelper.getPublicadorUsuario();
           

            // Obtener datos del formulario
            String nombre = request.getParameter("nombre");
            String password = request.getParameter("password");
            String confirm = request.getParameter("confirm");

            // Validaciones bÃ¡sicas
            String error = validarDatosBasicos(nombre, password, confirm);
            if (error != null) {
                mostrarFormularioConError(request, response, error);
                return;
            }

            // Obtener usuario actual
            DtUsuario usuarioActual = publicadorUs.getDTUsuario(nickname);
            if (usuarioActual == null) {
                mostrarFormularioConError(request, response, "Usuario no encontrado");
                return;
            }

            // Procesar imagen si existe
            String rutaImagen = procesarImagen(request);
            if (rutaImagen == null) {
                rutaImagen = usuarioActual.getAvatar(); // Mantener avatar actual si no se sube uno nuevo
            }

            // Crear DTUsuario actualizado segÃºn el tipo
            DtUsuario usuarioModificado = null;
            
            if (usuarioActual instanceof DtAsistente) {
                // Procesar datos especÃ­ficos de asistente
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
                
                DtAsistente asistenteActual = (DtAsistente) usuarioActual;
                DtAsistente asistenteModificado = new DtAsistente(); // tipo correcto
                asistenteModificado.setNickname(nickname);
                asistenteModificado.setNombre(nombre);
                asistenteModificado.setCorreo(usuarioActual.getCorreo());
                asistenteModificado.setPassword(password != null && !password.trim().isEmpty()
                    ? password : usuarioActual.getPassword());
                asistenteModificado.setApellido(apellido);
                asistenteModificado.setFechaNacimiento(fechaNac != null ? fechaNac : asistenteActual.getFechaNacimiento());
                asistenteModificado.setInstitucion(institucion != null && !institucion.trim().isEmpty()
                    ? institucion : asistenteActual.getInstitucion());
                asistenteModificado.setAvatar(rutaImagen);
                
                usuarioModificado = asistenteModificado;
                
            } else if (usuarioActual instanceof DtOrganizador) {
                // Procesar datos especÃ­ficos de organizador
                String descripcion = request.getParameter("descripcion");
                String sitioWeb = request.getParameter("web");
                
                DtOrganizador organizadorActual = (DtOrganizador) usuarioActual;
                DtOrganizador organizadorModificado = new DtOrganizador();
                organizadorModificado.setNickname(nickname);
                organizadorModificado.setNombre(nombre);
                organizadorModificado.setCorreo(usuarioActual.getCorreo());
                organizadorModificado.setPassword(password != null && !password.trim().isEmpty()
                    ? password : usuarioActual.getPassword());
                organizadorModificado.setDescripcion(descripcion != null ? descripcion : organizadorActual.getDescripcion());
                organizadorModificado.setLink(sitioWeb != null ? sitioWeb : organizadorActual.getLink());
                organizadorModificado.setAvatar(rutaImagen);
                
                usuarioModificado = organizadorModificado; 
            
            }

            if (usuarioModificado == null) {
                mostrarFormularioConError(request, response, "Error procesando datos del usuario");
                return;
            }

            // Modificar usuario en el sistema
            publicadorUs.modificarUsuario(nickname, usuarioModificado);

            // Actualizar datos en la sesión
            session.setAttribute("avatar", rutaImagen);

            // Redirigir al perfil con mensaje de éxito
            session.setAttribute("datosMensaje", "El perfil del usuario '" + nickname + "' fue modificado exitosamente");
            session.setAttribute("datosMensajeTipo", "info");
            response.sendRedirect(request.getContextPath() + "/miPerfil");

        } catch (UsuarioNoExisteException_Exception e) {
            mostrarFormularioConError(request, response, "❌ Usuario no encontrado");
        } catch (FechaInvalidaException_Exception e) {
            mostrarFormularioConError(request, response, "❌ La fecha de nacimiento no es válida");
        } catch (Exception e) {
            mostrarFormularioConError(request, response, "❌ Error al modificar usuario: " + e.getMessage());
        }
    }

    private DTFecha convertirFecha(String fechaStr) throws Exception {
        try {
            // Convertir fecha en formato DD/MM/YYYY
            String[] partes = fechaStr.split("/");
            if (partes.length == 3) {
                int dia = Integer.parseInt(partes[0]);
                int mes = Integer.parseInt(partes[1]);
                int anio = Integer.parseInt(partes[2]);
                DTFecha fech =  new DTFecha();
                fech.setDia(dia);
                fech.setMes(mes);
                fech.setAnio(anio);
                return fech;
            } else {
            	throw new Exception("Formato de fecha inválido. Use DD/MM/YYYY");
            }
        } catch (NumberFormatException e) {
            throw new Exception("Formato de fecha inválido");
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

    private String validarDatosBasicos(String nombre, String password, String confirm) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return "El nombre es requerido";
        }
        if (nombre.length() > 60) {
            return "El nombre no puede tener mÃ¡s de 60 caracteres";
        }
        
        // Solo validar contraseÃ±a si se estÃ¡ intentando cambiar
        if (password != null && !password.trim().isEmpty()) {
            if (password.length() < 6) {
                return "La contraseÃ±a debe tener al menos 6 caracteres";
            }
            
            if (!password.equals(confirm)) {
                return "Las contraseÃ±as no coinciden";
            }
        }
        
        return null; // Sin errores
    }

    private void mostrarFormularioConError(HttpServletRequest request, HttpServletResponse response, 
                                         String error) throws ServletException, IOException {
        
        try {
            // Recargar datos necesarios
            String nickname = (String) request.getSession().getAttribute("usuario");
            PublicadorUsuario publicadorUs = SoapClientHelper.getPublicadorUsuario();
            PublicadorControlador publicadorEv = SoapClientHelper.getPublicadorControlador();
            
            DtUsuario usuario = publicadorUs.getDTUsuario(nickname);
            Set<String> instituciones = new HashSet<>(publicadorUs.listarInstituciones().getItem());
            Set<String> categoriasSet = new HashSet<>(publicadorEv.listarCategorias().getItem());
            List<String> categorias = new ArrayList<>(categoriasSet);
            Collections.sort(categorias);
            
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

