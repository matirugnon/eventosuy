	package gui.internal;

import logica.Edicion;
import logica.Evento;
import logica.Controladores.ControladorEvento;
import logica.Controladores.ControladorRegistro;
import logica.DatatypesYEnum.DTEdicion;
import logica.DatatypesYEnum.DTEvento;
import logica.DatatypesYEnum.DTTipoDeRegistro;
import logica.DatatypesYEnum.DTPatrocinio;
import logica.manejadores.ManejadorEventos;

import javax.swing.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.Map;
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
        cargarTipoRegistro();
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
        	String tr = (String) comboTipoDeRegistros.getSelectedItem();
        	if (tr == null) {
        		JOptionPane.showMessageDialog(this,
                        "Debe seleccionar un Tipo de Registro \n",
                        "Error", JOptionPane.INFORMATION_MESSAGE);
        	}
        	else {
        		String edicion = (String) comboEdiciones.getSelectedItem();
        		ControladorRegistro ctrlRegistros = ControladorRegistro.getInstance();
        		DTTipoDeRegistro dt = ctrlRegistros.consultaTipoDeRegistro(edicion, tr);

        		JOptionPane.showMessageDialog(this,
                        "Nombre: " + dt.getNombre() + "\n" +
                        "Descripcion: " + dt.getDescripcion() + "\n" +
                        "Costo: " + dt.getCosto() + "\n" +
                        "Cupo: " + dt.getCupo() + "\n",
                        "Detalles de " + dt.getNombre(), JOptionPane.INFORMATION_MESSAGE);

        	}


        });

        btnVerPatrocinio.addActionListener(e -> {
        	String p = (String) comboPatrocinios.getSelectedItem();
        	if (p == null) {
        		JOptionPane.showMessageDialog(this,
                        "Debe seleccionar un Patrocinio \n",
                        "Error", JOptionPane.INFORMATION_MESSAGE);
        	}
        	else {
        		String edicion = (String) comboEdiciones.getSelectedItem();
        		ControladorEvento ctrlEventos = ControladorEvento.getInstance();
        		DTPatrocinio dt = ctrlEventos.consultarTipoPatrocinioEdicion(edicion, p);

        		JOptionPane.showMessageDialog(this,
                        "Codigo: " + dt.getCodigo() + "\n" +
                        "Monto: " + dt.getMonto() + "\n" +
                        "Nivel: " + dt.getNivel() + "\n" +
                        "Edicion: " + edicion + "\n" +
                       // "Institucion: " + dt.get + "\n" +
                        "Fecha Alta: " + dt.getFechaAlta() + "\n",
                        "Detalles del Patrocinio:  " + dt.getCodigo(), JOptionPane.INFORMATION_MESSAGE);

        	}
        });
    }

    private void cargarTipoRegistro() {
    	ControladorRegistro ctrlRegistros = ControladorRegistro.getInstance();
    	comboTipoDeRegistros.removeAllItems();


    	String edicion = (String) comboEdiciones.getSelectedItem();
    	Set<String> tiposDeRegistro = ctrlRegistros.listarTipoRegistro(edicion);
    	for (String ed : tiposDeRegistro) {
    		comboTipoDeRegistros.addItem(ed);
    	}
    }

    private void cargarPatrocinio() {

    	String edicion = (String) comboEdiciones.getSelectedItem();
    	ManejadorEventos mEventos = ManejadorEventos.getInstance();
    	Edicion ed = mEventos.obtenerEdicion(edicion);
    	Set<String> patrocinios = ed.getNombresPatrocinios();
    	for (String p : patrocinios) {
    		comboPatrocinios.addItem(p);
    	}

    }

    private void cargarEventos() {
        ControladorEvento ctrlEventos = ControladorEvento.getInstance();
        Set<String> eventos = ctrlEventos.listarEventos();

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

        if (edicionS == null || edicionS.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Debe seleccionar una edición.",
                "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String evento = (String) comboEventos.getSelectedItem();
        ControladorEvento cont = ControladorEvento.getInstance();

        DTEdicion dte = cont.consultarEdicion(edicionS);

        if (dte == null) {
            JOptionPane.showMessageDialog(this,
                "No se encontraron datos para la edición seleccionada.",
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Obtener datos de la edición
        String nombreEdicion = dte.getNombre();
        String sigla = dte.getSigla();
        String ciudad = dte.getCiudad();
        String pais = dte.getPais();
        String fechaInicio = dte.getFechaInicio().toString();
        String fechaFin = dte.getFechaFin().toString();
        String organizador = dte.getOrganizador();
        Set<String> tiposDeRegistro = dte.getTiposDeRegistro();
        Set<Map.Entry<String, String>> registros = dte.getRegistros(); // ✅ Aquí están los registros

        // Contar registros
        int totalRegistros = registros.size();

        // Construir detalles
        StringBuilder detalles = new StringBuilder();
        detalles.append("=== DETALLES DE LA EDICIÓN ===\n\n");
        detalles.append("Edición: ").append(nombreEdicion).append("\n");
        detalles.append("Sigla: ").append(sigla).append("\n");
        detalles.append("Evento: ").append(evento).append("\n");
        detalles.append("Organizador: ").append(organizador).append("\n");
        detalles.append("Ciudad: ").append(ciudad).append("\n");
        detalles.append("País: ").append(pais).append("\n");
        detalles.append("Fecha Inicio: ").append(fechaInicio).append("\n");
        detalles.append("Fecha Fin: ").append(fechaFin).append("\n");
        detalles.append("Tipos de Registro: ").append(String.join(", ", tiposDeRegistro)).append("\n");
        detalles.append("Registros: ").append(totalRegistros).append(" registrados\n\n");

        // === Listar registros ===
        if (totalRegistros == 0) {
            detalles.append("No hay registros en esta edición.\n");
        } else {
            detalles.append("=== REGISTROS ===\n");
            registros.stream()
                .sorted(Map.Entry.comparingByKey()) // Ordenar por nombre de asistente
                .forEach(entry -> {
                    detalles.append("Asistente: ")
                            .append(entry.getKey())
                            .append(" → Tipo: ")
                            .append(entry.getValue())
                            .append("\n");
                });
        }

        // Mostrar en el área de texto
        areaDetalles.setText(detalles.toString());
    }
}