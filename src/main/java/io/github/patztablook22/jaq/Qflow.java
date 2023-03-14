package io.github.patztablook22.jaq;

import io.github.patztablook22.jaq.nodes.*;
import io.github.patztablook22.jaq.Qop;

import java.util.NoSuchElementException;
import java.util.Stack;


/**
 * Sequential {@link Qop} feed from given {@link Qcircuit}.
 *
 * <p>
 *   {@code Qflow} should be used to iterate over the {@link Qop atomic operations}
 *   of a {@link Qcircuit} contextualized globally. 
 *
 *   In pracitce, this amounts to
 *   receiving the totally inlined version of a {@link Qcircuit}, with all other
 *   {@code Qcircuits} nested in it through 
 *   {@link io.github.patztablook22.jaq.nodes.Subcircuit} begin recursively
 *   inlined into the outer-most {@code Qcircuit}.
 *
 *   The operations are fed in chronological/topological order.
 *   This is helpful for various {@code Qcircuit} executors / transpilers,
 *   since the {@code Qflow} strips off all the higher-level API complexities 
 *   of the {@code Qcircuit} and gives back the most basic representation.
 * </p>
 *
 * */
public abstract class Qflow {
    private Qcircuit circuit;
    private Stack<int[]> qubitScopes = new Stack<>();
    private Stack<int[]> cbitScopes = new Stack<>();

    /**
     * Constructs a {@code Qflow} from given {@link Qcircuit}.
     * Note that the underlying {@code Qcircuit} is never
     * modified by the {@code Qflow}.
     *
     * @param circuit the underlying Qcircuit
     *
     * */
    protected Qflow(Qcircuit circuit) {
        this.circuit = circuit;
    }

    /**
     * Returns the underlying {@link Qcircuit}.
     *
     * @return the underlying Qcircuit
     *
     * */
    public final Qcircuit getCircuit() {
        return circuit;
    }

    /**
     * Runs the operation feed.
     *
     * */
    public void flow() {
        runRecursive(circuit);
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
                feed((Qop) node);
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

    /**
     * Recives the next {@link Qop} and feeds it forward into
     * the dedicated method.
     *
     * @param op the operation being fed
     *
     * */
    protected void feed(Qop op) {
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

    /**
     * {@link io.github.patztablook22.jaq.nodes.Hadamard} gate.
     *
     * @param qubit the gate's qubit
     *
     * */
    protected abstract void hadamard(int qubit);

    /**
     * {@link io.github.patztablook22.jaq.nodes.Measure} operations.
     *
     * @param source the source qubit
     * @param target the target classical bit
     *
     * */
    protected abstract void measure(int source, int target);

    /**
     * {@link io.github.patztablook22.jaq.nodes.Cnot} gate.
     *
     * @param control the control qubit
     * @param target the target qubit
     *
     * */
    protected abstract void cnot(int control, int target);

    /**
     * {@link io.github.patztablook22.jaq.nodes.PauliX} gate.
     *
     * @param qubit the gate's qubit
     *
     * */
    protected abstract void pauliX(int qubit);

    /**
     * {@link io.github.patztablook22.jaq.nodes.RotateX} gate.
     *
     * @param qubit the gate's qubit
     * @param angle the rotation angle
     *
     * */
    protected abstract void rotateX(int qubit, double angle);
}
