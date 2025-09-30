package servlets;

import java.io.IOException;
import java.util.Set;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import logica.Controladores.IControladorEvento;
import logica.Controladores.IControladorUsuario;
import logica.Controladores.IControladorRegistro;
import utils.Utils;

@WebServlet("/inicio")
public class inicioServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            IControladorEvento ctrlEvento = IControladorEvento.getInstance();

            // Si no hay datos cargados, ejecutar Utils.cargarDatos()
            if (ctrlEvento.listarEventos() == null || ctrlEvento.listarEventos().isEmpty()) {
                Utils.cargarDatos(
                    logica.Controladores.IControladorUsuario.getInstance(),
                    ctrlEvento,
                    logica.Controladores.IControladorRegistro.getInstance()
                );
            }

            // Obtener categorías y eventos después de cargar
            Set<String> categorias = ctrlEvento.listarCategorias(); 
            Set<String> eventos = ctrlEvento.listarEventos(); 

            request.setAttribute("categorias", categorias);
            request.setAttribute("eventos", eventos);

        } catch (Exception e) {
            throw new ServletException("Error inicializando datos", e);
        }

        request.getRequestDispatcher("/WEB-INF/views/inicio.jsp")
               .forward(request, response);
    }
}
