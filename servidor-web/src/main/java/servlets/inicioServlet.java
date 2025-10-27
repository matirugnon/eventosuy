package servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import soap.PublicadorCargaDatos;
import soap.PublicadorControlador;
import soap.StringArray;
import utils.SoapClientHelper;
import servlets.dto.EventoDTO;

@WebServlet("/inicio")
public class inicioServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    	request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        try {
            // Obtener el publicador SOAP
            PublicadorControlador publicador = SoapClientHelper.getPublicadorControlador();
            
            // Verificar si los datos ya fueron precargados (solo para la primera vez)
            
            
            PublicadorCargaDatos publicadorDatos = SoapClientHelper.getPublicadorCargaDatos();
            
            boolean datosCargados = publicadorDatos.datosPrecargados();
            boolean hayDatosBasicos = publicadorDatos.hayDatosBasicos();

            if (!datosCargados && !hayDatosBasicos) {
            	publicadorDatos.cargarDatos();
            	publicadorDatos.marcarDatosCargados();
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

            // Obtener eventos vía SOAP
            StringArray nombresEventosWs = publicador.listarEventos();
            List<EventoDTO> eventos = new ArrayList<>();
            
            if (nombresEventosWs != null && nombresEventosWs.getItem() != null) {
                for (String nombreEvento : nombresEventosWs.getItem()) {
                    StringArray detallesWs = publicador.obtenerDetalleEvento(nombreEvento);
                    if (detallesWs != null && detallesWs.getItem() != null && detallesWs.getItem().size() > 0) {
                        // Convertir List<String> a String[]
                        String[] detalles = detallesWs.getItem().toArray(new String[0]);
                        eventos.add(new EventoDTO(detalles));
                    }
                }
            }

            // Categorías via SOAP
            StringArray categoriasWs = publicador.listarCategorias();
            List<String> categorias = new ArrayList<>();
            if (categoriasWs != null && categoriasWs.getItem() != null) {
                categorias.addAll(categoriasWs.getItem());
                Collections.sort(categorias);
            }

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
                        .collect(Collectors.toList());
            }

            // FILTRO POR BÚSQUEDA
            if (busqueda != null && !busqueda.isBlank()) {
                final String texto = normalizar(busqueda);
                eventos = eventos.stream()
                        .filter(e -> normalizar(e.getNombre()).contains(texto) ||
                                     normalizar(e.getDescripcion()).contains(texto))
                        .collect(Collectors.toList());
            }

            // Ordenar por nombre
            List<EventoDTO> eventosOrdenados = new ArrayList<>(eventos);
            eventosOrdenados.sort(Comparator.comparing(EventoDTO::getNombre));

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
            List<EventoDTO> eventosPagina;
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