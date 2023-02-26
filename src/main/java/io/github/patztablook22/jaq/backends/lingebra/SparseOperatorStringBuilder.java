package io.github.patztablook22.jaq.backends.lingebra;


class SparseOperatorStringBuilder {
    private SparseOperator operator;
    private StringBuilder sb;
    private int curRow, curCol;
    private int colSize;

    public SparseOperatorStringBuilder(SparseOperator operator) {
        this.operator = operator;
    }

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

    private void cell(float real, float imag) {
        String temp;
        if (imag >= 0)
            temp = "" + real + "+" + imag + "j";
        else
            temp = "" + real + "-" + -imag + "j";
        cell(temp);
    }

    private void cell() {
        cell("");
    }

    private void padUntil(int row, int col) {
        while (curRow < row || curCol < col)
            cell();
    }

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

    public String build() {
        sb = new StringBuilder();
        curRow = 0;
        curCol = 0;
        setColSize();

        sb.append("[[");
        for (int iter = 0; iter < operator.storedSize(); iter++) {
            padUntil(operator.storedRows[iter], operator.storedCols[iter]);
            cell(operator.storedReals[iter], operator.storedImags[iter]);
        }

        padUntil(operator.getDim(), 0);
        sb.append("]]");

        return sb.toString();
    }
}
