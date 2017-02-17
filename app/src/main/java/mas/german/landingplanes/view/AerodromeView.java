package mas.german.landingplanes.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.ImageView;
import java.util.HashMap;
import java.util.Map;
import mas.german.landingplanes.Game;
import mas.german.landingplanes.R;
import mas.german.landingplanes.aircrafts.Aircraft;
import mas.german.landingplanes.aircrafts.Helicopter;
import mas.german.landingplanes.aircrafts.LargePlane;
import mas.german.landingplanes.aircrafts.LightPlane;
import mas.german.landingplanes.landingsites.LandingSite;

/**
 * This view represents the field where the landing sites stand and where the aircraft fly.
 */
public class AerodromeView extends ImageView implements Game.Listener {
  private static final String TAG = AerodromeView.class.getSimpleName();

  // Each plane need Context in order to access the resources and get their colours.
  private Context mContext;

  // Paints used in the view.
  private Paint mBackgroundPaint = new Paint();
  private Paint mSitePaint = new Paint();

  // Game instance that contains the Model data.
  private Game mGame = Game.getInstance();

  // Map Matrix used to fit the model into the view.
  private Matrix mMap = new Matrix();
  private boolean mIsMapLoaded = false;

  // Map of Aircraft Drawables. They represent the aircrafts from the model.
  private Map<Integer, AircraftDrawable> mDrawables = new HashMap<>();

  public AerodromeView(Context context) {
    super(context);
    init(context);
  }

  public AerodromeView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(context);
  }

  public AerodromeView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(context);
  }

  private void init(Context context) {
    mBackgroundPaint.setColor(getResources().getColor(R.color.grass));
    mSitePaint.setColor(getResources().getColor(R.color.landingSite));
    mContext = context;
    mGame.setListener(this);
  }

  /**
   * Auxiliary method used to scale the Map Matrix in order to fit the screen.
   * Note that this method is only called once, as the view's size is constant throughout the game.
   */
  private void prepareMap() {
    float scaleX = getWidth() / (float) (mGame.getMap().getWidth());
    float scaleY = getHeight() / (float) (mGame.getMap().getHeight());
    mMap.postScale(scaleX, scaleY);
    mIsMapLoaded = true;
  }

  /**
   * Remove a Drawable from the Collection, given it's ID. This operation is synchronized to keep it
   * thread-safe.
   *
   * @param id  Drawable Aircraft to be removed.
   */
  private void removeAircraftDrawableById(int id) {
    synchronized (mDrawables.values()) {
      mDrawables.remove(id);
    }
    postInvalidate();
  }

  @Override
  protected void onDraw(Canvas canvas) {
    canvas.save();

    // Set the Map Matrix only once.
    if (!mIsMapLoaded) {
      prepareMap();
    }

    // Concatenate the Canvas with the Map.
    canvas.concat(mMap);

    // Draw the Map's Background.
    canvas.drawRect(0, 0, getWidth(), getHeight(), mBackgroundPaint);

    // Draw the Landing Sites.
    for (LandingSite site : mGame.getSites()) {
      float cx = (float) site.getPosition().getX();
      float cy = (float) site.getPosition().getY();
      float radius = 1f;
      canvas.drawCircle(cx, cy, radius, mSitePaint);
    }

    // Draw the Aircrafts.
    synchronized (mDrawables.values()) {
      for (AircraftDrawable aircraft : mDrawables.values()) {
        aircraft.draw(canvas);
      }
    }

    canvas.restore();
  }

  @Override
  public void onAircraftsMoved() {
    synchronized (mDrawables.values()) {
      for (Aircraft modelAircraft : mGame.getAircraft()) {
        mDrawables.get(modelAircraft.getId()).updatePosition(modelAircraft);
      }
    }
    postInvalidate();
  }

  @Override
  public void onGameOver() {
    // Game Over visual effect.
    mBackgroundPaint.setColor(getResources().getColor(R.color.landingSite));
    postInvalidate();
  }

  @Override
  public void onAircraftGenerated(Aircraft aircraft) {
    // A generic Aircraft was created. This will be called before the specific subtypes of Aircraft
    // notify their creation.
  }

  @Override
  public void onLargePlaneGenerated(LargePlane largePlane) {
    synchronized (mDrawables.values()) {
      mDrawables.put(largePlane.getId(), new LargePlaneDrawable(mContext, largePlane));
    }
    postInvalidate();
  }

  @Override
  public void onLightPlaneGenerated(LightPlane lightPlane) {
    synchronized (mDrawables.values()) {
      mDrawables.put(lightPlane.getId(), new LightPlaneDrawable(mContext, lightPlane));
    }
    postInvalidate();
  }

  @Override
  public void onHelicopterGenerated(Helicopter helicopter) {
    synchronized (mDrawables.values()) {
      mDrawables.put(helicopter.getId(), new HelicopterDrawable(mContext, helicopter));
    }
    postInvalidate();
  }

  @Override
  public void onLand(int id) {
    removeAircraftDrawableById(id);
  }

  @Override
  public void onAircraftOutsideMap(int id) {
    removeAircraftDrawableById(id);
  }

  @Override
  public void onCrash(int firstId, int secondId) {
    // Crash happened between the aircrafts of the given IDs.
  }
}
