package de.stefanhoth.android.got2048.logic.model;

import android.util.Log;

import java.util.ArrayList;

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

    public Grid() {
        this(DEFAULT_GRID_SIZE);
    }

    public Grid(int size) {
        this.gridSize = size;

        this.grid = new ArrayList<>(gridSize);
        ArrayList<Cell> row;
        for (int i = 0; i < gridSize; i++) {
            row = new ArrayList<>(gridSize);

            for (int j = 0; j < gridSize; j++) {
                row.add(j, new Cell());
            }

            this.grid.add(i, row);
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
}
