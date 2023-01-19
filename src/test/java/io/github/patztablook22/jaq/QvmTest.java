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
            Qubit[] qs = new Qubit[4];
            for (int i = 0; i < qs.length; i++)
                qs[i] = new Qubit();

            for (int i = 0; i < qs.length; i++)
                for (int j = i + 1; j < qs.length; j++)
                    qs[i].hadamard(qs[j]);
            
            for (var q: qs)
                q.pauliX();

            Qubit.measureJoint(qs);
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
