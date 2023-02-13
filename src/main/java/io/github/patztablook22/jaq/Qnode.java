package io.github.patztablook22.jaq;

import java.util.List;


public interface Qnode {
    default List<Integer> qubits() {
        return null;
    }

    default List<Integer> cbits() {
        return null;
    }

    default List<Qnode> nodes() {
        return null;
    }
}
