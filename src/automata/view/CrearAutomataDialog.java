package automata.view;

import automata.model.Automata;
import automata.model.Estado;
import automata.model.Transicion;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Diálogo para crear autómatas.
 *
 * Permite:
 * - Crear AFD
 * - Crear AFND
 * - Crear AFND-ε
 */
public class CrearAutomataDialog extends JDialog {

    // ─────────────────────────────────────────────
    // Resultado
    // ─────────────────────────────────────────────

    private Automata automataCreado;

    // ─────────────────────────────────────────────
    // Componentes
    // ─────────────────────────────────────────────

    private JTextField txtNombre;

    private JComboBox<String> cmbTipo;

    private JTextField txtEstados;

    private JTextField txtInicial;

    private JTextField txtAceptacion;

    private JTextField txtAlfabeto;

    private JTextArea txtTransiciones;

    // ─────────────────────────────────────────────
    // Constructor
    // ─────────────────────────────────────────────

    public CrearAutomataDialog(Frame owner) {

        super(owner, "Crear Autómata", true);

        configurarVentana();

        inicializarComponentes();

        construirLayout();

        registrarEventos();
    }

    // ─────────────────────────────────────────────
    // Configuración general
    // ─────────────────────────────────────────────

    private void configurarVentana() {

        setSize(650, 620);

        setLocationRelativeTo(getOwner());

        setResizable(false);

        getContentPane().setBackground(
                new Color(22, 27, 34)
        );
    }

    // ─────────────────────────────────────────────
    // Inicializar componentes
    // ─────────────────────────────────────────────

