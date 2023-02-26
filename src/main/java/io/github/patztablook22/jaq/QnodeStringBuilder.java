package io.github.patztablook22.jaq;

import io.github.patztablook22.jaq.nodes.*;
import java.util.Arrays;


class QnodeStringBuilder {
    public QnodeStringBuilder(Qnode node, int qubits, int cbits) {
        this.node = node;
        this.qubits = qubits;
        this.cbits = cbits;
    }

    private StringBuilder sb = new StringBuilder();
    private Qnode node;
    private int qubits;
    private int cbits;

    private void repeat(String s, int times) {
        for (int i = 0; i < times; i++) sb.append(s);
    }

    private void ljust(String s, int length) {
        append(s);
        repeat(" ", length - s.length());
    }

    private int min(int[] arr) {
        int min = Integer.MAX_VALUE / 2;
        for (int i: arr)
            min = Math.min(min, i);
        return min;
    }

    private int max(int[] arr) {
        int max = Integer.MIN_VALUE / 2;
        for (int i: arr)
            max = Math.max(max, i);
        return max;
    }

    private boolean contains(int[] arr, int val) {
        for (int i: arr)
            if (i == val)
                return true;
        return false;
    }

    private int indexOf(int[] arr, int val) {
        for (int i = 0; i < arr.length; i++)
            if (arr[i] == val)
                return i;
        return -1;
    }

    private void append(Object o) {
        sb.append(o);
    }

    private void dump(Subcircuit node) {
        if (node.getQubits().length == 0 && node.getCbits().length == 0)
            return;

        String name = node.getCircuit().getName();

        int firstRow = Math.min(min(node.getQubits()), min(node.getCbits()) + qubits);
        int lastRow = Math.max(max(node.getQubits()), max(node.getCbits()) + qubits);
        int height = lastRow - firstRow + 2;

        int argPosWidth = Math.max(Integer.toString(node.getQubits().length).length(),
                                   Integer.toString(node.getCbits().length).length()) + 2;

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
            if (i < qubits && contains(node.getQubits(), i)) {
                argPos = Integer.toString(indexOf(node.getQubits(), i));
                borderLeft = '┤';
                borderRight = '├';
            } else if (contains(node.getCbits(), i - qubits)) {
                argPos = Integer.toString(indexOf(node.getCbits(), i - qubits));
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

    private void dump(Cnot node) {
        int control = node.getControl(), target = node.getTarget();
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

    private void dump(Measure node) {
        //append("\n");
        //repeat("─\n", node.getSource());
        repeat("\n", node.getSource() + 1);
        append("M\n");
        repeat("║\n", qubits - node.getSource() - 1);
        repeat("║\n", node.getTarget());
        append("╚\n");
    }

    private void dumpSingleQubit(int qubit, String label) {
        repeat("\n", qubit + 1);
        append(label);
    }

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
