package UI;

/*
 * Proposito: Clase que permite gestionar y visualizar la tabla de transiciones de un autómata.
 * Esta clase proporciona funcionalidades para crear, actualizar y obtener modelos de tabla
 * basados en los datos del autómata.
 * 
 * Autor: Victor Alfonso Pardo Guiterrez - Maryury Hernandez Marin
 * Fecha: 2024-5-16
 * versión: 1.0
 */
import javax.swing.table.DefaultTableModel;
import Automata.Automata;

public class TablaTransiciones {
    private Automata automata;
    private DefaultTableModel modeloTabla;

    public TablaTransiciones(Automata automata) {
        this.automata = automata;
        this.modeloTabla = crearModeloTabla();
    }

    /**
     * Crea un modelo de tabla DefaultTableModel basado en las transiciones del autómata.
     * Las filas representan estados y las columnas representan símbolos del alfabeto.
     * 
     * @return DefaultTableModel con la tabla de transiciones
     */
    private DefaultTableModel crearModeloTabla() {
        if (automata == null) {
            return new DefaultTableModel();
        }

        String[] estados = automata.getEstados();
        String[] alfabeto = automata.getAlfabeto();
        String[][] transiciones = automata.getTransiciones();

        String[] columnas = new String[alfabeto.length + 1];
        columnas[0] = "Estado";
        for (int i = 0; i < alfabeto.length; i++) {
            columnas[i + 1] = alfabeto[i];
        }

        DefaultTableModel modelo = new DefaultTableModel(columnas, 0);

        for (String estado : estados) {
            Object[] fila = new Object[alfabeto.length + 1];
            fila[0] = estado;

            for (int j = 0; j < alfabeto.length; j++) {
                String simbolo = alfabeto[j];
                String destino = obtenerDestino(estado, simbolo, transiciones);
                fila[j + 1] = destino != null ? destino : "-";
            }

            modelo.addRow(fila);
        }

        return modelo;
    }

    /**
     * Obtiene el estado destino de una transición dado un estado origen y un símbolo.
     * 
     * @param origen Estado origen
     * @param simbolo Símbolo de entrada
     * @param transiciones Matriz de transiciones del autómata
     * @return Estado destino o null si no existe transición
     */
    private String obtenerDestino(String origen, String simbolo, String[][] transiciones) {
        for (String[] transicion : transiciones) {
            if (transicion != null && transicion.length >= 3) {
                if (transicion[0].equals(origen) && transicion[1].equals(simbolo)) {
                    return transicion[2];
                }
            }
        }
        return null;
    }

    /**
     * Actualiza el modelo de tabla con los datos del autómata actual.
     */
    public void actualizarTabla() {
        this.modeloTabla = crearModeloTabla();
    }

    /**
     * Actualiza el autómata y recrea el modelo de tabla.
     * 
     * @param nuevoAutomata Nuevo autómata para visualizar
     */
    public void setAutomata(Automata nuevoAutomata) {
        this.automata = nuevoAutomata;
        actualizarTabla();
    }

    /**
     * Obtiene el autómata actual.
     * 
     * @return Automata actual
     */
    public Automata getAutomata() {
        return automata;
    }

    /**
     * Obtiene el modelo de tabla para ser utilizado en un JTable.
     * 
     * @return DefaultTableModel con la tabla de transiciones
     */
    public DefaultTableModel getModeloTabla() {
        return modeloTabla;
    }

    /**
     * Obtiene información del autómata como string (estados iniciales, finales, etc).
     * 
     * @return String con información del autómata
     */
    public String obtenerInformacionAutomata() {
        if (automata == null) {
            return "No hay autómata cargado";
        }

        StringBuilder info = new StringBuilder();
        info.append("Estado Inicial: ").append(automata.getEstadoInicial()).append("\n");
        
        info.append("Estados Finales: ");
        String[] estadosFinales = automata.getEstadosFinales();
        for (int i = 0; i < estadosFinales.length; i++) {
            info.append(estadosFinales[i]);
            if (i < estadosFinales.length - 1) {
                info.append(", ");
            }
        }
        info.append("\n");

        info.append("Alfabeto: ");
        String[] alfabeto = automata.getAlfabeto();
        for (int i = 0; i < alfabeto.length; i++) {
            info.append(alfabeto[i]);
            if (i < alfabeto.length - 1) {
                info.append(", ");
            }
        }

        return info.toString();
    }
}
