package logica.datatypesyenum;


import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;
import java.io.Serializable;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DTFecha", propOrder = {"dia", "mes", "anio"})

public class DTFecha implements Comparable<DTFecha>, Serializable {
    private int dia, mes, anio;

    public DTFecha() {}

    public DTFecha(int dia, int mes, int anio) {
        this.dia = dia;
        this.mes = mes;
        this.anio = anio;
    }

    // Getters
    public int getDia() { return dia; }
    public int getMes() { return mes; }
    public int getAnio() { return anio; }

    @Override
    public int compareTo(DTFecha otra) {
        if (this.anio != otra.anio) return Integer.compare(this.anio, otra.anio);
        if (this.mes != otra.mes) return Integer.compare(this.mes, otra.mes);
        return Integer.compare(this.dia, otra.dia);
    }

    @Override
    public String toString() {
        return String.format("%02d/%02d/%04d", dia, mes, anio);
    }
}