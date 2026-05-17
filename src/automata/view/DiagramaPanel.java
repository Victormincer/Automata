package automata.view;

import automata.model.Automata;
import automata.model.Estado;
import automata.model.Transicion;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.QuadCurve2D;
import java.util.*;
import java.util.List;

/**
 * Panel encargado de dibujar el diagrama del autómata.
 * Renderiza:
 * - Estados
 * - Transiciones
 * - Flechas
 * - Loops
 * - Arcos curvos
 */
public class DiagramaPanel extends JPanel {

    private final Automata automata;

    private final Map<String, Point> posiciones =
            new LinkedHashMap<>();

    private static final int RADIO_ESTADO = 30;

    // ─────────────────────────────────────────────
    // Constructor
    // ─────────────────────────────────────────────

    public DiagramaPanel(Automata automata) {

        this.automata = automata;

        setBackground(new Color(13, 17, 23));

        calcularPosiciones();

        setToolTipText(
                "Diagrama del autómata " +
                        automata.getNombre()
        );
    }

    // ─────────────────────────────────────────────
    // Calcular posiciones circulares
    // ─────────────────────────────────────────────

    private void calcularPosiciones() {

        int cantidadEstados =
                automata.getEstados().size();

        int centroX = 400;
        int centroY = 280;

        int radio =
                Math.min(
                        220,
                        40 + cantidadEstados * 28
                );

        // Caso: solo un estado
        if (cantidadEstados == 1) {

            Estado estado =
                    automata.getEstados().get(0);

            posiciones.put(
                    estado.getNombre(),
                    new Point(centroX, centroY)
            );

            return;
        }

        for (int i = 0; i < cantidadEstados; i++) {

            double angulo =
                    (2 * Math.PI * i / cantidadEstados)
                            - Math.PI / 2;

            int x =
                    centroX +
                            (int) (radio * Math.cos(angulo));

            int y =
                    centroY +
                            (int) (radio * Math.sin(angulo));

            Estado estado =
                    automata.getEstados().get(i);

            posiciones.put(
                    estado.getNombre(),
                    new Point(x, y)
            );
        }
    }

    // ─────────────────────────────────────────────
    // Render principal
    // ─────────────────────────────────────────────

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        Graphics2D g2 =
                (Graphics2D) g;

        activarAntialias(g2);

        // Colores
        Color colorTexto =
                new Color(230, 237, 243);

        Color colorInicial =
                new Color(88, 166, 255);

        Color colorAceptacion =
                new Color(63, 185, 80);

        Color colorNormal =
                new Color(48, 54, 61);

        Color colorFlecha =
                new Color(180, 190, 200);

        Color colorEtiqueta =
                new Color(210, 160, 50);

        // Dibujar transiciones
        dibujarTransiciones(
                g2,
                colorFlecha,
                colorEtiqueta
        );

        // Dibujar estados
        dibujarEstados(
                g2,
                colorTexto,
                colorInicial,
                colorAceptacion,
                colorNormal
        );

