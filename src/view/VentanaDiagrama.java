package view;

import model.Automata;
import utils.UIStyle;

import javax.swing.*;
import java.awt.*;

/*---------------------------------------------------------------------------
PROPOSITO: Ventana para mostrar el diagrama de burbuja del autómata. 
           Se muestra un panel central con el diagrama y una barra de leyenda 
           inferior que explica los símbolos usados.
FECHA: 2026-05-16
AUTOR: Victor Alfonso Pardo Gutierrez - Maryury Hernandez Marin
VERSION: 1.3
ACTUALIZACIÓN: Se agregó la barra de leyenda inferior para explicar los símbolos del diagrama.
-----------------------------------------------------------------------------*/
public class VentanaDiagrama extends JDialog {
    /*---------------------------------------------------------------------------
    PROPOSITO: Constructor que inicializa la ventana con el diagrama de burbuja 
               del autómata dado. 
               Configura el tamaño, el título, el layout y agrega el panel del diagrama 
                y la barra de leyenda.
    FECHA: 2026-05-16
    AUTOR: Victor Alfonso Pardo Gutierrez - Maryury Hernandez Marin
    VERSION: 1.3
    -----------------------------------------------------------------------------*/
    public VentanaDiagrama(Frame owner, Automata automata) {
        super(owner, "Diagrama de Burbuja — " + automata.nombre, false);
        setSize(860, 640);
        setMinimumSize(new Dimension(640, 480));
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());

        getContentPane().setBackground(UIStyle.BG);

        // Panel central con el diagrama
        DiagramaPanel panelDiagrama = new DiagramaPanel(automata);
        add(panelDiagrama, BorderLayout.CENTER);

        // Barra de leyenda inferior
        add(crearLeyenda(), BorderLayout.SOUTH);
    }

    /*---------------------------------------------------------------------------
    PROPOSITO: Crea un panel de leyenda que explica los símbolos usados en el 
                diagrama de burbuja. 
               Cada símbolo se muestra con su respectivo color y descripción.
    FECHA: 2026-05-16
    AUTOR: Victor Alfonso Pardo Gutierrez - Maryury Hernandez Marin
    VERSION: 1.3
    -----------------------------------------------------------------------------*/
    private JPanel crearLeyenda() {
        JPanel leyenda = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 6));
        leyenda.setBackground(UIStyle.SURFACE);
        leyenda.setBorder(UIStyle.borderMatte(1, 0, 0, 0));

        leyenda.add(item("\u2192  Estado inicial", UIStyle.ACCENT));
        leyenda.add(item("\u25CE  Estado de aceptación", UIStyle.ACCENT_G));
        leyenda.add(item("\u25CB  Estado normal", UIStyle.FG_MUTED));
        leyenda.add(item("\u2717  Error \u00D8", UIStyle.ACCENT_R));
        leyenda.add(item("---  Transición a error", UIStyle.ACCENT_R.darker()));

        return leyenda;
    }

    private JLabel item(String texto, Color color) {
        JLabel l = new JLabel(texto);
        l.setFont(UIStyle.FONT_MONO_SMALL);
        l.setForeground(color);
        return l;
    }
}
