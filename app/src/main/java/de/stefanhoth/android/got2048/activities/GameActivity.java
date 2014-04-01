package de.stefanhoth.android.got2048.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import de.stefanhoth.android.got2048.GameEngineService;
import de.stefanhoth.android.got2048.Got2048App;
import de.stefanhoth.android.got2048.R;
import de.stefanhoth.android.got2048.fragments.NavigationDrawerFragment;
import de.stefanhoth.android.got2048.fragments.PlayingFieldFragment;
import de.stefanhoth.android.got2048.logic.model.MOVE_DIRECTION;


public class GameActivity extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, PlayingFieldFragment.OnPlayingFieldEventListener {

    private static final String TAG = GameActivity.class.getName();
    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_game);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlayingFieldFragment.newInstance(1337)) //TODO save/load to sharedprefs
                .commit();
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        /*
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
        */
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

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.game, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
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
    public void onPlayingFieldReady() {

        Log.d(TAG, "onPlayingFieldReady: View is ready for gaming. Starting game.");
        GameEngineService.startActionStartGame(getBaseContext());

    }


}
