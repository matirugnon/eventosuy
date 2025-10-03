package gui.internal;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import excepciones.UsuarioNoExisteException;
import logica.Controladores.IControladorUsuario;
import logica.DatatypesYEnum.DTAsistente;
import logica.DatatypesYEnum.DTFecha;
import logica.DatatypesYEnum.DTOrganizador;
import logica.DatatypesYEnum.DTUsuario;

public class ModificarUsuarioFrame extends JInternalFrame {

    private static final long serialVersionUID = 1L;
    private JComboBox<String> comboUsuarios;
    private JTextField txtNombre;
    private JTextField txtApellido;
    private JTextField txtDescripcion;
    private JTextField txtLink;
    private JTextField txtDia, txtMes, txtAnio;

    private JLabel lblNombre, lblApellido, lblDescripcion, lblLink, lblFecha;

    private DTUsuario dtUsuarioActual;

    private JButton btnGuardar;
    private IControladorUsuario ctrlUsuario;
    private JPanel panelCampos;

    public ModificarUsuarioFrame() {
        ctrlUsuario = IControladorUsuario.getInstance();
        inicializarGUI();
    }

    private void inicializarGUI() {
        setTitle("Modificar Usuario");
        setClosable(true);
        setMaximizable(false);
        setIconifiable(false);
        setFrameIcon(null);
        setSize(500, 400);
        setLayout(new BorderLayout());

        // Panel superior: selección de usuario
        JPanel panelSeleccion = new JPanel();
        panelSeleccion.add(new JLabel("Seleccionar usuario: "));
        comboUsuarios = new JComboBox<>();
        cargarUsuarios();
        comboUsuarios.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    mostrarDatosUsuario();
                } catch (UsuarioNoExisteException e1) {
                    e1.printStackTrace();
                }
            }
        });
        panelSeleccion.add(comboUsuarios);
        add(panelSeleccion, BorderLayout.NORTH);

        // Panel central: campos editables
        panelCampos = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int fila = 0;

        // Nombre
        lblNombre = new JLabel("Nombre:");
        gbc.gridx = 0; gbc.gridy = fila; panelCampos.add(lblNombre, gbc);
        gbc.gridx = 1; txtNombre = new JTextField(20); panelCampos.add(txtNombre, gbc); fila++;

        // Apellido
        lblApellido = new JLabel("Apellido:");
        gbc.gridx = 0; gbc.gridy = fila; panelCampos.add(lblApellido, gbc);
        gbc.gridx = 1; txtApellido = new JTextField(20); panelCampos.add(txtApellido, gbc); fila++;

        // Descripción
        lblDescripcion = new JLabel("Descripción:");
        gbc.gridx = 0; gbc.gridy = fila; panelCampos.add(lblDescripcion, gbc);
        gbc.gridx = 1; txtDescripcion = new JTextField(20); panelCampos.add(txtDescripcion, gbc); fila++;

        // Link
        lblLink = new JLabel("Link:");
        gbc.gridx = 0; gbc.gridy = fila; panelCampos.add(lblLink, gbc);
        gbc.gridx = 1; txtLink = new JTextField(20); panelCampos.add(txtLink, gbc); fila++;

        // Fecha de nacimiento
        lblFecha = new JLabel("Fecha Nac (dd/mm/aaaa):");
        gbc.gridx = 0; gbc.gridy = fila; panelCampos.add(lblFecha, gbc);
        JPanel panelFecha = new JPanel();
        txtDia = new JTextField("dd", 2);
        txtMes = new JTextField("mm", 2);
        txtAnio = new JTextField("aaaa", 4);
        panelFecha.add(txtDia);
        panelFecha.add(new JLabel("/"));
        panelFecha.add(txtMes);
        panelFecha.add(new JLabel("/"));
        panelFecha.add(txtAnio);
        gbc.gridx = 1; panelCampos.add(panelFecha, gbc); fila++;

        add(panelCampos, BorderLayout.CENTER);
        ocultarCamposPorDefecto();

        // Botón guardar
        btnGuardar = new JButton("Guardar Cambios");
        btnGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarCambios();
            }
        });
        add(btnGuardar, BorderLayout.SOUTH);
    }

    private void cargarUsuarios() {
        comboUsuarios.addItem("-- Seleccionar usuario --");
        Set<String> usuarios = ctrlUsuario.listarUsuarios();
        for (String nick : usuarios) {
            comboUsuarios.addItem(nick);
        }
    }

    private void mostrarDatosUsuario() throws UsuarioNoExisteException {
        String nick = (String) comboUsuarios.getSelectedItem();
        if (nick == null || nick.equals("-- Seleccionar usuario --")) {
            limpiarCampos();
            ocultarCamposPorDefecto();
            return;
        }

        dtUsuarioActual = ctrlUsuario.getDTUsuario(nick);
        if (dtUsuarioActual == null) {
            JOptionPane.showMessageDialog(this, "No se pudo cargar la información del usuario.");
            return;
        }

        txtNombre.setText(dtUsuarioActual.getNombre());
        ocultarCamposPorDefecto();
        mostrarCamposGenerales(true);

        if (dtUsuarioActual instanceof DTAsistente) {
            DTAsistente dtA = (DTAsistente) dtUsuarioActual;
            txtApellido.setText(dtA.getApellido());
            DTFecha fecha = dtA.getFechaNacimiento();
            if (fecha != null) {
                txtDia.setText(String.valueOf(fecha.getDia()));
                txtMes.setText(String.valueOf(fecha.getMes()));
                txtAnio.setText(String.valueOf(fecha.getAnio()));
            }
            mostrarCamposAsistente(true);
        } else if (dtUsuarioActual instanceof DTOrganizador) {
            DTOrganizador dtO = (DTOrganizador) dtUsuarioActual;
            txtDescripcion.setText(dtO.getDescripcion());
            txtLink.setText(dtO.getLink());
            mostrarCamposOrganizador(true);
        }
    }

    private void mostrarCamposGenerales(boolean mostrar) {
        lblNombre.setVisible(mostrar);
        txtNombre.setVisible(mostrar);
    }

    private void mostrarCamposAsistente(boolean mostrar) {
        lblApellido.setVisible(mostrar);
        txtApellido.setVisible(mostrar);
        lblFecha.setVisible(mostrar);
        txtDia.setVisible(mostrar);
        txtMes.setVisible(mostrar);
        txtAnio.setVisible(mostrar);
    }

    private void mostrarCamposOrganizador(boolean mostrar) {
        lblDescripcion.setVisible(mostrar);
        txtDescripcion.setVisible(mostrar);
        lblLink.setVisible(mostrar);
        txtLink.setVisible(mostrar);
    }

    private void ocultarCamposPorDefecto() {
        mostrarCamposGenerales(false);
        mostrarCamposAsistente(false);
        mostrarCamposOrganizador(false);
    }

    private void limpiarCampos() {
        txtNombre.setText("");
        txtApellido.setText("");
        txtDescripcion.setText("");
        txtLink.setText("");
        txtDia.setText("dd");
        txtMes.setText("mm");
        txtAnio.setText("aaaa");
    }

    private void guardarCambios() {
        String nick = (String) comboUsuarios.getSelectedItem();
        if (nick == null || nick.equals("-- Seleccionar usuario --")) {
            JOptionPane.showMessageDialog(this, "Seleccione un usuario válido.");
            return;
        }

        String nombre = txtNombre.getText().trim();
        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre no puede estar vacío.");
            return;
        }

        try {
            DTUsuario dtModificado;

            if (dtUsuarioActual instanceof DTAsistente) {
                String apellido = txtApellido.getText().trim();
                if (apellido.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "El apellido no puede estar vacío.");
                    return;
                }

                int dia, mes, anio;
                try {
                    dia = Integer.parseInt(txtDia.getText());
                    mes = Integer.parseInt(txtMes.getText());
                    anio = Integer.parseInt(txtAnio.getText());
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Formato de fecha inválido.");
                    return;
                }

                if (!ctrlUsuario.esFechaValida(dia, mes, anio)) {
                    JOptionPane.showMessageDialog(this, "Fecha de nacimiento no válida.");
                    return;
                }

                DTFecha fechaNac = new DTFecha(dia, mes, anio);
                DTAsistente dtA = (DTAsistente) dtUsuarioActual;

                dtModificado = new DTAsistente(
                        nick,
                        nombre,
                        dtA.getCorreo(),
                        "", // password vacío
                        apellido,
                        fechaNac,
                        dtA.getInstitucion(),
                        "" // avatar vacío
                );
            } else if (dtUsuarioActual instanceof DTOrganizador) {
                String descripcion = txtDescripcion.getText().trim();
                String link = txtLink.getText().trim();

                DTOrganizador dtO = (DTOrganizador) dtUsuarioActual;
                dtModificado = new DTOrganizador(
                        nick,
                        nombre,
                        dtO.getCorreo(),
                        "", // password vacío
                        descripcion,
                        link,
                        "" // avatar vacío
                );
            } else {
                JOptionPane.showMessageDialog(this, "Tipo de usuario desconocido.");
                return;
            }

            ctrlUsuario.modificarUsuario(nick, dtModificado);
            JOptionPane.showMessageDialog(this, "Usuario modificado con éxito.");
            dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al modificar usuario: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
