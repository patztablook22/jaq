package io.github.patztablook22.jaq.ops;

import io.github.patztablook22.jaq.Qnode;
import io.github.patztablook22.jaq.Qcircuit;

import java.util.List;
import java.util.ArrayList;

public class Cnot implements Qnode {
    List<Integer> operands = new ArrayList<>();

    public Cnot(int controlQubit, int targetQubit) {
        operands.add(controlQubit);
        operands.add(targetQubit);
    }

    @Override
    public List<Integer> qubits() {
        return operands;
    }

    public int controlQubit() {
        return operands.get(0);
    }
 
    public int targetQubit() {
        return operands.get(1);
    } 
}
