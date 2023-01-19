package io.github.patztablook22.jaq;

import io.github.patztablook22.jaq.QcircuitTracer;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A quantum gate. Stores the IDs of qubits it operates on.
 * The ordering respects the semantics of the operands.
 *
 * */

public interface Qgate extends Iterable<Integer> {

    /**
     * @return The number of qubits the quntum gate operates on.
     *
     * */

    int arity();

    /**
     * @param index Index of the operand.
     * @return The ID of the specified operand qubit.
     *
     * */

    int operand(int index);

    /**
     * @return A label of the quantum gate to be used when e.g. printing.
     *
     * */

    String label();

    /**
     * @return An iterator over the IDs of the quantum gate's operands.
     *
     * */

    default Iterator<Integer> iterator() {
        return new Iterator<Integer>() {
            int index = 0;

            @Override
            public boolean hasNext() {
                return index < arity();
            }

            @Override
            public Integer next() {
                if (!hasNext())
                    throw new NoSuchElementException();

                return operand(index++);
            }
        };
    }
}
