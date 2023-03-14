package io.github.patztablook22.jaq;


/**
 * Atomic quantum operation.
 *
 * Can be either a unitary transformation gate {@link Qgate}
 * or a non-unitary operation such as 
 * {@link io.github.patztablook22.jaq.nodes.Measure}.
 *
 * <p>
 *   Intuitively, computations manipulating purely the
 *   system's quantum state are {@link Qgate Qgates}. Contrarily,
 *   computatons that mix the quantum and the classical 
 *   world in some concrete way are proper {@code Qop}s.
 * </p>
 *
 * */
public interface Qop extends Qnode {
}
