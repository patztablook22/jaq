package io.github.patztablook22.jaq;

import java.util.ArrayList;
import java.util.List;


/**
 * Generator of string diagram representations for 
 * {@link Qcircuit Qcircuits}. Intended to be used
 * internally by {@link Qcircuit#toString()}
 *
 * @see QnodeStringBuilder
 * @see Qcircuit#toString()
 *
 * */
class QcircuitStringBuilder {

    /**
     * {@link Qcircuit} the {@code QcircuitStringBuilder} is working with.
     *
     * */
    private Qcircuit circuit;

    /**
     * Generated vertical blocks.
     *
     * */
    private String[] verticalBlocks;

    /**
     * Line buffers.
     *
     * */
    private StringBuilder[] buffers;

    /**
     * Constructs {@code QcircuitStringBuilder} for given {@link Qcircuit}.
     *
     * @param circuit the Qcircuit to work with
     *
     * */
    public QcircuitStringBuilder(Qcircuit circuit) {
        this.circuit = circuit;
    }

    /**
     * Generates and dumps into {@code verticalBlocks} the initial
     * vertical block consisting mainly of all annotated qubits
     * and classical bits.
     *
     * */
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

    /**
     * Generates and dumps into {@code verticalBlocks} the vertical blocks
     * corresponding to the individual Qcircuit's nodes.
     *
     * */
    private void generateNodeBlocks() {
        for (int i = 0; i < circuit.length(); i++) {
            var builder = new QnodeStringBuilder(circuit.nodes().get(i),
                                                 circuit.qubits(),
                                                 circuit.cbits());
            verticalBlocks[i + 1] = builder.build();
        }
    }

    /**
     * Generates and dumps into {@code verticalBlocks} the terminal 
     * vertical block. 
     *
     * Essentially a padding making sure the formatting is right.
     *
     * */
    private void generateTerminalBlock() {
        var sb = new StringBuilder();
        for (int i = 0; i < 2 + circuit.qubits() + circuit.cbits(); i++)
            sb.append(" \n");
        verticalBlocks[verticalBlocks.length - 1] = sb.toString();
    }

    /**
     * Generates and dumps into {@code verticalBlocks} all veritcal blocks
     * for the given Qcircuit.
     *
     * */
    private void generateVerticalBlocks() {
        verticalBlocks = new String[2 + circuit.length()];
        generateInitialBlock();
        generateNodeBlocks();
        generateTerminalBlock();
    }

    /**
     * Adds given verticalBlock to the end of the currently concatenated
     * verticalBlocks in {@code buffers} in a compact way. Namely the
     * buffers are padded by the smalles amount necessary to guarantee
     * consistency of the non-empty lines of the block being added. 
     *
     * @param block block to add
     *
     * */
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

    /**
     * Concatenates corresponding lines of all {@code verticalBlocks}
     * in a compact way. Dumps the result incrementlly into {@code buffers}.
     *
     * */
    private void concatenateVerticalBlocks() {
        buffers = new StringBuilder[circuit.qubits() + circuit.cbits() + 2];
        for (int i = 0; i < buffers.length; i++) 
            buffers[i] = new StringBuilder();

        for (String block: verticalBlocks)
            addVerticalBlock(block);
    }

    /**
     * Invokes the String building process.
     *
     * @return the built String representation
     *
     * */
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
