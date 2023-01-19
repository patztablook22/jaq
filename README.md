# Jaq

Quantum computing engine for Java based on a simple life cycle:

1. Build a quantum circuit.
2. Choose a QVM (quantum virtual machine) backend.
3. Run the circuit.


## Example

```java
class Program {
  public static void main(String[] args) {
    
    // Step 1. Build a quantum circuit.
    Qcircuit circ = new Qcircuit(() -> {
      var q1 = new Qubit();
      var q2 = new Qubit();
    
      q2.hadamard();
      q1.hadamard(q2).cnot();
      q1.measure();
    });
    
    // Step 2. Choose a QVM backend.
    Qvm backend = new SimpleSimulator();
    
    // Step 3. Run the circuit.
    byte[] measurements = backend.run(circ);
  
  }
}
```

## Visualization

Especially when debugging, it is often useful to see the quantum circuit's diagram. This can be done simply:

```java
Qcircuit circ = /* ... */;
System.out.println(circ);
```

Output:

```
0:   ─H──H──H───────────X───────────M─
1:   ─┴──┊──┊──H──H────────X────────┼─
2:   ────┴──┊──┴──┊──H────────X─────┼─
3:   ───────┴─────┴──┴───────────X──┴─
```

## Quantum virtual machine

Jaq quantum circuits are backend-agnostic. Implementations of `Qvm`:

- `SimpleSimulator` - simulates the quantum circuit using linear algebra on the CPU
