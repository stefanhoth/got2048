package de.stefanhoth.android.got2048;

import android.app.Application;

import de.stefanhoth.android.got2048.logic.MCP;

/**
 * TODO describe class
 *
 * @author Stefan Hoth <sh@jnamic.com>
 *         date: 30.03.2014 21:36
 * @since TODO add version
 */
public class Got2048App extends Application {

    MCP masterControlProgram;
    private int mGridsize = 4;

    @Override
    public void onCreate() {
        super.onCreate();

        masterControlProgram = new MCP(mGridsize);
        masterControlProgram.addStartCells();
    }

    public MCP getMCP() {
        return masterControlProgram;
    }

}
