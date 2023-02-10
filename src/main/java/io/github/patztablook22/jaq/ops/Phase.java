package io.github.patztablook22.jaq.ops;

import io.github.patztablook22.jaq.Qop;


public class Phase implements Qop {
    private int id;
    double phi;

    public Phase(int id, double phi) {
        this.id = id;
        this.phi = phi;
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
        return "P";
    }
}
