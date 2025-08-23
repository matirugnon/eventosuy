package gui.internal;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.*;

public class ConsultaUsuarioFrame extends JInternalFrame {
    private JComboBox<String> comboUsuarios;
    private JTextArea areaDatos;
    private JTable tablaExtra;

    public ConsultaUsuarioFrame() {
        super("Consulta de Usuario", true, true, true, true);
        setSize(600, 400);
        setLayout(new BorderLayout());

        // TOP: selección de usuario
        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Seleccione Usuario:"));
        comboUsuarios = new JComboBox<>();
        // ⚠️ datos de prueba: luego conectar al controlador
        comboUsuarios.addItem("juan123 (Asistente)");
        comboUsuarios.addItem("mariaOrg (Organizador)");
        topPanel.add(comboUsuarios);
        JButton btnConsultar = new JButton("Consultar");
        topPanel.add(btnConsultar);
        add(topPanel, BorderLayout.NORTH);

        // CENTER: área de datos y tabla
        JPanel centerPanel = new JPanel(new BorderLayout());

        areaDatos = new JTextArea();
        areaDatos.setEditable(false);
        areaDatos.setBorder(BorderFactory.createTitledBorder("Datos del Usuario"));
        centerPanel.add(new JScrollPane(areaDatos), BorderLayout.CENTER);

        tablaExtra = new JTable();
        tablaExtra.setBorder(BorderFactory.createTitledBorder("Detalle extra"));
        centerPanel.add(new JScrollPane(tablaExtra), BorderLayout.SOUTH);

        add(centerPanel, BorderLayout.CENTER);

        // Accion botón Consultar
        btnConsultar.addActionListener(e -> mostrarDatosUsuario());
    }

    // ⚠️ Mock de datos de ejemplo
    private void mostrarDatosUsuario() {
        String seleccionado = (String) comboUsuarios.getSelectedItem();

        if (seleccionado.contains("Asistente")) {
            areaDatos.setText("Nickname: juan123\nNombre: Juan Pérez\nCorreo: juan@mail.com\nTipo: Asistente\nFecha Nac: 1999-05-21");
            // Mostrar registros en tabla
            String[] columnas = {"Evento", "Edición", "Tipo Registro", "Costo"};
            Object[][] datos = {
                {"Jornadas2025", "J2025-Montevideo", "Estudiante", 500},
                {"Hackathon", "Hack2025", "General", 1000}
            };
            tablaExtra.setModel(new DefaultTableModel(datos, columnas));
        } else {
            areaDatos.setText("Nickname: mariaOrg\nNombre: María Gómez\nCorreo: maria@mail.com\nTipo: Organizador\nDescripción: Org. de conferencias");
            // Mostrar ediciones en tabla
            String[] columnas = {"Evento", "Edición", "Ciudad", "País"};
            Object[][] datos = {
                {"ConfUdelar", "Conf2025", "Montevideo", "Uruguay"},
                {"ExpoTech", "Expo2025", "Buenos Aires", "Argentina"}
            };
            tablaExtra.setModel(new DefaultTableModel(datos, columnas));
        }
    }
}
