package gui.internal;

import javax.swing.*;

import excepciones.CorreoInvalidoException;
import excepciones.UsuarioRepetidoException;
import logica.controladores.ControladorUsuario;
import logica.datatypesyenum.DTFecha;

import java.awt.*;
import java.util.Set;

public class AltaUsuarioFrame extends JInternalFrame {
    private static final long serialVersionUID = 1L;

    // Campos comunes
    private JTextField txtNickname, txtNombre, txtCorreo, txtApellido;
    private JPasswordField txtPassword, txtConfirmPassword;

    // Campos asistente
    private JComboBox<String> comboInstitucion;
    private JSpinner spinnerDia, spinnerMes, spinnerAnio;

    // Campos organizador
    private JTextField txtDescripcion, txtLink;

    // Combo tipo usuario
    private JComboBox<String> comboTipoUsuario;

    // Panel del formulario
    private JPanel form;

    public AltaUsuarioFrame() {
        super("Alta de Usuario", true, true, true, true);
        setSize(600, 450);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Crear panel del formulario
        form = new JPanel(new GridLayout(0, 2, 5, 5));
        form.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ---- Combo tipo usuario ----
        form.add(new JLabel("Tipo Usuario:"));
        comboTipoUsuario = new JComboBox<>(new String[]{"Asistente", "Organizador"});
        comboTipoUsuario.addActionListener(e -> cambiarTipo());
        form.add(comboTipoUsuario);

        // ---- Campos comunes ----
        form.add(new JLabel("Nickname:")); txtNickname = new JTextField(); form.add(txtNickname);
        form.add(new JLabel("Nombre:")); txtNombre = new JTextField(); form.add(txtNombre);
        form.add(new JLabel("Correo:")); txtCorreo = new JTextField(); form.add(txtCorreo);
        form.add(new JLabel("Contraseña:")); txtPassword = new JPasswordField(); form.add(txtPassword);
        form.add(new JLabel("Confirmar Contraseña:")); txtConfirmPassword = new JPasswordField(); form.add(txtConfirmPassword);

        // ---- Agregar campos específicos (inicialmente vacíos) ----
        agregarCamposPorDefecto();

        add(form, BorderLayout.CENTER);

        // ---- Botones ----
        JPanel botones = new JPanel();
        JButton btnAceptar = new JButton("Aceptar");
        JButton btnCancelar = new JButton("Cancelar");

        btnAceptar.addActionListener(e -> guardar());
        btnCancelar.addActionListener(e -> dispose());

        botones.add(btnAceptar);
        botones.add(btnCancelar);
        add(botones, BorderLayout.SOUTH);

        // Inicializar con el tipo por defecto
        cambiarTipo();
    }

    private void agregarCamposPorDefecto() {
        // Campos Asistente
        txtApellido = new JTextField();
        comboInstitucion = new JComboBox<>();
        comboInstitucion.setEnabled(false);

        spinnerDia = new JSpinner(new SpinnerNumberModel(1, 1, 31, 1));
        spinnerMes = new JSpinner(new SpinnerNumberModel(1, 1, 12, 1));
        spinnerAnio = new JSpinner(new SpinnerNumberModel(2000, 1900, 2100, 1));

        configurarSpinnerEditable(spinnerDia);
        configurarSpinnerEditable(spinnerMes);
        configurarSpinnerEditable(spinnerAnio);

        // Campos Organizador
        txtDescripcion = new JTextField();
        txtLink = new JTextField();

        // Deshabilitar al inicio
        deshabilitarCamposAsistente();
        deshabilitarCamposOrganizador();
    }

