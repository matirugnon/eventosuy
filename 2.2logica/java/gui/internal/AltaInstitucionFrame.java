package gui.internal;

import javax.swing.*;

import logica.controladores.IControladorUsuario;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@SuppressWarnings("serial")
public class AltaInstitucionFrame extends JInternalFrame {

    private JTextField tfNombre;
    private JTextField tfWeb;
    private JTextArea taDescripcion;
    private JButton btnAlta;
    private JButton btnCancelar;

    private IControladorUsuario controladorUsuario;

    public AltaInstitucionFrame() {

        super("Alta de Institución", true, true, true, true);
        controladorUsuario = IControladorUsuario.getInstance();

        // Configuración del frame
        setSize(400, 300);
        setLayout(new BorderLayout());

        // Panel de campos
        JPanel panelCampos = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Nombre
        gbc.gridx = 0;
        gbc.gridy = 0;
        panelCampos.add(new JLabel("Nombre:"), gbc);

        tfNombre = new JTextField(20);
        gbc.gridx = 1;
        panelCampos.add(tfNombre, gbc);

        // Descripción
        gbc.gridx = 0;
        gbc.gridy = 1;
        panelCampos.add(new JLabel("Descripción:"), gbc);

        taDescripcion = new JTextArea(5, 20);
        taDescripcion.setLineWrap(true);
        taDescripcion.setWrapStyleWord(true);
        JScrollPane scrollDescripcion = new JScrollPane(taDescripcion);
        gbc.gridx = 1;
        panelCampos.add(scrollDescripcion, gbc);

        // Web
        gbc.gridx = 0;
        gbc.gridy = 2;
        panelCampos.add(new JLabel("Sitio Web:"), gbc);

        tfWeb = new JTextField(20);
        gbc.gridx = 1;
        panelCampos.add(tfWeb, gbc);

        add(panelCampos, BorderLayout.CENTER);

        // Panel de botones
        JPanel panelBotones = new JPanel();
        btnAlta = new JButton("Dar de alta");
        btnCancelar = new JButton("Cancelar");

        panelBotones.add(btnAlta);
        panelBotones.add(btnCancelar);

        add(panelBotones, BorderLayout.SOUTH);

        // Acción del botón Alta
        btnAlta.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                altaInstitucion();
            }
        });

        // Acción del botón Cancelar
        btnCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }

    private void altaInstitucion() {
        String nombre = tfNombre.getText().trim();
        String descripcion = taDescripcion.getText().trim();
        String web = tfWeb.getText().trim();

        if(nombre.isEmpty() || descripcion.isEmpty() || web.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (controladorUsuario.existeInstitucion(nombre)) {
            JOptionPane.showMessageDialog(this, "Ya existe una institución con ese nombre.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }


        try {
            controladorUsuario.altaInstitucion(nombre, descripcion, web);
            JOptionPane.showMessageDialog(this, "Institución dada de alta correctamente.",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
            dispose(); // Cierra el frame tras el alta
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
