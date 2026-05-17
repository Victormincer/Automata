package automata.view;

import automata.controller.AutomataController;
import automata.model.Automata;
import automata.model.Estado;
import automata.model.Transicion;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Ventana principal de la aplicación.
 *
 * Responsabilidades:
 * - Mostrar interfaz principal
 * - Mostrar tabla de transiciones
 * - Coordinar acciones UI
 * - Abrir diálogos
 * - Invocar controller
 */
public class MainFrame extends JFrame {

    // ─────────────────────────────────────────────
    // Controller
    // ─────────────────────────────────────────────

    private final AutomataController controller;

    // ─────────────────────────────────────────────
    // Componentes UI
    // ─────────────────────────────────────────────

    private JTable tablaTransiciones;

    private DefaultTableModel modeloTabla;

    private JTextArea areaLog;

    private JLabel lblNombreAutomata;

    private JLabel lblTipoAutomata;

    private JButton btnConvertir;

    private JButton btnDiagrama;

    // ─────────────────────────────────────────────
    // Constructor
    // ─────────────────────────────────────────────

    public MainFrame() {

        this.controller =
                new AutomataController();

        configurarVentana();

        inicializarComponentes();

        construirLayout();

        registrarEventos();

        log("Bienvenido a AutomataStudio");
    }

    // ─────────────────────────────────────────────
    // Configuración general
    // ─────────────────────────────────────────────

    private void configurarVentana() {

        setTitle("AutomataStudio");

        setSize(1100, 720);

        setMinimumSize(
                new Dimension(900, 600)
        );

        setLocationRelativeTo(null);

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        getContentPane().setBackground(
                new Color(13, 17, 23)
        );
    }

    // ─────────────────────────────────────────────
    // Inicializar componentes
    // ─────────────────────────────────────────────

    private void inicializarComponentes() {

        inicializarTabla();

        inicializarLog();

        inicializarLabels();

        inicializarBotones();
    }

    // ─────────────────────────────────────────────
    // Tabla
    // ─────────────────────────────────────────────

    private void inicializarTabla() {

        modeloTabla =
                new DefaultTableModel() {

                    @Override
                    public boolean isCellEditable(
                            int row,
                            int column
                    ) {
                        return false;
                    }
                };

        tablaTransiciones =
                new JTable(modeloTabla);

        tablaTransiciones.setFont(
                new Font(
                        "Monospaced",
                        Font.PLAIN,
                        13
                )
        );

        tablaTransiciones.setRowHeight(30);

        tablaTransiciones.setBackground(
                new Color(22, 27, 34)
        );

        tablaTransiciones.setForeground(
                new Color(230, 237, 243)
        );

        tablaTransiciones.setGridColor(
                new Color(48, 54, 61)
        );

        tablaTransiciones.setSelectionBackground(
                new Color(56, 91, 131)
        );

        JTableHeader header =
                tablaTransiciones.getTableHeader();

        header.setBackground(
                new Color(33, 38, 45)
        );

        header.setForeground(
                new Color(88, 166, 255)
        );

        header.setFont(
                new Font(
                        "Monospaced",
                        Font.BOLD,
                        12
                )
        );
    }

    // ─────────────────────────────────────────────
    // Área log
    // ─────────────────────────────────────────────

    private void inicializarLog() {

        areaLog = new JTextArea();

        areaLog.setEditable(false);

        areaLog.setLineWrap(true);

        areaLog.setFont(
                new Font(
                        "Monospaced",
                        Font.PLAIN,
                        11
                )
        );

        areaLog.setBackground(
                new Color(13, 17, 23)
        );

        areaLog.setForeground(
                new Color(139, 148, 158)
        );
    }

    // ─────────────────────────────────────────────
    // Labels
    // ─────────────────────────────────────────────

    private void inicializarLabels() {

        lblNombreAutomata =
                new JLabel("—");

        lblNombreAutomata.setFont(
                new Font(
                        "Monospaced",
                        Font.BOLD,
                        15
                )
        );

        lblNombreAutomata.setForeground(
                new Color(230, 237, 243)
        );

        lblTipoAutomata =
                new JLabel("");

        lblTipoAutomata.setFont(
                new Font(
                        "Monospaced",
                        Font.PLAIN,
                        12
                )
        );

        lblTipoAutomata.setForeground(
                new Color(63, 185, 80)
        );
    }

    // ─────────────────────────────────────────────
    // Botones
    // ─────────────────────────────────────────────

