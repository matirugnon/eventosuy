package gui.internal;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import logica.Asistente;
import logica.Edicion;
import logica.Organizador;
import logica.Registro;
import logica.Usuario;
import logica.Controladores.IControladorEvento;
import logica.Controladores.IControladorUsuario;
import logica.DatatypesYEnum.DTUsuario;
import logica.manejadores.ManejadorUsuario;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

@SuppressWarnings("serial")
public class ConsultaUsuarioFrame extends JInternalFrame {

    private final Consumer<JInternalFrame> openInternal; // para abrir detalle

    private JTextArea areaDatos;
    private JTable tablaDetalle;
    private JButton btnVerDetalle;
    private JScrollPane scrollTabla;

    private JComboBox<String> comboUsuarios;
    private JList<String> listaResultados;

    private IControladorUsuario ctrlUsuarios = IControladorUsuario.getInstance();
    private IControladorEvento ctrlEventos = IControladorEvento.getInstance();

    public ConsultaUsuarioFrame(Consumer<JInternalFrame> openInternal) {
        super("Consulta de Usuario", true, true, true, true);
        setSize(600, 500);
        setLayout(new BorderLayout());

        this.openInternal = openInternal;

        inicializarComponentes();
    }

    private void inicializarComponentes() {
        JPanel form = new JPanel(new GridLayout(0, 2, 5, 5));
        form.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // --- Combo de usuarios ---
        form.add(new JLabel("Seleccionar Usuario:"));
        comboUsuarios = new JComboBox<>();
        cargarUsuarios();

        IControladorUsuario controladorUsuario = IControladorUsuario.getInstance();
        Set<String> usuarios = controladorUsuario.listarUsuarios();

        for (String u : usuarios) {

        	comboUsuarios.addItem(u);

		}

        form.add(comboUsuarios);

        // --- Área de texto para datos del usuario ---
        form.add(new JLabel("Datos del Usuario:"));
        areaDatos = new JTextArea(4, 20);
        areaDatos.setEditable(false);
        areaDatos.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollDatos = new JScrollPane(areaDatos);
        form.add(scrollDatos);

        // --- Lista de elementos asociados ---
        form.add(new JLabel("Elementos Asociados:"));
        listaResultados = new JList<>();
        listaResultados.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollResultados = new JScrollPane(listaResultados);
        form.add(scrollResultados);

        add(form, BorderLayout.CENTER);

        // --- Listener ---
        comboUsuarios.addActionListener(e -> actualizarListaSegunUsuario());
    }

    private void cargarUsuarios() {

    	ManejadorUsuario mUsuario = ManejadorUsuario.getinstance();
        Set<String> usuarios = ctrlUsuarios.listarUsuarios(); // ✅ Ahora es List
        for (String usr : usuarios) {
            comboUsuarios.addItem(usr);
        }
    }

    private void actualizarListaSegunUsuario() {
        String seleccionadoS = (String) comboUsuarios.getSelectedItem();

        if (seleccionadoS == "") {
            return;
        }

        // Construir datos del usuario

        IControladorUsuario contrU = IControladorUsuario.getInstance();

        DTUsuario dtU = contrU.getDTUsuario(seleccionadoS);



        StringBuilder datos = new StringBuilder();
        datos.append("Nombre: ").append(seleccionado.getNombre()).append("\n");
        datos.append("Nickname: ").append(seleccionado.getNickname()).append("\n");
        datos.append("Correo: ").append(seleccionado.getCorreo()).append("\n");

        if (seleccionado instanceof Asistente) {
            Asistente asistente = (Asistente) seleccionado;
            datos.append("Apellido: ").append(asistente.getApellido()).append("\n");
            datos.append("Fecha Nac.: ").append(
                asistente.getFechaNacimiento() != null ?
                asistente.getFechaNacimiento().toString() :
                "No especificada"
            ).append("\n");
            datos.append("Institución: ").append(
                asistente.getInstitucion() != null ?
                asistente.getInstitucion() :
                "No especificada"
            ).append("\n");
            datos.append("Tipo: Asistente");

        } else if (seleccionado instanceof Organizador) {
            Organizador organizador = (Organizador) seleccionado;
            datos.append("Descripción: ").append(
                organizador.getDescripcion() != null ?
                organizador.getDescripcion() :
                "No disponible"
            ).append("\n");
            datos.append("Link: ").append(
                organizador.getLink() != null ?
                organizador.getLink() :
                "No disponible"
            ).append("\n");
            datos.append("Tipo: Organizador");
        }

        areaDatos.setText(datos.toString());

        // Actualizar lista de elementos asociados (sin cambios)
        if (seleccionado instanceof Organizador) {

            Organizador org = (Organizador) seleccionado;
            Set<Edicion> ediciones = org.getEdiciones();
            if (ediciones.isEmpty()) {
                listaResultados.setListData(new String[]{"No tiene ediciones a cargo."});
            } else {
                listaResultados.setListData(ediciones.toArray(new Edicion[0]));
            }
        } else if (seleccionado instanceof Asistente) {
            Asistente asistente = (Asistente) seleccionado;
            Set<Registro> registros = asistente.getRegistros();
            if (registros.isEmpty()) {
                listaResultados.setListData(new String[]{"No tiene registros."});
            } else {
                listaResultados.setListData(registros.toArray(new Registro[0]));
            }
        }
    }

    // Renderizador para mostrar nombre y apellido si es posible
    class UsuarioListRenderer extends JLabel implements ListCellRenderer<Usuario> {
        @Override
        public Component getListCellRendererComponent(JList<? extends Usuario> list, Usuario value, int index,
                boolean isSelected, boolean cellHasFocus) {

            if (value == null) {
                setText("<sin usuario>");
            } else {
                String tipo = (value instanceof Organizador) ? "Organizador" : "Asistente";
                setText(value.getNickname() + "(" + tipo + ")");
            }

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