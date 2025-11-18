package logica.datatypesyenum;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class DTAsistente extends DTUsuario {

    private String apellido;
	private DTFecha fechaNacimiento;
    private String institucion;
    
    public DTAsistente() {}

    public DTAsistente(String nickname, String nombre, String correo, String password, String apellido, DTFecha fechaNacimiento, String institucion, String avatar) {
        super(nickname, nombre, correo, password, avatar); // Actualizado para incluir avatar
        this.apellido = apellido;
        this.fechaNacimiento = fechaNacimiento;
        this.institucion = institucion;
    }

    // Getters
    public String getApellido() {
        return apellido;
    }

    public DTFecha getFechaNacimiento() {
        return fechaNacimiento;
    }

    public String getInstitucion() {
        return institucion;
    }
    public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public void setFechaNacimiento(DTFecha fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	public void setInstitucion(String institucion) {
		this.institucion = institucion;
	}
    
   
}
