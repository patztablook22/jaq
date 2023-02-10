package io.github.patztablook22.jaq;

import java.util.List;
import java.util.Iterator;
import java.util.Arrays;
import java.util.NoSuchElementException;
import io.github.patztablook22.jaq.QcircuitTracer;
import io.github.patztablook22.jaq.Qop;


/**
 * A quantum circuit. Stores an ordered list of operations on qubits.
 *
 * */

public class Qcircuit implements Iterable<Qop> {

    private int qubitsTotal;
    private int measurementsTotal;
    private List<Qop> ops;

    /**
     * Builder method to be used when subclassing Qcircuit
     * The body should describe the structure of the quantum circuit.
     *
     * */

    protected void build() {
    }

    /**
     * Constructs a quantum circuit using the provided build method.
     * @param build The build method.
     *
     * */

    public Qcircuit(Runnable build) {
        var tracer = QcircuitTracer.trace(build);

        qubitsTotal = tracer.getQubits();
        ops = tracer.getOps();
        measurementsTotal = tracer.getMeasurements();
    }

    /**
     * Constructs an empty quantum circuit. Intended for inheriting.
     *
     * */

    protected Qcircuit() {
        var tracer = QcircuitTracer.trace(this::build);

        qubitsTotal = tracer.getQubits();
        ops = tracer.getOps();
        measurementsTotal = tracer.getMeasurements();
    }

    /**
     * @return The total number of qubits the quantum circuit employs.
     *
     * */

    public int qubits() {
        return qubitsTotal;
    }

    /**
     * @return The total number of measured bits in the quantum circuit.
     * Note: A joint measurement on n qubits counts indeed as n.
     *
     * */

    public int measurements() {
        return measurementsTotal;
    }

    /**
     * @return An iterator over the quantum circuit's ops.
     * Topological order - can be considered a task queue.
     *
     * */

    @Override
    public Iterator<Qop> iterator() {
        return new Iterator<Qop>() {
            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < ops.size();
            }

            @Override
            public Qop next() {
                if (!hasNext())
                    throw new NoSuchElementException();

                return ops.get(index++);
            }
        };
    }

    /**
     * @return The number of ops in the quantum circuit.
     *
     * */

    public int length() {
        return ops.size();
    }

    /**
     * @param index Index of the op.
     * @return The op with the given index.
     *
     * */

    public Qop get(int index) {
        return ops.get(index);
    }

    /**
     * Creates a multiple-line string representation.
     * of the quantum circuit diagram.
     *
     * @return String representation of the quantum circuit diagram.
     *
     * */

    @Override
    public String toString() {
        var sb = new StringBuilder();
        int qubitsDigits = (int) Math.log10(qubitsTotal + 0.5);
        int minOpWidth = 3;

        String labels[] = new String[length()];
        for (int i = 0; i < length(); i++)
            labels[i] = get(i).label();

        for (int q = 0; q < qubits(); q++) {
            if (q != 0)
                sb.append("\n");

            String temp = Integer.toString(q);
            sb.append(temp);
            sb.append(": ");

            for (int i = temp.length(); i < qubitsDigits; i++)
                sb.append(' ');

            //sb.append("|0〉");
            sb.append("  ");

            for (int g = 0; g < length(); g++) {
                Qop op = get(g);
                boolean isAbove = false, isBelow = false; 
                boolean isOperand = false, isMainOperand = false;

                for (int operand: op) {
                    if (operand > q) 
                        isBelow = true;

                    if (operand < q)
                        isAbove = true;

                    if (operand == q)
                        isOperand = true;
                }

                if (op.arity() > 0 && op.operand(0) == q)
                    isMainOperand = true;

                if (isMainOperand) {
                    int pad = Math.max(minOpWidth - labels[g].length(), 2);
                    int padLeft = pad / 2;

                    for (int i = 0; i < padLeft; i++)
                        sb.append("─");

                    sb.append(labels[g]);

                    for (int i = padLeft; i < pad; i++)
                        sb.append("─");

                } else {
                    int pad = Math.max(minOpWidth, labels[g].length() + 2) - 1;
                    int padLeft = pad / 2;

                    for (int i = 0; i < padLeft; i++)
                            sb.append("─");

                    if (isOperand) {
                        if (isAbove && isBelow)
                            sb.append("┼");
                        else if (isAbove)
                            sb.append("┴");
                        else if (isBelow)
                            sb.append("┬");
                        else
                            sb.append("┼");
                    } else {
                        if (isAbove && isBelow)
                            sb.append("┊");
                        else
                            sb.append("─");
                    }

                    for (int i = padLeft; i < pad; i++)
                            sb.append("─");
                }
            }
        }
        return sb.toString();
    }
}
