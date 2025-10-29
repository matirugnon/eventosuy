package logica.controladores;

import java.util.Set;

import excepciones.CategoriaNoSeleccionadaException;
import excepciones.EdicionExistenteException;
import excepciones.EdicionNoExisteException;
import excepciones.EdicionSinPatrociniosException;
import excepciones.EventoNoExisteException;
import excepciones.EventoRepetidoException;
import excepciones.FechaInvalidaException;
import excepciones.FechasIncompatiblesException;
import excepciones.PatrocinioDuplicadoException;
import excepciones.PatrocinioNoEncontradoException;
import excepciones.SiglaRepetidaException;

import logica.datatypesyenum.DTEdicion;
import logica.datatypesyenum.DTEvento;
import logica.datatypesyenum.DTFecha;
import logica.datatypesyenum.DTPatrocinio;
import logica.datatypesyenum.DTSeleccionEvento;
import logica.datatypesyenum.EstadoEdicion;
import logica.datatypesyenum.NivelPatrocinio;

public interface IControladorEvento {

    static IControladorEvento getInstance() {
    return ControladorEvento.getInstance();
    }

    boolean existeEvento(String nomEvento);

    boolean darAltaEvento(String nomEvento, String desc, DTFecha fechaAlta, String sigla, Set<String> nomcategorias)
        throws EventoRepetidoException, SiglaRepetidaException, CategoriaNoSeleccionadaException, FechaInvalidaException;

    Set<String> listarEventos();

    Set<String> listarEdiciones();

    Set<String> listarEdicionesActivas(String nomEvento)
        throws EventoNoExisteException;

    DTSeleccionEvento seleccionarEvento(String nomEvento)
        throws EventoNoExisteException;

    DTEdicion consultarEdicion(String nomEdicion);

    boolean existeCategoria(String cat);

    void altaCategoria(String cat);

    boolean existeEdicion(String nomEdicion);

    // sin nada
    boolean altaEdicion(String nomEvento, String nickOrganizador, String nomEdicion, String sigla,
        String ciudad, String pais, DTFecha fechaIni, DTFecha fechaFin, DTFecha fechaAlta)
        throws EdicionExistenteException, SiglaRepetidaException, FechaInvalidaException, FechasIncompatiblesException, EventoNoExisteException;

    // con imagen sin video
    boolean altaEdicion(String nomEvento, String nickOrganizador, String nomEdicion, String sigla,
        String ciudad, String pais, DTFecha fechaIni, DTFecha fechaFin, DTFecha fechaAlta, String img)
        throws EdicionExistenteException, SiglaRepetidaException, FechaInvalidaException, FechasIncompatiblesException, EventoNoExisteException;

    // con imagen y video
    boolean altaEdicion(String nomEvento, String nickOrganizador, String nomEdicion, String sigla, String ciudad,
        String pais, DTFecha fechaIni, DTFecha fechaFin, DTFecha fechaAlta, String imagen, String video)
        throws EdicionExistenteException, SiglaRepetidaException, FechaInvalidaException, FechasIncompatiblesException, EventoNoExisteException;

    Set<String> listarEdiciones(String nomEvento)
        throws EventoNoExisteException;

    DTPatrocinio consultarTipoPatrocinioEdicion(String nomEdicion, String codPatrocinio)
        throws EdicionNoExisteException, EdicionSinPatrociniosException, PatrocinioNoEncontradoException;

    boolean existePatrocinio(String nomEdicion, String nomInstitucion);

    boolean costoSuperaAporte(String nomEdicion, String nomInstitucion, String nomTipoRegistro, double monto, int cantRegGrat);

    void altaPatrocinio(String nomEdicion, String nomInstitucion, NivelPatrocinio nivel, double aporte, String nomTipoRegistro, int cantRegistrosGratuitos, String codigo, DTFecha fechaAlta)
        throws PatrocinioDuplicadoException;

    Set<String> listarPatrocinios(String nomEdicion);

    boolean existeCodigoPatrocinioEnEdicion(String edicion, String codigo);

    boolean esFechaValida(int dia, int mes, int anio);

    Set<String> listarCategorias();

    Set<DTEvento> obtenerDTEventos();

    DTEvento obtenerEventoPorEdicion(String nomEdicion);

    String obtenerEventoDeEdicion(String nombreEdicion) throws EdicionNoExisteException;

    void actualizarEstadoEdicion(String nomEdicion, EstadoEdicion nuevoEstado) throws EdicionNoExisteException;

    Set<String> listarEdicionesPorEstado(EstadoEdicion estado);

    Set<DTEdicion> listarEdicionesOrganizadasPorEstado(String nicknameOrganizador, EstadoEdicion estado);

    boolean darAltaEvento(String nombreEvento, String descripcion, DTFecha fechaAlta, String sigla, Set<String> nomCategorias,
        String rutaImagen) throws EventoRepetidoException, SiglaRepetidaException, CategoriaNoSeleccionadaException, FechaInvalidaException;

    Set<String> listarEdicionesPorEstadoDeEvento(String nomEvento, EstadoEdicion estado);

}