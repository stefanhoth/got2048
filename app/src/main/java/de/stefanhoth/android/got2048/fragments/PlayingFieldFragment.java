package de.stefanhoth.android.got2048.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.stefanhoth.android.got2048.Got2048App;
import de.stefanhoth.android.got2048.R;
import de.stefanhoth.android.got2048.logic.MCP;
import de.stefanhoth.android.got2048.logic.model.MOVE_DIRECTION;
import de.stefanhoth.android.got2048.widgets.SquareGridView;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link de.stefanhoth.android.got2048.fragments.PlayingFieldFragment.OnPlayingFieldEventListener} interface
 * to handle interaction events.
 * Use the {@link PlayingFieldFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class PlayingFieldFragment extends Fragment implements MCP.GridUpdateListener {

    private static final String KEY_LAST_HIGHSCORE = "KEY_LAST_HIGHSCORE";

    private int mCurrentScore;
    private int mLastHighScore;

    @InjectView(R.id.playing_field)
    SquareGridView mSquareGridView;

    @InjectView(R.id.game_status)
    TextView mGameStatus;

    private OnPlayingFieldEventListener mPlayingFieldEventListener;
    private GestureDetector mGestureDetector;
    private View.OnTouchListener mGestureListener;

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
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mLastHighScore = getArguments().getInt(KEY_LAST_HIGHSCORE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_playing_field, container, false);
        ButterKnife.inject(this, view);

        if (getActivity() != null) {
            ((Got2048App) getActivity().getApplication()).getMCP().addGridUpdateListeners(this);
        }

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
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mPlayingFieldEventListener = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (getActivity() != null) {
            ((Got2048App) getActivity().getApplication()).getMCP().removeGridUpdateListeners(this);
        }
    }

    @Override
    public void gridUpdated(int[][] updatedGrid) {
        mSquareGridView.updateGrid(updatedGrid);
    }

    @Override
    public void gameOver() {
        mGameStatus.setText("GAME OVER");
    }

    @Override
    public void gameWon() {
        mGameStatus.setText("YOU WON!");
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface OnPlayingFieldEventListener {
        public void onMovementRecognized(MOVE_DIRECTION direction);
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

    private void announceMovement(MOVE_DIRECTION direction) {

        if (mPlayingFieldEventListener != null) {
            mPlayingFieldEventListener.onMovementRecognized(direction);
        }
    }
}
