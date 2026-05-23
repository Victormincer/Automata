package view;

import controller.AutomataController;
import model.Automata;
import utils.UIStyle;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

/*---------------------------------------------------------------------------
 PROPOSITO: Diálogo modal para crear un nuevo autómata.
 FECHA: 2026-05-16
 AUTOR: Victor Alfonso Pardo Gutierrez - Maryury Hernandez Marin
 VERSION: 1.3
 ACTUALIZACIÓN: 2026-05-23 - Se mejoró el diseño del diálogo, 
                            con mejores colores, fuentes y estilos de los campos.
 -----------------------------------------------------------------------------*/

public class CrearAutomataDialog extends JDialog {

    private final AutomataController ctrl;
    private final Consumer<Automata> onCreado;

    // Campos del formulario
    private JTextField txtNombre;
    private JComboBox<String> cmbTipo;
    private JTextField txtEstados;
    private JTextField txtInicial;
    private JTextField txtAceptacion;
    private JTextField txtAlfabeto;
    private JTextArea txtTransiciones;

    public CrearAutomataDialog(Frame owner,
            AutomataController ctrl,
            Consumer<Automata> onCreado) {
        super(owner, "Crear Autómata", true);
        this.ctrl = ctrl;
        this.onCreado = onCreado;
        setSize(620, 580);
        setMinimumSize(new Dimension(540, 500));
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(0, 0));
        getContentPane().setBackground(UIStyle.SURFACE);
        initUI();
    }

    private void initUI() {
        add(crearFormulario(), BorderLayout.CENTER);
        add(crearPanelBotones(), BorderLayout.SOUTH);
    }

    /*---------------------------------------------------------------------------
    PROPOSITO: Crea el formulario para ingresar los datos del autómata.
    FECHA: 2026-05-23
    AUTOR: Victor Alfonso Pardo Gutierrez - Maryury Hernandez Marin
    VERSION: 1.3
    -----------------------------------------------------------------------------*/
    private JPanel crearFormulario() {
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(UIStyle.SURFACE);
        form.setBorder(BorderFactory.createEmptyBorder(18, 18, 8, 18));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 4, 6, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        int row = 0;

        // Nombre
        txtNombre = UIStyle.crearCampo();
        agregarFila(form, gbc, row++, "Nombre:", txtNombre, null);

        // Tipo (solo AFD y AFND — sin ε)
        cmbTipo = new JComboBox<>(new String[] { "AFD", "AFND" });
        UIStyle.estilizarCombo(cmbTipo);
        agregarFila(form, gbc, row++, "Tipo:", cmbTipo, null);

        // Estados
        txtEstados = UIStyle.crearCampo();
        agregarFila(form, gbc, row++, "Estados:", txtEstados, "Ej: q0, q1, q2");

        // Inicial
        txtInicial = UIStyle.crearCampo();
        agregarFila(form, gbc, row++, "Inicial:", txtInicial, "Ej: q0");

        // Aceptación
        txtAceptacion = UIStyle.crearCampo();
        agregarFila(form, gbc, row++, "Aceptación:", txtAceptacion, "Ej: q2, q3");

        // Alfabeto
        txtAlfabeto = UIStyle.crearCampo();
        agregarFila(form, gbc, row++, "Alfabeto:", txtAlfabeto, "Ej: a, b");

        // Transiciones (área multilinea)
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.NORTH;
        form.add(UIStyle.crearLabel("Transiciones:", UIStyle.FG, UIStyle.FONT_MONO_LABEL), gbc);

        txtTransiciones = new JTextArea(7, 22);
        txtTransiciones.setFont(UIStyle.FONT_MONO_BODY);
        txtTransiciones.setBackground(UIStyle.BG);
        txtTransiciones.setForeground(UIStyle.FG);
        txtTransiciones.setCaretColor(UIStyle.ACCENT);
        txtTransiciones.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIStyle.BORDER),
                BorderFactory.createEmptyBorder(4, 6, 4, 6)));
        txtTransiciones.setToolTipText("Una por línea: origen,simbolo,destino");

        JScrollPane spTrans = new JScrollPane(txtTransiciones);
        spTrans.setBorder(BorderFactory.createLineBorder(UIStyle.BORDER));

        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        form.add(spTrans, gbc);
        row++;

        // Hint
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        JLabel hint = UIStyle.crearLabel(
                "  Formato: origen,simbolo,destino   (ej: q0,a,q1)",
                UIStyle.FG_MUTED, UIStyle.FONT_MONO_SMALL);
        hint.setBorder(BorderFactory.createEmptyBorder(2, 0, 2, 0));
        form.add(hint, gbc);

        return form;
    }

    /*---------------------------------------------------------------------------
    PROPOSITO: Agrega una fila al formulario con un label y un campo de texto.
    FECHA: 2026-05-23
    AUTOR: Victor Alfonso Pardo Gutierrez - Maryury Hernandez Marin
    VERSION: 1.3
    -----------------------------------------------------------------------------*/
    private void agregarFila(JPanel form, GridBagConstraints gbc,
            int row, String label,
            JComponent campo, String tooltip) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        form.add(UIStyle.crearLabel(label, UIStyle.FG, UIStyle.FONT_MONO_LABEL), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        if (tooltip != null && campo instanceof JTextField)
            ((JTextField) campo).setToolTipText(tooltip);
        form.add(campo, gbc);
    }

    /*---------------------------------------------------------------------------
    PROPOSITO: Crea el panel con los botones de acción.
    FECHA: 2026-05-23
    AUTOR: Victor Alfonso Pardo Gutierrez - Maryury Hernandez Marin
    VERSION: 1.3
    -----------------------------------------------------------------------------*/
    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        panel.setBackground(UIStyle.SURFACE);
        panel.setBorder(UIStyle.borderMatte(1, 0, 0, 0));

        // Botón Ejemplo
        JButton btnEjemplo = new JButton("Cargar ejemplo");
        UIStyle.estilizarBotonSimple(btnEjemplo, UIStyle.ACCENT_G);
        btnEjemplo.addActionListener(e -> cargarEjemplo());

        // Botón Cancelar
        JButton btnCancelar = new JButton("Cancelar");
        UIStyle.estilizarBotonSimple(btnCancelar, UIStyle.FG_MUTED);
        btnCancelar.addActionListener(e -> dispose());

        // Botón Crear
        JButton btnCrear = new JButton("Crear Autómata");
        UIStyle.estilizarBotonSimple(btnCrear, UIStyle.ACCENT);
        btnCrear.addActionListener(e -> confirmar());

        panel.add(btnEjemplo);
        panel.add(btnCancelar);
        panel.add(btnCrear);
        return panel;
    }

    /*---------------------------------------------------------------------------
    PROPOSITO: Confirma la creación del autómata.
    FECHA: 2026-05-23
    AUTOR: Victor Alfonso Pardo Gutierrez - Maryury Hernandez Marin
    VERSION: 1.3
    -----------------------------------------------------------------------------*/

    private void confirmar() {
        try {
            Automata a = ctrl.crearAutomata(
                    txtNombre.getText(),
                    (String) cmbTipo.getSelectedItem(),
                    txtEstados.getText(),
                    txtInicial.getText(),
                    txtAceptacion.getText(),
                    txtAlfabeto.getText(),
                    txtTransiciones.getText());
            onCreado.accept(a);
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Error de validación", JOptionPane.ERROR_MESSAGE);
        }
    }

    /*---------------------------------------------------------------------------
    PROPOSITO: Carga un ejemplo predefinido en los campos del formulario.
    FECHA: 2026-05-23
    AUTOR: Victor Alfonso Pardo Gutierrez - Maryury Hernandez Marin
    VERSION: 1.3
    -----------------------------------------------------------------------------*/
    private void cargarEjemplo() {
        txtNombre.setText("M1");
        cmbTipo.setSelectedItem("AFND");
        txtEstados.setText("q0, q1, q2");
        txtInicial.setText("q0");
        txtAceptacion.setText("q2");
        txtAlfabeto.setText("a, b");
        txtTransiciones.setText(
                "q0,a,q0\n" +
                        "q0,a,q1\n" +
                        "q1,b,q2\n" +
                        "q2,b,q2");
    }
}
