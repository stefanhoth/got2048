package de.stefanhoth.android.got2048.logic.model;

import junit.framework.Assert;
import junit.framework.TestCase;

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
        assertNotNull("Grid is not null", grid);
        assertEquals("Gridsize is DEFAULT_GRID_SIZE", Grid.DEFAULT_GRID_SIZE, grid.getGridSize());
        assertNotNull("Grid.getGrid not null", grid.getGrid());
        assertEquals("Actual grid is DEFAULT_GRID_SIZE", Grid.DEFAULT_GRID_SIZE, grid.getGrid().length);


        for (int rowNumber = 0; rowNumber < grid.getGridSize(); rowNumber++) {
            assertEquals("Row=" + rowNumber + " is DEFAULT_GRID_SIZE ", Grid.DEFAULT_GRID_SIZE, grid.getRow(rowNumber).length);
        }

        int testSize = 8;
        grid = new Grid(testSize);

        assertNotNull("Grid is not null", grid);
        assertEquals("Gridsize is DEFAULT_GRID_SIZE", testSize, grid.getGridSize());
        assertNotNull("Grid.getGrid not null", grid.getGrid());
        assertEquals("Actual grid is DEFAULT_GRID_SIZE", testSize, grid.getGrid().length);


        for (int rowNumber = 0; rowNumber < grid.getGridSize(); rowNumber++) {
            assertEquals("Row=" + rowNumber + " is DEFAULT_GRID_SIZE ", testSize, grid.getRow(rowNumber).length);
        }
    }

    public void testGetCell() throws Exception {

        Grid grid = new Grid();

        int row = 0;
        int column = 0;
        assertNotNull(grid.getCellValue(row, column));
        assertSame(grid.getGrid()[row][column], grid.getCellValue(row, column));

        row = 1;
        column = 1;
        assertNotNull(grid.getCellValue(row, column));
        assertSame(grid.getGrid()[row][column], grid.getCellValue(row, column));

        row = grid.getGridSize() - 1;
        column = 1;
        assertNotNull(grid.getCellValue(row, column));
        assertSame(grid.getGrid()[row][column], grid.getCellValue(row, column));

        row = grid.getGridSize() - 1;
        column = grid.getGridSize() - 1;
        assertNotNull(grid.getCellValue(row, column));
        assertSame(grid.getGrid()[row][column], grid.getCellValue(row, column));

        row = grid.getGridSize();
        column = grid.getGridSize();
        try {
            assertNotNull(grid.getCellValue(row, column));
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
                grid.setCellValue(row, column, row + column);
            }
        }

        grid.reset();

        //then check if each cell has no value anymore after reset
        for (int row = 0; row < grid.getGridSize(); row++) {
            for (int column = 0; column < grid.getGridSize(); column++) {
                assertEquals("Cell [" + row + "," + column + "] has value", false, grid.cellHasValue(row, column));
            }
        }
    }

    public void testGetActiveCells() throws Exception {

        Grid grid = new Grid();

        assertTrue(grid.getActiveCells() == 0);

        grid.setCellValue(0, 0, 1337);

        assertTrue(grid.getActiveCells() == 1);

        grid.setCellValue(0, 0, 42);

        assertTrue(grid.getActiveCells() == 1);


        grid.setCellValue(1, 1, 31337);
        assertTrue(grid.getActiveCells() == 2);

        grid.reset();
        assertTrue(grid.getActiveCells() == 0);
    }

    public void testTranspose() throws Exception {

        Grid grid = new Grid(4);
        assertTrue(grid.getActiveCells() == 0);


        int counter = 1;
        for (int row = 0; row < 4; row++) {
            for (int column = 0; column < 4; column++) {
                grid.setCellValue(row, column, counter++);
            }
        }

        assertTrue(grid.getActiveCells() == 16);

        grid.transposeGrid();

        assertTrue(grid.getActiveCells() == 16);

        assertEquals(1, grid.getCellValue(0, 0));
        assertEquals(6, grid.getCellValue(1, 1));
        assertEquals(11, grid.getCellValue(2, 2));
        assertEquals(16, grid.getCellValue(3, 3));

        assertEquals(2, grid.getCellValue(1, 0));
        assertEquals(3, grid.getCellValue(2, 0));
        assertEquals(4, grid.getCellValue(3, 0));

        assertEquals(5, grid.getCellValue(0, 1));
        assertEquals(7, grid.getCellValue(2, 1));
        assertEquals(8, grid.getCellValue(3, 1));

        assertEquals(9, grid.getCellValue(0, 2));
        assertEquals(10, grid.getCellValue(1, 2));
        assertEquals(12, grid.getCellValue(3, 2));

        assertEquals(13, grid.getCellValue(0, 3));
        assertEquals(14, grid.getCellValue(1, 3));
        assertEquals(15, grid.getCellValue(2, 3));
    }

    public void testReverseEachRow() throws Exception {

        Grid grid = new Grid(4);
        assertTrue(grid.getActiveCells() == 0);

        int counter = 1;
        for (int row = 0; row < 4; row++) {
            for (int column = 0; column < 4; column++) {
                grid.setCellValue(row, column, counter++);
            }
        }

        assertTrue(grid.getActiveCells() == 16);

        grid.reverseEachRow();

        assertTrue(grid.getActiveCells() == 16);

        assertEquals("Cell [0,0]", 4, grid.getCellValue(0, 0));
        assertEquals("Cell [0,1]", 3, grid.getCellValue(0, 1));
        assertEquals("Cell [0,2]", 2, grid.getCellValue(0, 2));
        assertEquals("Cell [0,3]", 1, grid.getCellValue(0, 3));

        assertEquals("Cell [1,0]", 8, grid.getCellValue(1, 0));
        assertEquals("Cell [1,1]", 7, grid.getCellValue(1, 1));
        assertEquals("Cell [1,2]", 6, grid.getCellValue(1, 2));
        assertEquals("Cell [1,3]", 5, grid.getCellValue(1, 3));

        assertEquals("Cell [2,0]", 12, grid.getCellValue(2, 0));
        assertEquals("Cell [2,1]", 11, grid.getCellValue(2, 1));
        assertEquals("Cell [2,2]", 10, grid.getCellValue(2, 2));
        assertEquals("Cell [2,3]", 9, grid.getCellValue(2, 3));

        assertEquals("Cell [3,0]", 16, grid.getCellValue(3, 0));
        assertEquals("Cell [3,1]", 15, grid.getCellValue(3, 1));
        assertEquals("Cell [3,2]", 14, grid.getCellValue(3, 2));
        assertEquals("Cell [3,3]", 13, grid.getCellValue(3, 3));
    }

    public void testRotate90Clockwise() throws Exception {

        Grid grid = new Grid(4);

        assertTrue(grid.getActiveCells() == 0);

        int counter = 1;
        for (int row = 0; row < 4; row++) {
            for (int column = 0; column < 4; column++) {
                grid.setCellValue(row, column, counter++);
            }
        }

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
                assertEquals("Cell [" + i + "," + j + "] has value", true, grid.cellHasValue(i, j));
            }
        }

        assertEquals("Cell [0,0]", 13, grid.getCellValue(0, 0));
        assertEquals("Cell [0,1]", 9, grid.getCellValue(0, 1));
        assertEquals("Cell [0,2]", 5, grid.getCellValue(0, 2));
        assertEquals("Cell [0,3]", 1, grid.getCellValue(0, 3));

        assertEquals("Cell [1,0]", 14, grid.getCellValue(1, 0));
        assertEquals("Cell [1,1]", 10, grid.getCellValue(1, 1));
        assertEquals("Cell [1,2]", 6, grid.getCellValue(1, 2));
        assertEquals("Cell [1,3]", 2, grid.getCellValue(1, 3));

        assertEquals("Cell [2,0]", 15, grid.getCellValue(2, 0));
        assertEquals("Cell [2,1]", 11, grid.getCellValue(2, 1));
        assertEquals("Cell [2,2]", 7, grid.getCellValue(2, 2));
        assertEquals("Cell [2,3]", 3, grid.getCellValue(2, 3));

        assertEquals("Cell [3,0]", 16, grid.getCellValue(3, 0));
        assertEquals("Cell [3,1]", 12, grid.getCellValue(3, 1));
        assertEquals("Cell [3,2]", 8, grid.getCellValue(3, 2));
        assertEquals("Cell [3,3]", 4, grid.getCellValue(3, 3));
    }


    public void testRotate90CounterClockwise() throws Exception {

        Grid grid = new Grid(4);

        assertEquals("Active cells", 0, grid.getActiveCells());

        int counter = 1;
        for (int row = 0; row < 4; row++) {
            for (int column = 0; column < 4; column++) {
                grid.setCellValue(row, column, counter++);
            }
        }

        assertEquals("Active cells", 16, grid.getActiveCells());

        grid.rotateGrid90(false);

        assertEquals("Active cells", 16, grid.getActiveCells());

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
                assertEquals("Cell [" + i + "," + j + "] has value", true, grid.cellHasValue(i, j));
            }
        }

        assertEquals("Cell [0,0]", 4, grid.getCellValue(0, 0));
        assertEquals("Cell [0,1]", 8, grid.getCellValue(0, 1));
        assertEquals("Cell [0,2]", 12, grid.getCellValue(0, 2));
        assertEquals("Cell [0,3]", 16, grid.getCellValue(0, 3));

        assertEquals("Cell [1,0]", 3, grid.getCellValue(1, 0));
        assertEquals("Cell [1,1]", 7, grid.getCellValue(1, 1));
        assertEquals("Cell [1,2]", 11, grid.getCellValue(1, 2));
        assertEquals("Cell [1,3]", 15, grid.getCellValue(1, 3));

        assertEquals("Cell [2,0]", 2, grid.getCellValue(2, 0));
        assertEquals("Cell [2,1]", 6, grid.getCellValue(2, 1));
        assertEquals("Cell [2,2]", 10, grid.getCellValue(2, 2));
        assertEquals("Cell [2,3]", 14, grid.getCellValue(2, 3));

        assertEquals("Cell [3,0]", 1, grid.getCellValue(3, 0));
        assertEquals("Cell [3,1]", 5, grid.getCellValue(3, 1));
        assertEquals("Cell [3,2]", 9, grid.getCellValue(3, 2));
        assertEquals("Cell [3,3]", 13, grid.getCellValue(3, 3));
    }


    public void testRotate180() throws Exception {

        Grid grid = new Grid(4);

        assertEquals("Active cells", 0, grid.getActiveCells());

        int counter = 1;
        for (int row = 0; row < 4; row++) {
            for (int column = 0; column < 4; column++) {
                grid.setCellValue(row, column, counter++);
            }
        }

        assertEquals("Active cells", 16, grid.getActiveCells());

        grid.rotateGrid180();

        assertEquals("Active cells", 16, grid.getActiveCells());

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
                assertEquals("Cell [" + i + "," + j + "] has value", true, grid.cellHasValue(i, j));
            }
        }

        assertEquals("Cell [0,0]", 16, grid.getCellValue(0, 0));
        assertEquals("Cell [0,1]", 15, grid.getCellValue(0, 1));
        assertEquals("Cell [0,2]", 14, grid.getCellValue(0, 2));
        assertEquals("Cell [0,3]", 13, grid.getCellValue(0, 3));

        assertEquals("Cell [1,0]", 12, grid.getCellValue(1, 0));
        assertEquals("Cell [1,1]", 11, grid.getCellValue(1, 1));
        assertEquals("Cell [1,2]", 10, grid.getCellValue(1, 2));
        assertEquals("Cell [1,3]", 9, grid.getCellValue(1, 3));

        assertEquals("Cell [2,0]", 8, grid.getCellValue(2, 0));
        assertEquals("Cell [2,1]", 7, grid.getCellValue(2, 1));
        assertEquals("Cell [2,2]", 6, grid.getCellValue(2, 2));
        assertEquals("Cell [2,3]", 5, grid.getCellValue(2, 3));

        assertEquals("Cell [3,0]", 4, grid.getCellValue(3, 0));
        assertEquals("Cell [3,1]", 3, grid.getCellValue(3, 1));
        assertEquals("Cell [3,2]", 2, grid.getCellValue(3, 2));
        assertEquals("Cell [3,3]", 1, grid.getCellValue(3, 3));
    }
}
