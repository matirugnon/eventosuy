package servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import logica.controladores.IControladorEvento;
import logica.controladores.IControladorUsuario;
import logica.controladores.IControladorRegistro;
import logica.datatypesyenum.DTEvento;
import utils.Utils;

@WebServlet("/inicio")
public class inicioServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    	request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        try {
            // Obtener los controladores
            IControladorEvento ctrlEvento = IControladorEvento.getInstance();
            IControladorUsuario ctrlUsuario = IControladorUsuario.getInstance();
            IControladorRegistro ctrlRegistro = IControladorRegistro.getInstance();

            // Verificar si los datos ya fueron precargados
            boolean datosCargados = Utils.datosPrecargados(getServletContext());
            boolean hayDatosBasicos = Utils.hayDatosBasicos(ctrlUsuario, ctrlEvento, ctrlRegistro);

            // Solo cargar datos si no están cargados y no hay datos básicos
            if (!datosCargados && !hayDatosBasicos) {
                Utils.cargarDatos(ctrlUsuario, ctrlEvento, ctrlRegistro);
                Utils.marcarDatosCargados(getServletContext());

                // Mostrar mensaje de confirmación
                request.setAttribute("datosMensaje", "Datos de ejemplo cargados automáticamente.");
                request.setAttribute("datosMensajeTipo", "success");
            }

            HttpSession session = request.getSession();
            Object mensaje = session.getAttribute("datosMensaje");
            if (mensaje != null) {
                request.setAttribute("datosMensaje", mensaje);
                Object tipo = session.getAttribute("datosMensajeTipo");
                if (tipo != null) {
                    request.setAttribute("datosMensajeTipo", tipo);
                }
                session.removeAttribute("datosMensaje");
                session.removeAttribute("datosMensajeTipo");
            }

            // Obtener eventos
            Set<DTEvento> eventos = ctrlEvento.obtenerDTEventos();

            // Categorías para el sidebar (ordenadas alfabéticamente)
            Set<String> categoriasSet = ctrlEvento.listarCategorias();
            List<String> categorias = new ArrayList<>(categoriasSet);
            Collections.sort(categorias);

            // Parámetros del filtro
            String categoriaSeleccionada = request.getParameter("categoria");
            String busqueda = request.getParameter("busqueda");

            // FILTRO POR CATEGORÍA
            if (categoriaSeleccionada != null && !categoriaSeleccionada.isBlank()
                    && !"todas".equalsIgnoreCase(categoriaSeleccionada)) {

                final String cat = categoriaSeleccionada;
                eventos = eventos.stream()
                        .filter(e -> e.getCategorias() != null &&
                                     e.getCategorias().stream()
                                       .anyMatch(c -> c.equalsIgnoreCase(cat)))
                        .collect(Collectors.toCollection(LinkedHashSet::new));
            }

            // FILTRO POR BÚSQUEDA
            if (busqueda != null && !busqueda.isBlank()) {
                final String texto = normalizar(busqueda);
                eventos = eventos.stream()
                        .filter(e -> normalizar(e.getNombre()).contains(texto) ||
                                     normalizar(e.getDescripcion()).contains(texto))
                        .collect(Collectors.toCollection(LinkedHashSet::new));
            }

            // Ordenar por nombre
            List<DTEvento> eventosOrdenados = new ArrayList<>(eventos);
            eventosOrdenados.sort(Comparator.comparing(DTEvento::getNombre));

            // PAGINACIÓN
            int pageSize = 3;
            int page = 1;
            String pageParam = request.getParameter("page");
            if (pageParam != null) {
                try {
                    page = Integer.parseInt(pageParam);
                    if (page < 1) page = 1;
                } catch (NumberFormatException e) {
                    page = 1;
                }
            }

            int totalEventos = eventos.size();
            int totalPages = (int) Math.ceil((double) totalEventos / pageSize);

            // Si no hay eventos, establecer página 1 y páginas totales en 1
            if (totalEventos == 0) {
                totalPages = 1;
                page = 1;
            } else if (page > totalPages) {
                page = totalPages;
            }

            int fromIndex = (page - 1) * pageSize;
            int toIndex = Math.min(fromIndex + pageSize, totalEventos);

            // Solo crear sublista si hay elementos
            List<DTEvento> eventosPagina;
            if (totalEventos == 0) {
                eventosPagina = new ArrayList<>();
            } else {
                eventosPagina = eventosOrdenados.subList(fromIndex, toIndex);
            }

            // Atributos JSP
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("eventosOrdenados", eventosPagina);
            request.setAttribute("eventos", eventos);
            request.setAttribute("categorias", categorias);
            request.setAttribute("categoriaSeleccionada",
                    (categoriaSeleccionada == null || categoriaSeleccionada.isBlank()) ? "todas" : categoriaSeleccionada);
            request.setAttribute("busqueda", busqueda != null ? busqueda : "");

            // Usuario
            String role = (String) request.getSession().getAttribute("role");
            request.setAttribute("role", role);
            request.setAttribute("nickname", request.getSession().getAttribute("usuario"));
            request.setAttribute("avatar", request.getSession().getAttribute("avatar"));

            request.getRequestDispatcher("/WEB-INF/views/inicio.jsp").forward(request, response);

        } catch (Exception e) {
            throw new ServletException("Error obteniendo eventos", e);
        }
    }

    // Función para normalizar texto (quitar tildes y convertir a minúsculas)
    private String normalizar(String input) {
        if (input == null) return "";
        String espaniolizado = java.text.Normalizer.normalize(input, java.text.Normalizer.Form.NFD);
        espaniolizado = espaniolizado.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        return espaniolizado.toLowerCase();
    }
}