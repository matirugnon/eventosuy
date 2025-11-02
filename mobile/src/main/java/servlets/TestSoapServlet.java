package servlets;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import soap.PublicadorControlador;
import utils.SoapClientHelper;

@WebServlet("/test-soap")
public class TestSoapServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Test SOAP Connection</title>");
        out.println("<style>");
        out.println("body { font-family: Arial, sans-serif; margin: 40px; }");
        out.println("h1 { color: #333; }");
        out.println(".success { color: green; }");
        out.println(".error { color: red; }");
        out.println(".info { color: blue; }");
        out.println("pre { background: #f4f4f4; padding: 10px; border-radius: 5px; }");
        out.println("</style>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>Test de Conexión SOAP - PublicadorControlador</h1>");
        
        try {
            // Verificar si el servidor está disponible
            out.println("<h2>1. Verificando disponibilidad del servidor...</h2>");
            boolean available = SoapClientHelper.isServerAvailable();
            
            if (!available) {
                out.println("<p class='error'>❌ El servidor SOAP no está disponible</p>");
                out.println("<p>Asegúrate de que el ServidorCentral esté ejecutándose en http://localhost:9128/publicador</p>");
                out.println("</body></html>");
                return;
            }
            
            out.println("<p class='success'>✓ Servidor SOAP disponible</p>");
            
            // Obtener el cliente SOAP
            PublicadorControlador publicador = SoapClientHelper.getPublicadorControlador();
            
            // Test 1: Método hola()
            out.println("<h2>2. Test método hola()</h2>");
            String holaResponse = publicador.hola();
            out.println("<p class='success'>✓ Respuesta: " + holaResponse + "</p>");
            
            // Test 2: Método obtenerEventos()
            out.println("<h2>3. Test método obtenerEventos()</h2>");
            String eventosStr = publicador.obtenerEventos();
            out.println("<p class='info'>Eventos (como string): " + eventosStr + "</p>");
            
            // Nota: Los métodos listarEventos() y listarCategorias() estarán disponibles 
            // después de regenerar el cliente SOAP
            out.println("<h2>4. Info</h2>");
            out.println("<p class='info'>Los métodos listarEventos() y listarCategorias() ");
            out.println("estarán disponibles después de regenerar el cliente SOAP con: </p>");
            out.println("<pre>mvn clean jaxws:wsimport</pre>");
            
            out.println("<h2 class='success'>✅ Tests básicos pasaron exitosamente</h2>");
            out.println("<p><a href='" + request.getContextPath() + "/'>Volver al inicio</a></p>");
            
        } catch (Exception e) {
            out.println("<h2 class='error'>❌ Error al conectar con SOAP</h2>");
            out.println("<pre>");
            e.printStackTrace(out);
            out.println("</pre>");
        }
        
        out.println("</body>");
        out.println("</html>");
    }
}
