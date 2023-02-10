package io.github.patztablook22.jaq;

import java.io.Closeable;
import io.github.patztablook22.jaq.Qop;
import java.util.List;
import java.util.ArrayList;

import io.github.patztablook22.jaq.IllegalQcircuitContextException;
import io.github.patztablook22.jaq.ops.Measure;


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
    private List<Qop> ops = new ArrayList<>();

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
     * @return The total number of Qops traced.
     *
     * */

    public List<Qop> getOps() {
        return ops;
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
     * Adds a quantum op to the trace.
     * @param op The quantum op to add.
     *
     * */

    public void addOp(Qop op) {
        if (currentTracer != this)
            throw new IllegalQcircuitContextException();

        ops.add(op);

        if (op instanceof Measure)
            measurements += op.arity();
    }
}
