package presentacion;

import javax.swing.*;

import excepciones.CategoriaNoSeleccionadaException;
import excepciones.CorreoInvalidoException;
import excepciones.EdicionExistenteException;
import excepciones.EdicionNoExisteException;
import excepciones.EventoNoExisteException;
import excepciones.EventoRepetidoException;
import excepciones.ExisteInstitucionException;
import excepciones.FechaInvalidaException;
import excepciones.FechasIncompatiblesException;
import excepciones.NombreTipoRegistroDuplicadoException;
import excepciones.PatrocinioDuplicadoException;
import excepciones.SiglaRepetidaException;
import excepciones.UsuarioNoExisteException;
import excepciones.UsuarioRepetidoException;
import excepciones.UsuarioYaRegistradoEnEdicionException;
import gui.internal.AceptarRechazarEdicionFrame;
import gui.internal.AltaEdicionFrame;
import gui.internal.AltaEventoFrame;
import gui.internal.AltaInstitucionFrame;
import gui.internal.AltaPatrocinioFrame;
import gui.internal.AltaTipoRegistroFrame;
import gui.internal.AltaUsuarioFrame;
import gui.internal.ConsultaEdicionFrame;
import gui.internal.ConsultaEventoFrame;
import gui.internal.ConsultaPatrocinioFrame;
import gui.internal.ConsultaRegistroFrame;
import gui.internal.ConsultaTipoRegistroFrame;
import gui.internal.ModificarUsuarioFrame;
import gui.internal.RegistroAEdicionFrame;


