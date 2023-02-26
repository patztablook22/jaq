package io.github.patztablook22.jaq.nodes;

import io.github.patztablook22.jaq.Qgate;


/**
 * The Controlled-NOT (CNOT) gate.
 *
 * <p>
 *   Leaves the {@code target} qubit unchanged when the {@code control} qubit is 0.
 *   Otherwise it negates the {@code target} qubit.
 * </p>
 *
 * <p>
 *   In a 2-qubit system, it can be represented by the following unitary matrix, 
 *   the first qubit being the {@code control} one:
 *   \[
 *      \mathop{CNOT} = \begin{pmatrix}
 *                          1 &amp; 0 &amp; 0 &amp; 0 \\
 *                          0 &amp; 1 &amp; 0 &amp; 0 \\
 *                          0 &amp; 0 &amp; 0 &amp; 1 \\
 *                          0 &amp; 0 &amp; 1 &amp; 0 \\
 *                      \end{pmatrix}
 *   \]
 * </p>
 *
 * <p>
 *   In a multi-qubit system, it's generally not possible to represent it as one matrix.
 *   Instead, the following algebraic representation can be used, assuming {@code control < target}
 *   (the opposite case is symmetric):
 * </p>
 *
 * \[
 *    \mathop{CNOT} = I_{2^{\mathrm{control}}} \otimes 
 *                      ( \ket{0} \bra{0} \otimes I_{2^{\mathrm{target} - \mathrm{control}}}
 *                      + \ket{1} \bra{1} \otimes I_{2^{\mathrm{target} - \mathrm{control} - 1}} \otimes 
 *                      \begin{pmatrix}
 *                          0 &amp; 1 \\
 *                          1 &amp; 0 \\
 *                      \end{pmatrix}
 *                      ) \otimes 
 *
 *                      I_{2^{\mathrm{qubits} - \mathrm{target} - 1}}
 * \]
 *
 * <p>
 *   The first and the last identity matrices represent leaving the non-participating qubits before and after unchanged.
 * </p>
 *
 * <p>
 *   The \( \ket{0} \bra{0} \) kronecker product specifies what happens when the control qubit is 0. Namely,
 *   control qubit is left unchanged, which is followed by an identity matrix over the non-participating qubits
 *   between the {@code control} and the {@code target} qubit and the {@code target} qubit itself.
 * </p>
 *
 * <p>
 *   The \( \ket{1} \bra{1} \) kronecker product block specifies what happens when the control qubit is 1.
 *   The control qubit is again left unchanged. The non-participating qubits between are left unchanged too,
 *   which is specified again by an identity matrix. This is followed by the permutation ("mirror identity") matrix which flips 1 and 0
 *   in the {@code target} qubit.
 * </p>
 *
 * <p>
 *   The matrix is Hermitean, therefore the CNOT gate is an involution:
 *   \[
 *        \mathop{CNOT} \mathop{CNOT} \ket{\psi} = 
 *        \mathop{CNOT} \mathop{CNOT}^\dagger \ket{\psi} = I 
 *        \ket{\psi} = \ket{\psi}
 *   \]
 * </p>
 *
 *
 * */
public class Cnot implements Qgate {
    private int control, target;

    /**
     * Constructs a CNOT gate controlled by and acting on
     * specified qubits.
     *
     * @param control controlling qubit
     * @param target target qubit
     *
     * */
    public Cnot(int control, int target) {
        this.control = control;
        this.target = target;
    }

    /**
     * Returns the control qubit.
     *
     * @return the control qubit.
     *
     * */
    public int getControl() {
        return control;
    }

    /**
     * Returns the target qubit.
     *
     * @return the target qubit.
     *
     * */
    public int getTarget() {
        return target;
    }
}
