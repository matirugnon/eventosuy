package gui.internal;

import javax.swing.*;
import java.awt.*;

public class AltaUsuarioFrame extends JInternalFrame {
    /**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private JTextField txtNickname, txtNombre, txtCorreo;
    private JComboBox<String> comboTipoUsuario;

    public AltaUsuarioFrame() {
        super("Alta de Usuario", true, true, true, true);
        setSize(400, 300);
        setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridLayout(0, 2, 5, 5));
        form.add(new JLabel("Nickname:"));
        txtNickname = new JTextField();
        form.add(txtNickname);

        form.add(new JLabel("Nombre:"));
        txtNombre = new JTextField();
        form.add(txtNombre);

        form.add(new JLabel("Correo:"));
        txtCorreo = new JTextField();
        form.add(txtCorreo);

        form.add(new JLabel("Tipo Usuario:"));
        comboTipoUsuario = new JComboBox<>(new String[]{"Asistente", "Organizador"});
        form.add(comboTipoUsuario);

        add(form, BorderLayout.CENTER);

        JPanel botones = new JPanel();
        JButton btnAceptar = new JButton("Aceptar");
        JButton btnCancelar = new JButton("Cancelar");

        btnAceptar.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Usuario dado de alta correctamente");
            dispose();
        });
        btnCancelar.addActionListener(e -> dispose());

        botones.add(btnAceptar);
        botones.add(btnCancelar);

        add(botones, BorderLayout.SOUTH);
    }
}
