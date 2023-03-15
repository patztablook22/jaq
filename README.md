
<p align="center">
  <img src="src/main/java/io/github/patztablook22/jaq/doc-files/jaq.png" label="Jaq">
</p>

## About

Jaq is a Quantum computing engine for Java based on a simple life cycle:

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

## Quantum circuits

Jaq provides `Qcircuit`, a convenient API for design, inspection, and composition of Quantum circuits.

A compact way of defining a quantum circuit is using anynomous subclassing and the initializer block, like in the example above:

```java
var circuit = new Qcircuit() {{

  /* superposition */
  hadamard(0);
       
  /* entanglement */
  cnot(0, 1);

  /* measurement */
  measure(0, 0);
  measure(1, 1);

}};
```
 
For a more fine control, `Qcircuit` can be simply inherited by a named subclass, which can e.g. automate the construction of the quantum circuit based on some parameters:

```java
class MyCircuit extends Qcircuit {
  public MyCircuit(int qubits) {

    for (int i = 0; i < qubits; i++)
      hadamard(i);

    /* ... */

  }
}
```

The circuits can be then run on any implementation of the Quantum virtual machine. The QVM is an abstraction of a quantum computer. The backend implementing that abstraction can be a custom quantum computer, a simulator, or a quantum computer availble over a public service.

Jaq is designed to be as modular as possible. New backends can be implemented by easily by hand. It is possible to build application-specific quantum computing libraries based on Jaq, utilizing the flexible `Qcircuit` API.

Especially when debugging, it is often useful to see the quantum circuit's diagram. This can be done simply by:

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

## QVM backends

Jaq quantum circuits are backend-agnostic. A `Qvm` implementation is expected to provide the full functionality of a quantum computer, constrained only by the resources available. Namely:

- Accepting circuits with any quantum nodes.
- Implicitly or explicitly inlining all nested subcircuits.
- Never modifying the given quantum circuits. All intermediate representations should be kept separately.
- Transpiling not directly supported (e.g. more complex) quantum operations into the backend's universal set.
- Optimizing the resulting flow graph.
- Caching it for efficient reuse.

### Implementations of `Qvm`:

- `SimpleSimulator` - simulates the quantum circuit using sparse linear algebra on the CPU
