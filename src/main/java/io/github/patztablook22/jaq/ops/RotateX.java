package io.github.patztablook22.jaq.ops;

import io.github.patztablook22.jaq.Qnode;
import java.util.List;
import java.util.ArrayList;


public class RotateX implements Qnode {
    private double angle;
    private List<Integer> operands = new ArrayList<>();

    public RotateX(int qubit, double angle) {
        this.angle = angle;
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
