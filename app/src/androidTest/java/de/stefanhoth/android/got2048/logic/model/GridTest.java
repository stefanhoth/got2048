package de.stefanhoth.android.got2048.logic.model;

import junit.framework.Assert;
import junit.framework.TestCase;

import java.util.ArrayList;

/**
 * TODO describe class
 *
 * @author Stefan Hoth <sh@jnamic.com>
 *         date: 20.03.14 21:24
 * @since TODO add version
 */
public class GridTest extends TestCase {

    public GridTest() {
        this(GridTest.class.getSimpleName());
    }

    public GridTest(String name) {
        super(name);
    }

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

    public void testGetActiveCells() throws Exception {

        Grid grid = new Grid();

        assertTrue(grid.getActiveCells() == 0);

        grid.getCell(0, 0).setValue(1337);

        assertTrue(grid.getActiveCells() == 1);

        grid.getCell(0, 0).setValue(42);

        assertTrue(grid.getActiveCells() == 1);


        grid.getCell(1, 1).setValue(31337);
        assertTrue(grid.getActiveCells() == 2);

        grid.reset();
        assertTrue(grid.getActiveCells() == 0);
    }

    public void testTranspose() throws Exception {

        Grid grid = new Grid(4);
        assertTrue(grid.getActiveCells() == 0);

        grid.getCell(0, 0).setValue(1);
        grid.getCell(0, 1).setValue(2);
        grid.getCell(0, 2).setValue(3);
        grid.getCell(0, 3).setValue(4);
        grid.getCell(1, 0).setValue(5);
        grid.getCell(1, 1).setValue(6);
        grid.getCell(1, 2).setValue(7);
        grid.getCell(1, 3).setValue(8);
        grid.getCell(2, 0).setValue(9);
        grid.getCell(2, 1).setValue(10);
        grid.getCell(2, 2).setValue(11);
        grid.getCell(2, 3).setValue(12);
        grid.getCell(3, 0).setValue(13);
        grid.getCell(3, 1).setValue(14);
        grid.getCell(3, 2).setValue(15);
        grid.getCell(3, 3).setValue(16);

        assertTrue(grid.getActiveCells() == 16);

        grid.transposeGrid();

        assertTrue(grid.getActiveCells() == 16);

        assertEquals(1, (int) grid.getCell(0, 0).getValue());
        assertEquals(6, (int) grid.getCell(1, 1).getValue());
        assertEquals(11, (int) grid.getCell(2, 2).getValue());
        assertEquals(16, (int) grid.getCell(3, 3).getValue());

        assertEquals(2, (int) grid.getCell(1, 0).getValue());
        assertEquals(3, (int) grid.getCell(2, 0).getValue());
        assertEquals(4, (int) grid.getCell(3, 0).getValue());

        assertEquals(5, (int) grid.getCell(0, 1).getValue());
        assertEquals(7, (int) grid.getCell(2, 1).getValue());
        assertEquals(8, (int) grid.getCell(3, 1).getValue());

        assertEquals(9, (int) grid.getCell(0, 2).getValue());
        assertEquals(10, (int) grid.getCell(1, 2).getValue());
        assertEquals(12, (int) grid.getCell(3, 2).getValue());

        assertEquals(13, (int) grid.getCell(0, 3).getValue());
        assertEquals(14, (int) grid.getCell(1, 3).getValue());
        assertEquals(15, (int) grid.getCell(2, 3).getValue());
    }

