package io.github.patztablook22.jaq.nodes;

import io.github.patztablook22.jaq.Qgate;


/**
 * The Pauli X gate:
 * \[
 *      X = 
 *      \begin{pmatrix}
 *      0 &amp; 1 \\
 *      1 &amp; 0 \\
 *      \end{pmatrix}
 * \]
 *
 * The Pauli X gate is the quantum equaivalent of the {@code NOT} gate. It
 * swaps the coordinates corresponding to the 1 and 0 computational basis vectors.
 *
 * For example:
 * \begin{align}
 *      \ket{0} &amp; \mapsto \ket{1} \\ 
 *      \ket{1} &amp; \mapsto \ket{0} \\ 
 *      \ket{+} &amp; \mapsto \ket{+} \\ 
 *      \ket{-} &amp; \mapsto \ket{-} \\ 
 * \end{align}
 *
 * The matrix is Hermitean, therefore the Pauli X gate is an involution:
 * \[
 *      X X \ket{\psi} = X X^\dagger \ket{\psi} = I \ket{\psi} = \ket{\psi}
 * \]
 *
 *
 * */
public class PauliX implements Qgate {

    /**
     * The underlying qubit.
     *
     * */
    private int qubit;

    /**
     * Constructs a Pauli X gate acting on a given qubit.
     *
     * @param qubit the gate's qubit
     *
     * */
    public PauliX(int qubit) {
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