    private void inicializarBotones() {

        btnConvertir =
                crearBoton(
                        "AFND → AFD",
                        new Color(63, 185, 80)
                );

        btnDiagrama =
                crearBoton(
                        "Ver Diagrama",
                        new Color(210, 153, 34)
                );

        JButton btnCrear =
                crearBoton(
                        "Crear Autómata",
                        new Color(88, 166, 255)
                );

        // Eventos
        btnCrear.addActionListener(
                e -> abrirDialogoCrear()
        );

        btnConvertir.addActionListener(
                e -> convertirAutomata()
        );

        btnDiagrama.addActionListener(
                e -> abrirDiagrama()
        );

        btnConvertir.setEnabled(false);

        btnDiagrama.setEnabled(false);

        // Sidebar temporal
        sidebarBotones = new JPanel();
        sidebarBotones.setOpaque(false);
        sidebarBotones.setLayout(
                new BoxLayout(
                        sidebarBotones,
                        BoxLayout.Y_AXIS
                )
        );

        sidebarBotones.add(btnCrear);
        sidebarBotones.add(Box.createVerticalStrut(12));
        sidebarBotones.add(btnConvertir);
        sidebarBotones.add(Box.createVerticalStrut(12));
        sidebarBotones.add(btnDiagrama);
    }

    private JPanel sidebarBotones;

    // ─────────────────────────────────────────────
    // Layout principal
    // ─────────────────────────────────────────────

    private void construirLayout() {

        setLayout(new BorderLayout());

        add(crearHeader(), BorderLayout.NORTH);

        add(crearSidebar(), BorderLayout.WEST);

        add(crearPanelCentral(), BorderLayout.CENTER);
    }

    // ─────────────────────────────────────────────
    // Header
    // ─────────────────────────────────────────────

