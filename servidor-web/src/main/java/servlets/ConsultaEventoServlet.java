package servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import soap.PublicadorControlador;
import soap.StringArray;
import utils.SoapClientHelper;
import servlets.dto.EventoDetalleDTO;
import servlets.dto.EdicionDTO;

@WebServlet("/consultaEvento")
public class ConsultaEventoServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	
    	request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
    	
        try {
            PublicadorControlador publicador = SoapClientHelper.getPublicadorControlador();

            // Obtener el nombre del evento desde el parámetro
            String nombreEvento = request.getParameter("evento");

            if (nombreEvento == null || nombreEvento.trim().isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/inicio");
                return;
            }

            // Obtener datos del evento vía SOAP
            StringArray detalleEventoSoap = publicador.obtenerDetalleEvento(nombreEvento);
            
            // Verificar que el evento existe
            if (detalleEventoSoap == null || detalleEventoSoap.getItem() == null || detalleEventoSoap.getItem().isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/inicio");
                return;
            }
            
            EventoDetalleDTO eventoDetalle = new EventoDetalleDTO(detalleEventoSoap);

            // Obtener las ediciones aceptadas vía SOAP
            StringArray nombresEdicionesArray = publicador.listarEdicionesDeEvento(nombreEvento);
            List<EdicionDTO> edicionesAceptadas = new ArrayList<>();
            
            if (nombresEdicionesArray != null && nombresEdicionesArray.getItem() != null) {
                for (String nombreEdicion : nombresEdicionesArray.getItem()) {
                    try {
                        StringArray detalleEdicionArray = publicador.obtenerDetalleEdicion(nombreEdicion);
                        if (detalleEdicionArray != null && detalleEdicionArray.getItem() != null 
                            && detalleEdicionArray.getItem().size() >= 2) {
                            EdicionDTO edicionDTO = new EdicionDTO(detalleEdicionArray);
                            edicionesAceptadas.add(edicionDTO);
                        }
                    } catch (Exception e) {
                        // Si hay error con una edición, continuar con las demás
                        System.err.println("Error obteniendo detalle de edición " + nombreEdicion + ": " + e.getMessage());
                    }
                }
            }
            
            request.setAttribute("edicionesAceptadas", edicionesAceptadas);

            // Obtener todas las categorías vía SOAP
            soap.StringArray categoriasArray = publicador.listarCategorias();
            List<String> categoriasList = new ArrayList<>();
            if (categoriasArray != null && categoriasArray.getItem() != null) {
                categoriasList = new ArrayList<>(categoriasArray.getItem());
                Collections.sort(categoriasList);
            }

            // Pasar los datos a la JSP
            request.setAttribute("eventoDetalle", eventoDetalle); // DTO desde SOAP
            request.setAttribute("eventoSeleccionado", nombreEvento); // TODO: migrar a SOAP
            request.setAttribute("categorias", categoriasList);

            // Obtener el rol desde la sesión y pasarlo a la JSP
            String role = (String) request.getSession().getAttribute("role");
            request.setAttribute("role", role);
            request.setAttribute("nickname", request.getSession().getAttribute("usuario"));
            request.setAttribute("avatar", request.getSession().getAttribute("avatar"));
            request.setAttribute("imagen", request.getSession().getAttribute("imagen"));

            request.getRequestDispatcher("/WEB-INF/views/consultaEvento.jsp").forward(request, response);

        } catch (Exception e) {
            throw new ServletException("Error obteniendo información del evento", e);
        }
    }
}