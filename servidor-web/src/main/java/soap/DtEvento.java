<<<<<<< HEAD

package soap;

import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para dtEvento complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>{@code
 * <complexType name="dtEvento">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="categorias" minOccurs="0">
 *           <complexType>
 *             <complexContent>
 *               <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 <sequence>
 *                   <element name="categoria" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *                 </sequence>
 *               </restriction>
 *             </complexContent>
 *           </complexType>
 *         </element>
 *         <element name="descripcion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         <element name="estado" type="{http://publicadores/}estadoEvento" minOccurs="0"/>
 *         <element name="fechaEvento" type="{http://publicadores/}DTFecha" minOccurs="0"/>
 *         <element name="imagen" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         <element name="nombre" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         <element name="sigla" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         <element name="visitas" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       </sequence>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * }</pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "dtEvento", propOrder = {
    "categorias",
    "descripcion",
    "estado",
    "fechaEvento",
    "imagen",
    "nombre",
    "sigla",
    "visitas"
})
public class DtEvento {

    protected DtEvento.Categorias categorias;
    protected String descripcion;
    @XmlSchemaType(name = "string")
    protected EstadoEvento estado;
    protected DTFecha fechaEvento;
    protected String imagen;
    protected String nombre;
    protected String sigla;
    protected int visitas;

    /**
     * Obtiene el valor de la propiedad categorias.
     * 
     * @return
     *     possible object is
     *     {@link DtEvento.Categorias }
     *     
     */
    public DtEvento.Categorias getCategorias() {
        return categorias;
    }

    /**
     * Define el valor de la propiedad categorias.
     * 
     * @param value
     *     allowed object is
     *     {@link DtEvento.Categorias }
     *     
     */
    public void setCategorias(DtEvento.Categorias value) {
        this.categorias = value;
    }

    /**
     * Obtiene el valor de la propiedad descripcion.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Define el valor de la propiedad descripcion.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescripcion(String value) {
        this.descripcion = value;
    }

    /**
     * Obtiene el valor de la propiedad estado.
     * 
     * @return
     *     possible object is
     *     {@link EstadoEvento }
     *     
     */
    public EstadoEvento getEstado() {
        return estado;
    }

    /**
     * Define el valor de la propiedad estado.
     * 
     * @param value
     *     allowed object is
     *     {@link EstadoEvento }
     *     
     */
    public void setEstado(EstadoEvento value) {
        this.estado = value;
    }

    /**
     * Obtiene el valor de la propiedad fechaEvento.
     * 
     * @return
     *     possible object is
     *     {@link DTFecha }
     *     
     */
    public DTFecha getFechaEvento() {
        return fechaEvento;
    }

    /**
     * Define el valor de la propiedad fechaEvento.
     * 
     * @param value
     *     allowed object is
     *     {@link DTFecha }
     *     
     */
    public void setFechaEvento(DTFecha value) {
        this.fechaEvento = value;
    }

    /**
     * Obtiene el valor de la propiedad imagen.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getImagen() {
        return imagen;
    }

    /**
     * Define el valor de la propiedad imagen.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setImagen(String value) {
        this.imagen = value;
    }

    /**
     * Obtiene el valor de la propiedad nombre.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Define el valor de la propiedad nombre.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNombre(String value) {
        this.nombre = value;
    }

    /**
     * Obtiene el valor de la propiedad sigla.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSigla() {
        return sigla;
    }

    /**
     * Define el valor de la propiedad sigla.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSigla(String value) {
        this.sigla = value;
    }

    /**
     * Obtiene el valor de la propiedad visitas.
     * 
     */
    public int getVisitas() {
        return visitas;
    }

    /**
     * Define el valor de la propiedad visitas.
     * 
     */
    public void setVisitas(int value) {
        this.visitas = value;
    }


    /**
     * <p>Clase Java para anonymous complex type.
     * 
     * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
     * 
     * <pre>{@code
     * <complexType>
     *   <complexContent>
     *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       <sequence>
     *         <element name="categoria" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
     *       </sequence>
     *     </restriction>
     *   </complexContent>
     * </complexType>
     * }</pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "categoria"
    })
    public static class Categorias {

        protected List<String> categoria;

        /**
         * Gets the value of the categoria property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the Jakarta XML Binding object.
         * This is why there is not a {@code set} method for the categoria property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getCategoria().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link String }
         * 
         * 
         * @return
         *     The value of the categoria property.
         */
        public List<String> getCategoria() {
            if (categoria == null) {
                categoria = new ArrayList<>();
            }
            return this.categoria;
        }

    }

}
=======

