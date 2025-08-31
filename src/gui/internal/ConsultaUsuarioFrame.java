package gui.internal;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;



import logica.Controladores.IControladorEvento;
import logica.Controladores.IControladorUsuario;
import logica.DatatypesYEnum.DTAsistente;
import logica.DatatypesYEnum.DTOrganizador;
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
    private  JLabel labelCambiante;

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


        labelCambiante = new JLabel("Elementos Asociados:");

        form.add(labelCambiante);
        listaResultados = new JList<>();
        listaResultados.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollResultados = new JScrollPane(listaResultados);
        form.add(scrollResultados);

        add(form, BorderLayout.CENTER);

        comboUsuarios.addActionListener(e -> actualizarListaSegunUsuario());
    }

    private void cargarUsuarios() {

        Set<String> usuarios = ctrlUsuarios.listarUsuarios();

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
        datos.append("Nombre: ").append(dtU.getNombre()).append("\n");
        datos.append("Nickname: ").append(dtU.getNickname()).append("\n");
        datos.append("Correo: ").append(dtU.getCorreo()).append("\n");

        if (dtU instanceof DTAsistente) {

        	labelCambiante.setText("Registros Asociados");

        	DTAsistente dta = (DTAsistente) dtU;

            datos.append("Apellido: ").append(dta.getApellido()).append("\n");
            datos.append("Fecha Nac.: ").append(
            		dta.getFechaNacimiento() != null ?
            		dta.getFechaNacimiento().toString() :
                "No especificada"
            ).append("\n");
            datos.append("Institución: ").append(
            		dta.getInstitucion() != null ?
            		dta.getInstitucion() :
                "No especificada"
            ).append("\n");
            datos.append("Tipo: Asistente");


            Set<String> registros = contrU.obtenerRegistros(seleccionadoS);


            if (registros.isEmpty()) {
                listaResultados.setListData(new String[]{"No tiene registros."});
            } else {
                listaResultados.setListData(registros.toArray(new String[0]));
            }



        } else if (dtU instanceof DTOrganizador) {

        	labelCambiante.setText("Ediciones Asociadas");

        	DTOrganizador dto = (DTOrganizador) dtU;

            datos.append("Descripción: ").append(
            		dto.getDescripcion() != null ?
            		dto.getDescripcion() :
                "No disponible"
            ).append("\n");
            datos.append("Link: ").append(
            		dto.getLink() != null ?
            		dto.getLink() :
                "No disponible"
            ).append("\n");
            datos.append("Tipo: Organizador");

            Set<String> ediciones = ctrlEventos.listarEdiciones(seleccionadoS);

            if (ediciones.isEmpty()) {
                listaResultados.setListData(new String[]{"No tiene ediciones a cargo."});
            } else {
                listaResultados.setListData(ediciones.toArray(new String[0]));
            }

        }

        areaDatos.setText(datos.toString());

    }



}