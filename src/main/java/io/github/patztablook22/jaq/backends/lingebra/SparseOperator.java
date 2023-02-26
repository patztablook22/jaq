package io.github.patztablook22.jaq.backends.lingebra;

import java.util.Arrays;


/**
 * Immutable linear operator on a finite-dimensional Hilbert space 
 * \( \mathbf H \rightarrow \mathbf H \), represented internally
 * as a sparse matrix. As it is limited to endomorphisms, the matrix is 
 * necessarily square; both the number of rows and columns are equal to
 * the dimension of the Hilbert space.
 *
 * */
public class SparseOperator {
    int dim;
    int[] storedRows, storedCols;
    float[] storedReals, storedImags;

    /**
     * Constructs an operator from a flattened dense matrix representation.
     * One argument can be null. Otherwise they must have the same size.
     * The size must be a square of an integer.
     * The dimension of the Hilbert space is deduced from the size.
     *
     * @param real flattened array of real values
     * @param imag flattened array of imaginary values
     *
     * */
    public SparseOperator(float[] real, float[] imag) {
        if (real == null && imag == null)
            throw new IllegalArgumentException();
        else if (real == null)
            real = new float[imag.length];
        else if (imag == null)
            imag = new float[real.length];

        dim = (int) Math.sqrt(real.length);
        int size = dim * dim;
        if (real.length != size || imag.length != size)
            throw new IllegalArgumentException();

        int nonzero = 0;
        for (int i = 0; i < size; i++)
            if (real[i] != 0 || imag[i] != 0)
                nonzero++;

        storedRows = new int[nonzero];
        storedCols = new int[nonzero];
        storedReals = new float[nonzero];
        storedImags = new float[nonzero];

        int iter = 0;
        for (int i = 0; i < size; i++) {
            if (real[i] == 0 && imag[i] == 0)
                continue;

            storedRows[iter] = i / dim;
            storedCols[iter] = i % dim;
            storedReals[iter] = real[i];
            storedImags[iter] = imag[i];
            iter++;
        }
    }

    private SparseOperator() {
    }

    /**
     * Constructs the zero operator.
     * \(
     *     \ket{x} \mapsto 0
     * \)
     *
     * @param dim dimension of the Hilbert space
     * @return the resulting operator
     *
     * */
    public static SparseOperator zeros(int dim) {
        var out = new SparseOperator();
        out.dim = dim;

        out.storedRows = new int[0];
        out.storedCols = out.storedRows;
        out.storedReals = new float[0];
        out.storedImags = out.storedReals;
        return out;
    }

    /**
     * Constructs the identity operator
     * \(
     *      \ket{x} \mapsto \ket{x}
     * \)
     *
     * @param dim dimension of the Hilbert space
     * @return the resulting operator
     *
     * */
    public static SparseOperator eye(int dim) {
        var out = new SparseOperator();
        out.dim = dim;

        out.storedRows = new int[dim];
        for (int i = 0; i < dim; i++)
            out.storedRows[i] = i;
        out.storedCols = out.storedRows;

        out.storedReals = new float[dim];
        out.storedImags = new float[dim];
        Arrays.fill(out.storedReals, 1);
        return out;
    }

    /**
     * Constructs the reversed permutation operator
     * \( 
     *      \ket{x} \mapsto \ket{(x_{dim - k - 1})_k}
     * \)
     *
     * @param dim dimension of the Hilbert space
     * @return the resulting operator
     *
     * */
    public static SparseOperator yey(int dim) {
        var out = new SparseOperator();
        out.dim = dim;

        out.storedRows = new int[dim];
        out.storedCols = new int[dim];
        for (int i = 0; i < dim; i++) {
            out.storedRows[i] = i;
            out.storedCols[i] = dim - i - 1;
        }

        out.storedReals = new float[dim];
        out.storedImags = new float[dim];
        Arrays.fill(out.storedReals, 1);
        return out;
    }

    /**
     * Constructs the operator
     * \(
     *      \ket{a} \bra{b}
     * \)
     *
     * Where a and b are the indices of the computational basis vectors.
     *
     * @param ket ket basis index
     * @param bra bra basis index
     * @return the resulting operator
     *
     * */
    public static SparseOperator basisKetbra(int ket, int bra) {
        var out = new SparseOperator();
        out.dim = 2;
        out.storedRows = new int[1];
        out.storedCols = new int[1];
        out.storedReals = new float[1];
        out.storedImags = new float[1];

        out.storedRows[0] = ket;
        out.storedCols[0] = bra;
        out.storedReals[0] = 1;

        return out;
    }

    /**
     * Returns the dimension of the Hilbert space
     * \(
     *      \mathop{dim} \mathbf H
     * \)
     *
     * @return the dimension of the Hilbert space
     *
     * */
    public int getDim() {
        return dim;
    }

