package solver;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.stream.*;

public class Matrix {
    private final ArrayList<ArrayList<Complex>> matrix;
    private final int nRows;
    private final int nCols;
    private Boolean isSolvable;

    //Note: deep copies the source list
    public Matrix(ArrayList<ArrayList<Complex>> input) {
        matrix = input.stream().map(ArrayList::new).collect(Collectors.toCollection(ArrayList::new));
        nRows = matrix.size();
        nCols = matrix.get(0).size();
        isSolvable = null;
    }

    private void swapRows(int row1, int row2) throws IllegalArgumentException {
        validateRowInput(row1);
        validateRowInput(row2);
        var temp = matrix.get(row1);
        matrix.set(row1, matrix.get(row2));
        matrix.set(row2, temp);
    }

    private void scalarMultiplyRow(int row, Complex scalar) throws IllegalArgumentException {
        validateRowInput(row);
        matrix.set(row, matrix.get(row).stream()
                .map(d -> d.multiply(scalar))
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll));
    }

    private void addMultipleOfRowToRow(int rowTarget, int rowSource, Complex scalar) throws IllegalArgumentException {
        validateRowInput(rowSource);
        validateRowInput(rowTarget);
        matrix.set(rowTarget, IntStream.range(0, nCols)
                .mapToObj(i -> matrix.get(rowTarget).get(i).add(matrix.get(rowSource).get(i).multiply(scalar)))
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll));
    }

    //Gets the index of the first row (starting from rowStart) with a nonzero entry in col; returns -1 if none exists
    private int getFirstNonzeroEntryRowForCol(int rowStart, int col) throws IllegalArgumentException {
        validateRowInput(rowStart);
        validateColumnInput(col);
        for (int i = rowStart; i < nRows; ++i)
            if (!matrix.get(i).get(col).equals(Complex.ZERO))
                return i;
        return -1;
    }

    //Gets the index of row and col (starting from rowStart and colFrom)
    //with the leftmost nonzero leading value; if all equal just returns rowStart
    private int[] getLeftmostLeadingRow(int rowStart, int colFrom) throws IllegalArgumentException {
        validateRowInput(rowStart);
        validateColumnInput(colFrom);
        for (int i = colFrom; i < nCols; ++i) {
            int row = getFirstNonzeroEntryRowForCol(rowStart, i);
            if (row == -1)
                continue;
            else
                return new int[]{row, i} ;
        }
        return new int[]{rowStart, colFrom};
    }

    //Return true if the submatrix from rowFrom and colFrom (inclusive) is a zero matrix
    private boolean isZeroSubmatrix(int rowFrom, int colFrom) throws IllegalArgumentException {
        validateRowInput(rowFrom);
        validateColumnInput(colFrom);
        for (int i = rowFrom; i < nRows; ++i)
            for (int j = colFrom; j < nCols; ++j)
                if (!matrix.get(i).get(j).equals(Complex.ZERO))
                    return false;
        return true;
    }

    public void toReducedRowEchelonForm() {
        //First: convert to upper triangular matrix, storing leading values as we go
        ArrayList<int[]> leadingValues = new ArrayList<>();
        int leftMostCol = 0;
        for (int i = 0; i < nRows; ++i) {
            System.out.println("i: " + i);
            printMatrix();
            //If no more work to be done, break
            if (isZeroSubmatrix(i, leftMostCol))
                break;

            //Get the leftmost leading row up to row i
            int[] leadingVal = getLeftmostLeadingRow(i, leftMostCol);
            leftMostCol = leadingVal[1];
            if (leadingVal[0] != i) {
                swapRows(i, leadingVal[0]);
                leadingVal[0] = i;
            }
            leadingValues.add(leadingVal);

            //Zero out everything under the leading value
            for (int j = i + 1; j < nRows; ++j)
                if (!matrix.get(j).get(leftMostCol).equals(Complex.ZERO)) {
                    Complex val = matrix.get(j).get(leftMostCol);
                    addMultipleOfRowToRow(j, i, val.divide(matrix.get(i).get(leftMostCol)).scalarMultiply(-1));
                }

            //Done with this column, next one must be at least one to the right
            if (leftMostCol < nCols - 1)
                ++leftMostCol;
        }

        //Check to see if any no-solution state
        for (int i = 0; i < nRows; ++i) {
            final int row = i;
            if (!matrix.get(i).get(nCols - 1).equals(Complex.ZERO) && IntStream.range(0, nCols - 1).allMatch(j -> matrix.get(row).get(j).equals(Complex.ZERO))) {
                isSolvable = false;
                System.out.println("Zero row detected, no solution possible: aborting.");
                return;
            }
        }

        //Check to see if infinite or single solution
        int numNonzeroRows = matrix.stream().mapToInt(a -> a.stream().anyMatch(d -> !d.equals(Complex.ZERO)) ? 1 : 0).sum();;
        if (numNonzeroRows == nCols - 1) {
            //Only one solution
            System.out.println("Only one solution possible, finding it: ");
            isSolvable = true;
        } else {
            //Infinitely many solutions
            isSolvable = null;
            System.out.println("Infinitely many solutions possible, aborting");
            return;
        }

        //only single solution: find it

        //Second: clear out the values above the leading vals, starting from the right
        ListIterator<int[]> it = leadingValues.listIterator(leadingValues.size());
        System.out.println("Leading values: ");
        leadingValues.stream().forEach(o -> System.out.println(o[0] + " " + o[1]));
        while (it.hasPrevious()) {
            int[] coords = it.previous();
            for (int i = coords[0] - 1; i >= 0; --i)
                if (!matrix.get(i).get(coords[1]).equals(Complex.ZERO)) {
                    Complex val = matrix.get(i).get(coords[1]);
                    addMultipleOfRowToRow(i, coords[0], val.divide(matrix.get(coords[0]).get(coords[1])).scalarMultiply(-1));
                }
        }
        System.out.println("Cleared matrix: ");
        printMatrix();

        //Third: scalar multiply rows to set leading values to 1
        for (int[] coords : leadingValues)
            scalarMultiplyRow(coords[0], Complex.ONE.divide(matrix.get(coords[0]).get(coords[1])));
        System.out.println("Final matrix: ");
        printMatrix();
        System.out.println("END CALCULATION");
    }

    public void printMatrix() {
        matrix.forEach(a -> {a.forEach(d -> System.out.print(d + " ")); System.out.println("");});
    }

    public ArrayList<ArrayList<Complex>> getCopyOfMatrix() {
        return matrix.stream().map(ArrayList::new).collect(Collectors.toCollection(ArrayList::new));
    }

    public int getnRows() {
        return nRows;
    }

    public int getnCols() {
        return nCols;
    }

    public Boolean isSolvable() {
        return this.isSolvable;
    }

    private void validateRowInput(int rowIn) throws IllegalArgumentException {
        if (rowIn < 0 || rowIn >= nRows)
            throw new IllegalArgumentException("Invalid input: Row" + rowIn + " is out of bounds!");
    }

    private void validateColumnInput(int colIn) throws IllegalArgumentException {
        if (colIn < 0 || colIn >= nCols)
            throw new IllegalArgumentException("Invalid input: Column" + colIn + " is out of bounds!");
    }
}
