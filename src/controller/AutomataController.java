package controller;

import model.Automata;
import model.Estado;
import model.Transicion;
import service.ConversorAFDService;

import java.util.*;

/*---------------------------------------------------------------------------
   PROPOSITO: Controlar la lógica de creación y conversión de autómatas, gestionando el estado actual del autómata cargado y su posible conversión a AFD. 
              Proporciona métodos para crear un nuevo autómata a partir de los datos del formulario, 
              y para convertir un AFND al AFD equivalente utilizando el servicio de conversión.
   FECHA: 2026-05-16
   AUTOR: Victor Alfonso Pardo Gutierrez - Maryury Hernandez Marin
   VERSION: 1.3
   -----------------------------------------------------------------------------*/
public class AutomataController {

    private Automata automataActual = null;
    private Automata automataAFD = null;
    private final ConversorAFDService conversor = new ConversorAFDService();

    // ── Getters de estado ──────────────────────────────────────────────────────

    public Automata getAutomataActual() {
        return automataActual;
    }

    public Automata getAutomataAFD() {
        return automataAFD;
    }

    /** Devuelve el AFD si existe, de lo contrario el autómata actual. */
    public Automata getAutomataParaDiagrama() {
        return automataAFD != null ? automataAFD : automataActual;
    }

    public boolean hayAutomata() {
        return automataActual != null;
    }

    public boolean hayAFDConvertido() {
        return automataAFD != null;
    }

    public boolean esAFND() {
        return automataActual != null && automataActual.esAFND;
    }

    // ── Comandos ───────────────────────────────────────────────────────────────

    /**
     * Crea un nuevo autómata a partir de los parámetros del formulario.
     *
     * @param nombre      Nombre del autómata
     * @param tipo        "AFD" | "AFND"
     * @param estadosRaw  Cadena de estados separados por coma, ej: "q0, q1, q2"
     * @param inicial     Nombre del estado inicial
     * @param aceptRaw    Cadena de estados de aceptación separados por coma
     * @param alfabetoRaw Cadena del alfabeto separado por coma, ej: "a, b"
     * @param transRaw    Cada línea: "origen,simbolo,destino"
     * @throws IllegalArgumentException si algún dato es inválido
     */
    /*---------------------------------------------------------------------------
    PROPOSITO: Crear un nuevo autómata a partir de los parámetros del formulario. Se validan los datos de entrada para asegurar que el autómata se construya correctamente, 
               incluyendo la existencia del estado inicial, la no vaciedad del alfabeto y el formato correcto de las transiciones. 
               El método actualiza el estado del controlador con el nuevo autómata creado, y resetea cualquier conversión previa a AFD.
    FECHA: 2026-05-16
    AUTOR: Victor Alfonso Pardo Gutierrez - Maryury Hernandez Marin
    VERSION: 1.3
    -----------------------------------------------------------------------------*/
    public Automata crearAutomata(String nombre, String tipo,
            String estadosRaw, String inicial,
            String aceptRaw, String alfabetoRaw,
            String transRaw) {

        if (nombre == null || nombre.isBlank())
            throw new IllegalArgumentException("El nombre no puede estar vacío.");

        Automata a = new Automata(nombre.trim());

        // Estados de aceptación
        Set<String> acepSet = new HashSet<>();
        for (String s : aceptRaw.split(","))
            if (!s.isBlank())
                acepSet.add(s.trim());

        // Estados
        for (String s : estadosRaw.split(",")) {
            String n = s.trim();
            if (n.isEmpty())
                continue;
            a.estados.add(new Estado(n, n.equals(inicial.trim()), acepSet.contains(n)));
        }
        if (a.estados.isEmpty())
            throw new IllegalArgumentException("Debe definir al menos un estado.");
        if (a.getEstadoInicial() == null)
            throw new IllegalArgumentException("El estado inicial '" + inicial.trim()
                    + "' no existe en la lista de estados.");

        // Alfabeto
        for (String s : alfabetoRaw.split(","))
            if (!s.isBlank())
                a.alfabeto.add(s.trim());
        if (a.alfabeto.isEmpty())
            throw new IllegalArgumentException("El alfabeto no puede estar vacío.");

        // Transiciones
        int linea = 0;
        for (String rawLine : transRaw.split("\n")) {
            linea++;
            String line = rawLine.trim();
            if (line.isEmpty())
                continue;
            String[] p = line.split(",");
            if (p.length != 3)
                throw new IllegalArgumentException(
                        "Transición inválida en línea " + linea + ": \"" + line
                                + "\"\nFormato esperado: origen,simbolo,destino");
            a.transiciones.add(new Transicion(p[0].trim(), p[1].trim(), p[2].trim()));
        }

        a.esAFND = "AFND".equals(tipo);
        if (!a.esAFND)
            a.marcarEstadosError();

        this.automataActual = a;
        this.automataAFD = null;
        return a;
    }

    /**
     * Convierte el autómata actual (AFND) al AFD equivalente.
     *
     * @return El AFD generado
     * @throws IllegalStateException si no hay autómata o ya es AFD
     */
    /*---------------------------------------------------------------------------
    PROPOSITO: Implementar el algoritmo de subconjuntos para convertir un AFND en un AFD, 
               asegurando que se manejen correctamente los estados de error y trampa. 
               El método verifica que haya un autómata cargado y que sea un AFND antes de proceder con la conversión. 
               Si la conversión es exitosa, el nuevo AFD se almacena en el 
               estado del controlador para su uso posterior.
    FECHA: 2026-05-16
    AUTOR: Victor Alfonso Pardo Gutierrez - Maryury Hernandez Marin
    VERSION: 1.3
    -----------------------------------------------------------------------------*/
    public Automata convertirAFD() {
        if (automataActual == null)
            throw new IllegalStateException("No hay autómata cargado.");
        if (!automataActual.esAFND)
            throw new IllegalStateException("El autómata ya es un AFD.");

        automataAFD = conversor.convertir(automataActual);
        return automataAFD;
    }
}
