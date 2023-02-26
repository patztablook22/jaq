package io.github.patztablook22.jaq;


/**
 * Atomic quantum operation.
 *
 * Can be either a unitary transformation gate {@link Qgate}
 * or a non-unitary operation such as 
 * {@link io.github.patztablook22.jaq.nodes.Measure}.
 *
 * */
public interface Qop extends Qnode {
}
