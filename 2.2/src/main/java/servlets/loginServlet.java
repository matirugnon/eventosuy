package servlets;

import java.io.IOException;
import java.util.Set;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import logica.controladores.IControladorUsuario;
import logica.Usuario;
import logica.datatypesyenum.DTUsuario;
import logica.manejadores.ManejadorUsuario; // Importar ManejadorUsuario
import utils.Utils;


@WebServlet("/login")
public class loginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response); // Reutilizar lÃ³gica comÃºn
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response); // Reutilizar lÃ³gica comÃºn
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            IControladorUsuario ctrlUsuario = IControladorUsuario.getInstance();
            ManejadorUsuario manejador = ManejadorUsuario.getinstance();

            String usuario = request.getParameter("usuario");
            String password = request.getParameter("password");

            // ValidaciÃ³n de parÃ¡metros
            if (usuario == null || usuario.isEmpty() || password == null || password.isEmpty()) {
                request.setAttribute("error", "Usuario y contraseña son obligatorios.");
                request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
                return;
            }

            if (!Utils.asegurarDatosCargados(request, response)) {
                return;
            }
            Set<String> usuarios = ctrlUsuario.listarUsuarios();
            request.setAttribute("usuarios", usuarios);

            Usuario user = manejador.obtenerUsuario(usuario);
            if (user != null && user.getPassword().equals(password)) {
                DTUsuario dtUsuario = ctrlUsuario.getDTUsuario(usuario);
                HttpSession session = request.getSession();
                session.setAttribute("usuario", dtUsuario.getNickname());
                session.setAttribute("avatar", dtUsuario.getAvatar()); // Agregado el atributo avatar
                session.setAttribute("role", user.getTipo()); // Guardar el rol en la sesiÃ³n
                response.sendRedirect(request.getContextPath() + "/inicio"); // Redirigir a la pÃ¡gina de inicio
                return;
            } else {
                request.setAttribute("error", "Usuario o contraseÃ±a incorrectos.");
            }

            // Recargar la pÃ¡gina de login con el mensaje de error
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);

        } catch (Exception e) {
            throw new ServletException("Error procesando el login: " + e.getMessage(), e);
        }
    }
}
