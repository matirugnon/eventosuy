package gui.internal;

import javax.swing.*;

import logica.Controladores.ControladorEvento;
import logica.DatatypesYEnum.DTFecha;

import java.awt.*;
import java.util.Calendar;
import java.util.Set;

@SuppressWarnings("serial")
public class AltaEventoFrame extends JInternalFrame {
    private JTextField txtNombre, txtSigla;
    private JTextArea txtDescripcion;
    private JList<String> listaCategorias;
    private JButton btnAceptar, btnCancelar;

    // Spinners para día, mes y año
    private JSpinner spinnerDia, spinnerMes, spinnerAnio;


  //constructor del internal frame, solo diseno
    public AltaEventoFrame() {

        super("Alta de Evento", true, true, true, true);
        setSize(500, 400);
        setLayout(new BorderLayout());

        // Formulario central
        JPanel form = new JPanel(new GridLayout(0, 2, 5, 5));

        form.add(new JLabel("Nombre:"));
        txtNombre = new JTextField();
        form.add(txtNombre);

        form.add(new JLabel("Sigla:"));
        txtSigla = new JTextField();
        form.add(txtSigla);


        form.add(new JLabel("Fecha Alta (Día/Mes/Año):"));
        JPanel panelFecha = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));

        // Spinner para día (1-31)
        spinnerDia = new JSpinner(new SpinnerNumberModel(1, 1, 31, 1));
        JSpinner.DefaultEditor editorDia = (JSpinner.DefaultEditor) spinnerDia.getEditor();
        editorDia.getTextField().setHorizontalAlignment(JTextField.CENTER);
        editorDia.getTextField().setPreferredSize(new Dimension(40, 20));

        // Spinner para mes (1-12)
        spinnerMes = new JSpinner(new SpinnerNumberModel(1, 1, 12, 1));
        JSpinner.DefaultEditor editorMes = (JSpinner.DefaultEditor) spinnerMes.getEditor();
        editorMes.getTextField().setHorizontalAlignment(JTextField.CENTER);
        editorMes.getTextField().setPreferredSize(new Dimension(40, 20));

        // Spinner para año (rango personalizable)
        spinnerAnio = new JSpinner(new SpinnerNumberModel(2025, 0, 20000, 1));
        JSpinner.DefaultEditor editorAnio = (JSpinner.DefaultEditor) spinnerAnio.getEditor();
        editorAnio.getTextField().setHorizontalAlignment(JTextField.CENTER);
        editorAnio.getTextField().setPreferredSize(new Dimension(60, 20));

        // Añadir spinners al panel
        panelFecha.add(spinnerDia);
        panelFecha.add(new JLabel("/"));
        panelFecha.add(spinnerMes);
        panelFecha.add(new JLabel("/"));
        panelFecha.add(spinnerAnio);

        form.add(panelFecha);

        // Descripción
        form.add(new JLabel("Descripción:"));
        txtDescripcion = new JTextArea(3, 20);
        JScrollPane scrollDesc = new JScrollPane(txtDescripcion);
        form.add(scrollDesc);

        // Categorías
        form.add(new JLabel("Categorías:"));
        listaCategorias = new JList<>(new String[]{"Conferencia", "Taller", "Seminario", "Workshop"});
        listaCategorias.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane scrollCat = new JScrollPane(listaCategorias);
        form.add(scrollCat);

        add(form, BorderLayout.CENTER);

        // Panel de botones
        JPanel botones = new JPanel();
        btnAceptar = new JButton("Aceptar");
        btnCancelar = new JButton("Cancelar");
        botones.add(btnAceptar);
        botones.add(btnCancelar);
        add(botones, BorderLayout.SOUTH);

        // Eventos de botones
        btnAceptar.addActionListener(e -> altaEvento()); //ejecuta funcion de abajo si se apreta el boton
        btnCancelar.addActionListener(e -> dispose());
    }


    //------------------LOGICA-----------------
    private void altaEvento() {

    	//instancia de controlador eventos
    	ControladorEvento cont = ControladorEvento.getInstance();


    	//valores para dar de alta el evento
        String nombre = txtNombre.getText().trim();
        String sigla = txtSigla.getText().trim();
        String descripcion = txtDescripcion.getText().trim();
        java.util.Set<String> categorias = new java.util.HashSet<>(listaCategorias.getSelectedValuesList());

        // Obtener valores de los spinners
        int dia = (Integer) spinnerDia.getValue();
        int mes = (Integer) spinnerMes.getValue();
        int anio = (Integer) spinnerAnio.getValue();


        //---------------------------------FILTROS (AGREGAR MAS SI SE NECESITAN)----------------------------------

        // Validar que el nombre y categorías no estén vacíos
        if (nombre.isEmpty() || categorias.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Debe ingresar un nombre y al menos una categoría.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (esFechaValida(dia, mes, anio)) {
            JOptionPane.showMessageDialog(this,
                    "La fecha ingresada no es válida (ej. 30 de febrero).",
                    "Error de Fecha", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if(cont.existeEvento(nombre)) {
        	JOptionPane.showMessageDialog(this,
                    "Nombre de evento ya existente, elija otro",
                    "Error de Evento", JOptionPane.ERROR_MESSAGE);
            return;
        }


        //---------------------------------paso los filtros, se puede dar de alta--------------------------------

        DTFecha fechaAlta = new DTFecha(dia, mes, anio);

        cont.darAltaEvento(nombre,descripcion ,fechaAlta ,sigla , categorias);



        String fecha = String.format("%04d-%02d-%02d", anio, mes, dia);
        // Mostrar confirmación
        JOptionPane.showMessageDialog(this,
                "Evento '" + nombre + "' dado de alta correctamente.\n" +
                "Sigla: " + sigla + "\nFecha: " + fecha + "\nCategorías: " + categorias + "\nDescripción: " + descripcion,
                "Éxito", JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }

    private boolean esFechaValida(int dia, int mes, int anio) {
        return mes < 1 || mes > 12 || dia > 31|| dia<1 ;
    }
}