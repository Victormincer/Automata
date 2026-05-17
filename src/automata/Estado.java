/*
* Proposito: Clase que permite validar el estado de un autómata, es decir, si es un estado de aceptación o no.
*            Un estado de aceptación es aquel que se encuentra al final de una cadena de símbolos 
*            y que permite que el autómata acepte la cadena. Un estado no de aceptación 
*            es aquel que no se encuentra al final de una cadena de símbolos 
*            y que no permite que el autómata acepte la cadena.
*            Esta clase puede ser utilizada para crear autómatas finitos deterministas (AFD) o autómatas finitos no deterministas (AFND) 
*            y para validar si una cadena de símbolos es aceptada por el autómata.
 * Autor: Victor Alfonso Pardo Guiterrez - Maryury Hernandez Marin
 * Fecha: 2024-5-16
 * versión: 1.0
 */
public class Estado {
    private String nombre;
    private boolean inicial;
    private boolean aceptacion;

    public Estado(String nombre, boolean inicial, boolean aceptacion) {
        this.nombre = nombre;
        this.inicial = inicial;
        this.aceptacion = aceptacion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean isInicial() {
        return inicial;
    }

    public void setInicial(boolean inicial) {
        this.inicial = inicial;
    }

    public boolean isAceptacion() {
        return aceptacion;
    }

    public void setAceptacion(boolean aceptacion) {
        this.aceptacion = aceptacion;
    }
    
}
