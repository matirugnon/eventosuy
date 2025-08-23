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

        // Menú Usuarios
        JMenu menuUsuarios = new JMenu("Usuarios");

        JMenuItem altaUsuario = new JMenuItem("Alta Usuario");
        AltaUsuarioFrame altaUsuarioFrame = new AltaUsuarioFrame();
        altaUsuarioFrame.setLocation(23, 11);
        altaUsuario.addActionListener(e -> openInternal(altaUsuarioFrame));


        JMenuItem consultaUsuario = new JMenuItem("Consulta Usuario");
        consultaUsuario.addActionListener(e -> openInternal(new gui.internal.ConsultaUsuarioFrame(this::openInternal)));
        menuUsuarios.add(consultaUsuario);


        JMenuItem modificarUsuario = new JMenuItem("Modificar Usuario");
        modificarUsuario.addActionListener(e -> openInternal(new ModificarUsuarioFrame()));
        menuUsuarios.add(altaUsuario);
        menuUsuarios.add(consultaUsuario);
        menuUsuarios.add(modificarUsuario);


        // Menú Eventos
        JMenu menuEventos = new JMenu("Eventos");


        JMenuItem altaEvento = new JMenuItem("Alta Evento");
        altaEvento.addActionListener(e -> openInternal(new AltaEventoFrame()));
        menuEventos.add(altaEvento);


        JMenuItem consultaEvento = new JMenuItem("Consulta Evento");
        consultaEvento.addActionListener(e -> openInternal(new ConsultaEventoFrame()));
        menuEventos.add(consultaEvento);



        menuEventos.add(new JMenuItem("Alta Edición"));
        menuEventos.add(new JMenuItem("Consulta Edición"));
        menuEventos.add(new JMenuItem("Alta Tipo Registro"));
        menuEventos.add(new JMenuItem("Consulta Tipo Registro"));
        menuEventos.add(new JMenuItem("Registro a Edición"));
        menuEventos.add(new JMenuItem("Consulta Registro"));

        // Menú Instituciones
        JMenu menuInstituciones = new JMenu("Instituciones");
        menuInstituciones.add(new JMenuItem("Alta Institución"));

        // Menú Patrocinios
        JMenu menuPatrocinios = new JMenu("Patrocinios");
        menuPatrocinios.add(new JMenuItem("Alta Patrocinio"));
        menuPatrocinios.add(new JMenuItem("Consulta Patrocinio"));

        menuBar.add(menuUsuarios);
        menuBar.add(menuEventos);
        menuBar.add(menuInstituciones);
        menuBar.add(menuPatrocinios);

        return menuBar;
    }

    private void openInternal(JInternalFrame frame) {
        desktop.add(frame);
        frame.setVisible(true);
        try { frame.setSelected(true); } catch (Exception ignored) {}
    }
}