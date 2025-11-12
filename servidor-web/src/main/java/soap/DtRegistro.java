
package soap;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para dtRegistro complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>{@code
 * <complexType name="dtRegistro">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="asistente" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         <element name="asistio" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         <element name="costo" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
 *         <element name="fechaRegistro" type="{http://publicadores/}DTFecha" minOccurs="0"/>
 *         <element name="nomEdicion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         <element name="patrocinado" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         <element name="tipoDeRegistro" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       </sequence>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * }</pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "dtRegistro", propOrder = {
    "asistente",
    "asistio",
    "costo",
    "fechaRegistro",
    "nomEdicion",
    "patrocinado",
    "tipoDeRegistro"
})
public class DtRegistro {

    protected String asistente;
    protected boolean asistio;
    protected Double costo;
    protected DTFecha fechaRegistro;
    protected String nomEdicion;
    protected boolean patrocinado;
    protected String tipoDeRegistro;

    /**
     * Obtiene el valor de la propiedad asistente.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAsistente() {
        return asistente;
    }

    /**
     * Define el valor de la propiedad asistente.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAsistente(String value) {
        this.asistente = value;
    }

    /**
     * Obtiene el valor de la propiedad asistio.
     * 
     */
    public boolean isAsistio() {
        return asistio;
    }

    /**
     * Define el valor de la propiedad asistio.
     * 
     */
    public void setAsistio(boolean value) {
        this.asistio = value;
    }

    /**
     * Obtiene el valor de la propiedad costo.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getCosto() {
        return costo;
    }

    /**
     * Define el valor de la propiedad costo.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setCosto(Double value) {
        this.costo = value;
    }

    /**
     * Obtiene el valor de la propiedad fechaRegistro.
     * 
     * @return
     *     possible object is
     *     {@link DTFecha }
     *     
     */
    public DTFecha getFechaRegistro() {
        return fechaRegistro;
    }

    /**
     * Define el valor de la propiedad fechaRegistro.
     * 
     * @param value
     *     allowed object is
     *     {@link DTFecha }
     *     
     */
    public void setFechaRegistro(DTFecha value) {
        this.fechaRegistro = value;
    }

    /**
     * Obtiene el valor de la propiedad nomEdicion.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNomEdicion() {
        return nomEdicion;
    }

    /**
     * Define el valor de la propiedad nomEdicion.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNomEdicion(String value) {
        this.nomEdicion = value;
    }

    /**
     * Obtiene el valor de la propiedad patrocinado.
     * 
     */
    public boolean isPatrocinado() {
        return patrocinado;
    }

    /**
     * Define el valor de la propiedad patrocinado.
     * 
     */
    public void setPatrocinado(boolean value) {
        this.patrocinado = value;
    }

    /**
     * Obtiene el valor de la propiedad tipoDeRegistro.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoDeRegistro() {
        return tipoDeRegistro;
    }

    /**
     * Define el valor de la propiedad tipoDeRegistro.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoDeRegistro(String value) {
        this.tipoDeRegistro = value;
    }

}