    public void testReverseEachRow() throws Exception {

        Grid grid = new Grid(4);
        assertTrue(grid.getActiveCells() == 0);

        grid.getCell(0, 0).setValue(1);
        grid.getCell(0, 1).setValue(2);
        grid.getCell(0, 2).setValue(3);
        grid.getCell(0, 3).setValue(4);
        grid.getCell(1, 0).setValue(5);
        grid.getCell(1, 1).setValue(6);
        grid.getCell(1, 2).setValue(7);
        grid.getCell(1, 3).setValue(8);
        grid.getCell(2, 0).setValue(9);
        grid.getCell(2, 1).setValue(10);
        grid.getCell(2, 2).setValue(11);
        grid.getCell(2, 3).setValue(12);
        grid.getCell(3, 0).setValue(13);
        grid.getCell(3, 1).setValue(14);
        grid.getCell(3, 2).setValue(15);
        grid.getCell(3, 3).setValue(16);

        assertTrue(grid.getActiveCells() == 16);

        grid.reverseEachRow();

        assertTrue(grid.getActiveCells() == 16);

        assertEquals(4, (int) grid.getCell(0, 0).getValue());
        assertEquals(3, (int) grid.getCell(0, 1).getValue());
        assertEquals(2, (int) grid.getCell(0, 2).getValue());
        assertEquals(1, (int) grid.getCell(0, 3).getValue());

        assertEquals(8, (int) grid.getCell(1, 0).getValue());
        assertEquals(7, (int) grid.getCell(1, 1).getValue());
        assertEquals(6, (int) grid.getCell(1, 2).getValue());
        assertEquals(5, (int) grid.getCell(1, 3).getValue());

        assertEquals(12, (int) grid.getCell(2, 0).getValue());
        assertEquals(11, (int) grid.getCell(2, 1).getValue());
        assertEquals(10, (int) grid.getCell(2, 2).getValue());
        assertEquals(9, (int) grid.getCell(2, 3).getValue());

        assertEquals(16, (int) grid.getCell(3, 0).getValue());
        assertEquals(15, (int) grid.getCell(3, 1).getValue());
        assertEquals(14, (int) grid.getCell(3, 2).getValue());
        assertEquals(13, (int) grid.getCell(3, 3).getValue());

    }

