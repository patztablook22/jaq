package io.github.patztablook22.jaq.gates;

import io.github.patztablook22.jaq.Qgate;


public class Toffoli implements Qgate {
    private int id1, id2, id3;

    public Toffoli(int id1, int id2, int id3) {
        this.id1 = id1;
        this.id2 = id2;
        this.id3 = id3;
    }

    @Override
    public int arity() {
        return 3;
    }

    @Override 
    public int operand(int idx) {
        if (idx < 0 || idx >= arity())
            throw new IndexOutOfBoundsException();

        if (idx == 0)
            return id1;
        if (idx == 1)
            return id2;
        return id3;
    }

    @Override
    public String label() {
        return "T";
    }
}
