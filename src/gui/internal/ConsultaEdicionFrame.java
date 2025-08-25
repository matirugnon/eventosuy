package gui.internal;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@SuppressWarnings("serial")
public class ConsultaEdicionFrame extends JInternalFrame {

    private JComboBox<String> comboEventos;
    private JComboBox<String> comboEdiciones;
    private JTextArea areaDetalles;

    // Mock de datos (simulando una base de datos)
    private final String[] eventos = {"JUGConf", "DevDay", "TechUy"};
    private final String[][] edicionesPorEvento = {
        {"JUGConf 2024", "JUGConf 2025"},
        {"DevDay 2025", "DevDay 2023"},
        {"TechUy 2025"}
    };
    private final String[] tiposRegistro = {"General", "Estudiante", "Speaker"};
    private final String[] patrocinios = {"Empresa A", "Empresa B", "Startup X"};

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
        comboEventos = new JComboBox<>(eventos);
        gbc.gridx = 1;
        panelSeleccion.add(comboEventos, gbc);

        // Fila 2: Edición
        gbc.gridx = 0; gbc.gridy = 1;
        panelSeleccion.add(new JLabel("Seleccione Edición:"), gbc);
        comboEdiciones = new JComboBox<>();
        comboEdiciones.setEnabled(false); // Deshabilitado hasta que se elija evento
        gbc.gridx = 1;
        panelSeleccion.add(comboEdiciones, gbc);

        // Botón para cargar detalles
        JButton btnVerDetalles = new JButton("Ver Detalles");
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panelSeleccion.add(btnVerDetalles, gbc);

        add(panelSeleccion, BorderLayout.NORTH);

        // Área de detalles
        areaDetalles = new JTextArea();
        areaDetalles.setEditable(false);
        areaDetalles.setFont(new Font("Monospaced", Font.PLAIN, 12));
        add(new JScrollPane(areaDetalles), BorderLayout.CENTER);

        // Panel inferior: selección de tipo de registro o patrocinio
        JPanel panelAcciones = new JPanel(new FlowLayout());
        JComboBox<String> comboTipoRegistro = new JComboBox<>(tiposRegistro);
        JButton btnVerTipoRegistro = new JButton("Ver Tipo de Registro");
        JComboBox<String> comboPatrocinio = new JComboBox<>(patrocinios);
        JButton btnVerPatrocinio = new JButton("Ver Patrocinio");

        panelAcciones.add(new JLabel("Tipo de Registro:"));
        panelAcciones.add(comboTipoRegistro);
        panelAcciones.add(btnVerTipoRegistro);
        panelAcciones.add(Box.createHorizontalStrut(15));
        panelAcciones.add(new JLabel("Patrocinio:"));
        panelAcciones.add(comboPatrocinio);
        panelAcciones.add(btnVerPatrocinio);

        add(panelAcciones, BorderLayout.SOUTH);

        // --- LISTENERS ---

        // Cambiar evento → actualizar ediciones
        comboEventos.addActionListener(e -> onEventoSeleccionado());

        // Botón: ver detalles de la edición
        btnVerDetalles.addActionListener(e -> mostrarDetallesEdicion());

        // Botón: ver tipo de registro
        btnVerTipoRegistro.addActionListener(e -> {
            String tipo = (String) comboTipoRegistro.getSelectedItem();
            JOptionPane.showMessageDialog(this,
                "Caso de uso: Consulta de Tipo de Registro\n\n" +
                "Tipo seleccionado: " + tipo + "\n" +
                "Costo: $150\n" +
                "Beneficios: Acceso a todas las charlas, coffee break",
                "Tipo de Registro", JOptionPane.INFORMATION_MESSAGE);
        });

        // Botón: ver patrocinio
        btnVerPatrocinio.addActionListener(e -> {
            String patrocinio = (String) comboPatrocinio.getSelectedItem();
            JOptionPane.showMessageDialog(this,
                "Caso de uso: Consulta de Patrocinio\n\n" +
                "Empresa: " + patrocinio + "\n" +
                "Monto: $5000\n" +
                "Beneficios: Logo en banners, stand, mención en redes",
                "Patrocinio", JOptionPane.INFORMATION_MESSAGE);
        });
    }

    private void cargarEventos() {
        for (String evento : eventos) {
            comboEventos.addItem(evento);
        }
    }

    private void onEventoSeleccionado() {
        String eventoSeleccionado = (String) comboEventos.getSelectedItem();
        comboEdiciones.removeAllItems();
        comboEdiciones.setEnabled(false);

        for (int i = 0; i < eventos.length; i++) {
            if (eventos[i].equals(eventoSeleccionado)) {
                for (String edicion : edicionesPorEvento[i]) {
                    comboEdiciones.addItem(edicion);
                }
                comboEdiciones.setEnabled(true);
                break;
            }
        }
    }

    private void mostrarDetallesEdicion() {
        String edicion = (String) comboEdiciones.getSelectedItem();
        if (edicion == null) {
            JOptionPane.showMessageDialog(this,
                "Debe seleccionar una edición.",
                "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Mock de datos detallados
        String organizador = "Ana López";
        String ciudad = "Montevideo";
        String pais = "Uruguay";
        String fechaInicio = "01/09/2025";
        String fechaFin = "03/09/2025";
        String registros = "520 registrados (General: 400, Estudiante: 100, Speaker: 20)";
        String patrociniosInfo = "3 patrocinios activos: Empresa A, Empresa B, Startup X";

        areaDetalles.setText(
            "=== DETALLES DE LA EDICIÓN ===\n\n" +
            "Edición: " + edicion + "\n" +
            "Evento: " + comboEventos.getSelectedItem() + "\n" +
            "Organizador: " + organizador + "\n" +
            "Ciudad: " + ciudad + "\n" +
            "País: " + pais + "\n" +
            "Fecha Inicio: " + fechaInicio + "\n" +
            "Fecha Fin: " + fechaFin + "\n" +
            "Registros: " + registros + "\n" +
            "Patrocinios: " + patrociniosInfo + "\n\n" +
            "Observaciones: Edición confirmada, todos los servicios contratados."
        );
    }
}
