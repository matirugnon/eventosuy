package gui.internal;

import javax.swing.*;



import logica.Controladores.IControladorUsuario;
import logica.DatatypesYEnum.DTFecha;


import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("serial")
public class ModificarUsuarioFrame extends JInternalFrame {

	private JComboBox<String> comboUsuarios;
    private JTextField txtNickname, txtCorreo, txtNombre, txtApellido, txtFechaNac, txtDescripcion, txtWeb;
    private JButton btnGuardar, btnCancelar;


    //logica
    private IControladorUsuario controlador;
    private Map<String, Usuario> mapaUsuarios;





    public ModificarUsuarioFrame() {
        super("Modificar Usuario", true, true, true, true);

        //logica
        controlador = IControladorUsuario.getInstance();

        this.mapaUsuarios = new HashMap<>();

        setSize(500, 400);
        setLayout(new BorderLayout());

        // TOP: selección de usuario
        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Seleccione Usuario:"));
        comboUsuarios = new JComboBox<>();

        Set<String> usrsSet = controlador.listarUsuarios();

        for (String usr : usrsSet) {

        	Usuario u = mu.obtenerUsuario(usr);

            String texto = u.getNickname() + " (" +
                          (u instanceof Asistente ? "Asistente" : "Organizador") + ")";
            comboUsuarios.addItem(texto);
            mapaUsuarios.put(u.getNickname(), u); // guardamos referencia
        }

        topPanel.add(comboUsuarios);
        JButton btnCargar = new JButton("Cargar Datos");
        topPanel.add(btnCargar);
        add(topPanel, BorderLayout.NORTH);

        // CENTER: formulario editable
        JPanel form = new JPanel(new GridLayout(0, 2, 5, 5));

        form.add(new JLabel("Nickname:"));
        txtNickname = new JTextField();
        txtNickname.setEditable(false); // no se puede modificar
        form.add(txtNickname);

        form.add(new JLabel("Correo:"));
        txtCorreo = new JTextField();
        txtCorreo.setEditable(false); // no se puede modificar
        form.add(txtCorreo);

        form.add(new JLabel("Nombre:"));
        txtNombre = new JTextField();
        form.add(txtNombre);

        form.add(new JLabel("Apellido (solo asistentes):"));
        txtApellido = new JTextField();
        form.add(txtApellido);

        form.add(new JLabel("Fecha Nacimiento (YYYY-MM-DD)(solo asistentes):"));
        txtFechaNac = new JTextField();
        form.add(txtFechaNac);

        form.add(new JLabel("Descripción (solo organizador):"));
        txtDescripcion = new JTextField();
        form.add(txtDescripcion);

        form.add(new JLabel("Sitio Web (solo organizador):"));
        txtWeb = new JTextField();
        form.add(txtWeb);

        add(form, BorderLayout.CENTER);

        // BOTTOM: botones
        JPanel botones = new JPanel();
        btnGuardar = new JButton("Guardar");
        btnCancelar = new JButton("Cancelar");
        botones.add(btnGuardar);
        botones.add(btnCancelar);
        add(botones, BorderLayout.SOUTH);

        // Eventos
        btnCargar.addActionListener(e -> cargarDatosUsuario());
        btnGuardar.addActionListener(e -> guardarCambios());
        btnCancelar.addActionListener(e -> dispose());

        // Inicializar estado de campos según selección inicial (por defecto)
        actualizarCamposPorRol();
        comboUsuarios.addActionListener(e -> actualizarCamposPorRol());
    }

    // Actualiza la habilitación de campos según el rol seleccionado
    private void actualizarCamposPorRol() {

    	Usuario seleccionado = (Usuario) comboUsuarios.getSelectedItem();
    	boolean esAsistente = seleccionado instanceof Asistente;
    	boolean esOrganizador = seleccionado instanceof Organizador;

        // Asistente: habilita apellido y fecha, deshabilita descripción y web
        txtApellido.setEditable(esAsistente);
        txtFechaNac.setEditable(esAsistente);

        // Organizador: habilita descripción y web, deshabilita apellido y fecha
        txtDescripcion.setEditable(esOrganizador);
        txtWeb.setEditable(esOrganizador);
    }

    // ⚠️ Mock de datos de ejemplo
    private void cargarDatosUsuario() {
        String seleccionado = (String) comboUsuarios.getSelectedItem();
        if (seleccionado == null) return;

        // ⚠️ el texto tiene la forma "nick (rol)", me quedo solo con el nick
        String nick = seleccionado.split(" ")[0];

        Usuario u = mapaUsuarios.get(nick);

        txtNickname.setText(u.getNickname());
        txtCorreo.setText(u.getCorreo());
        txtNombre.setText(u.getNombre());

        if (u instanceof Asistente) {
            Asistente a = (Asistente) u;
            txtApellido.setText(a.getApellido());
            txtFechaNac.setText(a.getFechaNacimiento().toString());
            txtDescripcion.setText("");
            txtWeb.setText("");
        } else if (u instanceof Organizador) {
            Organizador o = (Organizador) u;
            txtDescripcion.setText(o.getDescripcion());
            txtWeb.setText(o.getLink());
            txtApellido.setText("");
            txtFechaNac.setText("");
        }

        actualizarCamposPorRol();
    }


    private void guardarCambios() {
        String seleccionado = (String) comboUsuarios.getSelectedItem();
        if (seleccionado == null) return;

        String nick = seleccionado.split(" ")[0];
        Usuario u = mapaUsuarios.get(nick);

        u.setNombre(txtNombre.getText());

        if (u instanceof Asistente) {
            Asistente a = (Asistente) u;
            a.setApellido(txtApellido.getText());
            a.setFechaNac(txtFechaNac.getText());
        } else if (u instanceof Organizador) {
            Organizador o = (Organizador) u;
            o.setDescripcion(txtDescripcion.getText());
            o.setLink(txtWeb.getText());
        }

        JOptionPane.showMessageDialog(this,
                "Usuario " + u.getNickname() + " modificado con éxito.",
                "Éxito", JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }

}