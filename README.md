# JAQ

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
