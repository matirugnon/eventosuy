package servlets;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import servlets.dto.EdicionDetalleDTO;
import soap.DtAsistente;
import soap.DtEdicion;
import soap.DtRegistro;
import soap.DtUsuario;
import soap.PublicadorControlador;
import soap.PublicadorRegistro;
import soap.PublicadorUsuario;
import utils.SoapClientHelper;

@WebServlet("/descargaConstancia")
public class DescargaConstanciaServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        
        // Verificar que el usuario esté logueado
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        String role = (String) session.getAttribute("role");
        // Permitir tanto asistentes como organizadores
        if (!"asistente".equals(role) && !"organizador".equals(role)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "No tienes permisos para descargar constancias");
            return;
        }
        
        try {
            // Obtener parámetros
            String asistente = request.getParameter("asistente");
            String edicion = request.getParameter("edicion");
            String tipoRegistro = request.getParameter("tipoRegistro");
            
            if (asistente == null || edicion == null || tipoRegistro == null ||
                asistente.trim().isEmpty() || edicion.trim().isEmpty() || tipoRegistro.trim().isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Parámetros incompletos");
                return;
            }
            
            // Si es asistente, solo puede descargar su propia constancia
            String usuarioSesion = (String) session.getAttribute("usuario");
            if ("asistente".equals(role) && !asistente.equals(usuarioSesion)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "No puedes descargar constancias de otros usuarios");
                return;
            }
            
            PublicadorControlador publicadorEv = SoapClientHelper.getPublicadorControlador();
            PublicadorRegistro publicadorReg = SoapClientHelper.getPublicadorRegistro();
            PublicadorUsuario publicadorUsu = SoapClientHelper.getPublicadorUsuario();
            
            // Obtener el registro
            Set<DtRegistro> registros = new HashSet<>(publicadorReg.listarRegistrosPorAsistente(asistente).getItem());
            DtRegistro registro = null;
            
            for (DtRegistro reg : registros) {
                if (reg.getNomEdicion().equals(edicion) && reg.getTipoDeRegistro().equals(tipoRegistro)) {
                    registro = reg;
                    break;
                }
            }
            
            if (registro == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Registro no encontrado");
                return;
            }
            
            // Verificar que el asistente haya confirmado asistencia
            if (!registro.isAsistio()) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "La asistencia no ha sido confirmada");
                return;
            }
            
            // Obtener información de la edición
            EdicionDetalleDTO edicionInfo = new EdicionDetalleDTO(publicadorEv.obtenerDetalleCompletoEdicion(edicion));
            
            // Obtener información del asistente
            DtUsuario usuarioInfo = publicadorUsu.getDTUsuario(asistente);
            
            // Verificar que sea un asistente
            if (!(usuarioInfo instanceof DtAsistente)) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "El usuario no es un asistente");
                return;
            }
            DtAsistente asistenteInfo = (DtAsistente) usuarioInfo;
            
            // Generar el PDF
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", 
                "attachment; filename=\"constancia_asistencia_" + edicion.replaceAll("\\s+", "_") + ".pdf\"");
            
            // Código de patrocinio (por ahora no disponible en SOAP)
            String codigoPatrocinio = null;
            
            generarPDF(response, registro, edicionInfo, asistenteInfo, codigoPatrocinio);
            
        } catch (Exception e) {
            throw new ServletException("Error generando la constancia de asistencia", e);
        }
    }
    
    private void generarPDF(HttpServletResponse response, DtRegistro registro, 
                           EdicionDetalleDTO edicionInfo, DtAsistente asistenteInfo, String codigoPatrocinio) throws IOException {
        
        PdfWriter writer = new PdfWriter(response.getOutputStream());
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);
        
        try {
            // Color corporativo
            DeviceRgb colorPrimario = new DeviceRgb(24, 32, 128); // #182080
            
            // Banner superior de eventos.uy (mantener proporción original pero más pequeño)
            try {
                String bannerPath = "C:\\Users\\Mati\\eclipse-workspace\\tpgr15\\servidor-web\\src\\main\\webapp\\img\\banner.png";
                Image banner = new Image(ImageDataFactory.create(bannerPath));
                // Ocupar el 100% del ancho y mantener proporción
                banner.setWidth(UnitValue.createPercentValue(90));
                banner.setAutoScale(true); // Mantener proporción de la imagen original
                banner.setMarginBottom(20);
                document.add(banner);
            } catch (Exception e) {
                // Si no se encuentra el banner, continuar sin él
                System.err.println("No se pudo cargar el banner: " + e.getMessage());
                e.printStackTrace(); // Ver detalles del error
            }

            // Título
            Paragraph titulo = new Paragraph("CONSTANCIA DE ASISTENCIA")
                .setFontSize(24)
                .setBold()
                .setFontColor(colorPrimario)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(30);
            document.add(titulo);
            
            
            // Imagen de la edición (centrada)
            try {
                PublicadorControlador publicadorEv = SoapClientHelper.getPublicadorControlador();
                DtEdicion dtEdicion = publicadorEv.consultarEdicion(edicionInfo.getNombre());
                
                System.out.println("DEBUG: Intentando cargar imagen de edición");
                System.out.println("DEBUG: DtEdicion obtenido: " + (dtEdicion != null));
                
                if (dtEdicion != null) {
                    String imagenEdicion = dtEdicion.getImagen();
                    System.out.println("DEBUG: Ruta de imagen: " + imagenEdicion);
                    
                    if (imagenEdicion != null && !imagenEdicion.trim().isEmpty()) {
                        // Construir la ruta completa si es relativa
                        String imagenPath = imagenEdicion;
                        
                        // Si es una ruta relativa (no empieza con http o C:), construir ruta absoluta
                        if (!imagenEdicion.startsWith("http") && !imagenEdicion.matches("^[A-Za-z]:.*")) {
                            // Asumir que es relativa al directorio de recursos de la aplicación
                            imagenPath = "C:\\Users\\Mati\\eclipse-workspace\\tpgr15\\mobile\\src\\main\\webapp\\" + imagenEdicion.replace("/", "\\");
                            System.out.println("DEBUG: Ruta absoluta construida: " + imagenPath);
                        }
                        
                        // Intentar cargar la imagen
                        Image imgEdicion = new Image(ImageDataFactory.create(imagenPath));
                        imgEdicion.setWidth(300);
                        imgEdicion.setHorizontalAlignment(HorizontalAlignment.CENTER);
                        imgEdicion.setMarginBottom(30);
                        document.add(imgEdicion);
                        System.out.println("DEBUG: Imagen de edición agregada exitosamente");
                    } else {
                        System.out.println("DEBUG: La edición no tiene imagen configurada");
                    }
                }
            } catch (Exception e) {
                // Si no se encuentra la imagen de la edición, continuar sin ella
                System.err.println("No se pudo cargar la imagen de la edición: " + e.getMessage());
                e.printStackTrace();
            }
            
            // Texto principal
            Paragraph textoPrincipal = new Paragraph()
                .add("Se certifica que ")
                .add(new Paragraph(asistenteInfo.getNombre() + " " + asistenteInfo.getApellido())
                    .setBold()
                    .setFontSize(14)
                    .setFontColor(colorPrimario))
                .add(" asistió a la edición ")
                .add(new Paragraph(edicionInfo.getNombre())
                    .setBold()
                    .setFontSize(14)
                    .setFontColor(colorPrimario))
                .add(" del evento ")
                .add(new Paragraph(edicionInfo.getEvento())
                    .setBold()
                    .setFontSize(14)
                    .setFontColor(colorPrimario))
                .add(".")
                .setFontSize(12)
                .setTextAlignment(TextAlignment.JUSTIFIED)
                .setMarginBottom(30);
            document.add(textoPrincipal);
            
            // Tabla con detalles
            Table table = new Table(UnitValue.createPercentArray(new float[]{1, 2}))
                .setWidth(UnitValue.createPercentValue(100))
                .setMarginBottom(40);
            
            // Encabezado de la tabla
            table.addCell(createHeaderCell("Detalle"));
            table.addCell(createHeaderCell("Información"));
            
            // Filas de datos
            table.addCell(createDataCell("Asistente:"));
            table.addCell(createDataCell(asistenteInfo.getNombre() + " " + asistenteInfo.getApellido()));
            
            table.addCell(createDataCell("Nickname:"));
            table.addCell(createDataCell(asistenteInfo.getNickname()));
            
            table.addCell(createDataCell("Evento:"));
            table.addCell(createDataCell(edicionInfo.getEvento()));
            
            table.addCell(createDataCell("Edición:"));
            table.addCell(createDataCell(edicionInfo.getNombre()));
            
            table.addCell(createDataCell("Ciudad:"));
            table.addCell(createDataCell(edicionInfo.getCiudad() + ", " + edicionInfo.getPais()));
            
            table.addCell(createDataCell("Fecha del evento:"));
            table.addCell(createDataCell(
                edicionInfo.getFechaInicioDia() + "/" + 
                edicionInfo.getFechaInicioMes() + "/" + 
                edicionInfo.getFechaInicioAnio() + 
                " - " + 
                edicionInfo.getFechaFinDia() + "/" + 
                edicionInfo.getFechaFinMes() + "/" + 
                edicionInfo.getFechaFinAnio()
            ));
            
            table.addCell(createDataCell("Tipo de Registro:"));
            table.addCell(createDataCell(registro.getTipoDeRegistro()));
            
            // Código de patrocinio (si se usó)
            table.addCell(createDataCell("Patrocinio:"));
            if (codigoPatrocinio != null && !codigoPatrocinio.trim().isEmpty()) {
                table.addCell(createDataCell(codigoPatrocinio));
            } else {
                table.addCell(createDataCell("No se utilizó patrocinio"));
            }
            
            document.add(table);
            
            // Pie de página
            Paragraph pie = new Paragraph("Este documento certifica la asistencia del participante al evento mencionado.")
                .setFontSize(10)
                .setItalic()
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginTop(50);
            document.add(pie);
            
            Paragraph fecha = new Paragraph("Documento generado el " + 
                java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                .setFontSize(9)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginTop(20);
            document.add(fecha);
            
        } finally {
            document.close();
        }
    }
    
    private Cell createHeaderCell(String text) {
        return new Cell()
            .add(new Paragraph(text).setBold().setFontColor(ColorConstants.WHITE))
            .setBackgroundColor(new DeviceRgb(24, 32, 128))
            .setTextAlignment(TextAlignment.CENTER)
            .setBorder(Border.NO_BORDER)
            .setPadding(10);
    }
    
    private Cell createDataCell(String text) {
        return new Cell()
            .add(new Paragraph(text))
            .setBorder(Border.NO_BORDER)
            .setPadding(8)
            .setBackgroundColor(new DeviceRgb(245, 245, 245));
    }
}
