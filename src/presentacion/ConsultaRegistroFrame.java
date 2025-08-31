package presentacion;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import logica.Controladores.ControladorRegistro;
import logica.Controladores.ControladorUsuario;
import logica.DatatypesYEnum.DTFecha;
import logica.DatatypesYEnum.DTRegistro;

import java.awt.*;
import java.util.Set;

public class ConsultaRegistroFrame extends JInternalFrame {

    private JComboBox<String> comboUsuarios;
    private JComboBox<String> comboRegistros;


    private JTextArea areaDetalles;


    public ConsultaRegistroFrame() {
        super("Consulta de Registro", true, true, true, true);
        setSize(750, 450);
        setLayout(new BorderLayout());
        configurarComponentes();
        cargarUsuarios();
        setVisible(true);
    }


    private void configurarComponentes() {
        // ----- NORTE -----
        JPanel panelNorte = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Col 0: etiqueta Usuarios
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        panelNorte.add(new JLabel("Usuarios:"), gbc);

        // Col 1: comboUsuarios
        comboUsuarios = new JComboBox<>();
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1;
        panelNorte.add(comboUsuarios, gbc);

        // Col 2: etiqueta Registros
        gbc.gridx = 2; gbc.gridy = 0; gbc.weightx = 0;
        panelNorte.add(new JLabel("Registros:"), gbc);

        // Col 3: comboRegistros (arranca deshabilitado)
        comboRegistros = new JComboBox<>();
        comboRegistros.setEnabled(false);
        gbc.gridx = 3; gbc.gridy = 0; gbc.weightx = 1;
        panelNorte.add(comboRegistros, gbc);

        // Col 4: botón Ver Detalles
        JButton btnVerDetalles = new JButton("Ver Detalles");
        gbc.gridx = 4; gbc.gridy = 0; gbc.weightx = 0;
        panelNorte.add(btnVerDetalles, gbc);

        add(panelNorte, BorderLayout.NORTH);

        areaDetalles = new JTextArea();
        areaDetalles.setEditable(false);
        areaDetalles.setFont(new Font("Monospaced", Font.PLAIN, 12));
        add(new JScrollPane(areaDetalles), BorderLayout.CENTER);

        // ----- LISTENERS -----
        comboUsuarios.addActionListener(e -> onUsuarioSeleccionado());
        btnVerDetalles.addActionListener(e -> mostrarDetallesRegistro());
    }


    private void cargarUsuarios() {
        ControladorUsuario cu = ControladorUsuario.getInstance();
        Set<String> usrs = cu.listarAsistentes();

        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        for (String usr : usrs) {
            model.addElement(usr);
        }

        comboUsuarios.setModel(model);
        //comboUsuarios.setSelectedIndex(0); // selecciona el primero
    }


    private void onUsuarioSeleccionado() {
        Object sel = comboUsuarios.getSelectedItem();
        boolean valido = sel != null && !"— Seleccione —".equals(sel.toString());

        if (!valido) {
            comboRegistros.setModel(new DefaultComboBoxModel<>());
            comboRegistros.setEnabled(false);
            areaDetalles.setText("");
            return;
        }

        String usuario = sel.toString();
        cargarRegistrosDeUsuario(usuario);
        comboRegistros.setEnabled(true);
    }

    private void cargarRegistrosDeUsuario(String asistente) {
        ControladorRegistro cr = ControladorRegistro.getInstance();
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        Set<String> nomstiporeg = cr.obtenerNomsTipoRegistro(asistente);

        for (String nomtiporeg : nomstiporeg) {
            model.addElement(nomtiporeg);
        }

        comboRegistros.setModel(model);
        //comboRegistros.setSelectedIndex(0);
    }


    private void mostrarDetallesRegistro() {
        Object selUsuario = comboUsuarios.getSelectedItem();
        Object selReg = comboRegistros.getSelectedItem();

        if (selUsuario == null || "— Seleccione —".equals(selUsuario.toString())) {
            JOptionPane.showMessageDialog(this, "Primero seleccione un usuario.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (selReg == null || "— Seleccione —".equals(selReg.toString())) {
            JOptionPane.showMessageDialog(this, "Seleccione un registro.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String usuario = selUsuario.toString();
        String registro = selReg.toString();

        ControladorRegistro contrR = ControladorRegistro.getInstance();
        DTRegistro dtr = contrR.getRegistro(usuario, registro);

        if (dtr == null) {
            JOptionPane.showMessageDialog(this, "No se encontró el registro seleccionado.", 
                                          "Aviso", JOptionPane.WARNING_MESSAGE);
            areaDetalles.setText("");
            return;
        }

        // Formatear fecha
        DTFecha fecha = dtr.getFechaRegistro();
        String fechaStr = (fecha != null) 
                ? String.format("%02d/%02d/%04d", fecha.getDia(), fecha.getMes(), fecha.getAnio())
                : "—";

        StringBuilder sb = new StringBuilder();
        sb.append("=== Detalles del Registro ===\n");
        sb.append("Asistente: ").append(dtr.getAsistente()).append("\n");
        sb.append("Tipo de registro: ").append(dtr.getTipoDeRegistro()).append("\n");
        sb.append("Fecha de registro: ").append(fechaStr).append("\n");
        sb.append("Costo: $ ").append(
            String.format(java.util.Locale.US, "%.2f", dtr.getCosto())
        ).append("\n");
        
  

        areaDetalles.setText(sb.toString());
        areaDetalles.setCaretPosition(0);
    }




















}