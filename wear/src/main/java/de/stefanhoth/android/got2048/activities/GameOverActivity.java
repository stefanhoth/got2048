package de.stefanhoth.android.got2048.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import de.stefanhoth.android.got2048.R;

public class GameOverActivity extends Activity {

    public final static String DID_WIN_EXTRA = "de.stefanhoth.android.got2048.DID_WIN";
    public final static String SCORE_EXTRA = "de.stefanhoth.android.got2048.SCORE";
    public final static String BEST_SCORE_EXTRA = "de.stefanhoth.android.got2048.BEST_SCORE_EXTRA";

    private RelativeLayout mContainer;
    private TextView mBestScore, mScore, mGameOverTitle;
    private ImageView mGameOverIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                mContainer = (RelativeLayout) stub.findViewById(R.id.game_over_container);
                mBestScore = (TextView) stub.findViewById(R.id.game_over_best_score);
                mScore = (TextView) stub.findViewById(R.id.game_over_current_score);
                mGameOverIcon = (ImageView) stub.findViewById(R.id.game_over_icon);
                mGameOverTitle = (TextView) stub.findViewById(R.id.game_over_title);

                if(getIntent().getBooleanExtra(DID_WIN_EXTRA, false)) {
                    mContainer.setBackgroundColor(getResources().getColor(R.color.won));
                    mGameOverIcon.setImageResource(R.drawable.ic_won);
                    mGameOverTitle.setText(R.string.title_game_over_won);
                } else {
                    mContainer.setBackgroundColor(getResources().getColor(R.color.lost));
                    mGameOverIcon.setImageResource(R.drawable.ic_gameover);
                    mGameOverTitle.setText(R.string.title_game_over);
                }

                mScore.setText(getString(R.string.label_score)+" "+ getIntent().getIntExtra(SCORE_EXTRA, 0));
                mBestScore.setText(getString(R.string.label_score_best)+" "+ getIntent().getIntExtra(BEST_SCORE_EXTRA, 0));
            }
        });
    }
}
