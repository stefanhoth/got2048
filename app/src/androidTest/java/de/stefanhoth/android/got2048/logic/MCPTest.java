package de.stefanhoth.android.got2048.logic;

import junit.framework.TestCase;

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
}
