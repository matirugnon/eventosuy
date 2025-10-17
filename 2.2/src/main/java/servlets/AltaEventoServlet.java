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

import logica.controladores.IControladorEvento;
import logica.datatypesyenum.DTFecha;
import excepciones.EventoRepetidoException;
import excepciones.CategoriaNoSeleccionadaException;
import excepciones.FechaInvalidaException;
import utils.Utils;

@WebServlet("/altaEvento")
@MultipartConfig(maxFileSize = 5 * 1024 * 1024) // 5MB max para imÃ¡genes
public class AltaEventoServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Verificar que el usuario estÃ© logueado y sea organizador
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("usuario") == null) {
                response.sendRedirect(request.getContextPath() + "/login");
                return;
            }

            String role = (String) session.getAttribute("role");
            if (!"organizador".equals(role)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Solo los organizadores pueden crear eventos");
                return;
            }

            String nickname = (String) session.getAttribute("usuario");
            String avatar = (String) session.getAttribute("avatar");

            // Obtener los controladores
            IControladorEvento ctrlEvento = IControladorEvento.getInstance();
            Set<String> categorias = ctrlEvento.listarCategorias();

            // Pasar datos a la JSP
            request.setAttribute("categorias", categorias);
            request.setAttribute("nickname", nickname);
            request.setAttribute("avatar", avatar);
            request.setAttribute("role", role);

            request.getRequestDispatcher("/WEB-INF/views/altaEvento.jsp").forward(request, response);

        } catch (Exception e) {
            throw new ServletException("Error al cargar formulario de alta de evento: " + e.getMessage(), e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Verificar que el usuario estÃ© logueado y sea organizador
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("usuario") == null) {
                response.sendRedirect(request.getContextPath() + "/login");
                return;
            }

            String role = (String) session.getAttribute("role");
            if (!"organizador".equals(role)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Solo los organizadores pueden crear eventos");
                return;
            }

            // Obtener parÃ¡metros del formulario
            String nombre = request.getParameter("nombre");
            String sigla = request.getParameter("sigla");
            String descripcion = request.getParameter("descripcion");
            String[] categoriasSeleccionadas = request.getParameterValues("categorias");

            // Validaciones bÃ¡sicas
            String error = validarDatos(nombre, sigla, descripcion, categoriasSeleccionadas);
            if (error != null) {
                mostrarFormularioConError(request, response, error);
                return;
            }

            // Procesar imagen si existe
            String rutaImagen = procesarImagen(request);

            // Obtener controlador
            IControladorEvento ctrlEvento = IControladorEvento.getInstance();

            // Crear fecha actual para el alta
            java.time.LocalDate hoy = java.time.LocalDate.now();
            DTFecha fechaAlta = new DTFecha(hoy.getDayOfMonth(), hoy.getMonthValue(), hoy.getYear());

            // Convertir array de categorÃ­as a Set
            Set<String> categoriasSet = new java.util.HashSet<>();
            for (String categoria : categoriasSeleccionadas) {
                categoriasSet.add(categoria);
            }

            // Crear evento (el mÃ©todo original no soporta imagen, se omite por ahora)
            ctrlEvento.darAltaEvento(nombre, descripcion, fechaAlta, sigla, categoriasSet);

            // Redirigir con mensaje de Ã©xito
            response.sendRedirect(request.getContextPath() + "/consultaEvento?evento=" + nombre + "&mensaje=Evento creado exitosamente");

        } catch (EventoRepetidoException e) {
            mostrarFormularioConError(request, response, "Ya existe un evento con ese nombre");
        } catch (CategoriaNoSeleccionadaException e) {
            mostrarFormularioConError(request, response, "Debe seleccionar al menos una categoria");
        } catch (FechaInvalidaException e) {
            mostrarFormularioConError(request, response, "La fecha del evento no es valida");
        } catch (Exception e) {
            mostrarFormularioConError(request, response, "Error al crear evento: " + e.getMessage());
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
                
                String nombreArchivo = "evento_" + System.currentTimeMillis() + extension;
                String uploadPath = getServletContext().getRealPath("/uploads/eventos");
                
                // Crear directorio si no existe
                java.io.File uploadDir = new java.io.File(uploadPath);
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }
                
                // Guardar archivo
                String rutaCompleta = uploadPath + java.io.File.separator + nombreArchivo;
                imagenPart.write(rutaCompleta);
                
                // Retornar ruta relativa para guardar en la base de datos
                return "/uploads/eventos/" + nombreArchivo;
            } else {
                throw new IllegalArgumentException("El archivo debe ser una imagen valida");
            }
        }
        return null; // Sin imagen
    }

    private String validarDatos(String nombre, String sigla, String descripcion, String[] categorias) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return "El nombre del evento es requerido";
        }
        if (nombre.length() > 120) {
            return "El nombre no puede tener más de 120 caracteres";
        }
        
        if (sigla == null || sigla.trim().isEmpty()) {
            return "La sigla del evento es requerida";
        }
        if (sigla.length() > 15) {
            return "La sigla no puede tener mÃ¡s de 15 caracteres";
        }
        
        if (descripcion == null || descripcion.trim().isEmpty()) {
            return "La descripción del evento es requerida";
        }
        if (descripcion.length() > 800) {
            return "La descripción no puede tener mÃ¡s de 800 caracteres";
        }
        
        if (categorias == null || categorias.length == 0) {
            return "Debe seleccionar al menos una categori­a";
        }
        
        return null; // Sin errores
    }

    private void mostrarFormularioConError(HttpServletRequest request, HttpServletResponse response, 
                                         String error) throws ServletException, IOException {
        
        try {
            // Recargar datos necesarios
            IControladorEvento ctrlEvento = IControladorEvento.getInstance();
            Set<String> categorias = ctrlEvento.listarCategorias();
            
            request.setAttribute("categorias", categorias);
            request.setAttribute("nickname", request.getSession().getAttribute("usuario"));
            request.setAttribute("avatar", request.getSession().getAttribute("avatar"));
            request.setAttribute("role", request.getSession().getAttribute("role"));
        } catch (Exception e) {
            // Si no se pueden cargar datos, continuar sin ellos
        }
        
        request.setAttribute("error", error);
        request.getRequestDispatcher("/WEB-INF/views/altaEvento.jsp").forward(request, response);
    }
}
