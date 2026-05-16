/*
 * Proposito: Clase que permite representar una transición en un autómata, es decir, la acción de pasar de un estado a otro con un símbolo de entrada.
 * Esta clase puede ser utilizada para crear autómatas finitos deterministas (AFD) o autómatas finitos no deterministas (AFND) 
 * y para validar si una cadena de símbolos es aceptada por el autómata.
 * 
 * Autor: Victor Alfonso Pardo Guiterrez - Maryury Hernandez Marin
 * Fecha: 2024-5-16
 * versión: 1.0
 */
public class Transicion {
    private Estado origen;
    private Estado destino;
    private String simbolo;

    public Transicion(Estado origen, Estado destino, String simbolo) {
        this.origen = origen;
        this.destino = destino;
        this.simbolo = simbolo;
    }

    public Estado getOrigen() {
        return origen;
    }

    public void setOrigen(Estado origen) {
        this.origen = origen;
    }

    public Estado getDestino() {
        return destino;
    }

    public void setDestino(Estado destino) {
        this.destino = destino;
    }

    public String getSimbolo() {
        return simbolo;
    }

    public void setSimbolo(String simbolo) {
        this.simbolo = simbolo;
    }
    

}
