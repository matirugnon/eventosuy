package servlets;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import logica.Controladores.IControladorUsuario;
import logica.Usuario;

@WebServlet("/listarUsuarios")
public class ListarUsuariosServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Obtener el controlador de usuarios
            IControladorUsuario ctrlUsuario = IControladorUsuario.getInstance();

            // Obtener los nicks de los usuarios registrados
            Set<String> nicksUsuarios = ctrlUsuario.listarUsuarios();
            

            // Convertir los nicks en objetos Usuario
            Set<Usuario> usuarios = new HashSet<>();
            for (String nick : nicksUsuarios) {
                Usuario usuario = ctrlUsuario.obtenerUsuario(nick);
                if (usuario != null) {
                    usuarios.add(usuario);
                }
            }
           

            // Pasar los usuarios como atributo a la JSP
            request.setAttribute("usuarios", usuarios);

            // Pasar datos de sesión (nickname, avatar, nombre, role)
            request.setAttribute("nickname", request.getSession().getAttribute("usuario"));
            request.setAttribute("avatar", request.getSession().getAttribute("avatar"));
            request.setAttribute("role", request.getSession().getAttribute("role"));
            request.setAttribute("nombre", request.getSession().getAttribute("nombre"));

            // Redirigir a la JSP
            request.getRequestDispatcher("/WEB-INF/views/listarUsuarios.jsp").forward(request, response);

        } catch (Exception e) {
            throw new ServletException("Error al listar usuarios: " + e.getMessage(), e);
        }
    }
}
