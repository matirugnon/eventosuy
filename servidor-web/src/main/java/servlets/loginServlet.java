package servlets;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import soap.PublicadorUsuario;
import utils.SoapClientHelper;

@WebServlet("/login")
public class loginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	
    	request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        
        // Simplemente mostrar la página de login
        request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	
    	request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	
    	request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        
        try {
            String usuario = request.getParameter("usuario");
            String password = request.getParameter("password");

            // Validación de parámetros
            if (usuario == null || usuario.isEmpty() || password == null || password.isEmpty()) {
                request.setAttribute("error", "Usuario y contraseña son obligatorios.");
                request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
                return;
            }

            // Obtener cliente SOAP
            PublicadorUsuario publicador = SoapClientHelper.getPublicadorUsuario();
            
            // Validar credenciales via SOAP
            boolean credencialesValidas = publicador.validarCredenciales(usuario, password);
            
            if (credencialesValidas) {
                // Obtener tipo de usuario via SOAP
                String tipoUsuario = publicador.obtenerTipoUsuario(usuario);
                
                // Obtener avatar del usuario via SOAP
                String avatar = publicador.obtenerAvatar(usuario);
                
                // Crear sesión
                HttpSession session = request.getSession();
                session.setAttribute("usuario", usuario);
                session.setAttribute("avatar", avatar);
                session.setAttribute("role", tipoUsuario);
                
                response.sendRedirect(request.getContextPath() + "/inicio");
                return;
            } else {
                request.setAttribute("error", "Usuario o contraseña incorrectos.");
            }

            // Recargar la página de login con el mensaje de error
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);

        } catch (Exception e) {
            System.err.println("[loginServlet] Error: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "Error al procesar el login: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
        }
    }
}
