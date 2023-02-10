package io.github.patztablook22.jaq;

import static org.junit.Assert.*;

import org.junit.Test;
import io.github.patztablook22.jaq.Qcircuit;
import io.github.patztablook22.jaq.Qubit;


public class QcircuitTest {

    class TestCircuit extends Qcircuit {
        @Override
        protected void build() {
            double phi = 3.1314;
            Qubit[] qs = new Qubit[4];
            for (int i = 0; i < qs.length; i++)
                qs[i] = new Qubit();

            for (var q: qs)
                q.phase(phi);
        }
    }

    @Test
    public void subclassingApi() {
        var circ = new TestCircuit();
        //System.out.println(circ);
    }

    @Test
    public void functionalApi() {

        var circ = new Qcircuit(() -> {
            Qubit[] qs = new Qubit[6];
            for (int i = 0; i < qs.length; i++)
                qs[i] = new Qubit();

            for (int i = 0; i < qs.length; i++)
                for (int j = i + 1; j < qs.length; j++)
                    qs[i].hadamard();

            for (var q: qs)
                q.measure();
        });


        //System.out.println(circ);
    }
}