    private JPanel crearHeader() {

        JPanel panel =
                new JPanel(new BorderLayout());

        panel.setPreferredSize(
                new Dimension(0, 60)
        );

        panel.setBackground(
                new Color(22, 27, 34)
        );

        JLabel titulo =
                new JLabel(
                        "  ◈ AutomataStudio"
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
                        "AFND / AFD   "
                );

        subtitulo.setFont(
                new Font(
                        "Monospaced",
                        Font.PLAIN,
                        12
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
    // Sidebar
    // ─────────────────────────────────────────────

    private JPanel crearSidebar() {

        JPanel sidebar =
                new JPanel();

        sidebar.setPreferredSize(
                new Dimension(240, 0)
        );

        sidebar.setBackground(
                new Color(22, 27, 34)
        );

        sidebar.setLayout(
                new BorderLayout()
        );

        JPanel top =
                new JPanel();

        top.setOpaque(false);

        top.setLayout(
                new BoxLayout(
                        top,
                        BoxLayout.Y_AXIS
                )
        );

        top.setBorder(
                new EmptyBorder(20, 20, 20, 20)
        );

        top.add(sidebarBotones);

        top.add(Box.createVerticalStrut(30));

        top.add(lblNombreAutomata);

        top.add(Box.createVerticalStrut(5));

        top.add(lblTipoAutomata);

        sidebar.add(top, BorderLayout.NORTH);

        JScrollPane scrollLog =
                new JScrollPane(areaLog);

        scrollLog.setBorder(null);

        sidebar.add(scrollLog, BorderLayout.SOUTH);

        return sidebar;
    }

    // ─────────────────────────────────────────────
    // Panel central
    // ─────────────────────────────────────────────

    private JPanel crearPanelCentral() {

        JPanel panel =
                new JPanel(new BorderLayout());

        panel.setBackground(
                new Color(13, 17, 23)
        );

        JLabel titulo =
                new JLabel(
                        "Tabla de Transiciones"
                );

        titulo.setFont(
                new Font(
                        "Monospaced",
                        Font.BOLD,
                        15
                )
        );

        titulo.setForeground(
                new Color(230, 237, 243)
        );

        JPanel top =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT
                        )
                );

        top.setOpaque(false);

        top.add(titulo);

        JScrollPane scroll =
                new JScrollPane(
                        tablaTransiciones
                );

        scroll.setBorder(
                BorderFactory.createEmptyBorder(
                        10,
                        10,
                        10,
                        10
                )
        );

        panel.add(top, BorderLayout.NORTH);

        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    // ─────────────────────────────────────────────
    // Eventos
    // ─────────────────────────────────────────────

    private void registrarEventos() {

        // Eventos adicionales aquí
    }

    // ─────────────────────────────────────────────
    // Abrir diálogo crear
    // ─────────────────────────────────────────────

    private void abrirDialogoCrear() {

        CrearAutomataDialog dialog =
                new CrearAutomataDialog(this);

        dialog.setVisible(true);

        Automata automata =
                dialog.getAutomataCreado();

        if (automata != null) {

            controller.setAutomataActual(
                    automata
            );

            actualizarUI();

            log(
                    "Autómata creado: "
                            + automata.getNombre()
            );
        }
    }

    // ─────────────────────────────────────────────
    // Convertir AFND → AFD
    // ─────────────────────────────────────────────

    private void convertirAutomata() {

        Automata actual =
                controller.getAutomataActual();

        if (actual == null)
            return;

        if (!actual.isAFND()) {

            JOptionPane.showMessageDialog(
                    this,
                    "El autómata ya es AFD"
            );

            return;
        }

        Automata afd =
                controller.convertirAFD();

        actualizarTabla(afd);

        lblNombreAutomata.setText(
                afd.getNombre()
        );

        lblTipoAutomata.setText(
                "AFD (Convertido)"
        );

        log(
                "Conversión completada"
        );

        JOptionPane.showMessageDialog(
                this,
                "Conversión exitosa"
        );
    }

    // ─────────────────────────────────────────────
    // Abrir diagrama
    // ─────────────────────────────────────────────

    private void abrirDiagrama() {

        Automata automata =
                controller.getAutomataActual();

        if (automata == null)
            return;

        VentanaDiagrama ventana =
                new VentanaDiagrama(
                        this,
                        automata
                );

        ventana.setVisible(true);
    }

    // ─────────────────────────────────────────────
    // Actualizar UI
    // ─────────────────────────────────────────────

    private void actualizarUI() {

        Automata automata =
                controller.getAutomataActual();

        if (automata == null)
            return;

        lblNombreAutomata.setText(
                automata.getNombre()
        );

        lblTipoAutomata.setText(
                automata.isAFND()
                        ? "AFND"
                        : "AFD"
        );

        actualizarTabla(automata);

        btnConvertir.setEnabled(
                automata.isAFND()
        );

        btnDiagrama.setEnabled(true);
    }

    // ─────────────────────────────────────────────
    // Actualizar tabla
    // ─────────────────────────────────────────────

    private void actualizarTabla(
            Automata automata
    ) {

        modeloTabla.setRowCount(0);

        modeloTabla.setColumnCount(0);

        List<String> simbolos =
                new ArrayList<>(
                        automata.getAlfabeto()
                );

        modeloTabla.addColumn("Estado");

        for (String simbolo : simbolos) {

            modeloTabla.addColumn(
                    "δ(" + simbolo + ")"
            );
        }

        for (Estado estado :
                automata.getEstados()) {

            Object[] fila =
                    new Object[
                            simbolos.size() + 1
                    ];

            String prefijo = "";

            if (estado.isInicial())
                prefijo += "→ ";

            if (estado.isAceptacion())
                prefijo += "* ";

            fila[0] =
                    prefijo +
                            estado.getNombre();

            for (int i = 0; i < simbolos.size(); i++) {

                String simbolo =
                        simbolos.get(i);

                StringBuilder destinos =
                        new StringBuilder();

                for (Transicion t :
                        automata.getTransiciones()) {

                    if (t.getOrigen().equals(
                            estado.getNombre()
                    ) && t.getSimbolo().equals(
                            simbolo
                    )) {

                        if (!destinos.isEmpty()) {

                            destinos.append(", ");
                        }

                        destinos.append(
                                t.getDestino()
                        );
                    }
                }

                fila[i + 1] =
                        destinos.isEmpty()
                                ? "—"
                                : destinos.toString();
            }

            modeloTabla.addRow(fila);
        }

        configurarRendererEstados();
    }

    // ─────────────────────────────────────────────
    // Renderer colores
    // ─────────────────────────────────────────────

    private void configurarRendererEstados() {

        tablaTransiciones
                .getColumnModel()
                .getColumn(0)
                .setCellRenderer(
                        new DefaultTableCellRenderer() {

                            @Override
                            public Component
                            getTableCellRendererComponent(
                                    JTable table,
                                    Object value,
                                    boolean selected,
                                    boolean focused,
                                    int row,
                                    int column
                            ) {

                                Component c =
                                        super
                                                .getTableCellRendererComponent(
                                                        table,
                                                        value,
                                                        selected,
                                                        focused,
                                                        row,
                                                        column
                                                );

                                String texto =
                                        value.toString();

                                if (texto.contains("*")) {

                                    c.setForeground(
                                            new Color(
                                                    63,
                                                    185,
                                                    80
                                            )
                                    );

                                } else if (texto.contains("→")) {

                                    c.setForeground(
                                            new Color(
                                                    88,
                                                    166,
                                                    255
                                            )
                                    );

                                } else {

                                    c.setForeground(
                                            new Color(
                                                    230,
                                                    237,
                                                    243
                                            )
                                    );
                                }

                                return c;
                            }
                        }
                );
    }

    // ─────────────────────────────────────────────
    // Botón reutilizable
    // ─────────────────────────────────────────────

    private JButton crearBoton(
            String texto,
            Color color
    ) {

        JButton btn =
                new JButton(texto);

        btn.setFont(
                new Font(
                        "Monospaced",
                        Font.BOLD,
                        13
                )
        );

        btn.setForeground(color);

        btn.setFocusPainted(false);

        btn.setCursor(
                Cursor.getPredefinedCursor(
                        Cursor.HAND_CURSOR
                )
        );

        btn.setBackground(
                new Color(
                        color.getRed(),
                        color.getGreen(),
                        color.getBlue(),
                        30
                )
        );

        btn.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                color
                        ),
                        new EmptyBorder(
                                8,
                                12,
                                8,
                                12
                        )
                )
        );

        btn.setMaximumSize(
                new Dimension(180, 42)
        );

        return btn;
    }

    // ─────────────────────────────────────────────
    // Logger
    // ─────────────────────────────────────────────

    private void log(String mensaje) {

        areaLog.append(
                mensaje
                        + "\n---------------------\n"
        );

        areaLog.setCaretPosition(
                areaLog.getDocument().getLength()
        );
    }
}