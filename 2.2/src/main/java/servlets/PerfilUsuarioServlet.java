package servlets;

import java.io.IOException;
import java.util.Set;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import logica.Controladores.IControladorUsuario;
import logica.Controladores.IControladorEvento;
import logica.Controladores.IControladorRegistro;
import logica.DatatypesYEnum.DTUsuario;
import logica.DatatypesYEnum.DTAsistente;
import logica.DatatypesYEnum.DTOrganizador;
import utils.Utils;

@WebServlet("/perfilUsuario")
public class PerfilUsuarioServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Obtener el nickname del usuario a consultar
            String nickname = request.getParameter("nickname");
            
            if (nickname == null || nickname.trim().isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Nickname de usuario requerido");
                return;
            }

            // Obtener los controladores
            IControladorUsuario ctrlUsuario = IControladorUsuario.getInstance();
            IControladorEvento ctrlEvento = IControladorEvento.getInstance();

            // Carga inicial de datos si hace falta
            Set<String> usuariosExistentes = ctrlUsuario.listarUsuarios();
            if (usuariosExistentes == null || usuariosExistentes.isEmpty()) {
                Utils.cargarDatos(
                    ctrlUsuario,
                    ctrlEvento,
                    IControladorRegistro.getInstance()
                );
            }

            // Verificar que el usuario existe
            if (!ctrlUsuario.ExisteNickname(nickname)) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Usuario no encontrado");
                return;
            }

            // Obtener el DTUsuario específico
            DTUsuario usuario = ctrlUsuario.getDTUsuario(nickname);
            
            if (usuario == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "No se pudo obtener la información del usuario");
                return;
            }

            // Determinar el tipo de usuario y obtener información específica
            String tipoUsuario;
            DTAsistente asistente = null;
            DTOrganizador organizador = null;
            
            if (usuario instanceof DTAsistente) {
                tipoUsuario = "Asistente";
                asistente = (DTAsistente) usuario;
            } else if (usuario instanceof DTOrganizador) {
                tipoUsuario = "Organizador";
                organizador = (DTOrganizador) usuario;
            } else {
                tipoUsuario = "Usuario";
            }

            // Obtener categorías para el sidebar
            Set<String> categorias = ctrlEvento.listarCategorias();

            // Pasar los datos como atributos a la JSP
            request.setAttribute("usuario", usuario);
            request.setAttribute("tipoUsuario", tipoUsuario);
            request.setAttribute("asistente", asistente);
            request.setAttribute("organizador", organizador);
            request.setAttribute("categorias", categorias);

            // Pasar datos de sesión (nickname, avatar, nombre, role)
            request.setAttribute("nickname", request.getSession().getAttribute("usuario"));
            request.setAttribute("avatar", request.getSession().getAttribute("avatar"));
            request.setAttribute("role", request.getSession().getAttribute("role"));
            request.setAttribute("nombre", request.getSession().getAttribute("nombre"));

            // Redirigir a la JSP
            request.getRequestDispatcher("/WEB-INF/views/perfilUsuario.jsp").forward(request, response);

        } catch (Exception e) {
            throw new ServletException("Error al obtener el perfil del usuario: " + e.getMessage(), e);
        }
    }
}