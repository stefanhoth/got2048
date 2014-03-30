package de.stefanhoth.android.got2048.logic;

import junit.framework.TestCase;

import de.stefanhoth.android.got2048.logic.model.Cell;
import de.stefanhoth.android.got2048.logic.model.MOVE_DIRECTION;

/**
 * TODO describe class
 *
 * @author Stefan Hoth <sh@jnamic.com>
 *         date: 20.03.14 22:03
 * @since TODO add version
 */
public class MCPTest extends TestCase {

    private MCP mcp;

    public MCPTest() {
        this(MCPTest.class.getSimpleName());
    }

    public MCPTest(String name) {
        super(name);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        mcp = new MCP();
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
        mcp = null;
    }

    public void testSetup() throws Exception {

        assertNotNull("Playing field exists", mcp.getPlaylingField());
    }

    public void testStartCellsAdded() throws Exception {

        mcp.addStartCells();

        assertEquals("Active cells", MCP.DEFAULT_START_FIELDS, mcp.getPlaylingField().getActiveCells());
    }

    public void testMoveRightWorksSingle() throws Exception {

        int gridSize = mcp.getPlaylingField().getGridSize();
        int row = 0;
        int column = 0;

        mcp.getPlaylingField().reset();
        mcp.getPlaylingField().setCellValue(row, column, MCP.DEFAULT_START_VALUE);

        mcp.move(MOVE_DIRECTION.RIGHT);

        for (int testColumn = 0; testColumn < gridSize; testColumn++) {

            if (testColumn < gridSize - 1) {
                assertEquals("Cell [" + row + "|" + testColumn + "] has value", false, mcp.getPlaylingField().cellHasValue(row, testColumn));
            } else {
                assertEquals("Cell [" + row + "|" + testColumn + "]", MCP.DEFAULT_START_VALUE, mcp.getPlaylingField().getCellValue(row, testColumn));
            }
        }
    }

    public void testMoveRightWorksSingle_0_1() throws Exception {

        int gridSize = mcp.getPlaylingField().getGridSize();
        int row = 0;
        int column = 1;

        mcp.getPlaylingField().reset();
        mcp.getPlaylingField().setCellValue(row, column, MCP.DEFAULT_START_VALUE);

        mcp.move(MOVE_DIRECTION.RIGHT);

        assertEquals("Cell [" + row + "|" + column + "] has value", false, mcp.getPlaylingField().cellHasValue(row, column));

        for (int testColumn = 0; testColumn < gridSize; testColumn++) {

            if (testColumn < gridSize - 1) {
                assertEquals("Cell [" + row + "|" + testColumn + "] has value", false, mcp.getPlaylingField().cellHasValue(row, testColumn));
            } else {
                assertEquals("Cell [" + row + "|" + testColumn + "]", MCP.DEFAULT_START_VALUE, mcp.getPlaylingField().getCellValue(row, testColumn));
            }
        }
    }

    public void testMoveRightWorksSingle_0_4() throws Exception {

        int gridSize = mcp.getPlaylingField().getGridSize();
        int row = 0;
        int column = gridSize - 1;

        mcp.getPlaylingField().reset();
        mcp.getPlaylingField().setCellValue(row, column, MCP.DEFAULT_START_VALUE);

        mcp.move(MOVE_DIRECTION.RIGHT);

        for (int testColumn = 0; testColumn < gridSize; testColumn++) {

            if (testColumn < gridSize - 1) {
                assertEquals("Cell [" + row + "|" + testColumn + "] has value", false, mcp.getPlaylingField().cellHasValue(row, testColumn));
            } else {
                assertEquals("Cell [" + row + "|" + testColumn + "]", MCP.DEFAULT_START_VALUE, mcp.getPlaylingField().getCellValue(row, testColumn));
            }
        }
    }

