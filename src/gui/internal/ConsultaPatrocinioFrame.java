package gui.internal;

import javax.swing.*;
import java.awt.*;

import logica.Controladores.IControladorEvento;
import logica.DatatypesYEnum.DTPatrocinio;
import logica.DatatypesYEnum.DTFecha;

public class ConsultaPatrocinioFrame extends JInternalFrame {
    private JComboBox<String> comboEventos;
    private JComboBox<String> comboEdiciones;
    private JComboBox<String> comboPatrocinios;
    private JTextArea areaDatos;

    private IControladorEvento ctrlEvento;

    public ConsultaPatrocinioFrame() {
        super("Consulta de Patrocinio", true, true, true, true);
        this.ctrlEvento = IControladorEvento.getInstance();

        setSize(600, 450);
        setLayout(new BorderLayout(10, 10));

        JPanel top = new JPanel(new GridLayout(3, 2, 10, 10));
        top.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        comboEventos = new JComboBox<>(ctrlEvento.listarEventos().toArray(new String[0]));
        comboEdiciones = new JComboBox<>();
        comboPatrocinios = new JComboBox<>();

        top.add(new JLabel("Evento:"));
        top.add(comboEventos);

        top.add(new JLabel("Edición:"));
        top.add(comboEdiciones);

        top.add(new JLabel("Patrocinio:"));
        top.add(comboPatrocinios);

        add(top, BorderLayout.NORTH);

        areaDatos = new JTextArea();
        areaDatos.setEditable(false);
        JScrollPane scroll = new JScrollPane(areaDatos);
        scroll.setBorder(BorderFactory.createTitledBorder("Datos del Patrocinio"));
        add(scroll, BorderLayout.CENTER);

        // Listeners
        comboEventos.addActionListener(e -> cargarEdiciones());
        comboEdiciones.addActionListener(e -> cargarPatrocinios());
        comboPatrocinios.addActionListener(e -> mostrarDatos());

        // inicializar
        cargarEdiciones();
    }

    private void cargarEdiciones() {
        comboEdiciones.removeAllItems();
        comboPatrocinios.removeAllItems();
        areaDatos.setText("");

        String evento = (String) comboEventos.getSelectedItem();
        if (evento != null) {
            for (String ed : ctrlEvento.listarEdiciones(evento)) {
                comboEdiciones.addItem(ed);
            }
        }
        cargarPatrocinios();
    }

    private void cargarPatrocinios() {
        comboPatrocinios.removeAllItems();
        areaDatos.setText("");

        String edicion = (String) comboEdiciones.getSelectedItem();
        if (edicion != null) {
            for (String cod : ctrlEvento.listarPatrocinios(edicion)) {
                comboPatrocinios.addItem(cod);
            }
        }
    }

    private void mostrarDatos() {
        areaDatos.setText("");

        String edicion = (String) comboEdiciones.getSelectedItem();
        String codigo = (String) comboPatrocinios.getSelectedItem();

        if (edicion == null) return;

        if (codigo == null) {
        	return;
		}

        if(edicion != null && codigo != null) {

	        DTPatrocinio dto = ctrlEvento.consultarTipoPatrocinioEdicion(edicion, codigo);

	        if (dto != null) {
	            DTFecha f = dto.getFechaAlta();
	            areaDatos.setText(
	                    "Código: " + dto.getCodigo() + "\n" +
	                    "Institución: " + dto.getInstitucion() + "\n" +
	                    "Edición: " + dto.getEdicion() + "\n" +
	                    "Nivel: " + dto.getNivel() + "\n" +
	                    "Monto: " + dto.getMonto() + "\n" +
	                    "Fecha Alta: " + f.getDia() + "/" + f.getMes() + "/" + f.getAnio()
	            );
	        }
        }
    }
}
