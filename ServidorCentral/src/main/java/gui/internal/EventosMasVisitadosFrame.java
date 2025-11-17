package gui.internal;

import java.awt.Font;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

import logica.controladores.IControladorEvento;
import logica.datatypesyenum.DTEvento;

@SuppressWarnings("serial")
public class EventosMasVisitadosFrame extends JInternalFrame {

	private JTable tabla;
	private IControladorEvento ctrlEvento = IControladorEvento.getInstance();

	public EventosMasVisitadosFrame() {
		super("Eventos Más Visitados", true, true, true, true);
		setSize(500, 300);
		setLayout(new BorderLayout());
		inicializarComponentes();
		cargarDatos();
	}

	private void inicializarComponentes() {
		String[] columnNames = { "#", "Nombre Evento", "Cantidad de Visitas" };
		DefaultTableModel modelo = new DefaultTableModel(columnNames, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false; // La tabla NO es editable
			}
		};

		tabla = new JTable(modelo);
		tabla.setRowHeight(22);
		tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
		tabla.setFont(new Font("Segoe UI", Font.PLAIN, 12));

		JScrollPane scrollPane = new JScrollPane(tabla);
		add(scrollPane, BorderLayout.CENTER);
	}

	private void cargarDatos() {
		try {
			List<DTEvento> topEventos = ctrlEvento.obtenerMasVisitados();

			DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();
			modelo.setRowCount(0);

			int rank = 1;
			for (var ev : topEventos) {
				modelo.addRow(new Object[] { rank++, ev.getNombre(), ev.getVisitas() });
			}

		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Error al cargar los eventos: " + e.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}
}
