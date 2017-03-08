package mas.german.landingplanes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import mas.german.landingplanes.controller.Controller;
import mas.german.landingplanes.view.AerodromeView;
import mas.german.landingplanes.view.GameView;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private GameView mGameView;
    private Controller mController = new Controller();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Pass the layout elements to the View Class.
        mGameView = new GameView(getApplicationContext());
        mGameView.setAerodrome((AerodromeView) findViewById(R.id.view_aerodrome));
        mGameView.setScoreText((TextView) findViewById(R.id.score_field));
        mGameView.setGameOverText((TextView) findViewById(R.id.game_over_text));
        mGameView.setRestartButton((Button) findViewById(R.id.btn_restart));

        mGameView.setController(mController);
        mGameView.initialize();
    }
}
