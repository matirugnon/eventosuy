package gui.internal;

import logica.controladores.IControladorEvento;
import logica.datatypesyenum.EstadoEdicion;

import javax.swing.*;
import excepciones.EdicionNoExisteException;
import java.awt.*;
import java.util.Set;

@SuppressWarnings("serial")
public class AceptarRechazarEdicionFrame extends JInternalFrame {

    private JComboBox<String> comboEventos;
    private JComboBox<String> comboEdiciones;
    private JTextArea areaDetalles;
    private JButton btnAceptar;
    private JButton btnRechazar;

    private IControladorEvento ctrlEvento = IControladorEvento.getInstance();

    public AceptarRechazarEdicionFrame() {
        super("Aceptar/Rechazar Edición", true, true, true, true);
        setSize(600, 400);
        setLayout(new BorderLayout());
        configurarComponentes();
        cargarEventos();
        setVisible(true);
    }

    private void configurarComponentes() {
        // Panel superior: selección de evento y edición
        JPanel panelSeleccion = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Evento
        gbc.gridx = 0; gbc.gridy = 0;
        panelSeleccion.add(new JLabel("Seleccione Evento:"), gbc);
        comboEventos = new JComboBox<>();
        gbc.gridx = 1;
        panelSeleccion.add(comboEventos, gbc);

        // Edición
        gbc.gridx = 0; gbc.gridy = 1;
        panelSeleccion.add(new JLabel("Seleccione Edición:"), gbc);
        comboEdiciones = new JComboBox<>();
        comboEdiciones.setEnabled(false);
        gbc.gridx = 1;
        panelSeleccion.add(comboEdiciones, gbc);

        add(panelSeleccion, BorderLayout.NORTH);

        // Área central: detalles de la edición
        areaDetalles = new JTextArea();
        areaDetalles.setEditable(false);
        areaDetalles.setFont(new Font("Monospaced", Font.PLAIN, 12));
        areaDetalles.setLineWrap(true);
        areaDetalles.setWrapStyleWord(true);
        add(new JScrollPane(areaDetalles), BorderLayout.CENTER);

        // Panel inferior: botones
        JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        btnAceptar = new JButton("Aceptar");
        btnRechazar = new JButton("Rechazar");
        panelAcciones.add(btnAceptar);
        panelAcciones.add(btnRechazar);
        add(panelAcciones, BorderLayout.SOUTH);

        // Listeners
        comboEventos.addActionListener(e -> onEventoSeleccionado());
        comboEdiciones.addActionListener(e -> mostrarDetallesEdicion());
        btnAceptar.addActionListener(e -> cambiarEstado(EstadoEdicion.ACEPTADA));
        btnRechazar.addActionListener(e -> cambiarEstado(EstadoEdicion.RECHAZADA));
    }

    private void cargarEventos() {
        comboEventos.removeAllItems();
        Set<String> eventos = ctrlEvento.listarEventos();
        for (String ev : eventos) {
            comboEventos.addItem(ev);
        }
        if (comboEventos.getItemCount() > 0) {
            comboEventos.setSelectedIndex(0);
            onEventoSeleccionado();
        }
    }

    private void onEventoSeleccionado() {
        String evento = (String) comboEventos.getSelectedItem();
        comboEdiciones.removeAllItems();
        comboEdiciones.setEnabled(false);
        limpiarCampos();

        if (evento != null) {
            Set<String> ediciones = ctrlEvento.listarEdicionesPorEstadoDeEvento(evento, EstadoEdicion.INGRESADA);
            for (String ed : ediciones) {
                comboEdiciones.addItem(ed);
            }
            if (comboEdiciones.getItemCount() > 0) {
                comboEdiciones.setEnabled(true);
                comboEdiciones.setSelectedIndex(0);
                mostrarDetallesEdicion();
            }
        }
    }

    private void mostrarDetallesEdicion() {
        String edicion = (String) comboEdiciones.getSelectedItem();
        if (edicion == null) {
            limpiarCampos();
            return;
        }
        try {
            var dte = ctrlEvento.consultarEdicion(edicion);
            if (dte != null) {
                StringBuilder sb = new StringBuilder();
                sb.append("=== DETALLES DE LA EDICIÓN ===\n\n");
                sb.append("Nombre: ").append(dte.getNombre()).append("\n");
                sb.append("Sigla: ").append(dte.getSigla()).append("\n");
                sb.append("Organizador: ").append(dte.getOrganizador()).append("\n");
                sb.append("Estado: ").append(formatearEstado(dte.getEstado())).append("\n");
                sb.append("Ciudad: ").append(dte.getCiudad()).append("\n");
                sb.append("País: ").append(dte.getPais()).append("\n");
                sb.append("Fecha Inicio: ").append(dte.getFechaInicio()).append("\n");
                sb.append("Fecha Fin: ").append(dte.getFechaFin()).append("\n");
                
                areaDetalles.setText(sb.toString());
            }
        } catch (Exception ex) {
            areaDetalles.setText("Error al cargar detalles: " + ex.getMessage());
        }
    }

    private void cambiarEstado(EstadoEdicion nuevoEstado) {
        String edicion = (String) comboEdiciones.getSelectedItem();
        if (edicion == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar una edición.",
                    "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            ctrlEvento.actualizarEstadoEdicion(edicion, nuevoEstado);
            JOptionPane.showMessageDialog(this,
                    "La edición '" + edicion + "' fue " + formatearEstado(nuevoEstado),
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
            onEventoSeleccionado();
        } catch (EdicionNoExisteException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarCampos() {
        areaDetalles.setText("");
    }

    private String formatearEstado(EstadoEdicion estado) {
        if (estado == null) return "";
        String texto = estado.toString().toLowerCase(); 
        return texto.substring(0, 1).toUpperCase() + texto.substring(1); 
    }
}
