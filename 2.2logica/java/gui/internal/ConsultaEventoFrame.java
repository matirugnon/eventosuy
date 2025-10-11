package gui.internal;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import excepciones.EventoNoExisteException;
import logica.controladores.IControladorEvento;
import logica.datatypesyenum.DTSeleccionEvento;
import logica.datatypesyenum.EstadoEdicion;

import java.awt.*;
import java.util.Set;

@SuppressWarnings("serial")
public class ConsultaEventoFrame extends JInternalFrame {
    private JComboBox<String> comboEventos;
    private JTextArea areaDatos;
    private JList<String> listaCategorias;
    private JTable tablaEdiciones;
    private JButton btnVerEdicion;

    private IControladorEvento contrEvento = IControladorEvento.getInstance();

    private static final String ITEM_SELECCIONAR = "--- Seleccionar evento ---";

    public ConsultaEventoFrame() {
        super("Consulta de Evento", true, true, true, true);
        setSize(700, 500);
        setLayout(new BorderLayout());

        // TOP: selección de evento con placeholder
        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Evento:"));

        comboEventos = new JComboBox<>();
        comboEventos.addItem(ITEM_SELECCIONAR);

        Set<String> eventos = contrEvento.listarEventos();
        for (String evento : eventos) {
            comboEventos.addItem(evento);
        }

        if (eventos.isEmpty()) {
            comboEventos.setEnabled(false);
        }

        // Acción: solo cargar si no es el placeholder
        comboEventos.addActionListener(e -> {
            String seleccionado = (String) comboEventos.getSelectedItem();
            if (seleccionado != null && !seleccionado.equals(ITEM_SELECCIONAR)) {
                try {
                    mostrarDatosEvento();
                } catch (EventoNoExisteException ex) {
                    JOptionPane.showMessageDialog(this,
                        "Error al cargar el evento: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                limpiarCampos();
            }
        });

        topPanel.add(comboEventos);
        add(topPanel, BorderLayout.NORTH);

        // CENTER: datos + panel inferior con categorías y ediciones
        JPanel centerPanel = new JPanel(new BorderLayout());

        // Datos del evento (arriba)
        areaDatos = new JTextArea();
        areaDatos.setEditable(false);
        areaDatos.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        areaDatos.setBorder(BorderFactory.createTitledBorder("Datos del Evento"));
        centerPanel.add(new JScrollPane(areaDatos), BorderLayout.NORTH);

        // Panel inferior: categorías y ediciones con proporción ajustada
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

        // Categorías (izquierda - más ancho)
        listaCategorias = new JList<>();
        JScrollPane scrollCategorias = new JScrollPane(listaCategorias);
        scrollCategorias.setBorder(BorderFactory.createTitledBorder("Categorías"));
        splitPane.setLeftComponent(scrollCategorias);

        // Ediciones (derecha - un poco más angosta)
        tablaEdiciones = new JTable();
        tablaEdiciones.setFillsViewportHeight(true);
        JScrollPane scrollEdiciones = new JScrollPane(tablaEdiciones);
        scrollEdiciones.setBorder(BorderFactory.createTitledBorder("Ediciones"));
        splitPane.setRightComponent(scrollEdiciones);

        // Ajustar proporción: 42% para categorías, 58% para ediciones
        splitPane.setDividerLocation(0.32);
        splitPane.setResizeWeight(0.42); // permite redimensionar proporcionalmente

        centerPanel.add(splitPane, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);

        // BOTTOM: botón para ver edición
        JPanel bottomPanel = new JPanel();
        btnVerEdicion = new JButton("Consultar Edición");
        bottomPanel.add(btnVerEdicion);
        add(bottomPanel, BorderLayout.SOUTH);

        btnVerEdicion.addActionListener(e -> verEdicion());

        // Estado inicial
        limpiarCampos();
    }

    private void mostrarDatosEvento() throws EventoNoExisteException {
        String eventoS = (String) comboEventos.getSelectedItem();
        if (eventoS == null || eventoS.equals(ITEM_SELECCIONAR)) return;

        DTSeleccionEvento evento = contrEvento.seleccionarEvento(eventoS);

        areaDatos.setText(
            "Nombre: " + evento.getNombre() + "\n" +
            "Sigla: " + evento.getSigla() + "\n" +
            "Descripción: " + evento.getDescripcion() + "\n" +
            "Fecha Alta: " + evento.getFechaEvento()
        );

        // Categorías
        Set<String> cats = evento.getCategorias();
        if (cats == null || cats.isEmpty()) {
            listaCategorias.setListData(new String[]{"(Sin categorías)"});
        } else {
            listaCategorias.setListData(cats.toArray(new String[0]));
        }

        // Ediciones
        Set<String> ediciones = evento.getEdiciones();
        if (ediciones == null || ediciones.isEmpty()) {
            tablaEdiciones.setModel(new DefaultTableModel(
                new Object[][]{{"(Sin ediciones)"}},
                new String[]{"Nombre"}
            ));
        } else {
            Object[][] datos = ediciones.stream()
                .map(nombre -> new Object[]{nombre})
                .toArray(Object[][]::new);

            String[] columnas = {"Nombre"};
            DefaultTableModel modelo = new DefaultTableModel(datos, columnas) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            tablaEdiciones.setModel(modelo);
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
        logica.datatypesyenum.DTEdicion dtEdicion = contrEvento.consultarEdicion(nombreEdicion);

        if (dtEdicion == null) {
            JOptionPane.showMessageDialog(this,
                "No se pudo cargar la información de la edición: " + nombreEdicion,
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Construir contenido en HTML para que el JLabel lo renderice bien
        StringBuilder html = new StringBuilder("<html><body style='width: 400px; font-family: Arial, sans-serif; font-size: 12px;'>");

        html.append("<b>Nombre:</b> ").append(dtEdicion.getNombre()).append("<br>");
        html.append("<b>Sigla:</b> ").append(dtEdicion.getSigla()).append("<br>");
        html.append("<b>Fecha Inicio:</b> ").append(dtEdicion.getFechaInicio()).append("<br>");
        html.append("<b>Fecha Fin:</b> ").append(dtEdicion.getFechaFin()).append("<br>");
        html.append("<b>Alta Edición:</b> ").append(dtEdicion.getAltaEdicion()).append("<br>");
        html.append("<b>Ciudad:</b> ").append(dtEdicion.getCiudad()).append("<br>");
        html.append("<b>País:</b> ").append(dtEdicion.getPais()).append("<br>");
        html.append("<b>Organizador:</b> ").append(dtEdicion.getOrganizador()).append("<br>");
        html.append("<b>Estado:</b> ").append(formatearEstado(dtEdicion.getEstado())).append("<br>");
        
        // Patrocinios
        html.append("<b>Patrocinios:</b> ");
        if (dtEdicion.getPatrocinios().isEmpty()) {
            html.append("Ninguno").append("<br>");
        } else {
            html.append(String.join(", ", dtEdicion.getPatrocinios())).append("<br>");
        }

        // Tipos de Registro
        html.append("<b>Tipos de Registro:</b> ");
        if (dtEdicion.getTiposDeRegistro().isEmpty()) {
            html.append("Ninguno").append("<br>");
        } else {
            html.append(String.join(", ", dtEdicion.getTiposDeRegistro())).append("<br>");
        }

        html.append("</body></html>");

        // Usar JLabel con HTML: el JOptionPane se ajustará automáticamente
        JLabel label = new JLabel(html.toString());
        label.setFont(new Font("Arial", Font.PLAIN, 12));
        label.setVerticalAlignment(SwingConstants.TOP);
        label.setHorizontalAlignment(SwingConstants.LEFT);

        JOptionPane.showMessageDialog(
            this,
            label,
            "Detalle de Edición: " + dtEdicion.getNombre(),
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void limpiarCampos() {
        areaDatos.setText("");
        listaCategorias.setListData(new String[]{"(Seleccione un evento)"});
        tablaEdiciones.setModel(new DefaultTableModel(
            new Object[][]{{"(Seleccione un evento)"}},
            new String[]{"Nombre"}
        ));
    }
    
    private String formatearEstado(EstadoEdicion estado) {
        if (estado == null) return "";
        String texto = estado.toString().toLowerCase(); 
        return texto.substring(0, 1).toUpperCase() + texto.substring(1); 
    }
}