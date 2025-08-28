package gui.internal;

import javax.swing.*;

import excepciones.UsuarioRepetidoException;
import logica.Controladores.ControladorUsuario;
import logica.DatatypesYEnum.DTFecha;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AltaUsuarioFrame extends JInternalFrame {
    private static final long serialVersionUID = 1L;

    // Campos comunes
    private JTextField txtNickname, txtNombre, txtCorreo;

    // Campos asistente
    private JTextField txtApellido;
    private JComboBox<String> comboInstitucion;
    private JSpinner spinnerDia, spinnerMes, spinnerAnio;

    // Campos organizador
    private JTextField txtDescripcion, txtLink;

    // Combo tipo usuario
    private JComboBox<String> comboTipoUsuario;

    // Panel del formulario
    private JPanel form;

    // Datos existentes (simulación)
    private List<Usuario> usuariosExistentes = new ArrayList<>();

    // Instituciones de prueba
    private String[] instituciones = {
        "Universidad Nacional", "Colegio San José", "Instituto Tecnológico",
        "Escuela de Arte", "Centro Cultural"
    };

    public AltaUsuarioFrame() {
        super("Alta de Usuario", true, true, true, true);
        setSize(600, 400);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Inicializar usuarios de prueba
        usuariosExistentes.add(new Usuario("admin", "admin@mail.com", "Organizador"));
        usuariosExistentes.add(new Usuario("user1", "user1@mail.com", "Asistente"));

        // Crear panel del formulario
        form = new JPanel(new GridLayout(0, 2, 5, 5));
        form.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ---- Campos comunes ----
        form.add(new JLabel("Nickname:"));
        txtNickname = new JTextField();
        form.add(txtNickname);

        form.add(new JLabel("Nombre:"));
        txtNombre = new JTextField();
        form.add(txtNombre);

        form.add(new JLabel("Correo:"));
        txtCorreo = new JTextField();
        form.add(txtCorreo);

        form.add(new JLabel("Tipo Usuario:"));
        comboTipoUsuario = new JComboBox<>(new String[]{"Asistente", "Organizador"});
        comboTipoUsuario.addActionListener(e -> cambiarTipo());
        form.add(comboTipoUsuario);

        // ---- Agregar campos específicos (inicialmente vacíos) ----
        agregarCamposPorDefecto();

        add(form, BorderLayout.CENTER);

        // ---- Botones ----
        JPanel botones = new JPanel();
        JButton btnAceptar = new JButton("Aceptar");
        JButton btnCancelar = new JButton("Cancelar");

        btnAceptar.addActionListener(e -> {
			try {
				guardar();
			} catch (UsuarioRepetidoException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
        btnCancelar.addActionListener(e -> dispose());

        botones.add(btnAceptar);
        botones.add(btnCancelar);
        add(botones, BorderLayout.SOUTH);

        // Inicializar con el tipo por defecto
        cambiarTipo();
    }

    private void agregarCamposPorDefecto() {
        // Campos para Asistente
        txtApellido = new JTextField();
        comboInstitucion = new JComboBox<>(instituciones);
        comboInstitucion.setEnabled(false); // Inicialmente deshabilitado

        // Spinner para fecha de nacimiento
        spinnerDia = new JSpinner(new SpinnerNumberModel(1, 1, 31, 1));
        spinnerMes = new JSpinner(new SpinnerNumberModel(1, 1, 12, 1));
        spinnerAnio = new JSpinner(new SpinnerNumberModel(2000, 1900, 2025, 1));

        // Evitar que se escriba texto libre en los spinners
        JSpinner.DefaultEditor editorDia = (JSpinner.DefaultEditor) spinnerDia.getEditor();
        JSpinner.DefaultEditor editorMes = (JSpinner.DefaultEditor) spinnerMes.getEditor();
        JSpinner.DefaultEditor editorAnio = (JSpinner.DefaultEditor) spinnerAnio.getEditor();

        editorDia.getTextField().setEditable(false);
        editorMes.getTextField().setEditable(false);
        editorAnio.getTextField().setEditable(false);

        // Campos para Organizador
        txtDescripcion = new JTextField();
        txtLink = new JTextField();

        // Deshabilitar todos los campos específicos al inicio
        deshabilitarCamposAsistente();
        deshabilitarCamposOrganizador();
    }

    private void cambiarTipo() {
        // Limpiar el panel
        form.removeAll();

        // Reagregar los campos comunes
        form.add(new JLabel("Nickname:")); form.add(txtNickname);
        form.add(new JLabel("Nombre:")); form.add(txtNombre);
        form.add(new JLabel("Correo:")); form.add(txtCorreo);
        form.add(new JLabel("Tipo Usuario:")); form.add(comboTipoUsuario);

        String tipo = (String) comboTipoUsuario.getSelectedItem();

        if ("Asistente".equals(tipo)) {
            // Apellido
            form.add(new JLabel("Apellido:")); form.add(txtApellido);

            // Fecha de nacimiento con spinners
            form.add(new JLabel("Fecha Nacimiento:"));
            JPanel panelFecha = new JPanel(new FlowLayout(FlowLayout.LEFT));
            panelFecha.add(new JLabel("Día:"));
            panelFecha.add(spinnerDia);
            panelFecha.add(new JLabel("Mes:"));
            panelFecha.add(spinnerMes);
            panelFecha.add(new JLabel("Año:"));
            panelFecha.add(spinnerAnio);
            form.add(panelFecha);

            // Institución
            form.add(new JLabel("Institución:")); form.add(comboInstitucion);

            // Habilitar campos de asistente
            habilitarCamposAsistente();
            deshabilitarCamposOrganizador();

        } else if ("Organizador".equals(tipo)) {
            // Descripción
            form.add(new JLabel("Descripción:")); form.add(txtDescripcion);

            // Sitio web
            form.add(new JLabel("Sitio Web:")); form.add(txtLink);

            // Habilitar campos de organizador
            habilitarCamposOrganizador();
            deshabilitarCamposAsistente();
        }

        form.revalidate();
        form.repaint();
    }

    private void habilitarCamposAsistente() {
        txtApellido.setEnabled(true);
        spinnerDia.setEnabled(true);
        spinnerMes.setEnabled(true);
        spinnerAnio.setEnabled(true);
        comboInstitucion.setEnabled(true);
    }

    private void deshabilitarCamposAsistente() {
        txtApellido.setEnabled(false);
        spinnerDia.setEnabled(false);
        spinnerMes.setEnabled(false);
        spinnerAnio.setEnabled(false);
        comboInstitucion.setEnabled(false);
    }

    private void habilitarCamposOrganizador() {
        txtDescripcion.setEnabled(true);
        txtLink.setEnabled(true);
    }

    private void deshabilitarCamposOrganizador() {
        txtDescripcion.setEnabled(false);
        txtLink.setEnabled(false);
    }

    private void guardar() throws UsuarioRepetidoException {
        String nickname = txtNickname.getText().trim();
        String nombre = txtNombre.getText().trim();
        String correo = txtCorreo.getText().trim();
        String tipo = (String) comboTipoUsuario.getSelectedItem();

        //campos organizador
        String descr = txtDescripcion.getText().trim();
        String link = txtLink.getText().trim();

        //campos Asistente
        String apellido = txtApellido.getText().trim();
        String ins = (String) comboInstitucion.getSelectedItem();

        int diaV = (int)spinnerDia.getValue();
        int mesV = (int)spinnerMes.getValue();
        int anioV = (int)spinnerAnio.getValue();
        DTFecha fechanac = new DTFecha(diaV,mesV,anioV);


        ControladorUsuario cont = ControladorUsuario.getInstance();


        if (nickname.isEmpty() || nombre.isEmpty() || correo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nickname, Nombre y Correo son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        //se manejan los repetidos en la interfaz

        if (cont.ExisteNickname(nickname)) {
            JOptionPane.showMessageDialog(this, "El nickname '" + nickname + "' ya está en uso.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (cont.ExisteCorreo(correo)) {
            JOptionPane.showMessageDialog(this, "El correo '" + correo + "' ya está en uso.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if ("Asistente".equals(tipo)) {
            if (txtApellido.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debe ingresar el apellido.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (comboInstitucion.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(this, "Debe seleccionar una institución.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int dia = (Integer) spinnerDia.getValue();
            int mes = (Integer) spinnerMes.getValue();
            int anio = (Integer) spinnerAnio.getValue();

            if (dia < 1 || dia > 31 || mes < 1 || mes > 12) {
                JOptionPane.showMessageDialog(this, "Fecha inválida.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } else if ("Organizador".equals(tipo)) {
            if (txtDescripcion.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debe ingresar una descripción.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        //paso todos los filtros, esta listo para crear

        if("Organizador".equals(tipo)) {
        	cont.altaOrganizador(nickname, nombre, correo, descr, link);
        }else {
        	cont.altaAsistente(nickname, nombre, correo, apellido, fechanac, ins);
        }


        JOptionPane.showMessageDialog(this, "Usuario dado de alta correctamente.");
        limpiarFormulario();
        dispose();
    }

    private void limpiarFormulario() {
        txtNickname.setText("");
        txtNombre.setText("");
        txtCorreo.setText("");
        txtApellido.setText("");
        txtDescripcion.setText("");
        txtLink.setText("");
        comboInstitucion.setSelectedIndex(0);

        // Resetear spinners
        spinnerDia.setValue(1);
        spinnerMes.setValue(1);
        spinnerAnio.setValue(2000);

        // Volver a cargar el tipo (y actualiza los campos)
        cambiarTipo();
    }



    // Clase interna para simular usuarios
    private static class Usuario {
        String nickname, correo, rol;
        Usuario(String nickname, String correo, String rol) {
            this.nickname = nickname;
            this.correo = correo;
            this.rol = rol;
        }
    }
}