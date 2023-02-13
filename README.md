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
    var circ = new Qcircuit() {{
      hadamard(0);
      cnot(0, 1);
      measure(1, 0);
    }};
    
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

Output for the circuit in the example code:

```
q0:  ─H─┬───
q1:  ───+─M─
c0:  ═════╚═
```

Or a more complex circuit, with some nested subcircuits: 

```
q0:  ─H───────────────────┌──────────┐─
q1:  ─H─Rx─┬─X────────────┤0  Inner2 ├─
q2:  ─H────┊─┬────────M─X─┤1         ├─
q3:  ─H────+─+────────║─X─└──────────┘─
q4:  ─┌─────────────┐─║────────────────
q5:  ─┤0  InnerA... ├─║────────────────
c0:  ═╡0            ╞═║════════════════
c1:  ═└─────────────┘═╚════════════════
```

## Quantum virtual machine

Jaq quantum circuits are backend-agnostic. Implementations of `Qvm`:

- `SimpleSimulator` - simulates the quantum circuit using linear algebra on the CPU
