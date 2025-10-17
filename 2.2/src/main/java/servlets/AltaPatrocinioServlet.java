package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import logica.controladores.IControladorEvento;
import logica.controladores.IControladorRegistro;
import logica.controladores.IControladorUsuario;
import logica.datatypesyenum.DTEdicion;
import logica.datatypesyenum.DTFecha;
import logica.datatypesyenum.DTTipoDeRegistro;
import logica.datatypesyenum.NivelPatrocinio;
import excepciones.PatrocinioDuplicadoException;

@WebServlet("/altaPatrocinio")
public class AltaPatrocinioServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Verificar sesión y rol
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

            // Cargar datos para el formulario
            IControladorRegistro ctrlRegistro = IControladorRegistro.getInstance();
            IControladorUsuario ctrlUsuario = IControladorUsuario.getInstance();
            IControladorEvento ctrlEvento = IControladorEvento.getInstance();
            
            // Verificar que la edición no haya finalizado
            DTEdicion dtEdicion = ctrlEvento.consultarEdicion(edicion);
            if (dtEdicion != null && dtEdicion.getFechaFin() != null) {
                LocalDate hoy = LocalDate.now();
                LocalDate fechaFin = LocalDate.of(
                    dtEdicion.getFechaFin().getAnio(),
                    dtEdicion.getFechaFin().getMes(),
                    dtEdicion.getFechaFin().getDia()
                );
                
                if (fechaFin.isBefore(hoy)) {
                    session.setAttribute("datosMensaje", "❌ No se puede dar de alta patrocinios para una edición que ya finalizó");
                    session.setAttribute("datosMensajeTipo", "error");
                    response.sendRedirect(request.getContextPath() + "/edicionesOrganizadas");
                    return;
                }
            }

            Set<String> nombresTipos = ctrlRegistro.listarTipoRegistro(edicion);
            Set<DTTipoDeRegistro> tiposRegistro = new HashSet<>();
            for (String nombreTipo : nombresTipos) {
                tiposRegistro.add(ctrlRegistro.consultaTipoDeRegistro(edicion, nombreTipo));
            }
            // Obtener categorías para el sidebar (ordenadas alfabéticamente)
            Set<String> categoriasSet = ctrlEvento.listarCategorias();
            List<String> categorias = new ArrayList<>(categoriasSet);
            Collections.sort(categorias);
            System.out.println("DEBUG: Categorias obtenidas: " + (categorias != null ? categorias.size() : "null"));
            
     
            request.setAttribute("categorias", categorias);

            request.setAttribute("tiposRegistro", tiposRegistro);
            request.setAttribute("instituciones", ctrlUsuario.listarInstituciones());
            request.setAttribute("edicionSeleccionada", edicion);
            
            // Pasar datos de sesión al JSP
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

        IControladorEvento ctrlEvento = IControladorEvento.getInstance();
        IControladorRegistro ctrlRegistro = IControladorRegistro.getInstance();
        IControladorUsuario ctrlUsuario = IControladorUsuario.getInstance();

        // Validaciones básicas
        if (edicion == null || tipoRegistro == null || institucion == null || nivelPatrocinioStr == null ||
                aporteStr == null || registrosGratuitosStr == null || codigo == null ||
                edicion.isEmpty() || tipoRegistro.isEmpty() || institucion.isEmpty() ||
                nivelPatrocinioStr.isEmpty() || aporteStr.isEmpty() || registrosGratuitosStr.isEmpty() || codigo.isEmpty()) {

<<<<<<< HEAD
                request.setAttribute("msg", "⚠️ Todos los campos son obligatorios.");
                setValoresPrevios(request, edicion, tipoRegistro, institucion, nivelPatrocinioStr, aporteStr, registrosGratuitosStr, codigo);
                recargarFormulario(request, ctrlRegistro, ctrlUsuario, edicion);
                request.getRequestDispatcher("/WEB-INF/views/altaPatrocinio.jsp").forward(request, response);
                return;
            }

            try {
                double aporte = Double.parseDouble(aporteStr);
                int registrosGratuitos = Integer.parseInt(registrosGratuitosStr);
                NivelPatrocinio nivel = NivelPatrocinio.valueOf(nivelPatrocinioStr);

                LocalDate hoy = LocalDate.now();
                DTFecha fechaAlta = new DTFecha(hoy.getDayOfMonth(), hoy.getMonthValue(), hoy.getYear());

                ctrlEvento.altaPatrocinio(edicion, institucion, nivel, aporte, tipoRegistro, registrosGratuitos, codigo, fechaAlta);

                // Redirigir con mensaje de éxito
                session.setAttribute("datosMensaje", "El patrocinio de '" + institucion + "' para la edición '" + edicion + "' fue creado exitosamente");
                session.setAttribute("datosMensajeTipo", "info");
                response.sendRedirect(request.getContextPath() + "/edicionesOrganizadas");

            } catch (NumberFormatException e) {
                request.setAttribute("msg", "❌ Formato inválido en números.");
                setValoresPrevios(request, edicion, tipoRegistro, institucion, nivelPatrocinioStr, aporteStr, registrosGratuitosStr, codigo);
                recargarFormulario(request, ctrlRegistro, ctrlUsuario, edicion);
                request.getRequestDispatcher("/WEB-INF/views/altaPatrocinio.jsp").forward(request, response);

            } catch (IllegalArgumentException e) {
                request.setAttribute("msg", "❌ Nivel de patrocinio no reconocido.");
                setValoresPrevios(request, edicion, tipoRegistro, institucion, nivelPatrocinioStr, aporteStr, registrosGratuitosStr, codigo);
                recargarFormulario(request, ctrlRegistro, ctrlUsuario, edicion);
                request.getRequestDispatcher("/WEB-INF/views/altaPatrocinio.jsp").forward(request, response);

            } catch (PatrocinioDuplicadoException e) {
                request.setAttribute("msg", "❌ Ya existe un patrocinio de esta institución en esta edición.");
                setValoresPrevios(request, edicion, tipoRegistro, institucion, nivelPatrocinioStr, aporteStr, registrosGratuitosStr, codigo);
                recargarFormulario(request, ctrlRegistro, ctrlUsuario, edicion);
                request.getRequestDispatcher("/WEB-INF/views/altaPatrocinio.jsp").forward(request, response);
            }

        } catch (Exception e) {
            throw new ServletException("❌ Error inesperado en alta patrocinio", e);
=======
            request.setAttribute("msg", "⚠️ Todos los campos son obligatorios.");
            setValoresPrevios(request, edicion, tipoRegistro, institucion, nivelPatrocinioStr, aporteStr, registrosGratuitosStr, codigo);
            // Recargar datos necesarios para la JSP
            recargarFormulario(request, ctrlRegistro, ctrlUsuario, edicion);
            cargarSesionYCategorias(request, session);
            request.getRequestDispatcher("/WEB-INF/views/altaPatrocinio.jsp").forward(request, response);
            return;
>>>>>>> 68af7a028bb5e2cc511dc9d0f1717cff17ffccda
        }

        try {
            double aporte = Double.parseDouble(aporteStr);
            int registrosGratuitos = Integer.parseInt(registrosGratuitosStr);
            NivelPatrocinio nivel = NivelPatrocinio.valueOf(nivelPatrocinioStr);

            LocalDate hoy = LocalDate.now();
            DTFecha fechaAlta = new DTFecha(hoy.getDayOfMonth(), hoy.getMonthValue(), hoy.getYear());

            ctrlEvento.altaPatrocinio(edicion, institucion, nivel, aporte, tipoRegistro, registrosGratuitos, codigo, fechaAlta);

            response.sendRedirect(request.getContextPath() + "/edicionesOrganizadas");

        } catch (NumberFormatException e) {
            request.setAttribute("msg", "⚠️ Formato inválido en números.");
            setValoresPrevios(request, edicion, tipoRegistro, institucion, nivelPatrocinioStr, aporteStr, registrosGratuitosStr, codigo);
            recargarFormulario(request, ctrlRegistro, ctrlUsuario, edicion);
            cargarSesionYCategorias(request, session);
            request.getRequestDispatcher("/WEB-INF/views/altaPatrocinio.jsp").forward(request, response);

        } catch (IllegalArgumentException e) {
            request.setAttribute("msg", "⚠️ Nivel de patrocinio no reconocido.");
            setValoresPrevios(request, edicion, tipoRegistro, institucion, nivelPatrocinioStr, aporteStr, registrosGratuitosStr, codigo);
            recargarFormulario(request, ctrlRegistro, ctrlUsuario, edicion);
            cargarSesionYCategorias(request, session);
            request.getRequestDispatcher("/WEB-INF/views/altaPatrocinio.jsp").forward(request, response);

        } catch (PatrocinioDuplicadoException e) {
            request.setAttribute("msg", "⚠️ Ya existe un patrocinio de esta institución en esta edición.");
            setValoresPrevios(request, edicion, tipoRegistro, institucion, nivelPatrocinioStr, aporteStr, registrosGratuitosStr, codigo);
            recargarFormulario(request, ctrlRegistro, ctrlUsuario, edicion);
            cargarSesionYCategorias(request, session);
            request.getRequestDispatcher("/WEB-INF/views/altaPatrocinio.jsp").forward(request, response);
        }
    }

    // Método para recargar categorías y datos de sesión
    private void cargarSesionYCategorias(HttpServletRequest request, HttpSession session) {
        try {
            IControladorEvento ctrlEvento = IControladorEvento.getInstance();
            request.setAttribute("categorias", ctrlEvento.listarCategorias());
            request.setAttribute("nickname", session.getAttribute("usuario"));
            request.setAttribute("avatar", session.getAttribute("avatar"));
            request.setAttribute("role", session.getAttribute("role"));
            request.setAttribute("nombre", session.getAttribute("nombre"));
        } catch (Exception ignored) {}
    }

    private void recargarFormulario(HttpServletRequest request,
                                    IControladorRegistro ctrlRegistro,
                                    IControladorUsuario ctrlUsuario,
                                    String edicion) {
        try {
            Set<String> nombresTipos = ctrlRegistro.listarTipoRegistro(edicion);
            Set<DTTipoDeRegistro> tiposRegistro = new HashSet<>();
            for (String nombreTipo : nombresTipos) {
                tiposRegistro.add(ctrlRegistro.consultaTipoDeRegistro(edicion, nombreTipo));
            }
            request.setAttribute("tiposRegistro", tiposRegistro);
            request.setAttribute("instituciones", ctrlUsuario.listarInstituciones());
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

