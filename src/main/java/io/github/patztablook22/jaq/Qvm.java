package io.github.patztablook22.jaq;


/**
 * Quantum virtual machine interface. Represents an abstraction
 * of a quantum computer - a machine capable of running quantum
 * circuits.
 *
 * Can be either connected to a physical quantum computer
 * or to a linear algebra based simulator.
 *
 * */
public interface Qvm {

    /**
     * Runs the {@link Qcircuit} and returns the resulting
     * classical register as a byte array.
     *
     * @param circuit quantum circuit to run
     * @return resultng classical register
     *
     * */
    byte[] run(Qcircuit circuit);
    
    /**
     * Runs the {@link Qcircuit} repeatedly {@code shots} times
     * and returns the resulting classical registers as
     * a 2D byte array.
     *
     * Can be faster than {@link #run(Qcircuit)} by reusing
     * allocated resources.
     *
     * @param circuit quantum circuit to run
     * @param shots the number of repetitions
     *
     * @return resultng classical registers
     *
     * */
    default byte[][] run(Qcircuit circuit, int shots) {
        byte[][] result = new byte[shots][];
        for (int i = 0; i < shots; i++)
            result[i] = run(circuit);
        return result;
    }
}
