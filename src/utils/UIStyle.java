package utils;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

/*---------------------------------------------------------------------------
PROPOSITO: Definir una paleta de colores, fuentes y estilos de componentes 
           para toda la interfaz gráfica.
FECHA: 2026-05-16
AUTOR: Victor Alfonso Pardo Gutierrez - Maryury Hernandez Marin
VERSION: 1.3
ACTUALIZACIÓN: 2026-05-23 - Se agregaron nuevos colores a la paleta para mejorar 
               la consistencia visual de la interfaz.
                            Se corrige error en el método crearBotonSidebar 
                            para que el efecto hover funcione correctamente.
                            Se corrige el método estilizarBotonSimple 
                            para que el fondo del botón tenga un color semitransparente.
-----------------------------------------------------------------------------*/
public final class UIStyle {
    /*---------------------------------------------------------------------------
    PROPOSITO: Clase de utilidades para estilos de la interfaz gráfica, 
               con una paleta de colores 
               y fuentes predefinidas, además de métodos para crear 
               componentes estilizados.
    FECHA: 2026-05-16
    AUTOR: Victor Alfonso Pardo Gutierrez - Maryury Hernandez Marin
    VERSION: 1.3
    ACTUALIZACIÓN: 2026-05-23 - Se agregaron nuevos colores a la paleta para mejorar 
                   la consistencia visual de la interfaz.
    -----------------------------------------------------------------------------*/
    private UIStyle() {
    }

    // ── Paleta de colores ──────────────────────────────────────────────────────

    public static final Color BG = new Color(13, 17, 23);
    public static final Color SURFACE = new Color(22, 27, 34);
    public static final Color BORDER = new Color(48, 54, 61);
    public static final Color ACCENT = new Color(88, 166, 255); // azul
    public static final Color ACCENT_G = new Color(63, 185, 80); // verde
    public static final Color ACCENT_R = new Color(248, 81, 73); // rojo
    public static final Color ACCENT_Y = new Color(210, 153, 34); // amarillo
    public static final Color FG = new Color(230, 237, 243);
    public static final Color FG_MUTED = new Color(139, 148, 158);
    public static final Color SEL_BG = new Color(56, 91, 131);

    // Error específico
    public static final Color ERROR_BG = new Color(45, 20, 20);
    public static final Color ERROR_SEL = new Color(100, 40, 40);

    // ── Fuentes ────────────────────────────────────────────────────────────────

    public static final Font FONT_MONO_TITLE = new Font("Monospaced", Font.BOLD, 18);
    public static final Font FONT_MONO_HEADER = new Font("Monospaced", Font.BOLD, 14);
    public static final Font FONT_MONO_LABEL = new Font("Monospaced", Font.BOLD, 12);
    public static final Font FONT_MONO_BODY = new Font("Monospaced", Font.PLAIN, 13);
    public static final Font FONT_MONO_SMALL = new Font("Monospaced", Font.PLAIN, 11);
    public static final Font FONT_MONO_TINY = new Font("Monospaced", Font.PLAIN, 10);

    // ── Fábrica de componentes ─────────────────────────────────────────────────

    /*---------------------------------------------------------------------------
    PROPOSITO: Crear un botón estilizado para la barra lateral, con fondo transparente y 
               efecto hover. El color del texto se define según el tipo de acción 
               (normal, acento, error).
    FECHA: 2026-05-16
    AUTOR: Victor Alfonso Pardo Gutierrez - Maryury Hernandez Marin
    VERSION: 1.3
    -----------------------------------------------------------------------------*/
    public static JButton crearBotonSidebar(String texto, Color color) {
        JButton btn = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                Color c = getForeground();
                if (getModel().isPressed())
                    g2.setColor(c.darker().darker());
                else if (getModel().isRollover())
                    g2.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), 40));
                else
                    g2.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), 20));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.setColor(isEnabled() ? c : c.darker());
                g2.setStroke(new BasicStroke(1.4f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setForeground(color);
        btn.setFont(FONT_MONO_LABEL);
        btn.setBackground(new Color(0, 0, 0, 0));
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(190, 38));
        btn.setPreferredSize(new Dimension(190, 38));
        return btn;
    }

    /*---------------------------------------------------------------------------
    PROPOSITO: Estilizar un botón simple para el contenido principal, con fondo semitransparente y 
               borde redondeado. El color del texto se define según el tipo de acción 
               (normal, acento, error).
    FECHA: 2026-05-16
    AUTOR: Victor Alfonso Pardo Gutierrez - Maryury Hernandez Marin
    VERSION: 1.3
    -----------------------------------------------------------------------------*/
    public static void estilizarBotonSimple(JButton btn, Color color) {
        btn.setForeground(color);
        btn.setBackground(new Color(color.getRed(), color.getGreen(), color.getBlue(), 25));
        btn.setFont(FONT_MONO_LABEL);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(color, 1, true),
                BorderFactory.createEmptyBorder(6, 14, 6, 14)));
    }

    /*---------------------------------------------------------------------------
    PROPOSITO: Crear un campo de texto estilizado.
    FECHA: 2026-05-16
    AUTOR: Victor Alfonso Pardo Gutierrez - Maryury Hernandez Marin
    VERSION: 1.3
    -----------------------------------------------------------------------------*/
    public static JTextField crearCampo() {
        JTextField tf = new JTextField();
        tf.setFont(FONT_MONO_BODY);
        tf.setBackground(BG);
        tf.setForeground(FG);
        tf.setCaretColor(ACCENT);
        tf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                BorderFactory.createEmptyBorder(4, 6, 4, 6)));
        return tf;
    }

    /*---------------------------------------------------------------------------
    PROPOSITO: Crear una etiqueta estilizada.
    FECHA: 2026-05-16
    AUTOR: Victor Alfonso Pardo Gutierrez - Maryury Hernandez Marin
    VERSION: 1.3
    -----------------------------------------------------------------------------*/
    public static JLabel crearLabel(String texto, Color color, Font font) {
        JLabel l = new JLabel(texto);
        l.setFont(font);
        l.setForeground(color);
        return l;
    }

    /*---------------------------------------------------------------------------
    PROPOSITO: Estilizar un combo box.
    FECHA: 2026-05-16
    AUTOR: Victor Alfonso Pardo Gutierrez - Maryury Hernandez Marin
    VERSION: 1.3
    -----------------------------------------------------------------------------*/
    public static void estilizarCombo(JComboBox<?> cb) {
        cb.setBackground(BG);
        cb.setForeground(FG);
        cb.setFont(FONT_MONO_BODY);
    }

    /*---------------------------------------------------------------------------
    PROPOSITO: Crear un borde tipo matte con el color de borde definido en la paleta.
    FECHA: 2026-05-16
    AUTOR: Victor Alfonso Pardo Gutierrez - Maryury Hernandez Marin
    VERSION: 1.3
    -----------------------------------------------------------------------------*/
    public static Border borderMatte(int top, int left, int bottom, int right) {
        return BorderFactory.createMatteBorder(top, left, bottom, right, BORDER);
    }
}
