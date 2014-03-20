package de.stefanhoth.android.got2048.logic.model;

import android.test.AndroidTestCase;

import junit.framework.Assert;

import java.util.ArrayList;

/**
 * TODO describe class
 *
 * @author Stefan Hoth <sh@jnamic.com>
 *         date: 20.03.14 21:24
 * @since TODO add version
 */
public class GridTest extends AndroidTestCase {


    public void testGridSetup() {

        Grid grid = new Grid();
        assertNotNull(grid);
        assertEquals(grid.getGridSize(), Grid.DEFAULT_GRID_SIZE);
        assertNotNull(grid.getGrid());
        assertEquals(grid.getGrid().size(), Grid.DEFAULT_GRID_SIZE);

        for (ArrayList<Cell> cells : grid.getGrid()) {
            assertEquals(cells.size(), Grid.DEFAULT_GRID_SIZE);
        }

        int testSize = 5;
        grid = new Grid(5);

        assertNotNull(grid);
        assertEquals(grid.getGridSize(), testSize);
        assertNotNull(grid.getGrid());

        assertEquals(grid.getGrid().size(), testSize);
        for (ArrayList<Cell> cells : grid.getGrid()) {
            assertEquals(cells.size(), testSize);
        }
    }

    public void testGetCell() throws Exception {

        Grid grid = new Grid();

        int row = 0;
        int column = 0;
        assertNotNull(grid.getCell(row, column));
        assertSame(grid.getGrid().get(row).get(column), grid.getCell(row, column));

        row = 1;
        column = 1;
        assertNotNull(grid.getCell(row, column));
        assertSame(grid.getGrid().get(row).get(column), grid.getCell(row, column));

        row = grid.getGridSize() - 1;
        column = 1;
        assertNotNull(grid.getCell(row, column));
        assertSame(grid.getGrid().get(row).get(column), grid.getCell(row, column));

        row = grid.getGridSize() - 1;
        column = grid.getGridSize() - 1;
        assertNotNull(grid.getCell(row, column));
        assertSame(grid.getGrid().get(row).get(column), grid.getCell(row, column));

        row = grid.getGridSize();
        column = grid.getGridSize();
        try {
            assertNotNull(grid.getCell(row, column));
            Assert.fail("Expected ArrayIndexOutOfBoundsException for index >= grid size");
        } catch (ArrayIndexOutOfBoundsException e) {
            //works as expected
        }
    }

    public void testReset() throws Exception {

        Grid grid = new Grid();

        //First assign a value to each cell
        for (int row = 0; row < grid.getGridSize(); row++) {
            for (int column = 0; column < grid.getGridSize(); column++) {
                grid.getCell(row, column).setValue(row + column);
            }
        }

        grid.reset();

        //then check if each cell has no value anymore after reset
        for (int row = 0; row < grid.getGridSize(); row++) {
            for (int column = 0; column < grid.getGridSize(); column++) {
                assertFalse(grid.getCell(row, column).hasValue());
            }
        }


    }
}
