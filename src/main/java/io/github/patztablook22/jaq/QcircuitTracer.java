package io.github.patztablook22.jaq;

import java.io.Closeable;
import io.github.patztablook22.jaq.Qgate;
import java.util.List;
import java.util.ArrayList;

import io.github.patztablook22.jaq.IllegalQcircuitContextException;
import io.github.patztablook22.jaq.gates.Measure;


/**
 * A tracing-based Qcircuit construction helper. Capable of running methods
 * in a special context with the power to assemble an imperative intermediate
 * representation of the method's body.
 *
 * */

class QcircuitTracer implements Closeable {

    private static QcircuitTracer currentTracer = null;

    private int qubits = 0;
    private int measurements = 0;
    private List<Qgate> gates = new ArrayList<>();

    /**
     * Trace the provided method. Only one tracing can occur at a time,
     * therefore the method is synchronized.
     * 
     * @return The closed `QcircuitTracer` capturing the method trace.
     *
     * */

    public synchronized static QcircuitTracer trace(Runnable builder) {
        QcircuitTracer success = null;

        try (var tracer = new QcircuitTracer()) {
            builder.run();
            success = tracer;
        } catch (Exception e) {
            throw e;
        }

        return success;
    }

    /**
     * @return The total number of Qubits traced.
     *
     * */

    public int getQubits() {
        return qubits;
    }

    /**
     * @return The total number of measured bits.
     * */

    public int getMeasurements() {
        return measurements;
    }

    /**
     * @return The total number of Qgates traced.
     *
     * */

    public List<Qgate> getGates() {
        return gates;
    }

    /**
     * Constructs and opens a `QcircuitTracer`.
     *
     * */

    private QcircuitTracer() {
        if (currentTracer != null)
            throw new IllegalQcircuitContextException();
        currentTracer = this;
    }

    /**
     * Closes the `QcircuitTracer`.
     *
     * */

    @Override
    public void close() {
        if (currentTracer != this)
            throw new IllegalQcircuitContextException();
        currentTracer = null;
    }

    /**
     * @return Whether there an ongoing tracing.
     *
     * */

    public static boolean tracing() {
        return currentTracer != null;
    }

    /**
     * @return The current `QcircuitTracer`. Exception thrown if none.
     *
     * */

    public static QcircuitTracer getCurrentTracer() {
        if (!tracing())
            throw new IllegalQcircuitContextException();

        return currentTracer;
    }

    /**
     * Adds a qubit to the current trace.
     * @return The ID of the new qubit.
     *
     * */

    public int addQubit() {
        if (currentTracer != this)
            throw new IllegalQcircuitContextException();
        return qubits++;
    }

    /**
     * Adds a quantum gate to the trace.
     * @param gate The quantum gate to add.
     *
     * */

    public void addGate(Qgate gate) {
        if (currentTracer != this)
            throw new IllegalQcircuitContextException();

        gates.add(gate);

        if (gate instanceof Measure)
            measurements += gate.arity();
    }
}
