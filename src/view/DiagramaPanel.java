package view;

import model.Automata;
import model.Estado;
import model.Transicion;
import utils.UIStyle;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.util.*;
import java.util.List;

/*---------------------------------------------------------------------------
PROPOSITO: Crea el panel para mostrar el diagrama del autómata.
FECHA: 2026-05-16
AUTOR: Victor Alfonso Pardo Gutierrez - Maryury Hernandez Marin
VERSION: 1.3
ACTUALIZACIÓN: 2026-05-23 - Se mejoró el diseño del diagrama, 
              con mejores colores, fuentes y estilos de línea.
-----------------------------------------------------------------------------*/
public class DiagramaPanel extends JPanel {

    private final Automata automata;
    private final Map<String, Point> posiciones = new LinkedHashMap<>();
    private static final int R = 30; // radio del círculo de estado
    private static final int SL = 26; // radio del self-loop

    /*---------------------------------------------------------------------------
    PROPOSITO: Constructor del panel de diagrama. Calcula las posiciones de los estados.
    FECHA: 2026-05-23
    AUTOR: Victor Alfonso Pardo Gutierrez - Maryury Hernandez Marin
    VERSION: 1.3
    -----------------------------------------------------------------------------*/
    public DiagramaPanel(Automata automata) {
        this.automata = automata;
        setBackground(UIStyle.BG);
        calcularPosiciones();
    }

    /*---------------------------------------------------------------------------
    PROPOSITO: Calcula las posiciones de los estados para el diagrama. 
                Los estados normales se distribuyen en un círculo, 
                y el estado Error (si existe) se coloca separado 
                en la esquina inferior derecha.
    FECHA: 2026-05-23
    AUTOR: Victor Alfonso Pardo Gutierrez - Maryury Hernandez Marin
    VERSION: 1.3
    -----------------------------------------------------------------------------*/
    private void calcularPosiciones() {
        List<Estado> normales = new ArrayList<>();
        Estado estadoError = null;

        for (Estado est : automata.estados) {
            if (est.esError)
                estadoError = est;
            else
                normales.add(est);
        }

        int n = normales.size();
        int cx = 370, cy = 265;
        int radioDisp = Math.min(195, 45 + n * 30);

        if (n == 1) {
            posiciones.put(normales.get(0).nombre, new Point(cx, cy));
        } else {
            for (int i = 0; i < n; i++) {
                double ang = 2 * Math.PI * i / n - Math.PI / 2;
                posiciones.put(normales.get(i).nombre,
                        new Point(cx + (int) (radioDisp * Math.cos(ang)),
                                cy + (int) (radioDisp * Math.sin(ang))));
            }
        }

        // El estado Error se ubica en la esquina inferior derecha, separado
        if (estadoError != null) {
            posiciones.put(estadoError.nombre,
                    new Point(cx + radioDisp + 100, cy + radioDisp + 55));
        }
    }

    /*---------------------------------------------------------------------------
    PROPOSITO: Dibuja el diagrama del autómata. Se dibujan primero los arcos (debajo) 
               y luego los nodos (encima).
    FECHA: 2026-05-23
    AUTOR: Victor Alfonso Pardo Gutierrez - Maryury Hernandez Marin
    VERSION: 1.3
    -----------------------------------------------------------------------------*/
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

        // Mapa nombre → Estado para consultas rápidas
        Map<String, Estado> mapaEst = new LinkedHashMap<>();
        for (Estado e : automata.estados)
            mapaEst.put(e.nombre, e);

        // Agrupar transiciones por (origen, destino) → lista de símbolos
        Map<String, List<String>> arcos = new LinkedHashMap<>();
        for (Transicion t : automata.transiciones) {
            String key = t.estadoOrigen + "##" + t.estadoDestino;
            arcos.computeIfAbsent(key, k -> new ArrayList<>()).add(t.simbolo);
        }

        // 1. Dibujar arcos (debajo de los nodos)
        dibujarArcos(g2, arcos, mapaEst);

        // 2. Dibujar nodos (encima)
        dibujarEstados(g2, mapaEst);

