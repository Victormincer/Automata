/*
 * Proposito: Clase que permite minimizar un autómata finito determinista (AFD) utilizando el algoritmo de minimización de estados.
 *            Esta clase puede ser utilizada para reducir el número de estados de un AFD sin cambiar el lenguaje que acepta.
 * Autor: Victor Alfonso Pardo Guiterrez - Maryury Hernandez Marin
 * Fecha: 2024-5-16
 * versión: 1.0
 */
public class Minimizador {
    private Automata automata;

    public Minimizador(Automata automata) {
        this.automata = automata;
    }

    public Automata getAutomata() {
        return automata;
    }

    public void setAutomata(Automata automata) {
        this.automata = automata;
    }

    /**
     * Minimiza el autómata finito determinista (AFD) utilizando el algoritmo de particiones.
     * El algoritmo agrupa estados equivalentes en una sola partición.
     * 
     * @return Un nuevo Automata minimizado
     */
    public Automata minimizar() {
        if (automata == null || automata.getEstados() == null) {
            return null;
        }

        String[] estados = automata.getEstados();
        String[] alfabeto = automata.getAlfabeto();
        String estadoInicial = automata.getEstadoInicial();
        String[] estadosFinales = automata.getEstadosFinales();
        String[][] transiciones = automata.getTransiciones();

        // Paso 1: Crear partición inicial (estados de aceptación vs no aceptación)
        java.util.List<java.util.Set<String>> particiones = crearParticionesIniciales(estados, estadosFinales);

        // Paso 2: Refinar particiones iterativamente
        java.util.List<java.util.Set<String>> nuevasParticiones;
        do {
            nuevasParticiones = refinarParticiones(particiones, transiciones, alfabeto);
            if (nuevasParticiones.size() == particiones.size()) {
                break;
            }
            particiones = nuevasParticiones;
        } while (true);

        // Paso 3: Construir el autómata minimizado
        return construirAutomataMinimizado(particiones, estadoInicial, estadosFinales, transiciones, alfabeto);
    }

    /**
     * Crea las particiones iniciales separando estados finales de no finales.
     */
    private java.util.List<java.util.Set<String>> crearParticionesIniciales(String[] estados, String[] estadosFinales) {
        java.util.List<java.util.Set<String>> particiones = new java.util.ArrayList<>();
        java.util.Set<String> estadosFinalesSet = new java.util.HashSet<>();
        java.util.Set<String> estadosNoFinales = new java.util.HashSet<>();

        for (String estado : estadosFinales) {
            estadosFinalesSet.add(estado);
        }

        for (String estado : estados) {
            if (estadosFinalesSet.contains(estado)) {
                continue;
            }
            estadosNoFinales.add(estado);
        }

        if (!estadosNoFinales.isEmpty()) {
            particiones.add(estadosNoFinales);
        }
        if (!estadosFinalesSet.isEmpty()) {
            particiones.add(estadosFinalesSet);
        }

        return particiones;
    }

    /**
     * Refina las particiones actuales comparando transiciones.
     */
    private java.util.List<java.util.Set<String>> refinarParticiones(
            java.util.List<java.util.Set<String>> particiones, String[][] transiciones, String[] alfabeto) {
        java.util.List<java.util.Set<String>> nuevasParticiones = new java.util.ArrayList<>();

        for (java.util.Set<String> particion : particiones) {
            java.util.Map<String, java.util.Set<String>> grupos = new java.util.HashMap<>();

            for (String estado : particion) {
                String firma = crearFirmaEstado(estado, particiones, transiciones, alfabeto);
                grupos.computeIfAbsent(firma, k -> new java.util.HashSet<>()).add(estado);
            }

            nuevasParticiones.addAll(grupos.values());
        }

        return nuevasParticiones;
    }

    /**
     * Crea una firma para cada estado basada en sus transiciones y particiones.
     */
    private String crearFirmaEstado(String estado, java.util.List<java.util.Set<String>> particiones,
            String[][] transiciones, String[] alfabeto) {
        StringBuilder firma = new StringBuilder();

        for (String simbolo : alfabeto) {
            String destino = obtenerDestino(estado, simbolo, transiciones);
            int particionDestino = obtenerParticionDeEstado(destino, particiones);
            firma.append(particionDestino).append("|");
        }

        return firma.toString();
    }

