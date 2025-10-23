package servlets;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	
    	request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
    	
        // Invalidar la sesión actual
        HttpSession session = request.getSession(false); // Obtener la sesión actual si existe
        if (session != null) {
            session.invalidate(); // Invalidar la sesión
        }

        // Redirigir al inicio (inicio.jsp) sin sesión
        response.sendRedirect(request.getContextPath() + "/inicio");
    }
}
