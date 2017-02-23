package mas.german.landingplanes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import mas.german.landingplanes.controller.Controller;
import mas.german.landingplanes.view.AerodromeView;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private AerodromeView mView;
    private Game mGame = Game.getInstance();
    private Controller mCtrl = new Controller();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mView = (AerodromeView) findViewById(R.id.view_aerodrome);
        mView.setListener(mCtrl);

        mGame.initialize();
    }
}
