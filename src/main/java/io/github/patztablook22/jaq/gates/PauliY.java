package io.github.patztablook22.jaq.gates;

import io.github.patztablook22.jaq.gates.Pauli;


public class PauliY extends Pauli {
    private int id;

    public PauliY(int id) {
        super(id);
    }

    @Override
    public String label() {
        return "Y";
    }
}
