package io.github.patztablook22.jaq;

import io.github.patztablook22.jaq.Qcircuit;
import io.github.patztablook22.jaq.Qubit;
import io.github.patztablook22.jaq.Qgate;


/**
 * Simulates the quantum computations on a CPU.
 * Useful for testing quantum algorithms on a smaller scale.
 *
 * Note that the resources (memory, performance) necessary for
 * simulating a quantum circuit scale exponentially with the
 * number of qubits.
 *
 * */

public class SimpleSimulator implements Qvm {

    /**
     * Handles the potentially repeated
     * execution of a given Qcircuit.
     *
     * */

    private static class Executor {

        private Qcircuit circuit;
        private byte[] quantumState;

        public Executor(Qcircuit circuit) {
            this.circuit = circuit;
            int spaceDim = 2 << circuit.qubits();
            quantumState = new byte[spaceDim];
        }

        public byte[] run() {
            byte[] measurements = new byte[circuit.measurements()];

            // TODO

            return measurements;
        }
    }

    @Override
    public byte[] run(Qcircuit circuit) {
        var exec = new Executor(circuit);
        return exec.run();
    }

    @Override
    public byte[][] run(Qcircuit circuit, int samples) {
        byte[][] measurements = new byte[samples][];
        var exec = new Executor(circuit);
        for (int i = 0; i < samples; i++)
            measurements[i] = exec.run();
        return measurements;
    }
}
