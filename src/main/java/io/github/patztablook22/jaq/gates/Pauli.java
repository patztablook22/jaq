package io.github.patztablook22.jaq.gates;

import io.github.patztablook22.jaq.Qgate;


abstract class Pauli implements Qgate {
    private int id;

    public Pauli(int id) {
        this.id = id;
    }

    @Override
    public int arity() {
        return 1;
    }

    @Override 
    public int operand(int idx) {
        if (idx != 0)
            throw new IndexOutOfBoundsException();

        return id;
    }
}
