package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logica.controladores.IControladorUsuario;
import logica.controladores.IControladorEvento;
import excepciones.ExisteInstitucionException;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.util.Set;
import java.io.File;

@WebServlet("/altaInstitucion")
@MultipartConfig
public class AltaInstitucionServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private IControladorUsuario controladorUsuario;
    private IControladorEvento controladorEvento;

    @Override
    public void init() throws ServletException {
        super.init();
        this.controladorUsuario = IControladorUsuario.getInstance();
        this.controladorEvento = IControladorEvento.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Cargar categorías para el sidebar
        Set<String> categorias = controladorEvento.listarCategorias();
        request.setAttribute("categorias", categorias);
        request.setAttribute("nickname", request.getParameter("nickname"));
        
        // Mostrar el formulario de alta
        request.getRequestDispatcher("/WEB-INF/views/altaInstitucion.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        
        try {
            // Obtener parámetros del formulario
            String nombre = request.getParameter("nombre");
            String descripcion = request.getParameter("descripcion");
            String sitioWeb = request.getParameter("sitioWeb");
            
            // Validar parámetros obligatorios
            if (nombre == null || nombre.trim().isEmpty() ||
                descripcion == null || descripcion.trim().isEmpty() ||
                sitioWeb == null || sitioWeb.trim().isEmpty()) {
                
                Set<String> categorias = controladorEvento.listarCategorias();
                request.setAttribute("categorias", categorias);
                request.setAttribute("error", "Todos los campos obligatorios deben estar completos.");
                request.getRequestDispatcher("/WEB-INF/views/altaInstitucion.jsp")
                        .forward(request, response);
                return;
            }
            
            // Limpiar espacios en blanco
            nombre = nombre.trim();
            descripcion = descripcion.trim();
            sitioWeb = sitioWeb.trim();
            
            // Validar formato de URL
            if (!sitioWeb.matches("^https?://.*")) {
                Set<String> categorias = controladorEvento.listarCategorias();
                request.setAttribute("categorias", categorias);
                request.setAttribute("error", "El sitio web debe comenzar con http:// o https://");
                request.setAttribute("nombre", nombre);
                request.setAttribute("descripcion", descripcion);
                request.setAttribute("sitioWeb", sitioWeb);
                request.getRequestDispatcher("/WEB-INF/views/altaInstitucion.jsp")
                        .forward(request, response);
                return;
            }
            
            // Procesar logo si se subió (opcional)
            Part filePart = request.getPart("logo");
            String logoPath = null;
            if (filePart != null && filePart.getSize() > 0) {
                logoPath = procesarLogo(filePart);
            }
            
            // Crear la institución
            if (logoPath != null) {
                controladorUsuario.altaInstitucion(nombre, descripcion, sitioWeb, logoPath);
            } else {
                controladorUsuario.altaInstitucion(nombre, descripcion, sitioWeb);
            }
            
            // Redirigir con mensaje de éxito usando sesión
            HttpSession session = request.getSession();
            session.setAttribute("datosMensaje", "La institución '" + nombre + "' fue creada exitosamente");
            session.setAttribute("datosMensajeTipo", "info");
            response.sendRedirect(request.getContextPath() + "/inicio");
            
        } catch (ExisteInstitucionException e) {
            // La institución ya existe
            Set<String> categorias = controladorEvento.listarCategorias();
            request.setAttribute("categorias", categorias);
            request.setAttribute("error", "❌ Ya existe una institucion con ese nombre. Por favor ingrese uno diferente.");
            request.setAttribute("nombre", request.getParameter("nombre"));
            request.setAttribute("nickname", request.getParameter("nickname"));
            request.setAttribute("descripcion", request.getParameter("descripcion"));
            request.setAttribute("sitioWeb", request.getParameter("sitioWeb"));
            request.getRequestDispatcher("/WEB-INF/views/altaInstitucion.jsp")
                    .forward(request, response);
            
        } catch (IllegalArgumentException e) {
            // Error de validación (ej: tipo de archivo inválido)
            Set<String> categorias = controladorEvento.listarCategorias();
            request.setAttribute("categorias", categorias);
            request.setAttribute("error", e.getMessage());
            request.setAttribute("nombre", request.getParameter("nombre"));
            request.setAttribute("nickname", request.getParameter("nickname"));
            request.setAttribute("descripcion", request.getParameter("descripcion"));
            request.setAttribute("sitioWeb", request.getParameter("sitioWeb"));
            request.getRequestDispatcher("/WEB-INF/views/altaInstitucion.jsp")
                    .forward(request, response);
            
        } catch (Exception e) {
            // Error inesperado
            Set<String> categorias = controladorEvento.listarCategorias();
            request.setAttribute("categorias", categorias);
            request.setAttribute("error", "❌ Ocurrió un error inesperado: " + e.getMessage());
            request.setAttribute("nombre", request.getParameter("nombre"));
            request.setAttribute("nickname", request.getParameter("nickname"));
            request.setAttribute("descripcion", request.getParameter("descripcion"));
            request.setAttribute("sitioWeb", request.getParameter("sitioWeb"));
            request.getRequestDispatcher("/WEB-INF/views/altaInstitucion.jsp")
                    .forward(request, response);
        }
    }
    
    // Método auxiliar para procesar el logo
    private String procesarLogo(Part filePart) throws IOException {
        String fileName = getSubmittedFileName(filePart);
        if (fileName != null && !fileName.isEmpty()) {
            // Validar tipo de archivo
            String contentType = filePart.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                throw new IllegalArgumentException("El archivo debe ser una imagen");
            }
            
            String uploadsDir = getServletContext().getRealPath("/") + "uploads/logos/";
            File uploadsDirectory = new File(uploadsDir);
            if (!uploadsDirectory.exists()) {
                uploadsDirectory.mkdirs();
            }
            
            // Generar nombre único para evitar conflictos
            String extension = "";
            int dotIndex = fileName.lastIndexOf('.');
            if (dotIndex > 0) {
                extension = fileName.substring(dotIndex);
            }
            String newFileName = System.currentTimeMillis() + "_" + fileName.replaceAll("[^a-zA-Z0-9._-]", "_");
            String filePath = uploadsDir + newFileName;
            filePart.write(filePath);
            return "uploads/logos/" + newFileName;
        }
        return null;
    }
    
    private String getSubmittedFileName(Part part) {
        for (String cd : part.getHeader("content-disposition").split(";")) {
            if (cd.trim().startsWith("filename")) {
                return cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return null;
    }
}