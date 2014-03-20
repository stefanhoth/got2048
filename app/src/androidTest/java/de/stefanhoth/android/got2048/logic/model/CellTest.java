package de.stefanhoth.android.got2048.logic.model;

import android.test.AndroidTestCase;

import junit.framework.TestCase;

/**
 * TODO describe class
 *
 * @author Stefan Hoth <sh@jnamic.com>
 *         date: 20.03.14 20:52
 * @since TODO add version
 */
public class CellTest extends AndroidTestCase {


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
}