    public void testMoveRightDoubleNoMerge() throws Exception {

        int gridSize = mcp.getPlaylingField().getGridSize();
        int row = 1;
        int column = 0;
        int testValue1 = 42;
        int testValue2 = 1337;

        mcp.getPlaylingField().reset();
        mcp.getPlaylingField().setCellValue(row, column, testValue1);
        assertEquals("Active cells", 1, mcp.getPlaylingField().getActiveCells());
        assertEquals("Cell [" + row + "|" + column + "]", testValue1, mcp.getPlaylingField().getCellValue(row, column));

        column += 2;
        mcp.getPlaylingField().setCellValue(row, column, testValue2);
        assertEquals("Active cells", 2, mcp.getPlaylingField().getActiveCells());
        assertEquals("Cell [" + row + "|" + column + "]", testValue2, mcp.getPlaylingField().getCellValue(row, column));

        Cell cell;
        for (int testColumn = 0; testColumn < gridSize; testColumn++) {
            if (testColumn == column || testColumn == column - 2) {
                continue;
            }

            assertEquals("Cell [" + row + "|" + testColumn + "] has value", false, mcp.getPlaylingField().cellHasValue(row, testColumn));
        }

        mcp.move(MOVE_DIRECTION.RIGHT);

        assertEquals("Active cells", 2, mcp.getPlaylingField().getActiveCells());

        for (int testColumn = 0; testColumn < gridSize; testColumn++) {

            if (testColumn < gridSize - 2) {
                assertEquals("Cell [" + row + "|" + testColumn + "] has value", false, mcp.getPlaylingField().cellHasValue(row, testColumn));
            } else if (testColumn == gridSize - 2) {
                assertEquals("Cell [" + row + "|" + testColumn + "]", testValue1, mcp.getPlaylingField().getCellValue(row, testColumn));
            } else if (testColumn == gridSize - 1) {
                assertEquals("Cell [" + row + "|" + testColumn + "]", testValue2, mcp.getPlaylingField().getCellValue(row, testColumn));
            }
        }
    }

    public void testMoveRightDoubleWithMerge() throws Exception {

        int gridSize = mcp.getPlaylingField().getGridSize();
        int row = 0;
        int column = 0;
        int testValue1 = MCP.DEFAULT_START_VALUE;
        int testValue2 = MCP.DEFAULT_START_VALUE;

        mcp.getPlaylingField().reset();
        mcp.getPlaylingField().setCellValue(row, column, testValue1);
        assertEquals("Active cells", 1, mcp.getPlaylingField().getActiveCells());
        assertEquals("Cell [" + row + "|" + column + "]", testValue1, mcp.getPlaylingField().getCellValue(row, column));

        column += 2;
        mcp.getPlaylingField().setCellValue(row, column, testValue2);
        assertEquals("Active cells", 2, mcp.getPlaylingField().getActiveCells());
        assertEquals("Cell [" + row + "|" + column + "]", testValue2, mcp.getPlaylingField().getCellValue(row, column));

        mcp.move(MOVE_DIRECTION.RIGHT);
        assertEquals("Active cells", 1, mcp.getPlaylingField().getActiveCells());

        for (int testColumn = 0; testColumn < gridSize; testColumn++) {

            if (testColumn < gridSize - 1) {
                assertEquals("Cell [" + row + "|" + testColumn + "] has value", false, mcp.getPlaylingField().cellHasValue(row, testColumn));
            } else {
                assertEquals("Cell [" + row + "|" + testColumn + "]", testValue1 + testValue2, mcp.getPlaylingField().getCellValue(row, testColumn));
            }
        }
    }

