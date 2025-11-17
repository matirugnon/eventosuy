package servlets;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import soap.DtEdicion;
import soap.PublicadorControlador;
import soap.PublicadorRegistro;
import utils.SoapClientHelper;
import java.time.LocalDate;

@WebServlet("/registrarAsistencia")
public class RegistrarAsistenciaServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        
        try {
            // Obtener parámetros
            String asistente = request.getParameter("asistente");
            String edicion = request.getParameter("edicion");
            String tipoRegistro = request.getParameter("tipoRegistro");
            String from = request.getParameter("from");
            
            // Validar parámetros
            if (asistente == null || edicion == null || tipoRegistro == null ||
                asistente.trim().isEmpty() || edicion.trim().isEmpty() || tipoRegistro.trim().isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/inicio");
                return;
            }
            
            // Verificar que el usuario logueado es el asistente
            String usuarioSesion = (String) request.getSession().getAttribute("usuario");
            String rolSesion = (String) request.getSession().getAttribute("role");
            
            if (!"asistente".equals(rolSesion) || !asistente.equals(usuarioSesion)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, 
                    "No tienes permisos para registrar esta asistencia");
                return;
            }
            
            // Verificar que el evento ya haya comenzado
            PublicadorControlador publicadorEv = SoapClientHelper.getPublicadorControlador();
            DtEdicion edicionInfo = publicadorEv.consultarEdicion(edicion);
            
            if (edicionInfo == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Edición no encontrada");
                return;
            }
            
            // Obtener fecha de inicio del evento
            int diaInicio = edicionInfo.getFechaInicio().getDia();
            int mesInicio = edicionInfo.getFechaInicio().getMes();
            int anioInicio = edicionInfo.getFechaInicio().getAnio();
            LocalDate fechaInicioEvento = LocalDate.of(anioInicio, mesInicio, diaInicio);
            LocalDate fechaActual = LocalDate.now();
            
            // Validar que el evento ya haya comenzado
            if (fechaActual.isBefore(fechaInicioEvento)) {
                // Redireccionar con mensaje de error
                String encodedAsistente = URLEncoder.encode(asistente, StandardCharsets.UTF_8);
                String encodedEdicion = URLEncoder.encode(edicion, StandardCharsets.UTF_8);
                String encodedTipoRegistro = URLEncoder.encode(tipoRegistro, StandardCharsets.UTF_8);
                
                String redirectUrl = String.format("%s/consultaRegistro?asistente=%s&edicion=%s&tipoRegistro=%s&error=eventoNoIniciado",
                    request.getContextPath(),
                    encodedAsistente,
                    encodedEdicion,
                    encodedTipoRegistro
                );
                
                if (from != null && !from.trim().isEmpty()) {
                    redirectUrl += "&from=" + URLEncoder.encode(from, StandardCharsets.UTF_8);
                }
                
                response.sendRedirect(redirectUrl);
                return;
            }
            
            // Llamar al servicio SOAP para registrar la asistencia
            PublicadorRegistro publicadorReg = SoapClientHelper.getPublicadorRegistro();
            publicadorReg.registrarAsistencia(asistente, edicion, tipoRegistro);
            
            // Redirigir de vuelta a la consulta del registro con encoding correcto
            String encodedAsistente = URLEncoder.encode(asistente, StandardCharsets.UTF_8);
            String encodedEdicion = URLEncoder.encode(edicion, StandardCharsets.UTF_8);
            String encodedTipoRegistro = URLEncoder.encode(tipoRegistro, StandardCharsets.UTF_8);
            
            String redirectUrl = String.format("%s/consultaRegistro?asistente=%s&edicion=%s&tipoRegistro=%s",
                request.getContextPath(),
                encodedAsistente,
                encodedEdicion,
                encodedTipoRegistro
            );
            
            // Preservar el parámetro 'from' si existe
            if (from != null && !from.trim().isEmpty()) {
                redirectUrl += "&from=" + URLEncoder.encode(from, StandardCharsets.UTF_8);
            }
            
            System.out.println("DEBUG RegistrarAsistenciaServlet - Redirigiendo a: " + redirectUrl);
            response.sendRedirect(redirectUrl);
            
        } catch (Exception e) {
            throw new ServletException("Error registrando asistencia", e);
        }
    }
}
