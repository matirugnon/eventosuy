package gui.internal;

import logica.Controladores.ControladorEvento;
import logica.DatatypesYEnum.DTEdicion;
import logica.DatatypesYEnum.DTEvento;
import logica.manejadores.ManejadorEventos;
import logica.Evento;
import logica.Edicion;

import javax.swing.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.Set;

@SuppressWarnings("serial")
public class ConsultaEdicionFrame extends JInternalFrame {

    private JComboBox<String> comboEventos;
    private JComboBox<String> comboEdiciones;

    private JComboBox<String> comboTipoDeRegistros;
    private JComboBox<String> comboPatrocinios;


    private JTextArea areaDetalles;

    // Formato de fecha
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

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





        // Panel inferior: acciones
        JPanel panelAcciones = new JPanel(new FlowLayout());
        JButton btnVerTipoRegistro = new JButton("Ver Tipo de Registro");
        JButton btnVerPatrocinio = new JButton("Ver Patrocinio");
        comboTipoDeRegistros = new JComboBox<>();
        comboPatrocinios = new JComboBox<>();





        panelAcciones.add(comboTipoDeRegistros);
        panelAcciones.add(btnVerTipoRegistro);

        panelAcciones.add(comboPatrocinios);
        panelAcciones.add(btnVerPatrocinio);
        add(panelAcciones, BorderLayout.SOUTH);

        // --- LISTENERS ---

        // Cambiar evento → actualizar combo de ediciones
        comboEventos.addActionListener(e -> onEventoSeleccionado());

        // Botón: ver detalles de la edición
        btnVerDetalles.addActionListener(e -> mostrarDetallesEdicion());

        // Ejemplos de funcionalidad futura
        btnVerTipoRegistro.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,
                "Funcionalidad de consulta de tipo de registro\n" +
                "Se abrirá un nuevo caso de uso más adelante.",
                "En desarrollo", JOptionPane.INFORMATION_MESSAGE);
        });

        btnVerPatrocinio.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,
                "Funcionalidad de consulta de patrocinio\n" +
                "Se abrirá un nuevo caso de uso más adelante.",
                "En desarrollo", JOptionPane.INFORMATION_MESSAGE);
        });
    }

    private void cargarEventos() {
        ControladorEvento ctrl = ControladorEvento.getInstance();
        Set<String> eventos = ctrl.listarEventos();

        comboEventos.removeAllItems();
        for (String evento : eventos) {
            comboEventos.addItem(evento);
        }

        // Si hay eventos, seleccionar el primero y cargar sus ediciones
        if (comboEventos.getItemCount() > 0) {
            comboEventos.setSelectedIndex(0);
            onEventoSeleccionado();
        }
    }

    private void onEventoSeleccionado() {
        String eventoS = (String) comboEventos.getSelectedItem();

        ManejadorEventos mEventos = ManejadorEventos.getInstance();

        Evento evento = mEventos.obtenerEvento(eventoS);

        comboEdiciones.removeAllItems();
        comboEdiciones.setEnabled(false);

        if (evento != null) {
            Set<String> ediciones = evento.getEdiciones();
            if (ediciones != null && !ediciones.isEmpty()) {
                for (String edicion : ediciones) {
                    comboEdiciones.addItem(edicion);
                }
                comboEdiciones.setEnabled(true);
            }
        }
    }

    private void mostrarDetallesEdicion() {
        String edicionS = (String) comboEdiciones.getSelectedItem();

        if (edicionS == "") {
            JOptionPane.showMessageDialog(this,
                "Debe seleccionar una edición.",
                "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String evento = (String) comboEventos.getSelectedItem();


        ControladorEvento cont = ControladorEvento.getInstance();

        DTEdicion dte = cont.consultarEdicion(edicionS);

        // Obtener datos de la edición
        String nombreEdicion = dte.getNombre();
        String sigla = dte.getSigla();
        String ciudad = dte.getCiudad();
        String pais = dte.getPais();
        String fechaInicio = dte.getFechaInicio().toString();
        String fechaFin = dte.getFechaFin().toString();
        String organizador = dte.getOrganizador();


        int totalRegistros = dte.getTiposDeRegistro().size();

        //falta poner que liste tipos de registro registros


        //-------------------ACA--------------------------

        areaDetalles.setText(
            "=== DETALLES DE LA EDICIÓN ===\n\n" +
            "Edición: " + nombreEdicion + "\n" +
            "Sigla: " + sigla + "\n" +
            "Evento: " + evento + "\n" +
            "Organizador: " + organizador + "\n" +
            "Ciudad: " + ciudad + "\n" +
            "País: " + pais + "\n" +
            "Fecha Inicio: " + fechaInicio + "\n" +
            "Fecha Fin: " + fechaFin + "\n" +
            "Registros: " + totalRegistros + " registrados" + "\n\n"
        );
    }
}