    private void inicializarComponentes() {

        txtNombre = crearCampo();

        txtEstados = crearCampo();

        txtInicial = crearCampo();

        txtAceptacion = crearCampo();

        txtAlfabeto = crearCampo();

        cmbTipo =
                new JComboBox<>(
                        new String[]{
                                "AFD",
                                "AFND",
                                "AFND-ε"
                        }
                );

        estilizarCombo(cmbTipo);

        txtTransiciones =
                new JTextArea();

        txtTransiciones.setFont(
                new Font(
                        "Monospaced",
                        Font.PLAIN,
                        13
                )
        );

        txtTransiciones.setBackground(
                new Color(13, 17, 23)
        );

        txtTransiciones.setForeground(
                new Color(230, 237, 243)
        );

        txtTransiciones.setCaretColor(
                new Color(88, 166, 255)
        );

        txtTransiciones.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(48, 54, 61)
                        ),
                        new EmptyBorder(
                                8,
                                8,
                                8,
                                8
                        )
                )
        );
    }

    // ─────────────────────────────────────────────
    // Construir layout
    // ─────────────────────────────────────────────

    private void construirLayout() {

        setLayout(new BorderLayout());

        add(crearFormulario(), BorderLayout.CENTER);

        add(crearFooter(), BorderLayout.SOUTH);
    }

    // ─────────────────────────────────────────────
    // Formulario
    // ─────────────────────────────────────────────

    private JPanel crearFormulario() {

        JPanel panel =
                new JPanel(
                        new GridBagLayout()
                );

        panel.setOpaque(false);

        panel.setBorder(
                new EmptyBorder(
                        20,
                        20,
                        20,
                        20
                )
        );

        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.insets =
                new Insets(8, 8, 8, 8);

        gbc.fill =
                GridBagConstraints.HORIZONTAL;

        gbc.weightx = 1;

        // Nombre
        agregarFila(
                panel,
                gbc,
                0,
                "Nombre:",
                txtNombre
        );

        // Tipo
        agregarFila(
                panel,
                gbc,
                1,
                "Tipo:",
                cmbTipo
        );

        // Estados
        agregarFila(
                panel,
                gbc,
                2,
                "Estados:",
                txtEstados
        );

        // Inicial
        agregarFila(
                panel,
                gbc,
                3,
                "Inicial:",
                txtInicial
        );

        // Aceptación
        agregarFila(
                panel,
                gbc,
                4,
                "Aceptación:",
                txtAceptacion
        );

        // Alfabeto
        agregarFila(
                panel,
                gbc,
                5,
                "Alfabeto:",
                txtAlfabeto
        );

        // Transiciones
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.NORTHWEST;

        panel.add(
                crearLabel("Transiciones:"),
                gbc
        );

        JScrollPane scroll =
                new JScrollPane(
                        txtTransiciones
                );

        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;

        panel.add(scroll, gbc);

        // Hint
        JLabel hint =
                new JLabel(
                        "Formato: origen,simbolo,destino"
                );

        hint.setFont(
                new Font(
                        "Monospaced",
                        Font.ITALIC,
                        11
                )
        );

        hint.setForeground(
                new Color(139, 148, 158)
        );

        gbc.gridx = 1;
        gbc.gridy = 7;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        panel.add(hint, gbc);

        return panel;
    }

    // ─────────────────────────────────────────────
    // Footer
    // ─────────────────────────────────────────────

    private JPanel crearFooter() {

        JPanel footer =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT
                        )
                );

        footer.setOpaque(false);

        footer.setBorder(
                new EmptyBorder(
                        10,
                        10,
                        10,
                        10
                )
        );

        JButton btnEjemplo =
                crearBoton(
                        "Cargar ejemplo",
                        new Color(63, 185, 80)
                );

        JButton btnCancelar =
                crearBoton(
                        "Cancelar",
                        new Color(139, 148, 158)
                );

        JButton btnCrear =
                crearBoton(
                        "Crear",
                        new Color(88, 166, 255)
                );

        // Eventos
        btnCancelar.addActionListener(
                e -> dispose()
        );

        btnCrear.addActionListener(
                e -> crearAutomata()
        );

        btnEjemplo.addActionListener(
                e -> cargarEjemplo()
        );

        footer.add(btnEjemplo);

        footer.add(btnCancelar);

        footer.add(btnCrear);

        return footer;
    }

    // ─────────────────────────────────────────────
    // Registrar eventos
    // ─────────────────────────────────────────────

    private void registrarEventos() {

        // Eventos futuros
    }

    // ─────────────────────────────────────────────
    // Crear autómata
    // ─────────────────────────────────────────────

    private void crearAutomata() {

        try {

            String nombre =
                    txtNombre.getText().trim();

            if (nombre.isEmpty()) {

                throw new Exception(
                        "El nombre es obligatorio"
                );
            }

            String tipo =
                    cmbTipo
                            .getSelectedItem()
                            .toString();

            Automata automata =
                    new Automata(nombre);

            automata.setAFND(
                    tipo.equals("AFND")
                            || tipo.equals("AFND-ε")
            );

            // Estados
            String[] estados =
                    txtEstados
                            .getText()
                            .split(",");

            String estadoInicial =
                    txtInicial
                            .getText()
                            .trim();

            // Aceptación
            Set<String> aceptacion =
                    new HashSet<>();

            String[] aceptacionArray =
                    txtAceptacion
                            .getText()
                            .split(",");

            for (String a : aceptacionArray) {

                if (!a.trim().isEmpty()) {

                    aceptacion.add(
                            a.trim()
                    );
                }
            }

            // Crear estados
            for (String estado : estados) {

                String nombreEstado =
                        estado.trim();

                if (nombreEstado.isEmpty())
                    continue;

                automata.getEstados().add(
                        new Estado(
                                nombreEstado,
                                nombreEstado.equals(
                                        estadoInicial
                                ),
                                aceptacion.contains(
                                        nombreEstado
                                )
                        )
                );
            }

            // Alfabeto
            String[] simbolos =
                    txtAlfabeto
                            .getText()
                            .split(",");

            for (String simbolo : simbolos) {

                if (!simbolo.trim().isEmpty()) {

                    automata.getAlfabeto().add(
                            simbolo.trim()
                    );
                }
            }

            // ε
            if (tipo.equals("AFND-ε")) {

                automata.getAlfabeto().add("ε");
            }

            // Transiciones
            String[] lineas =
                    txtTransiciones
                            .getText()
                            .split("\n");

            for (String linea : lineas) {

                linea = linea.trim();

                if (linea.isEmpty())
                    continue;

                String[] partes =
                        linea.split(",");

                if (partes.length != 3) {

                    throw new Exception(
                            "Transición inválida:\n"
                                    + linea
                    );
                }

                automata.getTransiciones().add(
                        new Transicion(
                                partes[0].trim(),
                                partes[1].trim(),
                                partes[2].trim()
                        )
                );
            }

            automataCreado = automata;

            dispose();

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // ─────────────────────────────────────────────
    // Cargar ejemplo
    // ─────────────────────────────────────────────

    private void cargarEjemplo() {

        txtNombre.setText("M1");

        cmbTipo.setSelectedItem("AFND-ε");

        txtEstados.setText(
                "q0, q1, q2"
        );

        txtInicial.setText("q0");

        txtAceptacion.setText("q2");

        txtAlfabeto.setText("a, b");

        txtTransiciones.setText(
                "q0,a,q0\n" +
                "q0,ε,q1\n" +
                "q1,b,q1\n" +
                "q1,ε,q2\n" +
                "q2,a,q2"
        );
    }

    // ─────────────────────────────────────────────
    // Helpers UI
    // ─────────────────────────────────────────────

    private JTextField crearCampo() {

        JTextField field =
                new JTextField();

        field.setFont(
                new Font(
                        "Monospaced",
                        Font.PLAIN,
                        13
                )
        );

        field.setBackground(
                new Color(13, 17, 23)
        );

        field.setForeground(
                new Color(230, 237, 243)
        );

        field.setCaretColor(
                new Color(88, 166, 255)
        );

        field.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(48, 54, 61)
                        ),
                        new EmptyBorder(
                                8,
                                8,
                                8,
                                8
                        )
                )
        );

        return field;
    }

    private JLabel crearLabel(String texto) {

        JLabel label =
                new JLabel(texto);

        label.setFont(
                new Font(
                        "Monospaced",
                        Font.BOLD,
                        13
                )
        );

        label.setForeground(
                new Color(230, 237, 243)
        );

        return label;
    }

    private JButton crearBoton(
            String texto,
            Color color
    ) {

        JButton btn =
                new JButton(texto);

        btn.setFocusPainted(false);

        btn.setFont(
                new Font(
                        "Monospaced",
                        Font.BOLD,
                        12
                )
        );

        btn.setForeground(color);

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
                                14,
                                8,
                                14
                        )
                )
        );

        return btn;
    }

    private void estilizarCombo(
            JComboBox<String> combo
    ) {

        combo.setFont(
                new Font(
                        "Monospaced",
                        Font.PLAIN,
                        13
                )
        );

        combo.setBackground(
                new Color(13, 17, 23)
        );

        combo.setForeground(
                new Color(230, 237, 243)
        );
    }

    private void agregarFila(
            JPanel panel,
            GridBagConstraints gbc,
            int fila,
            String texto,
            Component componente
    ) {

        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.weightx = 0;

        panel.add(
                crearLabel(texto),
                gbc
        );

        gbc.gridx = 1;
        gbc.weightx = 1;

        panel.add(
                componente,
                gbc
        );
    }

    // ─────────────────────────────────────────────
    // Getter resultado
    // ─────────────────────────────────────────────

    public Automata getAutomataCreado() {

        return automataCreado;
    }
}