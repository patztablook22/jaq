package io.github.patztablook22.jaq;

import java.util.Arrays;
import java.util.stream.*;
import io.github.patztablook22.jaq.QcircuitTracer;
import io.github.patztablook22.jaq.gates.*;

/**
 * A qubit.
 *
 * */

public class Qubit {

    private QcircuitTracer tracer;
    private int id;

    /**
     * Constructs a qubit initialized to |0〉. Can only be constructed
     * inside a Qcircuit build scope, otherwise an exception is thrown.
     *
     * */

    public Qubit() {
        tracer = QcircuitTracer.getCurrentTracer();
        id = tracer.addQubit();
    }

    /**
     * Applies the Hadamard gate.
     *
     * */

    public Qubit hadamard() {
        tracer.addGate(new Hadamard(id));
        return this;
    }

    public Qubit hadamard(Qubit b) {
        if (tracer != b.tracer)
            throw new IllegalQcircuitContextException();

        tracer.addGate(new Hadamard(id, b.id));
        return this;
    }

    public Qubit cnot() {
        tracer.addGate(new Cnot(id));
        return this;
    }

    public Qubit cnot(Qubit b) {
        if (tracer != b.tracer)
            throw new IllegalQcircuitContextException();

        tracer.addGate(new Cnot(id, b.id));
        return this;
    }

    public Qubit measure() {
        tracer.addGate(new Measure(id));
        return this;
    }

    public static void measureJoint(Qubit[] qubits) {
        if (qubits.length == 0)
            throw new IllegalQcircuitContextException();

        QcircuitTracer tracer = qubits[0].tracer;
        if (Arrays.stream(qubits).anyMatch(q -> q.tracer != tracer))
            throw new IllegalQcircuitContextException();

        tracer.addGate(new Measure(Arrays.stream(qubits)
                                        .mapToInt(q -> q.id)
                                        .toArray()));
    }

    public Qubit toffoli(Qubit b, Qubit c) {
        if (tracer != b.tracer || tracer != c.tracer)
            throw new IllegalQcircuitContextException();
        tracer.addGate(new Toffoli(id, b.id, c.id));
        return this;
    }

    public Qubit pauliX() {
        tracer.addGate(new PauliX(id));
        return this;
    }

    public Qubit pauliY() {
        tracer.addGate(new PauliY(id));
        return this;
    }

    public Qubit pauliZ() {
        tracer.addGate(new PauliZ(id));
        return this;
    }

    public Qubit phase(double phi) {
        tracer.addGate(new Phase(id, phi));
        return this;
    }

    public Qubit swap(Qubit b) {
        if (tracer != b.tracer)
            throw new IllegalQcircuitContextException();
        tracer.addGate(new Swap(id, b.id));
        return this;
    }
}
