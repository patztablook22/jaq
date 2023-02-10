package io.github.patztablook22.jaq.ops;

import io.github.patztablook22.jaq.ops.Pauli;


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
