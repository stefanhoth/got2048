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

    public void testGetValue() throws Exception {

        Cell cell = new Cell();
        assertNull(cell.getValue());
    }

    public void testSetValue() throws Exception {

        Cell cell = new Cell();

        Integer testValue = 1;

        cell.setValue(testValue);

        assertTrue(cell.hasValue());
        assertEquals(testValue, cell.getValue());
    }

    public void testHasValue() throws Exception {

        Cell cell = new Cell();
        assertFalse(cell.hasValue());

        cell.setValue(1);
        assertTrue(cell.hasValue());
    }

    public void testEmptyField() throws Exception {
        Cell cell = new Cell();
        cell.setValue(1);
        cell.emptyField();

        assertFalse(cell.hasValue());
    }

    public void testEquals() throws Exception {

        int row = 1;
        int column = 3;
        Cell cell1 = new Cell(row, column);
        Cell cell2 = new Cell();
        cell2.setRow(row);
        cell2.setColumn(column);

        assertEquals(cell1, cell2);

        int testValue = 42;
        cell1.setValue(testValue);
        assertFalse(cell1.equals(cell2));

        cell2.setValue(testValue);
        assertTrue(cell1.equals(cell2));
    }
}
