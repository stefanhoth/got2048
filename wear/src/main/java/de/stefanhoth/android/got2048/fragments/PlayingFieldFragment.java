package de.stefanhoth.android.got2048.fragments;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import com.deploygate.sdk.DeployGate;

import java.util.Random;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.stefanhoth.android.got2048.GameEngineService;
import de.stefanhoth.android.got2048.R;
import de.stefanhoth.android.got2048.helpers.SettingsHelper;
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

    private static final String KEY_HIGHSCORE = "KEY_HIGHSCORE";
    private static final String TAG = PlayingFieldFragment.class.getName();
    private static final String PREF_USER_LEARNED_SWIPE = "PREF_USER_LEARNED_SWIPE";

    private int mCurrentScore;
    private int mHighscore;

    @InjectView(R.id.playing_field)
    SquareGridView mSquareGridView;

    @InjectView(R.id.game_status)
    TextView mGameStatus;

    @InjectView(R.id.txt_score_current)
    TextView mTvCurrentScore;

    @InjectView(R.id.txt_score_best)
    TextView mTvHighscore;

    private OnPlayingFieldEventListener mPlayingFieldEventListener;
    private GestureDetector mGestureDetector;
    private View.OnTouchListener mGestureListener;
    private McpEventReceiver mMcpEventReceiver;
    private boolean mReadyAnnounced;
    private boolean mUserLearnedSwipe;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param lastHighscore The last mHighscore of the player
     * @return A new instance of fragment PlayingFieldFragment.
     */
    public static PlayingFieldFragment newInstance(int lastHighscore) {
        PlayingFieldFragment fragment = new PlayingFieldFragment();
        Bundle args = new Bundle();
        args.putInt(KEY_HIGHSCORE, lastHighscore);
        fragment.setArguments(args);
        return fragment;
    }

    public PlayingFieldFragment() {
        // Required empty public constructor
        super();
        mHighscore = 0;
        mCurrentScore = 0;
        mReadyAnnounced = false;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        if (getArguments() != null) {
            mHighscore = getArguments().getInt(KEY_HIGHSCORE);
        }

        // Read in the flag indicating whether or not the user has demonstrated awareness of the
        // drawer. See PREF_USER_LEARNED_SWIPE for details.
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mUserLearnedSwipe = sp.getBoolean(PREF_USER_LEARNED_SWIPE, false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_playing_field, container, false);
        ButterKnife.inject(this, view);

        if (mHighscore > 0) {
            mTvHighscore.setText(String.valueOf(mHighscore));
        } else {
            mTvHighscore.setText("-");
        }
        mTvCurrentScore.setText(String.valueOf(mCurrentScore));

        // Gesture detection
        mGestureDetector = new GestureDetector(container.getContext(), new MyGestureDetector(getActivity()));
        mGestureListener = new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {

                ViewGroup vg = (ViewGroup) v;
                vg.requestDisallowInterceptTouchEvent(true);

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

        setHasOptionsMenu(true);

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
        mStatusIntentFilter.addAction(MCP.BROADCAST_ACTION_GAME_WON);
        mStatusIntentFilter.addAction(MCP.BROADCAST_ACTION_GAME_OVER);

        LocalBroadcastManager
                .getInstance(getActivity().getBaseContext())
                .registerReceiver(
                        mMcpEventReceiver,
                        mStatusIntentFilter
                );

        Log.d(TAG, "onAttach: View is ready for playing.");
    }

    @Override
    public void onStart() {
        super.onStart();
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
        public void onGameOver(int score, int bestScore, boolean won);
    }

    class MyGestureDetector extends GestureDetector.SimpleOnGestureListener {

        private static final int SWIPE_MIN_DISTANCE = 80;
        private static final int SWIPE_MAX_OFF_PATH = 100;
        private static final int SWIPE_THRESHOLD_VELOCITY = 150;

        Activity mActivity;

        public MyGestureDetector(Activity act) {
            this.mActivity = act;
        }

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
            mActivity.finish();
        }


        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
//        if (Math.abs(e1.getX() - e2.getX()) > SWIPE_MAX_OFF_PATH) {
//            return false;
//        }

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

            mUserLearnedSwipe = true;
            SharedPreferences sp = PreferenceManager
                    .getDefaultSharedPreferences(getActivity());
            sp.edit().putBoolean(PREF_USER_LEARNED_SWIPE, mUserLearnedSwipe).apply();

            return true;
        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /*int id = item.getItemId();
        Log.d(TAG, "onOptionsItemSelected: id=" + id);

        if (id == R.id.action_restart) {
            handleRestartAction();
            return true;
        } else if (id == R.id.action_random) {
            Toast.makeText(getActivity().getBaseContext(), "Making a move…", Toast.LENGTH_SHORT).show();
            announceMovement(MOVE_DIRECTION.values()[new Random().nextInt(MOVE_DIRECTION.values().length)]);
            return true;
        }
        */
        return super.onOptionsItemSelected(item);
    }

    private void handleRestartAction() {

        new AlertDialog.Builder(getActivity())
                .setTitle(R.string.action_restart)
                .setMessage(R.string.dialog_restart_question)
                .setPositiveButton(R.string.dialog_restart_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        GameEngineService.startActionRestartGame(getActivity().getBaseContext());
                    }
                })
                .setNegativeButton(R.string.dialog_restart_no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create()
                .show();
    }

    private void announceReady() {

        if (mReadyAnnounced) {
            return;
        }

        if (mPlayingFieldEventListener != null) {
            mPlayingFieldEventListener.onPlayingFieldReady();
        }

        mReadyAnnounced = true;

        teachSwipeMovement();

        Toast.makeText(getActivity(), R.string.long_press_to_exit, Toast.LENGTH_LONG).show();
    }

    private void teachSwipeMovement() {
        if (mUserLearnedSwipe) {
            return;
        }

        float translateBy = 50f;
        int startDelay = 1000;
        int duration = 300;

        ObjectAnimator animXRight = ObjectAnimator.ofFloat(mSquareGridView, "translationX", translateBy);
        ObjectAnimator animXLeft = ObjectAnimator.ofFloat(mSquareGridView, "translationX", -translateBy);
        ObjectAnimator animXReset = ObjectAnimator.ofFloat(mSquareGridView, "translationX", 0);
        ObjectAnimator animYUp = ObjectAnimator.ofFloat(mSquareGridView, "translationY", -translateBy);
        ObjectAnimator animYDown = ObjectAnimator.ofFloat(mSquareGridView, "translationY", translateBy);
        ObjectAnimator animYReset = ObjectAnimator.ofFloat(mSquareGridView, "translationY", 0);

        AnimatorSet bouncer = new AnimatorSet();
        bouncer.playSequentially(animXRight, animXLeft, animXReset, animYUp, animYDown, animYReset);

        bouncer.setStartDelay(startDelay);
        bouncer.setDuration(duration);
        bouncer.setInterpolator(new AccelerateDecelerateInterpolator());
        bouncer.start();
    }

    private void announceMovement(MOVE_DIRECTION direction) {

        if (mPlayingFieldEventListener != null) {
            mPlayingFieldEventListener.onMovementRecognized(direction);
        }

        String property;
        float movement = 50f;

        switch (direction) {

            case UP:
                property = "translationY";
                movement = -movement;
                break;
            case DOWN:
                property = "translationY";
                break;
            case LEFT:
                property = "translationX";
                movement = -movement;
                break;
            case RIGHT:
                property = "translationX";
                break;
            default:
                return;
        }

        ObjectAnimator animOut = ObjectAnimator.ofFloat(mSquareGridView, property, movement);
        ObjectAnimator animIn = ObjectAnimator.ofFloat(mSquareGridView, property, 0);

        AnimatorSet bouncer = new AnimatorSet();
        bouncer.playSequentially(animOut, animIn);
        bouncer.setDuration(200);
        bouncer.setInterpolator(new AccelerateDecelerateInterpolator());
        bouncer.start();

    }


    private void checkHighscore() {

        if (mCurrentScore > mHighscore) {
            //TODO do something visually fancy
            Toast.makeText(getActivity().getBaseContext(), "NEW HIGHSCORE!", Toast.LENGTH_SHORT).show();
            mHighscore = mCurrentScore;

            if (!SettingsHelper.storeSettings(getActivity().getBaseContext(), mCurrentScore)) {
                Log.e(TAG, "checkHighscore: Could not persist highscore into shared prefs.");
                return;
            }
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
                mSquareGridView.addCells(changes.getAddedCells(), changes.addedCellsValue);

                if (changes.isRestart()) {
                    mCurrentScore = 0;
                    mGameStatus.setText("");
                } else {
                    mCurrentScore += changes.pointsEarned;
                }
                //TODO animate new points
                mTvCurrentScore.setText(String.valueOf(mCurrentScore));

            } catch (Exception e) {
                Log.e(TAG, "Could not read movements from broadcast.", e);
            }
        }

        private void handleGameOver(Intent intent) {

            checkHighscore();

            if(mPlayingFieldEventListener != null)
                mPlayingFieldEventListener.onGameOver(mCurrentScore, mHighscore, false);
        }

        private void handleGameWon(Intent intent) {

            checkHighscore();

            if(mPlayingFieldEventListener != null)
                mPlayingFieldEventListener.onGameOver(mCurrentScore, mHighscore, true);
        }
    }
}