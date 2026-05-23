package service;

import model.Automata;
import model.Estado;
import model.Transicion;

import java.util.*;

/*---------------------------------------------------------------------------
PROPOSITO:  Implementar el algoritmo de subconjuntos para convertir un AFND en un AFD, 
            asegurando que se manejen correctamente los estados de error y trampa.
PARAMETROS; Automata afnd: El autómata no determinista a convertir 
            (debe tener esAFND == true).
RETORNO:     Un nuevo Automata de tipo AFD que es equivalente al AFND dado
FECHA:      2026-05-16
AUTOR:      Victor Alfonso Pardo Gutierrez - Maryury Hernandez Marin
VERSION:    1.3
-----------------------------------------------------------------------------*/
public class ConversorAFDService {

    /**
     * Convierte el AFND dado en un AFD equivalente.
     *
     * @param afnd Autómata origen (debe tener esAFND == true)
     * @return Nuevo Automata de tipo AFD con estado Error si aplica
     */
    /*---------------------------------------------------------------------------
    PROPOSITO: Implementar el algoritmo de subconjuntos para convertir un AFND en un AFD, 
               asegurando que se manejen correctamente los estados de error y trampa.
    
    FECHA: 2026-05-16
    AUTOR: Victor Alfonso Pardo Gutierrez - Maryury Hernandez Marin
    VERSION: 1.3
    -----------------------------------------------------------------------------*/
    public Automata convertir(Automata afnd) {
        Automata afd = new Automata(afnd.nombre + "_AFD");
        afd.alfabeto.addAll(afnd.alfabeto);
        afd.alfabeto.remove("ε");

        Map<Set<String>, String> nombreConjunto = new LinkedHashMap<>();
        Deque<Set<String>> pendientes = new ArrayDeque<>();

        // Estado inicial del AFD = clausura-ε del estado inicial del AFND
        Set<String> conjInicial = clausuraEpsilon(afnd,
                Collections.singleton(afnd.getEstadoInicial()));
        String nomInicial = conjuntoANombre(conjInicial);
        nombreConjunto.put(conjInicial, nomInicial);
        pendientes.add(conjInicial);

        boolean esAcepInicial = conjInicial.stream().anyMatch(e -> {
            Estado st = afnd.getEstado(e);
            return st != null && st.esAceptacion;
        });
        afd.estados.add(new Estado(nomInicial, true, esAcepInicial));

        // Algoritmo de subconjuntos
        while (!pendientes.isEmpty()) {
            Set<String> actual = pendientes.poll();
            String nomActual = nombreConjunto.get(actual);

            for (String sym : afd.alfabeto) {
                Set<String> destino = clausuraEpsilon(afnd, mover(afnd, actual, sym));

                if (destino.isEmpty()) {
                    // Transición sin destino → estado Error
                    afd.transiciones.add(
                            new Transicion(nomActual, sym, Estado.NOMBRE_ERROR));
                } else {
                    if (!nombreConjunto.containsKey(destino)) {
                        String nomDest = conjuntoANombre(destino);
                        nombreConjunto.put(destino, nomDest);
                        pendientes.add(destino);

                        boolean esAcep = destino.stream().anyMatch(e -> {
                            Estado st = afnd.getEstado(e);
                            return st != null && st.esAceptacion;
                        });
                        afd.estados.add(new Estado(nomDest, false, esAcep));
                    }
                    afd.transiciones.add(
                            new Transicion(nomActual, sym, nombreConjunto.get(destino)));
                }
            }
        }

        // Si hay transiciones al estado Error, crear dicho estado con self-loops
        boolean necesitaError = afd.transiciones.stream()
                .anyMatch(t -> t.estadoDestino.equals(Estado.NOMBRE_ERROR));
        if (necesitaError) {
            afd.estados.add(new Estado(Estado.NOMBRE_ERROR, false, false, true));
            for (String sym : afd.alfabeto) {
                afd.transiciones.add(
                        new Transicion(Estado.NOMBRE_ERROR, sym, Estado.NOMBRE_ERROR));
            }
        }

        // Detectar estados trampa adicionales
        afd.marcarEstadosError();
        return afd;
    }

    // ── Algoritmos auxiliares ─────────────────────────────────────────────────

    /*---------------------------------------------------------------------------
    PROPOSITO: Calcular la clausura-ε de un conjunto de estados en el AFND, es decir, el conjunto 
               de estados alcanzables desde el conjunto dado siguiendo solo transiciones ε.
    FECHA: 2026-05-16
    AUTOR: Victor Alfonso Pardo Gutierrez - Maryury Hernandez Marin
    VERSION: 1.3
    -----------------------------------------------------------------------------*/
    private Set<String> clausuraEpsilon(
            Automata afnd, Set<String> conj) {
        Set<String> resultado = new LinkedHashSet<>(conj);
        Deque<String> pila = new ArrayDeque<>(conj);
        while (!pila.isEmpty()) {
            String est = pila.pop();
            for (Transicion t : afnd.transiciones) {
                if (t.estadoOrigen.equals(est) && t.simbolo.equals("ε")
                        && !resultado.contains(t.estadoDestino)) {
                    resultado.add(t.estadoDestino);
                    pila.push(t.estadoDestino);
                }
            }
        }
        return resultado;
    }

    /*---------------------------------------------------------------------------
    PROPOSITO: Calcular los estados alcanzables desde un conjunto con el símbolo dado.
    FECHA: 2026-05-16
    AUTOR: Victor Alfonso Pardo Gutierrez - Maryury Hernandez Marin
    VERSION: 1.3
    -----------------------------------------------------------------------------*/
    private Set<String> mover(Automata afnd, Set<String> conj, String simbolo) {
        Set<String> resultado = new LinkedHashSet<>();
        for (String est : conj)
            for (Transicion t : afnd.transiciones)
                if (t.estadoOrigen.equals(est) && t.simbolo.equals(simbolo))
                    resultado.add(t.estadoDestino);
        return resultado;
    }

    /*---------------------------------------------------------------------------
    PROPOSITO: Convertir un conjunto de estados en una representación 
                de nombre única, por ejemplo, 
               usando llaves y comas para delimitar los estados (e.g., {q0,q1,q2}).
    FECHA: 2026-05-16
    AUTOR: Victor Alfonso Pardo Gutierrez - Maryury Hernandez Marin
    VERSION: 1.3
    -----------------------------------------------------------------------------*/
    private String conjuntoANombre(Set<String> conj) {
        return "{" + String.join(",", conj) + "}";
    }
}
