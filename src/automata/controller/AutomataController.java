package automata.controller;

import automata.model.Automata;
import automata.service.ConversorAFDService;

public class AutomataController {

    private Automata automataActual;

    private Automata automataConvertido;

    private ConversorAFDService conversor =
            new ConversorAFDService();

    public void setAutomataActual(Automata automata) {
        this.automataActual = automata;
    }

    public Automata getAutomataActual() {
        return automataActual;
    }

    public Automata convertirAFD() {

        automataConvertido =
                conversor.convertir(automataActual);

        return automataConvertido;
    }
}