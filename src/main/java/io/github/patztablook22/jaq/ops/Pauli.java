package io.github.patztablook22.jaq.ops;

import io.github.patztablook22.jaq.Qop;


abstract class Pauli implements Qop {
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
