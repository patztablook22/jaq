package io.github.patztablook22.jaq;

import java.util.List;
import java.util.Iterator;
import java.util.Arrays;
import java.util.NoSuchElementException;
import io.github.patztablook22.jaq.QcircuitTracer;
import io.github.patztablook22.jaq.Qgate;


/**
 * A quantum circuit. Stores an ordered list of its quantum gates.
 *
 * */

public class Qcircuit implements Iterable<Qgate> {

    private int qubitsTotal;
    private int measurementsTotal;
    private List<Qgate> gates;

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
        gates = tracer.getGates();
        measurementsTotal = tracer.getMeasurements();
    }

    /**
     * Constructs an empty quantum circuit. Intended for inheriting.
     *
     * */

    protected Qcircuit() {
        var tracer = QcircuitTracer.trace(this::build);

        qubitsTotal = tracer.getQubits();
        gates = tracer.getGates();
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
     * @return An iterator over the quantum circuit's gates.
     * Topological order - can be considered a task queue.
     *
     * */

    @Override
    public Iterator<Qgate> iterator() {
        return new Iterator<Qgate>() {
            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < gates.size();
            }

            @Override
            public Qgate next() {
                if (!hasNext())
                    throw new NoSuchElementException();

                return gates.get(index++);
            }
        };
    }

    /**
     * @return The number of gates in the quantum circuit.
     *
     * */

    public int length() {
        return gates.size();
    }

    /**
     * @param index Index of the quantum gate.
     * @return The quantum gate with the given index.
     *
     * */

    public Qgate get(int index) {
        return gates.get(index);
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
        int minGateWidth = 3;

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
                Qgate gate = get(g);
                boolean isAbove = false, isBelow = false; 
                boolean isOperand = false, isMainOperand = false;

                for (int operand: gate) {
                    if (operand > q) 
                        isBelow = true;

                    if (operand < q)
                        isAbove = true;

                    if (operand == q)
                        isOperand = true;
                }

                if (gate.arity() > 0 && gate.operand(0) == q)
                    isMainOperand = true;

                if (isMainOperand) {
                    int pad = Math.max(minGateWidth - labels[g].length(), 2);
                    int padLeft = pad / 2;

                    for (int i = 0; i < padLeft; i++)
                        sb.append("─");

                    sb.append(labels[g]);

                    for (int i = padLeft; i < pad; i++)
                        sb.append("─");

                } else {
                    int pad = Math.max(minGateWidth, labels[g].length() + 2) - 1;
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
