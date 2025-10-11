package gui.internal;

import logica.Controladores.IControladorEvento;
import logica.Controladores.ControladorEvento;
import logica.Controladores.ControladorUsuario;
import logica.DatatypesYEnum.DTFecha;

import javax.swing.*;
import java.awt.*;
import java.util.Set;

@SuppressWarnings("serial")
public class AltaEdicionFrame extends JInternalFrame {

    // Campos nuevos: Cupo y Costo (validados por separado)
    private JTextField txtCupo, txtCosto;

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

        // --- Combos dinámicos ---
        comboEventos = new JComboBox<>();
        comboOrganizadores = new JComboBox<>();

        cargarEventos();
        cargarOrganizadores();

        // --- Campos ---
        txtCupo = new JTextField();
        txtCosto = new JTextField();
        txtCupo.setToolTipText("Ingrese un entero \u2265 0 (sin comas ni puntos).");
        txtCosto.setToolTipText("Ingrese un número \u2265 0 (coma o punto como separador decimal).");

        // (Opcional) podrías agregar DocumentFilters para restringir input,
        // pero lo dejamos solo con validación para no agregar complejidad.

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

        form.add(new JLabel("Cupo:"));
        form.add(txtCupo);

        form.add(new JLabel("Costo:"));
        form.add(txtCosto);

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
        IControladorEvento ctrlEvento = ControladorEvento.getInstance();
        var eventos = ctrlEvento.listarEventos();

        comboEventos.removeAllItems();
        for (String evento : eventos) {
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
        ControladorUsuario ctrlU = ControladorUsuario.getInstance();
        Set<String> organizadores = ctrlU.listarOrganizadores();

        comboOrganizadores.removeAllItems();
        for (String org : organizadores) {
            comboOrganizadores.addItem(org);
        }

        if (comboOrganizadores.getItemCount() == 0) {
            JOptionPane.showMessageDialog(this,
                "No hay organizadores disponibles.",
                "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void confirmarAlta() {
        String evento = (String) comboEventos.getSelectedItem();
        String organizador = (String) comboOrganizadores.getSelectedItem();

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

        if (evento == null || evento.isEmpty() || organizador == null || organizador.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Debe seleccionar un evento y un organizador.",
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validar nombre duplicado
        if (existeEdicion(nombre)) {
            JOptionPane.showMessageDialog(this,
                "Ya existe una edición con el nombre '" + nombre + "'.\n"
                + "Por favor, elija un nombre diferente.",
                "Nombre duplicado", JOptionPane.ERROR_MESSAGE);
            txtNombre.requestFocus();
            return;
        }

        // Parsear fechas a DTFecha
        DTFecha inicio, fin, alta;
        try {
            inicio = parseDTFecha(txtFechaInicio.getText());
            fin    = parseDTFecha(txtFechaFin.getText());
            alta   = parseDTFecha(txtFechaAlta.getText());
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

        // --- Validación separada de Cupo y Costo ---
        final String cupoStr  = txtCupo.getText().trim();
        final String costoStr = txtCosto.getText().trim();

        try {
            parseCupo(cupoStr);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Cupo inválido", JOptionPane.ERROR_MESSAGE);
            txtCupo.requestFocus();
            return;
        }

        try {
            parseCosto(costoStr);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Costo inválido", JOptionPane.ERROR_MESSAGE);
            txtCosto.requestFocus();
            return;
        }

        // NOTA: En esta versión (A) validamos cupo/costo pero NO los enviamos
        // al controlador porque la firma de AltaEdicion(...) no los incluye.
        // Si decidís incluirlos, avisame y te paso la variante (B).

        ControladorEvento ctrlEvento = ControladorEvento.getInstance();
        try {
            ctrlEvento.altaEdicion(
                evento,
                organizador,
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

    // ---- Helpers de validación ----
    private int parseCupo(String s) {
        if (s == null || s.isEmpty()) {
            throw new IllegalArgumentException("El cupo es obligatorio.");
        }
        // Solo enteros no negativos (sin puntos ni comas)
        if (!s.matches("\\d+")) {
            throw new IllegalArgumentException("El cupo debe ser un número entero (sin comas ni puntos).");
        }
        try {
            int v = Integer.parseInt(s);
            if (v < 0) throw new IllegalArgumentException("El cupo debe ser mayor o igual a 0.");
            return v;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("El cupo ingresado es demasiado grande.");
        }
    }

    private double parseCosto(String s) {
        if (s == null || s.isEmpty()) {
            throw new IllegalArgumentException("El costo es obligatorio.");
        }
        // Acepta coma o punto como separador decimal
        String norm = s.replace(" ", "").replace(",", ".");
        if (!norm.matches("\\d+(\\.\\d+)?")) {
            throw new IllegalArgumentException("El costo debe ser numérico. Ej.: 1200, 1200.50 o 1200,50.");
        }
        double v = Double.parseDouble(norm);
        if (v < 0) throw new IllegalArgumentException("El costo debe ser mayor o igual a 0.");
        return v;
    }
}
