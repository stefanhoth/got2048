package de.stefanhoth.android.got2048;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import de.stefanhoth.android.got2048.logic.model.MOVE_DIRECTION;

/**
 * An {@link android.app.IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * helper methods.
 */
public class GameEngineService extends IntentService {
    private static final String TAG = GameEngineService.class.getName();

    private static final String ACTION_MOVE = GameEngineService.class.getPackage() + ".action.MOVE";
    private static final String ACTION_START_GAME = GameEngineService.class.getPackage() + ".action.START_GAME";
    private static final String ACTION_RESTART_GAME = GameEngineService.class.getPackage() + ".action.RESTART_GAME";

    private static final String EXTRA_DIRECTION = GameEngineService.class.getPackage() + ".extra.PARAM1";

    public static void startActionStartGame(Context context) {
        Intent intent = new Intent(context, GameEngineService.class);
        intent.setAction(ACTION_START_GAME);
        context.startService(intent);
    }

    public static void startActionRestartGame(Context context) {
        Intent intent = new Intent(context, GameEngineService.class);
        intent.setAction(ACTION_RESTART_GAME);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see android.app.IntentService
     */
    public static void startActionMove(Context context, MOVE_DIRECTION direction) {
        Intent intent = new Intent(context, GameEngineService.class);
        intent.setAction(ACTION_MOVE);
        intent.putExtra(EXTRA_DIRECTION, direction.ordinal());
        context.startService(intent);
    }

    public GameEngineService() {
        super("GameEngineService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_START_GAME.equals(action)) {
                handleActionStartGame();
            } else if (ACTION_RESTART_GAME.equals(action)) {
                handleActionRestartGame();
            } else if (ACTION_MOVE.equals(action)) {
                try {
                    final MOVE_DIRECTION direction = MOVE_DIRECTION.values()[intent.getIntExtra(EXTRA_DIRECTION, 0)];
                    handleActionMove(direction);
                } catch (NullPointerException e) {
                    Log.e(TAG, "Could not read EXTRA_DIRECTION");
                } catch (IllegalArgumentException e) {
                    Log.e(TAG, "Could not convert EXTRA_DIRECTION into MOVE_DIRECTION enum value");
                }
            } else {
                Log.w(TAG, "Unhandled action=" + action);
            }
        }
    }

    private void handleActionStartGame() {

        if (getApplication() == null || !(getApplication() instanceof Got2048App)) {
            Log.e(TAG, "handleActionMove: Could not access application object");
            return;
        }

        ((Got2048App) getApplication()).getMCP().addStartCells(false);
    }

    private void handleActionRestartGame() {

        if (getApplication() == null || !(getApplication() instanceof Got2048App)) {
            Log.e(TAG, "handleActionMove: Could not access application object");
            return;
        }

        ((Got2048App) getApplication()).getMCP().restartGame();
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionMove(MOVE_DIRECTION direction) {

        if (direction == null) {
            Log.e(TAG, "handleActionMove: Invalid direction.");
            return;
        } else if (getApplication() == null || !(getApplication() instanceof Got2048App)) {
            Log.e(TAG, "handleActionMove: Could not access application object");
            return;
        }

        ((Got2048App) getApplication()).getMCP().move(direction);
    }
}