    public void testMoveRightTripleNoMerge() throws Exception {

        int gridSize = mcp.getPlaylingField().getGridSize();
        int row = 1;
        int column = 0;
        int testValue1 = 42;
        int testValue2 = 1337;
        int testValue3 = 1338;

        mcp.getPlaylingField().reset();
        mcp.getPlaylingField().setCellValue(row, column, testValue1);
        assertEquals("Active cells", 1, mcp.getPlaylingField().getActiveCells());
        assertEquals("Cell [" + row + "|" + column + "]", testValue1, mcp.getPlaylingField().getCellValue(row, column));

        column += 1;
        mcp.getPlaylingField().setCellValue(row, column, testValue2);
        assertEquals("Active cells", 2, mcp.getPlaylingField().getActiveCells());
        assertEquals("Cell [" + row + "|" + column + "]", testValue2, mcp.getPlaylingField().getCellValue(row, column));

        column += 1;
        mcp.getPlaylingField().setCellValue(row, column, testValue3);
        assertEquals("Active cells", 3, mcp.getPlaylingField().getActiveCells());
        assertEquals("Cell [" + row + "|" + column + "]", testValue3, mcp.getPlaylingField().getCellValue(row, column));

        Cell cell;
        for (int testColumn = 0; testColumn < gridSize; testColumn++) {
            if (testColumn == column || testColumn == column - 1 || testColumn == column - 2) {
                continue;
            }

            assertEquals("Cell [" + row + "|" + testColumn + "] has value", false, mcp.getPlaylingField().cellHasValue(row, testColumn));
        }

        mcp.move(MOVE_DIRECTION.RIGHT);

        assertEquals("Active cells", 3, mcp.getPlaylingField().getActiveCells());

        for (int testColumn = 0; testColumn < gridSize; testColumn++) {


            if (testColumn < gridSize - 3) {
                assertEquals("Cell [" + row + "|" + testColumn + "] has value", false, mcp.getPlaylingField().cellHasValue(row, testColumn));
            } else if (testColumn == gridSize - 3) {
                assertEquals("Cell [" + row + "|" + testColumn + "]", testValue1, mcp.getPlaylingField().getCellValue(row, testColumn));
            } else if (testColumn == gridSize - 2) {
                assertEquals("Cell [" + row + "|" + testColumn + "]", testValue2, mcp.getPlaylingField().getCellValue(row, testColumn));
            } else if (testColumn == gridSize - 1) {
                assertEquals("Cell [" + row + "|" + testColumn + "]", testValue3, mcp.getPlaylingField().getCellValue(row, testColumn));
            }
        }
    }

    public void testMoveRightTripleOneMerge() throws Exception {

        int gridSize = mcp.getPlaylingField().getGridSize();
        int row = 1;
        int column = 0;
        int testValue1 = 42;
        int testValue2 = 1337;
        int testValue3 = 1337;

        mcp.getPlaylingField().reset();
        mcp.getPlaylingField().setCellValue(row, column, testValue1);
        assertEquals("Active cells", 1, mcp.getPlaylingField().getActiveCells());
        assertEquals("Cell [" + row + "|" + column + "]", testValue1, mcp.getPlaylingField().getCellValue(row, column));

        column += 1;
        mcp.getPlaylingField().setCellValue(row, column, testValue2);
        assertEquals("Active cells", 2, mcp.getPlaylingField().getActiveCells());
        assertEquals("Cell [" + row + "|" + column + "]", testValue2, mcp.getPlaylingField().getCellValue(row, column));

        column += 1;
        mcp.getPlaylingField().setCellValue(row, column, testValue3);
        assertEquals("Active cells", 3, mcp.getPlaylingField().getActiveCells());
        assertEquals("Cell [" + row + "|" + column + "]", testValue3, mcp.getPlaylingField().getCellValue(row, column));

        Cell cell;
        for (int testColumn = 0; testColumn < gridSize; testColumn++) {
            if (testColumn == column || testColumn == column - 1 || testColumn == column - 2) {
                continue;
            }

            assertEquals("Cell [" + row + "|" + testColumn + "] has value", false, mcp.getPlaylingField().cellHasValue(row, testColumn));
        }

        mcp.move(MOVE_DIRECTION.RIGHT);

        assertEquals("Active cells", 2, mcp.getPlaylingField().getActiveCells());

        for (int testColumn = 0; testColumn < gridSize; testColumn++) {

            if (testColumn < gridSize - 3) {
                assertEquals("Cell [" + row + "|" + testColumn + "] has value", false, mcp.getPlaylingField().cellHasValue(row, testColumn));
            } else if (testColumn == gridSize - 2) {
                assertEquals("Cell [" + row + "|" + testColumn + "]", testValue1, mcp.getPlaylingField().getCellValue(row, testColumn));
            } else if (testColumn == gridSize - 1) {
                assertEquals("Cell [" + row + "|" + testColumn + "]", testValue2 + testValue3, mcp.getPlaylingField().getCellValue(row, testColumn));
            }
        }
    }


