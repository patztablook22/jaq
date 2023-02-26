package io.github.patztablook22.jaq;

import java.util.ArrayList;
import java.util.List;


class QcircuitStringBuilder {
    Qcircuit circuit;
    String[] verticalBlocks;
    StringBuilder[] buffers;


    public QcircuitStringBuilder(Qcircuit circuit) {
        this.circuit = circuit;
    }

    private void generateInitialBlock() {
        var sb = new StringBuilder("\n");
        int idxPadding = Integer.toString(
                Math.max(circuit.qubits(), circuit.cbits())).length();

        for (int i = 0; i < circuit.qubits() + circuit.cbits(); i++) {
            int idx = i;
            char prefix = 'q';
            if (idx >= circuit.qubits()) {
                idx -= circuit.qubits();
                prefix = 'c';
            }
            String idxString = Integer.toString(idx);
            sb.append(prefix).append(idxString);
            for (int j = idxString.length(); j < idxPadding; j++)
                sb.append(' ');

            sb.append(":  \n");
        }
        verticalBlocks[0] = sb.toString();
    }

    private void generateNodeBlocks() {
        for (int i = 0; i < circuit.length(); i++) {
            var builder = new QnodeStringBuilder(circuit.nodes().get(i),
                                                 circuit.qubits(),
                                                 circuit.cbits());
            verticalBlocks[i + 1] = builder.build();
        }
    }

    private void generateTerminalBlock() {
        var sb = new StringBuilder();
        for (int i = 0; i < 2 + circuit.qubits() + circuit.cbits(); i++)
            sb.append(" \n");
        verticalBlocks[verticalBlocks.length - 1] = sb.toString();
    }

    private void generateVerticalBlocks() {
        verticalBlocks = new String[2 + circuit.length()];
        generateInitialBlock();
        generateNodeBlocks();
        generateTerminalBlock();
    }

    private void addVerticalBlock(String block) {
        String[] lines = block.split("\\n");
        int padding = 0;
        for (int line = 0; line < lines.length; line++) {
            if (lines[line].isEmpty()) continue;
            int len = buffers[line].length();
            padding = Math.max(padding, len == 0 ? len : len + 1);
        }

        for (int line = 0; line < lines.length; line++) {
            char paddingChar;

            if (line >= 1 && line < 1 + circuit.qubits())
                paddingChar = '─';
            else if (line >= 1 + circuit.qubits() && line < 1 + circuit.qubits() + circuit.cbits())
                paddingChar = '═';
            else
                paddingChar = ' ';

            if (lines[line].isEmpty()) continue;

            for (int i = buffers[line].length(); i < padding; i++)
                buffers[line].append(paddingChar);
            buffers[line].append(lines[line]);
        }
    }

    private void concatenateVerticalBlocks() {
        buffers = new StringBuilder[circuit.qubits() + circuit.cbits() + 2];
        for (int i = 0; i < buffers.length; i++) 
            buffers[i] = new StringBuilder();

        for (String block: verticalBlocks)
            addVerticalBlock(block);
    }

    public String build() {
        generateVerticalBlocks();
        concatenateVerticalBlocks();

        var sb = new StringBuilder();

        for (int i = 0; i < buffers.length; i++) {
            if (i == 0 || i == buffers.length - 1)
                if (buffers[i].toString().isBlank())
                    continue;

            sb.append(buffers[i]).append('\n');
        }

        return sb.toString();
    }
}
