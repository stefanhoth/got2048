package de.stefanhoth.android.got2048.logic.model;

import android.util.Log;

import java.util.HashSet;
import java.util.Random;

/**
 * TODO describe class
 *
 * @author Stefan Hoth <sh@jnamic.com>
 *         date: 20.03.14 20:28
 * @since TODO add version
 */
public class Grid {
    private static final String TAG = Grid.class.getSimpleName();
    protected static final int DEFAULT_GRID_SIZE = 4;
    protected static final int DEFAULT_EMPTY_VALUE = -1;

    private int[][] grid;
    private int gridSize;
    private Random randomGenerator;
    private HashSet<String> cellImmunities;

    public Grid() {
        this(DEFAULT_GRID_SIZE);
    }

    public Grid(int size) {
        this.gridSize = size;
        cellImmunities = new HashSet<>();

        this.grid = initGrid();
        Log.d(TAG, "Grid set up with grid size=" + gridSize);
    }

    private int[][] initGrid() {

        int[][] newGrid = new int[gridSize][gridSize];

        for (int rowNumber = 0; rowNumber < gridSize; rowNumber++) {
            for (int columnNumber = 0; columnNumber < gridSize; columnNumber++) {
                newGrid[rowNumber][columnNumber] = DEFAULT_EMPTY_VALUE;
            }
        }

        return newGrid;
    }

    public int getCellValue(int row, int column) {
        if (row >= gridSize || column >= gridSize) {
            throw new ArrayIndexOutOfBoundsException("row and column can't exceed grid size=" + gridSize + ". Values row=" + row + ", column=" + column);
        }

        return grid[row][column];
    }

    public int[] getRow(int row) {
        if (row >= gridSize) {
            throw new ArrayIndexOutOfBoundsException("row can't exceed grid size=" + gridSize + ". Value row=" + row);
        }

        return grid[row];
    }

    public int[] getColumn(int column) {
        if (column >= gridSize) {
            throw new ArrayIndexOutOfBoundsException("column can't exceed grid size=" + gridSize + ". Value column=" + column);
        }

        int[] columnValues = new int[gridSize];

        for (int row = 0; row < gridSize; row++) {
            columnValues[row] = getCellValue(row, column);
        }

        return columnValues;
    }

    public void setCellValue(int row, int column, int value) {
        if (row >= gridSize || column >= gridSize) {
            throw new ArrayIndexOutOfBoundsException("row and column can't exceed grid size=" + gridSize + ". Values row=" + row + ", column=" + column);
        }

        grid[row][column] = value;
    }

    public void resetCell(int row, int column) {
        Log.v(TAG, "resetCell: Resetting value and immunity of cell [" + row + "," + column + "]");
        setCellValue(row, column, DEFAULT_EMPTY_VALUE);
        cellImmunities.remove(row + "-" + column);
    }

    public void reset() {

        Log.d(TAG, "Resetting grid.");

        for (int row = 0; row < grid.length; row++) {
            for (int column = 0; column < grid[row].length; column++) {
                grid[row][column] = DEFAULT_EMPTY_VALUE;
            }
        }
    }

    public int getGridSize() {
        return gridSize;
    }

    public int[][] getGridStatus() {

        int length = grid.length;
        int[][] target = new int[length][grid[0].length];
        for (int i = 0; i < length; i++) {
            System.arraycopy(grid[i], 0, target[i], 0, grid[i].length);
        }

        return target;
    }

    public int getActiveCells() {
        int activeCells = 0;

        for (int[] cells : grid) {
            for (int cellValue : cells) {
                if (cellValue > DEFAULT_EMPTY_VALUE) {
                    activeCells++;
                }
            }
        }

        return activeCells;
    }

