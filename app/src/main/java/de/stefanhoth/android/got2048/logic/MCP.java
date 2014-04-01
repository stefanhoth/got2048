package de.stefanhoth.android.got2048.logic;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.deploygate.sdk.DeployGate;

import de.stefanhoth.android.got2048.logic.model.Cell;
import de.stefanhoth.android.got2048.logic.model.Grid;
import de.stefanhoth.android.got2048.logic.model.MOVE_DIRECTION;
import de.stefanhoth.android.got2048.logic.model.MovementChanges;

/**
 * TODO describe class
 *
 * @author Stefan Hoth <sh@jnamic.com>
 *         date: 20.03.14 20:40
 * @since TODO add version
 */
public class MCP {

    private static final String TAG = MCP.class.getName();
    protected static final int DEFAULT_START_FIELDS = 2;
    protected static final int DEFAULT_START_VALUE = 2;
    protected static final int DEFAULT_WON_VALUE = 2048;

    public static final String BROADCAST_MCP = MCP.class.getPackage() + ".BROADCAST_MCP";

    public static final String BROADCAST_ACTION_MOVE_START = MCP.class.getPackage() + ".action.MOVE_START";
    public static final String BROADCAST_ACTION_MOVE_DONE = MCP.class.getPackage() + ".action.MOVE_DONE";
    public static final String BROADCAST_ACTION_GAME_WON = MCP.class.getPackage() + ".action.GAME_WON";
    public static final String BROADCAST_ACTION_GAME_OVER = MCP.class.getPackage() + ".action.GAME_OVER";
    public static final String KEY_DIRECTION = MCP.class.getPackage() + ".key.DIRECTION";
    public static final String KEY_MOVEMENT_CHANGES = MCP.class.getPackage() + ".key.MOVEMENTS";
    private final Context mContext;

    private Grid playlingField;
    private boolean mGameStopped;
    private boolean mCurrentlyMoving;

    public MCP(Context context, int gridSize) {
        mContext = context;
        playlingField = new Grid(gridSize);
        mGameStopped = false;
        mCurrentlyMoving = false;
    }

    protected Grid getPlaylingField() {
        return playlingField;
    }

    public void addStartCells() {

        Cell cell = playlingField.getRandomCell();

        playlingField.setCellValue(cell.getRow(), cell.getColumn(), DEFAULT_START_VALUE);

        Cell nextCell = playlingField.getRandomCell();

        for (int count = playlingField.getActiveCells(); count < DEFAULT_START_FIELDS; count++) {
            while (cell.equals(nextCell)) {
                nextCell = playlingField.getRandomCell();
            }

            playlingField.setCellValue(nextCell.getRow(), nextCell.getColumn(), DEFAULT_START_VALUE);
            cell = nextCell;
        }


        updateMoveDoneListeners(new MovementChanges(getPlaylingField().getGridStatus()));
    }

    public MovementChanges addNewCell(MovementChanges changes) {

        if (playlingField.getActiveCells() == (playlingField.getGridSize() * playlingField.getGridSize())) {
            Log.i(TAG, "addNewCell: Field is full. Can't add new cell.");
            return changes;
        }

        Cell cell;

        do {
            cell = playlingField.getRandomCell();

        } while (playlingField.cellHasValue(cell.getRow(), cell.getColumn()));

        changes.addCell(cell, DEFAULT_START_VALUE);
        playlingField.setCellValue(cell.getRow(), cell.getColumn(), DEFAULT_START_VALUE);

        return changes;
    }

    public void move(MOVE_DIRECTION direction) {
        move(direction, true);
    }

    protected void move(MOVE_DIRECTION direction, boolean spawnNewCell) {

        if (mGameStopped) {
            Log.w(TAG, "move: Game is stopped. Not accepting any movement at this time.");
            return;
        } else if (mCurrentlyMoving) {
            Log.d(TAG, "move: Currently working on a move, not accepting further input until done");
            return;
        }

        mCurrentlyMoving = true;

        if (playlingField.wouldMoveCells(direction)) {
            updateMoveStartListeners(direction);

            Log.v(TAG, "move: Executing move to " + direction + ".");
            MovementChanges changes = playlingField.moveCells(direction);
            if (spawnNewCell) {
                changes = addNewCell(changes);
            }
            updateMoveDoneListeners(changes);
        } else {
            Log.d(TAG, "move: Move to " + direction + " wouldn't move any cells, so nothing is happening.");
        }

        if (playlingField.isGameOver()) {
            mGameStopped = true;
            updateGameOverListeners();
        } else if (playlingField.isGameWon(DEFAULT_WON_VALUE)) {
            mGameStopped = true;
            updateGameWonListeners();
        }

        mCurrentlyMoving = false;
    }

    private void updateMoveStartListeners(MOVE_DIRECTION direction) {

        Bundle extras = new Bundle();
        extras.putSerializable(KEY_DIRECTION, direction.ordinal());

        sendLocalBroadcast(BROADCAST_ACTION_MOVE_START, extras);
    }

    private void updateMoveDoneListeners(MovementChanges changes) {

        Bundle extras = new Bundle();
        extras.putParcelable(KEY_MOVEMENT_CHANGES, changes);

        sendLocalBroadcast(BROADCAST_ACTION_MOVE_DONE, extras);
    }

    private void updateGameOverListeners() {

        sendLocalBroadcast(BROADCAST_ACTION_GAME_OVER, null);

        String username = DeployGate.getLoginUsername();
        String message = String.format("%s just LOST a game.", (username == null) ? "UNKNOWN" : username);
        DeployGate.logInfo(message);
    }

    private void updateGameWonListeners() {

        sendLocalBroadcast(BROADCAST_ACTION_GAME_WON, null);
        String username = DeployGate.getLoginUsername();
        String message = String.format("%s just WON a game.", (username == null) ? "UNKNOWN" : username);
        DeployGate.logInfo(message);
    }

    private void sendLocalBroadcast(String action, Bundle extras) {

        if (mContext == null) {
            Log.e(TAG, "sendLocalBroadcast: Context=null, can't broadcast action=" + action);
            return;
        }

        Intent localIntent =
                new Intent(BROADCAST_MCP)
                        .setAction(action);

        if (extras != null && !extras.isEmpty()) {
            localIntent.putExtras(extras);
        }

        LocalBroadcastManager.getInstance(mContext).sendBroadcast(localIntent);

    }
}
