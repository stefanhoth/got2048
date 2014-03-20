package de.stefanhoth.android.got2048.logic;

import android.annotation.TargetApi;
import android.os.Build;

import junit.framework.Test;
import junit.framework.TestSuite;

import de.stefanhoth.android.got2048.logic.model.CellTest;
import de.stefanhoth.android.got2048.logic.model.GridTest;

/**
 * TODO describe class
 *
 * @author Stefan Hoth <sh@jnamic.com>
 *         date: 20.03.14 21:55
 * @since TODO add version
 */
public class LogicTestSuite extends TestSuite {

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static Test suite() {

        Class[] testClasses = {
                CellTest.class,
                GridTest.class,
                MCPTest.class
        };

        TestSuite suite = new TestSuite(testClasses);
        suite.setName(LogicTestSuite.class.getSimpleName());

        return suite;
    }

}
