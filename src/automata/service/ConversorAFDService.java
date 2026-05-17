package automata.service;

import automata.model.*;

import java.util.*;

public class ConversorAFDService {

    public Automata convertir(Automata afnd) {

        Automata afd = new Automata(
                afnd.getNombre() + "_AFD"
        );

        afd.getAlfabeto().addAll(
                afnd.getAlfabeto()
        );

        afd.getAlfabeto().remove("ε");

        Map<Set<String>, String> nombres =
                new LinkedHashMap<>();

        Queue<Set<String>> cola =
                new LinkedList<>();

        Set<String> inicial =
                clausuraEpsilon(
                        afnd,
                        new LinkedHashSet<>(
                                Collections.singleton(
                                        afnd.getEstadoInicial()
                                )
                        )
                );

        String nombreInicial =
                nombreConjunto(inicial);

        nombres.put(inicial, nombreInicial);

        cola.add(inicial);

        afd.getEstados().add(
                new Estado(
                        nombreInicial,
                        true,
                        contieneAceptacion(afnd, inicial)
                )
        );

        while (!cola.isEmpty()) {

            Set<String> actual = cola.poll();

            String nombreActual =
                    nombres.get(actual);

            for (String simbolo : afd.getAlfabeto()) {

                Set<String> mover =
                        mover(afnd, actual, simbolo);

                Set<String> destino =
                        clausuraEpsilon(afnd, mover);

                if (destino.isEmpty())
                    continue;

                if (!nombres.containsKey(destino)) {

                    String nombreDestino =
                            nombreConjunto(destino);

                    nombres.put(destino, nombreDestino);

                    cola.add(destino);

                    afd.getEstados().add(
                            new Estado(
                                    nombreDestino,
                                    false,
                                    contieneAceptacion(afnd, destino)
                            )
                    );
                }

                afd.getTransiciones().add(
                        new Transicion(
                                nombreActual,
                                simbolo,
                                nombres.get(destino)
                        )
                );
            }
        }

        return afd;
    }

    private Set<String> clausuraEpsilon(
            Automata a,
            Set<String> estados
    ) {

        Set<String> resultado =
                new LinkedHashSet<>(estados);

        Stack<String> pila = new Stack<>();

        pila.addAll(estados);

        while (!pila.isEmpty()) {

            String actual = pila.pop();

            for (Transicion t : a.getTransiciones()) {

                if (t.getOrigen().equals(actual)
                        && t.getSimbolo().equals("ε")
                        && !resultado.contains(t.getDestino())) {

                    resultado.add(t.getDestino());

                    pila.push(t.getDestino());
                }
            }
        }

        return resultado;
    }

    private Set<String> mover(
            Automata a,
            Set<String> estados,
            String simbolo
    ) {

        Set<String> resultado =
                new LinkedHashSet<>();

        for (String estado : estados) {

            for (Transicion t : a.getTransiciones()) {

                if (t.getOrigen().equals(estado)
                        && t.getSimbolo().equals(simbolo)) {

                    resultado.add(t.getDestino());
                }
            }
        }

        return resultado;
    }

    private boolean contieneAceptacion(
            Automata a,
            Set<String> estados
    ) {

        return estados.stream()
                .anyMatch(s -> a.getEstado(s).isAceptacion());
    }

    private String nombreConjunto(Set<String> conjunto) {

        return "{" + String.join(",", conjunto) + "}";
    }
}