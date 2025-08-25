package gui.internal;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;

@SuppressWarnings("serial")
public class ConsultaUsuarioFrame extends JInternalFrame {
    private final Consumer<JInternalFrame> openInternal; // para abrir detalle
    private JComboBox<String> comboUsuarios;
    private JTextArea areaDatos;
    private JTable tablaDetalle;
    private JButton btnVerDetalle;
    private JScrollPane scrollTabla;

    // Constructor recomendado: le pasás un "opener" (MainFrame::openInternal)
    public ConsultaUsuarioFrame(Consumer<JInternalFrame> openInternal) {
        super("Consulta de Usuario", true, true, true, true);
        this.openInternal = openInternal;
        ui();
    }

    // Constructor de respaldo (si no pasás opener)
    public ConsultaUsuarioFrame() {
        this(frame -> JOptionPane.showMessageDialog(null,
                "Sin opener: aquí se abriría el detalle como JInternalFrame."));
    }

    private void ui() {
        setSize(720, 520);
        setLayout(new BorderLayout());

        // TOP: selección
        JPanel top = new JPanel();
        top.add(new JLabel("Usuario:"));
        comboUsuarios = new JComboBox<>(new String[]{
                "juan123 (Asistente)", "mariaOrg (Organizador)"
        });
        top.add(comboUsuarios);
        JButton btnConsultar = new JButton("Consultar");
        top.add(btnConsultar);
        add(top, BorderLayout.NORTH);

        // CENTER: datos + tabla
        JPanel center = new JPanel(new BorderLayout(10, 10));

        areaDatos = new JTextArea();
        areaDatos.setEditable(false);
        JScrollPane spDatos = new JScrollPane(areaDatos);
        spDatos.setBorder(BorderFactory.createTitledBorder("Datos del Usuario"));
        spDatos.setPreferredSize(new Dimension(400, 180));
        center.add(spDatos, BorderLayout.NORTH);

        tablaDetalle = new JTable();
        tablaDetalle.setFillsViewportHeight(true);
        tablaDetalle.getTableHeader().setReorderingAllowed(false);


        JScrollPane spTabla = new JScrollPane(tablaDetalle);
        spTabla.setBorder(BorderFactory.createTitledBorder("Detalle"));
        center.add(spTabla, BorderLayout.CENTER);

        this.scrollTabla = spTabla;

        add(center, BorderLayout.CENTER);

        // SOUTH: botón abrir detalle (opcional al doble clic)
        JPanel south = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnVerDetalle = new JButton("Ver Detalle");
        south.add(btnVerDetalle);
        add(south, BorderLayout.SOUTH);

        // Listeners
        btnConsultar.addActionListener(e -> cargarDatosYDetalle());
        btnVerDetalle.addActionListener(e -> abrirDetalleSeleccion());
        tablaDetalle.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && tablaDetalle.getSelectedRow() != -1) {
                    abrirDetalleSeleccion();
                }
            }
        });
    }

    private void cargarDatosYDetalle() {
        String sel = (String) comboUsuarios.getSelectedItem();
        if (sel == null) return;

        // MOCK: completamos datos según tipo
        if (sel.contains("Asistente")) {
            areaDatos.setText(
                    "Nickname: juan123\n" +
                    "Nombre: Juan Pérez\n" +
                    "Correo: juan@mail.com\n" +
                    "Tipo: Asistente\n" +
                    "Fecha Nac: 1999-05-21\n"
            );

            // Tabla: REGISTROS del asistente
            String[] cols = {"Registro ID", "Evento", "Edición", "Tipo", "Costo"};
            Object[][] data = {
                    {"R-101", "Jornadas2025", "J2025-Montevideo", "Estudiante", 500},
                    {"R-205", "Hackathon",    "Hack2025",        "General",   1000}
            };
            tablaDetalle.setModel(new DefaultTableModel(data, cols));

            scrollTabla.setBorder(BorderFactory.createTitledBorder("Registros del Asistente"));

            // Guardamos un flag en la tabla para saber qué detalle abrir
            tablaDetalle.putClientProperty("detalleTipo", "REGISTRO");

        } else { // Organizador
            areaDatos.setText(
                    "Nickname: mariaOrg\n" +
                    "Nombre: María Gómez\n" +
                    "Correo: maria@mail.com\n" +
                    "Tipo: Organizador\n" +
                    "Descripción: Org. de conferencias\n"
            );

            // Tabla: EDICIONES del organizador
            String[] cols = {"Edición", "Sigla", "Evento", "Ciudad", "País"};
            Object[][] data = {
                    {"Conf2025", "C25", "ConfUdelar", "Montevideo",     "Uruguay"},
                    {"Expo2025", "E25", "ExpoTech",   "Buenos Aires",   "Argentina"}
            };
            tablaDetalle.setModel(new DefaultTableModel(data, cols));

            scrollTabla.setBorder(BorderFactory.createTitledBorder("Ediciones del Organizador"));

            tablaDetalle.putClientProperty("detalleTipo", "EDICION");
        }

        tablaDetalle.revalidate();
        tablaDetalle.repaint();
    }

    private void abrirDetalleSeleccion() {
        int row = tablaDetalle.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una fila primero.", "Atención",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        String tipo = (String) tablaDetalle.getClientProperty("detalleTipo");
        if ("REGISTRO".equals(tipo)) {
            // Datos mínimos mock para abrir detalle de registro
            String regId  = String.valueOf(tablaDetalle.getValueAt(row, 0));
            String evento = String.valueOf(tablaDetalle.getValueAt(row, 1));
            String edicion= String.valueOf(tablaDetalle.getValueAt(row, 2));
            openInternal.accept(new ConsultaRegistroFrame(regId, evento, edicion));
        } else if ("EDICION".equals(tipo)) {
            // Datos mínimos mock para abrir detalle de edición
            String nombreEd = String.valueOf(tablaDetalle.getValueAt(row, 0));
            String sigla    = String.valueOf(tablaDetalle.getValueAt(row, 1));
            String evento   = String.valueOf(tablaDetalle.getValueAt(row, 2));
            openInternal.accept(new ConsultaEdicionFrame());
        } else {
            JOptionPane.showMessageDialog(this, "No se reconoce el tipo de detalle.", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
