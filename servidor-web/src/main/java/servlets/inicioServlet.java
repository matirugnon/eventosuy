package servlets;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import soap.*;
import utils.SoapClientHelper;

@WebServlet("/inicio")
public class inicioServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        try {
            PublicadorControlador publicador = SoapClientHelper.getPublicadorControlador();

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

            // 🔹 Obtener EVENTOS ACTIVOS (no finalizados)
            List<DtEvento> eventos = new ArrayList<>();
            StringArray nombresEventosWs = publicador.listarEventos();
            if (nombresEventosWs != null && nombresEventosWs.getItem() != null) {
                for (String nombreEvento : nombresEventosWs.getItem()) {
                    DtEvento e = publicador.obtenerDTEvento(nombreEvento);
                    if (e != null) {
                        eventos.add(e);
                    }
                }
            }

            // 🔹 Obtener EDICIONES ACTIVAS Y ACEPTADAS (no archivadas, no finalizadas)
            List<DtEdicion> ediciones = new ArrayList<>();
            for (DtEvento evento : eventos) {
                StringArray nombresEdicionesWs = publicador.listarEdicionesActivasAceptadas(evento.getNombre());
                if (nombresEdicionesWs != null && nombresEdicionesWs.getItem() != null) {
                    for (String nombreEdicion : nombresEdicionesWs.getItem()) {
                        try {
                            DtEdicion ed = publicador.consultarEdicion(nombreEdicion);
                            if (ed != null) {
                                ediciones.add(ed);
                            }
                        } catch (Exception e) {
                            System.err.println("Error obteniendo edición " + nombreEdicion + ": " + e.getMessage());
                        }
                    }
                }
            }

            // 🔹 Obtener CATEGORÍAS para el filtro
            StringArray categoriasWs = publicador.listarCategorias();
            List<String> categorias = new ArrayList<>();
            if (categoriasWs != null && categoriasWs.getItem() != null) {
                categorias.addAll(categoriasWs.getItem());
                Collections.sort(categorias);
            }

            // 🔹 Crear lista unificada como mapas para facilitar la vista JSP
            List<Map<String, Object>> resultados = new ArrayList<>();

            for (DtEvento e : eventos) {
                Map<String, Object> m = new HashMap<>();
                m.put("tipo", "evento");
                m.put("nombre", e.getNombre());
                m.put("descripcion", e.getDescripcion());
                m.put("imagen", e.getImagen());
                
                // Categorías como string separado por comas
                List<String> listaCategorias = new ArrayList<>();
                if (e.getCategorias() != null && e.getCategorias().getCategoria() != null) {
                    listaCategorias.addAll(e.getCategorias().getCategoria());
                    m.put("categorias", String.join(", ", e.getCategorias().getCategoria()));
                } else {
                    m.put("categorias", "");
                }
                m.put("categoriasList", listaCategorias); // Lista para filtrar
                
                // Fecha del evento
                if (e.getFechaEvento() != null) {
                    DTFecha f = e.getFechaEvento();
                    try {
                        m.put("fecha", LocalDate.of(f.getAnio(), f.getMes(), f.getDia()));
                    } catch (Exception ex) {
                        m.put("fecha", null);
                    }
                } else {
                    m.put("fecha", null);
                }
                resultados.add(m);
            }

            for (DtEdicion ed : ediciones) {
                Map<String, Object> m = new HashMap<>();
                m.put("tipo", "edicion");
                m.put("nombre", ed.getNombre());
                m.put("evento", ed.getEvento());
                m.put("ciudad", ed.getCiudad());
                m.put("pais", ed.getPais());
                m.put("imagen", ed.getImagen());
                
                // Obtener categorías del evento padre para filtrar las ediciones
                String nombreEvento = ed.getEvento();
                List<String> listaCategorias = new ArrayList<>();
                if (nombreEvento != null) {
                    // Buscar el evento en la lista de eventos ya obtenidos
                    DtEvento eventoPadre = eventos.stream()
                        .filter(e -> e.getNombre().equals(nombreEvento))
                        .findFirst()
                        .orElse(null);
                    
                    if (eventoPadre != null && eventoPadre.getCategorias() != null && eventoPadre.getCategorias().getCategoria() != null) {
                        listaCategorias.addAll(eventoPadre.getCategorias().getCategoria());
                    }
                }
                m.put("categoriasList", listaCategorias);
                
                // Fecha de alta de la edición
                if (ed.getAltaEdicion() != null) {
                    DTFecha f = ed.getAltaEdicion();
                    try {
                        m.put("fecha", LocalDate.of(f.getAnio(), f.getMes(), f.getDia()));
                    } catch (Exception ex) {
                        m.put("fecha", null);
                    }
                } else {
                    m.put("fecha", null);
                }
                resultados.add(m);
            }

            // 🔹 Parámetros de filtrado y ordenamiento
            String busqueda = request.getParameter("busqueda");
            String tipo = request.getParameter("tipo"); // eventos | ediciones | todos
            String orden = request.getParameter("orden"); // nombreAsc | nombreDesc | fechaAsc | fechaDesc
            String categoriaSeleccionada = request.getParameter("categoria");

