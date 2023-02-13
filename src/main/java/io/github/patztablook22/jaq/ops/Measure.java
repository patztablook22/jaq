package io.github.patztablook22.jaq.ops;

import io.github.patztablook22.jaq.Qnode;
import java.util.ArrayList;
import java.util.List;


public class Measure implements Qnode {
    List<Integer> source = new ArrayList<>();
    List<Integer> target = new ArrayList<>();

    public Measure(int q, int c) {
        source.add(q);
        target.add(c);
    }

    public List<Integer> qubits() {
        return source;
    }

    public List<Integer> cbits() {
        return target;
    }

    public int qubit() {
        return source.get(0);
    }

    public int cbit() {
        return target.get(0);
    }
}
