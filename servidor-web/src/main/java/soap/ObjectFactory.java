
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

    private static final QName _CorreoInvalidoFault_QNAME = new QName("http://publicadores/", "CorreoInvalidoFault");
    private static final QName _ExisteInstitucionFault_QNAME = new QName("http://publicadores/", "ExisteInstitucionFault");
    private static final QName _FechaInvalidaFault_QNAME = new QName("http://publicadores/", "FechaInvalidaFault");
    private static final QName _UsuarioNoExisteFault_QNAME = new QName("http://publicadores/", "UsuarioNoExisteFault");
    private static final QName _UsuarioRepetidoFault_QNAME = new QName("http://publicadores/", "UsuarioRepetidoFault");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: soap
     * 
     */
    public ObjectFactory() {
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
     * Create an instance of {@link DtUsuario }
     * 
     * @return
     *     the new instance of {@link DtUsuario }
     */
    public DtUsuario createDtUsuario() {
        return new DtUsuario();
    }

    /**
     * Create an instance of {@link DtInstitucion }
     * 
     * @return
     *     the new instance of {@link DtInstitucion }
     */
    public DtInstitucion createDtInstitucion() {
        return new DtInstitucion();
    }

    /**
     * Create an instance of {@link DtUsuarioArray }
     * 
     * @return
     *     the new instance of {@link DtUsuarioArray }
     */
    public DtUsuarioArray createDtUsuarioArray() {
        return new DtUsuarioArray();
    }

    /**
     * Create an instance of {@link StringArray }
     * 
     * @return
     *     the new instance of {@link StringArray }
     */
    public StringArray createStringArray() {
        return new StringArray();
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

}
