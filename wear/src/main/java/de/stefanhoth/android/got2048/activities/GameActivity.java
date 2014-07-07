package de.stefanhoth.android.got2048.activities;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.view.KeyEvent;

import de.stefanhoth.android.got2048.GameEngineService;
import de.stefanhoth.android.got2048.Got2048App;
import de.stefanhoth.android.got2048.R;
import de.stefanhoth.android.got2048.fragments.PlayingFieldFragment;
import de.stefanhoth.android.got2048.helpers.SettingsHelper;
import de.stefanhoth.android.got2048.logic.model.MOVE_DIRECTION;


public class GameActivity extends Activity
        implements PlayingFieldFragment.OnPlayingFieldEventListener {

    private static final String TAG = GameActivity.class.getName();

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_game);

        WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub watchViewStub) {
                mTitle = getTitle();

                int highscore = SettingsHelper.loadHighscore(getBaseContext());

                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, PlayingFieldFragment.newInstance(highscore))
                        .commit();
            }
        });
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
        }
    }

    @Override
    public void onMovementRecognized(MOVE_DIRECTION direction) {

        if (direction == null) {
            Log.e(TAG, "onMovementRecognized: direction = null");
            return;
        }

        if (getApplication() == null || !(getApplication() instanceof Got2048App)) {
            Log.e(TAG, "onMovementRecognized: Can't access application object");
            return;
        }

        Log.d(TAG, "onMovementRecognized: Received swipe to " + direction);
        GameEngineService.startActionMove(getBaseContext(), direction);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return true;
    }

    @Override
    public void onPlayingFieldReady() {

        Log.d(TAG, "onPlayingFieldReady: View is ready for gaming. Starting game.");
        GameEngineService.startActionStartGame(getBaseContext());

    }


}
