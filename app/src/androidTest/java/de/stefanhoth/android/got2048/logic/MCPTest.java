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

        assertNotNull(mcp.getPlaylingField());
    }

    public void testStartCellsAdded() throws Exception {

        mcp.addStartCells();

        assertTrue(mcp.getPlaylingField().getActiveCells() == mcp.DEFAULT_START_FIELDS);
    }

    public void testMoveRightWorksSingle() throws Exception {

        int gridSize = mcp.getPlaylingField().getGridSize();
        int row = 0;
        int column = 0;

        mcp.getPlaylingField().reset();
        mcp.getPlaylingField().getCell(row, column).setValue(mcp.DEFAULT_START_VALUE);
        assertTrue(mcp.getPlaylingField().getActiveCells() == 1);
        assertTrue(mcp.getPlaylingField().getCell(row, column).getValue().equals(mcp.DEFAULT_START_VALUE));

        Cell cell;
        for (int testColumn = 0; testColumn < gridSize; testColumn++) {
            if (testColumn == column) {
                continue;
            }
            cell = mcp.getPlaylingField().getCell(row, testColumn);
            assertFalse(cell.hasValue());
        }

        mcp.move(MOVE_DIRECTION.RIGHT);

        for (int testColumn = 0; testColumn < gridSize; testColumn++) {

            cell = mcp.getPlaylingField().getCell(row, testColumn);

            if (testColumn < gridSize - 1) {
                assertFalse(cell.hasValue());
            } else {
                assertTrue(cell.getValue().equals(mcp.DEFAULT_START_VALUE));
            }
        }
    }

    public void testMoveRightWorksSingle_0_1() throws Exception {

        int gridSize = mcp.getPlaylingField().getGridSize();
        int row = 0;
        int column = 1;

        mcp.getPlaylingField().reset();
        mcp.getPlaylingField().getCell(row, column).setValue(mcp.DEFAULT_START_VALUE);
        assertTrue(mcp.getPlaylingField().getActiveCells() == 1);
        assertTrue(mcp.getPlaylingField().getCell(row, column).getValue().equals(mcp.DEFAULT_START_VALUE));

        Cell cell;
        for (int testColumn = 0; testColumn < gridSize; testColumn++) {
            if (testColumn == column) {
                continue;
            }
            cell = mcp.getPlaylingField().getCell(row, testColumn);

            assertFalse(cell.hasValue());
        }

        mcp.move(MOVE_DIRECTION.RIGHT);
        assertFalse(mcp.getPlaylingField().getCell(row, column).hasValue());

        for (int testColumn = 0; testColumn < gridSize; testColumn++) {

            cell = mcp.getPlaylingField().getCell(row, testColumn);

            if (testColumn < gridSize - 1) {
                assertFalse(cell.hasValue());
            } else {
                assertTrue(cell.getValue().equals(mcp.DEFAULT_START_VALUE));
            }
        }
    }

    public void testMoveRightWorksSingle_0_4() throws Exception {

        int gridSize = mcp.getPlaylingField().getGridSize();
        int row = 0;
        int column = gridSize - 1;

        mcp.getPlaylingField().reset();
        mcp.getPlaylingField().getCell(row, column).setValue(mcp.DEFAULT_START_VALUE);
        assertTrue(mcp.getPlaylingField().getActiveCells() == 1);
        assertTrue(mcp.getPlaylingField().getCell(row, column).getValue().equals(mcp.DEFAULT_START_VALUE));

        Cell cell;
        for (int testColumn = 0; testColumn < gridSize; testColumn++) {
            if (testColumn == column) {
                continue;
            }

            cell = mcp.getPlaylingField().getCell(row, testColumn);
            assertFalse(cell.hasValue());
        }

        mcp.move(MOVE_DIRECTION.RIGHT);

        for (int testColumn = 0; testColumn < gridSize; testColumn++) {

            cell = mcp.getPlaylingField().getCell(row, testColumn);

            if (testColumn < gridSize - 1) {
                assertFalse(cell.hasValue());
            } else {
                assertTrue(cell.getValue().equals(mcp.DEFAULT_START_VALUE));
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
        assertTrue(mcp.getPlaylingField().getActiveCells() == 1);
        assertTrue(mcp.getPlaylingField().getCell(row, column).getValue().equals(testValue1));

        column += 2;
        mcp.getPlaylingField().getCell(row, column).setValue(testValue2);
        assertTrue(mcp.getPlaylingField().getActiveCells() == 2);
        assertTrue(mcp.getPlaylingField().getCell(row, column).getValue().equals(testValue2));

        Cell cell;
        for (int testColumn = 0; testColumn < gridSize; testColumn++) {
            if (testColumn == column || testColumn == column - 2) {
                continue;
            }

            cell = mcp.getPlaylingField().getCell(row, testColumn);
            assertFalse(cell.hasValue());
        }

        mcp.move(MOVE_DIRECTION.RIGHT);

        assertTrue(mcp.getPlaylingField().getActiveCells() == 2);

        for (int testColumn = 0; testColumn < gridSize; testColumn++) {

            cell = mcp.getPlaylingField().getCell(row, testColumn);

            if (testColumn < gridSize - 2) {
                assertFalse(cell.hasValue());
            } else if (testColumn == gridSize - 2) {
                assertTrue(cell.getValue().equals(testValue1));
            } else if (testColumn == gridSize - 1) {
                assertTrue(cell.getValue().equals(testValue2));
            }
        }
    }

    public void testMoveRightDoubleWithMerge() throws Exception {

        int gridSize = mcp.getPlaylingField().getGridSize();
        int row = 1;
        int column = 0;
        int testValue1 = mcp.DEFAULT_START_VALUE;
        int testValue2 = mcp.DEFAULT_START_VALUE;

        mcp.getPlaylingField().reset();
        mcp.getPlaylingField().getCell(row, column).setValue(testValue1);
        assertTrue(mcp.getPlaylingField().getActiveCells() == 1);
        assertTrue(mcp.getPlaylingField().getCell(row, column).getValue().equals(testValue1));

        column += 2;
        mcp.getPlaylingField().getCell(row, column).setValue(testValue2);
        assertTrue(mcp.getPlaylingField().getActiveCells() == 2);
        assertTrue(mcp.getPlaylingField().getCell(row, column).getValue().equals(testValue2));

        Cell cell;
        for (int testColumn = 0; testColumn < gridSize; testColumn++) {
            if (testColumn == column || testColumn == column - 2) {
                continue;
            }

            cell = mcp.getPlaylingField().getCell(row, testColumn);
            assertFalse(cell.hasValue());
        }

        mcp.move(MOVE_DIRECTION.RIGHT);
        assertTrue(mcp.getPlaylingField().getActiveCells() == 1);

        for (int testColumn = 0; testColumn < gridSize; testColumn++) {

            cell = mcp.getPlaylingField().getCell(row, testColumn);

            if (testColumn < gridSize - 1) {
                assertFalse(cell.hasValue());
            } else {
                assertTrue(cell.getValue().equals(testValue1 + testValue2));
            }
        }
    }
}
