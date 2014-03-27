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
        mcp.getPlaylingField().getCell(row, column).setValue(MCP.DEFAULT_START_VALUE);
        assertEquals("Active cells", 1, mcp.getPlaylingField().getActiveCells());
        assertEquals("Cell [" + row + "|" + column + "]", MCP.DEFAULT_START_VALUE, (int) mcp.getPlaylingField().getCell(row, column).getValue());

        Cell cell;
        for (int testColumn = 0; testColumn < gridSize; testColumn++) {
            if (testColumn == column) {
                continue;
            }
            cell = mcp.getPlaylingField().getCell(row, testColumn);
            assertEquals("Cell [" + row + "|" + testColumn + "] has value", false, cell.hasValue());
        }

        mcp.move(MOVE_DIRECTION.RIGHT);

        for (int testColumn = 0; testColumn < gridSize; testColumn++) {

            cell = mcp.getPlaylingField().getCell(row, testColumn);

            if (testColumn < gridSize - 1) {
                assertEquals("Cell [" + row + "|" + testColumn + "] has value", false, cell.hasValue());
            } else {
                assertEquals("Cell [" + row + "|" + testColumn + "]", MCP.DEFAULT_START_VALUE, (int) cell.getValue());
            }
        }
    }

    public void testMoveRightWorksSingle_0_1() throws Exception {

        int gridSize = mcp.getPlaylingField().getGridSize();
        int row = 0;
        int column = 1;

        mcp.getPlaylingField().reset();
        mcp.getPlaylingField().getCell(row, column).setValue(MCP.DEFAULT_START_VALUE);
        assertEquals("Active cells", 1, mcp.getPlaylingField().getActiveCells());
        assertEquals("Cell [" + row + "|" + column + "]", MCP.DEFAULT_START_VALUE, (int) mcp.getPlaylingField().getCell(row, column).getValue());

        Cell cell;
        for (int testColumn = 0; testColumn < gridSize; testColumn++) {
            if (testColumn == column) {
                continue;
            }
            cell = mcp.getPlaylingField().getCell(row, testColumn);

            assertEquals("Cell [" + row + "|" + testColumn + "] has value", false, cell.hasValue());
        }

        mcp.move(MOVE_DIRECTION.RIGHT);

        assertEquals("Cell [" + row + "|" + column + "] has value", false, mcp.getPlaylingField().getCell(row, column).hasValue());

        for (int testColumn = 0; testColumn < gridSize; testColumn++) {

            cell = mcp.getPlaylingField().getCell(row, testColumn);

            if (testColumn < gridSize - 1) {
                assertEquals("Cell [" + row + "|" + testColumn + "] has value", false, cell.hasValue());
            } else {
                assertEquals("Cell [" + row + "|" + testColumn + "]", MCP.DEFAULT_START_VALUE, (int) cell.getValue());
            }
        }
    }

    public void testMoveRightWorksSingle_0_4() throws Exception {

        int gridSize = mcp.getPlaylingField().getGridSize();
        int row = 0;
        int column = gridSize - 1;

        mcp.getPlaylingField().reset();
        mcp.getPlaylingField().getCell(row, column).setValue(MCP.DEFAULT_START_VALUE);
        assertEquals("Active cells", 1, mcp.getPlaylingField().getActiveCells());
        assertEquals("Cell [" + row + "|" + column + "]", MCP.DEFAULT_START_VALUE, (int) mcp.getPlaylingField().getCell(row, column).getValue());

        Cell cell;
        for (int testColumn = 0; testColumn < gridSize; testColumn++) {
            if (testColumn == column) {
                continue;
            }

            cell = mcp.getPlaylingField().getCell(row, testColumn);
            assertEquals("Cell [" + row + "|" + testColumn + "] has value", false, cell.hasValue());
        }

        mcp.move(MOVE_DIRECTION.RIGHT);

        for (int testColumn = 0; testColumn < gridSize; testColumn++) {

            cell = mcp.getPlaylingField().getCell(row, testColumn);

            if (testColumn < gridSize - 1) {
                assertEquals("Cell [" + row + "|" + testColumn + "] has value", false, cell.hasValue());
            } else {
                assertEquals("Cell [" + row + "|" + testColumn + "]", MCP.DEFAULT_START_VALUE, (int) cell.getValue());
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
        mcp.getPlaylingField().getCell(row, column).setValue(testValue1);
        assertEquals("Active cells", 1, mcp.getPlaylingField().getActiveCells());
        assertEquals("Cell [" + row + "|" + column + "]", testValue1, (int) mcp.getPlaylingField().getCell(row, column).getValue());

        column += 2;
        mcp.getPlaylingField().getCell(row, column).setValue(testValue2);
        assertEquals("Active cells", 2, mcp.getPlaylingField().getActiveCells());
        assertEquals("Cell [" + row + "|" + column + "]", testValue2, (int) mcp.getPlaylingField().getCell(row, column).getValue());

        Cell cell;
        for (int testColumn = 0; testColumn < gridSize; testColumn++) {
            if (testColumn == column || testColumn == column - 2) {
                continue;
            }

            cell = mcp.getPlaylingField().getCell(row, testColumn);
            assertEquals("Cell [" + row + "|" + testColumn + "] has value", false, cell.hasValue());
        }

        mcp.move(MOVE_DIRECTION.RIGHT);

        assertEquals("Active cells", 2, mcp.getPlaylingField().getActiveCells());

        for (int testColumn = 0; testColumn < gridSize; testColumn++) {

            cell = mcp.getPlaylingField().getCell(row, testColumn);

            if (testColumn < gridSize - 2) {
                assertEquals("Cell [" + row + "|" + testColumn + "] has value", false, cell.hasValue());
            } else if (testColumn == gridSize - 2) {
                assertEquals("Cell [" + row + "|" + testColumn + "]", testValue1, (int) cell.getValue());
            } else if (testColumn == gridSize - 1) {
                assertEquals("Cell [" + row + "|" + testColumn + "]", testValue2, (int) cell.getValue());
            }
        }
    }

    public void testMoveRightDoubleWithMerge() throws Exception {

        int gridSize = mcp.getPlaylingField().getGridSize();
        int row = 1;
        int column = 0;
        int testValue1 = MCP.DEFAULT_START_VALUE;
        int testValue2 = MCP.DEFAULT_START_VALUE;

        mcp.getPlaylingField().reset();
        mcp.getPlaylingField().getCell(row, column).setValue(testValue1);
        assertEquals("Active cells", 1, mcp.getPlaylingField().getActiveCells());
        assertEquals("Cell [" + row + "|" + column + "]", testValue1, (int) mcp.getPlaylingField().getCell(row, column).getValue());

        column += 2;
        mcp.getPlaylingField().getCell(row, column).setValue(testValue2);
        assertEquals("Active cells", 2, mcp.getPlaylingField().getActiveCells());
        assertEquals("Cell [" + row + "|" + column + "]", testValue2, (int) mcp.getPlaylingField().getCell(row, column).getValue());

        Cell cell;
        for (int testColumn = 0; testColumn < gridSize; testColumn++) {
            if (testColumn == column || testColumn == column - 2) {
                continue;
            }

            cell = mcp.getPlaylingField().getCell(row, testColumn);
            assertEquals("Cell [" + row + "|" + testColumn + "] has value", false, cell.hasValue());
        }

        mcp.move(MOVE_DIRECTION.RIGHT);
        assertEquals("Active cells", 1, mcp.getPlaylingField().getActiveCells());

        for (int testColumn = 0; testColumn < gridSize; testColumn++) {

            cell = mcp.getPlaylingField().getCell(row, testColumn);

            if (testColumn < gridSize - 1) {
                assertEquals("Cell [" + row + "|" + testColumn + "] has value", false, cell.hasValue());
            } else {
                assertEquals("Cell [" + row + "|" + testColumn + "]", testValue1 + testValue2, (int) cell.getValue());
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
        mcp.getPlaylingField().getCell(row, column).setValue(testValue1);
        assertEquals("Active cells", 1, mcp.getPlaylingField().getActiveCells());
        assertEquals("Cell [" + row + "|" + column + "]", testValue1, (int) mcp.getPlaylingField().getCell(row, column).getValue());

        column += 1;
        mcp.getPlaylingField().getCell(row, column).setValue(testValue2);
        assertEquals("Active cells", 2, mcp.getPlaylingField().getActiveCells());
        assertEquals("Cell [" + row + "|" + column + "]", testValue2, (int) mcp.getPlaylingField().getCell(row, column).getValue());

        column += 1;
        mcp.getPlaylingField().getCell(row, column).setValue(testValue3);
        assertEquals("Active cells", 3, mcp.getPlaylingField().getActiveCells());
        assertEquals("Cell [" + row + "|" + column + "]", testValue3, (int) mcp.getPlaylingField().getCell(row, column).getValue());

        Cell cell;
        for (int testColumn = 0; testColumn < gridSize; testColumn++) {
            if (testColumn == column || testColumn == column - 1 || testColumn == column - 2) {
                continue;
            }

            cell = mcp.getPlaylingField().getCell(row, testColumn);
            assertEquals("Cell [" + row + "|" + testColumn + "] has value", false, cell.hasValue());
        }

        mcp.move(MOVE_DIRECTION.RIGHT);

        assertEquals("Active cells", 3, mcp.getPlaylingField().getActiveCells());

        for (int testColumn = 0; testColumn < gridSize; testColumn++) {

            cell = mcp.getPlaylingField().getCell(row, testColumn);

            if (testColumn < gridSize - 3) {
                assertEquals("Cell [" + row + "|" + testColumn + "] has value", cell.hasValue(), mcp.getPlaylingField().getCell(row, testColumn).hasValue());
            } else if (testColumn == gridSize - 3) {
                assertEquals("Cell [" + row + "|" + testColumn + "]", testValue1, (int) mcp.getPlaylingField().getCell(row, testColumn).getValue());
            } else if (testColumn == gridSize - 2) {
                assertEquals("Cell [" + row + "|" + testColumn + "]", testValue2, (int) mcp.getPlaylingField().getCell(row, testColumn).getValue());
            } else if (testColumn == gridSize - 1) {
                assertEquals("Cell [" + row + "|" + testColumn + "]", testValue3, (int) mcp.getPlaylingField().getCell(row, testColumn).getValue());
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
        mcp.getPlaylingField().getCell(row, column).setValue(testValue1);
        assertEquals("Active cells", 1, mcp.getPlaylingField().getActiveCells());
        assertEquals("Cell [" + row + "|" + column + "]", testValue1, (int) mcp.getPlaylingField().getCell(row, column).getValue());

        column += 1;
        mcp.getPlaylingField().getCell(row, column).setValue(testValue2);
        assertEquals("Active cells", 2, mcp.getPlaylingField().getActiveCells());
        assertEquals("Cell [" + row + "|" + column + "]", testValue2, (int) mcp.getPlaylingField().getCell(row, column).getValue());

        column += 1;
        mcp.getPlaylingField().getCell(row, column).setValue(testValue3);
        assertEquals("Active cells", 3, mcp.getPlaylingField().getActiveCells());
        assertEquals("Cell [" + row + "|" + column + "]", testValue3, (int) mcp.getPlaylingField().getCell(row, column).getValue());

        Cell cell;
        for (int testColumn = 0; testColumn < gridSize; testColumn++) {
            if (testColumn == column || testColumn == column - 1 || testColumn == column - 2) {
                continue;
            }

            cell = mcp.getPlaylingField().getCell(row, testColumn);
            assertEquals("Cell [" + row + "|" + testColumn + "] has value", false, cell.hasValue());
        }

        mcp.move(MOVE_DIRECTION.RIGHT);

        assertEquals("Active cells", 2, mcp.getPlaylingField().getActiveCells());

        for (int testColumn = 0; testColumn < gridSize; testColumn++) {

            cell = mcp.getPlaylingField().getCell(row, testColumn);

            if (testColumn < gridSize - 3) {
                assertEquals("Cell [" + row + "|" + testColumn + "] has value", false, cell.hasValue());
            } else if (testColumn == gridSize - 2) {
                assertEquals("Cell [" + row + "|" + testColumn + "]", testValue1, (int) mcp.getPlaylingField().getCell(row, testColumn).getValue());
            } else if (testColumn == gridSize - 1) {
                assertEquals("Cell [" + row + "|" + testColumn + "]", testValue2 + testValue3, (int) mcp.getPlaylingField().getCell(row, testColumn).getValue());
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
        mcp.getPlaylingField().getCell(row, column).setValue(testValue1);
        assertEquals("Active cells", 1, mcp.getPlaylingField().getActiveCells());
        assertEquals("Cell [" + row + "|" + column + "]", testValue1, (int) mcp.getPlaylingField().getCell(row, column).getValue());

        column += 1;
        mcp.getPlaylingField().getCell(row, column).setValue(testValue2);
        assertEquals("Active cells", 2, mcp.getPlaylingField().getActiveCells());
        assertEquals("Cell [" + row + "|" + column + "]", testValue2, (int) mcp.getPlaylingField().getCell(row, column).getValue());

        column += 1;
        mcp.getPlaylingField().getCell(row, column).setValue(testValue3);
        assertEquals("Active cells", 3, mcp.getPlaylingField().getActiveCells());
        assertEquals("Cell [" + row + "|" + column + "]", testValue3, (int) mcp.getPlaylingField().getCell(row, column).getValue());


        Cell cell;
        for (int testColumn = 0; testColumn < gridSize; testColumn++) {
            if (testColumn == column || testColumn == column - 1 || testColumn == column - 2) {
                continue;
            }

            cell = mcp.getPlaylingField().getCell(row, testColumn);

            assertEquals("Cell [" + row + "|" + testColumn + "] has value", false, cell.hasValue());
        }

        mcp.move(MOVE_DIRECTION.RIGHT);

        assertEquals("Active cells", 2, mcp.getPlaylingField().getActiveCells());

        for (int testColumn = 0; testColumn < gridSize; testColumn++) {

            cell = mcp.getPlaylingField().getCell(row, testColumn);

            if (testColumn < gridSize - 3) {
                assertEquals("Cell [" + row + "|" + testColumn + "] has value", false, cell.hasValue());
            } else if (testColumn == gridSize - 2) {
                assertEquals("Cell [" + row + "|" + testColumn + "]", testValue1, (int) mcp.getPlaylingField().getCell(row, testColumn).getValue());
            } else if (testColumn == gridSize - 1) {
                assertEquals("Cell [" + row + "|" + testColumn + "]", testValue2 + testValue3, (int) mcp.getPlaylingField().getCell(row, testColumn).getValue());
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
        mcp.getPlaylingField().getCell(row, column).setValue(testValue1);
        assertEquals("Active cells", 1, mcp.getPlaylingField().getActiveCells());
        assertEquals("Cell [" + row + "|" + column + "]", testValue1, (int) mcp.getPlaylingField().getCell(row, column).getValue());

        column += 1;
        mcp.getPlaylingField().getCell(row, column).setValue(testValue2);
        assertEquals("Active cells", 2, mcp.getPlaylingField().getActiveCells());
        assertEquals("Cell [" + row + "|" + column + "]", testValue2, (int) mcp.getPlaylingField().getCell(row, column).getValue());

        column += 1;
        mcp.getPlaylingField().getCell(row, column).setValue(testValue3);
        assertEquals("Active cells", 3, mcp.getPlaylingField().getActiveCells());
        assertEquals("Cell [" + row + "|" + column + "]", testValue3, (int) mcp.getPlaylingField().getCell(row, column).getValue());

        Cell cell;
        for (int testColumn = 0; testColumn < gridSize; testColumn++) {
            if (testColumn == column || testColumn == column - 1 || testColumn == column - 2) {
                continue;
            }

            cell = mcp.getPlaylingField().getCell(row, testColumn);
            assertEquals("Cell [" + row + "|" + testColumn + "] has value", false, cell.hasValue());
        }

        mcp.move(MOVE_DIRECTION.RIGHT);

        assertEquals("Active cells", 2, mcp.getPlaylingField().getActiveCells());

        for (int testColumn = 0; testColumn < gridSize; testColumn++) {

            cell = mcp.getPlaylingField().getCell(row, testColumn);

            if (testColumn < gridSize - 2) {
                assertEquals("Cell [" + row + "|" + testColumn + "] has value", false, cell.hasValue());
            } else if (testColumn == gridSize - 1) {
                assertEquals("Cell [" + row + "|" + testColumn + "] has value", true, cell.hasValue());
                assertEquals("Cell [" + row + "|" + testColumn + "]", testValue1, (int) mcp.getPlaylingField().getCell(row, testColumn).getValue());
            } else {
                assertEquals("Cell [" + row + "|" + testColumn + "] has value", true, cell.hasValue());
                assertEquals("Cell [" + row + "|" + testColumn + "]", testValue2 + testValue3, (int) mcp.getPlaylingField().getCell(row, testColumn).getValue());
            }
        }


        mcp.move(MOVE_DIRECTION.RIGHT);

        assertEquals("Active cells", 1, mcp.getPlaylingField().getActiveCells());

        for (int testColumn = 0; testColumn < gridSize; testColumn++) {

            cell = mcp.getPlaylingField().getCell(row, testColumn);

            if (testColumn < gridSize - 1) {
                assertEquals("Cell [" + row + "|" + testColumn + "] has value", false, cell.hasValue());
            } else {
                assertEquals("Cell [" + row + "|" + testColumn + "] has value", true, cell.hasValue());
                assertEquals("Cell [" + row + "|" + testColumn + "]", testValue1 + testValue2 + testValue3, (int) mcp.getPlaylingField().getCell(row, testColumn).getValue());
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
            mcp.getPlaylingField().getCell(row, column).setValue(testValue);
            assertEquals("Active cells", column + 1, mcp.getPlaylingField().getActiveCells());
            assertEquals("Cell [" + row + "|" + column + "]", testValue, (int) mcp.getPlaylingField().getCell(row, column).getValue());
        }

        mcp.move(MOVE_DIRECTION.RIGHT);

        assertEquals("Active cells", column, mcp.getPlaylingField().getActiveCells());

        for (column = gridSize - 1; column <= 0; column--, testValue--) {
            assertEquals("Cell [" + row + "|" + column + "]", testValue, (int) mcp.getPlaylingField().getCell(row, column).getValue());
        }
    }
}