        // Nombre del autómata
        dibujarTitulo(g2);
    }

    // ─────────────────────────────────────────────
    // Dibujar transiciones
    // ─────────────────────────────────────────────

    private void dibujarTransiciones(
            Graphics2D g2,
            Color colorFlecha,
            Color colorEtiqueta
    ) {

        Map<String, List<String>> mapaEtiquetas =
                agruparTransiciones();

        g2.setStroke(
                new BasicStroke(
                        1.6f,
                        BasicStroke.CAP_ROUND,
                        BasicStroke.JOIN_ROUND
                )
        );

        for (Map.Entry<String, List<String>> entry :
                mapaEtiquetas.entrySet()) {

            String[] partes =
                    entry.getKey().split("##");

            String origen = partes[0];
            String destino = partes[1];

            String etiqueta =
                    String.join(",", entry.getValue());

            Point p1 = posiciones.get(origen);
            Point p2 = posiciones.get(destino);

            if (p1 == null || p2 == null)
                continue;

            // Self-loop
            if (origen.equals(destino)) {

                dibujarLoop(
                        g2,
                        p1,
                        etiqueta,
                        colorFlecha,
                        colorEtiqueta
                );

                continue;
            }

            boolean hayInverso =
                    mapaEtiquetas.containsKey(
                            destino + "##" + origen
                    );

            if (hayInverso) {

                dibujarArcoCurvo(
                        g2,
                        p1,
                        p2,
                        etiqueta,
                        colorFlecha,
                        colorEtiqueta
                );

            } else {

                dibujarArcoRecto(
                        g2,
                        p1,
                        p2,
                        etiqueta,
                        colorFlecha,
                        colorEtiqueta
                );
            }
        }
    }

    // ─────────────────────────────────────────────
    // Agrupar transiciones
    // ─────────────────────────────────────────────

    private Map<String, List<String>> agruparTransiciones() {

        Map<String, List<String>> mapa =
                new LinkedHashMap<>();

        for (Transicion t :
                automata.getTransiciones()) {

            String key =
                    t.getOrigen()
                            + "##"
                            + t.getDestino();

            mapa.computeIfAbsent(
                    key,
                    k -> new ArrayList<>()
            ).add(t.getSimbolo());
        }

        return mapa;
    }

    // ─────────────────────────────────────────────
    // Dibujar estados
    // ─────────────────────────────────────────────

    private void dibujarEstados(
            Graphics2D g2,
            Color colorTexto,
            Color colorInicial,
            Color colorAceptacion,
            Color colorNormal
    ) {

        for (Estado estado :
                automata.getEstados()) {

            Point p =
                    posiciones.get(
                            estado.getNombre()
                    );

            if (p == null)
                continue;

            dibujarSombra(g2, p);

            dibujarRellenoEstado(
                    g2,
                    estado,
                    p
            );

            dibujarBordeEstado(
                    g2,
                    estado,
                    p,
                    colorInicial,
                    colorAceptacion,
                    colorNormal
            );

            if (estado.isAceptacion()) {

                dibujarDobleCirculo(g2, p);
            }

            if (estado.isInicial()) {

                dibujarFlechaInicial(
                        g2,
                        p,
                        colorInicial
                );
            }

            dibujarNombreEstado(
                    g2,
                    estado,
                    p,
                    colorTexto
            );
        }
    }

    // ─────────────────────────────────────────────
    // Dibujar sombra
    // ─────────────────────────────────────────────

    private void dibujarSombra(
            Graphics2D g2,
            Point p
    ) {

        g2.setColor(
                new Color(0, 0, 0, 60)
        );

        g2.fillOval(
                p.x - RADIO_ESTADO + 3,
                p.y - RADIO_ESTADO + 3,
                RADIO_ESTADO * 2,
                RADIO_ESTADO * 2
        );
    }

    // ─────────────────────────────────────────────
    // Dibujar relleno
    // ─────────────────────────────────────────────

    private void dibujarRellenoEstado(
            Graphics2D g2,
            Estado estado,
            Point p
    ) {

        if (estado.isInicial()) {

            g2.setColor(
                    new Color(30, 50, 80)
            );

        } else if (estado.isAceptacion()) {

            g2.setColor(
                    new Color(20, 55, 30)
            );

        } else {

            g2.setColor(
                    new Color(30, 35, 43)
            );
        }

        g2.fillOval(
                p.x - RADIO_ESTADO,
                p.y - RADIO_ESTADO,
                RADIO_ESTADO * 2,
                RADIO_ESTADO * 2
        );
    }

    // ─────────────────────────────────────────────
    // Dibujar borde
    // ─────────────────────────────────────────────

    private void dibujarBordeEstado(
            Graphics2D g2,
            Estado estado,
            Point p,
            Color colorInicial,
            Color colorAceptacion,
            Color colorNormal
    ) {

        g2.setStroke(new BasicStroke(2f));

        if (estado.isInicial()) {

            g2.setColor(colorInicial);

        } else if (estado.isAceptacion()) {

            g2.setColor(colorAceptacion);

        } else {

            g2.setColor(colorNormal);
        }

        g2.drawOval(
                p.x - RADIO_ESTADO,
                p.y - RADIO_ESTADO,
                RADIO_ESTADO * 2,
                RADIO_ESTADO * 2
        );
    }

    // ─────────────────────────────────────────────
    // Doble círculo aceptación
    // ─────────────────────────────────────────────

    private void dibujarDobleCirculo(
            Graphics2D g2,
            Point p
    ) {

        int radioInterno =
                RADIO_ESTADO - 5;

        g2.setStroke(
                new BasicStroke(1.2f)
        );

        g2.drawOval(
                p.x - radioInterno,
                p.y - radioInterno,
                radioInterno * 2,
                radioInterno * 2
        );
    }

    // ─────────────────────────────────────────────
    // Flecha estado inicial
    // ─────────────────────────────────────────────

    private void dibujarFlechaInicial(
            Graphics2D g2,
            Point p,
            Color color
    ) {

        g2.setColor(color);

        g2.setStroke(
                new BasicStroke(2f)
        );

        g2.drawLine(
                p.x - RADIO_ESTADO - 30,
                p.y,
                p.x - RADIO_ESTADO - 2,
                p.y
        );

        dibujarPuntaFlecha(
                g2,
                p.x - RADIO_ESTADO,
                p.y,
                0,
                color
        );
    }

    // ─────────────────────────────────────────────
    // Nombre estado
    // ─────────────────────────────────────────────

    private void dibujarNombreEstado(
            Graphics2D g2,
            Estado estado,
            Point p,
            Color colorTexto
    ) {

        g2.setColor(colorTexto);

        g2.setFont(
                new Font(
                        "Monospaced",
                        Font.BOLD,
                        13
                )
        );

        FontMetrics fm =
                g2.getFontMetrics();

        int ancho =
                fm.stringWidth(
                        estado.getNombre()
                );

        g2.drawString(
                estado.getNombre(),
                p.x - ancho / 2,
                p.y + fm.getAscent() / 2
        );
    }

    // ─────────────────────────────────────────────
    // Arco recto
    // ─────────────────────────────────────────────

    private void dibujarArcoRecto(
            Graphics2D g2,
            Point p1,
            Point p2,
            String etiqueta,
            Color color,
            Color colorEtiqueta
    ) {

        double dx = p2.x - p1.x;
        double dy = p2.y - p1.y;

        double len =
                Math.sqrt(dx * dx + dy * dy);

        double ux = dx / len;
        double uy = dy / len;

        int sx =
                (int) (p1.x + ux * RADIO_ESTADO);

        int sy =
                (int) (p1.y + uy * RADIO_ESTADO);

        int ex =
                (int) (p2.x - ux * RADIO_ESTADO);

        int ey =
                (int) (p2.y - uy * RADIO_ESTADO);

        g2.setColor(color);

        g2.drawLine(sx, sy, ex, ey);

        dibujarPuntaFlecha(
                g2,
                ex,
                ey,
                Math.atan2(dy, dx),
                color
        );

        int mx = (sx + ex) / 2;
        int my = (sy + ey) / 2;

        dibujarEtiqueta(
                g2,
                etiqueta,
                mx,
                my - 8,
                colorEtiqueta
        );
    }

    // ─────────────────────────────────────────────
    // Arco curvo
    // ─────────────────────────────────────────────

    private void dibujarArcoCurvo(
            Graphics2D g2,
            Point p1,
            Point p2,
            String etiqueta,
            Color color,
            Color colorEtiqueta
    ) {

        double dx = p2.x - p1.x;
        double dy = p2.y - p1.y;

        double len =
                Math.sqrt(dx * dx + dy * dy);

        double ux = dx / len;
        double uy = dy / len;

        double curvatura = 35;

        int cx =
                (int) (
                        (p1.x + p2.x) / 2.0
                                - uy * curvatura
                );

        int cy =
                (int) (
                        (p1.y + p2.y) / 2.0
                                + ux * curvatura
                );

        int sx =
                (int) (p1.x + ux * RADIO_ESTADO);

        int sy =
                (int) (p1.y + uy * RADIO_ESTADO);

        int ex =
                (int) (p2.x - ux * RADIO_ESTADO);

        int ey =
                (int) (p2.y - uy * RADIO_ESTADO);

        QuadCurve2D curva =
                new QuadCurve2D.Float(
                        sx,
                        sy,
                        cx,
                        cy,
                        ex,
                        ey
                );

        g2.setColor(color);

        g2.draw(curva);

        dibujarPuntaFlecha(
                g2,
                ex,
                ey,
                Math.atan2(
                        ey - cy,
                        ex - cx
                ),
                color
        );

        dibujarEtiqueta(
                g2,
                etiqueta,
                cx,
                cy - 10,
                colorEtiqueta
        );
    }

    // ─────────────────────────────────────────────
    // Loop
    // ─────────────────────────────────────────────

    private void dibujarLoop(
            Graphics2D g2,
            Point p,
            String etiqueta,
            Color color,
            Color colorEtiqueta
    ) {

        int x = p.x - RADIO_ESTADO;
        int y = p.y - RADIO_ESTADO * 2 - 12;

        g2.setColor(color);

        g2.drawOval(
                x - RADIO_ESTADO,
                y - RADIO_ESTADO,
                RADIO_ESTADO * 2,
                RADIO_ESTADO * 2
        );

        dibujarEtiqueta(
                g2,
                etiqueta,
                x,
                y - RADIO_ESTADO - 10,
                colorEtiqueta
        );
    }

    // ─────────────────────────────────────────────
    // Etiquetas
    // ─────────────────────────────────────────────

    private void dibujarEtiqueta(
            Graphics2D g2,
            String texto,
            int x,
            int y,
            Color color
    ) {

        g2.setFont(
                new Font(
                        "Monospaced",
                        Font.BOLD,
                        11
                )
        );

        FontMetrics fm =
                g2.getFontMetrics();

        int ancho =
                fm.stringWidth(texto);

        g2.setColor(
                new Color(13, 17, 23, 180)
        );

        g2.fillRoundRect(
                x - ancho / 2 - 3,
                y - fm.getAscent(),
                ancho + 6,
                fm.getHeight(),
                4,
                4
        );

        g2.setColor(color);

        g2.drawString(
                texto,
                x - ancho / 2,
                y
        );
    }

    // ─────────────────────────────────────────────
    // Punta flecha
    // ─────────────────────────────────────────────

    private void dibujarPuntaFlecha(
            Graphics2D g2,
            int x,
            int y,
            double angulo,
            Color color
    ) {

        int tam = 10;

        double a1 =
                angulo + Math.PI * 0.8;

        double a2 =
                angulo - Math.PI * 0.8;

        int[] px = {
                x,
                x + (int) (tam * Math.cos(a1)),
                x + (int) (tam * Math.cos(a2))
        };

        int[] py = {
                y,
                y + (int) (tam * Math.sin(a1)),
                y + (int) (tam * Math.sin(a2))
        };

        g2.setColor(color);

        g2.fillPolygon(px, py, 3);
    }

    // ─────────────────────────────────────────────
    // Título
    // ─────────────────────────────────────────────

    private void dibujarTitulo(Graphics2D g2) {

        g2.setColor(
                new Color(88, 166, 255, 180)
        );

        g2.setFont(
                new Font(
                        "Monospaced",
                        Font.BOLD,
                        15
                )
        );

        g2.drawString(
                automata.getNombre(),
                16,
                24
        );
    }

    // ─────────────────────────────────────────────
    // Antialias
    // ─────────────────────────────────────────────

    private void activarAntialias(Graphics2D g2) {

        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        g2.setRenderingHint(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON
        );
    }
}