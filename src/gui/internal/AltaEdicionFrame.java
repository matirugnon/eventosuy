package gui.internal;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@SuppressWarnings("serial")
public class AltaEdicionFrame extends JInternalFrame {
    private JComboBox<String> comboEventos;
    private JComboBox<String> comboOrganizadores;
    private JTextField txtNombre, txtSigla, txtCiudad, txtPais;
    private JFormattedTextField txtFechaInicio, txtFechaFin, txtFechaAlta;

    public AltaEdicionFrame() {
        super("Alta de Edición de Evento", true, true, true, true);
        setSize(600, 450);
        setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridLayout(0, 2, 10, 10));
        form.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Combos
        comboEventos = new JComboBox<>(new String[]{"ConfUdelar", "ExpoTech"});
        comboOrganizadores = new JComboBox<>(new String[]{"mariaOrg", "carlosOrg"});

        txtNombre = new JTextField();
        txtSigla = new JTextField();
        txtCiudad = new JTextField();
        txtPais = new JTextField();

        // Fechas con máscara __/__/____
        try {
            javax.swing.text.MaskFormatter mf = new javax.swing.text.MaskFormatter("##/##/####");
            mf.setPlaceholderCharacter('_');
            txtFechaInicio = new JFormattedTextField(mf);
            txtFechaFin = new JFormattedTextField(mf);
            txtFechaAlta = new JFormattedTextField(mf);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // Agregar al formulario
        form.add(new JLabel("Evento:"));
        form.add(comboEventos);

        form.add(new JLabel("Organizador:"));
        form.add(comboOrganizadores);

        form.add(new JLabel("Nombre Edición:"));
        form.add(txtNombre);

        form.add(new JLabel("Sigla:"));
        form.add(txtSigla);

        form.add(new JLabel("Ciudad:"));
        form.add(txtCiudad);

        form.add(new JLabel("País:"));
        form.add(txtPais);

        form.add(new JLabel("Fecha Inicio:"));
        form.add(txtFechaInicio);

        form.add(new JLabel("Fecha Fin:"));
        form.add(txtFechaFin);

        form.add(new JLabel("Fecha Alta:"));
        form.add(txtFechaAlta);

        add(form, BorderLayout.CENTER);

        // Botones abajo
        JPanel buttons = new JPanel();
        JButton btnConfirmar = new JButton("Confirmar");
        JButton btnCancelar = new JButton("Cancelar");
        buttons.add(btnConfirmar);
        buttons.add(btnCancelar);
        add(buttons, BorderLayout.SOUTH);

        // Acción confirmar
        btnConfirmar.addActionListener(e -> confirmarAlta());

        // Acción cancelar
        btnCancelar.addActionListener(e -> dispose());
    }

    private void confirmarAlta() {
        String nombre = txtNombre.getText().trim();
        String sigla = txtSigla.getText().trim();

        if (nombre.isEmpty() || sigla.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Debe completar al menos nombre y sigla.",
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Validación mock: si el nombre ya existe
        if ("Conf2025".equalsIgnoreCase(nombre)) {
            int opt = JOptionPane.showConfirmDialog(this,
                    "El nombre de la edición ya está en uso.\n¿Desea modificarlo?",
                    "Nombre duplicado", JOptionPane.YES_NO_OPTION);
            if (opt == JOptionPane.NO_OPTION) {
                dispose(); // cancelar caso de uso
            }
            return;
        }

        // Validar y parsear fechas
        LocalDate inicio = null, fin = null, alta = null;
        try {
            inicio = parseDate(txtFechaInicio.getText());
            fin = parseDate(txtFechaFin.getText());
            alta = parseDate(txtFechaAlta.getText());
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this,
                    "Error en fecha: " + e.getMessage() + "\n\nFormato esperado: dd/MM/yyyy",
                    "Fecha inválida", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validar lógica de fechas (opcional)
        if (inicio.isAfter(fin)) {
            JOptionPane.showMessageDialog(this,
                    "La fecha de inicio no puede ser posterior a la de fin.",
                    "Fechas inconsistentes", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Aquí iría la llamada al controlador para dar de alta
        JOptionPane.showMessageDialog(this,
                "Edición creada:\n" +
                        "Evento: " + comboEventos.getSelectedItem() + "\n" +
                        "Organizador: " + comboOrganizadores.getSelectedItem() + "\n" +
                        "Nombre: " + nombre + "\n" +
                        "Sigla: " + sigla + "\n" +
                        "Ciudad: " + txtCiudad.getText().trim() + "\n" +
                        "País: " + txtPais.getText().trim() + "\n" +
                        "Inicio: " + inicio + "\n" +
                        "Fin: " + fin + "\n" +
                        "Alta: " + alta,
                "Éxito", JOptionPane.INFORMATION_MESSAGE);

        dispose(); // cerrar frame después de éxito
    }

    private LocalDate parseDate(String text) throws DateTimeParseException {
        // Verificar si el texto está incompleto (contiene '_')
        if (text == null || text.isEmpty() || text.contains("_")) {
            throw new DateTimeParseException("Fecha incompleta o inválida", text, 0);
        }

        String[] p = text.split("/");
        if (p.length != 3) {
            throw new DateTimeParseException("Formato incorrecto. Use dd/MM/yyyy", text, 0);
        }

        try {
            int d = Integer.parseInt(p[0].trim());
            int m = Integer.parseInt(p[1].trim());
            int y = Integer.parseInt(p[2].trim());

            // Validar día y mes (básico)
            if (d < 1 || d > 31) {
                throw new DateTimeParseException("Día inválido: " + d, text, 0);
            }
            if (m < 1 || m > 12) {
                throw new DateTimeParseException("Mes inválido: " + m, text, 0);
            }

            // Dejar que LocalDate valide combinaciones como 30 de febrero
            return LocalDate.of(y, m, d);
        } catch (NumberFormatException e) {
            throw new DateTimeParseException("Valores de fecha no numéricos", text, 0);
        }
    }
}
