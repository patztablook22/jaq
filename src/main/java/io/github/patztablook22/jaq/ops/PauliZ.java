package io.github.patztablook22.jaq.ops;

import io.github.patztablook22.jaq.ops.Pauli;


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
