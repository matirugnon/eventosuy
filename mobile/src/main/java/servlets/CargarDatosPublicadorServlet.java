package servlets;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.SoapClientHelper;
import soap.PublicadorControlador;

@WebServlet("/cargarDatosPublicador")
public class CargarDatosPublicadorServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");

        try {
            PublicadorControlador publicador = SoapClientHelper.getPublicadorControlador();
            String resultado = publicador.cargarDatosPrueba();

            if ("OK".equalsIgnoreCase(resultado)) {
                req.getSession().setAttribute("datosMensaje", "Datos de prueba cargados en el Servidor Central.");
                req.getSession().setAttribute("datosMensajeTipo", "success");
            } else {
                req.getSession().setAttribute("datosMensaje", "Error cargando datos en Servidor Central: " + resultado);
                req.getSession().setAttribute("datosMensajeTipo", "danger");
            }
        } catch (Exception e) {
            req.getSession().setAttribute("datosMensaje", "Error conectando con Servidor Central: " + e.getMessage());
            req.getSession().setAttribute("datosMensajeTipo", "danger");
        }

        // Redirigir de vuelta al login para mostrar el mensaje
        resp.sendRedirect(req.getContextPath() + "/login");
    }
}
