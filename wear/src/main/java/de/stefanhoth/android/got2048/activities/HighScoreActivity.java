package de.stefanhoth.android.got2048.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.widget.TextView;

import de.stefanhoth.android.got2048.R;
import de.stefanhoth.android.got2048.helpers.SettingsHelper;

public class HighScoreActivity extends Activity {

    private TextView mHighScoreView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);

        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                mHighScoreView = (TextView) stub.findViewById(R.id.highscore);
                mHighScoreView.setText(""+SettingsHelper.loadHighscore(HighScoreActivity.this));
            }
        });
    }
}
