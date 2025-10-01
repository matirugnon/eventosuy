package servlets;

import java.io.IOException;
import java.util.Set;
import java.util.HashSet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import logica.Controladores.IControladorUsuario;
import logica.Controladores.IControladorEvento;
import logica.Controladores.IControladorRegistro;
import logica.DatatypesYEnum.DTUsuario;
import logica.DatatypesYEnum.DTAsistente;
import logica.DatatypesYEnum.DTOrganizador;
import logica.DatatypesYEnum.DTRegistro;
import logica.Usuario;
import utils.Utils;

@WebServlet("/miPerfil")
public class MiPerfilServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Verificar que el usuario esté logueado
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("usuario") == null) {
                response.sendRedirect(request.getContextPath() + "/login");
                return;
            }

            String nickname = (String) session.getAttribute("usuario");
            String role = (String) session.getAttribute("role");
            String avatar = (String) session.getAttribute("avatar");

            // Obtener los controladores
            IControladorUsuario ctrlUsuario = IControladorUsuario.getInstance();
            IControladorEvento ctrlEvento = IControladorEvento.getInstance();
            IControladorRegistro ctrlRegistro = IControladorRegistro.getInstance();

            // Carga inicial de datos si hace falta
            Set<String> usuariosExistentes = ctrlUsuario.listarUsuarios();
            if (usuariosExistentes == null || usuariosExistentes.isEmpty()) {
                Utils.cargarDatos(ctrlUsuario, ctrlEvento, ctrlRegistro);
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

            // Obtener datos específicos según el tipo de usuario
            Set<DTRegistro> registros = new HashSet<>();
            Set<String> edicionesOrganizadas = new HashSet<>();
            
            if ("Asistente".equals(tipoUsuario)) {
                // Obtener registros del asistente
                try {
                    registros = ctrlRegistro.listarRegistrosPorAsistente(nickname);
                    if (registros == null) {
                        registros = new HashSet<>();
                    }
                } catch (Exception e) {
                    System.out.println("Error obteniendo registros: " + e.getMessage());
                    registros = new HashSet<>();
                }
            } else if ("Organizador".equals(tipoUsuario)) {
                // Obtener ediciones organizadas
                try {
                    edicionesOrganizadas = ctrlUsuario.listarEdicionesOrganizador(nickname);
                } catch (Exception e) {
                    System.out.println("Error obteniendo ediciones: " + e.getMessage());
                }
            }

            // Obtener categorías para el sidebar
            Set<String> categorias = ctrlEvento.listarCategorias();

            // Pasar los datos como atributos a la JSP
            request.setAttribute("usuario", usuario);
            request.setAttribute("tipoUsuario", tipoUsuario);
            request.setAttribute("asistente", asistente);
            request.setAttribute("organizador", organizador);
            request.setAttribute("registros", registros);
            request.setAttribute("edicionesOrganizadas", edicionesOrganizadas);
            request.setAttribute("categorias", categorias);

            // Pasar datos de sesión
            request.setAttribute("nickname", nickname);
            request.setAttribute("avatar", avatar);
            request.setAttribute("role", role);
            request.setAttribute("nombre", session.getAttribute("nombre"));

            // Redirigir a la JSP
            request.getRequestDispatcher("/WEB-INF/views/miPerfil.jsp").forward(request, response);

        } catch (Exception e) {
            throw new ServletException("Error al cargar el perfil del usuario: " + e.getMessage(), e);
        }
    }
}