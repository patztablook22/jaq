package io.github.patztablook22.jaq;


/**
 * Unitary quantum transformation gate.
 *
 * <p>
 *   In simple settings, it can be represented by
 *   a unitary matrix:
 *   \[
 *          \ket{\psi} \mapsto \hat{U} \ket{\psi}
 *   \]
 * </p>
 *
 * <p>
 *   In general, especially when spread across
 *   non-neighboring qubits, that matrix may not
 *   be able to represent the quantum gate. In
 *   that case, the matrix may be used as a
 *   building block for constructing the more global
 *   unitary operator:
 *   \[
 *          \ket{\psi} \mapsto
 *          (I_{2^{\mathrm{begin}}} \otimes 
 *          \hat{U} \otimes
 *          I_{2^{\mathrm{qubits} - \mathrm{end}}}) \ket{\psi}
 *   \]
 *   Where the identity matrices represent "padding" the operation
 *   kernel by inactivity on the non-participating qubits before and
 *   after the participating ones.
 * </p>
 *
 * */
public interface Qgate extends Qop {
}
