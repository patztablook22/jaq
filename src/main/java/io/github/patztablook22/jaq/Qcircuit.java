package io.github.patztablook22.jaq;

import io.github.patztablook22.jaq.ops.*;

import java.util.List;
import java.util.ArrayList;


public class Qcircuit {
    protected Qcircuit(int qubits, int cbits) {
        this.qRegister = qubits;
        this.cRegister = cbits;
        this.registersFixed = true;
    }

    protected Qcircuit() {
        this.registersFixed = false;
    }

    @Override
    public String toString() {
        var builder = new QcircuitStringBuilder(this);
        return builder.build();
    }

    public int qubits() {
        return qRegister;
    }

    public int cbits() {
        return cRegister;
    }

    public int length() {
        return nodesList.size();
    }

    private void checkQubitRegisterBounds(int q) {
        if (q < 0)
            throw new IndexOutOfBoundsException();

        if (registersFixed && q >= qRegister)
            throw new IndexOutOfBoundsException();

        if (!registersFixed)
            qRegister = Math.max(qRegister, q + 1);
    }

    private void checkCbitRegisterBounds(int c) {
        if (c < 0)
            throw new IndexOutOfBoundsException();

        if (registersFixed && c >= cRegister)
            throw new IndexOutOfBoundsException();

        if (!registersFixed)
            cRegister = Math.max(cRegister, c + 1);
    }

    protected void hadamard(int... qubits) {
        for (int q: qubits) {
            checkQubitRegisterBounds(q);
            nodesList.add(new Hadamard(q));
        }
    }

    protected void cnot(int controlQubit, int targetQubit) {
        checkQubitRegisterBounds(controlQubit);
        checkQubitRegisterBounds(targetQubit);
        nodesList.add(new Cnot(controlQubit, targetQubit));
    }

    protected void rotateX(int qubit, double angle) {
        checkQubitRegisterBounds(qubit);
        nodesList.add(new RotateX(qubit, angle));
    }

    protected void pauliX(int... qubits) {
        for (int qubit: qubits) {
            checkQubitRegisterBounds(qubit);
            nodesList.add(new PauliX(qubit));
        }
    }

    protected void apply(Qcircuit other, int[] qubits, int[] cbits) {
        for (int q: qubits) checkQubitRegisterBounds(q);
        for (int c: cbits) checkCbitRegisterBounds(c);
        nodesList.add(new Subcircuit(other, qubits, cbits));
    }

    protected void measure(int sourceQubit, int targetCbit) {
        checkQubitRegisterBounds(sourceQubit);
        checkCbitRegisterBounds(targetCbit);
        nodesList.add(new Measure(sourceQubit, targetCbit));
    }

    protected void setName(String name) {
        this.name = name;
    }

    public List<Qnode> nodes() {
        return nodesList;
    }

    public String getName() {
        if (name == null)
            return getClass().getSimpleName();
        else
            return name;
    }

    private int qRegister = 0;
    private int cRegister = 0;
    private boolean registersFixed = false;
    private List<Qnode> nodesList = new ArrayList<>();
    String name;
}
