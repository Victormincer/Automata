package Automata;

/*
 * Proposito: Clase que permite representar un autómata finito determinista (AFD) o un autómata finito no determinista (AFND).
 *            Esta clase puede ser utilizada para crear autómatas y para validar si una cadena de símbolos es aceptada por el autómata.
 * Autor: Victor Alfonso Pardo Guiterrez - Maryury Hernandez Marin
 * Fecha: 2024-5-16
 * versión: 1.0
 */
public class Automata {
    private String[] estados; // Conjunto de estados del autómata
    private String[] alfabeto; // Conjunto de símbolos del alfabeto
    private String estadoInicial; // Estado inicial del autómata
    private String[] estadosFinales; // Conjunto de estados finales del autómata
    private String[][] transiciones; // Matriz de transiciones del autómata

    public Automata(String[] estados, String[] alfabeto, String estadoInicial, String[] estadosFinales, String[][] transiciones) {
        this.estados = estados;
        this.alfabeto = alfabeto;
        this.estadoInicial = estadoInicial;
        this.estadosFinales = estadosFinales;
        this.transiciones = transiciones;
    }

    public String[] getEstados() {
        return estados;
    }

    public void setEstados(String[] estados) {
        this.estados = estados;
    }

    public String[] getAlfabeto() {
        return alfabeto;
    }

    public void setAlfabeto(String[] alfabeto) {
        this.alfabeto = alfabeto;
    }

    public String getEstadoInicial() {
        return estadoInicial;
    }

    public void setEstadoInicial(String estadoInicial) {
        this.estadoInicial = estadoInicial;
    }

    public String[] getEstadosFinales() {
        return estadosFinales;
    }

    public void setEstadosFinales(String[] estadosFinales) {
        this.estadosFinales = estadosFinales;
    }

    public String[][] getTransiciones() {
        return transiciones;
    }

    public void setTransiciones(String[][] transiciones) {
        this.transiciones = transiciones;
    }
}
