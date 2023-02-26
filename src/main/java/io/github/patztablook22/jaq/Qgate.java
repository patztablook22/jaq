package io.github.patztablook22.jaq;


/**
 * Unitary quantum transformation gate.
 *
 * In simple settings, it can be represented by
 * a unitary matrix.
 *
 * In general, especially when spread across
 * non-neighboring qubits, that matrix may not
 * be able to represent the quantum gate. In
 * that case, the matrix may be used as a
 * building block for constructing the more global
 * unitary operator.
 *
 * */
public interface Qgate extends Qop {
}