    public void testRotate90Clockwise() throws Exception {

        Grid grid = new Grid(4);

        assertTrue(grid.getActiveCells() == 0);

        grid.getCell(0, 0).setValue(1);
        grid.getCell(0, 1).setValue(2);
        grid.getCell(0, 2).setValue(3);
        grid.getCell(0, 3).setValue(4);
        grid.getCell(1, 0).setValue(5);
        grid.getCell(1, 1).setValue(6);
        grid.getCell(1, 2).setValue(7);
        grid.getCell(1, 3).setValue(8);
        grid.getCell(2, 0).setValue(9);
        grid.getCell(2, 1).setValue(10);
        grid.getCell(2, 2).setValue(11);
        grid.getCell(2, 3).setValue(12);
        grid.getCell(3, 0).setValue(13);
        grid.getCell(3, 1).setValue(14);
        grid.getCell(3, 2).setValue(15);
        grid.getCell(3, 3).setValue(16);

        assertEquals(16, grid.getActiveCells());

        grid.rotateGrid90(true);

        assertEquals(16, grid.getActiveCells());

        /**
         *  |  1 |  2 |  3 |  4 |
         *  |  5 |  6 |  7 |  8 |
         *  |  9 | 10 | 11 | 12 |
         *  | 13 | 14 | 15 | 16 |
         *
         *   => rot 90 cw
         *
         *  | 13 |  9 |  5 |  1 |
         *  | 14 | 10 |  6 |  2 |
         *  | 15 | 11 |  7 |  3 |
         *  | 16 | 12 |  8 |  4 |
         */


        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                assertTrue("Cell [" + i + "," + j + "] has value", grid.getCell(i, j).hasValue());
            }
        }

        assertEquals("Cell [0,0]", 13, (int) grid.getCell(0, 0).getValue());
        assertEquals("Cell [0,1]", 9, (int) grid.getCell(0, 1).getValue());
        assertEquals("Cell [0,2]", 5, (int) grid.getCell(0, 2).getValue());
        assertEquals("Cell [0,3]", 1, (int) grid.getCell(0, 3).getValue());

        assertEquals("Cell [1,0]", 14, (int) grid.getCell(1, 0).getValue());
        assertEquals("Cell [1,1]", 10, (int) grid.getCell(1, 1).getValue());
        assertEquals("Cell [1,2]", 6, (int) grid.getCell(1, 2).getValue());
        assertEquals("Cell [1,3]", 2, (int) grid.getCell(1, 3).getValue());

        assertEquals("Cell [2,0]", 15, (int) grid.getCell(2, 0).getValue());
        assertEquals("Cell [2,1]", 11, (int) grid.getCell(2, 1).getValue());
        assertEquals("Cell [2,2]", 7, (int) grid.getCell(2, 2).getValue());
        assertEquals("Cell [2,3]", 3, (int) grid.getCell(2, 3).getValue());

        assertEquals("Cell [3,0]", 16, (int) grid.getCell(3, 0).getValue());
        assertEquals("Cell [3,1]", 12, (int) grid.getCell(3, 1).getValue());
        assertEquals("Cell [3,2]", 8, (int) grid.getCell(3, 2).getValue());
        assertEquals("Cell [3,3]", 4, (int) grid.getCell(3, 3).getValue());
    }


    public void testRotate90CounterClockwise() throws Exception {

        Grid grid = new Grid(4);

        assertTrue(grid.getActiveCells() == 0);

        grid.getCell(0, 0).setValue(1);
        grid.getCell(0, 1).setValue(2);
        grid.getCell(0, 2).setValue(3);
        grid.getCell(0, 3).setValue(4);
        grid.getCell(1, 0).setValue(5);
        grid.getCell(1, 1).setValue(6);
        grid.getCell(1, 2).setValue(7);
        grid.getCell(1, 3).setValue(8);
        grid.getCell(2, 0).setValue(9);
        grid.getCell(2, 1).setValue(10);
        grid.getCell(2, 2).setValue(11);
        grid.getCell(2, 3).setValue(12);
        grid.getCell(3, 0).setValue(13);
        grid.getCell(3, 1).setValue(14);
        grid.getCell(3, 2).setValue(15);
        grid.getCell(3, 3).setValue(16);

        assertEquals(16, grid.getActiveCells());

        grid.rotateGrid90(false);

        assertEquals(16, grid.getActiveCells());

        /**
         *  |  1 |  2 |  3 |  4 |
         *  |  5 |  6 |  7 |  8 |
         *  |  9 | 10 | 11 | 12 |
         *  | 13 | 14 | 15 | 16 |
         *
         *   => rot 90 ccw
         *
         *  |  4 |  8 | 12 | 16 |
         *  |  3 |  7 | 11 | 15 |
         *  |  2 |  6 | 10 | 14 |
         *  |  1 |  5 |  9 | 13 |
         */


        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                assertTrue("Cell [" + i + "," + j + "] has value", grid.getCell(i, j).hasValue());
            }
        }

        assertEquals("Cell [0,0]", 4, (int) grid.getCell(0, 0).getValue());
        assertEquals("Cell [0,1]", 8, (int) grid.getCell(0, 1).getValue());
        assertEquals("Cell [0,2]", 12, (int) grid.getCell(0, 2).getValue());
        assertEquals("Cell [0,3]", 16, (int) grid.getCell(0, 3).getValue());

        assertEquals("Cell [1,0]", 3, (int) grid.getCell(1, 0).getValue());
        assertEquals("Cell [1,1]", 7, (int) grid.getCell(1, 1).getValue());
        assertEquals("Cell [1,2]", 11, (int) grid.getCell(1, 2).getValue());
        assertEquals("Cell [1,3]", 15, (int) grid.getCell(1, 3).getValue());

        assertEquals("Cell [2,0]", 2, (int) grid.getCell(2, 0).getValue());
        assertEquals("Cell [2,1]", 6, (int) grid.getCell(2, 1).getValue());
        assertEquals("Cell [2,2]", 10, (int) grid.getCell(2, 2).getValue());
        assertEquals("Cell [2,3]", 14, (int) grid.getCell(2, 3).getValue());

        assertEquals("Cell [3,0]", 1, (int) grid.getCell(3, 0).getValue());
        assertEquals("Cell [3,1]", 5, (int) grid.getCell(3, 1).getValue());
        assertEquals("Cell [3,2]", 9, (int) grid.getCell(3, 2).getValue());
        assertEquals("Cell [3,3]", 13, (int) grid.getCell(3, 3).getValue());
    }


    public void testRotate180() throws Exception {

        Grid grid = new Grid(4);

        assertTrue(grid.getActiveCells() == 0);

        grid.getCell(0, 0).setValue(1);
        grid.getCell(0, 1).setValue(2);
        grid.getCell(0, 2).setValue(3);
        grid.getCell(0, 3).setValue(4);
        grid.getCell(1, 0).setValue(5);
        grid.getCell(1, 1).setValue(6);
        grid.getCell(1, 2).setValue(7);
        grid.getCell(1, 3).setValue(8);
        grid.getCell(2, 0).setValue(9);
        grid.getCell(2, 1).setValue(10);
        grid.getCell(2, 2).setValue(11);
        grid.getCell(2, 3).setValue(12);
        grid.getCell(3, 0).setValue(13);
        grid.getCell(3, 1).setValue(14);
        grid.getCell(3, 2).setValue(15);
        grid.getCell(3, 3).setValue(16);

        assertEquals(16, grid.getActiveCells());

        grid.rotateGrid180();

        assertEquals(16, grid.getActiveCells());

        /**
         *  |  1 |  2 |  3 |  4 |
         *  |  5 |  6 |  7 |  8 |
         *  |  9 | 10 | 11 | 12 |
         *  | 13 | 14 | 15 | 16 |
         *
         *   => rot 180
         *
         *  | 16 | 15 | 14 | 13 |
         *  | 12 | 11 | 10 |  9 |
         *  |  8 |  7 |  6 |  5 |
         *  |  4 |  3 |  2 |  1 |
         */


        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                assertTrue("Cell [" + i + "," + j + "] has value", grid.getCell(i, j).hasValue());
            }
        }

        assertEquals("Cell [0,0]", 16, (int) grid.getCell(0, 0).getValue());
        assertEquals("Cell [0,1]", 15, (int) grid.getCell(0, 1).getValue());
        assertEquals("Cell [0,2]", 14, (int) grid.getCell(0, 2).getValue());
        assertEquals("Cell [0,3]", 13, (int) grid.getCell(0, 3).getValue());

        assertEquals("Cell [1,0]", 12, (int) grid.getCell(1, 0).getValue());
        assertEquals("Cell [1,1]", 11, (int) grid.getCell(1, 1).getValue());
        assertEquals("Cell [1,2]", 10, (int) grid.getCell(1, 2).getValue());
        assertEquals("Cell [1,3]", 9, (int) grid.getCell(1, 3).getValue());

        assertEquals("Cell [2,0]", 8, (int) grid.getCell(2, 0).getValue());
        assertEquals("Cell [2,1]", 7, (int) grid.getCell(2, 1).getValue());
        assertEquals("Cell [2,2]", 6, (int) grid.getCell(2, 2).getValue());
        assertEquals("Cell [2,3]", 5, (int) grid.getCell(2, 3).getValue());

        assertEquals("Cell [3,0]", 4, (int) grid.getCell(3, 0).getValue());
        assertEquals("Cell [3,1]", 3, (int) grid.getCell(3, 1).getValue());
        assertEquals("Cell [3,2]", 2, (int) grid.getCell(3, 2).getValue());
        assertEquals("Cell [3,3]", 1, (int) grid.getCell(3, 3).getValue());
    }
}
