package io.github.patztablook22.jaq.ops;

import io.github.patztablook22.jaq.ops.Pauli;


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
