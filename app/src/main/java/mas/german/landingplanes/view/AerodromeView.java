package mas.german.landingplanes.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import mas.german.landingplanes.Game;
import mas.german.landingplanes.R;
import mas.german.landingplanes.aircrafts.Aircraft;
import mas.german.landingplanes.landingsites.LandingSite;

/**
 * This view represents the field where the landing sites stand and where the aircraft fly.
 */
public class AerodromeView extends ImageView implements Game.Listener {
  private static final String TAG = AerodromeView.class.getSimpleName();

  // The Paints to use.
  private Paint mBackgroundPaint = new Paint();
  private Paint mSitePaint = new Paint();
  private Paint mAircraftPaint = new Paint();

  // Game instance that contains the Model data.
  private Game mGame = Game.getInstance();
  private Matrix mMap = new Matrix();
  private boolean mIsMapLoaded = false;

  public AerodromeView(Context context) {
    super(context);
    init();
  }

  public AerodromeView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  public AerodromeView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
  }

  private void init() {
    mBackgroundPaint.setColor(getResources().getColor(R.color.grass));
    mSitePaint.setColor(getResources().getColor(R.color.landingSite));
    mAircraftPaint.setColor(getResources().getColor(R.color.largeAircraft));
    mGame.setListener(this);
  }

  private void prepareMap() {

    float scaleX = getWidth() / (float) (mGame.getMap().getWidth());
    float scaleY = getHeight() / (float) (mGame.getMap().getHeight());
    mMap.postScale(scaleX, scaleY);
    mIsMapLoaded = true;
    Log.e(TAG, mMap.toString());
  }

  @Override
  protected void onDraw(Canvas canvas) {
    canvas.save();

    // Set the Map Matrix only once.
    if (!mIsMapLoaded) {
      prepareMap();
    }

    // Concatenate the Canvas with the Map. This helps to show the game's map in all screen's width
    // and height.
    canvas.concat(mMap);

    // Draw the Background.
    canvas.drawRect(getLeft(), getTop(), getRight(), getBottom(), mBackgroundPaint);

    // Draw the Landing Sites.
    for (LandingSite site : mGame.getSites()) {
      float cx = (float) site.getPosition().getX();
      float cy = (float) site.getPosition().getY();
      float radius = 1f;
      canvas.drawCircle(cx, cy, radius, mSitePaint);
    }

    // Draw the Aircrafts.
    for (Aircraft aircraft : mGame.getAircrafts()) {
      float cx = (float) aircraft.getPosition().getX();
      float cy = (float) aircraft.getPosition().getY();
      float radius = aircraft.getRadius();
      canvas.drawCircle(cx, cy, radius, mAircraftPaint);
    }

    canvas.restore();
  }

  @Override
  public void onUpdate() {
    // Update.
    postInvalidate();
  }

  @Override
  public void onGameOver() {
    Log.d(TAG, "Game Over!");
    mBackgroundPaint.setColor(getResources().getColor(R.color.landingSite));
    postInvalidate();
  }

  @Override
  public void onAircraftGenerated(Aircraft aircraft) {
    Log.d(TAG, "Aircraft #" + aircraft.getId() + " was created.");
  }

  @Override
  public void onLand(Aircraft aircraft) {
    Log.d(TAG, "Aircraft #" + aircraft.getId() + " landed.");
  }

  @Override
  public void onAircraftOutsideMap(Aircraft aircraft) {
    Log.d(TAG, "Aircraft #" + aircraft.getId() + " left the map.");
    // Remove the Drawable Aircraft.
  }

  @Override
  public void onCrash(int id1, int id2) {
    Log.d(TAG, "CRASH! Aircraft #" + id1 + " hit Aircraft #" + id2);
  }
}
