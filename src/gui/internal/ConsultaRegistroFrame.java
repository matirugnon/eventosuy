package gui.internal;

import javax.swing.*;
import java.awt.*;

@SuppressWarnings("serial")
public class ConsultaRegistroFrame extends JInternalFrame {
    public ConsultaRegistroFrame(String registroId, String evento, String edicion) {
        super("Consulta de Registro", true, true, true, true);
        setSize(480, 260);
        setLayout(new BorderLayout());
        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setText(
            "Registro: " + registroId + "\n" +
            "Evento: "   + evento     + "\n" +
            "Edición: "  + edicion    + "\n" +
            "Tipo: Estudiante (mock)\n" +
            "Costo: 500 (mock)\n" +
            "Fecha Registro: 2025-08-20 (mock)"
        );
        add(new JScrollPane(area), BorderLayout.CENTER);
    }
}
