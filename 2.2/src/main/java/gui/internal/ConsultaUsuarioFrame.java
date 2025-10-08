package gui.internal;

import javax.swing.*;


import excepciones.UsuarioNoExisteException;
import logica.Controladores.IControladorEvento;
import logica.Controladores.IControladorRegistro;
import logica.Controladores.IControladorUsuario;
import logica.DatatypesYEnum.DTAsistente;
import logica.DatatypesYEnum.DTEdicion;
import logica.DatatypesYEnum.DTOrganizador;
import logica.DatatypesYEnum.DTRegistro;
import logica.DatatypesYEnum.DTUsuario;
import logica.DatatypesYEnum.EstadoEdicion;

import java.awt.*;

import java.util.Set;
import java.util.function.Consumer;

@SuppressWarnings("serial")
public class ConsultaUsuarioFrame extends JInternalFrame {



    private JTextArea areaDatos;
    private JButton btnVerDetalle;

    private JComboBox<String> comboUsuarios;
    private JList<String> listaResultados;
    private  JLabel labelCambiante;

    private IControladorUsuario ctrlUsuarios = IControladorUsuario.getInstance();
    private IControladorEvento ctrlEventos = IControladorEvento.getInstance();

    public ConsultaUsuarioFrame(Consumer<JInternalFrame> openInternal) {
        super("Consulta de Usuario", true, true, true, true);
        setSize(700, 500);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
        setBackground(Color.WHITE);
        getContentPane().setBackground(Color.WHITE);
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        JPanel form = new JPanel(new GridBagLayout());
        labelCambiante = new JLabel("");

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // --- Combo de usuarios ---
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 1;
        form.add(new JLabel("Seleccionar Usuario:"), gbc);

        gbc.gridx = 1; gbc.gridy = 0; gbc.gridwidth = 2;
        comboUsuarios = new JComboBox<>();
        cargarUsuarios();
        form.add(comboUsuarios, gbc);

        // --- Área de datos del usuario ---
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1;
        form.add(new JLabel("Datos del Usuario:"), gbc);

        gbc.gridx = 1; gbc.gridy = 1; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH; // Ahora puede expandirse verticalmente
        gbc.weightx = 1.0;
        gbc.weighty = 0.3; // Permite algo de expansión

        areaDatos = new JTextArea(8, 30); // ✅ Aumentado de 4 a 8 filas
        areaDatos.setEditable(false);
        areaDatos.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        areaDatos.setLineWrap(true);
        areaDatos.setWrapStyleWord(true);
        JScrollPane scrollDatos = new JScrollPane(areaDatos);
        form.add(scrollDatos, gbc);

        // Resetear weighty para componentes siguientes si es necesario
        gbc.weighty = 0.0;

        // --- Lista de ediciones o registros ---
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 1;
        form.add(new JLabel("Elementos Asociados:"), gbc);

        gbc.gridx = 1; gbc.gridy = 2; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 0.5; // Permite que la lista ocupe más espacio y se expanda

        listaResultados = new JList<>();
        listaResultados.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaResultados.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        JScrollPane scrollResultados = new JScrollPane(listaResultados);
        scrollResultados.setPreferredSize(new Dimension(200, 150)); // ✅ Aumentado de 120 a 150
        form.add(scrollResultados, gbc);

        // Resetear pesos
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // --- Botón Ver Edición/Registro ---
        gbc.gridx = 1; gbc.gridy = 3; gbc.gridwidth = 1;
        btnVerDetalle = new JButton("Ver Detalles");
        btnVerDetalle.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        btnVerDetalle.setPreferredSize(new Dimension(100, 22));
        btnVerDetalle.setEnabled(false);

        form.add(btnVerDetalle, gbc);

        // Acción del botón (ya estaba)
        btnVerDetalle.addActionListener(e -> {
            String usuarioSeleccionado = (String) comboUsuarios.getSelectedItem();
            String elementoSeleccionado = listaResultados.getSelectedValue();

            if (elementoSeleccionado == null ||
                "No tiene registros.".equals(elementoSeleccionado) ||
                "No tiene ediciones a cargo.".equals(elementoSeleccionado)) {
                JOptionPane.showMessageDialog(this, "Seleccione un elemento.", "Atención", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                mostrarDetalle(usuarioSeleccionado, elementoSeleccionado);
            } catch (UsuarioNoExisteException e1) {
                e1.printStackTrace();
            }
        });

        // Combo acción
        comboUsuarios.addActionListener(e -> {
            try {
                actualizarListaSegunUsuario();
            } catch (UsuarioNoExisteException e1) {
                e1.printStackTrace();
            }
        });

        add(form, BorderLayout.CENTER);
    }

    private void cargarUsuarios() {

        Set<String> usuarios = ctrlUsuarios.listarUsuarios();

        for (String usr : usuarios) {
            comboUsuarios.addItem(usr);
        }
    }

    private void actualizarListaSegunUsuario() throws UsuarioNoExisteException {

        String seleccionadoS = (String) comboUsuarios.getSelectedItem();

        if (seleccionadoS == "") {
            return;
        }

        IControladorUsuario contrU = IControladorUsuario.getInstance();


        DTUsuario dtU = contrU.getDTUsuario(seleccionadoS);

        StringBuilder datos = new StringBuilder();
        datos.append("Nombre: ").append(dtU.getNombre()).append("\n");
        datos.append("Nickname: ").append(dtU.getNickname()).append("\n");
        datos.append("Correo: ").append(dtU.getCorreo()).append("\n");

        if (dtU instanceof DTAsistente) {

        	labelCambiante.setText("Registros Asociados");
        	btnVerDetalle.setText("Ver Registro");
        	btnVerDetalle.setEnabled(true);

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


            Set<String> nombresTipos = contrU.obtenerRegistros(seleccionadoS); // Son los tipos de registro

            if (nombresTipos.isEmpty()) {
                listaResultados.setListData(new String[]{"No tiene registros."});
            } else {
                IControladorRegistro ctrlReg = IControladorRegistro.getInstance();
                DefaultListModel<String> modeloLista = new DefaultListModel<>();

                for (String tipo : nombresTipos) {
                    DTRegistro dtReg = ctrlReg.getRegistro(seleccionadoS, tipo);
                    if (dtReg != null) {
                        String texto = dtReg.getnomEdicion() + " - " + dtReg.getTipoDeRegistro();
                        modeloLista.addElement(texto);
                    } else {
                        modeloLista.addElement("Desconocido - " + tipo);
                    }
                }

                listaResultados.setModel(modeloLista);
            }


        } else if (dtU instanceof DTOrganizador) {

        	labelCambiante.setText("Ediciones Asociadas");
        	btnVerDetalle.setText("Ver Edición");
        	btnVerDetalle.setEnabled(true);

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

            Set<String> ediciones = ctrlUsuarios.listarEdicionesOrganizador(seleccionadoS);

            if (ediciones.isEmpty()) {
                listaResultados.setListData(new String[]{"No tiene ediciones a cargo."});
            } else {
                listaResultados.setListData(ediciones.toArray(new String[0]));
            }

        }



        areaDatos.setText(datos.toString());
        areaDatos.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        areaDatos.setLineWrap(true);
        areaDatos.setWrapStyleWord(true);
    }



    //FUNCIONES EXTRA

    private void mostrarDetalle(String nicknameUsuario, String elementoSeleccionado) throws UsuarioNoExisteException {

        DTUsuario dtUsuario = ctrlUsuarios.getDTUsuario(nicknameUsuario);
        if (dtUsuario == null) return;

        String titulo;
        JTextArea area = new JTextArea();
        area.setFont(new Font("Monospaced", Font.PLAIN, 12));
        area.setEditable(false);

        if (dtUsuario instanceof DTAsistente) {

            int idx = elementoSeleccionado.lastIndexOf(" - ");
            if (idx == -1) {
                JOptionPane.showMessageDialog(this, "Formato inválido.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String tipoRegistro = elementoSeleccionado.substring(idx + 3);

            IControladorRegistro ctrlReg = IControladorRegistro.getInstance();
            DTRegistro dtRegistro = ctrlReg.getRegistro(nicknameUsuario, tipoRegistro);

            if (dtRegistro == null) {
                JOptionPane.showMessageDialog(this, "No se encontró el registro.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            area.setText(
                "Asistente: " + dtRegistro.getAsistente() + "\n" +
                "Tipo de Registro: " + dtRegistro.getTipoDeRegistro() + "\n" +
                "Fecha de Registro: " + dtRegistro.getFechaRegistro() + "\n" +
                "Costo: $" + String.format("%.2f", dtRegistro.getCosto()) + "\n" +
                "Edición: " + dtRegistro.getnomEdicion()
            );
            titulo = "Detalle de Registro";

        } else if (dtUsuario instanceof DTOrganizador) {
            DTEdicion dtEdicion = ctrlEventos.consultarEdicion(elementoSeleccionado);
            if (dtEdicion == null) {
                JOptionPane.showMessageDialog(this, "No se encontró la edición.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            area.setText(
                "Nombre: " + dtEdicion.getNombre() + "\n" +
                "Sigla: " + dtEdicion.getSigla() + "\n" +
                "Fecha Inicio: " + dtEdicion.getFechaInicio() + "\n" +
                "Fecha Fin: " + dtEdicion.getFechaFin() + "\n" +
                "Alta Edición: " + dtEdicion.getAltaEdicion() + "\n" +
                "Ciudad: " + dtEdicion.getCiudad() + "\n" +
                "País: " + dtEdicion.getPais() + "\n" +
                "Organizador: " + dtEdicion.getOrganizador() + "\n" +
                "Estado: " + formatearEstado(dtEdicion.getEstado()) + "\n" +
                "Patrocinios: " + String.join(", ", dtEdicion.getPatrocinios()) + "\n" +
                "Tipos de Registro: " + String.join(", ", dtEdicion.getTiposDeRegistro())
            );
            titulo = "Detalle de Edición";
        } else {
            return;
        }

        // ✅ Mostrar con JOptionPane (automáticamente tiene scroll si es grande)
        JOptionPane.showMessageDialog(
            this,                    // componente padre
            new JScrollPane(area),   // contenido con scroll
            titulo,                  // título
            JOptionPane.INFORMATION_MESSAGE
        );
    }


    private String formatearEstado(EstadoEdicion estado) {
        if (estado == null) return "";
        String texto = estado.toString().toLowerCase(); 
        return texto.substring(0, 1).toUpperCase() + texto.substring(1); 
    }



}