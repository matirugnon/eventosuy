package servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
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
// import logica.controladores.IControladorEvento; // <- quitalo si no lo usás
import logica.datatypesyenum.DTUsuario;
import logica.datatypesyenum.DTAsistente;
import logica.datatypesyenum.DTOrganizador;
import utils.Utils;

@WebServlet("/listarUsuarios")
public class ListarUsuariosServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Controladores
            IControladorUsuario ctrlUsuario = IControladorUsuario.getInstance();
            // IControladorEvento ctrlEvento = IControladorEvento.getInstance(); // usar si necesitás categorías

            if (!Utils.asegurarDatosCargados(request, response)) {
                return;
            }

            Set<DTUsuario> usuarios = ctrlUsuario.listarUsuariosDT();
            if (usuarios == null) {
                usuarios = Collections.emptySet();
            }

            List<DTUsuario> usuariosOrdenados = new ArrayList<>(usuarios);
            usuariosOrdenados.sort(
                Comparator.comparing(DTUsuario::getNickname, 
                    Comparator.nullsLast(String::compareToIgnoreCase))
            );

            Map<String, String> tiposUsuarios = new HashMap<>();
            for (DTUsuario u : usuariosOrdenados) {
                if (u instanceof DTAsistente) {
                    tiposUsuarios.put(u.getNickname(), "Asistente");
                } else if (u instanceof DTOrganizador) {
                    tiposUsuarios.put(u.getNickname(), "Organizador");
                } else {
                    tiposUsuarios.put(u.getNickname(), "Usuario");
                }
            }

            // Atributos para el JSP
            request.setAttribute("usuarios", usuariosOrdenados);
            request.setAttribute("tiposUsuarios", tiposUsuarios);

            // Si luego querés el sidebar de categorías y existe el método:
            // request.setAttribute("categorias", ctrlEvento.listarCategoriasDT());

            // Forward
            request.getRequestDispatcher("/listarUsuarios.jsp").forward(request, response);

        } catch (Exception e) {
            throw new ServletException("Error al listar usuarios", e);
        }
    }
}
