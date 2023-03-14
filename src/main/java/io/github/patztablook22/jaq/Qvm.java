package io.github.patztablook22.jaq;


/**
 * Quantum virtual machine interface. Represents an abstraction
 * of a quantum computer - a machine capable of running quantum
 * circuits.
 * Can be either connected to a physical quantum computer
 * or to a linear algebra based simulator.
 *
 * <p>
 *   Should be capable to run any valid {@link Qcircuit}. In case
 *   the {@code Qvm} does not support certain operations, it
 *   is expected to transpile them into the {@code Qvm}'s
 *   universal set.
 *   Can throw various {@link java.lang.RuntimeException RuntimeExceptions} 
 *   if it runs out of resources. For details, see
 *   {@link io.github.patztablook22.jaq.backends backends}.
 * </p>
 *
 * <p>
 *   Example usage:
 * </p>
 *
 *  <pre><code class="language-java">
 *     Qcircuit circuit = /* ... &#42;/;
 *
 *     Qvm backend = new SimpleSimulator();
 *
 *     byte[] data = backend.run(circuit);
 *
 *     for (byte b: data)
 *         System.out.print(b);
 *  </code></pre>
 *
 *  <p>
 *    Example output:
 *  </p>
 *
 *  <pre><code class="plaintext">
 *     10110110
 *  </code></pre>
 *
 * */
public interface Qvm {

    /**
     * Runs the {@link Qcircuit} and returns the resulting
     * classical register as a byte array.
     *
     * <p>
     *   Example usage:
     * </p>
     *
     *  <pre><code class="language-java">
     *     Qcircuit circuit = /* ... &#42;/;
     *
     *     Qvm backend = /* ... &#42;/;
     *
     *     byte[] data = backend.run(circuit);
     *
     *     for (byte b: data)
     *         System.out.print(b);
     *  </code></pre>
     *
     *  <p>
     *    Example output:
     *  </p>
     *
     *  <pre><code class="plaintext">
     *     10110110
     *  </code></pre>
     *
     * <p>
     *   For repeated execution, see {@link #run(Qcircuit, int)}.
     * </p>
     *
     * @param circuit quantum circuit to run
     * @return resultng classical register
     * @see #run(Qcircuit, int)
     *
     * */
    byte[] run(Qcircuit circuit);
    
    /**
     * Runs the {@link Qcircuit} repeatedly {@code shots} times
     * and returns the resulting classical registers as
     * a 2D byte array.
     *
     * <p>
     *   Example usage:
     * </p>
     *
     *  <pre><code class="language-java">
     *     Qcircuit circuit = /* ... &#42;/;
     *
     *     Qvm backend = /* ... &#42;/;
     *
     *     byte[][] data = backend.run(circuit, 4);
     *
     *     for (byte[] experiment: data) {
     *         for (byte b: experiment)
     *             System.out.print(b);
     *         System.out.println();
     *     }
     *  </code></pre>
     *
     *  <p>
     *    Example output:
     *  </p>
     *
     *  <pre><code class="plaintext">
     *     10110110
     *     01010111
     *     01001101
     *     10110110
     *  </code></pre>
     *
     * <p>
     *   Can be faster than {@link #run(Qcircuit)} by reusing
     *   allocated resources.
     * </p>
     *
     * @param circuit quantum circuit to run
     * @param shots the number of repetitions
     *
     * @return resultng classical registers
     * @see #run(Qcircuit)
     *
     * */
    default byte[][] run(Qcircuit circuit, int shots) {
        byte[][] result = new byte[shots][];
        for (int i = 0; i < shots; i++)
            result[i] = run(circuit);
        return result;
    }
}