            // 🔹 FILTRO por tipo
            if (tipo != null && !tipo.isBlank() && !"todos".equalsIgnoreCase(tipo)) {
                if (tipo.equalsIgnoreCase("eventos")) {
                    resultados = resultados.stream()
                            .filter(o -> "evento".equals(o.get("tipo")))
                            .collect(Collectors.toList());
                } else if (tipo.equalsIgnoreCase("ediciones")) {
                    resultados = resultados.stream()
                            .filter(o -> "edicion".equals(o.get("tipo")))
                            .collect(Collectors.toList());
                }
            }

            // 🔹 FILTRO por CATEGORÍA (aplica a eventos y ediciones según categoría del evento padre)
            if (categoriaSeleccionada != null && !categoriaSeleccionada.isBlank()
                    && !"todas".equalsIgnoreCase(categoriaSeleccionada)) {
                final String cat = categoriaSeleccionada;
                resultados = resultados.stream()
                        .filter(o -> {
                            // Filtrar tanto eventos como ediciones por categoría
                            @SuppressWarnings("unchecked")
                            List<String> cats = (List<String>) o.get("categoriasList");
                            return cats != null && cats.stream()
                                    .anyMatch(c -> c.equalsIgnoreCase(cat));
                        })
                        .collect(Collectors.toList());
            }

            // 🔹 FILTRO por búsqueda
            if (busqueda != null && !busqueda.isBlank()) {
                final String texto = normalizar(busqueda);
                resultados = resultados.stream()
                        .filter(o -> {
                            Object nombreObj = o.get("nombre");
                            String nombre = nombreObj != null ? nombreObj.toString() : "";
                            return normalizar(nombre).contains(texto);
                        })
                        .collect(Collectors.toList());
            }

            // 🔹 ORDENAMIENTO (por defecto: fecha descendente)
            if (orden == null || orden.isBlank()) {
                orden = "fechaDesc";
            }
            
            switch (orden) {
                case "nombreAsc":
                    resultados.sort(Comparator.comparing(o -> {
                        Object nombreObj = o.get("nombre");
                        return nombreObj != null ? nombreObj.toString() : "";
                    }));
                    break;
                case "nombreDesc":
                    resultados.sort(Comparator.comparing(o -> {
                        Object nombreObj = o.get("nombre");
                        return nombreObj != null ? nombreObj.toString() : "";
                    }, Comparator.reverseOrder()));
                    break;
                case "fechaAsc":
                    resultados.sort(Comparator.comparing(o -> (LocalDate) o.get("fecha"), 
                            Comparator.nullsLast(Comparator.naturalOrder())));
                    break;
                case "fechaDesc":
                default:
                    resultados.sort(Comparator.comparing(o -> (LocalDate) o.get("fecha"), 
                            Comparator.nullsLast(Comparator.reverseOrder())));
                    break;
            }

            // 🔹 Paginación simple
            int pageSize = 5;
            int page = 1;
            try {
                page = Integer.parseInt(request.getParameter("page"));
            } catch (Exception ignored) {}
            
            int total = resultados.size();
            int totalPages = total == 0 ? 1 : (int) Math.ceil((double) total / pageSize);
            if (page < 1) page = 1;
            if (page > totalPages) page = totalPages;
            
            int from = (page - 1) * pageSize;
            int to = Math.min(from + pageSize, total);
            List<Map<String, Object>> pagina = total == 0 ? new ArrayList<>() : resultados.subList(from, to);

            // 🔹 Enviar al JSP
            request.setAttribute("resultados", pagina);
            request.setAttribute("tipo", tipo != null ? tipo : "todos");
            request.setAttribute("busqueda", busqueda != null ? busqueda : "");
            request.setAttribute("orden", orden);
            request.setAttribute("categorias", categorias);
            request.setAttribute("categoriaSeleccionada",
                    (categoriaSeleccionada == null || categoriaSeleccionada.isBlank()) ? "todas" : categoriaSeleccionada);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("currentPage", page);

            // Usuario
            request.setAttribute("role", request.getSession().getAttribute("role"));
            request.setAttribute("nickname", request.getSession().getAttribute("usuario"));
            request.setAttribute("avatar", request.getSession().getAttribute("avatar"));

            request.getRequestDispatcher("/WEB-INF/views/inicio.jsp").forward(request, response);

        } catch (Exception e) {
            throw new ServletException("Error obteniendo eventos y ediciones activas", e);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String accion = request.getParameter("accion");

        if ("cargarDatos".equals(accion)) {
            try {
                PublicadorCargaDatos publicadorDatos = SoapClientHelper.getPublicadorCargaDatos();
                publicadorDatos.cargarDatos();

                // Guardar mensaje en sesión para mostrar después del redirect
                request.getSession().setAttribute("datosMensaje", "Datos cargados correctamente.");
                request.getSession().setAttribute("datosMensajeTipo", "success");
            } catch (Exception e) {
                e.printStackTrace();
                request.getSession().setAttribute("datosMensaje", "Error al cargar los datos: " + e.getMessage());
                request.getSession().setAttribute("datosMensajeTipo", "error");
            }
        }

        // Redirigir para que se vea el resultado actualizado
        response.sendRedirect(request.getContextPath() + "/inicio");
    }
    
    private String normalizar(String input) {
        if (input == null) return "";
        String espaniolizado = java.text.Normalizer.normalize(input, java.text.Normalizer.Form.NFD);
        espaniolizado = espaniolizado.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        return espaniolizado.toLowerCase();
    }

}
