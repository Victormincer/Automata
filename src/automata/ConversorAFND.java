import java.util.*;

/*
 * Proposito: Clase que permite convertir un autómata finito no determinista (AFND) a un autómata finito determinista (AFD).
 *            Esta clase puede ser utilizada para realizar la conversión de un AFND a un AFD.
 * Autor: Victor Alfonso Pardo Guiterrez - Maryury Hernandez Marin
 * Fecha: 2024-5-16
 * versión: 1.0
 */
public class ConversorAFND {
    
    private Automata afnd;
    private Set<String> estadosVisitados;
    private Set<String> estadosFinalesAFD;
    private List<String[]> transicionesAFD;
    private Queue<Set<String>> colaEstados;
    private Map<String, Set<String>> mapaEquivalencia;

    public ConversorAFND(Automata afnd) {
        this.afnd = afnd;
        this.estadosVisitados = new HashSet<>();
        this.estadosFinalesAFD = new HashSet<>();
        this.transicionesAFD = new ArrayList<>();
        this.colaEstados = new LinkedList<>();
        this.mapaEquivalencia = new HashMap<>();
    }

    public Automata convertir() {
        if (afnd == null) {
            throw new IllegalArgumentException("El AFND no puede ser nulo");
        }

        String[] alfabeto = afnd.getAlfabeto();
        String estadoInicial = afnd.getEstadoInicial();
        String[] estadosFinalesAFND = afnd.getEstadosFinales();
        String[][] transicionesAFND = afnd.getTransiciones();

        // Iniciar con el estado inicial
        Set<String> estadoInicial_conjunto = new HashSet<>();
        estadoInicial_conjunto.add(estadoInicial);
        
        // Nombre para el estado inicial del AFD (conjunto)
        String nombreEstadoInicial = convertirConjuntoAString(estadoInicial_conjunto);
        colaEstados.add(estadoInicial_conjunto);
        estadosVisitados.add(nombreEstadoInicial);
        
        // Procesar BFS para crear todos los estados del AFD
        while (!colaEstados.isEmpty()) {
            Set<String> estadoActual = colaEstados.poll();
            String nombreEstadoActual = convertirConjuntoAString(estadoActual);
            mapaEquivalencia.put(nombreEstadoActual, new HashSet<>(estadoActual));
            
            // Verificar si es estado de aceptación
            if (esEstadoAceptacion(estadoActual, estadosFinalesAFND)) {
                estadosFinalesAFD.add(nombreEstadoActual);
            }
            
            // Para cada símbolo del alfabeto
            for (String simbolo : alfabeto) {
                if (simbolo == null || simbolo.isEmpty()) continue;
                
                // Calcular cerradura epsilon y transiciones
                Set<String> nuevoEstado = calcularTransicion(estadoActual, simbolo, transicionesAFND);
                
                if (!nuevoEstado.isEmpty()) {
                    String nombreNuevoEstado = convertirConjuntoAString(nuevoEstado);
                    
                    // Registrar la transición
                    String[] transicion = {nombreEstadoActual, simbolo, nombreNuevoEstado};
                    transicionesAFD.add(transicion);
                    
                    // Agregar nuevo estado si no ha sido visitado
                    if (!estadosVisitados.contains(nombreNuevoEstado)) {
                        estadosVisitados.add(nombreNuevoEstado);
                        colaEstados.add(nuevoEstado);
                    }
                }
            }
        }
        
        // Construir los arreglos del AFD
        String[] estadosAFD = estadosVisitados.toArray(new String[0]);
        String[] estadosFinalesAFD_arr = estadosFinalesAFD.toArray(new String[0]);
        String[][] transicionesAFD_arr = transicionesAFD.toArray(new String[transicionesAFD.size()][]);
        
        return new Automata(estadosAFD, alfabeto, nombreEstadoInicial, estadosFinalesAFD_arr, transicionesAFD_arr);
    }

    private Set<String> calcularTransicion(Set<String> estadosActuales, String simbolo, String[][] transiciones) {
        Set<String> destinos = new HashSet<>();
        
        // Para cada estado actual en el conjunto
        for (String estadoActual : estadosActuales) {
            // Buscar todas las transiciones desde este estado con el símbolo dado
            for (String[] transicion : transiciones) {
                if (transicion != null && transicion.length >= 3) {
                    if (transicion[0].equals(estadoActual) && transicion[1].equals(simbolo)) {
                        destinos.add(transicion[2]);
                    }
                }
            }
        }
        
        return destinos;
    }

    private boolean esEstadoAceptacion(Set<String> conjunto, String[] estadosFinales) {
        for (String estado : estadosFinales) {
            if (conjunto.contains(estado)) {
                return true;
            }
        }
        return false;
    }

    private String convertirConjuntoAString(Set<String> conjunto) {
        if (conjunto.isEmpty()) {
            return "∅";
        }
        
        List<String> lista = new ArrayList<>(conjunto);
        Collections.sort(lista);
        
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for (int i = 0; i < lista.size(); i++) {
            if (i > 0) sb.append(",");
            sb.append(lista.get(i));
        }
        sb.append("}");
        
        return sb.toString();
    }

    public Map<String, Set<String>> getMapaEquivalencia() {
        return mapaEquivalencia;
    }

}
