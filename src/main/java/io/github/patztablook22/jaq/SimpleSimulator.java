package io.github.patztablook22.jaq;

import java.util.HashMap;
import io.github.patztablook22.jaq.Qcircuit;
import io.github.patztablook22.jaq.Qubit;
import io.github.patztablook22.jaq.Qop;


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
        private double[] stateReal;
        private double[] stateImag;

        public Executor(Qcircuit circuit) {
            this.circuit = circuit;
            if (circuit.qubits() == 0)
                return;

            int spaceDim = 2 << (circuit.qubits() - 1);
            stateReal = new double[spaceDim];
            stateImag = new double[spaceDim];
        }

        public byte[] run() {
            byte[] measurements = new byte[circuit.measurements()];
            for (Qop op: circuit) {
            }
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
