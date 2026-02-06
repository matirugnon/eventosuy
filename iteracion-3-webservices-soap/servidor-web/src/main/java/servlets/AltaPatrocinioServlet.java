package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import soap.DtEdicion;
import soap.PublicadorControlador;
import soap.PublicadorRegistro;
import soap.PublicadorUsuario;
import soap.StringArray;
import utils.SoapClientHelper;
import soap.DtTipoDeRegistro;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.HashSet;
import java.util.Set;



// (Patrocinio exceptions are handled by the SOAP publicador; catch blocks removed)

@WebServlet("/altaPatrocinio")
public class AltaPatrocinioServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	
    	request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
    	
        try {
            // Verificar sesiÃ³n y rol
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("usuario") == null) {
                response.sendRedirect(request.getContextPath() + "/login");
                return;
            }
            String role = (String) session.getAttribute("role");
            if (!"organizador".equals(role)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN,
                        "Solo los organizadores pueden dar de alta patrocinios");
                return;
            }

            String edicion = request.getParameter("edicion");
            if (edicion == null || edicion.trim().isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/edicionesOrganizadas");
                return;
            }

            // Cargar datos para el formulario - PUBLICADORES
            PublicadorControlador publicadorCtrl = SoapClientHelper.getPublicadorControlador();
            PublicadorRegistro publicadorReg = SoapClientHelper.getPublicadorRegistro();
            PublicadorUsuario publicadorUsr = SoapClientHelper.getPublicadorUsuario();
            
            // Verificar que la ediciÃ³n no haya finalizado
            DtEdicion dtEdicion = publicadorCtrl.consultarEdicion(edicion);
            if (dtEdicion != null && dtEdicion.getFechaFin() != null) {
                LocalDate hoy = LocalDate.now();
                LocalDate fechaFin = LocalDate.of(
                    dtEdicion.getFechaFin().getAnio(),
                    dtEdicion.getFechaFin().getMes(),
                    dtEdicion.getFechaFin().getDia()
                );
                
                if (fechaFin.isBefore(hoy)) {
                    session.setAttribute("datosMensaje", "âŒ No se puede dar de alta patrocinios para una ediciÃ³n que ya finalizÃ³");
                    session.setAttribute("datosMensajeTipo", "error");
                    response.sendRedirect(request.getContextPath() + "/edicionesOrganizadas");
                    return;
                }
            }

            StringArray nombresTipos = publicadorReg.listarTipoRegistro(edicion);
            Set<String> nombresTiposSet = new HashSet<>();
            if (nombresTipos != null && nombresTipos.getItem() != null) {
                nombresTiposSet.addAll(nombresTipos.getItem());
            }

            Set<DtTipoDeRegistro> tiposRegistro = new HashSet<>();
            for (String nombreTipo : nombresTiposSet) {
                tiposRegistro.add(publicadorReg.consultaTipoDeRegistro(edicion, nombreTipo));
            }
            // Obtener categorías para el sidebar (ordenadas alfabéticamente)
            StringArray categoriasSet = publicadorCtrl.listarCategorias();
            List<String> categorias = new ArrayList<>();
            if (categoriasSet != null && categoriasSet.getItem() != null) {
                categorias.addAll(categoriasSet.getItem());
            }
            Collections.sort(categorias);
            request.setAttribute("categorias", categorias);

            //listar instituciones
            StringArray institucionesArray = publicadorUsr.listarInstituciones();
            List<String> instituciones = new ArrayList<>();
            if (institucionesArray != null && institucionesArray.getItem() != null) {
                instituciones.addAll(institucionesArray.getItem());
            }

            request.setAttribute("tiposRegistro", tiposRegistro);
            request.setAttribute("instituciones", instituciones);
            request.setAttribute("edicionSeleccionada", edicion);
            
            // Pasar datos de sesiÃ³n al JSP
            request.setAttribute("nickname", session.getAttribute("usuario"));
            request.setAttribute("avatar", session.getAttribute("avatar"));
            request.setAttribute("role", session.getAttribute("role"));
            request.setAttribute("nombre", session.getAttribute("nombre"));

            request.getRequestDispatcher("/WEB-INF/views/altaPatrocinio.jsp").forward(request, response);

        } catch (Exception e) {
            throw new ServletException("Error al cargar el formulario de Alta Patrocinio", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	
    	request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
    	
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        String role = (String) session.getAttribute("role");
        if (!"organizador".equals(role)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN,
                    "Solo los organizadores pueden dar de alta patrocinios");
            return;
        }

        String edicion = request.getParameter("edicion");
        String tipoRegistro = request.getParameter("tipoRegistro");
        String institucion = request.getParameter("institucion");
        String nivelPatrocinioStr = request.getParameter("nivelPatrocinio");
        String aporteStr = request.getParameter("aporteEconomico");
        String registrosGratuitosStr = request.getParameter("registrosGratuitos");
        String codigo = request.getParameter("codigoPatrocinio");

        //obtener Publicadores


        PublicadorControlador publicadorCtrl = SoapClientHelper.getPublicadorControlador();
        PublicadorRegistro publicadorReg = SoapClientHelper.getPublicadorRegistro();
        PublicadorUsuario publicadorUsr = SoapClientHelper.getPublicadorUsuario();

        // Validaciones bÃ¡sicas
        if (edicion == null || tipoRegistro == null || institucion == null || nivelPatrocinioStr == null ||
                aporteStr == null || registrosGratuitosStr == null || codigo == null ||
                edicion.isEmpty() || tipoRegistro.isEmpty() || institucion.isEmpty() ||
                nivelPatrocinioStr.isEmpty() || aporteStr.isEmpty() || registrosGratuitosStr.isEmpty() || codigo.isEmpty()) {


            request.setAttribute("msg", "Todos los campos son obligatorios.");
            setValoresPrevios(request, edicion, tipoRegistro, institucion, nivelPatrocinioStr, aporteStr, registrosGratuitosStr, codigo);
            // Recargar datos necesarios para la JSP (usar publicadores)
            recargarFormulario(request, publicadorReg, publicadorUsr, edicion);
            cargarSesionYCategorias(request, session);
            request.getRequestDispatcher("/WEB-INF/views/altaPatrocinio.jsp").forward(request, response);
            return;
        }

        try {
            double aporte = Double.parseDouble(aporteStr);
            int registrosGratuitos = Integer.parseInt(registrosGratuitosStr);
            // Convertir a tipos SOAP
            soap.NivelPatrocinio nivel = soap.NivelPatrocinio.fromValue(nivelPatrocinioStr);

            LocalDate hoy = LocalDate.now();
            soap.DTFecha fechaAlta = new soap.DTFecha();
            fechaAlta.setDia(hoy.getDayOfMonth());
            fechaAlta.setMes(hoy.getMonthValue());
            fechaAlta.setAnio(hoy.getYear());

            String resultado = publicadorCtrl.altaPatrocinio(edicion, institucion, nivel, aporte, tipoRegistro, registrosGratuitos, codigo, fechaAlta);
            if (resultado == null || !"OK".equalsIgnoreCase(resultado)) {
                // Mostrar mensaje de error devuelto por el publicador
                request.setAttribute("msg", (resultado == null ? "Error al dar de alta el patrocinio" : resultado));
                setValoresPrevios(request, edicion, tipoRegistro, institucion, nivelPatrocinioStr, aporteStr, registrosGratuitosStr, codigo);
                recargarFormulario(request, publicadorReg, publicadorUsr, edicion);
                cargarSesionYCategorias(request, session);
                request.getRequestDispatcher("/WEB-INF/views/altaPatrocinio.jsp").forward(request, response);
                return;
            }

            // Mostrar mensaje de éxito, limpiar valores del formulario y quedarse en la misma página
            request.setAttribute("successMsg", "Patrocinio creado correctamente.");

            // Mantener la edición seleccionada pero limpiar los demás campos
            setValoresPrevios(request, edicion, "", "", "", "", "", "");
            recargarFormulario(request, publicadorReg, publicadorUsr, edicion);
            cargarSesionYCategorias(request, session);
            request.getRequestDispatcher("/WEB-INF/views/altaPatrocinio.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            request.setAttribute("msg", "Formato inválido en números.");
            setValoresPrevios(request, edicion, tipoRegistro, institucion, nivelPatrocinioStr, aporteStr, registrosGratuitosStr, codigo);
            // recargar con publicadores SOAP
            recargarFormulario(request, publicadorReg, publicadorUsr, edicion);
            cargarSesionYCategorias(request, session);
            request.getRequestDispatcher("/WEB-INF/views/altaPatrocinio.jsp").forward(request, response);

        } catch (IllegalArgumentException e) {
            // Puede ocurrir por nivel no reconocido o por validaciones del servidor (p.ej. costo supera aporte)
            request.setAttribute("msg", e.getMessage() == null ? "Nivel de patrocinio no reconocido." : e.getMessage());
            setValoresPrevios(request, edicion, tipoRegistro, institucion, nivelPatrocinioStr, aporteStr, registrosGratuitosStr, codigo);
            recargarFormulario(request, publicadorReg, publicadorUsr, edicion);
            cargarSesionYCategorias(request, session);
            request.getRequestDispatcher("/WEB-INF/views/altaPatrocinio.jsp").forward(request, response);

        }
    }

    // MÃ©todo para recargar categorÃ­as y datos de sesiÃ³n
    private void cargarSesionYCategorias(HttpServletRequest request, HttpSession session) {
        try {

            PublicadorControlador publicadorCtrl = SoapClientHelper.getPublicadorControlador();

            StringArray categoriasSet = publicadorCtrl.listarCategorias();
            List<String> categorias = new ArrayList<>();
            if (categoriasSet != null && categoriasSet.getItem() != null) {
                categorias.addAll(categoriasSet.getItem());
            }
            Collections.sort(categorias);

            request.setAttribute("categorias", categorias);
            request.setAttribute("nickname", session.getAttribute("usuario"));
            request.setAttribute("avatar", session.getAttribute("avatar"));
            request.setAttribute("role", session.getAttribute("role"));
            request.setAttribute("nombre", session.getAttribute("nombre"));
        } catch (Exception ignored) {}
    }

    private void recargarFormulario(HttpServletRequest request,
                                    PublicadorRegistro publicadorReg,
                                    PublicadorUsuario publicadorUsr,
                                    String edicion) {
        try {
            // obtener nombres de tipos via SOAP
            StringArray nombresTiposArr = publicadorReg.listarTipoRegistro(edicion);
            Set<DtTipoDeRegistro> tiposRegistro = new HashSet<>();
            if (nombresTiposArr != null && nombresTiposArr.getItem() != null) {
                for (String nombreTipo : nombresTiposArr.getItem()) {
                    try {
                        DtTipoDeRegistro dt = publicadorReg.consultaTipoDeRegistro(edicion, nombreTipo);
                        if (dt != null) tiposRegistro.add(dt);
                    } catch (Exception ex) {
                        // ignorar tipo que no se pueda obtener
                    }
                }
            }
            request.setAttribute("tiposRegistro", tiposRegistro);

            //categorias para el sidebar
            PublicadorControlador publicadorCtrl = SoapClientHelper.getPublicadorControlador();
            StringArray categoriasArr = publicadorCtrl.listarCategorias();
            List<String> categorias = new ArrayList<>();
            if (categoriasArr != null && categoriasArr.getItem() != null) {
                categorias.addAll(categoriasArr.getItem());
            }
            request.setAttribute("categorias", categorias);

            // instituciones
            StringArray institucionesArr = publicadorUsr.listarInstituciones();
            List<String> instituciones = new ArrayList<>();
            if (institucionesArr != null && institucionesArr.getItem() != null) {
                instituciones.addAll(institucionesArr.getItem());
            }
            request.setAttribute("instituciones", instituciones);
            request.setAttribute("edicionSeleccionada", edicion);
        } catch (Exception ignored) {}
    }

    private void setValoresPrevios(HttpServletRequest request,
                                   String edicion,
                                   String tipoRegistro,
                                   String institucion,
                                   String nivel,
                                   String aporte,
                                   String registrosGratuitos,
                                   String codigo) {
        request.setAttribute("tipoRegistroSeleccionado", tipoRegistro);
        request.setAttribute("institucionSeleccionada", institucion);
        request.setAttribute("nivelSeleccionado", nivel);
        request.setAttribute("aporteIngresado", aporte);
        request.setAttribute("registrosIngresados", registrosGratuitos);
        request.setAttribute("codigoIngresado", codigo);
        request.setAttribute("edicionSeleccionada", edicion);
    }
}


