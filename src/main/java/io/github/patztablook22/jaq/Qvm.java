package io.github.patztablook22.jaq;

import io.github.patztablook22.jaq.Qcircuit;

/**
 * A quantum virtual machine capable of running Qcircuits. 
 * Can be implemented using various backends, 
 * from simulators to a real quantum computer.
 *
 * */

interface Qvm {

    /**
     * Runs the given Qcircuit once.
     *
     * @param circuit The quantum circuit to run.
     * @return An array of qubit measurements, each one in a separate byte.
     *
     * */

    byte[] run(Qcircuit circuit);


    /**
     * Runs the given Qcircuit multiple times..
     *
     * @param circuit The quantum circuit to run.
     * @param samples The sample size (number of repeated executions).
     * @return A matrix of qubit measurements. One run per row.
     * Each measurement is stored in a separate byte.
     *
     * */

    default byte[][] run(Qcircuit circuit, int samples) {
        byte[][] measurements = new byte[samples][];
        for (int i = 0; i < samples; i++)
            measurements[i] = run(circuit);
        return measurements;
    }
}
