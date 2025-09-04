package gui.internal;

import javax.swing.*;

import logica.Controladores.IControladorRegistro;
import logica.Controladores.IControladorUsuario;
import logica.DatatypesYEnum.DTFecha;
import logica.DatatypesYEnum.DTRegistro;

import java.awt.*;
import java.util.Set;

public class ConsultaRegistroFrame extends JInternalFrame {


	private static final long serialVersionUID = 1L;
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
        // ----- NORTE: selección de usuario y registro -----
        JPanel panelNorte = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Usuario
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        panelNorte.add(new JLabel("Usuario:"), gbc);

        comboUsuarios = new JComboBox<>();
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1;
        panelNorte.add(comboUsuarios, gbc);

        // Registro
        gbc.gridx = 2; gbc.gridy = 0; gbc.weightx = 0;
        panelNorte.add(new JLabel("Registro:"), gbc);

        comboRegistros = new JComboBox<>();
        comboRegistros.setEnabled(false);
        gbc.gridx = 3; gbc.gridy = 0; gbc.weightx = 1;
        panelNorte.add(comboRegistros, gbc);

        // No hay botón "Ver Detalles"
        add(panelNorte, BorderLayout.NORTH);

        // ----- CENTRO: área de detalles -----
        areaDetalles = new JTextArea();
        areaDetalles.setEditable(false);
        areaDetalles.setFont(new Font("Monospaced", Font.PLAIN, 12));
        areaDetalles.setLineWrap(true);
        areaDetalles.setWrapStyleWord(true);
        add(new JScrollPane(areaDetalles), BorderLayout.CENTER);

        // ----- LISTENERS -----
        comboUsuarios.addActionListener(e -> {
            onUsuarioSeleccionado();
        });

        comboRegistros.addActionListener(e -> {
            String usuario = (String) comboUsuarios.getSelectedItem();
            String registro = (String) comboRegistros.getSelectedItem();

            if (usuario != null && registro != null) {
                mostrarDetallesRegistro();
            } else {
                areaDetalles.setText("");
            }
        });
    }

    private void cargarUsuarios() {
        IControladorUsuario cu = IControladorUsuario.getInstance();
        Set<String> usrs = cu.listarAsistentes();

        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        for (String usr : usrs) {
            model.addElement(usr);
        }

        comboUsuarios.setModel(model);

        // Inicializar con el primer usuario si existe
        if (model.getSize() > 0) {
            comboUsuarios.setSelectedIndex(0);
        } else {
            areaDetalles.setText("No hay asistentes registrados.");
        }
    }

    private void onUsuarioSeleccionado() {
        Object sel = comboUsuarios.getSelectedItem();
        if (sel == null) {
            comboRegistros.setModel(new DefaultComboBoxModel<>());
            comboRegistros.setEnabled(false);
            areaDetalles.setText("");
            return;
        }

        String usuario = sel.toString();
        cargarRegistrosDeUsuario(usuario);

        if (comboRegistros.getItemCount() > 0) {
            comboRegistros.setEnabled(true);
            comboRegistros.setSelectedIndex(0); // Esto activará el evento y mostrará los detalles
        } else {
            comboRegistros.setEnabled(false);
            areaDetalles.setText("Este usuario no tiene registros.");
        }
    }

    private void cargarRegistrosDeUsuario(String asistente) {
        IControladorRegistro cr = IControladorRegistro.getInstance();
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        Set<String> nomstiporeg = cr.obtenerNomsTipoRegistro(asistente);

        if (nomstiporeg.isEmpty()) {
            model.addElement("(Sin registros)");
        } else {
            for (String nomtiporeg : nomstiporeg) {
                model.addElement(nomtiporeg);
            }
        }

        comboRegistros.setModel(model);
    }

    private void mostrarDetallesRegistro() {
        String usuario = (String) comboUsuarios.getSelectedItem();
        String registro = (String) comboRegistros.getSelectedItem();

        // Evitar procesar si está en estado inválido
        if (usuario == null || registro == null || registro.equals("(Sin registros)")) {
            areaDetalles.setText("");
            return;
        }

        IControladorRegistro contrR = IControladorRegistro.getInstance();
        DTRegistro dtr = contrR.getRegistro(usuario, registro);

        if (dtr == null) {
            areaDetalles.setText("No se encontró el registro seleccionado.");
            return;
        }

        // Formatear fecha
        DTFecha fecha = dtr.getFechaRegistro();
        String fechaStr = (fecha != null)
                ? String.format("%02d/%02d/%04d", fecha.getDia(), fecha.getMes(), fecha.getAnio())
                : "—";

        StringBuilder sb = new StringBuilder();
        sb.append("=== Detalles del Registro ===\n\n");
        sb.append("Asistente: ").append(dtr.getAsistente()).append("\n");
        sb.append("Edición del Evento: ").append(dtr.getnomEdicion()).append("\n");
        sb.append("Tipo de Registro: ").append(dtr.getTipoDeRegistro()).append("\n");
        sb.append("Fecha de Registro: ").append(fechaStr).append("\n");
        sb.append("Costo: $ ").append(
            String.format(java.util.Locale.US, "%.2f", dtr.getCosto())
        ).append("\n");

        areaDetalles.setText(sb.toString());
        areaDetalles.setCaretPosition(0); // Scroll al inicio
    }
}