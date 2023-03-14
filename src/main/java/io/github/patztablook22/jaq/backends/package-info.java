/**
 * Implemented {@link io.github.patztablook22.jaq.Qvm Qvm} backends.
 *
 * A Qvm implementation is expected to provide the full
 * functionality of a quantum computer, constrained only
 * by the resources available. Namely:
 *
 * <ol>
 *  <li>Accepting circuits with any {@link io.github.patztablook22.jaq.Qnode Qnodes}.</li>
 *  <li>Implicitly or explicitly inlining all nested subcircuits.</li>
 *      <li>Never modifying given quantum circuits. All custom representations should be kept separately.</li>
 *  <li>Transpiling not directly supported (e.g. more complex) quantum operations into the backend's 
 *  <a href='https://en.wikipedia.org/wiki/Functional_completeness'>universal set</a>.</li>
 *  <li>Optimizing the resulting flow graph.</li>
 *  <li>Caching it for efficient reuse.</li>
 * </ol>
 *
 * The backend can throw various 
 * {@link java.lang.RuntimeException RuntimeExceptions}, for example when
 * running out of resources. In case some e.g. family of quantum circuits
 * is known to be handled inefficiently by the backend, a {@code stdout}
 * or {@code stderr} warning is preffered to refusing to run the circuit
 * by throwing an {@link java.lang.IllegalArgumentException IllegalArgumentException}
 * or otherwise.
 *
 * */
package io.github.patztablook22.jaq.backends;

