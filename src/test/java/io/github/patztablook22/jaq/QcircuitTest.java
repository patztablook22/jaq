package io.github.patztablook22.jaq;

import static org.junit.Assert.*;
import org.junit.Test;
import io.github.patztablook22.jaq.Qcircuit;
import io.github.patztablook22.jaq.backends.SimpleSimulator;


public class QcircuitTest {
     class UniformSuperposition extends Qcircuit {
         public UniformSuperposition(int qubits) {
             for (int i = 0; i < qubits; i++)
                 hadamard(i);
         }
     }
    @Test
    public void test() {
         var circuit = new Qcircuit() {{
             apply(new UniformSuperposition(5),
                   new int[] {3, 7, 2, 4, 8},
                   new int[] {}
             );
     
             measure(3, 0);
         }};
        System.out.println(circuit);

        Qvm backend = new SimpleSimulator();
        for (byte b: backend.run(circuit))
            System.out.print(b);
        System.out.println();
    }
}
