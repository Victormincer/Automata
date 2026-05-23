package model;

 /*---------------------------------------------------------------------------
    PROPOSITO: Definir la estructura básica de una transición dentro de un autómata finito, 
               con atributos para el estado de origen, el símbolo de transición y el estado de destino. 
               Incluye un método para representar la transición como cadena.
    FECHA: 2026-05-16
    AUTOR: Victor Alfonso Pardo Gutierrez - Maryury Hernandez Marin
    VERSION: 1.3
    -----------------------------------------------------------------------------*/
public class Transicion {

    public final String estadoOrigen;
    public final String simbolo;       // "ε" para epsilon (solo en AFND)
    public final String estadoDestino;
     /*---------------------------------------------------------------------------
    PROPOSITO: Constructor para crear una transición con el estado de origen, 
               el símbolo de transición y el estado de destino. 
               El símbolo puede ser cualquier cadena, incluyendo "ε" 
               para transiciones epsilon en AFND. 
               Se recomienda usar este constructor para definir transiciones 
               de manera clara y consistente, 
               asegurando que los estados de origen 
               y destino existan en el autómata al que se agregarán las transiciones.
    FECHA: 2026-05-16
    AUTOR: Victor Alfonso Pardo Gutierrez - Maryury Hernandez Marin
    VERSION: 1.3
    -----------------------------------------------------------------------------*/
    public Transicion(String estadoOrigen, String simbolo, String estadoDestino) {
        this.estadoOrigen  = estadoOrigen;
        this.simbolo       = simbolo;
        this.estadoDestino = estadoDestino;
    }
     /*---------------------------------------------------------------------------
    PROPOSITO: Representar la transición como una cadena, devolviendo una descripción legible de la misma. 
              Este método es útil para depuración y visual
    FECHA: 2026-05-16
    AUTOR: Victor Alfonso Pardo Gutierrez - Maryury Hernandez Marin
    VERSION: 1.3
    -----------------------------------------------------------------------------*/
    @Override
    public String toString() {
        return "δ(" + estadoOrigen + ", " + simbolo + ") = " + estadoDestino;
    }
}