    /**
     * Obtiene el estado destino de una transición.
     */
    private String obtenerDestino(String origen, String simbolo, String[][] transiciones) {
        int indiceOrigen = -1;

        for (int i = 0; i < transiciones.length; i++) {
            if (transiciones[i][0].equals(origen)) {
                indiceOrigen = i;
                break;
            }
        }

        if (indiceOrigen != -1) {
            for (int j = 1; j < transiciones[indiceOrigen].length; j += 2) {
                if (transiciones[indiceOrigen][j].equals(simbolo)) {
                    return transiciones[indiceOrigen][j + 1];
                }
            }
        }

        return null;
    }

    /**
     * Obtiene el índice de la partición a la que pertenece un estado.
     */
    private int obtenerParticionDeEstado(String estado, java.util.List<java.util.Set<String>> particiones) {
        if (estado == null) {
            return -1;
        }
        for (int i = 0; i < particiones.size(); i++) {
            if (particiones.get(i).contains(estado)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Construye el autómata minimizado a partir de las particiones finales.
     */
    private Automata construirAutomataMinimizado(java.util.List<java.util.Set<String>> particiones,
            String estadoInicial, String[] estadosFinales, String[][] transiciones, String[] alfabeto) {

        java.util.Map<String, String> mapaEquivalencia = crearMapaEquivalencia(particiones);

        String[] estadosMinimizados = new String[particiones.size()];
        for (int i = 0; i < particiones.size(); i++) {
            estadosMinimizados[i] = String.valueOf(i);
        }

        String estadoInicialMinimizado = mapaEquivalencia.get(estadoInicial);

        String[] estadosFinalesMinimizados = obtenerEstadosFinalesMinimizados(estadosFinales, mapaEquivalencia,
                particiones);

        String[][] transicionesMinimizadas = construirTransicionesMinimizadas(particiones, mapaEquivalencia,
                transiciones, alfabeto);

        return new Automata(estadosMinimizados, alfabeto, estadoInicialMinimizado, estadosFinalesMinimizados,
                transicionesMinimizadas);
    }

    /**
     * Crea un mapa que relaciona estados originales con sus estados equivalentes minimizados.
     */
    private java.util.Map<String, String> crearMapaEquivalencia(java.util.List<java.util.Set<String>> particiones) {
        java.util.Map<String, String> mapa = new java.util.HashMap<>();
        for (int i = 0; i < particiones.size(); i++) {
            for (String estado : particiones.get(i)) {
                mapa.put(estado, String.valueOf(i));
            }
        }
        return mapa;
    }

    /**
     * Obtiene los estados finales minimizados.
     */
    private String[] obtenerEstadosFinalesMinimizados(String[] estadosFinales,
            java.util.Map<String, String> mapaEquivalencia, java.util.List<java.util.Set<String>> particiones) {
        java.util.Set<String> estadosFinalesMin = new java.util.HashSet<>();
        for (String estado : estadosFinales) {
            String equivalente = mapaEquivalencia.get(estado);
            if (equivalente != null) {
                estadosFinalesMin.add(equivalente);
            }
        }
        return estadosFinalesMin.toArray(new String[0]);
    }

    /**
     * Construye las transiciones del autómata minimizado.
     */
    private String[][] construirTransicionesMinimizadas(java.util.List<java.util.Set<String>> particiones,
            java.util.Map<String, String> mapaEquivalencia, String[][] transiciones, String[] alfabeto) {

        java.util.Set<String> transicionesUnicas = new java.util.HashSet<>();
        java.util.List<String[]> transicionesList = new java.util.ArrayList<>();

        for (java.util.Set<String> particion : particiones) {
            String estadoRepresentante = particion.iterator().next();
            String estadoMinimizado = mapaEquivalencia.get(estadoRepresentante);

            for (String simbolo : alfabeto) {
                String destino = obtenerDestino(estadoRepresentante, simbolo, transiciones);
                if (destino != null) {
                    String destinoMinimizado = mapaEquivalencia.get(destino);
                    String transicion = estadoMinimizado + "-" + simbolo + "-" + destinoMinimizado;
                    if (!transicionesUnicas.contains(transicion)) {
                        transicionesUnicas.add(transicion);
                        String[] fila = { estadoMinimizado, simbolo, destinoMinimizado };
                        transicionesList.add(fila);
                    }
                }
            }
        }

        return transicionesList.toArray(new String[0][]);
    }
}
