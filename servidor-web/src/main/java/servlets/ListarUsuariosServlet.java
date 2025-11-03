package servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import soap.DtAsistente;
import soap.DtUsuario;
import soap.DtUsuarioArray;
import soap.PublicadorControlador;
import soap.PublicadorUsuario;
import soap.StringArray;
import utils.SoapClientHelper;
import utils.Utils;

@WebServlet("/listarUsuarios")
public class ListarUsuariosServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	
    	request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
    	
        try {
        	PublicadorUsuario publicadorUs = SoapClientHelper.getPublicadorUsuario();
        	PublicadorControlador publicadorEv = SoapClientHelper.getPublicadorControlador();
            
            // Obtener los nicks de los usuarios registrados
        	StringArray nickArr = publicadorUs.listarUsuarios();
            Set<String> nicksUsuarios = new HashSet<>(nickArr.getItem());
            System.out.println("DEBUG: Nicks después de carga: " + (nicksUsuarios != null ? nicksUsuarios.size() + " - " + nicksUsuarios : "null"));

            // Usar el método más eficiente que devuelve DTUsuarios directamente
            DtUsuarioArray dtUsArr =  publicadorUs.listarUsuariosDT();
            Set<DtUsuario> usuarios = new HashSet<>(dtUsArr.getItem());
            System.out.println("DEBUG: DTUsuarios obtenidos directamente:" + (usuarios != null ? usuarios.size() : "null"));
            
            List<DtUsuario> usuariosOrdenados = new ArrayList<>(usuarios);

            usuariosOrdenados.sort(Comparator.comparing(DtUsuario::getNickname));
            
            
            // Obtener lista de usuarios seguidosl
            HttpSession session = request.getSession(false);
            String nicknameActual = null;
            Set<String> seguidos = new HashSet<>();

            if (session != null && session.getAttribute("usuario") != null) {
                nicknameActual = (String) session.getAttribute("usuario");

                try {
                    
                    StringArray seguidosArr = publicadorUs.obtenerSeguidos(nicknameActual);
                    if (seguidosArr != null && seguidosArr.getItem() != null) {
                        for (String s : seguidosArr.getItem()) {
                            if (s != null) {
                                seguidos.add(s);
                            }
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            
            request.setAttribute("seguidos", seguidos);

            Map<String, String> tiposUsuarios = new HashMap<>(); // Para almacenar los tipos
            
            if (usuarios != null && !usuarios.isEmpty()) {
                for (DtUsuario dtUsuario : usuarios) {
                    String tipo = (dtUsuario instanceof DtAsistente) ? "Asistente" : "Organizador";
                    tiposUsuarios.put(dtUsuario.getNickname(), tipo);
                    System.out.println("DEBUG: DTUsuario procesado: " + dtUsuario.getNickname() + " - " + dtUsuario.getNombre() + " (" + tipo + ")");
                }
            } else {
                System.out.println("DEBUG: usuarios DTUsuario está vacío o es null");
            }
            System.out.println("DEBUG: Total DTUsuarios en set: " + (usuarios != null ? usuarios.size() : 0));            // Obtener categorías para el sidebar (ordenadas alfabéticamente)
            StringArray catArr = publicadorEv.listarCategorias();
            List<String> categorias = new ArrayList<>(catArr.getItem());
            Collections.sort(categorias);
            
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
            if (totalPages == 0) {
                page = 1;
            } else if (page > totalPages) {
                page = totalPages;
            }

            List<DtUsuario> usuariosPagina;
            if (totalUsuarios == 0) {
                usuariosPagina = new ArrayList<>();
            } else {
                int fromIndex = Math.max(0, (page - 1) * pageSize);
                int toIndex = Math.min(fromIndex + pageSize, totalUsuarios);
                usuariosPagina = usuariosOrdenados.subList(fromIndex, toIndex);
            }


            // Pasar los datos como atributos a la JSP
            request.setAttribute("usuariosOrdenados", usuariosPagina);
            request.setAttribute("usuarios", usuarios);
            request.setAttribute("tiposUsuarios", tiposUsuarios);
            request.setAttribute("categorias", categorias);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);

            // Pasar datos de sesión (nickname, avatar, nombre, role)
            request.setAttribute("nickname", nicknameActual);
            if (session != null) {
                request.setAttribute("avatar", session.getAttribute("avatar"));
                request.setAttribute("role", session.getAttribute("role"));
                request.setAttribute("nombre", session.getAttribute("nombre"));
            }

            // Redirigir a la JSP
            request.getRequestDispatcher("/WEB-INF/views/listarUsuarios.jsp").forward(request, response);

        } catch (Exception e) {
            throw new ServletException("Error al listar usuarios: " + e.getMessage(), e);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String accion = request.getParameter("accion");
        String seguido = request.getParameter("seguido");
        String seguidor = (String) request.getSession().getAttribute("usuario");

        if (accion != null && seguido != null && seguidor != null && !seguidor.equals(seguido)) {
            try {
                PublicadorUsuario publicadorUs = SoapClientHelper.getPublicadorUsuario();
                if ("seguir".equals(accion)) {
                    publicadorUs.seguirUsuario(seguidor, seguido);
                } else if ("dejar".equals(accion)) {
                    publicadorUs.dejarSeguirUsuario(seguidor, seguido);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Luego de realizar la acción, recargamos la lista
        doGet(request, response);
    }
}


