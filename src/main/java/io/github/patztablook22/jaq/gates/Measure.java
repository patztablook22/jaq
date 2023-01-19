package io.github.patztablook22.jaq.gates;

import io.github.patztablook22.jaq.Qgate;


public class Measure implements Qgate {
    int[] ids;

    public Measure(int id) {
        int[] temp = {id};
        ids = temp;
    }

    public Measure(int[] ids) {
        this.ids = ids;
    }

    @Override
    public int arity() {
        return ids.length;
    }

    @Override 
    public int operand(int idx) {
        if (idx < 0 || idx >= arity())
            throw new IndexOutOfBoundsException();

        return ids[idx];
    }

    @Override
    public String label() {
        return "M";
    }
}
