package de.stefanhoth.android.got2048.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * TODO describe class
 *
 * @author Stefan Hoth <sh@jnamic.com>
 *         date: 06.02.14 14:16
 * @since 0.18
 */
public abstract class SettingsHelper {

    private static final String TAG = SettingsHelper.class.getName();

    private static final String KEY_GAME_SETTINGS = "KEY_GAME_SETTINGS";
    private static final String KEY_HIGHSCORE = "KEY_HIGHSCORE";

    public static boolean storeSettings(Context context, int highscore) throws IllegalArgumentException {


        Log.d(TAG, "storeSettings: Storing new highscores=" + highscore);

        SharedPreferences.Editor editor = getPrefsEditor(context);
        editor.putInt(KEY_HIGHSCORE, highscore);

        if (editor.commit()) {
            return true;
        }

        return false;
    }

    public static int loadHighscore(Context context) {

        SharedPreferences preferences = getPreferences(context);

        if (preferences == null) {
            Log.e(TAG, "loadHighscore: Could get shared preferences for settings.");
            return -1;
        } else if (preferences.getAll().isEmpty()) {
            Log.e(TAG, "loadHighscore: Preferences empty. Indicating empty settings.");
            return -1;
        } else if (!preferences.getAll().containsKey(KEY_HIGHSCORE)) {
            Log.e(TAG, "loadHighscore: KEY_HIGHSCORE not set in settings. Indicating empty settings.");
            return -1;
        }

        int highscore = preferences.getInt(KEY_HIGHSCORE, -1);

        Log.d(TAG, "loadHighscore: Last highscore loaded=" + highscore);

        return highscore;
    }


    private static SharedPreferences.Editor getPrefsEditor(Context context) {
        return getPreferences(context).edit();
    }

    private static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(KEY_GAME_SETTINGS, Context.MODE_PRIVATE);
    }
}
