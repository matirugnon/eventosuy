package servlets;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import soap.PublicadorControlador;
import soap.StringArray;
import servlets.dto.EdicionDTO;
import servlets.dto.EventoDetalleDTO;
import utils.SoapClientHelper;

@WebServlet("/consultaEvento")
public class ConsultaEventoServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
        if (session != null) {
            Object msg = session.getAttribute("eventoMensaje");
            if (msg != null) {
                request.setAttribute("eventoMensaje", msg);
                Object tipo = session.getAttribute("eventoMensajeTipo");
                if (tipo != null) {
                    request.setAttribute("eventoMensajeTipo", tipo);
                }
                session.removeAttribute("eventoMensaje");
                session.removeAttribute("eventoMensajeTipo");
            }
        }

        try {
            PublicadorControlador publicador = SoapClientHelper.getPublicadorControlador();

            String nombreEvento = request.getParameter("evento");
            if (nombreEvento == null || nombreEvento.trim().isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/inicio");
                return;
            }

            StringArray detalleEventoSoap = publicador.obtenerDetalleEvento(nombreEvento);
            if (detalleEventoSoap == null || detalleEventoSoap.getItem() == null || detalleEventoSoap.getItem().isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/inicio");
                return;
            }

            EventoDetalleDTO eventoDetalle = new EventoDetalleDTO(detalleEventoSoap);
            boolean eventoFinalizado = eventoDetalle.isFinalizado();

            List<EdicionDTO> edicionesAceptadas = new ArrayList<>();
            if (!eventoFinalizado) {
                StringArray nombresEdicionesArray = publicador.listarEdicionesAceptadasDeEvento(nombreEvento);
                if (nombresEdicionesArray != null && nombresEdicionesArray.getItem() != null) {
                    for (String nombreEdicion : nombresEdicionesArray.getItem()) {
                        try {
                            StringArray detalleEdicionArray = publicador.obtenerDetalleEdicion(nombreEdicion);
                            if (detalleEdicionArray != null && detalleEdicionArray.getItem() != null
                                    && detalleEdicionArray.getItem().size() >= 2) {
                                edicionesAceptadas.add(new EdicionDTO(detalleEdicionArray));
                            }
                        } catch (Exception e) {
                            System.err.println("Error obteniendo detalle de edici\u00f3n " + nombreEdicion + ": " + e.getMessage());
                        }
                    }
                }
            }

            List<String> categoriasList = new ArrayList<>();
            StringArray categoriasArray = publicador.listarCategorias();
            if (categoriasArray != null && categoriasArray.getItem() != null) {
                categoriasList.addAll(categoriasArray.getItem());
                Collections.sort(categoriasList);
            }

            request.setAttribute("eventoDetalle", eventoDetalle);
            request.setAttribute("eventoSeleccionado", nombreEvento);
            request.setAttribute("categorias", categoriasList);
            request.setAttribute("edicionesAceptadas", edicionesAceptadas);
            request.setAttribute("eventoFinalizado", eventoFinalizado);
            request.setAttribute("estadoEvento", eventoDetalle.getEstado());

            HttpSession currentSession = session != null ? session : request.getSession(false);
            String role = currentSession != null ? (String) currentSession.getAttribute("role") : null;
            boolean puedeFinalizar = "organizador".equals(role) && !eventoFinalizado;

            request.setAttribute("role", role);
            request.setAttribute("puedeFinalizar", puedeFinalizar);
            if (currentSession != null) {
                request.setAttribute("nickname", currentSession.getAttribute("usuario"));
                request.setAttribute("avatar", currentSession.getAttribute("avatar"));
                request.setAttribute("imagen", currentSession.getAttribute("imagen"));
            }

            request.getRequestDispatcher("/WEB-INF/views/consultaEvento.jsp").forward(request, response);
        } catch (Exception e) {
            throw new ServletException("Error obteniendo informaci\u00f3n del evento", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession(false);

        if (session == null || !"organizador".equals(session.getAttribute("role"))) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Solo organizadores pueden finalizar eventos");
            return;
        }

        String nombreEvento = request.getParameter("evento");
        if (nombreEvento == null || nombreEvento.trim().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Evento no especificado");
            return;
        }

        try {
            PublicadorControlador publicador = SoapClientHelper.getPublicadorControlador();
            String resultado = publicador.finalizarEvento(nombreEvento);

            if ("OK".equalsIgnoreCase(resultado)) {
                session.setAttribute("eventoMensaje", "Evento finalizado correctamente.");
                session.setAttribute("eventoMensajeTipo", "success");
            } else {
                session.setAttribute("eventoMensaje",
                        resultado != null && !resultado.isBlank() ? resultado : "No se pudo finalizar el evento.");
                session.setAttribute("eventoMensajeTipo", "error");
            }
        } catch (Exception e) {
            session.setAttribute("eventoMensaje", "No se pudo finalizar el evento: " + e.getMessage());
            session.setAttribute("eventoMensajeTipo", "error");
        }

        String redirect = request.getContextPath() + "/consultaEvento?evento="
                + URLEncoder.encode(nombreEvento, StandardCharsets.UTF_8);
        response.sendRedirect(redirect);
    }
}
