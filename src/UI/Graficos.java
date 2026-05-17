package UI;
/*
* Proposito: Clase para manejar la interfaz gráfica del programa. Ventana que permita al usuario ingresar el autómata, 
*            mostrar el autómata minimizado y las tablas de transición.
* Autor: Victor Alfonso Pardo Guiterrez - Maryury Hernandez Marin
* Fecha: 2024-5-17
* versión: 1.0
 */
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import Automata.Automata;
import Automata.ConversorAFND;
import Automata.Minimizador;

public class Graficos extends JFrame {
    private Automata automata;
    private TablaTransiciones tablaTransiciones;
    private JTable tablaPrincipal;
    private JTextArea textoExpresionRegular;
    private JTextArea textoInformacionAutomata;
    private JButton btnConvertir;
    private JButton btnMinimizar;
    private JButton btnLimpiar;
    private JLabel lblTituloAutomata;
    private boolean esAFND;

    public Graficos(Automata automata, boolean esAFND) {
        this.automata = automata;
        this.esAFND = esAFND;
        this.tablaTransiciones = new TablaTransiciones(automata);
        
        inicializarComponentes();
        configurarVentana();
    }

    /**
     * Inicializa los componentes visuales de la interfaz gráfica.
     */
    private void inicializarComponentes() {
        // Panel principal
        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BorderLayout());

        // Panel superior con título y botones de acción
        JPanel panelSuperior = crearPanelSuperior();
        panelPrincipal.add(panelSuperior, BorderLayout.NORTH);

        // Panel central dividido en dos secciones
        JSplitPane panelCentral = crearPanelCentral();
        panelPrincipal.add(panelCentral, BorderLayout.CENTER);

        // Panel inferior con información
        JPanel panelInferior = crearPanelInferior();
        panelPrincipal.add(panelInferior, BorderLayout.SOUTH);

