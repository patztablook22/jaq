package io.github.patztablook22.jaq.backends;

import io.github.patztablook22.jaq.Qvm;
import io.github.patztablook22.jaq.Qcircuit;
import io.github.patztablook22.jaq.Qop;
import io.github.patztablook22.jaq.Qflow;
import io.github.patztablook22.jaq.backends.lingebra.Ket;
import io.github.patztablook22.jaq.backends.lingebra.SparseOperator;

import java.util.HashMap;
import java.util.Random;
import java.util.Iterator;
import java.util.Arrays;


/**
 * Linear algebra based quantum computer simulator.
 *
 * Uses the {@link io.github.patztablook22.jaq.backends.lingebra lingebra} 
 * package to perform the quantum transformations.
 *
 * <p>
 *   Note that the dimension of the underlying state Hilbert space grows
 *   exponentially with the number of qubits. Specifically,
 *   \(
 *        \mathop{dim} \mathbf H = 2^N
 *   \)
 *
 *   where {@code N} is the number of qubits. This renders simulating
 *   quantum circuits with too many qubits intractable, as the program
 *   runs out of memory or the computations take too much time.
 * </p>
 *
 * */
public class SimpleSimulator implements Qvm {

    /**
     * The internal randomness generator.
     *
     * */
    private Random random;

    /**
     * Constructs a {@code SimpleSimulator} with the default
     * internal {@link java.util.Random Random} random number generator.
     *
     * */
    public SimpleSimulator() {
        random = new Random();
    }

    /**
     * Constructs a {@code SimpleSimulator} with the specified
     * seed for the internal {@link java.util.Random Random} random number generator.
     *
     * @param seed randomness seed
     * */

    public SimpleSimulator(long seed) {
        random = new Random(seed);
    }

    @Override
    public byte[] run(Qcircuit circuit) {
        var worker = new Worker(circuit);
        return worker.run();
    }

    @Override
    public byte[][] run(Qcircuit circuit, int shots) {
        var worker = new Worker(circuit);
        return worker.run(shots);
    }

    /**
     * Reusable single Qcircuit execution worker.
     *
     * */
    private class Worker extends Qflow {

        /**
         * Complex vector representing the quantum system's global state.
         *
         * */
        private Ket state;

        /**
         * The process' classical register.
         *
         * */
        private byte[] classical;

        /**
         * Constructs a Worker for given {@code Qcircuit}.
         *
         * @param circuit underlying Qcircuit
         *
         * */
        public Worker(Qcircuit circuit) {
            super(circuit);
        }

        /**
         * Runs the Qcircuit once, returns a copy of the resulting
         * classical register.
         *
         * @return resulting classical register copy
         *
         * */
        public byte[] run() {
            flow();
            return classical.clone();
        }

        /**
         * Runs the Qcircuit {@code shots} times, 
         * returns copies of the resulting
         * classical registers as a 2D byte array.
         *
         * @param shots the number of execution repetitions
         * @return resulting classical registers copy
         *
         * */
        public byte[][] run(int shots) {
            byte[][] data = new byte[shots][];
            for (int i = 0; i < shots; i++)
                data[i] = run();
            return data;
        }

        @Override
        protected void hadamard(int qubit) {
            int padBefore = 1 << qubit;
            int padAfter = 1 << (getCircuit().qubits() - qubit - 1);

            state = SparseOperator.eye(padBefore)
                .kronecker(hadamardKernel)
                .kronecker(SparseOperator.eye(padAfter))
                .transform(state);
        }

        @Override
        protected void pauliX(int qubit) {
            int padBefore = 1 << qubit;
            int padAfter = 1 << (getCircuit().qubits() - qubit - 1);

            state = SparseOperator.eye(padBefore)
                .kronecker(SparseOperator.yey(2))
                .kronecker(SparseOperator.eye(padAfter))
                .transform(state);
        }

        @Override
        protected void rotateX(int qubit, double angle) {
        }

