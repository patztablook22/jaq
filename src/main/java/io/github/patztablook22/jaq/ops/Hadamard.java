package io.github.patztablook22.jaq.ops;

import io.github.patztablook22.jaq.Qop;


public class Hadamard implements Qop {
    private int id;

    public Hadamard(int id) {
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

    @Override
    public String label() {
        return "H";
    }
}
