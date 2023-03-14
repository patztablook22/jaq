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
 * <pre><code class="language-java">
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
 * </code></pre>
 *
 * <p> 
 *   For a more fine control, {@code Qcircuit} can be simply inherited by a named subclass,
 *   which can e.g. automate the construction of the quantum circuit based on
 *   some parameters:
 * </p>
 *
 * <pre><code class="language-java">
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
 * </code></pre>
 *
 * <p>
 *   And later in the code:
 * </p>
 *
 * <pre><code class="language-java">
 *    var circuit = new MyCircuit(16);
 *    Qvm backend = /* ... &#42;/;
 *    backend.run(circuit); // etc.
 * </code></pre>
 *
 * <p>
 *   For easy debugging, use the {@link #toString()} method, which
 *   generates textual quantum circuit diagram visualizations:
 * </p>
 *
 * <pre><code class="java">
 *    System.out.println(circuit);  // circuit from the first example
 * </code></pre>
 *
 * <p>
 *     Output for the first example:
 * </p>
 *
 * <pre><code class="plaintext">
 *    q0:  ─H─┬─M───
 *    q1:  ───+─║─M─
 *    c0:  ═════╚═║═
 *    c1:  ═══════╚═
 * </code></pre>
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
     * <p>
     *   Example output:
     * </p>
     *
     * <pre><code class="plaintext">
     *    q0:  ─H───────────────────┌──────────┐─
     *    q1:  ─H─Rx─┬─X────────────┤0  Inner2 ├─
     *    q2:  ─H────┊─┬────────M─X─┤1         ├─
     *    q3:  ─H────+─+────────║─X─└──────────┘─
     *    q4:  ─┌─────────────┐─║────────────────
     *    q5:  ─┤0  InnerA... ├─║────────────────
     *    c0:  ═╡0            ╞═║════════════════
     *    c1:  ═└─────────────┘═╚════════════════
     * </code></pre>
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

    /**
     * Lets the {@code Qcircuit} know a given qubit is being accessed.
     * Throws IndexOutOfBoundsException if accessing negative indices
     * or when accessing indices beyond the size of the quantum register
     * if its size is fixed. Otherwise it potentially adjusts the
     * register size.
     *
     * @param q qubit to check
     *
     * @see Qcircuit#registersFixed
     *
     * */
    private void checkQubitRegisterBounds(int q) {
        if (q < 0)
            throw new IndexOutOfBoundsException();

        if (registersFixed && q >= qRegister)
            throw new IndexOutOfBoundsException();

        if (!registersFixed)
            qRegister = Math.max(qRegister, q + 1);
    }

    /**
     * Lets the {@code Qcircuit} know a given classical bit is being accessed.
     * Throws IndexOutOfBoundsException if accessing negative indices
     * or when accessing indices beyond the size of the classical register
     * if its size is fixed. Otherwise it potentially adjusts the
     * register size.
     *
     * @param c classical bit to check
     *
     * @see Qcircuit#registersFixed
     *
     * */
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
     * <p>
     *   Example usage:
     * </p>
     *
     * <pre><code class="language-java">
     *    var circuit = new Qcircuit() {{
     *        /* ... &#42;/
     *
     *        hadamard(2, 4);
     *
     *        /* equivalent to &#42;/
     *        hadamard(2);
     *        hadamard(4);
     *
     *        /* ... &#42;/
     *    }};
     * </code></pre>
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
     * <p>
     *   Example usage:
     * </p>
     *
     * <pre><code class="language-java">
     *    var circuit = new Qcircuit() {{
     *        /* ... &#42;/
     *
     *        /*
     *         * based on the third qubit,
     *         * negate the first one
     *         &#42;/
     *        cnot(2, 0);
     *
     *        /* ... &#42;/
     *    }};
     * </code></pre>
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
     * <p>
     *   Example usage:
     * </p>
     *
     * <pre><code class="language-java">
     *    var circuit = new Qcircuit() {{
     *        /* ... &#42;/
     *
     *        pauliX(3, 1);
     *
     *        /* equivalent to &#42;/
     *        pauliX(3);
     *        pauliX(1);
     *
     *        /* ... &#42;/
     *    }};
     * </code></pre>
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
     * <p>
     *   Example usage:
     * </p>
     *
     * <pre><code class="language-java">
     *    class UniformSuperposition extends Qcircuit {
     *        public UniformSuperposition(int qubits) {
     *            for (int i = 0; i &lt; qubits; i++)
     *                hadamard(i);
     *        }
     *    }
     *
     *    /* later &#42;/
     *
     *    var circuit = new Qcircuit() {{
     *        apply(new UniformSuperposition(5),
     *              new int[] {3, 7, 2, 4, 8},
     *              new int[] {}
     *        );
     *
     *        measure(3, 0);
     *    }};
     *
     *    System.out.println(circuit);
     * </code></pre>
     *
     * <p>
     *   Output:
     * </p>
     *
     * <pre><code class="plaintext">
     *    q0:  ─────────────────────────────
     *    q1:  ─┌───────────────────────┐───
     *    q2:  ─┤2  UniformSuperposi... ├───
     *    q3:  ─┤0                      ├─M─
     *    q4:  ─┤3                      ├─║─
     *    q5:  ─│                       │─║─
     *    q6:  ─│                       │─║─
     *    q7:  ─┤1                      ├─║─
     *    q8:  ─┤4                      ├─║─
     *    c0:  ═└───────────────────────┘═╚═
     * </code></pre>
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
     * <p>
     *   Example usage:
     * </p>
     *
     * <pre><code class="language-java">
     *    var circuit = new Qcircuit() {{
     *        /* ... &#42;/
     *
     *        /*
     *         * measure the second qubit
     *         * and store the results in the
     *         * first classical bit
     *         &#42;/
     *        measure(1, 0);
     *
     *        /* ... &#42;/
     *    }};
     * </code></pre>
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
     * <p>
     *   Example usage:
     * </p>
     *
     * <pre><code class="language-java">
     *    var circuit = new Qcircuit() {{
     *        setName("MyCircuit");
     *    
     *        /* ... &#42;/
     *    }};
     * </code></pre>
     *
     * @param name name
     * @see #getName()
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
     * If the name wasn't set explicitly, the class name is used.
     * Useful for debugging and other inspection.
     * 
     * <p>
     *   Example usage:
     * </p>
     *
     * <pre><code class="language-java">
     *    class MyCircuit extends Qcircuit {{
     *    }};
     *
     *    /* later &#42;/
     *
     *    var circuit = new MyCircuit();
     *    System.out.println(circuit.getName());
     * </code></pre>
     *
     * <p>
     *   Output:
     * </p>
     *
     * <pre><code class="plaintext">
     *    MyCircuit
     * </code></pre>
     *
     * @see #setName(String)
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

    /** 
     * The quantum register size.
     *
     * @see Qcircuit#registersFixed
     *
     * */
    private int qRegister = 0;

    /** 
     * The classical register size.
     *
     * @see Qcircuit#registersFixed
     *
     * */
    private int cRegister = 0;

    /** 
     * If true, both quantum and classical registers are not allowed
     * to grow or shrink in size. If false, their sizes can be modified
     * freely.
     *
     * @see Qcircuit#qRegister
     * @see Qcircuit#cRegister
     *
     * */
    private boolean registersFixed = false;

    /**
     * The list of the {@code Qcircuit}'s {@link Qnode Qnodes}.
     *
     * */
    private List<Qnode> nodesList = new ArrayList<>();

    /**
     * The {@code Qcircuit}'s name, if not default.
     *
     * @see Qcircuit#getName
     * @see Qcircuit#setName(String)
     *
     * */
    private String name;
}
