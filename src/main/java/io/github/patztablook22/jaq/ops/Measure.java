package io.github.patztablook22.jaq.ops;

import io.github.patztablook22.jaq.Qop;


public class Measure implements Qop {
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
