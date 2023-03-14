package io.github.patztablook22.jaq;

import io.github.patztablook22.jaq.nodes.*;
import java.util.Arrays;

/**
 * Generator of string diagram representations for single
 * {@link Qnode Qnodes}. Intended to be used by interanlly
 * by {@link QcircuitStringBuilder}.
 *
 * @see QcircuitStringBuilder
 *
 * */
class QnodeStringBuilder {
    /**
     * Constructs {@code QnodeStringBuilder} for given {@link Qnode}
     * within the context of a parent {@link Qcircuit} with the specified
     * number of {@code qubits} and {@code cbits}.
     *
     * @param node the Qnode to work with
     * @param qubits the number of the Qcircuit's qubits
     * @param cbits the number of the Qcircuit's classical bits
     *
     * */
    public QnodeStringBuilder(Qnode node, int qubits, int cbits) {
        this.node = node;
        this.qubits = qubits;
        this.cbits = cbits;
    }

    /**
     * Buffer for the String creation.
     *
     * */
    private StringBuilder sb = new StringBuilder();

    /**
     * {@link Qnode} the {@code QnodeStringBuilder} is working with.
     *
     * */
    private Qnode node;

    /**
     * The number of the parent Qcircuit's qubits.
     *
     * */
    private int qubits;

    /**
     * The number of the parent Qcircuit's classical bits.
     *
     * */
    private int cbits;

    /**
     * Appends the String representation of given
     * object to the internal buffer.
     *
     * @param obj the object to append
     *
     * */
    private void append(Object obj) {
        sb.append(obj);
    }

    /**
     * Appends the String representation of given
     * object to the internal buffer repeated
     * {@code times} times.
     *
     * @param obj the object to repeat
     * @param times the number of repetitions
     *
     * */
    private void repeat(Object obj, int times) {
        for (int i = 0; i < times; i++) sb.append(obj);
    }

    /**
     * Appends a string to the internal buffer,
     * justified to the left to given {@code length}.
     *
     * @param s string to append
     * @param length justification length
     * */
    private void ljust(String s, int length) {
        append(s);
        repeat(" ", length - s.length());
    }

    /**
     * Returns the minimum value in an integer array.
     * returns {@code Integer.MAX_VALUE / 2} if empty.
     *
     * This is a convenience utility method.
     *
     * @param arr array
     * @return minimum value in the given array
     *
     * */
    private int min(int[] arr) {
        int min = Integer.MAX_VALUE / 2;
        for (int i: arr)
            min = Math.min(min, i);
        return min;
    }

    /**
     * Returns the maximum value in an integer array.
     * returns {@code Integer.MIN_VALUE / 2} if empty.
     *
     * This is a convenience utility method.
     *
     * @param arr array
     * @return maximum value in the given array
     *
     * */
    private int max(int[] arr) {
        int max = Integer.MIN_VALUE / 2;
        for (int i: arr)
            max = Math.max(max, i);
        return max;
    }

    /**
     * Returns whether an integer array contains
     * given value.
     *
     * This is a convenience utility method.
     *
     * @param arr array
     * @param val value
     * @return whether the array contains the value
     *
     * */
    private boolean contains(int[] arr, int val) {
        for (int i: arr)
            if (i == val)
                return true;
        return false;
    }

    /**
     * Returns the first occurance index of a value
     * in a given array. If the array doesn't
     * conatain the value, -1 is returned instead.
     *
     * This is a convenience utility method.
     *
     * @param arr array
     * @param val value
     * @return the first occurence index
     *
     * */
    private int indexOf(int[] arr, int val) {
        for (int i = 0; i < arr.length; i++)
            if (arr[i] == val)
                return i;
        return -1;
    }