    public void testMoveRightTripleSameValue() throws Exception {

        int gridSize = mcp.getPlaylingField().getGridSize();
        int row = 1;
        int column = 0;
        int testValue1 = 1337;
        int testValue2 = 1337;
        int testValue3 = 1337;

        mcp.getPlaylingField().reset();
        mcp.getPlaylingField().setCellValue(row, column, testValue1);
        assertEquals("Active cells", 1, mcp.getPlaylingField().getActiveCells());
        assertEquals("Cell [" + row + "|" + column + "]", testValue1, mcp.getPlaylingField().getCellValue(row, column));

        column += 1;
        mcp.getPlaylingField().setCellValue(row, column, testValue2);
        assertEquals("Active cells", 2, mcp.getPlaylingField().getActiveCells());
        assertEquals("Cell [" + row + "|" + column + "]", testValue2, mcp.getPlaylingField().getCellValue(row, column));

        column += 1;
        mcp.getPlaylingField().setCellValue(row, column, testValue3);
        assertEquals("Active cells", 3, mcp.getPlaylingField().getActiveCells());
        assertEquals("Cell [" + row + "|" + column + "]", testValue3, mcp.getPlaylingField().getCellValue(row, column));


        Cell cell;
        for (int testColumn = 0; testColumn < gridSize; testColumn++) {
            if (testColumn == column || testColumn == column - 1 || testColumn == column - 2) {
                continue;
            }

            assertEquals("Cell [" + row + "|" + testColumn + "] has value", false, mcp.getPlaylingField().cellHasValue(row, testColumn));
        }

        mcp.move(MOVE_DIRECTION.RIGHT);

        assertEquals("Active cells", 2, mcp.getPlaylingField().getActiveCells());

        for (int testColumn = 0; testColumn < gridSize; testColumn++) {

            if (testColumn < gridSize - 3) {
                assertEquals("Cell [" + row + "|" + testColumn + "] has value", false, mcp.getPlaylingField().cellHasValue(row, testColumn));
            } else if (testColumn == gridSize - 2) {
                assertEquals("Cell [" + row + "|" + testColumn + "]", testValue1, mcp.getPlaylingField().getCellValue(row, testColumn));
            } else if (testColumn == gridSize - 1) {
                assertEquals("Cell [" + row + "|" + testColumn + "]", testValue2 + testValue3, mcp.getPlaylingField().getCellValue(row, testColumn));
            }
        }
    }

    public void testMoveRightTripleMerge2StepsNeeded() throws Exception {

        int gridSize = mcp.getPlaylingField().getGridSize();
        int row = 1;
        int column = 0;
        int testValue1 = 84;
        int testValue2 = 42;
        int testValue3 = 42;

        mcp.getPlaylingField().reset();
        mcp.getPlaylingField().setCellValue(row, column, testValue1);
        assertEquals("Active cells", 1, mcp.getPlaylingField().getActiveCells());
        assertEquals("Cell [" + row + "|" + column + "]", testValue1, mcp.getPlaylingField().getCellValue(row, column));

        column += 1;
        mcp.getPlaylingField().setCellValue(row, column, testValue2);
        assertEquals("Active cells", 2, mcp.getPlaylingField().getActiveCells());
        assertEquals("Cell [" + row + "|" + column + "]", testValue2, mcp.getPlaylingField().getCellValue(row, column));

        column += 1;
        mcp.getPlaylingField().setCellValue(row, column, testValue3);
        assertEquals("Active cells", 3, mcp.getPlaylingField().getActiveCells());
        assertEquals("Cell [" + row + "|" + column + "]", testValue3, mcp.getPlaylingField().getCellValue(row, column));

        Cell cell;
        for (int testColumn = 0; testColumn < gridSize; testColumn++) {
            if (testColumn == column || testColumn == column - 1 || testColumn == column - 2) {
                continue;
            }

            assertEquals("Cell [" + row + "|" + testColumn + "] has value", false, mcp.getPlaylingField().cellHasValue(row, testColumn));
        }

        mcp.move(MOVE_DIRECTION.RIGHT);

        assertEquals("Active cells", 2, mcp.getPlaylingField().getActiveCells());

        for (int testColumn = 0; testColumn < gridSize; testColumn++) {

            if (testColumn < gridSize - 2) {
                assertEquals("Cell [" + row + "|" + testColumn + "] has value", false, mcp.getPlaylingField().cellHasValue(row, testColumn));
            } else if (testColumn == gridSize - 1) {
                assertEquals("Cell [" + row + "|" + testColumn + "]", testValue1, mcp.getPlaylingField().getCellValue(row, testColumn));
            } else {
                assertEquals("Cell [" + row + "|" + testColumn + "]", testValue2 + testValue3, mcp.getPlaylingField().getCellValue(row, testColumn));
            }
        }


        mcp.move(MOVE_DIRECTION.RIGHT);

        assertEquals("Active cells", 1, mcp.getPlaylingField().getActiveCells());

        for (int testColumn = 0; testColumn < gridSize; testColumn++) {

            if (testColumn < gridSize - 1) {
                assertEquals("Cell [" + row + "|" + testColumn + "] has value", false, mcp.getPlaylingField().cellHasValue(row, testColumn));
            } else {
                assertEquals("Cell [" + row + "|" + testColumn + "]", testValue1 + testValue2 + testValue3, mcp.getPlaylingField().getCellValue(row, testColumn));
            }
        }
    }

