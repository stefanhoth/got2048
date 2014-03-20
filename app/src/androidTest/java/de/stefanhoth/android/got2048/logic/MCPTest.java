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

        int row = 0;
        int column = 0;

        mcp.getPlaylingField().reset();
        mcp.getPlaylingField().getCell(row, column).setValue(mcp.DEFAULT_START_VALUE);
        assertTrue(mcp.getPlaylingField().getActiveCells() == 1);
        assertTrue(mcp.getPlaylingField().getCell(row, column).getValue().equals(mcp.DEFAULT_START_VALUE));

        Cell cell;
        for (int testColumn = 1; testColumn < mcp.getPlaylingField().getGridSize(); testColumn++) {
            cell = mcp.getPlaylingField().getCell(row, testColumn);
            assertFalse(cell.hasValue());
        }

        mcp.move(MOVE_DIRECTION.RIGHT);
        assertFalse(mcp.getPlaylingField().getCell(row, column).hasValue());

        for (int testColumn = 1; testColumn < mcp.getPlaylingField().getGridSize(); testColumn++) {

            cell = mcp.getPlaylingField().getCell(row, testColumn);

            if (testColumn < mcp.getPlaylingField().getGridSize() - 1) {
                assertFalse(cell.hasValue());
            } else {
                assertTrue(cell.getValue().equals(mcp.DEFAULT_START_VALUE));
            }
        }
    }
}
