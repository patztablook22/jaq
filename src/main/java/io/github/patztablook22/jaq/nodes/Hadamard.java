package io.github.patztablook22.jaq.nodes;

import io.github.patztablook22.jaq.Qgate;


/**
 * The Hadmard gate, acting on a single qubit:
 * \[
 *      H = \frac{1}{\sqrt{2}}
 *      \begin{pmatrix}
 *      1 &amp;  1 \\
 *      1 &amp; -1 \\
 *      \end{pmatrix}
 * \]
 *
 * <p>
 *   When applied on \( \ket{0} \), it generates the uniform 0-1 superposition:
 *   \[
 *        H \ket{0} = \frac{1}{\sqrt{2}} \ket{0} + \frac{1}{\sqrt{2}} \ket{1} = \ket{+}
 *   \]
 *
 *   The matrix is Hermitean, therefore the Hadamard gate is an involution:
 *   \[
 *        H H \ket{\psi} = H H^\dagger \ket{\psi} = I \ket{\psi} = \ket{\psi}
 *   \]
 * </p>
 *
 * */
public class Hadamard implements Qgate {

    /**
     * The underlying qubit.
     *
     * */
    private int qubit;

    /**
     * Constructs a Hadamard gate acting on a given qubit.
     *
     * @param qubit the gate's qubit
     *
     * */
    public Hadamard(int qubit) {
        this.qubit = qubit;
    }

    /**
     * Returns the gate's qubit.
     *
     * @return the gate's qubit
     *
     * */
    public int getQubit() {
        return qubit;
    }
}
