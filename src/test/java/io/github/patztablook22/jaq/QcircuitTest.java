package io.github.patztablook22.jaq;

import static org.junit.Assert.*;
import org.junit.Test;
import io.github.patztablook22.jaq.Qcircuit;
import io.github.patztablook22.jaq.backends.SimpleSimulator;


public class QcircuitTest {

    class MyCirc extends Qcircuit {
        class Inner2 extends Qcircuit {
            Inner2() {
            }
        }

        class InnerAsdf1 extends Qcircuit {
            InnerAsdf1() {
            }
        }

        MyCirc() {
            hadamard(0, 1, 2);
            cnot(0, 1);

            hadamard(0);
            cnot(1, 2);
        }
    }

    @Test
    public void test() {
        MyCirc circ = new MyCirc();
        System.out.println(circ);

        Qvm backend = new SimpleSimulator();
        backend.run(circ);
    }
}
