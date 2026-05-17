package automata.view;

import automata.model.Automata;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Ventana encargada de mostrar
 * el diagrama visual del autómata.
 *
 * Contiene:
 * - Panel gráfico del autómata
 * - Leyenda visual
 * - Configuración visual
 */
public class VentanaDiagrama extends JDialog {

    // ─────────────────────────────────────────────
    // Modelo
    // ─────────────────────────────────────────────

    private final Automata automata;

    // ─────────────────────────────────────────────
    // Constructor
    // ─────────────────────────────────────────────

    public VentanaDiagrama(
            Frame owner,
            Automata automata
    ) {

        super(
                owner,
                "Diagrama — " + automata.getNombre(),
                false
        );

        this.automata = automata;

        configurarVentana();

        inicializarComponentes();

        construirLayout();
    }

    // ─────────────────────────────────────────────
    // Configuración ventana
    // ─────────────────────────────────────────────

    private void configurarVentana() {

        setSize(950, 700);

        setLocationRelativeTo(getOwner());

        setMinimumSize(
                new Dimension(700, 500)
        );

        getContentPane().setBackground(
                new Color(13, 17, 23)
        );

        setLayout(new BorderLayout());
    }

    // ─────────────────────────────────────────────
    // Inicialización
    // ─────────────────────────────────────────────

    private void inicializarComponentes() {

        // Componentes futuros
    }

    // ─────────────────────────────────────────────
    // Construcción Layout
    // ─────────────────────────────────────────────

    private void construirLayout() {

        add(crearHeader(), BorderLayout.NORTH);

        add(crearPanelDiagrama(), BorderLayout.CENTER);

        add(crearLeyenda(), BorderLayout.SOUTH);
    }

    // ─────────────────────────────────────────────
    // Header superior
    // ─────────────────────────────────────────────

    private JPanel crearHeader() {

        JPanel panel =
                new JPanel(
                        new BorderLayout()
                );

        panel.setPreferredSize(
                new Dimension(0, 60)
        );

        panel.setBackground(
                new Color(22, 27, 34)
        );

        panel.setBorder(
                BorderFactory.createMatteBorder(
                        0,
                        0,
                        1,
                        0,
                        new Color(48, 54, 61)
                )
        );

        JLabel titulo =
                new JLabel(
                        "  ◎ Diagrama del Autómata"
                );

        titulo.setFont(
                new Font(
                        "Monospaced",
                        Font.BOLD,
                        18
                )
        );

        titulo.setForeground(
                new Color(88, 166, 255)
        );

        JLabel subtitulo =
                new JLabel(
                        automata.getNombre() + "   "
                );

        subtitulo.setFont(
                new Font(
                        "Monospaced",
                        Font.PLAIN,
                        13
                )
        );

        subtitulo.setForeground(
                new Color(139, 148, 158)
        );

        panel.add(titulo, BorderLayout.WEST);

        panel.add(subtitulo, BorderLayout.EAST);

        return panel;
    }

    // ─────────────────────────────────────────────
    // Panel gráfico
    // ─────────────────────────────────────────────

    private JScrollPane crearPanelDiagrama() {

        DiagramaPanel diagramaPanel =
                new DiagramaPanel(automata);

        diagramaPanel.setPreferredSize(
                new Dimension(1400, 1000)
        );

        JScrollPane scroll =
                new JScrollPane(diagramaPanel);

        scroll.setBorder(null);

        scroll.getViewport().setBackground(
                new Color(13, 17, 23)
        );

        // Velocidad scroll
        scroll.getVerticalScrollBar()
                .setUnitIncrement(14);

        scroll.getHorizontalScrollBar()
                .setUnitIncrement(14);

        return scroll;
    }

    // ─────────────────────────────────────────────
    // Leyenda
    // ─────────────────────────────────────────────

    private JPanel crearLeyenda() {

        JPanel panel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT,
                                20,
                                10
                        )
                );

        panel.setBackground(
                new Color(22, 27, 34)
        );

        panel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(
                                1,
                                0,
                                0,
                                0,
                                new Color(48, 54, 61)
                        ),
                        new EmptyBorder(
                                5,
                                10,
                                5,
                                10
                        )
                )
        );

        panel.add(
                crearItemLeyenda(
                        "→ Estado inicial",
                        new Color(88, 166, 255)
                )
        );

        panel.add(
                crearItemLeyenda(
                        "◎ Estado aceptación",
                        new Color(63, 185, 80)
                )
        );

        panel.add(
                crearItemLeyenda(
                        "○ Estado normal",
                        new Color(139, 148, 158)
                )
        );

        panel.add(
                crearItemLeyenda(
                        "↺ Self-loop",
                        new Color(210, 160, 50)
                )
        );

        panel.add(
                crearItemLeyenda(
                        "⇄ Transición bidireccional",
                        new Color(180, 190, 200)
                )
        );

        return panel;
    }

    // ─────────────────────────────────────────────
    // Item leyenda
    // ─────────────────────────────────────────────

    private JLabel crearItemLeyenda(
            String texto,
            Color color
    ) {

        JLabel label =
                new JLabel(texto);

        label.setFont(
                new Font(
                        "Monospaced",
                        Font.PLAIN,
                        12
                )
        );

        label.setForeground(color);

        return label;
    }
}