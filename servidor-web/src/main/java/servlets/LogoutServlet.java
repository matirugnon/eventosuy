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
    	
        // Invalidar la sesiÃ³n actual
        HttpSession session = request.getSession(false); // Obtener la sesiÃ³n actual si existe
        if (session != null) {
            session.invalidate(); // Invalidar la sesiÃ³n
        }

        // Redirigir al inicio (inicio.jsp) sin sesiÃ³n
        response.sendRedirect(request.getContextPath() + "/inicio");
    }
}

