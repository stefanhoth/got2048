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
    private static final int GRID_SIZE = 4;
    private static final String TAG = Grid.class.getSimpleName();

    private ArrayList<ArrayList<Cell>> grid;

    public Grid() {

        this.grid = new ArrayList<>(GRID_SIZE);
        ArrayList<Cell> row;
        for (int i = 0; i < GRID_SIZE; i++) {
            row = new ArrayList<>(GRID_SIZE);

            for (int j = 0; j < GRID_SIZE; j++) {
                row.add(j, new Cell());
            }

            this.grid.add(i, row);
        }
        Log.d(TAG, "Grid set up with GRID_SIZE="+GRID_SIZE);
    }

    public Cell getCell(int row, int column){
        if(row >= GRID_SIZE || column >= GRID_SIZE){
            throw new ArrayIndexOutOfBoundsException("row and column can't exceed GRID_SIZE="+GRID_SIZE+". Values row="+row+", column="+column);
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
}
