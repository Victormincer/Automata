package view;

import controller.AutomataController;
import model.Automata;
import model.Estado;
import model.Transicion;
import utils.UIStyle;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.*;
import java.util.List;

/*---------------------------------------------------------------------------
PROPOSITO: Ventana principal de la aplicación, muestra la tabla de 
           transiciones del autómata activo, y permite crear nuevos autómatas, 
           convertir AFND a AFD y ver el diagrama del autómata.
FECHA: 2026-05-16
AUTOR: Victor Alfonso Pardo Gutierrez - Maryury Hernandez Marin
VERSION: 1.3
ACTUALIZACIÓN: 2026-05-23 - Se mejoró el diseño de la ventana, 
                            con mejores colores, fuentes y estilos de los componentes.
-----------------------------------------------------------------------------*/
public class MainFrame extends JFrame {

    private final AutomataController ctrl = new AutomataController();

    // Componentes dinámicos
    private JLabel lblNombre;
    private JLabel lblTipo;
    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private JTextArea areaLog;
    private JButton btnConvertir;
    private JButton btnDiagrama;

    /*---------------------------------------------------------------------------
    PROPOSITO: Constructor de la ventana principal, configura la ventana y llama a initUI() para 
           inicializar los componentes gráficos.
    FECHA: 2026-05-16
    AUTOR: Victor Alfonso Pardo Gutierrez - Maryury Hernandez Marin
    VERSION: 1.3
    -----------------------------------------------------------------------------*/
    public MainFrame() {
        setTitle("AutomataStudio — AFD / AFND");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1050, 700);
        setMinimumSize(new Dimension(820, 560));
        setLocationRelativeTo(null);
        initUI();
    }

    /*---------------------------------------------------------------------------
    PROPOSITO: Inicializa los componentes gráficos de la ventana principal, creando el header, sidebar y panel principal. 
               También configura el área de log para mostrar mensajes al usuario.
    FECHA: 2026-05-16
    AUTOR: Victor Alfonso Pardo Gutierrez - Maryury Hernandez Marin
    VERSION: 1.3
    -----------------------------------------------------------------------------*/
    private void initUI() {
        getContentPane().setBackground(UIStyle.BG);
        setLayout(new BorderLayout());
        add(crearHeader(), BorderLayout.NORTH);
        add(crearSidebar(), BorderLayout.WEST);
        add(crearMain(), BorderLayout.CENTER);
        log("Bienvenido a AutomataStudio.\nCrea un autómata para comenzar.");
    }

    /*---------------------------------------------------------------------------
    PROPOSITO: Crea el header de la ventana principal, 
               que incluye el título de la aplicación y una breve descripción. 
               El header tiene un fondo oscuro y utiliza colores 
               de acento para resaltar el título.
    FECHA: 2026-05-16
    AUTOR: Victor Alfonso Pardo Gutierrez - Maryury Hernandez Marin
    VERSION: 1.3
    -----------------------------------------------------------------------------*/
    private JPanel crearHeader() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(UIStyle.SURFACE);
        p.setBorder(UIStyle.borderMatte(0, 0, 1, 0));
        p.setPreferredSize(new Dimension(0, 56));

        JLabel titulo = UIStyle.crearLabel("  \u25C8  AutomataStudio",
                UIStyle.ACCENT, UIStyle.FONT_MONO_TITLE);
        JLabel sub = UIStyle.crearLabel("Autómatas Finitos — AFD / AFND   ",
                UIStyle.FG_MUTED, UIStyle.FONT_MONO_SMALL);
        p.add(titulo, BorderLayout.WEST);
        p.add(sub, BorderLayout.EAST);
        return p;
    }

    /*---------------------------------------------------------------------------
    PROPOSITO: Constructor de la ventana principal, configura la ventana y llama a initUI() para 
           inicializar los componentes gráficos.
    FECHA: 2026-05-16
    AUTOR: Victor Alfonso Pardo Gutierrez - Maryury Hernandez Marin
    VERSION: 1.3
    -----------------------------------------------------------------------------*/

    private JPanel crearSidebar() {
        JPanel sb = new JPanel();
        sb.setLayout(new BoxLayout(sb, BoxLayout.Y_AXIS));
        sb.setBackground(UIStyle.SURFACE);
        sb.setBorder(UIStyle.borderMatte(0, 0, 0, 1));
        sb.setPreferredSize(new Dimension(224, 0));

        sb.add(Box.createVerticalStrut(20));

        // Botón Crear
        JButton btnCrear = UIStyle.crearBotonSidebar("\uff0b  Crear Autómata", UIStyle.ACCENT);
        btnCrear.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnCrear.addActionListener(e -> abrirDialogoCrear());
        sb.add(btnCrear);

        sb.add(Box.createVerticalStrut(10));

        // Botón Convertir
        btnConvertir = UIStyle.crearBotonSidebar("\u21C4  AFND \u2192 AFD", UIStyle.ACCENT_G);
        btnConvertir.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnConvertir.setEnabled(false);
        btnConvertir.addActionListener(e -> convertirAFD());
        sb.add(btnConvertir);

        sb.add(Box.createVerticalStrut(10));

        // Botón Diagrama
        btnDiagrama = UIStyle.crearBotonSidebar("\u25CE  Ver Diagrama", UIStyle.ACCENT_Y);
        btnDiagrama.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnDiagrama.setEnabled(false);
        btnDiagrama.addActionListener(e -> abrirDiagrama());
        sb.add(btnDiagrama);

        sb.add(Box.createVerticalStrut(22));

        // Separador
        JSeparator sep = new JSeparator();
        sep.setMaximumSize(new Dimension(184, 1));
        sep.setForeground(UIStyle.BORDER);
        sb.add(sep);
        sb.add(Box.createVerticalStrut(14));

        // Info autómata activo
        sb.add(crearPanelInfo());

        sb.add(Box.createVerticalGlue());

        // Log
        areaLog = new JTextArea(6, 18);
        areaLog.setEditable(false);
        areaLog.setFont(UIStyle.FONT_MONO_TINY);
        areaLog.setBackground(UIStyle.BG);
        areaLog.setForeground(UIStyle.FG_MUTED);
        areaLog.setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8));
        areaLog.setLineWrap(true);

        JScrollPane spLog = new JScrollPane(areaLog);
        spLog.setBorder(UIStyle.borderMatte(1, 0, 0, 0));
        spLog.setMaximumSize(new Dimension(224, 155));
        spLog.setPreferredSize(new Dimension(224, 155));
        sb.add(spLog);

        return sb;
    }

    /*---------------------------------------------------------------------------
    PROPOSITO: Crea el panel de información del autómata activo, 
               que muestra el nombre y tipo del autómata actualmente seleccionado. 
               El panel tiene un fondo claro y utiliza colores de acento para 
               resaltar el tipo del autómata.
    FECHA: 2026-05-16
    AUTOR: Victor Alfonso Pardo Gutierrez - Maryury Hernandez Marin
    VERSION: 1.3
    -----------------------------------------------------------------------------*/
    private JPanel crearPanelInfo() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(UIStyle.SURFACE);
        p.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 10));
        p.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lbl = UIStyle.crearLabel("Autómata activo", UIStyle.FG_MUTED, UIStyle.FONT_MONO_SMALL);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.add(lbl);
        p.add(Box.createVerticalStrut(6));

        lblNombre = UIStyle.crearLabel("\u2014", UIStyle.FG, UIStyle.FONT_MONO_HEADER);
        lblNombre.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.add(lblNombre);

        lblTipo = UIStyle.crearLabel("", UIStyle.ACCENT_G, UIStyle.FONT_MONO_SMALL);
        lblTipo.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.add(lblTipo);

        return p;
    }

    /*---------------------------------------------------------------------------
    PROPOSITO: Crea el panel principal que contiene la tabla de transiciones del autómata activo. 
               El panel tiene un fondo claro y utiliza colores de acento para resaltar 
               el título de la tabla. La tabla se configura para mostrar las transiciones 
               de manera clara y legible, con estilos personalizados para las celdas.
    FECHA: 2026-05-16
    AUTOR: Victor Alfonso Pardo Gutierrez - Maryury Hernandez Marin
    VERSION: 1.3
    -----------------------------------------------------------------------------*/
    private JPanel crearMain() {
        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(UIStyle.BG);

        // Cabecera de la tabla
        JPanel hdr = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 10));
        hdr.setBackground(UIStyle.BG);
        hdr.add(UIStyle.crearLabel("Tabla de Transiciones", UIStyle.FG, UIStyle.FONT_MONO_HEADER));
        main.add(hdr, BorderLayout.NORTH);

        // Tabla
        modeloTabla = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        tabla = new JTable(modeloTabla);
        tabla.setBackground(UIStyle.SURFACE);
        tabla.setForeground(UIStyle.FG);
        tabla.setGridColor(UIStyle.BORDER);
        tabla.setFont(UIStyle.FONT_MONO_BODY);
        tabla.setRowHeight(32);
        tabla.setShowGrid(true);
        tabla.setSelectionBackground(UIStyle.SEL_BG);
        tabla.setSelectionForeground(Color.WHITE);
        tabla.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        JTableHeader th = tabla.getTableHeader();
        th.setBackground(new Color(28, 33, 42));
        th.setForeground(UIStyle.ACCENT);
        th.setFont(UIStyle.FONT_MONO_LABEL);
        th.setBorder(UIStyle.borderMatte(0, 0, 1, 0));

        JScrollPane sp = new JScrollPane(tabla);
        sp.setBackground(UIStyle.BG);
        sp.getViewport().setBackground(UIStyle.SURFACE);
        sp.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        main.add(sp, BorderLayout.CENTER);

        return main;
    }

    /*---------------------------------------------------------------------------
    PROPOSITO: Abre el diálogo para crear un nuevo autómata. 
               El diálogo se muestra de forma modal, bloqueando la interacción 
               con la ventana principal hasta que se cierre. 
               Al crear un nuevo autómata, se actualiza la interfaz para 
               mostrar su información y se registra un mensaje en el log.
    FECHA: 2026-05-16
    AUTOR: Victor Alfonso Pardo Gutierrez - Maryury Hernandez Marin
    VERSION: 1.3
    -----------------------------------------------------------------------------*/
    private void abrirDialogoCrear() {
        new CrearAutomataDialog(this, ctrl, a -> {
            actualizarUI(a, false);
            log("Autómata '" + a.nombre + "' creado (" + (a.esAFND ? "AFND" : "AFD") + ")\n"
                    + "Estados: " + a.estados.size()
                    + "  |  Transiciones: " + a.transiciones.size());
        }).setVisible(true);
    }

    /*---------------------------------------------------------------------------
    PROPOSITO: Convierte el autómata activo de AFND a AFD. 
               Si la conversión es exitosa, se actualiza la interfaz para mostrar 
               el nuevo AFD, 
               y se registra un mensaje en el log con información 
               sobre el AFD generado. 
               Si ocurre un error durante la conversión, se muestra 
               un mensaje de error al usuario.
    FECHA: 2026-05-16
    AUTOR: Victor Alfonso Pardo Gutierrez - Maryury Hernandez Marin
    VERSION: 1.3
    -----------------------------------------------------------------------------*/
    private void convertirAFD() {
        try {
            Automata afd = ctrl.convertirAFD();
            actualizarUI(afd, true);
            log("Conversión completada.\nAFD: '" + afd.nombre + "'\n"
                    + "Estados: " + afd.estados.size()
                    + "  |  Transiciones: " + afd.transiciones.size());
            JOptionPane.showMessageDialog(this,
                    "¡Conversión exitosa!\nAFD generado con "
                            + afd.estados.size() + " estado(s).",
                    "AFND \u2192 AFD", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /*---------------------------------------------------------------------------
    PROPOSITO: Abre la ventana de diagrama para el autómata activo. 
               Si no hay un autómata válido para mostrar, la función no hace nada. 
               La ventana de diagrama muestra una representación visual del autómata, 
               con estados, transiciones y etiquetas claramente diferenciados.
    FECHA: 2026-05-16
    AUTOR: Victor Alfonso Pardo Gutierrez - Maryury Hernandez Marin
    VERSION: 1.3
    -----------------------------------------------------------------------------*/
    private void abrirDiagrama() {
        Automata a = ctrl.getAutomataParaDiagrama();
        if (a == null)
            return;
        new VentanaDiagrama(this, a).setVisible(true);
    }

    /*---------------------------------------------------------------------------
    PROPOSITO: Actualiza la interfaz para mostrar la información del autómata activo. 
               Se actualizan el nombre y tipo del autómata en el sidebar, 
               y se reconstruye la tabla de transiciones en el panel principal. 
               El parámetro esConvertido indica si el autómata mostrado es un AFD generado 
               a partir de un AFND, lo que afecta cómo se muestra el tipo del autómata.
    FECHA: 2026-05-16
    AUTOR: Victor Alfonso Pardo Gutierrez - Maryury Hernandez Marin
    VERSION: 1.3
    -----------------------------------------------------------------------------*/
    private void actualizarUI(Automata a, boolean esConvertido) {
        lblNombre.setText(a.nombre);
        lblTipo.setText(esConvertido ? "AFD (convertido)"
                : (a.esAFND ? "AFND" : "AFD"));
        lblTipo.setForeground(a.esAFND ? UIStyle.ACCENT_Y : UIStyle.ACCENT_G);
        actualizarTabla(a);
        btnConvertir.setEnabled(ctrl.esAFND() && !esConvertido);
        btnDiagrama.setEnabled(true);
    }

    /*---------------------------------------------------------------------------
    PROPOSITO: Actualiza la tabla de transiciones para mostrar las transiciones del autómata dado. 
               La tabla se reconstruye completamente, eliminando las filas y columnas anteriores, 
               y agregando nuevas filas y columnas según los estados y símbolos del autómata. 
               Se aplican renderers personalizados para mejorar la legibilidad de la tabla, 
               destacando los estados de error, el estado inicial y los estados de aceptación.
    FECHA: 2026-05-16
    AUTOR: Victor Alfonso Pardo Gutierrez - Maryury Hernandez Marin
    VERSION: 1.3
    -----------------------------------------------------------------------------*/
    private void actualizarTabla(Automata a) {
        modeloTabla.setRowCount(0);
        modeloTabla.setColumnCount(0);

        List<String> simbolos = new ArrayList<>(a.alfabeto);
        simbolos.remove("ε");

        modeloTabla.addColumn("Estado");
        for (String s : simbolos)
            modeloTabla.addColumn("\u03B4(" + s + ")");

        for (Estado est : a.estados) {
            Object[] fila = new Object[1 + simbolos.size()];
            fila[0] = est.etiquetaTabla();

            for (int i = 0; i < simbolos.size(); i++) {
                String sym = simbolos.get(i);
                StringBuilder sb = new StringBuilder();
                for (Transicion t : a.transiciones) {
                    if (t.estadoOrigen.equals(est.nombre) && t.simbolo.equals(sym)) {
                        if (sb.length() > 0)
                            sb.append(", ");
                        // Mostrar Ø para destinos al estado error
                        Estado dest = a.getEstado(t.estadoDestino);
                        if (dest != null && dest.esError)
                            sb.append("Error \u00D8"); // Error Ø
                        else
                            sb.append(t.estadoDestino);
                    }
                }
                // Fila de error: self-loops muestran Ø → Error Ø
                if (sb.length() == 0 && est.esError)
                    sb.append("Error \u00D8");
                fila[i + 1] = sb.length() > 0 ? sb.toString() : "\u2014";
            }
            modeloTabla.addRow(fila);
        }

        aplicarRenderersTabla();
    }

    /*---------------------------------------------------------------------------
    PROPOSITO: Aplica renderers personalizados a la tabla de transiciones para mejorar su legibilidad. 
               Se definen renderers específicos para la columna de estados y para las columnas de transición, 
               que cambian el color de fondo y el color del texto según el tipo de estado (error, inicial, aceptación) 
               y el contenido de la celda (destino error Ø). 
               Esto permite al usuario identificar rápidamente los estados y transiciones importantes en la tabla.
    FECHA: 2026-05-16
    AUTOR: Victor Alfonso Pardo Gutierrez - Maryury Hernandez Marin
    VERSION: 1.3
    -----------------------------------------------------------------------------*/
    private void aplicarRenderersTabla() {
        // Renderer columna "Estado"
        tabla.getColumnModel().getColumn(0).setCellRenderer(
                new DefaultTableCellRenderer() {
                    @Override
                    public Component getTableCellRendererComponent(
                            JTable t, Object v, boolean sel, boolean foc, int r, int c) {
                        super.getTableCellRendererComponent(t, v, sel, foc, r, c);
                        String val = v != null ? v.toString() : "";
                        if (val.startsWith("\u2717")) { // ✗ Error Ø
                            setForeground(UIStyle.ACCENT_R);
                            setBackground(sel ? UIStyle.ERROR_SEL : UIStyle.ERROR_BG);
                        } else if (val.startsWith("*")) {
                            setForeground(UIStyle.ACCENT_G);
                            setBackground(sel ? UIStyle.SEL_BG : UIStyle.SURFACE);
                        } else if (val.startsWith("\u2192")) { // →
                            setForeground(UIStyle.ACCENT);
                            setBackground(sel ? UIStyle.SEL_BG : UIStyle.SURFACE);
                        } else {
                            setForeground(UIStyle.FG);
                            setBackground(sel ? UIStyle.SEL_BG : UIStyle.SURFACE);
                        }
                        return this;
                    }
                });

        // Renderer columnas de transición
        DefaultTableCellRenderer rendCelda = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable t, Object v, boolean sel, boolean foc, int r, int c) {
                super.getTableCellRendererComponent(t, v, sel, foc, r, c);
                String val = v != null ? v.toString() : "";
                Object estadoV = t.getValueAt(r, 0);
                boolean filaErr = estadoV != null
                        && estadoV.toString().startsWith("\u2717");

                if (val.contains("Error") || val.contains("\u00D8")) {
                    // Celda con destino Error Ø
                    setForeground(UIStyle.ACCENT_R);
                    setBackground(sel ? UIStyle.ERROR_SEL : UIStyle.ERROR_BG);
                } else if (filaErr) {
                    // Resto de la fila de error
                    setForeground(new Color(200, 100, 90));
                    setBackground(sel ? UIStyle.ERROR_SEL : UIStyle.ERROR_BG);
                } else {
                    setForeground(UIStyle.FG);
                    setBackground(sel ? UIStyle.SEL_BG : UIStyle.SURFACE);
                }
                return this;
            }
        };

        for (int i = 1; i < tabla.getColumnCount(); i++)
            tabla.getColumnModel().getColumn(i).setCellRenderer(rendCelda);
    }

    /*---------------------------------------------------------------------------
PROPOSITO: Agrega un mensaje al área de log, mostrando información relevante para el usuario. 
           Cada mensaje se separa visualmente con una línea horizontal para mejorar la legibilidad. 
           El área de log se desplaza automáticamente hacia abajo para mostrar el mensaje más reciente.
FECHA: 2026-05-16
AUTOR: Victor Alfonso Pardo Gutierrez - Maryury Hernandez Marin
VERSION: 1.3
-----------------------------------------------------------------------------*/
    private void log(String msg) {
        areaLog.append(msg + "\n\u2500\u2500\u2500\u2500\u2500\u2500\u2500\n");
        areaLog.setCaretPosition(areaLog.getDocument().getLength());
    }
}
