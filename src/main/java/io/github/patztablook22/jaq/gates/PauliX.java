package io.github.patztablook22.jaq.gates;

import io.github.patztablook22.jaq.gates.Pauli;


public class PauliX extends Pauli {
    private int id;

    public PauliX(int id) {
        super(id);
    }

    @Override
    public String label() {
        return "X";
    }
}
