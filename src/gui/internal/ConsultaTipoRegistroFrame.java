package gui.internal;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import logica.Controladores.IControladorEvento;
import logica.Controladores.IControladorRegistro;
import logica.DatatypesYEnum.DTTipoDeRegistro;

import java.awt.*;
import java.util.Set;


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

	        comboEventos = new JComboBox<>();
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
	        cargarEventos();
	        cargarEdiciones();
	        cargarTiposRegistro();
	    }

	    private void cargarEdiciones() {
	        comboEdiciones.removeAllItems();
	        areaDatos.setText("");

	        String evento = (String) comboEventos.getSelectedItem();
	        if (evento == null) return;
	        else {
	    		IControladorEvento ctrlEventos = IControladorEvento.getInstance();
	    		Set<String> ediciones = ctrlEventos.listarEdiciones(evento);
	    		for (String ed: ediciones) {
	    			comboEdiciones.addItem(ed);
	    		}
	    	}
	    }

	    private void cargarEventos() {
	    	comboEventos.removeAllItems();

	    	IControladorEvento ctrlEventos = IControladorEvento.getInstance();
    		Set<String> eventos = ctrlEventos.listarEventos();
    		for (String ev: eventos) {
    			comboEventos.addItem(ev);
    		}
	    }

	    private void cargarTiposRegistro() {
	        comboTiposRegistro.removeAllItems();
	        areaDatos.setText("");

	        String edicion = (String) comboEdiciones.getSelectedItem();
	        if (edicion == null) return;
	        else {
	    		IControladorRegistro ctrlRegistros = IControladorRegistro.getInstance();
	    		Set<String> tiposRegistro = ctrlRegistros.listarTipoRegistro(edicion);
	    		for (String tr: tiposRegistro) {
	    			comboTiposRegistro.addItem(tr);
	    		}
	    	}
	    }

	    private void mostrarDatosTipoRegistro() {
	        areaDatos.setText("");
	        String edicion = (String) comboEdiciones.getSelectedItem();
	        String tipo = (String) comboTiposRegistro.getSelectedItem();
	        if (tipo == null) return;
	        else {
	        	IControladorRegistro ctrlRegistros = IControladorRegistro.getInstance();
	        	DTTipoDeRegistro dtTipoReg = ctrlRegistros.consultaTipoDeRegistro(edicion, tipo);
	        	areaDatos.setText(
	        			"Nombre: " + dtTipoReg.getNombre() + "\n" +
		                "Descripción: " + dtTipoReg.getDescripcion() + "\n" +
		                "Costo: " + dtTipoReg.getCosto() + "\n" +
		                "Cupo: " + dtTipoReg.getCupo() + "\n"
	        			);
	        }
	    }
	}