        // 3. Nombre del autómata en esquina superior izquierda
        g2.setColor(new Color(88, 166, 255, 180));
        g2.setFont(UIStyle.FONT_MONO_HEADER);
        g2.drawString(automata.nombre, 14, 26);
    }

    /*---------------------------------------------------------------------------
    PROPOSITO: Dibuja los arcos entre estados. Se manejan tres casos: 
                self-loop, arco recto y arco curvo (si hay arco inverso).
                Los arcos de error se dibujan en rojo y punteados.
    FECHA: 2026-05-23
    AUTOR: Victor Alfonso Pardo Gutierrez - Maryury Hernandez Marin
    VERSION: 1.3
    -----------------------------------------------------------------------------*/
    private void dibujarArcos(Graphics2D g2,
            Map<String, List<String>> arcos,
            Map<String, Estado> mapaEst) {

        BasicStroke normal = new BasicStroke(1.7f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
        BasicStroke puntead = new BasicStroke(1.5f, BasicStroke.CAP_ROUND,
                BasicStroke.JOIN_ROUND, 0, new float[] { 5, 3 }, 0);

        for (Map.Entry<String, List<String>> entry : arcos.entrySet()) {
            String[] parts = entry.getKey().split("##");
            String origen = parts[0];
            String destino = parts[1];
            String etiq = String.join(",", entry.getValue());

            Point p1 = posiciones.get(origen);
            Point p2 = posiciones.get(destino);
            if (p1 == null || p2 == null)
                continue;

            Estado eOrigen = mapaEst.get(origen);
            Estado eDestino = mapaEst.get(destino);
            boolean esErr = (eOrigen != null && eOrigen.esError)
                    || (eDestino != null && eDestino.esError);

            Color colArco = esErr ? UIStyle.ACCENT_R.darker() : new Color(160, 175, 190);
            Color colLbl = esErr ? UIStyle.ACCENT_R : new Color(210, 160, 50);

            g2.setStroke(esErr ? puntead : normal);
            g2.setColor(colArco);

            if (origen.equals(destino)) {
                // ── Self-loop ────────────────────────────────────────────────
                dibujarSelfLoop(g2, p1, etiq, colArco, colLbl, eOrigen);
            } else {
                boolean hayInverso = arcos.containsKey(destino + "##" + origen);
                if (hayInverso)
                    dibujarArcoCurvo(g2, p1, p2, etiq, colArco, colLbl);
                else
                    dibujarArcoRecto(g2, p1, p2, etiq, colArco, colLbl);
            }
            g2.setStroke(normal);
        }
    }

    /*---------------------------------------------------------------------------
    PROPOSITO: Dibuja un self-loop (bucle) sobre un estado. 
               El bucle se dibuja como un óvalo encima del nodo, 
               con la punta de flecha tocando el nodo en la parte superior. 
               La etiqueta se coloca sobre el bucle.
               Si el estado es de error, el bucle se dibuja en rojo.
    FECHA: 2026-05-23
    AUTOR: Victor Alfonso Pardo Gutierrez - Maryury Hernandez Marin
    VERSION: 1.3
    -----------------------------------------------------------------------------*/
    private void dibujarSelfLoop(Graphics2D g2, Point p,
            String etiq, Color colArco, Color colLbl,
            Estado estado) {
        // Centro del bucle: directamente encima del nodo
        int loopCX = p.x;
        int loopCY = p.y - R - SL;

        // Dibujamos el óvalo del self-loop
        g2.setColor(colArco);
        g2.drawOval(loopCX - SL, loopCY - SL, SL * 2, SL * 2);

        // Punta de flecha en el punto donde el bucle toca el nodo (parte superior)
        int arrowX = p.x;
        int arrowY = p.y - R;
        dibujarPunta(g2, arrowX, arrowY, Math.PI / 2 + 0.4, colArco);

        // Etiqueta sobre el bucle
        dibujarEtiqueta(g2, etiq, loopCX, loopCY - SL - 8, colLbl);
    }

    /*---------------------------------------------------------------------------
    PROPOSITO: Dibuja un arco recto entre dos estados. El arco se dibuja como una línea recta 
               con una punta de flecha en el destino. La etiqueta se coloca en el medio del arco.
               Si el arco es de error, se dibuja en rojo.
    FECHA: 2026-05-23
    AUTOR: Victor Alfonso Pardo Gutierrez - Maryury Hernandez Marin
    VERSION: 1.3
    -----------------------------------------------------------------------------*/
    private void dibujarArcoRecto(Graphics2D g2, Point p1, Point p2,
            String etiq, Color col, Color colLbl) {
        double dx = p2.x - p1.x, dy = p2.y - p1.y;
        double len = Math.sqrt(dx * dx + dy * dy);
        double ux = dx / len, uy = dy / len;

        int sx = (int) (p1.x + ux * R), sy = (int) (p1.y + uy * R);
        int ex = (int) (p2.x - ux * (R + 1)), ey = (int) (p2.y - uy * (R + 1));

        g2.setColor(col);
        g2.drawLine(sx, sy, ex, ey);
        dibujarPunta(g2, ex, ey, Math.atan2(dy, dx), col);
        dibujarEtiqueta(g2, etiq, (sx + ex) / 2, (sy + ey) / 2 - 10, colLbl);
    }

    /*---------------------------------------------------------------------------
    PROPOSITO: Dibuja un arco curvo entre dos estados. El arco se dibuja como una curva cuadrática 
               con una punta de flecha en el destino. La etiqueta se coloca en el medio del arco.
               Si el arco es de error, se dibuja en rojo.
    FECHA: 2026-05-23
    AUTOR: Victor Alfonso Pardo Gutierrez - Maryury Hernandez Marin
    VERSION: 1.3
    -----------------------------------------------------------------------------*/
    private void dibujarArcoCurvo(Graphics2D g2, Point p1, Point p2,
            String etiq, Color col, Color colLbl) {
        double dx = p2.x - p1.x, dy = p2.y - p1.y;
        double len = Math.sqrt(dx * dx + dy * dy);
        double ux = dx / len, uy = dy / len;
        double perp = 35;

        int ctrlX = (int) ((p1.x + p2.x) / 2.0 - uy * perp);
        int ctrlY = (int) ((p1.y + p2.y) / 2.0 + ux * perp);

        int sx = (int) (p1.x + ux * R), sy = (int) (p1.y + uy * R);
        int ex = (int) (p2.x - ux * (R + 1)), ey = (int) (p2.y - uy * (R + 1));

        QuadCurve2D curve = new QuadCurve2D.Float(sx, sy, ctrlX, ctrlY, ex, ey);
        g2.setColor(col);
        g2.draw(curve);
        dibujarPunta(g2, ex, ey, Math.atan2(ey - ctrlY, ex - ctrlX), col);
        dibujarEtiqueta(g2, etiq, ctrlX, ctrlY - 10, colLbl);
    }

    /*---------------------------------------------------------------------------
    PROPOSITO: Dibuja los estados del autómata. Cada estado se dibuja como un círculo con su nombre. 
               El estado inicial tiene un borde azul y una flecha de entrada. 
               El estado de aceptación tiene un doble círculo. 
               El estado de error se dibuja en rojo con una cruz interior y la etiqueta "Error Ø".
    FECHA: 2026-05-23
    AUTOR: Victor Alfonso Pardo Gutierrez - Maryury Hernandez Marin
    VERSION: 1.3
    -----------------------------------------------------------------------------*/

    private void dibujarEstados(Graphics2D g2, Map<String, Estado> mapaEst) {
        for (Estado est : automata.estados) {
            Point p = posiciones.get(est.nombre);
            if (p == null)
                continue;

            // Sombra
            g2.setColor(new Color(0, 0, 0, 55));
            g2.fillOval(p.x - R + 3, p.y - R + 3, R * 2, R * 2);

            if (est.esError) {
                dibujarEstadoError(g2, p, est);
            } else {
                dibujarEstadoNormal(g2, p, est);
            }
        }
    }

    /*---------------------------------------------------------------------------
    PROPOSITO: Dibuja un estado normal (no error) del autómata. El estado se dibuja como un círculo con su nombre. 
               El estado inicial tiene un borde azul y una flecha de entrada. 
               El estado de aceptación tiene un doble círculo.
    FECHA: 2026-05-23
    AUTOR: Victor Alfonso Pardo Gutierrez - Maryury Hernandez Marin
    VERSION: 1.3
    -----------------------------------------------------------------------------*/
    private void dibujarEstadoNormal(Graphics2D g2, Point p, Estado est) {
        // Relleno
        Color relleno;
        if (est.esInicial)
            relleno = new Color(25, 48, 78);
        else if (est.esAceptacion)
            relleno = new Color(18, 52, 28);
        else
            relleno = new Color(28, 33, 42);
        g2.setColor(relleno);
        g2.fillOval(p.x - R, p.y - R, R * 2, R * 2);

        // Borde
        g2.setStroke(new BasicStroke(2f));
        Color borde;
        if (est.esInicial)
            borde = UIStyle.ACCENT;
        else if (est.esAceptacion)
            borde = UIStyle.ACCENT_G;
        else
            borde = UIStyle.BORDER;
        g2.setColor(borde);
        g2.drawOval(p.x - R, p.y - R, R * 2, R * 2);

        // Doble círculo (estado de aceptación)
        if (est.esAceptacion) {
            int ri = R - 5;
            g2.setStroke(new BasicStroke(1.3f));
            g2.drawOval(p.x - ri, p.y - ri, ri * 2, ri * 2);
        }

        // Flecha de entrada (estado inicial)
        if (est.esInicial) {
            g2.setColor(UIStyle.ACCENT);
            g2.setStroke(new BasicStroke(2f));
            g2.drawLine(p.x - R - 32, p.y, p.x - R - 1, p.y);
            dibujarPunta(g2, p.x - R, p.y, 0, UIStyle.ACCENT);
        }

        // Nombre del estado
        g2.setColor(UIStyle.FG);
        g2.setFont(new Font("Monospaced", Font.BOLD, 13));
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(est.nombre,
                p.x - fm.stringWidth(est.nombre) / 2,
                p.y + fm.getAscent() / 2 - 1);
    }

    /*---------------------------------------------------------------------------
    PROPOSITO: Dibuja un estado de error del autómata. 
               El estado se dibuja como un círculo rojo con una cruz interior
                y la etiqueta "Error Ø" debajo.
    FECHA: 2026-05-23
    AUTOR: Victor Alfonso Pardo Gutierrez - Maryury Hernandez Marin
    VERSION: 1.3
    -----------------------------------------------------------------------------*/
    private void dibujarEstadoError(Graphics2D g2, Point p, Estado est) {
        // Relleno oscuro rojizo
        g2.setColor(new Color(55, 14, 14));
        g2.fillOval(p.x - R, p.y - R, R * 2, R * 2);

        // Patrón de rayas diagonales dentro del círculo
        Shape clipPrev = g2.getClip();
        g2.setClip(new Ellipse2D.Float(p.x - R, p.y - R, R * 2, R * 2));
        g2.setColor(new Color(248, 81, 73, 38));
        g2.setStroke(new BasicStroke(2f));
        for (int d = -R * 2; d <= R * 2; d += 9)
            g2.drawLine(p.x - R + d, p.y - R, p.x + R + d, p.y + R);
        g2.setClip(clipPrev);

        // Borde rojo
        g2.setColor(UIStyle.ACCENT_R);
        g2.setStroke(new BasicStroke(2.4f));
        g2.drawOval(p.x - R, p.y - R, R * 2, R * 2);

        // Cruz ✕ interior
        int off = 10;
        g2.setStroke(new BasicStroke(2.4f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.setColor(new Color(248, 81, 73, 210));
        g2.drawLine(p.x - off, p.y - off, p.x + off, p.y + off);
        g2.drawLine(p.x + off, p.y - off, p.x - off, p.y + off);

        // Etiqueta "Error Ø" debajo del nodo en rojo
        g2.setFont(new Font("Monospaced", Font.BOLD, 12));
        g2.setColor(UIStyle.ACCENT_R);
        String lblErr = "Error \u00D8"; // Error Ø
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(lblErr, p.x - fm.stringWidth(lblErr) / 2, p.y + R + 16);
    }

    /*---------------------------------------------------------------------------
    PROPOSITO: Dibuja una punta de flecha en la posición (x, y) con el ángulo dado. 
               La punta se dibuja como un triángulo isósceles con la base 
               perpendicular al ángulo.
               El color de la punta se especifica por el parámetro col.
    FECHA: 2026-05-23
    AUTOR: Victor Alfonso Pardo Gutierrez - Maryury Hernandez Marin
    VERSION: 1.3
    -----------------------------------------------------------------------------*/
    private void dibujarPunta(Graphics2D g2, int x, int y, double angulo, Color col) {
        int len = 11;
        double a1 = angulo + Math.PI * 0.78;
        double a2 = angulo - Math.PI * 0.78;
        int[] px = { x, x + (int) (len * Math.cos(a1)), x + (int) (len * Math.cos(a2)) };
        int[] py = { y, y + (int) (len * Math.sin(a1)), y + (int) (len * Math.sin(a2)) };
        g2.setColor(col);
        g2.fillPolygon(px, py, 3);
    }

    /*---------------------------------------------------------------------------
    PROPOSITO: Dibuja una etiqueta de texto con fondo semitransparente en la posición (x, y). 
               El fondo es un rectángulo redondeado que mejora la legibilidad del texto. 
               El color del texto se especifica por el parámetro col.
    FECHA: 2026-05-23
    AUTOR: Victor Alfonso Pardo Gutierrez - Maryury Hernandez Marin
    VERSION: 1.3
    -----------------------------------------------------------------------------*/
    private void dibujarEtiqueta(Graphics2D g2, String txt, int x, int y, Color col) {
        g2.setFont(new Font("Monospaced", Font.BOLD, 11));
        FontMetrics fm = g2.getFontMetrics();
        int tw = fm.stringWidth(txt);
        g2.setColor(new Color(13, 17, 23, 190));
        g2.fillRoundRect(x - tw / 2 - 4, y - fm.getAscent(),
                tw + 8, fm.getHeight(), 5, 5);
        g2.setColor(col);
        g2.drawString(txt, x - tw / 2, y);
    }
}
