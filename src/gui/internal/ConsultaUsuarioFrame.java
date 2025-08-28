package gui.internal;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import logica.Asistente;
import logica.Edicion;
import logica.Organizador;
import logica.Registro;
import logica.Usuario;
import logica.Controladores.ControladorEvento;
import logica.Controladores.ControladorUsuario;

import java.awt.*;
import java.util.List;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Set;
import java.util.function.Consumer;

@SuppressWarnings("serial")
public class ConsultaUsuarioFrame extends JInternalFrame {

    private final Consumer<JInternalFrame> openInternal; // para abrir detalle
    private JTextArea areaDatos;
    private JTable tablaDetalle;
    private JButton btnVerDetalle;
    private JScrollPane scrollTabla;

    private JComboBox<Usuario> comboUsuarios;
    private JList<Object> listaResultados;

    private ControladorUsuario ctrlUsuarios = ControladorUsuario.getinstance();
    private ControladorEvento ctrlEventos = ControladorEvento.getInstance();


    public ConsultaUsuarioFrame(Consumer<JInternalFrame> openInternal) {

    	super("Consulta de Usuario", true, true, true, true);
        setSize(500, 400);
        setLayout(new BorderLayout());

        this.openInternal = openInternal;

        inicializarComponentes();
    }

    private void inicializarComponentes() {
        // 1. Panel del formulario
        JPanel form = new JPanel(new GridLayout(0, 2, 5, 5));

        // --- Combo de usuarios ---

        form.add(new JLabel("Seleccionar Usuario:"));
        comboUsuarios = new JComboBox<>();
        cargarUsuarios(); // Llena el combo
        comboUsuarios.setRenderer(new UsuarioListRenderer());
        form.add(comboUsuarios);

        // --- Lista de resultados (ediciones o registros) ---
        form.add(new JLabel("Elementos asociados:"));
        listaResultados = new JList<>();
        JScrollPane scrollResultados = new JScrollPane(listaResultados);
        form.add(scrollResultados);

        // Añadir formulario al centro
        add(form, BorderLayout.CENTER);

        // --- Listener para actualizar la lista cuando cambia el usuario ---
        comboUsuarios.addActionListener(e -> actualizarListaSegunUsuario());
    }


    private void cargarUsuarios() {
        List<Usuario> usuarios = ctrlUsuarios.listarUsuarios();
        for (Usuario usr : usuarios) {
            comboUsuarios.addItem(usr);
        }
    }

    private void actualizarListaSegunUsuario() {
        Usuario seleccionado = (Usuario) comboUsuarios.getSelectedItem();
        if (seleccionado == null) {
            listaResultados.setListData(new Object[0]);
            return;
        }

        if (seleccionado instanceof Organizador) {
            Set<Edicion> ediciones = ctrlEventos.listarEdiciones();
            listaResultados.setListData(ediciones.toArray(new Edicion[0]));
        } else if (seleccionado instanceof Asistente) {
            Asistente asistente = (Asistente) seleccionado;
            Set<Registro> registros = asistente.getRegistros();
            listaResultados.setListData(registros.toArray(new Registro[0]));
        } else {
            listaResultados.setListData(new Object[0]);
        }
    }

    class UsuarioListRenderer extends JLabel implements ListCellRenderer<Usuario> {
        @Override
        public Component getListCellRendererComponent(JList<? extends Usuario> list, Usuario value, int index,
                boolean isSelected, boolean cellHasFocus) {
            setText(value != null ? value.getNombre() : "<sin nombre>");
            if (isSelected) {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
                setOpaque(true);
            } else {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
                setOpaque(false);
            }
            return this;
        }
    }

}