        @Override
        protected void measure(int source, int target) {
            float[] real = state.getReal();
            float[] imag = state.getImag();

            /* probability density from quantum state amplitude
             * density = \sum_k |state_k|^2
             */

            float density1 = 0;
            for (int i: marginalIndices(source, 1))
                density1 += real[i] * real[i] + imag[i] * imag[i];

            //System.out.println(density1);
            int result = random.nextFloat() < density1 ? 1 : 0;

            for (int i : marginalIndices(source, 1 - result)) {
                real[i] = 0;
                imag[i] = 0;
            }
            state.normalize();

            classical[target] = (byte) result;
        }

        @Override
        protected void cnot(int control, int target) {
            int padBefore, padAfter;
            SparseOperator kernel;

            if (control < target) {
                padBefore = 1 << control;
                padAfter = 1 << (getCircuit().qubits() - target - 1);

                kernel = SparseOperator.basisKetbra(0, 0)
                    .kronecker(SparseOperator.eye(1 << (target - control)))
                    .add(SparseOperator.basisKetbra(1, 1)
                        .kronecker(SparseOperator.eye(1 << (target - control - 1)))
                        .kronecker(SparseOperator.yey(2)));

            } else {

                padBefore = 1 << target;
                padAfter = 1 << (getCircuit().qubits() - control - 1);

                kernel = SparseOperator.eye(1 << (control - target))
                    .kronecker(SparseOperator.basisKetbra(0, 0))
                    .add(SparseOperator.yey(2)
                        .kronecker(SparseOperator.eye(1 << (control - target - 1)))
                        .kronecker(SparseOperator.basisKetbra(1, 1)));
            }

            state = SparseOperator.eye(padBefore)
                .kronecker(kernel)
                .kronecker(SparseOperator.eye(padAfter))
                .transform(state);
        }

        @Override
        public void flow() {
            if (state == null) {
                int stateDim = 1 << getCircuit().qubits();

                state = new Ket(stateDim);
                classical = new byte[getCircuit().cbits()];
            } else {
                state.zero();
                Arrays.fill(classical, (byte) 0);
            }
            state.getReal()[0] = 1;
            super.flow();
        }

        /**
         * Returns a lazy iterable returning the indices
         * of all global states in which {@code qubit} 
         * has the value {@code value}. Meant for a direct
         * use in for cycles for cleaner logic code.
         *
         * @param qubit the desired qubit
         * @param value the qubit's value
         *
         * @return iterable over the specified global state indices
         *
         * */
        private Iterable<Integer> marginalIndices(int qubit, int value) {
            return new Iterable<Integer>() {

                @Override
                public Iterator<Integer> iterator() {

                    return new Iterator<Integer>() {

                        private int iter = findNext(0);

                        private int findNext(int iter) {
                            while (iter < state.getDim()) {
                                /*
                                 * the binary representation of iter must have
                                 * the digit `state` (either 1 or 0) 
                                 * at `getCircuit().qubits() - system`-th position
                                 */

                                int digit = (iter >> (getCircuit().qubits() - qubit - 1)) & 1;
                                if (digit == value)
                                    break;

                                iter++;
                            }
                            return iter;
                        }

                        @Override
                        public boolean hasNext() {
                            return iter < state.getDim();
                        }

                        @Override
                        public Integer next() {
                            int temp = iter;
                            iter = findNext(iter + 1);
                            return temp;
                        }
                    };
                }
            };
        }
    }

    /**
     * Pre-cached {@link io.github.patztablook22.jaq.nodes.Hadamard}
     * gate kernel.
     * */
    private static SparseOperator hadamardKernel;

    static {
        float invsqrt2 = (float) Math.sqrt(2) / 2;

        hadamardKernel = new SparseOperator(
                new float[] {invsqrt2,  invsqrt2,
                             invsqrt2, -invsqrt2},
                             null);
    }
}
