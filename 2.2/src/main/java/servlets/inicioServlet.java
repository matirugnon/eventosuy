package servlets;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import logica.Controladores.IControladorEvento;
import logica.Controladores.IControladorRegistro;
import logica.Controladores.IControladorUsuario;
import logica.DatatypesYEnum.DTEvento;
import utils.Utils;

@WebServlet("/inicio")
public class inicioServlet extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    try {
      IControladorEvento ctrl = IControladorEvento.getInstance();

      // Carga inicial de datos si hace falta
      Set<DTEvento> eventos = ctrl.obtenerDTEventos();
      if (eventos == null || eventos.isEmpty()) {
        Utils.cargarDatos(
            IControladorUsuario.getInstance(),
            ctrl,
            IControladorRegistro.getInstance()
        );
        eventos = ctrl.obtenerDTEventos();
      }

      // Categorías para el sidebar
      Set<String> categorias = ctrl.listarCategorias();

      // ---- FILTRO POR CATEGORÍA ----
      String categoriaSeleccionada = request.getParameter("categoria"); // p.ej. "Tecnología"
      if (categoriaSeleccionada != null && !categoriaSeleccionada.isBlank()
          && !"todas".equalsIgnoreCase(categoriaSeleccionada)) {

        // Supuesto: cada DTEvento expone getCategorias(): Set<String>
        // (si fuera getCategoria() String, adapta el predicado del filter).
        final String cat = categoriaSeleccionada;
        eventos = eventos.stream()
            .filter(e -> e.getCategorias() != null &&
                         e.getCategorias().stream()
                           .anyMatch(c -> c.equalsIgnoreCase(cat)))
            .collect(Collectors.toCollection(LinkedHashSet::new)); // conserva orden
      }

      // Atributos para la JSP
      request.setAttribute("eventos", eventos);
      request.setAttribute("categorias", categorias);
      request.setAttribute("categoriaSeleccionada", 
          (categoriaSeleccionada == null || categoriaSeleccionada.isBlank()) ? "todas" : categoriaSeleccionada);

      // Obtener el rol desde la sesión y pasarlo a la JSP
      String role = (String) request.getSession().getAttribute("role");
      request.setAttribute("role", role);
      request.setAttribute("nickname", request.getSession().getAttribute("usuario"));
      request.setAttribute("avatar", request.getSession().getAttribute("avatar"));

      request.getRequestDispatcher("/WEB-INF/views/inicio.jsp").forward(request, response);

    } catch (Exception e) {
      throw new ServletException("Error obteniendo eventos", e);
    }
  }
}