import logica.controladores.IControladorEvento;
import logica.controladores.IControladorRegistro;
import logica.controladores.IControladorUsuario;
import utils.Utils;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {
    private JDesktopPane desktop;

    public MainFrame() {
        setTitle("Estación de Trabajo - eventos.uy");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        desktop = new JDesktopPane();
        setContentPane(desktop);

        setJMenuBar(createMenuBar());
    }



    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // ----------------Casos de uso Usuarios--------------------
        JMenu menuUsuarios = new JMenu("Usuarios");

        JMenuItem altaUsuario = new JMenuItem("Alta Usuario");
        altaUsuario.addActionListener(e -> {
            AltaUsuarioFrame frame = new AltaUsuarioFrame();
            frame.setLocation(23, 11);
            openInternal(frame);
        });


        JMenuItem consultaUsuario = new JMenuItem("Consulta Usuario");
        consultaUsuario.addActionListener(e -> openInternal(new gui.internal.ConsultaUsuarioFrame(this::openInternal)));
        menuUsuarios.add(consultaUsuario);


        JMenuItem modificarUsuario = new JMenuItem("Modificar Usuario");
        modificarUsuario.addActionListener(e -> openInternal(new ModificarUsuarioFrame()));
        menuUsuarios.add(altaUsuario);
        menuUsuarios.add(consultaUsuario);
        menuUsuarios.add(modificarUsuario);


        // ----------------Casos de uso Eventos--------------------
        JMenu menuEventos = new JMenu("Eventos");


        JMenuItem altaEvento = new JMenuItem("Alta Evento");
        altaEvento.addActionListener(e -> openInternal(new AltaEventoFrame()));
        menuEventos.add(altaEvento);


        JMenuItem consultaEvento = new JMenuItem("Consulta Evento");
        consultaEvento.addActionListener(e -> openInternal(new ConsultaEventoFrame()));
        menuEventos.add(consultaEvento);


        JMenuItem altaEdicion = new JMenuItem("Alta Edición");
        altaEdicion.addActionListener(e -> openInternal(new AltaEdicionFrame()));
        menuEventos.add(altaEdicion);


        JMenuItem consultaEdicion = new JMenuItem("Consulta Edición");
        consultaEdicion.addActionListener(e -> openInternal(new ConsultaEdicionFrame()));
        menuEventos.add(consultaEdicion);



        JMenuItem registroAEdicion = new JMenuItem("Registro a Edición");
        registroAEdicion.addActionListener(e -> {
			try {
				openInternal(new RegistroAEdicionFrame());
			} catch (EventoNoExisteException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
        menuEventos.add(registroAEdicion);
        
        
        JMenuItem aceptarRechazarEdicion = new JMenuItem("Aceptar/Rechazar Edición");
        aceptarRechazarEdicion.addActionListener(e -> openInternal(new AceptarRechazarEdicionFrame()));
        menuEventos.add(aceptarRechazarEdicion);


        // Menú Instituciones
        JMenu menuInstituciones = new JMenu("Instituciones");
        JMenuItem altaIns = new JMenuItem("Alta Institución");
        altaIns.addActionListener(e -> openInternal(new AltaInstitucionFrame()));
        menuInstituciones.add(altaIns);


        // Menú Patrocinios
        JMenu menuPatrocinios = new JMenu("Patrocinios");


        JMenuItem altaPatrocinio = new JMenuItem("Alta Patrocinio");
        altaPatrocinio.addActionListener(e -> openInternal(new AltaPatrocinioFrame()));
        menuPatrocinios.add(altaPatrocinio);


        JMenuItem consultaPatrocinio = new JMenuItem("Consulta Patrocinio");
        consultaPatrocinio.addActionListener(e -> openInternal(new ConsultaPatrocinioFrame()));
        menuPatrocinios.add(consultaPatrocinio);


        //Menu Registros
        JMenu menuRegistros = new JMenu("Registros");


        JMenuItem altaTipoRegistro = new JMenuItem("Alta Tipo Registro");
        altaTipoRegistro.addActionListener(e -> openInternal(new AltaTipoRegistroFrame()));
        menuRegistros.add(altaTipoRegistro);

        JMenuItem consultaTipoRegistro = new JMenuItem("Consulta Tipo Registro");
        consultaTipoRegistro.addActionListener(e -> {
			try {
				openInternal(new ConsultaTipoRegistroFrame());
			} catch (EventoNoExisteException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
        menuRegistros.add(consultaTipoRegistro);

        JMenuItem consultaRegistro = new JMenuItem("Consulta Registro");
        consultaRegistro.addActionListener(e -> openInternal(new ConsultaRegistroFrame()));
        menuRegistros.add(consultaRegistro);


        //menu cargar datos
        JMenu cargaDatosMenu = new JMenu("Cargar Datos");
        JMenuItem cargarDatos = new JMenuItem("Click para cargar datos");


        IControladorUsuario contrU = IControladorUsuario.getInstance();
        IControladorEvento contE =  IControladorEvento.getInstance();
        IControladorRegistro contR = IControladorRegistro.getInstance();

        cargarDatos.addActionListener(e -> {

            try {
				Utils.cargarDatos(contrU,contE,contR);
				JOptionPane.showMessageDialog(this, "Datos cargados exitosamente");
			} catch (UsuarioRepetidoException | EdicionNoExisteException |
					CorreoInvalidoException |
					EventoRepetidoException |
					SiglaRepetidaException |
					FechaInvalidaException |
					ExisteInstitucionException |
					EdicionExistenteException |
					FechasIncompatiblesException |
					NombreTipoRegistroDuplicadoException | UsuarioNoExisteException |
					UsuarioYaRegistradoEnEdicionException | CategoriaNoSeleccionadaException |
					PatrocinioDuplicadoException e1) {
				e1.printStackTrace();
			}//excepciones a nivel de logica
        });

        cargaDatosMenu.add(cargarDatos);




        //add menus
        menuBar.add(menuUsuarios);
        menuBar.add(menuEventos);
        menuBar.add(menuInstituciones);
        menuBar.add(menuPatrocinios);
        menuBar.add(menuRegistros);
        menuBar.add(cargaDatosMenu);
        return menuBar;



    }

    private void openInternal(JInternalFrame frame) {
        desktop.add(frame);
        frame.setVisible(true);
        try { frame.setSelected(true); } catch (Exception ignored) {}
    }
}