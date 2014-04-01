package de.stefanhoth.android.got2048.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.stefanhoth.android.got2048.R;
import de.stefanhoth.android.got2048.logic.MCP;
import de.stefanhoth.android.got2048.logic.model.MOVE_DIRECTION;
import de.stefanhoth.android.got2048.logic.model.MovementChanges;
import de.stefanhoth.android.got2048.widgets.SquareGridView;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link de.stefanhoth.android.got2048.fragments.PlayingFieldFragment.OnPlayingFieldEventListener} interface
 * to handle interaction events.
 * Use the {@link PlayingFieldFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlayingFieldFragment extends Fragment {

    private static final String KEY_LAST_HIGHSCORE = "KEY_LAST_HIGHSCORE";
    private static final String TAG = PlayingFieldFragment.class.getName();

    private int mCurrentScore;
    private int mLastHighScore;

    @InjectView(R.id.playing_field)
    SquareGridView mSquareGridView;

    @InjectView(R.id.game_status)
    TextView mGameStatus;

    @InjectView(R.id.txt_score_current)
    TextView mTxtCurrentScore;

    @InjectView(R.id.txt_score_best)
    TextView mTxtBestScore;

    private OnPlayingFieldEventListener mPlayingFieldEventListener;
    private GestureDetector mGestureDetector;
    private View.OnTouchListener mGestureListener;
    private McpEventReceiver mMcpEventReceiver;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param lastHighscore The last highscore of the player
     * @return A new instance of fragment PlayingFieldFragment.
     */
    public static PlayingFieldFragment newInstance(int lastHighscore) {
        PlayingFieldFragment fragment = new PlayingFieldFragment();
        Bundle args = new Bundle();
        args.putInt(KEY_LAST_HIGHSCORE, lastHighscore);
        fragment.setArguments(args);
        return fragment;
    }

    public PlayingFieldFragment() {
        // Required empty public constructor
        super();
        mLastHighScore = 0;
        mCurrentScore = 0;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        if (getArguments() != null) {
            mLastHighScore = getArguments().getInt(KEY_LAST_HIGHSCORE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_playing_field, container, false);
        ButterKnife.inject(this, view);

        mTxtBestScore.setText(String.valueOf(mLastHighScore));
        mTxtCurrentScore.setText(String.valueOf(mCurrentScore));

        // Gesture detection
        mGestureDetector = new GestureDetector(container.getContext(), new MyGestureDetector());
        mGestureListener = new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {

                if (mGestureDetector.onTouchEvent(event)) {
                    return true;
                }

                if (event.getAction() == MotionEvent.ACTION_UP) {

//                    if (mIsScrolling) {
//                        mIsScrolling = false;
//                        handleScrollFinished();
//                        return true;
//                    }
                }

                return false;
            }
        };

        mSquareGridView.setOnTouchListener(mGestureListener);

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mPlayingFieldEventListener = (OnPlayingFieldEventListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnPlayingFieldEventListener");
        }

        mMcpEventReceiver = new McpEventReceiver();
        IntentFilter mStatusIntentFilter = new IntentFilter();
        mStatusIntentFilter.addAction(MCP.BROADCAST_ACTION_MOVE_START);
        mStatusIntentFilter.addAction(MCP.BROADCAST_ACTION_MOVE_DONE);
        mStatusIntentFilter.addAction(MCP.BROADCAST_ACTION_ADD_POINTS);
        mStatusIntentFilter.addAction(MCP.BROADCAST_ACTION_GAME_WON);
        mStatusIntentFilter.addAction(MCP.BROADCAST_ACTION_GAME_OVER);

        LocalBroadcastManager
                .getInstance(getActivity().getBaseContext())
                .registerReceiver(
                        mMcpEventReceiver,
                        mStatusIntentFilter
                );

        Log.d(TAG, "onAttach: View is ready for playing.");
        announceReady();
    }


    @Override
    public void onDetach() {
        super.onDetach();

        if (mMcpEventReceiver != null && getActivity() != null && getActivity().getBaseContext() != null) {
            LocalBroadcastManager.getInstance(getActivity().getBaseContext()).unregisterReceiver(mMcpEventReceiver);
            mMcpEventReceiver = null;
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface OnPlayingFieldEventListener {
        public void onMovementRecognized(MOVE_DIRECTION direction);

        public void onPlayingFieldReady();
    }

    class MyGestureDetector extends GestureDetector.SimpleOnGestureListener {

        private static final int SWIPE_MIN_DISTANCE = 80;
        private static final int SWIPE_MAX_OFF_PATH = 100;
        private static final int SWIPE_THRESHOLD_VELOCITY = 150;

        @Override
        public boolean onDown(MotionEvent e) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {

        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
//        if (Math.abs(e1.getX() - e2.getX()) > SWIPE_MAX_OFF_PATH) {
//            return false;
//        }

            String direction = "";
            if (e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                //move up
                announceMovement(MOVE_DIRECTION.UP);
            } else if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                //move down
                announceMovement(MOVE_DIRECTION.DOWN);
            } else if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                //move left
                announceMovement(MOVE_DIRECTION.LEFT);
            } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                //move right
                announceMovement(MOVE_DIRECTION.RIGHT);
            }

            return true;
        }

    }

    private void announceReady() {

        if (mPlayingFieldEventListener != null) {
            mPlayingFieldEventListener.onPlayingFieldReady();
        }
    }

    private void announceMovement(MOVE_DIRECTION direction) {

        if (mPlayingFieldEventListener != null) {
            mPlayingFieldEventListener.onMovementRecognized(direction);
        }
    }

    // Broadcast receiver for receiving status updates from the IntentService
    private class McpEventReceiver extends BroadcastReceiver {

        private final String TAG = McpEventReceiver.class.getName();

        // Prevents instantiation
        private McpEventReceiver() {
        }

        // Called when the BroadcastReceiver gets an Intent it's registered to receive
        public void onReceive(Context context, Intent intent) {

            if (intent == null || intent.getAction() == null || intent.getAction().isEmpty()) {
                Log.d(TAG, "Invalid intent received. Null or no action named.");
                return;
            }

            String action = intent.getAction();

            if (action.equalsIgnoreCase(MCP.BROADCAST_ACTION_MOVE_START)) {
                handleMoveStart(intent);
            } else if (action.equalsIgnoreCase(MCP.BROADCAST_ACTION_MOVE_DONE)) {
                handleMoveDone(intent);
            } else if (action.equalsIgnoreCase(MCP.BROADCAST_ACTION_ADD_POINTS)) {
                handleAddPoints(intent);
            } else if (action.equalsIgnoreCase(MCP.BROADCAST_ACTION_GAME_OVER)) {
                handleGameOver(intent);
            } else if (action.equalsIgnoreCase(MCP.BROADCAST_ACTION_GAME_WON)) {
                handleGameWon(intent);
            } else {
                Log.e(TAG, "Unrecognized action received=" + action);
            }
        }

        private void handleMoveStart(Intent intent) {

            try {
                MOVE_DIRECTION direction = MOVE_DIRECTION.values()[intent.getIntExtra(MCP.KEY_DIRECTION, 0)];
                //Toast.makeText(getActivity().getBaseContext(), "Movement to "+direction+" received.", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Log.e(TAG, "Could not read movements from broadcast.", e);
            }
        }

        private void handleMoveDone(Intent intent) {
            try {

                MovementChanges changes = intent.getParcelableExtra(MCP.KEY_MOVEMENT_CHANGES);

                mSquareGridView.updateGrid(changes.gridStatus);
                //TODO animate new points
                mCurrentScore += changes.pointsEarned;
                mTxtCurrentScore.setText(String.valueOf(mCurrentScore));

            } catch (Exception e) {
                Log.e(TAG, "Could not read movements from broadcast.", e);
            }
        }

        private void handleAddPoints(Intent intent) {
            try {
                int pointsAdded = intent.getIntExtra(MCP.KEY_POINTS_ADDED, 0);
                if (pointsAdded > 0) {
                    Toast.makeText(getActivity().getBaseContext(), pointsAdded + " points added to score", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Log.e(TAG, "Could not read movements from broadcast.", e);
            }
        }

        private void handleGameOver(Intent intent) {
            mGameStatus.setText("GAME OVER");
        }

        private void handleGameWon(Intent intent) {
            mGameStatus.setText("YOU WON!");
        }
    }

}