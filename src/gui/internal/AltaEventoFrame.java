package gui.internal;

import javax.swing.*;
import java.awt.*;

import javax.swing.text.MaskFormatter;

@SuppressWarnings("serial")
public class AltaEventoFrame extends JInternalFrame {
    private JTextField txtNombre, txtSigla, txtFecha;
    private JTextArea txtDescripcion;
    private JList<String> listaCategorias;
    private JButton btnAceptar, btnCancelar;

    private JFormattedTextField txtFechaM;

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



        form.add(new JLabel("Fecha Alta (YYYY-MM-DD):"));

        try {
            MaskFormatter dateMask = new MaskFormatter("##/##/####");
            dateMask.setPlaceholderCharacter('_');
            txtFechaM = new JFormattedTextField(dateMask);
        } catch (Exception ex) {
            txtFechaM = new JFormattedTextField(); // fallback
        }
        form.add(txtFechaM);





        form.add(new JLabel("Descripción:"));
        txtDescripcion = new JTextArea(3, 20);
        JScrollPane scrollDesc = new JScrollPane(txtDescripcion);
        form.add(scrollDesc);

        form.add(new JLabel("Categorías:"));



        //después conectar al manejador que tiene las categorias, no me acuerdo cual es

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
        btnAceptar.addActionListener(e -> altaEvento());
        btnCancelar.addActionListener(e -> dispose());
    }

    private void altaEvento() {
        String nombre = txtNombre.getText();
        String sigla = txtSigla.getText();
        String fecha = txtFechaM.getText();
        String descripcion = txtDescripcion.getText();
        java.util.List<String> categorias = listaCategorias.getSelectedValuesList();

        if (nombre.isEmpty() || categorias.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Debe ingresar un nombre y al menos una categoría.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(this,
                "Evento '" + nombre + "' dado de alta correctamente.\n" +
                "Sigla: " + sigla + "\nFecha: " + fecha + "\nCategorías: " + categorias + "\nDescripcion: " + descripcion,
                "Éxito", JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }
}