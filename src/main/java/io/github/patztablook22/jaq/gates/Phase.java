package io.github.patztablook22.jaq.gates;

import io.github.patztablook22.jaq.Qgate;


public class Phase implements Qgate {
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
