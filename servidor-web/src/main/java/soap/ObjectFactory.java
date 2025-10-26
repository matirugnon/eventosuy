
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

    private static final QName _EdicionNoExisteFault_QNAME = new QName("http://publicadores/", "EdicionNoExisteFault");
    private static final QName _NombreTipoRegistroDuplicadoFault_QNAME = new QName("http://publicadores/", "NombreTipoRegistroDuplicadoFault");
    private static final QName _UsuarioNoExisteFault_QNAME = new QName("http://publicadores/", "UsuarioNoExisteFault");
    private static final QName _UsuarioYaRegistradoEnEdicionFault_QNAME = new QName("http://publicadores/", "UsuarioYaRegistradoEnEdicionFault");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: soap
     * 
     */
    public ObjectFactory() {
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
     * Create an instance of {@link NombreTipoRegistroDuplicadoException }
     * 
     * @return
     *     the new instance of {@link NombreTipoRegistroDuplicadoException }
     */
    public NombreTipoRegistroDuplicadoException createNombreTipoRegistroDuplicadoException() {
        return new NombreTipoRegistroDuplicadoException();
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
     * Create an instance of {@link UsuarioYaRegistradoEnEdicionException }
     * 
     * @return
     *     the new instance of {@link UsuarioYaRegistradoEnEdicionException }
     */
    public UsuarioYaRegistradoEnEdicionException createUsuarioYaRegistradoEnEdicionException() {
        return new UsuarioYaRegistradoEnEdicionException();
    }

    /**
     * Create an instance of {@link DtTipoDeRegistro }
     * 
     * @return
     *     the new instance of {@link DtTipoDeRegistro }
     */
    public DtTipoDeRegistro createDtTipoDeRegistro() {
        return new DtTipoDeRegistro();
    }

    /**
     * Create an instance of {@link DtRegistro }
     * 
     * @return
     *     the new instance of {@link DtRegistro }
     */
    public DtRegistro createDtRegistro() {
        return new DtRegistro();
    }

    /**
     * Create an instance of {@link DTFecha }
     * 
     * @return
     *     the new instance of {@link DTFecha }
     */
    public DTFecha createDTFecha() {
        return new DTFecha();
    }

    /**
     * Create an instance of {@link DtRegistroArray }
     * 
     * @return
     *     the new instance of {@link DtRegistroArray }
     */
    public DtRegistroArray createDtRegistroArray() {
        return new DtRegistroArray();
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
