package io.github.patztablook22.jaq;

import static org.junit.Assert.*;

import org.junit.Test;
import io.github.patztablook22.jaq.Qcircuit;
import io.github.patztablook22.jaq.Qubit;
import io.github.patztablook22.jaq.Qvm;
import io.github.patztablook22.jaq.SimpleSimulator;


public class QvmTest {
    class TestCircuit extends Qcircuit {
        @Override
        protected void build() {
            var q1 = new Qubit();
            var q2 = new Qubit();
            q1.hadamard().cnot(q2);
            q1.measure();
            q2.measure();
        }
    }

    @Test
    public void simpleSimulator() {
        Qvm backend = new SimpleSimulator();
        Qcircuit circ = new TestCircuit();

        System.out.println(circ);
        backend.run(circ);
    }
}
