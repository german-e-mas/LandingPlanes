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

public class GameView implements Game.EventsListener, AerodromeView.OnAerodromeEventListener {
  private static final String TAG = GameView.class.getSimpleName();

  /**
   * View-related events that let the attached controllers interact with the model.
   */
  public interface ViewEventsListener {
    /**
     * Notify the listeners (controllers) that a position in the aerodrome was tapped.
     *
     * @param position  The position in Aerodrome Coordinates that the Aircraft needs to point to.
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

  private AerodromeView mAerodromeView;
  private TextView mScoreField;
  private TextView mGameOverField;
  private Button mRestartButton;

  private Context mContext;
  private ViewEventsListener mController;

  private Game mGame = Game.getInstance();

  public GameView(Context context) {
    mContext = context;
  }

  public void initialize() {
    mGame.setListener(this);
    mAerodromeView.setListener(this);
  }

  public void setAerodrome(AerodromeView aerodrome) {
    mAerodromeView = aerodrome;
  }

  public void setScoreField(TextView scoreField) {
    mScoreField = scoreField;
    refreshScore();
  }

  public void setGameOverField(TextView gameOver) {
    mGameOverField = gameOver;
  }

  public void refreshScore() {
    // A handler is used to reach the activity's UI Thread.
    Handler handler = new Handler(Looper.getMainLooper());
    handler.post(new Runnable() {
      @Override
      public void run() {
        mScoreField.setText(String.format(mContext.getString(R.string.score_field),
            mGame.getScore()));
      }
    });
  }

  public void setRestartButton(Button restartButton) {
    mRestartButton = restartButton;
    mRestartButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if (mController != null) {
          mRestartButton.setVisibility(View.GONE);
          mGameOverField.setVisibility(View.GONE);
          mController.onRestartPressed();
        }
      }
    });
  }

  @Override
  public void onGameStart() {
    mAerodromeView.cleanView();
    refreshScore();
  }

  @Override
  public void onGameOver() {
    mAerodromeView.onGameOver();

    // A handler is used to reach the activity's UI Thread.
    Handler handler = new Handler(Looper.getMainLooper());
    handler.post(new Runnable() {
      @Override
      public void run() {
        mRestartButton.setVisibility(View.VISIBLE);
        mGameOverField.setVisibility(View.VISIBLE);
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
    mAerodromeView.onLongRunwayCreated(longRunway);
  }

  /**
   * A Short Runway was created. Delegate this callback to the Aerodrome View to create it's visual
   * representation.
   *
   * @param shortRunway  The short runway created.
   */
  @Override
  public void onShortRunwayCreated(ShortRunway shortRunway) {
    mAerodromeView.onShortRunwayCreated(shortRunway);
  }

  /**
   * A Helipad was created. Delegate this callback to the Aerodrome View to create it's visual
   * representation.
   *
   * @param helipad   The helipad created.
   */
  @Override
  public void onHelipadCreated(Helipad helipad) {
    mAerodromeView.onHelipadCreated(helipad);
  }

  /**
   * A Large Plane was created. Delegate this callback to the Aerodrome View to create it's visual
   * representation.
   *
   * @param largePlane  The large plane created.
   */
  @Override
  public void onLargePlaneGenerated(LargePlane largePlane) {
    mAerodromeView.onLargePlaneGenerated(largePlane);
  }

  /**
   * A Light Plane was created. Delegate this callback to the Aerodrome View to create it's visual
   * representation.
   *
   * @param lightPlane  The light plane created.
   */
  @Override
  public void onLightPlaneGenerated(LightPlane lightPlane) {
    mAerodromeView.onLightPlaneGenerated(lightPlane);
  }

  /**
   * A Helicopter was created. Delegate this callback to the Aerodrome View to create it's visual
   * representation.
   *
   * @param helicopter  The helicopter created.
   */
  @Override
  public void onHelicopterGenerated(Helicopter helicopter) {
    mAerodromeView.onHelicopterGenerated(helicopter);
  }

  /**
   * The positions of all aircraft were changed. Delegate this callback to the Aerodrome View to
   * update their visual representation.
   */
  @Override
  public void onAircraftPositionChanged() {
    mAerodromeView.onAircraftPositionChanged();
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
    mAerodromeView.onAircraftSelect(id, state);
  }

  /**
   * An aircraft has landed. Remove it from the View and refresh the score text field.
   *
   * @param id  ID of the aircraft that landed.
   */
  @Override
  public void onLand(int id) {
    mAerodromeView.removeAircraftDrawableById(id);
    refreshScore();
  }

  /**
   * An aircraft has left the aerodrome. Remove it from the View.
   *
   * @param id  ID of the aircraft that left the view.
   */
  @Override
  public void onAircraftOutsideAerodrome(int id) {
    mAerodromeView.removeAircraftDrawableById(id);
  }

  @Override
  public void onCrash(int firstId, int secondId) {
    mAerodromeView.onCrash(firstId, secondId);
  }

  @Override
  public void onAerodromeTapped(Position position) {
    //
    if (mController != null) {
      mController.onAerodromeTapped(position);
    }
  }

  @Override
  public void onViewReady() {
    //
    if (mController != null) {
      mController.onViewReady();
    }
  }
}
