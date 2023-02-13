package io.github.patztablook22.jaq.ops;

import io.github.patztablook22.jaq.Qnode;
import java.util.List;
import java.util.ArrayList;


public class PauliX implements Qnode {
    private List<Integer> operands = new ArrayList<>();

    public PauliX(int qubit) {
        operands.add(qubit);
    }

    @Override
    public List<Integer> qubits() {
        return operands;
    }

    public int qubit() {
        return operands.get(0);
    }
}
