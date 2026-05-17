package automata.model;

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

    public boolean isInicial() {
        return inicial;
    }

    public boolean isAceptacion() {
        return aceptacion;
    }

    @Override
    public String toString() {
        return nombre;
    }
}