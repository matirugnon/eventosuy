package servlets;

import java.io.IOException;
import java.util.Set;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import logica.Controladores.IControladorEvento;
import logica.Controladores.IControladorRegistro;
import logica.Controladores.IControladorUsuario;
import logica.DatatypesYEnum.DTRegistro;
import logica.DatatypesYEnum.DTEdicion;
import excepciones.UsuarioNoExisteException;
import utils.Utils;

@WebServlet("/consultaRegistro")
public class ConsultaRegistroServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Obtener parámetros
            String asistente = request.getParameter("asistente");
            String edicion = request.getParameter("edicion");
            String tipoRegistro = request.getParameter("tipoRegistro");
            
            if (asistente == null || edicion == null || tipoRegistro == null ||
                asistente.trim().isEmpty() || edicion.trim().isEmpty() || tipoRegistro.trim().isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/inicio");
                return;
            }

            // Obtener controladores
            IControladorRegistro ctrlRegistro = IControladorRegistro.getInstance();
            IControladorEvento ctrlEvento = IControladorEvento.getInstance();
            IControladorUsuario ctrlUsuario = IControladorUsuario.getInstance();

            // Carga inicial de datos si hace falta
            Set<String> usuariosExistentes = ctrlUsuario.listarUsuarios();
            if (usuariosExistentes == null || usuariosExistentes.isEmpty()) {
                Utils.cargarDatos(
                    ctrlUsuario,
                    ctrlEvento,
                    ctrlRegistro
                );
            }

            // Obtener el registro específico
            // Primero obtenemos todos los registros del asistente
            Set<DTRegistro> registros = ctrlRegistro.listarRegistrosPorAsistente(asistente);
            DTRegistro registro = null;
            
            // Buscar el registro específico por edición y tipo de registro
            for (DTRegistro reg : registros) {
                if (reg.getnomEdicion().equals(edicion) && reg.getTipoDeRegistro().equals(tipoRegistro)) {
                    registro = reg;
                    break;
                }
            }
            
            if (registro == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Registro no encontrado");
                return;
            }

            // Obtener información de la edición
            DTEdicion edicionInfo = ctrlEvento.consultarEdicion(edicion);
            
            // Obtener todas las categorías para el sidebar
            Set<String> categorias = ctrlEvento.listarCategorias();

            // Pasar los datos a la JSP
            request.setAttribute("registro", registro);
            request.setAttribute("edicionInfo", edicionInfo);
            request.setAttribute("categorias", categorias);

            // Obtener el rol desde la sesión y pasarlo a la JSP
            String role = (String) request.getSession().getAttribute("role");
            request.setAttribute("role", role);
            request.setAttribute("nickname", request.getSession().getAttribute("usuario"));
            request.setAttribute("avatar", request.getSession().getAttribute("avatar"));

            request.getRequestDispatcher("/WEB-INF/views/consultaRegistro.jsp").forward(request, response);

        } catch (Exception e) {
            throw new ServletException("Error obteniendo información del registro", e);
        }
    }
}