package de.stefanhoth.android.got2048.logic.model;

import junit.framework.TestCase;

/**
 * TODO describe class
 *
 * @author Stefan Hoth <sh@jnamic.com>
 *         date: 20.03.14 20:52
 * @since TODO add version
 */
public class CellTest extends TestCase {

    public CellTest() {
        this(CellTest.class.getSimpleName());
    }

    public CellTest(String name) {
        super(name);
    }

    public void testEquals() throws Exception {

        int row = 1;
        int column = 3;
        Cell cell1 = new Cell(row, column);
        Cell cell2 = new Cell(row, column);

        assertEquals(cell1, cell2);
    }
}
