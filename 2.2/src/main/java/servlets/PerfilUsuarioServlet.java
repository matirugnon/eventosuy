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

@WebServlet({"/perfilUsuario", "/miPerfil"})
public class PerfilUsuarioServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	
    	request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
    	
        try {
            // Determinar el nickname del usuario a consultar
            String requestPath = request.getServletPath();
            String nickname;
            HttpSession session = request.getSession(false);
            
            if ("/miPerfil".equals(requestPath)) {
                // Es la vista del propio perfil
                if (session == null || session.getAttribute("usuario") == null) {
                    response.sendRedirect(request.getContextPath() + "/login");
                    return;
                }
                nickname = (String) session.getAttribute("usuario");
            } else {
                // Es la vista del perfil de otro usuario
                nickname = request.getParameter("nickname");
                if (nickname == null || nickname.trim().isEmpty()) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Nickname no especificado");
                    return;
                }
            }
            
            // Obtener datos del usuario a través de SOAP
            PublicadorUsuario publicador = SoapClientHelper.getPublicadorUsuario();
            String tipoUsuario = publicador.obtenerTipoUsuario(nickname);
            
            if ("desconocido".equals(tipoUsuario)) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Usuario no encontrado");
                return;
            }
            
            String avatar = publicador.obtenerAvatar(nickname);
            
            // Atributos para el JSP
            request.setAttribute("nickname", nickname);
            request.setAttribute("tipoUsuario", tipoUsuario);
            request.setAttribute("avatar", avatar);
            
            // Atributos de sesión para la cabecera
            if (session != null) {
                request.setAttribute("sessionNickname", session.getAttribute("usuario"));
                request.setAttribute("sessionAvatar", session.getAttribute("avatar"));
                request.setAttribute("sessionRole", session.getAttribute("role"));
            }
            
            // Forward al JSP correspondiente
            if ("/miPerfil".equals(requestPath)) {
                request.getRequestDispatcher("/WEB-INF/views/miPerfil.jsp").forward(request, response);
            } else {
                request.getRequestDispatcher("/WEB-INF/views/perfilUsuario.jsp").forward(request, response);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al consultar el perfil: " + e.getMessage());
        }
    }
}
