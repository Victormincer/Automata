package automata.model;

import java.util.*;

public class Automata {

    private String nombre;

    private List<Estado> estados = new ArrayList<>();

    private List<Transicion> transiciones = new ArrayList<>();

    private Set<String> alfabeto = new LinkedHashSet<>();

    private boolean afnd;

    public Automata(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public List<Estado> getEstados() {
        return estados;
    }

    public List<Transicion> getTransiciones() {
        return transiciones;
    }

    public Set<String> getAlfabeto() {
        return alfabeto;
    }

    public boolean isAFND() {
        return afnd;
    }

    public void setAFND(boolean afnd) {
        this.afnd = afnd;
    }

    public Estado getEstado(String nombre) {
        return estados.stream()
                .filter(e -> e.getNombre().equals(nombre))
                .findFirst()
                .orElse(null);
    }

    public String getEstadoInicial() {
        return estados.stream()
                .filter(Estado::isInicial)
                .map(Estado::getNombre)
                .findFirst()
                .orElse(null);
    }
}