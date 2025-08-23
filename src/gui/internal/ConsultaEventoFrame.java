package gui.internal;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

@SuppressWarnings("serial")
public class ConsultaEventoFrame extends JInternalFrame {
    private JComboBox<String> comboEventos;
    private JTextArea areaDatos;
    private JList<String> listaCategorias;
    private JTable tablaEdiciones;
    private JButton btnVerEdicion;

    public ConsultaEventoFrame() {
        super("Consulta de Evento", true, true, true, true);
        setSize(650, 500);
        setLayout(new BorderLayout());

        // TOP: selección de evento
        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Seleccione Evento:"));
        comboEventos = new JComboBox<>(new String[]{"ConfUdelar", "ExpoTech"});
        topPanel.add(comboEventos);

        JButton btnConsultar = new JButton("Consultar");
        topPanel.add(btnConsultar);

        add(topPanel, BorderLayout.NORTH);

        // CENTER: datos + categorías + ediciones
        JPanel centerPanel = new JPanel(new BorderLayout());

        // Datos del evento
        areaDatos = new JTextArea();
        areaDatos.setEditable(false);
        areaDatos.setBorder(BorderFactory.createTitledBorder("Datos del Evento"));
        centerPanel.add(new JScrollPane(areaDatos), BorderLayout.NORTH);

        // Categorías
        listaCategorias = new JList<>();
        JScrollPane scrollCategorias = new JScrollPane(listaCategorias);
        scrollCategorias.setBorder(BorderFactory.createTitledBorder("Categorías"));
        centerPanel.add(scrollCategorias, BorderLayout.WEST);

        // Tabla de ediciones
        tablaEdiciones = new JTable();
        JScrollPane scrollEdiciones = new JScrollPane(tablaEdiciones);
        scrollEdiciones.setBorder(BorderFactory.createTitledBorder("Ediciones"));
        centerPanel.add(scrollEdiciones, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);

        // BOTTOM: botón para abrir ediciones
        JPanel bottomPanel = new JPanel();
        btnVerEdicion = new JButton("Consultar Edición");
        bottomPanel.add(btnVerEdicion);
        add(bottomPanel, BorderLayout.SOUTH);

        // Eventos
        btnConsultar.addActionListener(e -> mostrarDatosEvento());
        btnVerEdicion.addActionListener(e -> verEdicion());
    }

    private void mostrarDatosEvento() {
        String evento = (String) comboEventos.getSelectedItem();

        if ("ConfUdelar".equals(evento)) {
            // Datos
            areaDatos.setText(
                "Nombre: ConfUdelar\n" +
                "Sigla: CONF\n" +
                "Descripción: Conferencia anual de la UdelaR\n" +
                "Fecha Alta: 2025-03-01"
            );

            // Categorías
            listaCategorias.setListData(new String[]{"Conferencia", "Académico"});

            // Ediciones
            String[] columnas = {"Nombre", "Sigla", "Ciudad", "País"};
            Object[][] datos = {
                {"Conf2025", "C25", "Montevideo", "Uruguay"},
                {"Conf2024", "C24", "Salto", "Uruguay"}
            };
            tablaEdiciones.setModel(new DefaultTableModel(datos, columnas));

        } else if ("ExpoTech".equals(evento)) {
            // Datos
            areaDatos.setText(
                "Nombre: ExpoTech\n" +
                "Sigla: EXPO\n" +
                "Descripción: Feria tecnológica internacional\n" +
                "Fecha Alta: 2025-04-15"
            );

            // Categorías
            listaCategorias.setListData(new String[]{"Feria", "Innovación"});

            // Ediciones
            String[] columnas = {"Nombre", "Sigla", "Ciudad", "País"};
            Object[][] datos = {
                {"Expo2025", "E25", "Buenos Aires", "Argentina"}
            };
            tablaEdiciones.setModel(new DefaultTableModel(datos, columnas));
        }
    }

    private void verEdicion() {
        int fila = tablaEdiciones.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this,
                "Debe seleccionar una edición de la tabla.",
                "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String nombreEdicion = (String) tablaEdiciones.getValueAt(fila, 0);
        JOptionPane.showMessageDialog(this,
            "Abrir detalles de la edición: " + nombreEdicion,
            "Consulta de Edición", JOptionPane.INFORMATION_MESSAGE);

        // Aquí más adelante: openInternal(new ConsultaEdicionFrame(nombreEdicion));
    }
}
