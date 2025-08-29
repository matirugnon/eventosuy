package gui.internal;

import javax.swing.*;

import logica.Controladores.ControladorRegistro;

import java.awt.*;

@SuppressWarnings("serial")
public class AltaTipoRegistroFrame extends JInternalFrame {
    private JComboBox<String> comboEventos;
    private JComboBox<String> comboEdiciones;
    private JTextField txtNombre, txtDescripcion, txtCosto, txtCupo;


    private ControladorRegistro contrR;

    public AltaTipoRegistroFrame() {
        super("Alta de Tipo de Registro", true, true, true, true);
        setSize(600, 400);
        setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridLayout(0, 2, 10, 10));
        form.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Combo de eventos
        comboEventos = new JComboBox<>(new String[]{"ConfUdelar", "ExpoTech"});
        form.add(new JLabel("Evento:"));
        form.add(comboEventos);

        // Combo de ediciones (se llena según el evento elegido)
        comboEdiciones = new JComboBox<>();
        actualizarEdiciones();
        form.add(new JLabel("Edición:"));
        form.add(comboEdiciones);

        // Campos de datos
        txtNombre = new JTextField();
        txtDescripcion = new JTextField();
        txtCosto = new JTextField();
        txtCupo = new JTextField();

        form.add(new JLabel("Nombre:"));
        form.add(txtNombre);

        form.add(new JLabel("Descripción:"));
        form.add(txtDescripcion);

        form.add(new JLabel("Costo:"));
        form.add(txtCosto);

        form.add(new JLabel("Cupo:"));
        form.add(txtCupo);

        add(form, BorderLayout.CENTER);

        // Botones
        JPanel buttons = new JPanel();
        JButton btnConfirmar = new JButton("Confirmar");
        JButton btnCancelar = new JButton("Cancelar");
        buttons.add(btnConfirmar);
        buttons.add(btnCancelar);
        add(buttons, BorderLayout.SOUTH);

        // Listeners
        comboEventos.addActionListener(e -> actualizarEdiciones());
        btnConfirmar.addActionListener(e -> confirmarAlta());
        btnCancelar.addActionListener(e -> dispose());
    }

    private void actualizarEdiciones() {
        comboEdiciones.removeAllItems();
        String evento = (String) comboEventos.getSelectedItem();

        if ("ConfUdelar".equals(evento)) {
            comboEdiciones.addItem("Conf2025");
            comboEdiciones.addItem("Conf2026");
        } else if ("ExpoTech".equals(evento)) {
            comboEdiciones.addItem("Expo2025");
        }
    }

    private void confirmarAlta() {

    	//instancia de controlador registro
    	contrR = ControladorRegistro.getInstance();


        String evento = (String) comboEventos.getSelectedItem();
        String edicion = (String) comboEdiciones.getSelectedItem();
        String nombre = txtNombre.getText().trim();
        String descripcion = txtDescripcion.getText().trim();
        String costoStr = txtCosto.getText().trim();
        String cupoStr = txtCupo.getText().trim();

        if (evento == null || edicion == null || nombre.isEmpty() || costoStr.isEmpty() || cupoStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe completar todos los campos obligatorios.",
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            double costo = Double.parseDouble(costoStr);
            int cupo = Integer.parseInt(cupoStr);

            //validacion nombre duplicado

            if (contrR.existeTipoDeRegistro(edicion, nombre)) {
                int opt = JOptionPane.showConfirmDialog(this,
                        "El nombre de tipo de registro ya existe en esta edición.\n¿Desea modificarlo?",
                        "Nombre duplicado", JOptionPane.YES_NO_OPTION);
                if (opt == JOptionPane.NO_OPTION) {
                    dispose(); // cancelar caso de uso
                }
                return;
            }

            //mostrar datos de alta
            JOptionPane.showMessageDialog(this,
                    "Tipo de registro creado:\n" +
                            "Evento: " + evento + "\n" +
                            "Edición: " + edicion + "\n" +
                            "Nombre: " + nombre + "\n" +
                            "Descripción: " + descripcion + "\n" +
                            "Costo: " + costo + "\n" +
                            "Cupo: " + cupo,
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);

            contrR.altaTipoDeRegistro(edicion, nombre, descripcion, costo, cupo);

            dispose();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Costo y cupo deben ser valores numéricos.",
                    "Error de formato", JOptionPane.ERROR_MESSAGE);
        }
    }
}
