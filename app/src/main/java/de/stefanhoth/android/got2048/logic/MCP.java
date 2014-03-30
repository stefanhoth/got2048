package de.stefanhoth.android.got2048.logic;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import de.stefanhoth.android.got2048.logic.model.Cell;
import de.stefanhoth.android.got2048.logic.model.Grid;
import de.stefanhoth.android.got2048.logic.model.MOVE_DIRECTION;

/**
 * TODO describe class
 *
 * @author Stefan Hoth <sh@jnamic.com>
 *         date: 20.03.14 20:40
 * @since TODO add version
 */
public class MCP {

    private static final String TAG = MCP.class.getName();
    protected static final int DEFAULT_START_FIELDS = 2;
    protected static final int DEFAULT_START_VALUE = 2;
    private Grid playlingField;
    private boolean gameStopped;

    private List<GridUpdateListener> gridUpdateListeners;

    public MCP() {
        playlingField = new Grid();
        gridUpdateListeners = new ArrayList<>();
        gameStopped = false;
    }

    public MCP(int gridSize) {
        playlingField = new Grid(gridSize);
        gridUpdateListeners = new ArrayList<>();
        gameStopped = false;
    }

    protected Grid getPlaylingField() {
        return playlingField;
    }

    public void addStartCells() {

        Cell cell = playlingField.getRandomCell();

        playlingField.setCellValue(cell.getRow(), cell.getColumn(), DEFAULT_START_VALUE);

        Cell nextCell = playlingField.getRandomCell();

        for (int count = playlingField.getActiveCells(); count < DEFAULT_START_FIELDS; count++) {
            while (cell.equals(nextCell)) {
                nextCell = playlingField.getRandomCell();
            }

            playlingField.setCellValue(nextCell.getRow(), nextCell.getColumn(), DEFAULT_START_VALUE);
            cell = nextCell;
        }

        updateGridStatusListeners();
    }

    public void addNewCell() {

        if (playlingField.getActiveCells() == (playlingField.getGridSize() * playlingField.getGridSize())) {
            Log.i(TAG, "addNewCell: Field is full. Can't add new cell.");
            return;
        }

        Cell cell;

        do {
            cell = playlingField.getRandomCell();

        } while (playlingField.cellHasValue(cell.getRow(), cell.getColumn()));

        playlingField.setCellValue(cell.getRow(), cell.getColumn(), DEFAULT_START_VALUE);
    }

    public void move(MOVE_DIRECTION direction) {

        if (gameStopped) {
            return;
        }

        if (playlingField.wouldMoveCells(direction)) {
            playlingField.moveCells(direction);
            addNewCell();
            updateGridStatusListeners();
        }

        if (playlingField.isGameOver()) {
            gameStopped = true;
            updateGameOverListeners();
        } else if (playlingField.isGameWon()) {
            gameStopped = true;
            updateGameWonListeners();
        }

    }

    private void updateGridStatusListeners() {

        for (GridUpdateListener gridUpdateListener : gridUpdateListeners) {
            gridUpdateListener.gridUpdated(playlingField.getGridStatus());
        }
    }

    private void updateGameOverListeners() {

        for (GridUpdateListener gridUpdateListener : gridUpdateListeners) {
            gridUpdateListener.gameOver();
        }
    }

    private void updateGameWonListeners() {

        for (GridUpdateListener gridUpdateListener : gridUpdateListeners) {
            gridUpdateListener.gameWon();
        }
    }

    public void removeGridUpdateListeners(GridUpdateListener listener) {
        gridUpdateListeners.remove(listener);
    }

    public void addGridUpdateListeners(GridUpdateListener listener) {
        this.gridUpdateListeners.add(listener);
        //get a fresh update after registration
        updateGridStatusListeners();
    }

    public interface GridUpdateListener {
        public void gridUpdated(int[][] updatedGrid);

        public void gameOver();

        public void gameWon();
    }

}
