package gui.internal;

import javax.swing.*;

import logica.Controladores.ControladorEvento;
import logica.Controladores.IControladorEvento;

import logica.DatatypesYEnum.DTFecha;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;
import java.util.List;

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

        ControladorEvento contr = ControladorEvento.getInstance();

        Set<String> listacat = contr.listarCategorias();
        //falta hacer metodo en el controlador

        listaCategorias = new JList<>(listacat.toArray(new String[0]));
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
        IControladorEvento cont = IControladorEvento.getInstance();

        // Obtener datos del formulario
        String nombre = txtNombre.getText().trim();
        String sigla = txtSigla.getText().trim();
        String descripcion = txtDescripcion.getText().trim();
        List<String> seleccionadas = listaCategorias.getSelectedValuesList();

        // Validaciones en presentacion

        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe ingresar un nombre de evento.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (seleccionadas.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar al menos una categoría.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (sigla.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe ingresar una sigla.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validar fecha
        int dia = (Integer) spinnerDia.getValue();
        int mes = (Integer) spinnerMes.getValue();
        int anio = (Integer) spinnerAnio.getValue();

        if (!cont.esFechaValida(dia, mes, anio)) {
            JOptionPane.showMessageDialog(this, "La fecha ingresada no es válida.", "Error de Fecha", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Convertir categorías
        Set<String> categorias = new HashSet<>(seleccionadas);
        DTFecha fechaAlta = new DTFecha(dia, mes, anio);

        // ----------------- ALTA DEL EVENTO (con manejo de excepciones) -----------------

        try {
            cont.darAltaEvento(nombre, descripcion, fechaAlta, sigla, categorias);

            // Éxito: mostrar mensaje
            String fechaStr = String.format("%04d-%02d-%02d", anio, mes, dia);
            JOptionPane.showMessageDialog(this,
                    "Evento '" + nombre + "' dado de alta correctamente.\n" +
                    "Sigla: " + sigla + "\nFecha: " + fechaStr + "\nCategorías: " + categorias,
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
            dispose();

        } catch (excepciones.EventoRepetidoException e) {
            // Capturamos la excepción específica
            JOptionPane.showMessageDialog(this, e.getMessage(), "Evento duplicado", JOptionPane.WARNING_MESSAGE);
        } catch (Exception e) {
            // Captura cualquier otro error inesperado
            JOptionPane.showMessageDialog(this,
                    "Error inesperado al dar de alta el evento:\n" + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace(); // útil para debugging
        }
    }


}