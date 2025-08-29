package gui.internal;

import javax.swing.*;
import java.awt.*;

public class RegistroAEdicionFrame extends JInternalFrame {
    private JComboBox<String> comboEventos;
    private JComboBox<String> comboEdiciones;
    private JComboBox<String> comboTiposRegistro;
    private JComboBox<String> comboAsistentes;

    public RegistroAEdicionFrame() {
        super("Registro a Edición de Evento", true, true, true, true);
        setSize(600, 400);
        setLayout(new BorderLayout(10, 10));

        JPanel form = new JPanel(new GridLayout(0, 2, 10, 10));
        form.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Combos
        comboEventos = new JComboBox<>(new String[]{"ConfUdelar", "ExpoTech"});
        comboEdiciones = new JComboBox<>();
        comboTiposRegistro = new JComboBox<>();
        comboAsistentes = new JComboBox<>(new String[]{"juan123", "ana456", "pedro789"});

        form.add(new JLabel("Evento:"));
        form.add(comboEventos);

        form.add(new JLabel("Edición:"));
        form.add(comboEdiciones);

        form.add(new JLabel("Tipo de Registro:"));
        form.add(comboTiposRegistro);

        form.add(new JLabel("Asistente:"));
        form.add(comboAsistentes);

        add(form, BorderLayout.CENTER);

        // Botones
        JPanel buttons = new JPanel();
        JButton btnConfirmar = new JButton("Registrar");
        JButton btnCancelar = new JButton("Cancelar");
        buttons.add(btnConfirmar);
        buttons.add(btnCancelar);
        add(buttons, BorderLayout.SOUTH);

        // Listeners
        comboEventos.addActionListener(e -> cargarEdiciones());
        comboEdiciones.addActionListener(e -> cargarTiposRegistro());
        btnConfirmar.addActionListener(e -> registrar());
        btnCancelar.addActionListener(e -> dispose());

        // Inicialización
        cargarEdiciones();
    }

    private void cargarEdiciones() {
        comboEdiciones.removeAllItems();
        comboTiposRegistro.removeAllItems();

        String evento = (String) comboEventos.getSelectedItem();
        if (evento == null) return;

        if ("ConfUdelar".equals(evento)) {
            comboEdiciones.addItem("Conf2025");
            comboEdiciones.addItem("Conf2026");
        } else if ("ExpoTech".equals(evento)) {
            comboEdiciones.addItem("Expo2025");
        }
    }

    private void cargarTiposRegistro() {
        comboTiposRegistro.removeAllItems();

        String edicion = (String) comboEdiciones.getSelectedItem();
        if (edicion == null) return;

        if ("Conf2025".equals(edicion)) {
            comboTiposRegistro.addItem("General");
            comboTiposRegistro.addItem("Estudiante");
        } else if ("Conf2026".equals(edicion)) {
            comboTiposRegistro.addItem("EarlyBird");
        } else if ("Expo2025".equals(edicion)) {
            comboTiposRegistro.addItem("General");
            comboTiposRegistro.addItem("Premium");
        }
    }

    private void registrar() {
        String evento = (String) comboEventos.getSelectedItem();
        String edicion = (String) comboEdiciones.getSelectedItem();
        String tipo = (String) comboTiposRegistro.getSelectedItem();
        String asistente = (String) comboAsistentes.getSelectedItem();

        if (evento == null || edicion == null || tipo == null || asistente == null) {
            JOptionPane.showMessageDialog(this,
                    "Debe seleccionar evento, edición, tipo de registro y asistente.",
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(this,
                "Registro creado:\n" +
                        "Evento: " + evento + "\n" +
                        "Edición: " + edicion + "\n" +
                        "Tipo: " + tipo + "\n" +
                        "Asistente: " + asistente,
                "Éxito", JOptionPane.INFORMATION_MESSAGE);

        dispose();
    }
}
