package gui.internal;
             // Ajusta según tu modelo

import javax.swing.*;
import javax.swing.table.DefaultTableModel;


import logica.Controladores.IControladorEvento;
import logica.DatatypesYEnum.DTSeleccionEvento;

import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

@SuppressWarnings("serial")
public class ConsultaEventoFrame extends JInternalFrame {
    private JComboBox<String> comboEventos; // Cambiado a Evento, no String
    private JTextArea areaDatos;
    private JList<String> listaCategorias;
    private JTable tablaEdiciones;
    private JButton btnVerEdicion;
    IControladorEvento contrEvento = IControladorEvento.getInstance();
    public ConsultaEventoFrame() {
        super("Consulta de Evento", true, true, true, true);
        setSize(650, 500);
        setLayout(new BorderLayout());




        // TOP: selección de evento
        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Seleccione Evento:"));
        comboEventos = new JComboBox<>();

        Set<String> eventos = contrEvento.listarEventos();
        for (String evento : eventos) {
            comboEventos.addItem(evento);
        }

        JButton btnConsultar = new JButton("Consultar");
        topPanel.add(comboEventos);
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
        comboEventos.addActionListener(e -> {
            // Opcional: actualizar automáticamente al cambiar selección
            // mostrarDatosEvento();
        });
        btnVerEdicion.addActionListener(e -> verEdicion());
    }

    private void mostrarDatosEvento() {
        String eventoS = (String) comboEventos.getSelectedItem();
        DTSeleccionEvento evento = contrEvento.seleccionarEvento(eventoS);


        // Formato de fecha
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Datos del evento
        areaDatos.setText(
            "Nombre: " + evento.getNombre() + "\n" +
            "Sigla: " + evento.getSigla() + "\n" +
            "Descripción: " + evento.getDescripcion() + "\n" +
            "Fecha Alta: " + evento.getFechaEvento().toString()
        );

        // Categorías
        listaCategorias.setListData(evento.getCategorias().toArray(new String[0]));

        Set<String> cats = evento.getCategorias();
        System.out.println("Categorías del evento: " + cats); // 👈 Depuración

        if (cats == null || cats.isEmpty()) {
            listaCategorias.setListData(new String[]{"(Sin categorías)"});
        } else {
            listaCategorias.setListData(cats.toArray(new String[0]));
        }

        // Ediciones
        Set<String> ediciones = evento.getEdiciones(); //esto cambio


        if (ediciones == null || ediciones.isEmpty()) {
            tablaEdiciones.setModel(new DefaultTableModel(
                new Object[][]{{"(Sin ediciones)"}},
                new String[]{"Nombre"}
            ));
        }else {

        	Object[][] datos = ediciones.stream()
        	        .map(nombre -> new Object[]{nombre})
        	        .toArray(Object[][]::new);

        	    String[] columnas = {"Nombre"};


        	    DefaultTableModel modelo = new DefaultTableModel(datos, columnas) {
        	        @Override
        	        public boolean isCellEditable(int row, int column) {
        	            return false; // ❌ No se puede editar ninguna celda
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

        // Obtener el DTEdicion
        logica.DatatypesYEnum.DTEdicion dtEdicion = contrEvento.consultarEdicion(nombreEdicion);
        if (dtEdicion == null) {
            JOptionPane.showMessageDialog(this,
                "No se pudo cargar la información de la edición: " + nombreEdicion,
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Formato de los detalles
        String detalles =
            "Nombre: " + dtEdicion.getNombre() + "\n" +
            "Sigla: " + dtEdicion.getSigla() + "\n" +
            "Fecha Inicio: " + dtEdicion.getFechaInicio() + "\n" +
            "Fecha Fin: " + dtEdicion.getFechaFin() + "\n" +
            "Alta Edición: " + dtEdicion.getAltaEdicion() + "\n" +
            "Ciudad: " + dtEdicion.getCiudad() + "\n" +
            "País: " + dtEdicion.getPais() + "\n" +
            "Organizador: " + dtEdicion.getOrganizador() + "\n" +
            "Patrocinios: " + String.join(", ", dtEdicion.getPatrocinios()) + "\n" +
            "Tipos de Registro: " + String.join(", ", dtEdicion.getTiposDeRegistro());

        // Mostrar en ventana emergente
        JOptionPane.showMessageDialog(
            this,
            detalles,
            "Detalle de Edición: " + dtEdicion.getNombre(),
            JOptionPane.INFORMATION_MESSAGE
        );
    }
}