    /**
     * Dumps the string representation for 
     * {@link io.github.patztablook22.jaq.nodes.Subcircuit}
     * into the internal buffer.
     *
     * @param subcircuit the Subcircuit nodec
     *
     * */
    private void dump(Subcircuit subcircuit) {
        if (subcircuit.getQubits().length == 0 && subcircuit.getCbits().length == 0)
            return;

        String name = subcircuit.getCircuit().getName();

        int firstRow = Math.min(min(subcircuit.getQubits()), 
                                min(subcircuit.getCbits()) + qubits);

        int lastRow = Math.max(max(subcircuit.getQubits()), 
                               max(subcircuit.getCbits()) + qubits);

        int height = lastRow - firstRow + 2;

        int argPosWidth = Math.max(Integer.toString(subcircuit.getQubits().length).length(),
                                   Integer.toString(subcircuit.getCbits().length).length()) + 2;

        int nameWidth = Math.min(name.length(), 2 * height + 3);
        int width = argPosWidth + nameWidth + 1;

        int nameY = 0;

        String displayName;
        if (nameWidth >= name.length())
            displayName = name;
        else
            displayName = name.substring(0, nameWidth - 3) + "...";


        repeat("\n", firstRow);

        append("┌"); 
        repeat("─", width);
        append("┐\n");

        for (int i = firstRow; i <= lastRow; i++) {
            String argPos;
            char borderLeft, borderRight;
            if (i < qubits && contains(subcircuit.getQubits(), i)) {
                argPos = Integer.toString(indexOf(subcircuit.getQubits(), i));
                borderLeft = '┤';
                borderRight = '├';
            } else if (contains(subcircuit.getCbits(), i - qubits)) {
                argPos = Integer.toString(indexOf(subcircuit.getCbits(), i - qubits));
                borderLeft = '╡';
                borderRight = '╞';
            } else {
                argPos = "";
                borderLeft = '│';
                borderRight = '│';
            }
            sb.append(borderLeft);
            ljust(argPos, argPosWidth);

            if (i - firstRow == nameY)
                ljust(displayName, nameWidth + 1);
            else
                repeat(" ", nameWidth + 1);

            sb.append(borderRight).append('\n');
        }
        append("└");
        repeat("─", width);
        append("┘\n");
    }

    /**
     * Dumps the string representation for 
     * {@link io.github.patztablook22.jaq.nodes.Cnot}
     * into the internal buffer.
     *
     * @param cnot the Cnot node
     *
     * */
    private void dump(Cnot cnot) {
        int control = cnot.getControl(), target = cnot.getTarget();
        repeat("\n", Math.min(control, target) + 1);
        if (target < control) {
            append("+\n");
            repeat("┊\n", control - target - 1);
            append("┴\n");
        } else {
            append("┬\n");
            repeat("┊\n", target - control - 1);
            append("+\n");
        }
    }

    /**
     * Dumps the string representation for 
     * {@link io.github.patztablook22.jaq.nodes.Measure}
     * into the internal buffer.
     *
     * @param measure the Measure node
     *
     * */
    private void dump(Measure measure) {
        //append("\n");
        //repeat("─\n", node.getSource());
        repeat("\n", measure.getSource() + 1);
        append("M\n");
        repeat("║\n", qubits - measure.getSource() - 1);
        repeat("║\n", measure.getTarget());
        append("╚\n");
    }

    /**
     * General String representation builder for operations
     * acting on a single qubit. Dumps the representation
     * into the internal buffer.
     *
     * @param qubit the operation's qubit
     * @param label operation label to use
     *
     * */
    private void dumpSingleQubit(int qubit, String label) {
        repeat("\n", qubit + 1);
        append(label);
    }

    /**
     * Invokes the String building process.
     *
     * @return the built String representation
     *
     * */
    public String build() {

        if (node instanceof Hadamard) {
            dumpSingleQubit(((Hadamard) node).getQubit(), "H");
        } else if (node instanceof Subcircuit) {
            dump((Subcircuit) node);
        } else if (node instanceof Cnot) {
            dump((Cnot) node);
        } else if (node instanceof Measure) {
            dump((Measure) node);
        } else if (node instanceof RotateX) {
            dumpSingleQubit(((RotateX) node).getQubit(), "Rx");
        } else if (node instanceof PauliX) {
            dumpSingleQubit(((PauliX) node).getQubit(), "X");
        }

        return sb.toString();
    }
}
