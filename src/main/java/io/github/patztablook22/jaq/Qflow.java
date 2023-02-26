package io.github.patztablook22.jaq;

import io.github.patztablook22.jaq.nodes.*;
import io.github.patztablook22.jaq.Qop;

import java.util.NoSuchElementException;
import java.util.Stack;


/**
 * Sequential {@link Qop} iterator over a {@link Qcircuit}.
 *
 *
 *
 * */
public abstract class Qflow {
    private Qcircuit circuit;
    private Stack<int[]> qubitScopes = new Stack<>();
    private Stack<int[]> cbitScopes = new Stack<>();

    protected Qflow(Qcircuit circuit) {
        this.circuit = circuit;
    }

    public Qcircuit getCircuit() {
        return circuit;
    }

    public byte[] run() {
        before();
        runRecursive(circuit);
        after();
        return returnData();
    }

    public byte[][] run(int shots) {
        byte[][] data = new byte[shots][];
        for (int i = 0; i < shots; i++)
            data[i] = run();
        return data;
    }

    private int[] select(int[] from, int[] indices) {
        int[] result = new int[indices.length];
        for (int i = 0; i < result.length; i++)
            result[i] = from[indices[i]];
        return result;
    }

    private void runRecursive(Qcircuit circuit) {
        for (Qnode node: circuit.nodes()) {
            if (node instanceof Subcircuit) {
                var sc = (Subcircuit) node;
                var c = sc.getCircuit();

                int[] newQubitScope, newCbitScope;
                if (qubitScopes.empty()) {
                    newQubitScope = sc.getQubits();
                    newCbitScope = sc.getCbits();
                } else {
                    newQubitScope = select(qubitScopes.peek(), sc.getQubits());
                    newCbitScope = select(cbitScopes.peek(), sc.getCbits());
                }

                qubitScopes.push(newQubitScope);
                cbitScopes.push(newCbitScope);

                runRecursive(c);

                qubitScopes.pop();
                cbitScopes.pop();

            } else if (node instanceof Qop) {
                runOp((Qop) node);
            }
        }
    }

    private int scopedQubit(int q) {
        if (qubitScopes.empty())
            return q;
        else
            return qubitScopes.peek()[q];
    }

    private int scopedCbit(int c) {
        if (cbitScopes.empty())
            return c;
        else
            return cbitScopes.peek()[c];
    }

    protected void runOp(Qop op) {
        if (op instanceof Hadamard) {
            var h = (Hadamard) op;
            int qubit = scopedQubit(h.getQubit());
            hadamard(qubit);
        } else if (op instanceof Measure) {
            var m = (Measure) op;
            int source = scopedQubit(m.getSource());
            int target = scopedCbit(m.getTarget());
            measure(source, target);
        } else if (op instanceof Cnot) {
            var c = (Cnot) op;
            int control = scopedQubit(c.getControl());
            int target = scopedQubit(c.getTarget());
            cnot(control, target);
        } else if (op instanceof PauliX) {
            var px = (PauliX) op;
            int qubit = scopedQubit(px.getQubit());
            pauliX(qubit);
        } else if (op instanceof RotateX) {
            var rx = (RotateX) op;
            int qubit = scopedQubit(rx.getQubit());
            rotateX(qubit, rx.getAngle());
        } else {
            throw new NoSuchElementException();
        }
    }

    protected abstract byte[] returnData();

    protected void before() {
    }

    protected void after() {
    }

    protected abstract void hadamard(int qubit);
    protected abstract void measure(int source, int target);
    protected abstract void cnot(int control, int target);
    protected abstract void pauliX(int qubit);
    protected abstract void rotateX(int qubit, double angle);
}