package soap;

import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for dtEvento complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>{@code
 * <complexType name="dtEvento">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="categorias" minOccurs="0">
 *           <complexType>
 *             <complexContent>
 *               <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 <sequence>
 *                   <element name="categoria" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *                 </sequence>
 *               </restriction>
 *             </complexContent>
 *           </complexType>
 *         </element>
 *         <element name="descripcion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         <element name="estado" type="{http://publicadores/}estadoEvento" minOccurs="0"/>
 *         <element name="fechaEvento" type="{http://publicadores/}DTFecha" minOccurs="0"/>
 *         <element name="imagen" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         <element name="nombre" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         <element name="sigla" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         <element name="visitas" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       </sequence>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * }</pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "dtEvento", propOrder = {
    "categorias",
    "descripcion",
    "estado",
    "fechaEvento",
    "imagen",
    "nombre",
    "sigla",
    "visitas"
})
public class DtEvento {

    protected DtEvento.Categorias categorias;
    protected String descripcion;
    @XmlSchemaType(name = "string")
    protected EstadoEvento estado;
    protected DTFecha fechaEvento;
    protected String imagen;
    protected String nombre;
    protected String sigla;
    protected int visitas;

    /**
     * Gets the value of the categorias property.
     * 
     * @return
     *     possible object is
     *     {@link DtEvento.Categorias }
     *     
     */
    public DtEvento.Categorias getCategorias() {
        return categorias;
    }

    /**
     * Sets the value of the categorias property.
     * 
     * @param value
     *     allowed object is
     *     {@link DtEvento.Categorias }
     *     
     */
    public void setCategorias(DtEvento.Categorias value) {
        this.categorias = value;
    }

    /**
     * Gets the value of the descripcion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Sets the value of the descripcion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescripcion(String value) {
        this.descripcion = value;
    }

    /**
     * Gets the value of the estado property.
     * 
     * @return
     *     possible object is
     *     {@link EstadoEvento }
     *     
     */
    public EstadoEvento getEstado() {
        return estado;
    }

    /**
     * Sets the value of the estado property.
     * 
     * @param value
     *     allowed object is
     *     {@link EstadoEvento }
     *     
     */
    public void setEstado(EstadoEvento value) {
        this.estado = value;
    }

    /**
     * Gets the value of the fechaEvento property.
     * 
     * @return
     *     possible object is
     *     {@link DTFecha }
     *     
     */
    public DTFecha getFechaEvento() {
        return fechaEvento;
    }

    /**
     * Sets the value of the fechaEvento property.
     * 
     * @param value
     *     allowed object is
     *     {@link DTFecha }
     *     
     */
    public void setFechaEvento(DTFecha value) {
        this.fechaEvento = value;
    }

    /**
     * Gets the value of the imagen property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getImagen() {
        return imagen;
    }

    /**
     * Sets the value of the imagen property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setImagen(String value) {
        this.imagen = value;
    }

    /**
     * Gets the value of the nombre property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Sets the value of the nombre property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNombre(String value) {
        this.nombre = value;
    }

    /**
     * Gets the value of the sigla property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSigla() {
        return sigla;
    }

    /**
     * Sets the value of the sigla property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSigla(String value) {
        this.sigla = value;
    }

    /**
     * Gets the value of the visitas property.
     * 
     */
    public int getVisitas() {
        return visitas;
    }

    /**
     * Sets the value of the visitas property.
     * 
     */
    public void setVisitas(int value) {
        this.visitas = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>{@code
     * <complexType>
     *   <complexContent>
     *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       <sequence>
     *         <element name="categoria" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
     *       </sequence>
     *     </restriction>
     *   </complexContent>
     * </complexType>
     * }</pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "categoria"
    })
    public static class Categorias {

        protected List<String> categoria;

        /**
         * Gets the value of the categoria property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the Jakarta XML Binding object.
         * This is why there is not a {@code set} method for the categoria property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getCategoria().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link String }
         * 
         * 
         * @return
         *     The value of the categoria property.
         */
        public List<String> getCategoria() {
            if (categoria == null) {
                categoria = new ArrayList<>();
            }
            return this.categoria;
        }

    }

}
>>>>>>> 8e3f4e17fd01823b8a4bc129c00786b7138cfa5f