    public void testMoveRightFullRowNoMerge() throws Exception {

        int gridSize = mcp.getPlaylingField().getGridSize();
        int row = 1;
        int column = 0;
        int testValue = mcp.DEFAULT_START_VALUE;

        mcp.getPlaylingField().reset();

        //fill the whole row with different values
        for (column = 0; column < gridSize; column++, testValue++) {
            mcp.getPlaylingField().setCellValue(row, column, testValue);
            assertEquals("Active cells", column + 1, mcp.getPlaylingField().getActiveCells());
            assertEquals("Cell [" + row + "|" + column + "]", testValue, mcp.getPlaylingField().getCellValue(row, column));
        }

        mcp.move(MOVE_DIRECTION.RIGHT);

        assertEquals("Active cells", column, mcp.getPlaylingField().getActiveCells());

        for (column = gridSize - 1; column <= 0; column--, testValue--) {
            assertEquals("Cell [" + row + "|" + column + "]", testValue, mcp.getPlaylingField().getCellValue(row, column));
        }
    }

    public void testMoveUp2CellsNoMerge() throws Exception {

        int row = 3;
        int column = 1;
        int testValue1 = 84;
        int testValue2 = 42;

        mcp.getPlaylingField().reset();

        mcp.getPlaylingField().setCellValue(row, column, testValue1);
        assertEquals("Active cells", 1, mcp.getPlaylingField().getActiveCells());
        assertEquals("Cell [" + row + "|" + column + "]", testValue1, mcp.getPlaylingField().getCellValue(row, column));

        column += 2;
        mcp.getPlaylingField().setCellValue(row, column, testValue2);
        assertEquals("Active cells", 2, mcp.getPlaylingField().getActiveCells());
        assertEquals("Cell [" + row + "|" + column + "]", testValue2, mcp.getPlaylingField().getCellValue(row, column));

        mcp.move(MOVE_DIRECTION.UP);

        assertEquals("Active cells", 2, mcp.getPlaylingField().getActiveCells());

        row = 0;
        assertEquals("Cell [" + row + "|" + column + "] has value", true, mcp.getPlaylingField().cellHasValue(row, column));
        assertEquals("Cell [" + row + "|" + column + "]", testValue2, mcp.getPlaylingField().getCellValue(row, column));

        column -= 2;
        assertEquals("Cell [" + row + "|" + column + "] has value", true, mcp.getPlaylingField().cellHasValue(row, column));
        assertEquals("Cell [" + row + "|" + column + "]", testValue1, mcp.getPlaylingField().getCellValue(row, column));
    }

    public void testMoveUp3Cells1Merge() throws Exception {


        int testValue1 = 1337;
        int testValue2 = 42;
        int testValue3 = 42;

        mcp.getPlaylingField().reset();

        mcp.getPlaylingField().setCellValue(3, 0, testValue1);
        mcp.getPlaylingField().setCellValue(3, 2, testValue2);
        mcp.getPlaylingField().setCellValue(1, 2, testValue3);

        assertEquals("Active cells", 3, mcp.getPlaylingField().getActiveCells());

        mcp.move(MOVE_DIRECTION.UP);

        assertEquals("Active cells", 2, mcp.getPlaylingField().getActiveCells());

        assertEquals("Cell [0|0]", testValue1, mcp.getPlaylingField().getCellValue(0, 0));
        assertEquals("Cell [0|2]", testValue2 + testValue3, mcp.getPlaylingField().getCellValue(0, 2));
    }

