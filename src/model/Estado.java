package model;

/*---------------------------------------------------------------------------
   PROPOSITO: Definir la estructura básica de un estado dentro de un autómata finito, 
              con atributos para su nombre, si es inicial, de aceptación o de error. 
              Incluye métodos para obtener una etiqueta enriquecida para visualización en tablas y para representar el estado como cadena.
   FECHA: 2026-05-16
   AUTOR: Victor Alfonso Pardo Gutierrez - Maryury Hernandez Marin
   VERSION: 1.3
   -----------------------------------------------------------------------------*/
public class Estado {

    public String nombre;
    public boolean esInicial;
    public boolean esAceptacion;
    /** Estado trampa/muerto: absorbe entradas sin aceptar jamás. */
    public boolean esError;

    // ── Nombre de display en tabla para el estado error ────────────────────────
    public static final String NOMBRE_ERROR = "\u00D8"; // Ø
    public static final String LABEL_ERROR = "Error (" + NOMBRE_ERROR + ")";

    /*---------------------------------------------------------------------------
    PROPOSITO: Constructor para crear un estado con nombre, indicando si es inicial o de aceptación. 
               El estado de error se establece en false por defecto, 
               ya que no todos los estados son trampa. Para marcar un estado como error, 
               se debe usar el constructor con el parámetro esError o establecerlo 
               manualmente después de la creación. 
               Este diseño permite flexibilidad para definir estados que pueden 
               ser tanto de aceptación como de error,
    FECHA: 2026-05-16
    AUTOR: Victor Alfonso Pardo Gutierrez - Maryury Hernandez Marin
    VERSION: 1.3
    -----------------------------------------------------------------------------*/
    public Estado(String nombre, boolean esInicial, boolean esAceptacion) {
        this.nombre = nombre;
        this.esInicial = esInicial;
        this.esAceptacion = esAceptacion;
        this.esError = false;
    }

    /*---------------------------------------------------------------------------
    PROPOSITO: Constructor para crear un estado con nombre, indicando si es inicial, 
              de aceptación o de error. 
              Este constructor permite definir explícitamente el estado de error 
              al momento de la creación, 
              lo que es útil para marcar estados trampa que no aceptan ninguna cadena. 
              Se recomienda usar este constructor para estados que se 
              sabe de antemano que serán trampa, 
              mientras que el constructor sin el parámetro esError 
              puede ser suficiente para estados normales.
    FECHA: 2026-05-16
    AUTOR: Victor Alfonso Pardo Gutierrez - Maryury Hernandez Marin
    VERSION: 1.3
    -----------------------------------------------------------------------------*/
    public Estado(String nombre, boolean esInicial, boolean esAceptacion, boolean esError) {
        this.nombre = nombre;
        this.esInicial = esInicial;
        this.esAceptacion = esAceptacion;
        this.esError = esError;
    }

    /*---------------------------------------------------------------------------
    PROPOSITO: Calcular los estados alcanzables desde un conjunto con el símbolo dado.
    FECHA: 2026-05-16
    AUTOR: Victor Alfonso Pardo Gutierrez - Maryury Hernandez Marin
    VERSION: 1.3
    -----------------------------------------------------------------------------*/
    public String etiquetaTabla() {
        if (esError)
            return "\u2717 " + LABEL_ERROR; // ✗ Error (Ø)
        StringBuilder sb = new StringBuilder();
        if (esInicial)
            sb.append("\u2192 "); // →
        if (esAceptacion)
            sb.append("* ");
        sb.append(nombre);
        return sb.toString();
    }

    /*---------------------------------------------------------------------------
    PROPOSITO: Representar el estado como una cadena, devolviendo su nombre. 
              Este método es útil para depuración y visualización simple del estado, 
              ya que proporciona una representación legible del mismo. 
              Para obtener una etiqueta más enriquecida, se recomienda usar el método etiquetaTabla(), 
              que incluye símbolos para indicar si el estado es inicial o de aceptación.
    FECHA: 2026-05-16
    AUTOR: Victor Alfonso Pardo Gutierrez - Maryury Hernandez Marin
    VERSION: 1.3
    -----------------------------------------------------------------------------*/
    @Override
    public String toString() {
        return nombre;
    }
}
