package de.stefanhoth.android.got2048;

import android.app.Application;
import android.os.Handler;

import java.util.Random;

import de.stefanhoth.android.got2048.logic.MCP;
import de.stefanhoth.android.got2048.logic.model.MOVE_DIRECTION;

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
    private Random mRandom;

    @Override
    public void onCreate() {
        super.onCreate();

        masterControlProgram = new MCP(mGridsize);
        masterControlProgram.addStartCells();

        mRandom = new Random();

        final Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                simulateMove();
                mHandler.postDelayed(this, 500);
            }
        }, 2000);

    }

    private void simulateMove() {
        masterControlProgram.move(MOVE_DIRECTION.values()[mRandom.nextInt(MOVE_DIRECTION.values().length)]);
    }

    public MCP getMCP() {
        return masterControlProgram;
    }

}