    public Cell getRandomCell() {

        randomGenerator = new Random();
        if (randomGenerator == null) {
            randomGenerator = new Random();
        }

        return new Cell(randomGenerator.nextInt(gridSize), randomGenerator.nextInt(gridSize));
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {

        StringBuilder builder = new StringBuilder();

        for (int row = 0; row < grid.length; row++) {
            for (int column = 0; column < grid[row].length; column++) {

                builder.append("|");

                if (grid[row].length > DEFAULT_EMPTY_VALUE) {
                    builder.append(String.format("%2s", grid[row][column]));
                } else {
                    builder.append("  ");
                }
            }
            builder
                    .append("|")
                    .append("\n");
        }

        return builder.toString();
    }

    public MovementChanges moveCells(MOVE_DIRECTION direction) {
        return moveCells(direction, false);
    }

    private MovementChanges moveCells(MOVE_DIRECTION direction, boolean simulate) {

        MovementChanges changes = new MovementChanges(getGridStatus());
        changes.cellsMoved = false;
        changes.pointsEarned = 0;

        int[][] restoreGrid = getGridStatus();

        // taking the easy route: always make the move from
        // left to right by turning the grid in this direction
        switch (direction) {
            case UP:
                Log.d(TAG, "moveCells: direction=" + direction + ", rotating grid by 90° clockwise");
                rotateGrid90(true);
                break;
            case DOWN:
                Log.d(TAG, "moveCells: direction=" + direction + ", rotating grid by 90° counter-clockwise");
                rotateGrid90(false);
                break;
            case LEFT:
                Log.d(TAG, "moveCells: direction=" + direction + ", rotating grid by 180°");
                rotateGrid180();
                break;
            case RIGHT:
                // nothing necessary
                break;
        }

        Log.d(TAG, "moveCells: Direction adjustment done.");

        int currentCellValue, currentColumn, rightNeighborColumn, addedPoints;
        for (int row = 0; row < grid.length; row++) {
            for (int column = grid[row].length - 2; column >= 0; column--) { //start at the end of the row but one next to it (last position can't move anymore)

                currentCellValue = getCellValue(row, column);

                Log.d(TAG, "moveCells: Checking move for cell [" + row + "," + column + "]=" + currentCellValue);
                if (currentCellValue == DEFAULT_EMPTY_VALUE) {
                    //no value, nothing to move nor merge
                    Log.v(TAG, "moveCells: Cell [" + row + "," + column + "] is empty, nothing to move.");
                    continue;
                } else if (column + 1 == grid[row].length) {
                    Log.v(TAG, "moveCells: Cell [" + row + "," + column + "] is already the righter most column, nothing to move.");
                    continue;
                }

                currentColumn = column;
                rightNeighborColumn = column + 1;

                //move the cell as far to the right (along the row) as possible
                while (rightNeighborColumn < grid[row].length && !cellHasValue(row, rightNeighborColumn)) {

                    Log.v(TAG, "moveCells: Moving cell [" + row + "," + currentColumn + "] with value=" + currentCellValue + " to cell [" + row + "," + rightNeighborColumn + "]");
                    setCellValue(row, rightNeighborColumn, currentCellValue);
                    if (isCellImmune(row, currentColumn)) {
                        Log.v(TAG, "moveCells: Keeping immunity of cell [" + row + "," + currentColumn + "] intact by assigning it to cell [" + row + "," + rightNeighborColumn + "]");
                        setCellImmune(row, rightNeighborColumn);
                    }

                    Log.v(TAG, "moveCells: Clearing value of cell [" + row + "," + column + "]");
                    resetCell(row, currentColumn);
                    changes.cellsMoved = true;

                    if (rightNeighborColumn + 1 == getRow(row).length) {
                        Log.v(TAG, "moveCells: Row is completely moved, cell [" + row + "," + rightNeighborColumn + "] was last.");
                        break;
                    }

                    currentColumn++;
                    rightNeighborColumn++;
                }

                if (canCellsMerge(row, currentColumn, row, rightNeighborColumn)) {

                    Log.v(TAG, "moveCells: Cell [" + row + "," + currentColumn + "]=" + getCellValue(row, currentColumn) + " and cell [" + row + "," + rightNeighborColumn + "]=" + getCellValue(row, rightNeighborColumn) + " can and will be merged");
                    addedPoints = getCellValue(row, currentColumn) + getCellValue(row, rightNeighborColumn);
                    setCellValue(row, rightNeighborColumn, addedPoints);
                    resetCell(row, currentColumn);
                    setCellImmune(row, rightNeighborColumn);
                    changes.cellsMoved = true;
                    changes.pointsEarned += addedPoints;

                } else {
                    Log.v(TAG, "moveCells: Cell [" + row + "," + currentColumn + "]=" + getCellValue(row, currentColumn) + " and cell [" + row + "," + rightNeighborColumn + "]=" + getCellValue(row, rightNeighborColumn) + " can't be merged");
                }

                //no movement, no merging = do nothing and move on
            }
            Log.v(TAG, "moveCells: Movement and merging for row=" + row + " done.");
        }

        Log.v(TAG, "moveCells: Movement done.");

        //all moves done, reset immunity for all
        resetCellImmunities();

        //don't forget to turn them back
        switch (direction) {
            case UP:
                Log.d(TAG, "moveCells: direction=" + direction + ", rotating grid back by 90° counter-clockwise");
                rotateGrid90(false);
                break;
            case DOWN:
                Log.d(TAG, "moveCells: direction=" + direction + ", rotating grid back by 90° clockwise");
                rotateGrid90(true);
                break;
            case LEFT:
                Log.d(TAG, "moveCells: direction=" + direction + ", rotating grid back by 180°");
                rotateGrid180();
                break;
            case RIGHT:
                // nothing necessary
                break;
        }

        if (simulate) {
            grid = restoreGrid;
        }

        changes.gridStatus = getGridStatus();

        Log.d(TAG, "moveCells: Direction adjustment reverted. Move done.");

        return changes;
    }

    protected void rotateGrid90(boolean clockwise) {

        transposeGrid();
        if (clockwise) {
            reverseEachRow();
        } else {
            reverseEachColumn();
        }
    }

    protected void transposeGrid() {

        int[][] transposedGrid = initGrid();

        for (int row = 0; row < grid.length; row++) {
            for (int column = 0; column < getRow(row).length; column++) {
                Log.v(TAG, "transposeGrid: Assigning cell [" + column + "," + row + "] = cell[" + row + "," + column + "]");
                transposedGrid[column][row] = getCellValue(row, column);
            }
        }

        this.grid = transposedGrid;
    }

    protected void rotateGrid180() {
        reverseEachRow();
        reverseEachColumn();
    }

    protected void reverseEachRow() {

        for (int row = 0; row < grid.length; row++) {

            Log.v(TAG, "reverseEachRow: Reversing row=" + row);

            //solution from http://stackoverflow.com/a/3523066/409349
            int left = 0;
            int right = grid[row].length - 1;

            while (left < right) {
                // swap the values at the left and right indices
                int temp = grid[row][left];
                grid[row][left] = grid[row][right];
                grid[row][right] = temp;

                // move the left and right index pointers in toward the center
                left++;
                right--;
            }
        }
    }

    protected void reverseEachColumn() {

        for (int column = 0; column < grid[0].length; column++) {

            Log.v(TAG, "reverseEachColumn: Reversing column=" + column);

            //solution from http://stackoverflow.com/a/3523066/409349
            int up = 0;
            int down = grid[0].length - 1;

            while (up < down) {
                // swap the values at the up and down indices
                int temp = grid[up][column];
                grid[up][column] = grid[down][column];
                grid[down][column] = temp;

                // move the up and down index pointers in toward the center
                up++;
                down--;
            }
        }
    }

    private void resetCellImmunities() {
        Log.v(TAG, "resetCellImmunities: Clearing list of immune cells");
        cellImmunities.clear();
    }

    private boolean canCellsMerge(int currentCellRow, int currentCellColumn, int rightNeighborCellRow, int rightNeighborCellColumn) {

        if (getCellValue(currentCellRow, currentCellColumn) == DEFAULT_EMPTY_VALUE ||
                getCellValue(rightNeighborCellRow, rightNeighborCellColumn) == DEFAULT_EMPTY_VALUE ||
                isCellImmune(currentCellRow, currentCellColumn) ||
                isCellImmune(rightNeighborCellRow, rightNeighborCellColumn)
                ) {

            return false;
        }

        return getCellValue(currentCellRow, currentCellColumn) == getCellValue(rightNeighborCellRow, rightNeighborCellColumn);
    }

    private void setCellImmune(int row, int column) {
        Log.v(TAG, "setCellImmune: Adding immunity to cell [" + row + "," + column + "]");
        cellImmunities.add(row + "-" + column);
    }

    boolean isCellImmune(int row, int column) {
        Log.v(TAG, "isCellImmune: Cell [" + row + "," + column + "]=" + cellImmunities.contains(row + "-" + column));
        return cellImmunities.contains(row + "-" + column);
    }

    public boolean cellHasValue(int row, int column) {
        return getCellValue(row, column) > DEFAULT_EMPTY_VALUE;
    }

    public boolean wouldMoveCells(MOVE_DIRECTION direction) {

        return moveCells(direction, true).cellsMoved;
    }

    public boolean isGameOver() {

        for (MOVE_DIRECTION direction : MOVE_DIRECTION.values()) {
            if (wouldMoveCells(direction)) {
                return false;
            }
        }

        //if we can't move anymore, we have lost
        return true;
    }

    public boolean isGameWon(int wonThreshold) {

        for (int row = 0; row < grid.length; row++) {
            for (int column = 0; column < getRow(row).length; column++) {
                if (grid[row][column] >= wonThreshold) {
                    return true;
                }
            }
        }

        return false;
    }
}
