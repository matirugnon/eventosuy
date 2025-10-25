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
import logica.datatypesyenum.DTEdicion;

import soap.PublicadorControlador;
import soap.StringArray;
import utils.SoapClientHelper;
import servlets.dto.EventoDetalleDTO;

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

            // TODO: Obtener ediciones y otros datos vía SOAP cuando estén disponibles
            // Por ahora, crear conjuntos vacíos para evitar errores en el JSP
            java.util.Set<DTEdicion> edicionesAceptadas = new java.util.HashSet<>();
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
            request.setAttribute("eventoSeleccionado", null); // TODO: migrar a SOAP
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

