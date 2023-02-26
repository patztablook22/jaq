package io.github.patztablook22.jaq.nodes;

import io.github.patztablook22.jaq.Qop;


/**
 * <p>
 *   Partial measurement operation. Measures a single specified qubit
 *   and stores the result in a specified classical bit.
 * </p>
 *
 * <p>
 *   Measurement is a {@link io.github.patztablook22.jaq.Qop}, 
 *   not a quantum gate {@link io.github.patztablook22.jaq.Qgate},
 *   as its behavior is very non-unitary by nature. When one measures a qubit,
 *   there is no way of restoring its former superposition
 *   other than repeating the entire quantum process.
 *
 *   This collapse of superposition inherently causes an irrestorable <i>loss</I> 
 *   of information. Therefore, ideally it should be used as little as possible
 *   to maximize the computational power. In reality, today's quantum computers
 *   are noisy and make errors, so it is sometimes necessary to intentionally
 *   cause a controlled collapse to prevent high error rates.
 * </p>
 *
 * <p>
 *   Let a quantum system with {@code N} qubits be in the state
 *   \begin{align}
 *          \ket{\psi} &amp;= \sum_{s=0}^{2^N - 1} \lambda_s
 *          \bigotimes_{k=0}^{N-1} \ket{b_k^{(s)}} \\
 *          \\
 *          \lambda_s &amp;= \alpha_s e^{\phi_s}
 *   \end{align}
 *   where \( \lambda_s \) are the coordinates of the state with respect to
 *   the computational basis, \( \alpha_s \) are the corresponding amplitudes
 *   and \( \phi_s \) the corresponding phases.
 * </p>
 *
 * <p>
 *   The measurement's probability mass function is given by the marginal
 *   probabilities over the entire system. Specifically, it is the sum of the
 *   squared amplitudes of all global states consistent with given observation.
 *   \[
 *          P(\mathrm{source} = x) = \sum_{s} \alpha_s^2
 *          \quad \text{s.t.} \quad \ket{b_{\mathrm{source}}^{(s)}} = \ket{x}
 *   \]
 * </p>
 *
 * <p>
 *   After the measurement, the system can no longer include in its superposition
 *   any state inconsistent with the observation.
 *   Given the observation {@code x}:
 *   \[
 *          \tilde \alpha_s = \begin{cases}
 *               \alpha_s &amp; \text{if} \quad \ket{b_{\mathrm{source}}^{(s)}} = \ket{x} \\
 *               0 &amp; \text{otherwise}
 *          \end{cases}
 *   \]
 *   However, this is not a unit vector. To get a valid quantum state vector,
 *   it must be normalized:
 *   \[
 *      \ket{\psi'} = \frac{\tilde \psi}{||\tilde \psi||}
 *   \]
 *
 * </p>
 *
 *
 * */
public class Measure implements Qop {
    private int source, target;

    public Measure(int source, int target) {
        this.source = source;
        this.target = target;
    }

    public int getSource() {
        return source;
    }

    public int getTarget() {
        return target;
    }
}
