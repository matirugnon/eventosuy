package servlets;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import soap.PublicadorUsuario;
import utils.SoapClientHelper;

@WebServlet("/verificarDisponibilidad")
public class VerificarDisponibilidadServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        String tipo = request.getParameter("tipo"); // "nickname" o "email"
        String valor = request.getParameter("valor");
        
        if (tipo == null || valor == null || valor.trim().isEmpty()) {
            response.getWriter().write("{\"error\": \"Parámetros inválidos\"}");
            return;
        }
        
        try {
            PublicadorUsuario publicador = SoapClientHelper.getPublicadorUsuario();
            boolean existe = false;
            
            if ("nickname".equals(tipo)) {
                existe = publicador.existeNickname(valor.trim());
            } else if ("email".equals(tipo)) {
                existe = publicador.existeEmail(valor.trim());
            } else {
                response.getWriter().write("{\"error\": \"Tipo inválido\"}");
                return;
            }
            
            // Devolver JSON con el resultado
            response.getWriter().write(
                String.format("{\"existe\": %b, \"disponible\": %b}", existe, !existe)
            );
            
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("{\"error\": \"Error al verificar disponibilidad\"}");
        }
    }
}