    private void cambiarTipo() {
        form.removeAll();

        String tipo = (String) comboTipoUsuario.getSelectedItem();

        // ---- Tipo Usuario primero ----
        form.add(new JLabel("Tipo Usuario:")); form.add(comboTipoUsuario);

        // ---- Campos comunes ----
        form.add(new JLabel("Nickname:")); form.add(txtNickname);
        form.add(new JLabel("Nombre:")); form.add(txtNombre);
        if ("Asistente".equals(tipo)) {
            form.add(new JLabel("Apellido:")); form.add(txtApellido);
        }
        form.add(new JLabel("Correo:")); form.add(txtCorreo);
        form.add(new JLabel("Contraseña:")); form.add(txtPassword);
        form.add(new JLabel("Confirmar Contraseña:")); form.add(txtConfirmPassword);

        // ---- Campos específicos ----
        if ("Asistente".equals(tipo)) {
            // Fecha
            form.add(new JLabel("Fecha Nacimiento:"));
            JPanel panelFecha = new JPanel(new FlowLayout(FlowLayout.LEFT));
            panelFecha.add(new JLabel("Día:")); panelFecha.add(spinnerDia);
            panelFecha.add(new JLabel("Mes:")); panelFecha.add(spinnerMes);
            panelFecha.add(new JLabel("Año:")); panelFecha.add(spinnerAnio);
            form.add(panelFecha);

            // Institución
            ControladorUsuario contrU = ControladorUsuario.getInstance();
            Set<String> instituciones = contrU.listarInstituciones();
            comboInstitucion.removeAllItems();
            comboInstitucion.addItem("Sin institución");
            for(String i: instituciones) comboInstitucion.addItem(i);
            comboInstitucion.setSelectedIndex(0);
            form.add(new JLabel("Institución:")); form.add(comboInstitucion);

            habilitarCamposAsistente();
            deshabilitarCamposOrganizador();
        } else {
            // Organizador
            form.add(new JLabel("Descripción:")); form.add(txtDescripcion);
            form.add(new JLabel("Sitio Web:")); form.add(txtLink);

            habilitarCamposOrganizador();
            deshabilitarCamposAsistente();
        }

        form.revalidate();
        form.repaint();
    }

    private void configurarSpinnerEditable(JSpinner spinner) {
        JSpinner.NumberEditor editor = new JSpinner.NumberEditor(spinner, "#");
        spinner.setEditor(editor);
        JFormattedTextField textField = ((JSpinner.DefaultEditor) editor).getTextField();
        textField.setEditable(true);
        textField.setHorizontalAlignment(JTextField.CENTER);
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

    private void guardar() {
        String tipo = (String) comboTipoUsuario.getSelectedItem();
        String nickname = txtNickname.getText().trim();
        String nombre = txtNombre.getText().trim();
        String correo = txtCorreo.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();
        String confirmPassword = new String(txtConfirmPassword.getPassword()).trim();

        if (nickname.isEmpty() || nombre.isEmpty() || correo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nickname, Nombre y Correo son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe ingresar una contraseña.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (password.length() < 6) {
            JOptionPane.showMessageDialog(this, "La contraseña debe tener al menos 6 caracteres.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Las contraseñas no coinciden.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        DTFecha fechanac = new DTFecha(
                (int) spinnerDia.getValue(),
                (int) spinnerMes.getValue(),
                (int) spinnerAnio.getValue()
        );

        String apellido = txtApellido.getText().trim();
        String ins = (String) comboInstitucion.getSelectedItem();
        if ("Sin institución".equals(ins)) ins = "";

        String descr = txtDescripcion.getText().trim();
        String link = txtLink.getText().trim();

        // Validaciones por tipo
        if ("Asistente".equals(tipo)) {
            if (apellido.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debe ingresar el apellido.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } else {
            if (descr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debe ingresar una descripción.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (link.isEmpty() || !link.contains(".")) {
                JOptionPane.showMessageDialog(this, "El sitio web debe ser válido (debe contener un punto).", "Error de formato", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        try {
            ControladorUsuario cont = ControladorUsuario.getInstance();
            if ("Organizador".equals(tipo)) {
                cont.altaOrganizador(nickname, nombre, correo, descr, link, password);
            } else {
                cont.altaAsistente(nickname, nombre, correo, apellido, fechanac, ins, password);
            }

            JOptionPane.showMessageDialog(this, "Usuario dado de alta correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            limpiarFormulario();
            dispose();
        } catch (UsuarioRepetidoException | CorreoInvalidoException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error inesperado: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void limpiarFormulario() {
        txtNickname.setText("");
        txtNombre.setText("");
        txtApellido.setText("");
        txtCorreo.setText("");
        txtPassword.setText("");
        txtConfirmPassword.setText("");
        txtDescripcion.setText("");
        txtLink.setText("");
        comboInstitucion.setSelectedIndex(0);

        spinnerDia.setValue(1);
        spinnerMes.setValue(1);
        spinnerAnio.setValue(2000);

        cambiarTipo();
    }
}
