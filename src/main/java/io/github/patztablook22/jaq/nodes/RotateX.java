package io.github.patztablook22.jaq.nodes;

import io.github.patztablook22.jaq.Qgate;


public class RotateX implements Qgate {
    private int qubit;
    private double angle;

    public RotateX(int qubit, double angle) {
        this.qubit = qubit;
        this.angle = angle;
    }

    public int getQubit() {
        return qubit;
    }

    public double getAngle() {
        return angle;
    }
}
