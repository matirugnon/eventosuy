package servlets;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import logica.controladores.IControladorEvento;
import logica.datatypesyenum.DTEvento;
import logica.datatypesyenum.DTSeleccionEvento;

@WebServlet("/debug/eventos")
public class DebugEventosServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        try {
            IControladorEvento ctrl = IControladorEvento.getInstance();
            
            out.println("<!DOCTYPE html>");
            out.println("<html lang='es'>");
            out.println("<head>");
            out.println("<meta charset='UTF-8'>");
            out.println("<title>Debug - Lista de Eventos</title>");
            out.println("<style>");
            out.println("body { font-family: Arial, sans-serif; margin: 20px; background: #f5f5f5; }");
            out.println("h1 { color: #333; }");
            out.println(".evento { background: white; padding: 15px; margin: 10px 0; border-radius: 5px; box-shadow: 0 2px 4px rgba(0,0,0,0.1); }");
            out.println(".evento h2 { margin-top: 0; color: #2c3e50; }");
            out.println(".detalle { margin: 5px 0; }");
            out.println(".label { font-weight: bold; color: #555; }");
            out.println(".categorias { display: flex; gap: 5px; flex-wrap: wrap; margin-top: 10px; }");
            out.println(".categoria { background: #3498db; color: white; padding: 5px 10px; border-radius: 3px; font-size: 12px; }");
            out.println(".count { background: #27ae60; color: white; padding: 10px; border-radius: 5px; display: inline-block; margin-bottom: 20px; }");
            out.println(".empty { background: #e74c3c; color: white; padding: 15px; border-radius: 5px; }");
            out.println("</style>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>🔍 Debug - Eventos en la Base de Datos</h1>");
            
            // Obtener lista de eventos
            java.util.Set<String> nombresEventos = ctrl.listarEventos();
            
            out.println("<div class='count'>Total de eventos: " + nombresEventos.size() + "</div>");
            
            if (nombresEventos.isEmpty()) {
                out.println("<div class='empty'>");
                out.println("❌ No hay eventos en la base de datos.<br>");
                out.println("Crea uno desde: <a href='" + request.getContextPath() + "/altaEvento' style='color: white;'>Alta de Evento</a>");
                out.println("</div>");
            } else {
                // Mostrar cada evento
                for (String nombreEvento : nombresEventos) {
                    try {
                        DTSeleccionEvento dtEvento = ctrl.seleccionarEvento(nombreEvento);
                        
                        DTEvento evento = dtEvento.getEvento();
                        
                        out.println("<div class='evento'>");
                        out.println("<h2>📅 " + dtEvento.getNombre() + "</h2>");
                        out.println("<div class='detalle'><span class='label'>Sigla:</span> " + dtEvento.getSigla() + "</div>");
                        out.println("<div class='detalle'><span class='label'>Descripción:</span> " + dtEvento.getDescripcion() + "</div>");
                        
                        if (evento != null && evento.getFechaEvento() != null) {
                            out.println("<div class='detalle'><span class='label'>Fecha de Alta:</span> " + 
                                evento.getFechaEvento().getDia() + "/" + 
                                evento.getFechaEvento().getMes() + "/" + 
                                evento.getFechaEvento().getAnio() + "</div>");
                        }
                        
                        // Mostrar categorías
                        if (dtEvento.getCategorias() != null && !dtEvento.getCategorias().isEmpty()) {
                            out.println("<div class='detalle'><span class='label'>Categorías:</span></div>");
                            out.println("<div class='categorias'>");
                            for (String categoria : dtEvento.getCategorias()) {
                                out.println("<span class='categoria'>" + categoria + "</span>");
                            }
                            out.println("</div>");
                        }
                        
                        // Mostrar imagen si existe
                        if (evento != null && evento.getImagen() != null && !evento.getImagen().isEmpty()) {
                            out.println("<div class='detalle'><span class='label'>Imagen:</span> " + evento.getImagen() + "</div>");
                        }
                        
                        out.println("</div>");
                        
                    } catch (Exception e) {
                        out.println("<div class='evento' style='border-left: 4px solid #e74c3c;'>");
                        out.println("<h2>❌ " + nombreEvento + "</h2>");
                        out.println("<div class='detalle' style='color: #e74c3c;'>Error al cargar detalles: " + e.getMessage() + "</div>");
                        out.println("</div>");
                    }
                }
            }
            
            out.println("<hr style='margin: 30px 0;'>");
            out.println("<div style='background: #ecf0f1; padding: 15px; border-radius: 5px;'>");
            out.println("<h3>🔧 Opciones:</h3>");
            out.println("<ul>");
            out.println("<li><a href='" + request.getContextPath() + "/inicio'>← Volver al Inicio</a></li>");
            out.println("<li><a href='" + request.getContextPath() + "/altaEvento'>➕ Crear Nuevo Evento</a></li>");
            out.println("<li><a href='" + request.getContextPath() + "/test-soap'>🧪 Test SOAP</a></li>");
            out.println("<li><a href='?'>🔄 Recargar</a></li>");
            out.println("</ul>");
            out.println("</div>");
            
            out.println("</body>");
            out.println("</html>");
            
        } catch (Exception e) {
            out.println("<h1>❌ Error</h1>");
            out.println("<pre>");
            e.printStackTrace(out);
            out.println("</pre>");
        }
    }
}
