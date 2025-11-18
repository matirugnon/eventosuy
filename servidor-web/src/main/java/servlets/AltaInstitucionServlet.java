package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import soap.ExisteInstitucionException_Exception;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.io.File;

import soap.PublicadorControlador;
import soap.PublicadorUsuario;
import soap.StringArray;
import utils.SoapClientHelper;


@WebServlet("/altaInstitucion")
@MultipartConfig
public class AltaInstitucionServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    public void init() throws ServletException {
        super.init();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	
    	HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        String nickname = (String) session.getAttribute("usuario");
        request.setAttribute("nickname", nickname);

        //cargar publicadores
        
        PublicadorControlador publicadorControlador = SoapClientHelper.getPublicadorControlador();

        
        // Cargar categorÃ­as para el sidebar
        // Obtener todas las categorías para el sidebar (ordenadas alfabéticamente)
        StringArray categoriasSet = publicadorControlador.listarCategorias();
        List<String> categorias = new ArrayList<>(categoriasSet.getItem());
        Collections.sort(categorias);
        request.setAttribute("categorias", categorias);

        
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

            PublicadorControlador publicadorControlador = SoapClientHelper.getPublicadorControlador();
            // Obtener parÃ¡metros del formulario
            String nombre = request.getParameter("nombre");
            String descripcion = request.getParameter("descripcion");
            String sitioWeb = request.getParameter("sitioWeb");
            
            // Validar parÃ¡metros obligatorios
            if (nombre == null || nombre.trim().isEmpty() ||
                descripcion == null || descripcion.trim().isEmpty() ||
                sitioWeb == null || sitioWeb.trim().isEmpty()) {
                
                StringArray categoriasArray = publicadorControlador.listarCategorias();
                List<String> categorias = new ArrayList<>(categoriasArray.getItem());
                
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
                HttpSession sesionActual = request.getSession(false);
                String nicknameActual = (String) sesionActual.getAttribute("usuario");

                StringArray categoriasArray = publicadorControlador.listarCategorias();
                List<String> categorias = new ArrayList<>(categoriasArray.getItem());

                request.setAttribute("categorias", categorias);
                request.setAttribute("error", "El sitio web debe comenzar con http:// o https://");
                request.setAttribute("nombre", nombre);
                request.setAttribute("nickname", nicknameActual);
                request.setAttribute("descripcion", descripcion);
                request.setAttribute("sitioWeb", sitioWeb);
                request.getRequestDispatcher("/WEB-INF/views/altaInstitucion.jsp")
                        .forward(request, response);
                return;
            }
            
            // Procesar logo si se subiÃ³ (opcional)
            Part filePart = request.getPart("logo");
            String logoPath = null;
            if (filePart != null && filePart.getSize() > 0) {
                logoPath = procesarLogo(filePart);
            }
            PublicadorUsuario publicadorusuario = SoapClientHelper.getPublicadorUsuario();
            // Crear la instituciÃ³n
            if (logoPath != null) {
                publicadorusuario.altaInstitucionConLogo(nombre, descripcion, sitioWeb, logoPath);
            } else {
                publicadorusuario.altaInstitucion(nombre, descripcion, sitioWeb);
            }
            
            // Mensaje de éxito: permitir mostrar inline o redirigir con flash
            String stay = request.getParameter("stay");
            if ("true".equalsIgnoreCase(stay) || "on".equalsIgnoreCase(stay)) {
                // Mostrar el mismo formulario con mensaje de éxito (no redirigir)
                request.setAttribute("success", "La institución '" + nombre + "' fue creada exitosamente");
                request.setAttribute("nombre", "");
                request.setAttribute("descripcion", "");
                request.setAttribute("sitioWeb", "");
                // Cargar categorias para el sidebar (igual que en doGet)
                StringArray categoriasArray = publicadorControlador.listarCategorias();
                List<String> categorias = new ArrayList<>();
                if (categoriasArray != null && categoriasArray.getItem() != null) categorias.addAll(categoriasArray.getItem());
                Collections.sort(categorias);
                request.setAttribute("categorias", categorias);
                request.getRequestDispatcher("/WEB-INF/views/altaInstitucion.jsp").forward(request, response);
            } else {
                // Flash message en sesión y redirección a la página anterior (referer) o /inicio
                HttpSession session = request.getSession();
                session.setAttribute("datosMensaje", "La institución '" + nombre + "' fue creada exitosamente");
                session.setAttribute("datosMensajeTipo", "info");
                String referer = request.getHeader("Referer");
                if (referer != null && referer.contains(request.getContextPath())) {
                    // Extraer path relativa al contextPath
                    String path = referer.substring(referer.indexOf(request.getContextPath()) + request.getContextPath().length());
                    if (path == null || path.isEmpty()) path = "/";
                    response.sendRedirect(request.getContextPath() + path);
                } else {
                    response.sendRedirect(request.getContextPath() + "/inicio");
                }
            }
            
    } catch (ExisteInstitucionException_Exception e) {
            // La instituciÃ³n ya existe
            PublicadorControlador publicadorControlador = SoapClientHelper.getPublicadorControlador();
            StringArray categoriasArray = publicadorControlador.listarCategorias();
            List<String> categorias = new ArrayList<>(categoriasArray.getItem());
            request.setAttribute("categorias", categorias);
            request.setAttribute("error", "Ya existe una institucion con ese nombre. Por favor ingrese uno diferente.");
            request.setAttribute("nombre", request.getParameter("nombre"));
            request.setAttribute("nickname", request.getParameter("nickname"));
            request.setAttribute("descripcion", request.getParameter("descripcion"));
            request.setAttribute("sitioWeb", request.getParameter("sitioWeb"));
            request.getRequestDispatcher("/WEB-INF/views/altaInstitucion.jsp")
                    .forward(request, response);
            
        } catch (IllegalArgumentException e) {
            // Error de validaciÃ³n (ej: tipo de archivo invÃ¡lido)
            PublicadorControlador publicadorControlador = SoapClientHelper.getPublicadorControlador();
            StringArray categoriasArray = publicadorControlador.listarCategorias();
            List<String> categorias = new ArrayList<>(categoriasArray.getItem());
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
            PublicadorControlador publicadorControlador = SoapClientHelper.getPublicadorControlador();
            StringArray categoriasArray = publicadorControlador.listarCategorias();
            List<String> categorias = new ArrayList<>(categoriasArray.getItem());
            request.setAttribute("categorias", categorias);
            request.setAttribute("error", "⚠️ Ocurrió un error inesperado: " + e.getMessage());
            request.setAttribute("nombre", request.getParameter("nombre"));
            request.setAttribute("nickname", request.getParameter("nickname"));
            request.setAttribute("descripcion", request.getParameter("descripcion"));
            request.setAttribute("sitioWeb", request.getParameter("sitioWeb"));
            request.getRequestDispatcher("/WEB-INF/views/altaInstitucion.jsp")
                    .forward(request, response);
        }
    }
    
    // MÃ©todo auxiliar para procesar el logo
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

