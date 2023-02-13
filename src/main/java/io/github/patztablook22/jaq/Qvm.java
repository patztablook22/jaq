package io.github.patztablook22.jaq;


public abstract class Qvm {
    public byte[] run(Qcircuit circuit) {
        return null;
    }

    public byte[][] run(Qcircuit circuit, int shots) {
        return null;
    }

    protected Object getCache(Qcircuit circuit) {
        return null;
    }

    protected void storeCache(Qcircuit circuit, Object cache) {
    }
}
