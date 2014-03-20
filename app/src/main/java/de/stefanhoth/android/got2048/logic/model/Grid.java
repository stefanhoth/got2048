package de.stefanhoth.android.got2048.logic.model;

import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

/**
 * TODO describe class
 *
 * @author Stefan Hoth <sh@jnamic.com>
 *         date: 20.03.14 20:28
 * @since TODO add version
 */
public class Grid {
    protected static final int DEFAULT_GRID_SIZE = 4;
    private static final String TAG = Grid.class.getSimpleName();

    private ArrayList<ArrayList<Cell>> grid;
    private int gridSize;
    private Random randomGenerator;

    public Grid() {
        this(DEFAULT_GRID_SIZE);
    }

    public Grid(int size) {
        this.gridSize = size;

        this.grid = new ArrayList<>(gridSize);
        ArrayList<Cell> row;
        for (int rowNumber = 0; rowNumber < gridSize; rowNumber++) {
            row = new ArrayList<>(gridSize);

            for (int columnNumber = 0; columnNumber < gridSize; columnNumber++) {
                row.add(columnNumber, new Cell(rowNumber, columnNumber));
            }

            this.grid.add(rowNumber, row);
        }
        Log.d(TAG, "Grid set up with grid size=" + gridSize);
    }

    public Cell getCell(int row, int column){
        if (row >= gridSize || column >= gridSize) {
            throw new ArrayIndexOutOfBoundsException("row and column can't exceed grid size=" + gridSize + ". Values row=" + row + ", column=" + column);
        }

        return grid.get(row).get(column);
    }

    public void reset(){

        Log.d(TAG, "Resetting grid.");

        for (ArrayList<Cell> cells : grid) {
            for (Cell cell : cells) {
                cell.emptyField();
            }
        }
    }

    protected int getGridSize() {
        return gridSize;
    }

    protected ArrayList<ArrayList<Cell>> getGrid() {
        return grid;
    }

    public int getActiveCells() {
        int activeCells = 0;

        for (ArrayList<Cell> cells : grid) {
            for (Cell cell : cells) {
                if (cell.hasValue()) {
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

        return getCell(randomGenerator.nextInt(gridSize), randomGenerator.nextInt(gridSize));
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
        return super.toString();
    }
}
