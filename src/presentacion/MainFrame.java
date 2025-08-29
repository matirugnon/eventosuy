package presentacion;

import javax.swing.*;
import gui.internal.*;

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
        registroAEdicion.addActionListener(e -> openInternal(new RegistroAEdicionFrame())); //cambiar a Registroaedicionf
        menuEventos.add(registroAEdicion);

        




        // Menú Instituciones
        JMenu menuInstituciones = new JMenu("Instituciones");


        JMenuItem altaIns = new JMenuItem("Alta Institución");
        //altaIns.addActionListener(e -> openInternal(new AltaInstitucionFrame()));
        menuEventos.add(altaIns);

        menuInstituciones.add(new JMenuItem());

        // Menú Patrocinios
        JMenu menuPatrocinios = new JMenu("Patrocinios");
        menuPatrocinios.add(new JMenuItem("Alta Patrocinio"));
        menuPatrocinios.add(new JMenuItem("Consulta Patrocinio"));
        
        
        
        //Menu Registros
        JMenu menuRegistros = new JMenu("Registros");
        
        
        JMenuItem altaTipoRegistro = new JMenuItem("Alta Tipo Registro");
        altaTipoRegistro.addActionListener(e -> openInternal(new AltaTipoRegistroFrame()));
        menuRegistros.add(altaTipoRegistro);

        JMenuItem consultaTipoRegistro = new JMenuItem("Consulta Tipo Registro");
        consultaTipoRegistro.addActionListener(e -> openInternal(new ConsultaTipoRegistroFrame()));
        menuRegistros.add(consultaTipoRegistro);

        JMenuItem consultaRegistro = new JMenuItem("Consulta Registro");
        consultaRegistro.addActionListener(e -> openInternal(new ConsultaRegistroFrame()));
        menuRegistros.add(consultaRegistro);
        
        
        
        


        //add menus
        menuBar.add(menuUsuarios);
        menuBar.add(menuEventos);
        menuBar.add(menuInstituciones);
        menuBar.add(menuPatrocinios);
        menuBar.add(menuRegistros);

        return menuBar;
        
        
        
    }

    private void openInternal(JInternalFrame frame) {
        desktop.add(frame);
        frame.setVisible(true);
        try { frame.setSelected(true); } catch (Exception ignored) {}
    }
}