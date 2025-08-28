package gui.internal;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;


@SuppressWarnings("serial")
public class ConsultaTipoRegistroFrame extends JInternalFrame {

	 private JComboBox<String> comboEventos;
	    private JComboBox<String> comboEdiciones;
	    private JComboBox<String> comboTiposRegistro;
	    private JTextArea areaDatos;

	    public ConsultaTipoRegistroFrame() {
	        super("Consulta de Tipo de Registro", true, true, true, true);
	        setSize(600, 400);
	        setLayout(new BorderLayout(10, 10));

	        // --- Panel superior: combos de selección ---
	        JPanel top = new JPanel(new GridLayout(3, 2, 10, 10));
	        top.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

	        comboEventos = new JComboBox<>(new String[]{"ConfUdelar", "ExpoTech"});
	        comboEdiciones = new JComboBox<>();
	        comboTiposRegistro = new JComboBox<>();

	        top.add(new JLabel("Evento:"));
	        top.add(comboEventos);

	        top.add(new JLabel("Edición:"));
	        top.add(comboEdiciones);

	        top.add(new JLabel("Tipo de Registro:"));
	        top.add(comboTiposRegistro);

	        add(top, BorderLayout.NORTH);

	        // --- Panel central: datos ---
	        areaDatos = new JTextArea();
	        areaDatos.setEditable(false);
	        JScrollPane scroll = new JScrollPane(areaDatos);
	        scroll.setBorder(BorderFactory.createTitledBorder("Datos del Tipo de Registro"));
	        add(scroll, BorderLayout.CENTER);

	        // --- Listeners ---
	        comboEventos.addActionListener(e -> cargarEdiciones());
	        comboEdiciones.addActionListener(e -> cargarTiposRegistro());
	        comboTiposRegistro.addActionListener(e -> mostrarDatosTipoRegistro());

	        // Inicialización
	        cargarEdiciones();
	    }

	    private void cargarEdiciones() {
	        comboEdiciones.removeAllItems();
	        comboTiposRegistro.removeAllItems();
	        areaDatos.setText("");

	        String evento = (String) comboEventos.getSelectedItem();
	        if (evento == null) return;

	        if ("ConfUdelar".equals(evento)) {
	            comboEdiciones.addItem("Conf2025");
	            comboEdiciones.addItem("Conf2026");
	        } else if ("ExpoTech".equals(evento)) {
	            comboEdiciones.addItem("Expo2025");
	        }
	    }

	    private void cargarTiposRegistro() {
	        comboTiposRegistro.removeAllItems();
	        areaDatos.setText("");

	        String edicion = (String) comboEdiciones.getSelectedItem();
	        if (edicion == null) return;

	        if ("Conf2025".equals(edicion)) {
	            comboTiposRegistro.addItem("General");
	            comboTiposRegistro.addItem("Estudiante");
	        } else if ("Conf2026".equals(edicion)) {
	            comboTiposRegistro.addItem("EarlyBird");
	        } else if ("Expo2025".equals(edicion)) {
	            comboTiposRegistro.addItem("General");
	            comboTiposRegistro.addItem("Premium");
	        }
	    }

	    private void mostrarDatosTipoRegistro() {
	        areaDatos.setText("");
	        String tipo = (String) comboTiposRegistro.getSelectedItem();
	        if (tipo == null) return;

	        switch (tipo) {
	            case "General":
	                areaDatos.setText(
	                        "Nombre: General\n" +
	                        "Descripción: Acceso general al evento\n" +
	                        "Costo: 2000\n" +
	                        "Cupo: 500"
	                );
	                break;
	            case "Estudiante":
	                areaDatos.setText(
	                        "Nombre: Estudiante\n" +
	                        "Descripción: Acceso con descuento para estudiantes\n" +
	                        "Costo: 500\n" +
	                        "Cupo: 200"
	                );
	                break;
	            case "EarlyBird":
	                areaDatos.setText(
	                        "Nombre: EarlyBird\n" +
	                        "Descripción: Registro anticipado con descuento\n" +
	                        "Costo: 1500\n" +
	                        "Cupo: 100"
	                );
	                break;
	            case "Premium":
	                areaDatos.setText(
	                        "Nombre: Premium\n" +
	                        "Descripción: Acceso completo + beneficios exclusivos\n" +
	                        "Costo: 3000\n" +
	                        "Cupo: 50"
	                );
	                break;
	        }
	    }
	}

