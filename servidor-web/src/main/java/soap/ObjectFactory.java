
package soap;

import javax.xml.namespace.QName;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.annotation.XmlElementDecl;
import jakarta.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the soap package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private static final QName _CategoriaNoSeleccionadaFault_QNAME = new QName("http://publicadores/", "CategoriaNoSeleccionadaFault");
    private static final QName _CorreoInvalidoFault_QNAME = new QName("http://publicadores/", "CorreoInvalidoFault");
    private static final QName _EdicionExistenteFault_QNAME = new QName("http://publicadores/", "EdicionExistenteFault");
    private static final QName _EdicionNoExisteFault_QNAME = new QName("http://publicadores/", "EdicionNoExisteFault");
    private static final QName _EventoRepetidoFault_QNAME = new QName("http://publicadores/", "EventoRepetidoFault");
    private static final QName _ExisteInstitucionFault_QNAME = new QName("http://publicadores/", "ExisteInstitucionFault");
    private static final QName _FechaInvalidaFault_QNAME = new QName("http://publicadores/", "FechaInvalidaFault");
    private static final QName _FechasIncompatiblesFault_QNAME = new QName("http://publicadores/", "FechasIncompatiblesFault");
    private static final QName _NombreTipoRegistroDuplicadoFault_QNAME = new QName("http://publicadores/", "NombreTipoRegistroDuplicadoFault");
    private static final QName _PatrocinioDuplicadoFault_QNAME = new QName("http://publicadores/", "PatrocinioDuplicadoFault");
    private static final QName _SiglaRepetidaFault_QNAME = new QName("http://publicadores/", "SiglaRepetidaFault");
    private static final QName _UsuarioNoExisteFault_QNAME = new QName("http://publicadores/", "UsuarioNoExisteFault");
    private static final QName _UsuarioRepetidoFault_QNAME = new QName("http://publicadores/", "UsuarioRepetidoFault");
    private static final QName _UsuarioYaRegistradoEnEdicionFault_QNAME = new QName("http://publicadores/", "UsuarioYaRegistradoEnEdicionFault");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: soap
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link CategoriaNoSeleccionadaException }
     * 
     * @return
     *     the new instance of {@link CategoriaNoSeleccionadaException }
     */
    public CategoriaNoSeleccionadaException createCategoriaNoSeleccionadaException() {
        return new CategoriaNoSeleccionadaException();
    }

    /**
     * Create an instance of {@link CorreoInvalidoException }
     * 
     * @return
     *     the new instance of {@link CorreoInvalidoException }
     */
    public CorreoInvalidoException createCorreoInvalidoException() {
        return new CorreoInvalidoException();
    }

    /**
     * Create an instance of {@link EdicionExistenteException }
     * 
     * @return
     *     the new instance of {@link EdicionExistenteException }
     */
    public EdicionExistenteException createEdicionExistenteException() {
        return new EdicionExistenteException();
    }

    /**
     * Create an instance of {@link EdicionNoExisteException }
     * 
     * @return
     *     the new instance of {@link EdicionNoExisteException }
     */
    public EdicionNoExisteException createEdicionNoExisteException() {
        return new EdicionNoExisteException();
    }

    /**
     * Create an instance of {@link EventoRepetidoException }
     * 
     * @return
     *     the new instance of {@link EventoRepetidoException }
     */
    public EventoRepetidoException createEventoRepetidoException() {
        return new EventoRepetidoException();
    }

    /**
     * Create an instance of {@link ExisteInstitucionException }
     * 
     * @return
     *     the new instance of {@link ExisteInstitucionException }
     */
    public ExisteInstitucionException createExisteInstitucionException() {
        return new ExisteInstitucionException();
    }

    /**
     * Create an instance of {@link FechaInvalidaException }
     * 
     * @return
     *     the new instance of {@link FechaInvalidaException }
     */
    public FechaInvalidaException createFechaInvalidaException() {
        return new FechaInvalidaException();
    }

    /**
     * Create an instance of {@link FechasIncompatiblesException }
     * 
     * @return
     *     the new instance of {@link FechasIncompatiblesException }
     */
    public FechasIncompatiblesException createFechasIncompatiblesException() {
        return new FechasIncompatiblesException();
    }

    /**
     * Create an instance of {@link NombreTipoRegistroDuplicadoException }
     * 
     * @return
     *     the new instance of {@link NombreTipoRegistroDuplicadoException }
     */
    public NombreTipoRegistroDuplicadoException createNombreTipoRegistroDuplicadoException() {
        return new NombreTipoRegistroDuplicadoException();
    }

    /**
     * Create an instance of {@link PatrocinioDuplicadoException }
     * 
     * @return
     *     the new instance of {@link PatrocinioDuplicadoException }
     */
    public PatrocinioDuplicadoException createPatrocinioDuplicadoException() {
        return new PatrocinioDuplicadoException();
    }

    /**
     * Create an instance of {@link SiglaRepetidaException }
     * 
     * @return
     *     the new instance of {@link SiglaRepetidaException }
     */
    public SiglaRepetidaException createSiglaRepetidaException() {
        return new SiglaRepetidaException();
    }

    /**
     * Create an instance of {@link UsuarioNoExisteException }
     * 
     * @return
     *     the new instance of {@link UsuarioNoExisteException }
     */
    public UsuarioNoExisteException createUsuarioNoExisteException() {
        return new UsuarioNoExisteException();
    }

    /**
     * Create an instance of {@link UsuarioRepetidoException }
     * 
     * @return
     *     the new instance of {@link UsuarioRepetidoException }
     */
    public UsuarioRepetidoException createUsuarioRepetidoException() {
        return new UsuarioRepetidoException();
    }

    /**
     * Create an instance of {@link UsuarioYaRegistradoEnEdicionException }
     * 
     * @return
     *     the new instance of {@link UsuarioYaRegistradoEnEdicionException }
     */
    public UsuarioYaRegistradoEnEdicionException createUsuarioYaRegistradoEnEdicionException() {
        return new UsuarioYaRegistradoEnEdicionException();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CategoriaNoSeleccionadaException }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link CategoriaNoSeleccionadaException }{@code >}
     */
    @XmlElementDecl(namespace = "http://publicadores/", name = "CategoriaNoSeleccionadaFault")
    public JAXBElement<CategoriaNoSeleccionadaException> createCategoriaNoSeleccionadaFault(CategoriaNoSeleccionadaException value) {
        return new JAXBElement<>(_CategoriaNoSeleccionadaFault_QNAME, CategoriaNoSeleccionadaException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CorreoInvalidoException }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link CorreoInvalidoException }{@code >}
     */
    @XmlElementDecl(namespace = "http://publicadores/", name = "CorreoInvalidoFault")
    public JAXBElement<CorreoInvalidoException> createCorreoInvalidoFault(CorreoInvalidoException value) {
        return new JAXBElement<>(_CorreoInvalidoFault_QNAME, CorreoInvalidoException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EdicionExistenteException }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link EdicionExistenteException }{@code >}
     */
    @XmlElementDecl(namespace = "http://publicadores/", name = "EdicionExistenteFault")
    public JAXBElement<EdicionExistenteException> createEdicionExistenteFault(EdicionExistenteException value) {
        return new JAXBElement<>(_EdicionExistenteFault_QNAME, EdicionExistenteException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EdicionNoExisteException }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link EdicionNoExisteException }{@code >}
     */
    @XmlElementDecl(namespace = "http://publicadores/", name = "EdicionNoExisteFault")
    public JAXBElement<EdicionNoExisteException> createEdicionNoExisteFault(EdicionNoExisteException value) {
        return new JAXBElement<>(_EdicionNoExisteFault_QNAME, EdicionNoExisteException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EventoRepetidoException }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link EventoRepetidoException }{@code >}
     */
    @XmlElementDecl(namespace = "http://publicadores/", name = "EventoRepetidoFault")
    public JAXBElement<EventoRepetidoException> createEventoRepetidoFault(EventoRepetidoException value) {
        return new JAXBElement<>(_EventoRepetidoFault_QNAME, EventoRepetidoException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ExisteInstitucionException }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ExisteInstitucionException }{@code >}
     */
    @XmlElementDecl(namespace = "http://publicadores/", name = "ExisteInstitucionFault")
    public JAXBElement<ExisteInstitucionException> createExisteInstitucionFault(ExisteInstitucionException value) {
        return new JAXBElement<>(_ExisteInstitucionFault_QNAME, ExisteInstitucionException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FechaInvalidaException }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link FechaInvalidaException }{@code >}
     */
    @XmlElementDecl(namespace = "http://publicadores/", name = "FechaInvalidaFault")
    public JAXBElement<FechaInvalidaException> createFechaInvalidaFault(FechaInvalidaException value) {
        return new JAXBElement<>(_FechaInvalidaFault_QNAME, FechaInvalidaException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FechasIncompatiblesException }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link FechasIncompatiblesException }{@code >}
     */
    @XmlElementDecl(namespace = "http://publicadores/", name = "FechasIncompatiblesFault")
    public JAXBElement<FechasIncompatiblesException> createFechasIncompatiblesFault(FechasIncompatiblesException value) {
        return new JAXBElement<>(_FechasIncompatiblesFault_QNAME, FechasIncompatiblesException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link NombreTipoRegistroDuplicadoException }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link NombreTipoRegistroDuplicadoException }{@code >}
     */
    @XmlElementDecl(namespace = "http://publicadores/", name = "NombreTipoRegistroDuplicadoFault")
    public JAXBElement<NombreTipoRegistroDuplicadoException> createNombreTipoRegistroDuplicadoFault(NombreTipoRegistroDuplicadoException value) {
        return new JAXBElement<>(_NombreTipoRegistroDuplicadoFault_QNAME, NombreTipoRegistroDuplicadoException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PatrocinioDuplicadoException }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link PatrocinioDuplicadoException }{@code >}
     */
    @XmlElementDecl(namespace = "http://publicadores/", name = "PatrocinioDuplicadoFault")
    public JAXBElement<PatrocinioDuplicadoException> createPatrocinioDuplicadoFault(PatrocinioDuplicadoException value) {
        return new JAXBElement<>(_PatrocinioDuplicadoFault_QNAME, PatrocinioDuplicadoException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SiglaRepetidaException }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link SiglaRepetidaException }{@code >}
     */
    @XmlElementDecl(namespace = "http://publicadores/", name = "SiglaRepetidaFault")
    public JAXBElement<SiglaRepetidaException> createSiglaRepetidaFault(SiglaRepetidaException value) {
        return new JAXBElement<>(_SiglaRepetidaFault_QNAME, SiglaRepetidaException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UsuarioNoExisteException }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link UsuarioNoExisteException }{@code >}
     */
    @XmlElementDecl(namespace = "http://publicadores/", name = "UsuarioNoExisteFault")
    public JAXBElement<UsuarioNoExisteException> createUsuarioNoExisteFault(UsuarioNoExisteException value) {
        return new JAXBElement<>(_UsuarioNoExisteFault_QNAME, UsuarioNoExisteException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UsuarioRepetidoException }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link UsuarioRepetidoException }{@code >}
     */
    @XmlElementDecl(namespace = "http://publicadores/", name = "UsuarioRepetidoFault")
    public JAXBElement<UsuarioRepetidoException> createUsuarioRepetidoFault(UsuarioRepetidoException value) {
        return new JAXBElement<>(_UsuarioRepetidoFault_QNAME, UsuarioRepetidoException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UsuarioYaRegistradoEnEdicionException }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link UsuarioYaRegistradoEnEdicionException }{@code >}
     */
    @XmlElementDecl(namespace = "http://publicadores/", name = "UsuarioYaRegistradoEnEdicionFault")
    public JAXBElement<UsuarioYaRegistradoEnEdicionException> createUsuarioYaRegistradoEnEdicionFault(UsuarioYaRegistradoEnEdicionException value) {
        return new JAXBElement<>(_UsuarioYaRegistradoEnEdicionFault_QNAME, UsuarioYaRegistradoEnEdicionException.class, null, value);
    }

}
