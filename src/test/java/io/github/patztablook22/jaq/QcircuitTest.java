package io.github.patztablook22.jaq;

import static org.junit.Assert.*;
import org.junit.Test;
import io.github.patztablook22.jaq.Qcircuit;
import io.github.patztablook22.jaq.backends.SimpleSimulator;


public class QcircuitTest {
    @Test
    public void test() {
        var circ = new Qcircuit() {{
            hadamard(0);
            cnot(0, 1);
            measure(0, 0);
            measure(1, 1);
        }};
        System.out.println(circ);

        Qvm backend = new SimpleSimulator();
        byte[][] results = backend.run(circ, 10);

        for (int i = 0; i < results.length; i++) {
            for (int j = 0; j < results[i].length; j++)
                System.out.print(results[i][j]);
            System.out.println();
        }
    }
}
