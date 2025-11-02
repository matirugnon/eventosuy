package logica;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import logica.datatypesyenum.DTAsistente;
import logica.datatypesyenum.DTFecha;
import logica.datatypesyenum.DTRegistro;
import logica.datatypesyenum.DTUsuario;

public class Asistente extends Usuario {

    private String apellido;
    private DTFecha fechaNacimiento;
    private Set<Registro> registros;
    private String institucion;

    public Asistente(String nickname, String nombre, String correo, String apellido, DTFecha fechaNacimiento, String ins, String password, String avatar) {
        super(nickname, nombre, correo, password, avatar);
        this.apellido = apellido;
        this.fechaNacimiento = fechaNacimiento;
        this.registros = new HashSet<>();
        this.institucion = ins;
    }

    public String getApellido() {
        return apellido;
    }

    public DTFecha getFechaNacimiento() {
        return fechaNacimiento;
    }

    @Override
    public String getTipo() {
        return "asistente";
    }

    public void setApellido(String text) {
        apellido = text;
    }

    public void setFechaNacimiento(DTFecha fecha) {
        this.fechaNacimiento = fecha;
    }

    public void agregarRegistro(Registro reg) {
        this.registros.add(reg);
    }

    public String getInstitucion() {
        return institucion;
    }

    public Set<String> getNomsTipo() {
        Set<String> nombresTipos = new HashSet<>();
        for (Registro r : registros) {
            if (r != null) {
                String nombreTipo = r.getNomTipo();
                if (nombreTipo != null) {
                    nombresTipos.add(nombreTipo);
                }
            }
        }
        return nombresTipos;
    }

    public DTUsuario getDTAsistente() {
        DTAsistente dto = new DTAsistente(
                getNickname(),
                getNombre(),
                getCorreo(),
                getPassword(),
                getApellido(),
                getFechaNacimiento(),
                getInstitucion(),
                getAvatar());
        dto.setSeguidores(new ArrayList<>(getSeguidores()));
        dto.setSeguidos(new ArrayList<>(getSeguidos()));
        return dto;
    }

    public DTRegistro getRegistro(String nomTipoReg) {
        if (registros == null) {
            return null;
        }

        for (Registro r : this.registros) {
            String nombreTR = r.getTipoDeRegistro().getNombre();
            if (nombreTR != null && nombreTR.equals(nomTipoReg)) {
                String asistente = this.getNombre();
                DTFecha fecha = r.getFechaRegistro();
                Double costo = r.getCosto();
                String edicion = r.getNomEdicion();
                boolean asistio = r.getAsistio();

                return new DTRegistro(asistente, nombreTR, fecha, costo, edicion, asistio);
            }
        }

        return null;
    }

    public Set<Registro> getRegistros() {
        Set<Registro> resultado = new HashSet<>();

        for (Registro r : registros) {
            resultado.add(r);
        }

        return resultado;
    }
    
    public Registro obtenerRegistro(String nomEdicion, String nomTipoRegistro) {
        if (registros == null) {
            return null;
        }
        
        for (Registro r : registros) {
            String nombreTR = r.getTipoDeRegistro().getNombre();
            String nomEd = r.getNomEdicion();
            if (nombreTR != null && nombreTR.equals(nomTipoRegistro) && 
                nomEd != null && nomEd.equals(nomEdicion)) {
                return r;
            }
        }
        
        return null;
    }
}
