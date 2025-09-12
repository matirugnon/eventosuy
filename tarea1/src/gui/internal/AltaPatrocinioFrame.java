package gui.internal;

import javax.swing.*;

import excepciones.EdicionNoExisteException;
import excepciones.EventoNoExisteException;

import java.awt.*;
import java.time.LocalDate;

import logica.Controladores.IControladorEvento;
import logica.Controladores.IControladorUsuario;
import logica.Controladores.IControladorRegistro;
import logica.DatatypesYEnum.DTFecha;
import logica.DatatypesYEnum.NivelPatrocinio;

public class AltaPatrocinioFrame extends JInternalFrame {
    private JComboBox<String> comboEventos;
    private JComboBox<String> comboEdiciones;
    private JComboBox<String> comboInstituciones;
    private JComboBox<NivelPatrocinio> comboNivel;
    private JComboBox<String> comboTipoRegistro;
    private JTextField txtAporte;
    private JTextField txtCantidadRegGratuitos;
    private JTextField txtCodigo;

    private IControladorEvento ctrlEvento;
    private IControladorUsuario ctrlUsuario;
    private IControladorRegistro ctrlRegistro;

    public AltaPatrocinioFrame() {
        super("Alta de Patrocinio", true, true, true, true);

        this.ctrlEvento = IControladorEvento.getInstance();
        this.ctrlUsuario = IControladorUsuario.getInstance();
        this.ctrlRegistro = IControladorRegistro.getInstance();

        setSize(650, 500);
        setLayout(new BorderLayout(10, 10));

        JPanel form = new JPanel(new GridLayout(0, 2, 10, 10));
        form.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // --- Combos y campos ---
        comboEventos = new JComboBox<>(ctrlEvento.listarEventos().toArray(new String[0]));
        comboEdiciones = new JComboBox<>();
        comboInstituciones = new JComboBox<>(ctrlUsuario.listarInstituciones().toArray(new String[0]));
        comboNivel = new JComboBox<>(NivelPatrocinio.values());
        comboTipoRegistro = new JComboBox<>();

        txtAporte = new JTextField();
        txtCantidadRegGratuitos = new JTextField();
        txtCodigo = new JTextField();

        form.add(new JLabel("Evento:"));
        form.add(comboEventos);

        form.add(new JLabel("Edición:"));
        form.add(comboEdiciones);

        form.add(new JLabel("Institución:"));
        form.add(comboInstituciones);

        form.add(new JLabel("Nivel de patrocinio:"));
        form.add(comboNivel);

        form.add(new JLabel("Aporte económico:"));
        form.add(txtAporte);

        form.add(new JLabel("Tipo de registro:"));
        form.add(comboTipoRegistro);

        form.add(new JLabel("Cant. registros gratuitos:"));
        form.add(txtCantidadRegGratuitos);

        form.add(new JLabel("Código de patrocinio:"));
        form.add(txtCodigo);

        add(form, BorderLayout.CENTER);

        // --- Botones ---
        JPanel buttons = new JPanel();
        JButton btnConfirmar = new JButton("Confirmar");
        JButton btnCancelar = new JButton("Cancelar");
        buttons.add(btnConfirmar);
        buttons.add(btnCancelar);
        add(buttons, BorderLayout.SOUTH);

        // Listeners
        comboEventos.addActionListener(e -> cargarEdiciones());
        comboEdiciones.addActionListener(e -> cargarTiposRegistro());
        btnConfirmar.addActionListener(e -> confirmarAlta());
        btnCancelar.addActionListener(e -> dispose());

        // inicializar ediciones y tipos
        cargarEdiciones();
        cargarTiposRegistro();
    }

    private void cargarEdiciones() {
        comboEdiciones.removeAllItems();
        String evento = (String) comboEventos.getSelectedItem();
        if (evento != null) {
            try {
				for (String ed : ctrlEvento.listarEdiciones(evento)) {
				    comboEdiciones.addItem(ed);
				}
			} catch (EventoNoExisteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        cargarTiposRegistro();
    }

    private void cargarTiposRegistro() {
        comboTipoRegistro.removeAllItems();
        String edicion = (String) comboEdiciones.getSelectedItem();
        if (edicion != null) {
            try {
				for (String tipo : ctrlRegistro.listarTipoRegistro(edicion)) {
				    comboTipoRegistro.addItem(tipo);
				}
			} catch (EdicionNoExisteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
    }

    private void confirmarAlta() {
        try {
            String nomEdicion = (String) comboEdiciones.getSelectedItem();
            String nomInstitucion = (String) comboInstituciones.getSelectedItem();
            NivelPatrocinio nivel = (NivelPatrocinio) comboNivel.getSelectedItem();

            double aporte = Double.parseDouble(txtAporte.getText().trim());
            String nomTipoRegistro = (String) comboTipoRegistro.getSelectedItem();
            int cantRegGrat = Integer.parseInt(txtCantidadRegGratuitos.getText().trim());
            String codigo = txtCodigo.getText().trim();

            // fecha actual
            LocalDate hoy = LocalDate.now();
            DTFecha fechaAlta = new DTFecha(hoy.getDayOfMonth(), hoy.getMonthValue(), hoy.getYear());

            // Validaciones
            if (ctrlEvento.existePatrocinio(nomEdicion, nomInstitucion)) {
                JOptionPane.showMessageDialog(this,
                        "Ya existe un patrocinio de esta institución para la edición seleccionada.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (ctrlEvento.costoSuperaAporte(nomEdicion, nomInstitucion, nomTipoRegistro, aporte, cantRegGrat)) {
                JOptionPane.showMessageDialog(this,
                        "El costo de los registros gratuitos supera el 20% del aporte económico.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (ctrlEvento.existeCodigoPatrocinioEnEdicion(nomEdicion, codigo)) {
                JOptionPane.showMessageDialog(this,
                        "El Codigo Ingresado ya existe, ingrese otro",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }



            // Alta
            ctrlEvento.altaPatrocinio(nomEdicion, nomInstitucion, nivel, aporte,
                    nomTipoRegistro, cantRegGrat, codigo, fechaAlta);

            JOptionPane.showMessageDialog(this,
                    "Patrocinio dado de alta con éxito.",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
            dispose();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Aporte y cantidad deben ser valores numéricos.",
                    "Error de formato", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al dar de alta patrocinio: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
