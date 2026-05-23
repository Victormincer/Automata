package model;

import java.util.*;

/*---------------------------------------------------------------------------
   PROPOSITO: Definir la estructura básica de un autómata finito, con estados, alfabeto y transiciones. 
              Incluye métodos para obtener el estado inicial y marcar estados de error.
   FECHA: 2026-05-16
   AUTOR: Victor Alfonso Pardo Gutierrez - Maryury Hernandez Marin
   VERSION: 1.3
   ACTUALIZACIÓN: 2026-05-23 - Se agregó el método marcarEstadosError para identificar y marcar estados trampa en un AFD, 
                  mejorando la robustez del modelo frente a entradas no reconocidas.
   -----------------------------------------------------------------------------*/
public class Automata {

    public String nombre;
    public List<Estado> estados = new ArrayList<>();
    public Set<String> alfabeto = new LinkedHashSet<>();
    public List<Transicion> transiciones = new ArrayList<>();
    /** true = AFND, false = AFD */
    public boolean esAFND = false;

    /*---------------------------------------------------------------------------
    PROPOSITO: Constructor para crear un autómata con un nombre dado. El autómata se inicializa sin estados, alfabeto ni transiciones, 
               y se asume que es un AFD por defecto (esAFND = false). 
               Se recomienda usar este constructor para crear un AFD vacío, 
               y luego agregar estados, alfabeto y transiciones según sea necesario. 
               Para crear un AFND, se debe establecer esAFND = true después de la creación.
    FECHA: 2026-05-16
    AUTOR: Victor Alfonso Pardo Gutierrez - Maryury Hernandez Marin
    VERSION: 1.3
    -----------------------------------------------------------------------------*/
    public Automata(String nombre) {
        this.nombre = nombre;
    }

    public Estado getEstado(String nombre) {
        return estados.stream()
                .filter(e -> e.nombre.equals(nombre))
                .findFirst().orElse(null);
    }

    /*---------------------------------------------------------------------------
    PROPOSITO: Obtener el nombre del estado inicial del autómata. Se asume que solo hay un estado marcado como inicial. 
              Si no se encuentra ningún estado inicial, se devuelve null. 
              Este método es útil para iniciar procesos de simulación o conversión de autómatas, 
              ya que proporciona un punto de partida claro para la ejecución.
    FECHA: 2026-05-16
    AUTOR: Victor Alfonso Pardo Gutierrez - Maryury Hernandez Marin
    VERSION: 1.3
    -----------------------------------------------------------------------------*/
    public String getEstadoInicial() {
        return estados.stream()
                .filter(e -> e.esInicial)
                .map(e -> e.nombre)
                .findFirst().orElse(null);
    }

    /*---------------------------------------------------------------------------
    PROPOSITO: Identificar y marcar los estados de error en el autómata. Un estado se considera de error si no es de aceptación ni inicial, 
              y cumple alguna de las siguientes condiciones: 
              1) Su nombre coincide con "ERROR" (ignorando mayúsculas) o con el nombre definido en Estado.NOMBRE_ERROR. 
              2) Es un estado trampa, es decir, tiene transiciones de self-loop para todos los símbolos del alfabeto (excepto ε) y no tiene transiciones hacia otros estados. 
              Este método es especialmente útil para AFDs, ya que permite identificar estados. que no conduc
    FECHA: 2026-05-16
    AUTOR: Victor Alfonso Pardo Gutierrez - Maryury Hernandez Marin
    VERSION: 1.3
    ACTUALIZACIÓN: 2026-05-23 - Se agregó el método marcarEstadosError para identificar y marcar estados trampa en un AFD, 
                   mejorando la robustez del modelo frente a entradas no reconocidas.
    -----------------------------------------------------------------------------*/
    public void marcarEstadosError() {
        Set<String> alfa = new LinkedHashSet<>(alfabeto);
        alfa.remove("ε");

        for (Estado est : estados) {
            if (est.esAceptacion || est.esInicial)
                continue;

            // Marcado directo por nombre
            if (est.nombre.equals(Estado.NOMBRE_ERROR)
                    || est.nombre.equalsIgnoreCase("ERROR")) {
                est.esError = true;
                continue;
            }

            // Estado trampa: solo tiene self-loops para todos los símbolos
            if (alfa.isEmpty())
                continue;
            boolean soloSelfLoop = true;
            for (String sym : alfa) {
                final String e = est.nombre;
                boolean tieneSelfLoop = transiciones.stream().anyMatch(
                        t -> t.estadoOrigen.equals(e) && t.simbolo.equals(sym)
                                && t.estadoDestino.equals(e));
                boolean tieneOtra = transiciones.stream().anyMatch(
                        t -> t.estadoOrigen.equals(e) && t.simbolo.equals(sym)
                                && !t.estadoDestino.equals(e));
                if (!tieneSelfLoop || tieneOtra) {
                    soloSelfLoop = false;
                    break;
                }
            }
            if (soloSelfLoop)
                est.esError = true;
        }
    }
}
