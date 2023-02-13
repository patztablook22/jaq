package io.github.patztablook22.jaq.ops;

import io.github.patztablook22.jaq.Qnode;
import io.github.patztablook22.jaq.Qcircuit;

import java.util.List;
import java.util.ArrayList;

public class Subcircuit implements Qnode {
    Qcircuit circuit;
    List<Integer> qRegister = new ArrayList<>();
    List<Integer> cRegister = new ArrayList<>();

    public Subcircuit(Qcircuit circuit, int... bits) {
        this.circuit = circuit;
    }

    public Subcircuit(Qcircuit circuit, int[] qubits, int[] cbits) {
        this.circuit = circuit;
        for (int q: qubits) qRegister.add(q);
        for (int c: cbits) cRegister.add(c);
    }

    public Qcircuit getCircuit() {
        return circuit;
    }

    @Override
    public List<Qnode> nodes() {
        return circuit.nodes();
    }

    @Override
    public List<Integer> qubits() {
        return qRegister;
    }

    @Override
    public List<Integer> cbits() {
        return cRegister;
    }
}