    /**
     * Returns the kronecker product of the two operators.
     * The resulting operator corresponds to
     * \(
     *     (P \otimes Q) \ket{x}
     * \)
     *
     * @param other the second cronecker product operand
     * @return the resulting operator
     *
     * */
    public SparseOperator kronecker(SparseOperator other) {
        var out = new SparseOperator();
        out.dim = getDim() * other.getDim();

        int outStoredSize = storedSize() * other.storedSize();
        out.storedRows = new int[outStoredSize];
        out.storedCols = new int[outStoredSize];
        out.storedReals = new float[outStoredSize];
        out.storedImags = new float[outStoredSize];

        int target = 0;
        for (int iter = 0; iter < storedSize(); iter++) {
            for (int jter = 0; jter < other.storedSize(); jter++) {
                int outRow = storedRows[iter] * other.getDim()
                    + other.storedRows[jter];

                int outCol = storedCols[iter] * other.getDim()
                    + other.storedCols[jter];

                /* complex multiplication:
                 * (a + bi) * (c + di) = (ac - bd) + (ad + bc)i
                 */
                float outReal = storedReals[iter] * other.storedReals[jter]
                    - storedImags[iter] * other.storedImags[jter];

                float outImag = storedReals[iter] * other.storedImags[jter]
                    + storedImags[iter] * other.storedReals[jter];

                out.storedRows[target] = outRow;
                out.storedCols[target] = outCol;
                out.storedReals[target] = outReal;
                out.storedImags[target] = outImag;

                target++;
            }
        }

        return out;
    }

    /**
     * Adds two operators together by adding the underlying matrices.
     * The resulting operator corresponds to
     * \(
     *      \ket{x} \mapsto (P + Q) \ket{x}
     * \)
     *
     * where P is `this` and Q is `other`.
     *
     * @param other the second addition operand
     * @return the resulting operator
     *
     * */
    public SparseOperator add(SparseOperator other) {
        if (other.getDim() != getDim())
            throw new IllegalArgumentException("dimension mismatch: "
                    + getDim() + " vs " + other.getDim());

        var out = new SparseOperator();
        out.dim = getDim();

        int iter = 0, jter = 0;
        int outStoredSize = 0;
        int inf = Integer.MAX_VALUE;
        while (iter < storedSize() || jter < other.storedSize()) {
            int irow = iter < storedSize() ? storedRows[iter] : inf;
            int icol = iter < storedSize() ? storedCols[iter] : inf;
            int jrow = jter < other.storedSize() ? other.storedRows[jter] : inf;
            int jcol = jter < other.storedSize() ? other.storedCols[jter] : inf;

            if (irow == jrow && icol == jcol) {
                iter++;
                jter++;
            } else if (irow < jrow || (irow == jrow && icol < jcol)) {
                iter++;
            } else {
                jter++;
            }
            outStoredSize++;
        }

        out.storedRows = new int[outStoredSize];
        out.storedCols = new int[outStoredSize];
        out.storedReals = new float[outStoredSize];
        out.storedImags = new float[outStoredSize];

        iter = 0; 
        jter = 0;
        int kter = 0;
        while (iter < storedSize() || jter < other.storedSize()) {
            int irow = iter < storedSize() ? storedRows[iter] : inf;
            int icol = iter < storedSize() ? storedCols[iter] : inf;
            int jrow = jter < other.storedSize() ? other.storedRows[jter] : inf;
            int jcol = jter < other.storedSize() ? other.storedCols[jter] : inf;

            int krow = irow, kcol = icol;
            float real, imag;

            if (irow == jrow && icol == jcol) {
                real = storedReals[iter] + other.storedReals[jter];
                imag = storedImags[iter] + other.storedImags[jter];

                iter++;
                jter++;
            } else if (irow < jrow || (irow == jrow && icol < jcol)) {
                real = storedReals[iter];
                imag = storedImags[iter];

                iter++;
            } else {
                krow = jrow;
                kcol = jcol;
                real = other.storedReals[jter];
                imag = other.storedImags[jter];

                jter++;
            }

            out.storedRows[kter] = krow;
            out.storedCols[kter] = kcol;
            out.storedReals[kter] = real;
            out.storedImags[kter] = imag;

            kter++;
        }

        return out;
    }

    /**
     * Returns the underlying sparse matrix storage size.
     * Is roughly equal to the number of zero elements in the matrix.
     * Note that the storage can degenerate, i.e. zero entries will
     * be actually stored, e.g. by adding together opposite matrices.
     *
     * @return the underlying sparse matrix storage size
     *
     * */
    public int storedSize() {
        return storedReals.length;
    }

    /**
     * Applies the operator on the given ket vector:
     * \(
     *      \ket{x} \mapsto P \ket{x}
     * \)
     *
     * @param ket complex vector to transform
     * @return transformed complex vector
     *
     * */
    public Ket transform(Ket ket) {
        if (ket.getDim() != getDim())
            throw new IllegalArgumentException("dimension mismatch: "
                    + getDim() + " vs " + ket.getDim());

        Ket out = new Ket(getDim());

        float[] inReal = ket.getReal();
        float[] inImag = ket.getImag();
        float[] outReal = out.getReal();
        float[] outImag = out.getImag();

        for (int iter = 0; iter < storedSize(); iter++) {
            int row = storedRows[iter];
            int col = storedCols[iter];

            /* complex multiplication:
             * (a + bi) * (c + di) = (ac - bd) + (ad + bc)i
             */

            outReal[row] += storedReals[iter] * inReal[col]
                - storedImags[iter] * inImag[col];
            outImag[row] += storedReals[iter] * inImag[col]
                + storedImags[iter] * inReal[col];
        }
        return out;
    }

    /**
     * Returns a string representation of the underlying sparse complex matrix.
     *
     * @return string representation of the underlying matrix
     *
     * */
    @Override
    public String toString() {
        var builder = new SparseOperatorStringBuilder(this);
        return builder.build();
    }

}
