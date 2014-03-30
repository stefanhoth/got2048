package de.stefanhoth.android.got2048.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.stefanhoth.android.got2048.Got2048App;
import de.stefanhoth.android.got2048.R;
import de.stefanhoth.android.got2048.logic.MCP;
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

    private OnPlayingFieldEventListener mListener;

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

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onGameWon(int score) {
        if (mListener != null) {
            mListener.onFragmentWonMessage(score);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnPlayingFieldEventListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnPlayingFieldEventListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
        public void onFragmentWonMessage(int score);
    }

}
