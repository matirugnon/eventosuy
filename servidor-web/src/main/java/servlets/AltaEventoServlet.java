package servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

import soap.PublicadorControlador;
import soap.DTFecha;
import soap.StringArray;
// Bridge temporal: sincronizar con lógica local hasta migrar listados a SOAP

import logica.controladores.IControladorEvento;
import java.util.HashSet;
import java.util.Arrays;
import utils.SoapClientHelper;

@WebServlet("/altaEvento")
@MultipartConfig(maxFileSize = 5 * 1024 * 1024) // 5MB max para imÃ¡genes
public class AltaEventoServlet extends HttpServlet {

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
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Solo los organizadores pueden crear eventos");
                return;
            }

            String nickname = (String) session.getAttribute("usuario");
            String avatar = (String) session.getAttribute("avatar");

            // Obtener categorías desde el Servidor Central vía SOAP
            List<String> categorias = new ArrayList<>();
            try {
                PublicadorControlador publicador = SoapClientHelper.getPublicadorControlador();
                StringArray categoriasWs = publicador.listarCategorias();
                if (categoriasWs != null && categoriasWs.getItem() != null) {
                    categorias.addAll(categoriasWs.getItem());
                    Collections.sort(categorias);
                }
            } catch (Exception ex) {
                // Si falla el servidor central, mostramos el formulario igual sin categorías
                // y dejamos un mensaje informativo en el request.
                request.setAttribute("warning", "No se pudieron cargar las categorías desde el Servidor Central");
            }

            // Fallback: si no hay categorías en el servidor central (lista vacía),
            // mostramos un set mínimo para que el formulario sea usable y avisamos.
            if (categorias.isEmpty()) {
                categorias.add("Tecnología");
                categorias.add("Innovación");
                categorias.add("Cultura");
                categorias.add("Deporte");
                categorias.add("Música");
                Collections.sort(categorias);
                request.setAttribute("warning", "No hay categorías cargadas en el Servidor Central. Se muestran categorías por defecto");
            }

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
    	
    	request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
    	
        try {
            // Verificar que el usuario esta logueado y sea organizador
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

            // Procesar imagen si existe (por ahora no se envía via SOAP, pendiente de implementar)
            String rutaImagen = procesarImagen(request);
            // JAX-WS no acepta null en parámetros, usar cadena vacía si no hay imagen
            if (rutaImagen == null) {
                rutaImagen = "";
            }

            // Usar el PublicadorControlador via SOAP
            PublicadorControlador publicador = SoapClientHelper.getPublicadorControlador();

            // Crear fecha actual para el alta
            java.time.LocalDate hoy = java.time.LocalDate.now();
            DTFecha fechaAlta = new DTFecha();
            fechaAlta.setDia(hoy.getDayOfMonth());
            fechaAlta.setMes(hoy.getMonthValue());
            fechaAlta.setAnio(hoy.getYear());

            // Convertir array de categorías a StringArray para SOAP
            StringArray categoriasArray = new StringArray();
            for (String categoria : categoriasSeleccionadas) {
                categoriasArray.getItem().add(categoria);
            }

            // Crear evento vía SOAP (Servidor Central)
            String resultado = publicador.darAltaEvento(nombre, descripcion, fechaAlta, sigla, categoriasArray, rutaImagen);
            
            // Si no retorna "OK", es un mensaje de error específico
            if (!"OK".equals(resultado)) {
                mostrarFormularioConError(request, response, "❌ " + resultado);
                return;
            }

            // Bridge temporal: reflejar el evento en la lógica local para que el listado actual lo vea
            // (hasta migrar inicioServlet y consultas a SOAP)
            try {
                IControladorEvento ctrlLocal = IControladorEvento.getInstance();
                logica.datatypesyenum.DTFecha fechaLocal = new logica.datatypesyenum.DTFecha(
                        fechaAlta.getDia(), fechaAlta.getMes(), fechaAlta.getAnio());
                // Convertir cadena vacía a null para el bridge local
                String rutaImagenLocal = rutaImagen.isEmpty() ? null : rutaImagen;
                ctrlLocal.darAltaEvento(nombre, descripcion, fechaLocal, sigla,
                        new HashSet<>(Arrays.asList(categoriasSeleccionadas)), rutaImagenLocal);
            } catch (Exception ignore) {
                // Si falla por duplicado u otra razón, no bloqueamos el flujo.
            }

            // Redirigir con mensaje de éxito usando sesión
            session.setAttribute("datosMensaje", "El evento '" + nombre + "' fue creado exitosamente");
            session.setAttribute("datosMensajeTipo", "info");
            response.sendRedirect(request.getContextPath() + "/inicio");

        } catch (Exception e) {
            e.printStackTrace();
            mostrarFormularioConError(request, response, "❌ Error al crear evento: " + e.getMessage());
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
            // TODO: Obtener categorías via SOAP cuando el cliente esté regenerado
            List<String> categorias = new ArrayList<>();
            categorias.add("Deportes");
            categorias.add("Cultura");
            categorias.add("Tecnología");
            categorias.add("Música");
            categorias.add("Arte");
            Collections.sort(categorias);
            
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

