package io.github.patztablook22.jaq;

import io.github.patztablook22.jaq.nodes.*;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * Quantum circuit API. 
 *
 * Enables creation, modification, inspection, and composition of quantum circuits.
 *
 * <p>
 *   A compact way of defining a quantum circuit is using anynomous subclassing and
 *   the initializer block:
 * </p>
 *
 * <pre>
 *
 *    var circuit = new Qcircuit() {{
 *  
 *        /* superposition &#42;/
 *        hadamard(0);
 *       
 *        /* entanglement &#42;/
 *        cnot(0, 1);
 *
 *        /* measurement &#42;/
 *        measure(0, 0);
 *        measure(1, 1);
 *
 *    }};
 *
 * </pre>
 *
 * <p> 
 *   For a more fine control, it can be simply inherited by a named subclass,
 *   which can e.g. automate the construction of the quantum circuit based on
 *   some parameters:
 * </p>
 *
 * <pre>
 *
 *    class MyCircuit extends Qcircuit {
 *        public MyCircuit(int qubits) {
 *
 *            for (int i = 0; i &lt; qubits; i++)
 *                hadamard(i);
 *
 *            /* ... &#42;/
 *
 *        }
 *    }
 * 
 * </pre>
 *
 * <p>
 *   For easy debugging, use the {@link #toString()} method, which
 *   generates textual quantum circuit diagram visualizations:
 * </p>
 *
 * <pre>
 *    System.out.println(circuit);  // circuit from the first example
 * </pre>
 *
 * <p>
 *     Output:
 * </p>
 *
 * <pre>
 *    q0:  ─H─┬─M───
 *    q1:  ───+─║─M─
 *    c0:  ═════╚═║═
 *    c1:  ═══════╚═
 * </pre>
 *
 * */
public class Qcircuit {

    /**
     * Creates a Qcircuit with flexible register sizes.
     *
     * The register sizes will automatically adjust to further operations.
     *
     * For example, using {@code hadamard(5)} will make sure the quantum reigster
     * size is at least 6.
     *
     * */
    protected Qcircuit() {
        this.registersFixed = false;
    }

    /**
     * Creates a Qcircuit with fixed register sizes.
     *
     * Useful mainly for clearly defined behavior when nesting
     * quantum circuits inside each other.
     *
     * @param qubits quantum register size
     * @param cbits classical register size
     *
     * */
    protected Qcircuit(int qubits, int cbits) {
        this.qRegister = qubits;
        this.cRegister = cbits;
        this.registersFixed = true;
    }

    /**
     * Returns a string representation of the quantum circuit's diagram.
     *
     * Example output:
     *
     * <pre>
     *    q0:  ─H───────────────────┌──────────┐─
     *    q1:  ─H─Rx─┬─X────────────┤0  Inner2 ├─
     *    q2:  ─H────┊─┬────────M─X─┤1         ├─
     *    q3:  ─H────+─+────────║─X─└──────────┘─
     *    q4:  ─┌─────────────┐─║────────────────
     *    q5:  ─┤0  InnerA... ├─║────────────────
     *    c0:  ═╡0            ╞═║════════════════
     *    c1:  ═└─────────────┘═╚════════════════
     * </pre>
     *
     * @return text-based quantum circuit diagram
     *
     * */
    @Override
    public String toString() {
        var builder = new QcircuitStringBuilder(this);
        return builder.build();
    }

    /**
     * Returns the size of the quantum register.
     *
     * @return the size of the quantum register
     *
     * */
    public int qubits() {
        return qRegister;
    }

    /**
     * Return the size of the classical register.
     *
     * @return the size of the classical register
     *
     * */
    public int cbits() {
        return cRegister;
    }

    /**
     * Returns the number of {@link Qnode Qnodes} of the Qcircuit
     *
     * @return the number of Qnodes
     * @see Qnode
     *
     * */
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

    /**
     *
     * Adds the {@link io.github.patztablook22.jaq.nodes.Hadamard Hadamard} 
     * gate acting individually on the given qubits.
     *
     * @param qubits individual gate operands
     *
     * */
    protected void hadamard(int... qubits) {
        for (int q: qubits) {
            checkQubitRegisterBounds(q);
            nodesList.add(new Hadamard(q));
        }
    }

    /**
     * Adds the {@link io.github.patztablook22.jaq.nodes.Cnot CNOT} 
     * (Controlled-NOT) controlled by {@code controlQubit} and
     * acting on {@code targetQubit}.
     *
     * @param controlQubit the control qubit
     * @param targetQubit the target qubit
     *
     * */
    protected void cnot(int controlQubit, int targetQubit) {
        checkQubitRegisterBounds(controlQubit);
        checkQubitRegisterBounds(targetQubit);
        nodesList.add(new Cnot(controlQubit, targetQubit));
    }

    /**
     * Adds the {@link io.github.patztablook22.jaq.nodes.RotateX RotateX},
     * rotating {@code qubit} by {@code angle}.
     *
     * @param qubit qubit to rotate
     * @param angle rotation angle (in radians)
     *
     * */
    protected void rotateX(int qubit, double angle) {
        checkQubitRegisterBounds(qubit);
        nodesList.add(new RotateX(qubit, angle));
    }

    /**
     * Adds the {@link io.github.patztablook22.jaq.nodes.PauliX PauliX} 
     * gate acting individually on the given qubits.
     *
     * @param qubits individual gate operands
     *
     * */
    protected void pauliX(int... qubits) {
        for (int qubit: qubits) {
            checkQubitRegisterBounds(qubit);
            nodesList.add(new PauliX(qubit));
        }
    }

    /**
     * Nests another {@link Qcircuit Qcircuit} at the end of the
     * current circuit. 
     *
     * The quantum and classical register of the nested
     * circuit inherit transitively from the outer circuit based on
     * the provided {@code qubits} and {@code cbits} arguments.
     *
     * Note that the size {@code qubits} and {@code cbits} must
     * correspond to the size of the quantum and classical register
     * of the nested circuit.
     *
     * @param other the quantum circuit to nest into the current one
     * @param qubits qubits to inherit by the inner quantum register
     * @param cbits classical bits to inherit by the inner classical register
     *
     * */
    protected void apply(Qcircuit other, int[] qubits, int[] cbits) {
        for (int q: qubits) checkQubitRegisterBounds(q);
        for (int c: cbits) checkCbitRegisterBounds(c);
        nodesList.add(new Subcircuit(other, qubits, cbits));
    }

    /**
     * Adds the {@link io.github.patztablook22.jaq.nodes.Measure Measure}
     * operation.
     *
     * @param sourceQubit qubit to measure
     * @param targetCbit classical bit to store the measurement into
     *
     * */
    protected void measure(int sourceQubit, int targetCbit) {
        checkQubitRegisterBounds(sourceQubit);
        checkCbitRegisterBounds(targetCbit);
        nodesList.add(new Measure(sourceQubit, targetCbit));
    }

    /**
     * Sets the name of the Qcircuit.
     * Useful for debugging and other inspection.
     *
     * @param name name
     *
     * */
    protected void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the list of the Qcircuit's {@link Qnode Qnodes}.
     *
     * @return list of the Qcircuit's Qnodes
     *
     * */
    public List<Qnode> nodes() {
        return nodesList;
    }

    /**
     * Returns the Qcircuit's name.
     *
     * If the name wasn't set explicitly, the class name is used.
     *
     * @see Object#getClass()
     * @see Class#getSimpleName()
     *
     * @return the Qcircuit's name
     *
     * */
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
    private String name;
}
