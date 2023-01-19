package io.github.patztablook22.jaq.gates;

import io.github.patztablook22.jaq.gates.Pauli;


public class PauliZ extends Pauli {
    private int id;

    public PauliZ(int id) {
        super(id);
    }

    @Override
    public String label() {
        return "Z";
    }
}