    public void testMoveUp3CellsNoMerge() throws Exception {

        int testValue1 = 1337;
        int testValue2 = 42;
        int testValue3 = 43;

        mcp.getPlaylingField().reset();

        mcp.getPlaylingField().setCellValue(3, 0, testValue1);
        mcp.getPlaylingField().setCellValue(1, 2, testValue2);
        mcp.getPlaylingField().setCellValue(3, 2, testValue3);

        assertEquals("Active cells before move", 3, mcp.getPlaylingField().getActiveCells());

        mcp.move(MOVE_DIRECTION.UP);

        assertEquals("Active cells after move", 3, mcp.getPlaylingField().getActiveCells());

        assertEquals("Cell [0|0]", testValue1, mcp.getPlaylingField().getCellValue(0, 0));
        assertEquals("Cell [0|2]", testValue2, mcp.getPlaylingField().getCellValue(0, 2));
        assertEquals("Cell [1|2]", testValue3, mcp.getPlaylingField().getCellValue(1, 2));
    }

    public void testMoveDown5Cells1Merge() throws Exception {

        int testValue1 = 1337;
        int testValue2 = 42;
        int testValue3 = 42;
        int testValue4 = 1234;
        int testValue5 = 589;

        mcp.getPlaylingField().reset();

        mcp.getPlaylingField().setCellValue(0, 0, testValue1);
        mcp.getPlaylingField().setCellValue(1, 0, testValue2);
        mcp.getPlaylingField().setCellValue(2, 0, testValue3);
        mcp.getPlaylingField().setCellValue(3, 0, testValue4);

        mcp.getPlaylingField().setCellValue(1, 2, testValue5);

        assertEquals("Active cells before move", 5, mcp.getPlaylingField().getActiveCells());

        mcp.move(MOVE_DIRECTION.DOWN);

        assertEquals("Active cells after move", 4, mcp.getPlaylingField().getActiveCells());

        assertEquals("Cell [3|0]", testValue4, mcp.getPlaylingField().getCellValue(3, 0));
        assertEquals("Cell [2|0]", testValue2 + testValue3, mcp.getPlaylingField().getCellValue(2, 0));
        assertEquals("Cell [1|0]", testValue1, mcp.getPlaylingField().getCellValue(1, 0));
        assertEquals("Cell [1|0]", testValue5, mcp.getPlaylingField().getCellValue(3, 2));
    }

    public void testMoveLeft5Cells2Merge() throws Exception {

        int testValue1 = 1337;
        int testValue2 = 1337;
        int testValue3 = 42;
        int testValue4 = 42;
        int testValue5 = 589;

        mcp.getPlaylingField().reset();

        mcp.getPlaylingField().setCellValue(0, 1, testValue1);
        mcp.getPlaylingField().setCellValue(0, 3, testValue2);
        mcp.getPlaylingField().setCellValue(2, 0, testValue3);
        mcp.getPlaylingField().setCellValue(2, 3, testValue4);
        mcp.getPlaylingField().setCellValue(3, 2, testValue5);

        assertEquals("Active cells before move", 5, mcp.getPlaylingField().getActiveCells());

        mcp.move(MOVE_DIRECTION.LEFT);

        assertEquals("Active cells after move", 3, mcp.getPlaylingField().getActiveCells());

        assertEquals("Cell [0|0]", testValue1 + testValue2, mcp.getPlaylingField().getCellValue(0, 0));
        assertEquals("Cell [2|0]", testValue3 + testValue4, mcp.getPlaylingField().getCellValue(2, 0));
        assertEquals("Cell [3|0]", testValue5, mcp.getPlaylingField().getCellValue(3, 0));
    }

    public void testMoveLeft4Cells2MergeInSameRow() throws Exception {

        int testValue1 = 1337;
        int testValue2 = 1337;
        int testValue3 = 42;
        int testValue4 = 42;

        mcp.getPlaylingField().reset();

        mcp.getPlaylingField().setCellValue(0, 0, testValue1);
        mcp.getPlaylingField().setCellValue(0, 1, testValue2);
        mcp.getPlaylingField().setCellValue(0, 2, testValue3);
        mcp.getPlaylingField().setCellValue(0, 3, testValue4);

        assertEquals("Active cells before move", 4, mcp.getPlaylingField().getActiveCells());

        mcp.move(MOVE_DIRECTION.LEFT);

        assertEquals("Active cells after move", 2, mcp.getPlaylingField().getActiveCells());

        assertEquals("Cell [0|0]", testValue1 + testValue2, mcp.getPlaylingField().getCellValue(0, 0));
        assertEquals("Cell [0|1]", testValue3 + testValue4, mcp.getPlaylingField().getCellValue(0, 1));
    }
}
