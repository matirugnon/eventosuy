package gui.internal;

import javax.swing.*;
import java.awt.*;

@SuppressWarnings("serial")
public class ModificarUsuarioFrame extends JInternalFrame {
    private JComboBox<String> comboUsuarios;
    private JTextField txtNickname, txtCorreo, txtNombre, txtApellido, txtFechaNac, txtDescripcion, txtWeb;
    private JButton btnGuardar, btnCancelar;

    public ModificarUsuarioFrame() {
        super("Modificar Usuario", true, true, true, true);
        setSize(500, 400);
        setLayout(new BorderLayout());

        // TOP: selección de usuario
        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Seleccione Usuario:"));
        comboUsuarios = new JComboBox<>();
        // ⚠️ Datos de prueba
        comboUsuarios.addItem("juan123 (Asistente)");
        comboUsuarios.addItem("mariaOrg (Organizador)");
        topPanel.add(comboUsuarios);
        JButton btnCargar = new JButton("Cargar Datos");
        topPanel.add(btnCargar);
        add(topPanel, BorderLayout.NORTH);

        // CENTER: formulario editable
        JPanel form = new JPanel(new GridLayout(0, 2, 5, 5));

        form.add(new JLabel("Nickname:"));
        txtNickname = new JTextField();
        txtNickname.setEditable(false); // no se puede modificar
        form.add(txtNickname);

        form.add(new JLabel("Correo:"));
        txtCorreo = new JTextField();
        txtCorreo.setEditable(false); // no se puede modificar
        form.add(txtCorreo);

        form.add(new JLabel("Nombre:"));
        txtNombre = new JTextField();
        form.add(txtNombre);

        form.add(new JLabel("Apellido (solo asistentes):"));
        txtApellido = new JTextField();
        form.add(txtApellido);

        form.add(new JLabel("Fecha Nacimiento (YYYY-MM-DD)(solo asistentes):"));
        txtFechaNac = new JTextField();
        form.add(txtFechaNac);

        form.add(new JLabel("Descripción (solo organizador):"));
        txtDescripcion = new JTextField();
        form.add(txtDescripcion);

        form.add(new JLabel("Sitio Web (solo organizador):"));
        txtWeb = new JTextField();
        form.add(txtWeb);

        add(form, BorderLayout.CENTER);

        // BOTTOM: botones
        JPanel botones = new JPanel();
        btnGuardar = new JButton("Guardar");
        btnCancelar = new JButton("Cancelar");
        botones.add(btnGuardar);
        botones.add(btnCancelar);
        add(botones, BorderLayout.SOUTH);

        // Eventos
        btnCargar.addActionListener(e -> cargarDatosUsuario());
        btnGuardar.addActionListener(e -> guardarCambios());
        btnCancelar.addActionListener(e -> dispose());

        // Inicializar estado de campos según selección inicial (por defecto)
        actualizarCamposPorRol();
        comboUsuarios.addActionListener(e -> actualizarCamposPorRol());
    }

    // Actualiza la habilitación de campos según el rol seleccionado
    private void actualizarCamposPorRol() {
        String seleccionado = (String) comboUsuarios.getSelectedItem();
        boolean esAsistente = seleccionado != null && seleccionado.contains("Asistente");
        boolean esOrganizador = seleccionado != null && seleccionado.contains("Organizador");

        // Asistente: habilita apellido y fecha, deshabilita descripción y web
        txtApellido.setEditable(esAsistente);
        txtFechaNac.setEditable(esAsistente);

        // Organizador: habilita descripción y web, deshabilita apellido y fecha
        txtDescripcion.setEditable(esOrganizador);
        txtWeb.setEditable(esOrganizador);
    }

    // ⚠️ Mock de datos de ejemplo
    private void cargarDatosUsuario() {
        String seleccionado = (String) comboUsuarios.getSelectedItem();

        if (seleccionado.contains("Asistente")) {
            txtNickname.setText("juan123");
            txtCorreo.setText("juan@mail.com");
            txtNombre.setText("Juan");
            txtApellido.setText("Pérez");
            txtFechaNac.setText("1999-05-21");
            txtDescripcion.setText("");
            txtWeb.setText("");
        } else {
            txtNickname.setText("mariaOrg");
            txtCorreo.setText("maria@mail.com");
            txtNombre.setText("María");
            txtApellido.setText("");
            txtFechaNac.setText("");
            txtDescripcion.setText("Organizadora de conferencias");
            txtWeb.setText("https://mariaorg.com");
        }

        // Aseguramos que los campos se actualicen correctamente tras cargar
        actualizarCamposPorRol();
    }

    private void guardarCambios() {
        JOptionPane.showMessageDialog(this,
                "Usuario " + txtNickname.getText() + " modificado con éxito.",
                "Éxito", JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }
}