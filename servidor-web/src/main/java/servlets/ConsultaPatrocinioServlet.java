package servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import logica.controladores.IControladorEvento;
import logica.controladores.IControladorUsuario;
import logica.datatypesyenum.DTPatrocinio;
import logica.datatypesyenum.DTEdicion;
import logica.datatypesyenum.DTInstitucion;
import excepciones.EdicionNoExisteException;
import excepciones.EdicionSinPatrociniosException;
import excepciones.PatrocinioNoEncontradoException;
import utils.Utils;

import soap.DtEdicion;
import soap.PublicadorControlador;
import soap.PublicadorRegistro;
import soap.PublicadorUsuario;
import soap.StringArray;
import utils.SoapClientHelper;
import soap.DtTipoDeRegistro;
import soap.DtPatrocinio;
import soap.DtInstitucion;

@WebServlet("/consultaPatrocinio")
public class ConsultaPatrocinioServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	
    	request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
    	
        try {
            // Obtener parÃ¡metros
            String codigo = request.getParameter("codigo");
            String edicion = request.getParameter("edicion");

            if (codigo == null || edicion == null || codigo.trim().isEmpty() || edicion.trim().isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/inicio");
                return;
            }

            // Limpiar espacios en blanco
            codigo = codigo.trim();
            edicion = edicion.trim();

            // Obtener publicadores
            PublicadorControlador publicadorCtrl = SoapClientHelper.getPublicadorControlador();
            PublicadorUsuario publicadorUsr = SoapClientHelper.getPublicadorUsuario();

            // Obtener informaciÃ³n del patrocinio
            DtPatrocinio patrocinio = publicadorCtrl.consultarTipoPatrocinioEdicion(edicion, codigo);

            if (patrocinio == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Patrocinio no encontrado");
                return;
            }

            // Obtener informaciÃ³n de la instituciÃ³n (incluyendo logo)
            DtInstitucion institucion = publicadorUsr.getInstitucion(patrocinio.getInstitucion());

            // Obtener informaciÃ³n de la ediciÃ³n
            DtEdicion edicionInfo = publicadorCtrl.consultarEdicion(edicion);

            // Obtener todas las categorÃ­as para el sidebar (ordenadas alfabÃ©ticamente)
            StringArray categoriasSet = publicadorCtrl.listarCategorias();

            List<String> categorias = new ArrayList<>();
            if (categoriasSet != null && categoriasSet.getItem() != null) {
                categorias.addAll(categoriasSet.getItem());
            }
            Collections.sort(categorias);

            // Pasar los datos a la JSP
            request.setAttribute("patrocinio", patrocinio);
            request.setAttribute("institucion", institucion);
            request.setAttribute("edicionInfo", edicionInfo);
            request.setAttribute("categorias", categorias);

            // Obtener el rol desde la sesiÃ³n y pasarlo a la JSP
            String role = (String) request.getSession().getAttribute("role");
            request.setAttribute("role", role);
            request.setAttribute("nickname", request.getSession().getAttribute("usuario"));
            request.setAttribute("avatar", request.getSession().getAttribute("avatar"));

            request.getRequestDispatcher("/WEB-INF/views/consultaPatrocinio.jsp").forward(request, response);

        } catch (Exception e) {
            throw new ServletException("Error obteniendo información del patrocinio", e);
        }
    }
}

