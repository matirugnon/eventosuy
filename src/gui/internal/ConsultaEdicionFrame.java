package gui.internal;

import javax.swing.*;
import java.awt.*;

@SuppressWarnings("serial")
public class ConsultaEdicionFrame extends JInternalFrame {
    public ConsultaEdicionFrame(String nombreEdicion, String sigla, String evento) {
        super("Consulta de Edición", true, true, true, true);
        setSize(480, 300);
        setLayout(new BorderLayout());
        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setText(
            "Edición: " + nombreEdicion + "\n" +
            "Sigla: "   + sigla         + "\n" +
            "Evento: "  + evento        + "\n" +
            "Ciudad: Montevideo (mock)\n" +
            "País: Uruguay (mock)\n" +
            "Fecha Inicio: 2025-09-01 (mock)\n" +
            "Fecha Fin: 2025-09-03 (mock)\n" +
            "Tipos de registro: General, Estudiante (mock)"
        );
        add(new JScrollPane(area), BorderLayout.CENTER);
    }
}
