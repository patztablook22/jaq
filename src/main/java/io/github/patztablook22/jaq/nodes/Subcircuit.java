package io.github.patztablook22.jaq.nodes;

import io.github.patztablook22.jaq.Qnode;
import io.github.patztablook22.jaq.Qcircuit;

import java.util.Arrays;


/**
 * Nests given {@link io.github.patztablook22.jaq.Qcircuit} inside another.
 * The nested circuit inherits specified outer circuit's 
 * qubits and classical qubits.
 *
 * <p>
 *   Note that subcircuits are merely a convenience construct for design
 *   and debugging and have no implications (such as a persistent unique
 *   state) for the quantum runtime.
 *   During the runtime, subcircuits are simply recursively inlined 
 *   into the outer-most global context. This is done for example by
 *   {@link io.github.patztablook22.jaq.Qflow}.
 * </p>
 *
 * */
public class Subcircuit implements Qnode {

    /**
     *
     * The inner circuit.
     *
     * */
    private Qcircuit circuit;

    /**
     * Qubits to inherit from the outer circuit.
     *
     * */
    private int[] qubits;

    /**
     * Classical bits to inherit from the outer circuit.
     *
     * */
    private int[] cbits;

    /**
     * Checks the number of inherited outer qubits and classical bits 
     * corresponds to the inner circuit's register sizes.
     *
     * @return whether the inherited bits correspond to the inner circuit sizes
     *
     * */
    private boolean argsValid() {
        if (circuit.qubits() != qubits.length)
            return false;
        if (circuit.cbits() != cbits.length)
            return false;
        if (Arrays.stream(qubits).distinct().count() != qubits.length)
            return false;
        if (Arrays.stream(cbits).distinct().count() != cbits.length)
            return false;
        return true;
    }

    /**
     * Constructs a subcircuit {@link io.github.patztablook22.jaq.Qnode}
     * around a specified {@link io.github.patztablook22.jaq.Qcircuit}.
     *
     * The inner circuit's registers are connected to the outer 
     * circuit ones based on the {@code qubits} and {@code cbits} arguments.
     * Their sizes must match the inner circuit's register sizes,
     * and within each argument, all values must be unique.
     *
     * @param circuit the inner circuit
     * @param qubits qubits to inherit from the outer circuit
     * @param cbits classical bits to inherit from the outer circuit
     *
     * */
    public Subcircuit(Qcircuit circuit, int[] qubits, int[] cbits) {
        this.circuit = circuit;
        this.qubits = qubits.clone();
        this.cbits = cbits.clone();
        if (!argsValid())
            throw new IllegalArgumentException();
    }

    /**
     * Returns the inner {@link io.github.patztablook22.jaq.Qcircuit}.
     *
     * @return the inner circuit.
     *
     * */
    public Qcircuit getCircuit() {
        return circuit;
    }

    /**
     * Returns the qubits inherited from the outer
     * {@link io.github.patztablook22.jaq.Qcircuit}.
     *
     * @return the qubits inherited from the outer circuit
     *
     * */
    public int[] getQubits() {
        return qubits;
    }

    /**
     * Returns the classical inherited from the outer
     * {@link io.github.patztablook22.jaq.Qcircuit}.
     *
     * @return the classical bits inherited from the outer circuit
     *
     * */
    public int[] getCbits() {
        return cbits;
    }
}
