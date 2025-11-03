
package soap;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for dtRegistro complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
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
    "costo",
    "fechaRegistro",
    "nomEdicion",
    "tipoDeRegistro",
    "asistio"
})
public class DtRegistro {

    protected String asistente;
    protected Double costo;
    protected DTFecha fechaRegistro;
    protected String nomEdicion;
    protected String tipoDeRegistro;
    protected boolean asistio;

    /**
     * Gets the value of the asistente property.
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
     * Sets the value of the asistente property.
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
     * Gets the value of the asistio property.
     * 
     */
    /**
     * Gets the value of the costo property.
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
     * Sets the value of the costo property.
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
     * Gets the value of the fechaRegistro property.
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
     * Sets the value of the fechaRegistro property.
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
     * Gets the value of the nomEdicion property.
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
     * Sets the value of the nomEdicion property.
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
     * Gets the value of the tipoDeRegistro property.
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
     * Sets the value of the tipoDeRegistro property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoDeRegistro(String value) {
        this.tipoDeRegistro = value;
    }
    
    public boolean isAsistio() {
        return asistio;
    }
    
    public void setAsistio(boolean asistio) {
        this.asistio = asistio;
    }

}
