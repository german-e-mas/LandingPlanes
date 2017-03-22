package mas.german.landingplanes.view;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import mas.german.landingplanes.Game;
import mas.german.landingplanes.Position;
import mas.german.landingplanes.R;
import mas.german.landingplanes.aircrafts.Helicopter;
import mas.german.landingplanes.aircrafts.LargePlane;
import mas.german.landingplanes.aircrafts.LightPlane;
import mas.german.landingplanes.landingsites.Helipad;
import mas.german.landingplanes.landingsites.LongRunway;
import mas.german.landingplanes.landingsites.ShortRunway;

/**
 * This class contains all the view-related elements. It listens to the game events in order to
 * provide visual feedback.
 */
public class GameView implements Game.EventsListener, AerodromeView.OnAerodromeEventListener {
  private static final String TAG = GameView.class.getSimpleName();

  /**
   * View-related events that let the attached controllers interact with the model.
   */
  public interface ViewEventsListener {
    /**
     * Notify the listeners (controllers) that a position in the aerodrome was tapped.
     *
     * @param position  The position in Aerodrome Coordinates.
     */
    void onAerodromeTapped(Position position);

    /**
     * The view is ready. Notify the Controller so the game can get started.
     */
    void onViewReady();

    /**
     * The restart button was pressed. Notify the Controller so the game can restart.
     */
    void onRestartPressed();
  }

  public void setController(ViewEventsListener controller) {
    mController = controller;
  }

  // View Elements
  private AerodromeView mAerodrome;
  private TextView mGameOverText;
  private TextView mScoreText;
  private Button mRestart;

  private Game mGame = Game.getInstance();
  private Context mContext;
  private ViewEventsListener mController;

  public GameView(Context context, AerodromeView aerodrome, TextView scoreText,
                  TextView gameOverText, Button restart) {
    mContext = context;
    mAerodrome = aerodrome;
    mScoreText = scoreText;
    mGameOverText = gameOverText;
    mRestart = restart;
    mRestart.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if (mController != null) {
          mController.onRestartPressed();
        }
      }
    });
  }

  public void initialize() {
    mGame.setListener(this);
    mAerodrome.setListener(this);
  }

  private void refreshScore() {
    // A handler is used to reach the activity's UI Thread.
    Handler handler = new Handler(Looper.getMainLooper());
    handler.post(new Runnable() {
      @Override
      public void run() {
        mScoreText.setText(String.format(mContext.getString(R.string.score_field),
            mGame.getScore()));
      }
    });
  }

  @Override
  public void onGameStart() {
    // The Aerodrome is cleaned and the score refreshed.
    mAerodrome.cleanView();
    refreshScore();

    // A handler is used to reach the activity's UI Thread.
    Handler handler = new Handler(Looper.getMainLooper());
    handler.post(new Runnable() {
      @Override
      public void run() {
        mRestart.setVisibility(View.GONE);
        // By being Invisible it is taken into account by the layout.
        // This doesn't happens with Gone, which causes the layout to configure itself.
        mGameOverText.setVisibility(View.INVISIBLE);
      }
    });
  }

  @Override
  public void onGameOver() {
    // The Aerodrome must change it's visual representation.
    mAerodrome.onGameOver();

    // A handler is used to reach the activity's UI Thread.
    Handler handler = new Handler(Looper.getMainLooper());
    handler.post(new Runnable() {
      @Override
      public void run() {
        mRestart.setVisibility(View.VISIBLE);
        mGameOverText.setVisibility(View.VISIBLE);
      }
    });
  }

  /**
   * A Long Runway was created. Delegate this callback to the Aerodrome View to create it's visual
   * representation.
   *
   * @param longRunway  The long runway created.
   */
  @Override
  public void onLongRunwayCreated(LongRunway longRunway) {
    mAerodrome.onLongRunwayCreated(longRunway);
  }

  /**
   * A Short Runway was created. Delegate this callback to the Aerodrome View to create it's visual
   * representation.
   *
   * @param shortRunway  The short runway created.
   */
  @Override
  public void onShortRunwayCreated(ShortRunway shortRunway) {
    mAerodrome.onShortRunwayCreated(shortRunway);
  }

  /**
   * A Helipad was created. Delegate this callback to the Aerodrome View to create it's visual
   * representation.
   *
   * @param helipad   The helipad created.
   */
  @Override
  public void onHelipadCreated(Helipad helipad) {
    mAerodrome.onHelipadCreated(helipad);
  }

  /**
   * A Large Plane was created. Delegate this callback to the Aerodrome View to create it's visual
   * representation.
   *
   * @param largePlane  The large plane created.
   */
  @Override
  public void onLargePlaneGenerated(LargePlane largePlane) {
    mAerodrome.onLargePlaneGenerated(largePlane);
  }

  /**
   * A Light Plane was created. Delegate this callback to the Aerodrome View to create it's visual
   * representation.
   *
   * @param lightPlane  The light plane created.
   */
  @Override
  public void onLightPlaneGenerated(LightPlane lightPlane) {
    mAerodrome.onLightPlaneGenerated(lightPlane);
  }

  /**
   * A Helicopter was created. Delegate this callback to the Aerodrome View to create it's visual
   * representation.
   *
   * @param helicopter  The helicopter created.
   */
  @Override
  public void onHelicopterGenerated(Helicopter helicopter) {
    mAerodrome.onHelicopterGenerated(helicopter);
  }

  /**
   * The positions of all aircraft were changed. Delegate this callback to the Aerodrome View to
   * update their visual representation.
   */
  @Override
  public void onAircraftPositionChanged() {
    mAerodrome.onAircraftPositionChanged();
  }

  /**
   * An aircraft selection state was changed. Delegate this callback to the Aerodrome View to update
   * their visual representation.
   *
   * @param id      ID of the target Aircraft.
   * @param state   Selection state to be set.
   */
  @Override
  public void onAircraftSelect(int id, boolean state) {
    mAerodrome.onAircraftSelect(id, state);
  }

  /**
   * An aircraft has landed. Remove it from the View and refresh the score text field.
   *
   * @param id  ID of the aircraft that landed.
   */
  @Override
  public void onLand(int id) {
    mAerodrome.removeAircraftDrawableById(id);
    refreshScore();
  }

  /**
   * An aircraft has left the aerodrome. Remove it from the View.
   *
   * @param id  ID of the aircraft that left the view.
   */
  @Override
  public void onAircraftOutsideAerodrome(int id) {
    mAerodrome.removeAircraftDrawableById(id);
  }

  @Override
  public void onAerodromeTapped(Position position) {
    if (mController != null) {
      mController.onAerodromeTapped(position);
    }

  }

  @Override
  public void onAerodromeReady() {
    if (mController != null) {
      mController.onViewReady();
    }
  }
}
