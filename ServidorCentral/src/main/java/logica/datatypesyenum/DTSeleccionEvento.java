package logica.datatypesyenum;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlTransient;

public class DTSeleccionEvento implements Serializable {
    private static final long serialVersionUID = 1L;

    private DTEvento evento;
    private Set<String> nombresCategorias;
    private Set<String> nombresEdiciones;
    private Set<DTEdicion> edicionesCompletas;

    public DTSeleccionEvento() {}

    public DTSeleccionEvento(DTEvento evento, Set<String> categorias, Set<String> ediciones) {
        this.evento = evento;
        this.nombresCategorias = categorias != null ? categorias : new HashSet<>();
        this.nombresEdiciones = ediciones != null ? ediciones : new HashSet<>();
        this.edicionesCompletas = new HashSet<>();
    }

    public DTSeleccionEvento(DTEvento evento, Set<String> categorias, Set<String> ediciones, Set<DTEdicion> edicionesCompletas) {
        this.evento = evento;
        this.nombresCategorias = categorias != null ? categorias : new HashSet<>();
        this.nombresEdiciones = ediciones != null ? ediciones : new HashSet<>();
        this.edicionesCompletas = edicionesCompletas != null ? edicionesCompletas : new HashSet<>();
    }

    // === Getters originales (para la lógica interna) ===
    @XmlTransient
    public Set<String> getNombresCategorias() { return nombresCategorias; }

    @XmlTransient
    public Set<String> getNombresEdiciones() { return nombresEdiciones; }

    @XmlTransient
    public Set<DTEdicion> getEdicionesCompletas() { return edicionesCompletas; }

    // === Getters auxiliares para JAXB (SOAP-friendly) ===
    @XmlElement(name = "nombresCategorias")
    public List<String> getNombresCategoriasList() { return new ArrayList<>(nombresCategorias); }

    @XmlElement(name = "nombresEdiciones")
    public List<String> getNombresEdicionesList() { return new ArrayList<>(nombresEdiciones); }

    @XmlElement(name = "edicionesCompletas")
    public List<DTEdicion> getEdicionesCompletasList() { return new ArrayList<>(edicionesCompletas); }

    // === Otros getters ===
    public DTEvento getEvento() { return evento; }

    public String getNombre() { return evento != null ? evento.getNombre() : null; }

    public String getSigla() { return evento != null ? evento.getSigla() : null; }

    public String getDescripcion() { return evento != null ? evento.getDescripcion() : null; }

    public DTFecha getFechaEvento() { return evento != null ? evento.getFechaEvento() : null; }

    // === Setters ===
    public void setEvento(DTEvento evento) { this.evento = evento; }

    public void setNombresCategorias(Set<String> nombresCategorias) { this.nombresCategorias = nombresCategorias; }

    public void setNombresEdiciones(Set<String> nombresEdiciones) { this.nombresEdiciones = nombresEdiciones; }

    public void setEdicionesCompletas(Set<DTEdicion> edicionesCompletas) { this.edicionesCompletas = edicionesCompletas; }
}

