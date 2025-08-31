package gui.internal;

import javax.swing.*;

import excepciones.UsuarioNoExisteException;
import excepciones.UsuarioYaRegistradoEnEdicionException;
import logica.DatatypesYEnum.DTFecha;
import logica.Controladores.IControladorEvento;
import logica.Controladores.IControladorRegistro;
import logica.Controladores.IControladorUsuario;

import java.awt.*;
import java.time.LocalDate;
import java.util.Set;

public class RegistroAEdicionFrame extends JInternalFrame {
    private JComboBox<String> comboEventos;
    private JComboBox<String> comboEdiciones;
    private JComboBox<String> comboTiposRegistro;
    private JComboBox<String> comboAsistentes;

    private IControladorEvento ctrlEventos = IControladorEvento.getInstance();
    private IControladorRegistro ctrlRegistros = IControladorRegistro.getInstance();
    private IControladorUsuario ctrlUsuarios = IControladorUsuario.getInstance();

    public RegistroAEdicionFrame() {
        super("Registro a Edición de Evento", true, true, true, true);
        setSize(600, 400);
        setLayout(new BorderLayout(10, 10));

        JPanel form = new JPanel(new GridLayout(0, 2, 10, 10));
        form.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Combos
        //comboEventos = new JComboBox<>(new String[]{"ConfUdelar", "ExpoTech"});
        comboEventos = new JComboBox<>();
        cargarEventos();
        comboEdiciones = new JComboBox<>();
        comboTiposRegistro = new JComboBox<>();
        comboAsistentes = new JComboBox<>();
        cargarAsistentes();

        form.add(new JLabel("Evento:"));
        form.add(comboEventos);

        form.add(new JLabel("Edición:"));
        form.add(comboEdiciones);

        form.add(new JLabel("Tipo de Registro:"));
        form.add(comboTiposRegistro);

        form.add(new JLabel("Asistente:"));
        form.add(comboAsistentes);

        add(form, BorderLayout.CENTER);

        // Botones
        JPanel buttons = new JPanel();
        JButton btnConfirmar = new JButton("Registrar");
        JButton btnCancelar = new JButton("Cancelar");
        buttons.add(btnConfirmar);
        buttons.add(btnCancelar);
        add(buttons, BorderLayout.SOUTH);

        // Listeners
        comboEventos.addActionListener(e -> cargarEdiciones());
        comboEdiciones.addActionListener(e -> cargarTiposRegistro());
        btnConfirmar.addActionListener(e -> {
			try {
				registrar();
			} catch (UsuarioYaRegistradoEnEdicionException | UsuarioNoExisteException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
        btnCancelar.addActionListener(e -> dispose());

        // Inicialización
        cargarEdiciones();
    }

    private void cargarAsistentes() {
    	Set<String> asistentes = ctrlUsuarios.listarAsistentes();
    	for (String asistente : asistentes) {
    		comboAsistentes.addItem(asistente);
    	}
    }

    public DTFecha obtenerFechaActual() {
    	LocalDate hoy = LocalDate.now();

        int dia = hoy.getDayOfMonth();
        int mes = hoy.getMonthValue();
        int anio = hoy.getYear();

        DTFecha f = new DTFecha(dia, mes, anio);
        return f;
    }

    private void cargarEventos() {
    	Set<String> eventos = ctrlEventos.listarEventos();
    	for (String evento : eventos) {
    		comboEventos.addItem(evento);
    	}
    }

    private void cargarEdiciones() {
        comboEdiciones.removeAllItems();
        comboTiposRegistro.removeAllItems();

        String evento = (String) comboEventos.getSelectedItem();
        if (evento == null) return;
        else {
        	Set<String> eventos = ctrlEventos.listarEdiciones(evento);
        	for (String edicion : eventos) {
        		comboEdiciones.addItem(edicion);
        	}
        }
    }

    private void cargarTiposRegistro() {
        comboTiposRegistro.removeAllItems();

        String edicion = (String) comboEdiciones.getSelectedItem();
        if (edicion == null) return;
        else {
        	Set<String> tiposDeRegistro = ctrlRegistros.listarTipoRegistro(edicion);
        	for (String tr : tiposDeRegistro) {
        		comboTiposRegistro.addItem(tr);
        	}
        }
    }

    private void registrar() throws UsuarioYaRegistradoEnEdicionException, UsuarioNoExisteException {
        String evento = (String) comboEventos.getSelectedItem();
        String edicion = (String) comboEdiciones.getSelectedItem();
        String tipo = (String) comboTiposRegistro.getSelectedItem();
        String asistente = (String) comboAsistentes.getSelectedItem();

        if (evento == null || edicion == null || tipo == null || asistente == null) {
            JOptionPane.showMessageDialog(this,
                    "Debe seleccionar evento, edición, tipo de registro y asistente.",
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        //ya esta registrado
        try {
        	if (ctrlRegistros.estaRegistrado(edicion, asistente)) {
            	JOptionPane.showMessageDialog(this,
                        "El asistente ya esta registrado al evento.",
                        "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }
        } catch (Exception ex) {
        	JOptionPane.showMessageDialog(this,
                    "Hubo un error procesando el registro.",
                    "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        //no hay mas cupos
        if (ctrlRegistros.alcanzoCupo(edicion, tipo)) {
        	JOptionPane.showMessageDialog(this,
                    "No hay mas cupos disponibles.",
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }


        DTFecha fechaRegistro = obtenerFechaActual();
        double costo = ctrlRegistros.consultaTipoDeRegistro(edicion, tipo).getCosto();
        ctrlRegistros.altaRegistro(edicion, asistente, tipo, fechaRegistro, costo);
        JOptionPane.showMessageDialog(this,
                "Registro creado:\n" +
                        "Evento: " + evento + "\n" +
                        "Edición: " + edicion + "\n" +
                        "Tipo: " + tipo + "\n" +
                        "Asistente: " + asistente + "\n" +
                        "Fecha de Alta: " + fechaRegistro + "\n" +
                        "Costo: " + costo,
                "Éxito", JOptionPane.INFORMATION_MESSAGE);

        dispose();
    }
}