        this.setContentPane(panelPrincipal);
    }

    /**
     * Crea el panel superior con título y botones de acción.
     */
    private JPanel crearPanelSuperior() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Título
        lblTituloAutomata = new JLabel("Visualización de Autómata - " + (esAFND ? "AFND" : "AFD"));
        lblTituloAutomata.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(lblTituloAutomata, BorderLayout.WEST);

        // Botones de acción
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 0));

        btnConvertir = new JButton("Convertir AFND → AFD");
        btnConvertir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                convertirAFNDaAFD();
            }
        });
        btnConvertir.setEnabled(esAFND);

        btnMinimizar = new JButton("Minimizar AFD");
        btnMinimizar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                minimizarAFD();
            }
        });
        btnMinimizar.setEnabled(!esAFND);

        btnLimpiar = new JButton("Limpiar");
        btnLimpiar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limpiarDatos();
            }
        });

        panelBotones.add(btnConvertir);
        panelBotones.add(btnMinimizar);
        panelBotones.add(btnLimpiar);

        panel.add(panelBotones, BorderLayout.EAST);

        return panel;
    }

    /**
     * Crea el panel central dividido en tabla de transiciones e información.
     */
    private JSplitPane crearPanelCentral() {
        // Sección izquierda: Tabla de transiciones
        JPanel panelTabla = new JPanel();
        panelTabla.setLayout(new BorderLayout());
        panelTabla.setBorder(BorderFactory.createTitledBorder("Tabla de Transiciones"));

        tablaPrincipal = new JTable(tablaTransiciones.getModeloTabla());
        tablaPrincipal.setRowHeight(25);
        tablaPrincipal.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JScrollPane scrollTabla = new JScrollPane(tablaPrincipal);
        scrollTabla.setPreferredSize(new Dimension(500, 300));
        panelTabla.add(scrollTabla, BorderLayout.CENTER);

        // Sección derecha: Expresión regular e información
        JPanel panelDerecha = new JPanel();
        panelDerecha.setLayout(new GridLayout(2, 1, 10, 10));
        panelDerecha.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Expresión regular
        JPanel panelExpresion = new JPanel();
        panelExpresion.setLayout(new BorderLayout());
        panelExpresion.setBorder(BorderFactory.createTitledBorder("Expresión Regular"));
        
        textoExpresionRegular = new JTextArea();
        textoExpresionRegular.setEditable(false);
        textoExpresionRegular.setLineWrap(true);
        textoExpresionRegular.setWrapStyleWord(true);
        textoExpresionRegular.setFont(new Font("Courier New", Font.PLAIN, 12));
        textoExpresionRegular.setText("(a|b)*");
        
        JScrollPane scrollExpresion = new JScrollPane(textoExpresionRegular);
        panelExpresion.add(scrollExpresion, BorderLayout.CENTER);

        // Información del autómata
        JPanel panelInfo = new JPanel();
        panelInfo.setLayout(new BorderLayout());
        panelInfo.setBorder(BorderFactory.createTitledBorder("Información del Autómata"));
        
        textoInformacionAutomata = new JTextArea();
        textoInformacionAutomata.setEditable(false);
        textoInformacionAutomata.setFont(new Font("Arial", Font.PLAIN, 11));
        textoInformacionAutomata.setText(tablaTransiciones.obtenerInformacionAutomata());
        
        JScrollPane scrollInfo = new JScrollPane(textoInformacionAutomata);
        panelInfo.add(scrollInfo, BorderLayout.CENTER);

        panelDerecha.add(panelExpresion);
        panelDerecha.add(panelInfo);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelTabla, panelDerecha);
        splitPane.setDividerLocation(0.5);

        return splitPane;
    }

    /**
     * Crea el panel inferior con información de estado.
     */
    private JPanel crearPanelInferior() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        panel.setBackground(new Color(240, 240, 240));

        JLabel lblEstado = new JLabel("Estado: Listo");
        lblEstado.setFont(new Font("Arial", Font.PLAIN, 11));
        panel.add(lblEstado, BorderLayout.WEST);

        return panel;
    }

    /**
     * Convierte el AFND actual a AFD utilizando la clase ConversorAFND.
     */
    private void convertirAFNDaAFD() {
        try {
            if (automata == null || !esAFND) {
                JOptionPane.showMessageDialog(this, 
                    "El autómata actual no es un AFND", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            ConversorAFND conversor = new ConversorAFND(automata);
            Automata automataAFD = conversor.convertir();

            if (automataAFD != null) {
                automata = automataAFD;
                esAFND = false;
                tablaTransiciones.setAutomata(automata);
                actualizarTabla();

                lblTituloAutomata.setText("Visualización de Autómata - AFD");
                btnConvertir.setEnabled(false);
                btnMinimizar.setEnabled(true);

                JOptionPane.showMessageDialog(this, 
                    "Conversión AFND → AFD completada exitosamente", 
                    "Éxito", 
                    JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "Error durante la conversión: " + ex.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Minimiza el AFD actual utilizando la clase Minimizador.
     */
    private void minimizarAFD() {
        try {
            if (automata == null || esAFND) {
                JOptionPane.showMessageDialog(this, 
                    "El autómata actual no es un AFD", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            Minimizador minimizador = new Minimizador(automata);
            Automata automataMinimizado = minimizador.minimizar();

            if (automataMinimizado != null) {
                automata = automataMinimizado;
                tablaTransiciones.setAutomata(automata);
                actualizarTabla();

                JOptionPane.showMessageDialog(this, 
                    "Minimización completada exitosamente", 
                    "Éxito", 
                    JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "Error durante la minimización: " + ex.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Actualiza la tabla y la información mostrada.
     */
    private void actualizarTabla() {
        DefaultTableModel nuevoModelo = tablaTransiciones.getModeloTabla();
        tablaPrincipal.setModel(nuevoModelo);
        textoInformacionAutomata.setText(tablaTransiciones.obtenerInformacionAutomata());
    }

    /**
     * Limpia los datos mostrados en la pantalla.
     */
    private void limpiarDatos() {
        textoExpresionRegular.setText("");
        textoInformacionAutomata.setText("");
        if (tablaPrincipal.getModel() instanceof DefaultTableModel) {
            DefaultTableModel modelo = (DefaultTableModel) tablaPrincipal.getModel();
            modelo.setRowCount(0);
        }
    }

    /**
     * Configura la ventana principal.
     */
    private void configurarVentana() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1000, 600);
        this.setLocationRelativeTo(null);
        this.setTitle("Visualizador de Autómatas");
        this.setVisible(true);
    }

    /**
     * Obtiene el autómata actual.
     * 
     * @return Automata actual
     */
    public Automata getAutomata() {
        return automata;
    }

    /**
     * Establece un nuevo autómata.
     * 
     * @param nuevoAutomata Nuevo autómata a visualizar
     */
    public void setAutomata(Automata nuevoAutomata) {
        this.automata = nuevoAutomata;
        this.tablaTransiciones.setAutomata(nuevoAutomata);
        actualizarTabla();
    }

    /**
     * Obtiene el estado del autómata (AFND o AFD).
     * 
     * @return true si es AFND, false si es AFD
     */
    public boolean esAFND() {
        return esAFND;
    }

    /**
     * Establece el estado del autómata.
     * 
     * @param esAFND true si es AFND, false si es AFD
     */
    public void setEsAFND(boolean esAFND) {
        this.esAFND = esAFND;
        btnConvertir.setEnabled(esAFND);
        btnMinimizar.setEnabled(!esAFND);
        lblTituloAutomata.setText("Visualización de Autómata - " + (esAFND ? "AFND" : "AFD"));
    }
}