package io.github.patztablook22.jaq.backends.lingebra;

import java.util.Iterator;
import java.util.Arrays;


/**
 * Mutable column vector in a finite-dimensional Hilbert space \( \mathbf H \).
 *
 * Stored as a pair of dense arrays representing the real and imaginary
 * coordinate components.
 *
 * */
public class Ket {

    /**
     * The real coordinate components of the vector.
     *
     * */
    private float[] real;

    /**
     * The imaginary coordinate components of the vector.
     *
     * */
    private float[] imag;

    /**
     * Constructs a ket initialized to the zero vector \( 0 \).
     *
     * @param dim dimension of the Hilbert space
     *
     * */
    public Ket(int dim) {
        this.real = new float[dim];
        this.imag = new float[dim];
    }

    /**
     * Constructs a ket from given dense coordinate arrays.
     *
     * One argument can be null. Otherwse they must have the same size.
     * The dimension of the Hlbert space is deduced from the size.
     *
     * @param real real values
     * @param imag imaginary values
     *
     * */
    public Ket(float[] real, float[] imag) {
        this.real = real;
        this.imag = imag;

        if (this.real == null && this.imag == null)
            throw new IllegalArgumentException();

        if (this.real == null)
            this.real = new float[this.imag.length];

        if (this.imag == null)
            this.imag = new float[this.real.length];

        if (this.real.length != this.imag.length)
            throw new IllegalArgumentException();
    }

    /**
     * Resets the ket <i>inplace</i> to the zero vector \( 0 \).
     *
     * All coordinates are set to 0+0j:
     * <p>\(
     *      x_k \leftarrow 0
     * \)</p>
     *
     * */
    public void zero() {
        Arrays.fill(real, 0);
        Arrays.fill(imag, 0);
    }

    /**
     * Returns the dimension of the Hilbert space
     *
     * <p>\(
     *      \mathop{dim} \mathbf H
     * \)</p>
     *
     * @return the dimension of the Hilbert space
     *
     * */
    public int getDim() {
        return real.length;
    }

    /**
     * Returns a reference to the internal buffer
     * storing the real coordinate components.
     *
     * @return internal real values buffer
     *
     * */
    public float[] getReal() {
        return real;
    }

    /**
     * Returns a reference to the internal buffer
     * storing the imaginary coordinate components.
     *
     * @return internal imaginary values buffer
     *
     * */
    public float[] getImag() {
        return imag;
    }

    /**
     * Normalizes the ket <i>inplace</i>.
     *
     * Divides all coordinates by the Euclidean norm:
     * <p>\(
     *      x_k \leftarrow \frac{x_k}{||x||}
     * \)</p>
     *
     *
     * */
    public void normalize() {
        float sum = 0;
        for (int i = 0; i < getDim(); i++)
            sum += real[i] * real[i] + imag[i] * imag[i];

        float norm = (float) Math.sqrt(sum);
        for (int i = 0; i < getDim(); i++) {
            real[i] /= norm;
            imag[i] /= norm;
        }
    }
}
