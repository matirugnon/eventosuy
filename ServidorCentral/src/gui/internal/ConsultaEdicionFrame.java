package gui.internal;

import logica.controladores.IControladorEvento;
import logica.controladores.IControladorRegistro;
import logica.datatypesyenum.DTEdicion;
import logica.datatypesyenum.DTPatrocinio;
import logica.datatypesyenum.DTTipoDeRegistro;
import logica.datatypesyenum.EstadoEdicion;

import javax.swing.*;
import excepciones.*;
import java.awt.*;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("serial")
public class ConsultaEdicionFrame extends JInternalFrame {

    private JComboBox<String> comboEventos;
    private JComboBox<String> comboEdiciones;
    private JComboBox<String> comboTipoDeRegistros;
    private JComboBox<String> comboPatrocinios;
    private JTextArea areaDetalles;

    private IControladorEvento ctrlEvento = IControladorEvento.getInstance();
    private IControladorRegistro ctrlRegistro = IControladorRegistro.getInstance();

    public ConsultaEdicionFrame() {
        super("Consulta de Edición", true, true, true, true);
        setSize(750, 450);
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

        // Fila 1: Evento
        gbc.gridx = 0; gbc.gridy = 0;
        panelSeleccion.add(new JLabel("Seleccione Evento:"), gbc);
        comboEventos = new JComboBox<>();
        gbc.gridx = 1;
        panelSeleccion.add(comboEventos, gbc);

        // Fila 2: Edición
        gbc.gridx = 0; gbc.gridy = 1;
        panelSeleccion.add(new JLabel("Seleccione Edición:"), gbc);
        comboEdiciones = new JComboBox<>();
        comboEdiciones.setEnabled(false);
        gbc.gridx = 1;
        panelSeleccion.add(comboEdiciones, gbc);

        add(panelSeleccion, BorderLayout.NORTH);

        // Área de detalles
        areaDetalles = new JTextArea();
        areaDetalles.setEditable(false);
        areaDetalles.setFont(new Font("Monospaced", Font.PLAIN, 12));
        areaDetalles.setLineWrap(true);
        areaDetalles.setWrapStyleWord(true);
        add(new JScrollPane(areaDetalles), BorderLayout.CENTER);

        // Panel inferior: combos y botones
        JPanel panelAcciones = new JPanel(new FlowLayout());

        comboTipoDeRegistros = new JComboBox<>();
        JButton btnVerTipoRegistro = new JButton("Ver Tipo de Registro");
        comboPatrocinios = new JComboBox<>();
        JButton btnVerPatrocinio = new JButton("Ver Patrocinio");

        panelAcciones.add(new JLabel("Tipo de Registro:"));
        panelAcciones.add(comboTipoDeRegistros);
        panelAcciones.add(btnVerTipoRegistro);

        panelAcciones.add(new JLabel("Patrocinio:"));
        panelAcciones.add(comboPatrocinios);
        panelAcciones.add(btnVerPatrocinio);

        add(panelAcciones, BorderLayout.SOUTH);

        // --- LISTENERS ---

        // Al cambiar de evento → actualizar combo de ediciones
        comboEventos.addActionListener(e -> {
            try {
                onEventoSeleccionado();
            } catch (EventoNoExisteException ex) {
                JOptionPane.showMessageDialog(this,
                    "Error al cargar ediciones del evento: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Al cambiar de edición → actualizar todo automáticamente
        comboEdiciones.addActionListener(e -> {
            String edicion = (String) comboEdiciones.getSelectedItem();
            if (edicion != null && !edicion.isEmpty()) {
                mostrarDetallesEdicion();
            } else {
                areaDetalles.setText("");
                comboTipoDeRegistros.removeAllItems();
                comboPatrocinios.removeAllItems();
            }
        });

        // Botón: Ver Tipo de Registro
        btnVerTipoRegistro.addActionListener(e -> {
            String tr = (String) comboTipoDeRegistros.getSelectedItem();
            if (tr == null) {
                JOptionPane.showMessageDialog(this,
                    "Debe seleccionar un Tipo de Registro.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String edicion = (String) comboEdiciones.getSelectedItem();
            try {
                DTTipoDeRegistro dt = ctrlRegistro.consultaTipoDeRegistro(edicion, tr);
                if (dt != null) {
                    JOptionPane.showMessageDialog(this,
                        "<html><b>Nombre:</b> " + dt.getNombre() +
                        "<br><b>Descripción:</b> " + dt.getDescripcion() +
                        "<br><b>Costo:</b> $" + String.format("%.2f", dt.getCosto()) +
                        "<br><b>Cupo:</b> " + (dt.getCupo() == -1 ? "Ilimitado" : dt.getCupo()) + "</html>",
                        "Detalles de Tipo de Registro", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                    "No se pudo cargar el tipo de registro: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Botón: Ver Patrocinio
        btnVerPatrocinio.addActionListener(e -> {
            String p = (String) comboPatrocinios.getSelectedItem();
            if (p == null) {
                JOptionPane.showMessageDialog(this,
                    "Debe seleccionar un Patrocinio.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String edicion = (String) comboEdiciones.getSelectedItem();
            try {
                DTPatrocinio dt = ctrlEvento.consultarTipoPatrocinioEdicion(edicion, p);
                if (dt != null) {
                    JOptionPane.showMessageDialog(this,
                        "<html><b>Código:</b> " + dt.getCodigo() +
                        "<br><b>Monto:</b> $" + String.format("%.2f", dt.getMonto()) +
                        "<br><b>Nivel:</b> " + dt.getNivel() +
                        "<br><b>Edición:</b> " + edicion +
                        "<br><b>Institución:</b> " + dt.getInstitucion() +
                        "<br><b>Fecha Alta:</b> " + dt.getFechaAlta() +
                        "<br><b>Tipo de Registro:</b> " + dt.getTipoDeRegistro() +
                        "<br><b>Accesos Gratuitos:</b> " + dt.getCantidadGratis() + "</html>",
                        "Detalle de Patrocinio", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                    "No se pudo cargar el patrocinio: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void cargarEventos() {
        Set<String> eventos = ctrlEvento.listarEventos();
        comboEventos.removeAllItems();
        for (String evento : eventos) {
            comboEventos.addItem(evento);
        }

        if (comboEventos.getItemCount() > 0) {
            comboEventos.setSelectedIndex(0);
            try {
                onEventoSeleccionado();
            } catch (EventoNoExisteException e) {
                e.printStackTrace();
            }
        } else {
            limpiarCampos();
        }
    }

    private void onEventoSeleccionado() throws EventoNoExisteException {
        String evento = (String) comboEventos.getSelectedItem();
        Set<String> ediciones = ctrlEvento.listarEdiciones(evento);

        comboEdiciones.removeAllItems();
        comboEdiciones.setEnabled(false);
        limpiarCampos();

        if (ediciones != null && !ediciones.isEmpty()) {
            for (String edicion : ediciones) {
                comboEdiciones.addItem(edicion);
            }
            comboEdiciones.setEnabled(true);
            // Si hay ediciones, seleccionar la primera (esto activará el evento)
            comboEdiciones.setSelectedIndex(0);
        }
    }

    private void mostrarDetallesEdicion() {
        String edicionS = (String) comboEdiciones.getSelectedItem();
        if (edicionS == null || edicionS.isEmpty()) {
            limpiarCampos();
            return;
        }

        // Cargar combos inferiores
        try {
            cargarTipoRegistro();
        } catch (EdicionNoExisteException e) {
            JOptionPane.showMessageDialog(this,
                "No se pudieron cargar los tipos de registro: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
            comboTipoDeRegistros.removeAllItems();
        }
        cargarPatrocinio();

        // Obtener datos de la edición
        DTEdicion dte;
        try {
            dte = ctrlEvento.consultarEdicion(edicionS);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "No se pudo cargar la edición: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
            limpiarCampos();
            return;
        }

        if (dte == null) {
            areaDetalles.setText("No se encontraron datos para esta edición.");
            return;
        }

        // Datos generales
        StringBuilder detalles = new StringBuilder();
        detalles.append("=== DETALLES DE LA EDICIÓN ===\n\n");
        detalles.append("Edición: ").append(dte.getNombre()).append("\n");
        detalles.append("Sigla: ").append(dte.getSigla()).append("\n");
        detalles.append("Evento: ").append(comboEventos.getSelectedItem()).append("\n");
        detalles.append("Organizador: ").append(dte.getOrganizador()).append("\n");
        detalles.append("Estado: ").append(formatearEstado(dte.getEstado())).append("\n");
        detalles.append("Ciudad: ").append(dte.getCiudad()).append("\n");
        detalles.append("País: ").append(dte.getPais()).append("\n");
        detalles.append("Fecha Inicio: ").append(dte.getFechaInicio()).append("\n");
        detalles.append("Fecha Fin: ").append(dte.getFechaFin()).append("\n");
        

        
        // Tipos de registro
        Set<String> tiposDeRegistro = dte.getTiposDeRegistro();
        if (tiposDeRegistro.isEmpty()) {
            detalles.append("Tipos de Registro: (Ninguno)\n");
        } else {
            detalles.append("Tipos de Registro: ").append(String.join(", ", tiposDeRegistro)).append("\n");
        }

        // Patrocinios
        Set<String> patrocinios = dte.getPatrocinios();
        if (patrocinios.isEmpty()) {
            detalles.append("Patrocinios: (Ninguno)\n");
        } else {
            detalles.append("Patrocinios: ").append(String.join(", ", patrocinios)).append("\n");
        }

        // Registros
        Set<Map.Entry<String, String>> registros = dte.getRegistros();
        int totalRegistros = registros.size();
        detalles.append("Registros: ").append(totalRegistros).append(" asistentes\n\n");

        if (totalRegistros == 0) {
            detalles.append("No hay registros en esta edición.\n");
        } else {
            detalles.append("=== REGISTROS ===\n");
            registros.stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> {
                    detalles.append("Asistente: ")
                            .append(entry.getKey())
                            .append(" → Tipo: ")
                            .append(entry.getValue())
                            .append("\n");
                });
        }

        areaDetalles.setText(detalles.toString());
    }

    private void cargarTipoRegistro() throws EdicionNoExisteException {
        comboTipoDeRegistros.removeAllItems();
        String edicion = (String) comboEdiciones.getSelectedItem();
        Set<String> tipos = ctrlRegistro.listarTipoRegistro(edicion);
        for (String tipo : tipos) {
            comboTipoDeRegistros.addItem(tipo);
        }
    }

    private void cargarPatrocinio() {
        comboPatrocinios.removeAllItems();
        String edicion = (String) comboEdiciones.getSelectedItem();
        try {
            Set<String> patrocinios = ctrlEvento.listarPatrocinios(edicion);
            for (String p : patrocinios) {
                comboPatrocinios.addItem(p);
            }
        } catch (Exception e) {
            // Puede fallar si no existe la edición, pero ya debería estar validado
            e.printStackTrace();
        }
    }

    private void limpiarCampos() {
        areaDetalles.setText("");
        comboTipoDeRegistros.removeAllItems();
        comboPatrocinios.removeAllItems();
    }
    
    private String formatearEstado(EstadoEdicion estado) {
        if (estado == null) return "";
        String texto = estado.toString().toLowerCase(); 
        return texto.substring(0, 1).toUpperCase() + texto.substring(1); 
    }
}