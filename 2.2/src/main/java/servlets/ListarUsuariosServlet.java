package servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import logica.controladores.IControladorUsuario;
import logica.controladores.IControladorEvento;
import logica.controladores.IControladorRegistro;
import logica.datatypesyenum.DTUsuario;
import logica.datatypesyenum.DTAsistente;
import logica.datatypesyenum.DTOrganizador;
import logica.Usuario;
import utils.Utils;

@WebServlet("/listarUsuarios")
public class ListarUsuariosServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Obtener los controladores
            IControladorUsuario ctrlUsuario = IControladorUsuario.getInstance();
            IControladorEvento ctrlEvento = IControladorEvento.getInstance();

            // Carga inicial de datos si hace falta
            Set<String> usuariosExistentes = ctrlUsuario.listarUsuarios();
            System.out.println("DEBUG: Usuarios existentes: " + (usuariosExistentes != null ? usuariosExistentes.size() + " - " + usuariosExistentes : "null"));
         
            if (usuariosExistentes == null || usuariosExistentes.isEmpty()) {
                System.out.println("DEBUG: Cargando datos...");
                try {
                    Utils.cargarDatos(
                        ctrlUsuario,
                        ctrlEvento,
                        IControladorRegistro.getInstance()
                    );
                    System.out.println("DEBUG: Datos cargados exitosamente.");
                } catch (Exception e) {
                    System.out.println("ERROR: Error al cargar datos: " + e.getMessage());
                    e.printStackTrace();
                }
            }

            // Obtener los nicks de los usuarios registrados
            Set<String> nicksUsuarios = ctrlUsuario.listarUsuarios();
            System.out.println("DEBUG: Nicks después de carga: " + (nicksUsuarios != null ? nicksUsuarios.size() + " - " + nicksUsuarios : "null"));

            // Usar el método más eficiente que devuelve DTUsuarios directamente
            Set<DTUsuario> usuarios = ctrlUsuario.listarUsuariosDT();
            System.out.println("DEBUG: DTUsuarios obtenidos directamente:" + (usuarios != null ? usuarios.size() : "null"));
            
            List<DTUsuario> usuariosOrdenados = new ArrayList<>(usuarios);

            usuariosOrdenados.sort(Comparator.comparing(DTUsuario::getNickname));

            Map<String, String> tiposUsuarios = new HashMap<>(); // Para almacenar los tipos
            
            if (usuarios != null && !usuarios.isEmpty()) {
                for (DTUsuario dtUsuario : usuarios) {
                    String tipo = (dtUsuario instanceof DTAsistente) ? "Asistente" : "Organizador";
                    tiposUsuarios.put(dtUsuario.getNickname(), tipo);
                    System.out.println("DEBUG: DTUsuario procesado: " + dtUsuario.getNickname() + " - " + dtUsuario.getNombre() + " (" + tipo + ")");
                }
            } else {
                System.out.println("DEBUG: usuarios DTUsuario está vacío o es null");
            }
            System.out.println("DEBUG: Total DTUsuarios en set: " + (usuarios != null ? usuarios.size() : 0));            // Obtener categorías para el sidebar
            Set<String> categorias = ctrlEvento.listarCategorias();
            
            // --- PAGINACION ---
            int pageSize = 5; // 5 usuarios por página
            int page = 1;
            String pageParam = request.getParameter("page");
            if (pageParam != null) {
                try { page = Integer.parseInt(pageParam); if (page < 1) page = 1; } 
                catch (NumberFormatException e) { page = 1; }
            }

            int totalUsuarios = usuariosOrdenados.size();
            int totalPages = (int) Math.ceil((double) totalUsuarios / pageSize);
            if (page > totalPages) page = totalPages;

            int fromIndex = (page - 1) * pageSize;
            int toIndex = Math.min(fromIndex + pageSize, totalUsuarios);
            List<DTUsuario> usuariosPagina = usuariosOrdenados.subList(fromIndex, toIndex);


            // Pasar los datos como atributos a la JSP
            request.setAttribute("usuariosOrdenados", usuariosPagina);
            request.setAttribute("usuarios", usuarios);
            request.setAttribute("tiposUsuarios", tiposUsuarios);
            request.setAttribute("categorias", categorias);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);

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

