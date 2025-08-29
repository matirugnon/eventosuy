package gui.internal;

import logica.Controladores.ControladorEvento;
import logica.Evento;
import logica.Usuario;

import logica.DatatypesYEnum.DTFecha;

import javax.swing.*;
import java.awt.*;
import java.time.DateTimeException;

@SuppressWarnings("serial")
public class AltaEdicionFrame extends JInternalFrame {

    private JComboBox<Evento> comboEventos;
    private JComboBox<Usuario> comboOrganizadores;
    private JTextField txtNombre, txtSigla, txtCiudad, txtPais;
    private JFormattedTextField txtFechaInicio, txtFechaFin, txtFechaAlta;

    public AltaEdicionFrame() {
        super("Alta de Edición de Evento", true, true, true, true);
        setSize(600, 450);
        setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridLayout(0, 2, 10, 10));
        form.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // --- Combos dinámicos ---
        comboEventos = new JComboBox<>();
        comboOrganizadores = new JComboBox<>();

        cargarEventos();
        cargarOrganizadores();

        // --- Campos ---
        txtNombre = new JTextField();
        txtSigla = new JTextField();
        txtCiudad = new JTextField();
        txtPais = new JTextField();

        // --- Fechas con máscara dd/MM/yyyy ---
        try {
            javax.swing.text.MaskFormatter mf = new javax.swing.text.MaskFormatter("##/##/####");
            mf.setPlaceholderCharacter('_');
            txtFechaInicio = new JFormattedTextField(mf);
            txtFechaFin = new JFormattedTextField(mf);
            txtFechaAlta = new JFormattedTextField(mf);
        } catch (Exception e) {
            throw new RuntimeException("Error al crear máscara de fecha", e);
        }

        // --- Añadir al formulario ---
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

        // --- Botones ---
        JPanel buttons = new JPanel();
        JButton btnConfirmar = new JButton("Confirmar");
        JButton btnCancelar = new JButton("Cancelar");
        buttons.add(btnConfirmar);
        buttons.add(btnCancelar);
        add(buttons, BorderLayout.SOUTH);

        // --- LISTENERS ---
        btnConfirmar.addActionListener(e -> confirmarAlta());
        btnCancelar.addActionListener(e -> dispose());
    }

    private void cargarEventos() {
        ControladorEvento ctrlEvento = ControladorEvento.getInstance();
        var eventos = ctrlEvento.listarEventos();

        comboEventos.removeAllItems();
        for (Evento evento : eventos) {
            comboEventos.addItem(evento);
        }

        if (comboEventos.getItemCount() == 0) {
            JOptionPane.showMessageDialog(this,
                "No hay eventos disponibles para crear una edición.",
                "Advertencia", JOptionPane.WARNING_MESSAGE);
            dispose();
        }
    }

    private void cargarOrganizadores() {
        ControladorEvento ctrlEvento = ControladorEvento.getInstance();
        var organizadores = ctrlEvento.listarOrganizadores();

        comboOrganizadores.removeAllItems();
        for (Usuario org : organizadores) {
            comboOrganizadores.addItem(org);
        }

        if (comboOrganizadores.getItemCount() == 0) {
            JOptionPane.showMessageDialog(this,
                "No hay organizadores disponibles.",
                "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void confirmarAlta() {
        Evento evento = (Evento) comboEventos.getSelectedItem();
        Usuario organizador = (Usuario) comboOrganizadores.getSelectedItem();

        String nombre = txtNombre.getText().trim();
        String sigla = txtSigla.getText().trim();
        String ciudad = txtCiudad.getText().trim();
        String pais = txtPais.getText().trim();

        // Validación básica
        if (nombre.isEmpty() || sigla.isEmpty() || ciudad.isEmpty() || pais.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Debe completar todos los campos.",
                "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (evento == null || organizador == null) {
            JOptionPane.showMessageDialog(this,
                "Debe seleccionar un evento y un organizador.",
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validar nombre duplicado
        if (existeEdicion(nombre)) {
            int opt = JOptionPane.showConfirmDialog(this,
                "Ya existe una edición con ese nombre.\n¿Desea continuar igual?",
                "Nombre duplicado", JOptionPane.YES_NO_OPTION);
            if (opt == JOptionPane.NO_OPTION) return;
        }

        // Parsear fechas a DTFecha
        DTFecha inicio = null, fin = null, alta = null;
        try {
            inicio = parseDTFecha(txtFechaInicio.getText());
            fin = parseDTFecha(txtFechaFin.getText());
            alta = parseDTFecha(txtFechaAlta.getText());
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this,
                "Fecha inválida: " + e.getMessage() + "\nFormato: dd/MM/yyyy",
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validaciones de fechas
        if (inicio.compareTo(fin) > 0) {
            JOptionPane.showMessageDialog(this,
                "La fecha de inicio no puede ser posterior a la de fin.",
                "Fechas inválidas", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (alta.compareTo(inicio) > 0) {
            JOptionPane.showMessageDialog(this,
                "La fecha de alta no puede ser posterior a la fecha de inicio.",
                "Fechas inválidas", JOptionPane.WARNING_MESSAGE);
            return;
        }



        ControladorEvento ctrlEvento = ControladorEvento.getInstance();
        try {
            ctrlEvento.AltaEdicion(
            	evento.getNombre(),
                organizador.getNickname(),
                nombre, sigla, ciudad, pais,
                inicio, fin, alta
            );

            JOptionPane.showMessageDialog(this,
                "Edición '" + nombre + "' creada con éxito.",
                "Éxito", JOptionPane.INFORMATION_MESSAGE);
            dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Error al dar de alta la edición:\n" + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean existeEdicion(String nombre) {
        ControladorEvento ctrl = ControladorEvento.getInstance();
        return ctrl.existeEdicion(nombre);
    }

    // Convierte String "dd/MM/yyyy" a DTFecha
    private DTFecha parseDTFecha(String text) {
        if (text == null || text.trim().isEmpty() || text.contains("_")) {
            throw new IllegalArgumentException("Fecha incompleta o vacía");
        }

        String[] partes = text.split("/");
        if (partes.length != 3) {
            throw new IllegalArgumentException("Formato incorrecto. Use dd/MM/yyyy");
        }

        try {
            int dia = Integer.parseInt(partes[0].trim());
            int mes = Integer.parseInt(partes[1].trim());
            int anio = Integer.parseInt(partes[2].trim());

            // Validación básica
            if (dia < 1 || dia > 31) throw new IllegalArgumentException("Día inválido: " + dia);
            if (mes < 1 || mes > 12) throw new IllegalArgumentException("Mes inválido: " + mes);
            if (anio < 1900 || anio > 9999) throw new IllegalArgumentException("Año inválido: " + anio);

            return new DTFecha(dia, mes, anio);

        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Valores no numéricos en la fecha");
        }
    }
}