package io.github.patztablook22.jaq.backends.lingebra;


/**
 * A helper class for representing a given {@link SparseOperator} as a string.
 *
 * */
class SparseOperatorStringBuilder {

    /**
     * The underlying {@link SparseOperator}
     *
     * */
    private SparseOperator operator;

    /**
     * The internal buffer.
     *
     * */
    private StringBuilder sb;

    /**
     * Current row in the matrix being rendered.
     *
     * */
    private int curRow;

    /**
     * Current column in the matrix being rendered.
     *
     * */
    private int curCol;

    /**
     * Column size used internally for padding.
     *
     * */
    private int colSize;

    /**
     * Constructs a {@code SparseOperatorStringBuilder} for given operator.
     *
     * @param operator operator to represent
     *
     * */
    public SparseOperatorStringBuilder(SparseOperator operator) {
        this.operator = operator;
    }

    /**
     * Appends the next cell value to the internal buffer, 
     * including additional formatting (padding, brackets, ...).
     *
     * @param s value to show in the cell
     *
     * */
    private void cell(String s) {
        if (curCol == 0 && curRow != 0)
            sb.append("[");

        sb.append(s);
        for (int pad = s.length(); pad < colSize; pad++)
            sb.append(' ');

        curCol++;

        if (curCol == operator.getDim()) {
            curCol = 0;
            curRow++;
            if (curRow < operator.getDim())
                sb.append("]\n ");
        }
    }

    /**
     * Appends the next cell value to the internal buffer, 
     * including additional formatting (padding, brackets, ...).
     *
     * @param real real component of the number to show in the cell
     * @param imag imaginary component of the number to show in the cell
     *
     * */
    private void cell(float real, float imag) {
        String temp;
        if (imag >= 0)
            temp = "" + real + "+" + imag + "j";
        else
            temp = "" + real + "-" + -imag + "j";
        cell(temp);
    }

    /**
     * Appends an empty cell value to the internal buffer, 
     * including additional formatting (padding, brackets, ...).
     *
     * */
    private void cell() {
        cell("");
    }

    /**
     * Appends empty cells to the internal buffer until it
     * reaches the specified row and column.
     *
     * @param row the target row
     * @param col the target column
     *
     * */
    private void emptyUntil(int row, int col) {
        while (curRow < row || curCol < col)
            cell();
    }

    /**
     * Sets up {@code colSize} used internally for padding.
     *
     * */
    void setColSize() {
        colSize = 2;
        for (int iter = 0; iter < operator.storedSize(); iter++) {
            float real = operator.storedReals[iter];
            float imag = operator.storedImags[iter];
            String temp;

            if (imag >= 0)
                temp = "" + real + "+" + imag + "j";
            else
                temp = "" + real + "-" + -imag + "j";

            colSize = Math.max(colSize, temp.length() + 1);
        }
    }

    /**
     * Builds and returns the string representation of the underlying
     * {@link SparseOperator}.
     *
     * @return the string representation of the underlying SparseOperator
     *
     * */
    public String build() {
        sb = new StringBuilder();
        curRow = 0;
        curCol = 0;
        setColSize();

        sb.append("[[");
        for (int iter = 0; iter < operator.storedSize(); iter++) {
            emptyUntil(operator.storedRows[iter], operator.storedCols[iter]);
            cell(operator.storedReals[iter], operator.storedImags[iter]);
        }

        emptyUntil(operator.getDim(), 0);
        sb.append("]]");

        return sb.toString();
    }
}
