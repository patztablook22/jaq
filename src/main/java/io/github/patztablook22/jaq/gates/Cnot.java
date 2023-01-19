package io.github.patztablook22.jaq.gates;

import io.github.patztablook22.jaq.Qgate;


public class Cnot implements Qgate {
    private int id1 = -1;
    private int id2 = -1;

    public Cnot(int id1) {
        this.id1 = id1;
    }

    public Cnot(int id1, int id2) {
        this.id1 = id1;
        this.id2 = id2;
    }

    @Override
    public int arity() {
        return id2 == -1 ? 1 : 2;
    }

    @Override 
    public int operand(int idx) {
        if (idx < 0 || idx >= arity())
            throw new IndexOutOfBoundsException();

        return idx == 0 ? id1 : id2;
    }

    @Override
    public String label() {
        return "(+)";
    }
}
