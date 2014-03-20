package de.stefanhoth.android.got2048.logic;

import de.stefanhoth.android.got2048.logic.model.Cell;
import de.stefanhoth.android.got2048.logic.model.Grid;

/**
 * TODO describe class
 *
 * @author Stefan Hoth <sh@jnamic.com>
 *         date: 20.03.14 20:40
 * @since TODO add version
 */
public class MCP {

    protected static final int DEFAULT_START_FIELDS = 2;
    private static final int DEFAULT_START_VALUE = 2;
    private Grid playlingField;

    public MCP() {

        playlingField = new Grid();
    }

    protected Grid getPlaylingField() {
        return playlingField;
    }

    public void addStartCells() {

        Cell cell = playlingField.getRandomCell();

        cell.setValue(DEFAULT_START_VALUE);

        Cell nextCell = playlingField.getRandomCell();

        for (int i = playlingField.getActiveCells(); i < DEFAULT_START_FIELDS; i++) {
            while (cell.equals(nextCell)) {
                nextCell = playlingField.getRandomCell();
            }

            nextCell.setValue(DEFAULT_START_VALUE);
            cell = nextCell;
        }
    }
}
