package io.github.patztablook22.jaq.ops;

import io.github.patztablook22.jaq.Qnode;
import java.util.ArrayList;
import java.util.List;


public class Hadamard implements Qnode {
    List<Integer> operands = new ArrayList<>();

    public Hadamard(int q) {
        operands.add(q);
    }

    public List<Integer> qubits() {
        return operands;
    }

    public int qubit() {
        return operands.get(0);
    }
}
