package mas.german.landingplanes.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
 * This view represents the field where the landing sites stand and where the aircraft fly.
 */
public class AerodromeView extends ImageView implements Game.EventsListener {
  private static final String TAG = AerodromeView.class.getSimpleName();
  // Multiplier for the selection distance.
  private static final float SELECT_MODIFIER = 1.5f;

  /**
   * View-related events that let the attached controllers modify the model.
   */
  public interface OnViewEventListener {
    /**
     * Notify the listeners that an aircraft needs to face the given position.
     *
     * @param position  The position that Aircraft needs to point to.
     */
    void onAerodromeTapped(Position position);

    /**
     * An aircraft was selected.
     *
     * @param id  ID of the selected Aircraft.
     */
    void onAircraftSelected(int id);
  }

  public void setListener(OnViewEventListener listener) {
    mListener = listener;
  }

  // Each plane need Context in order to access the resources and get their colours.
  private Context mContext;

  // The Listener that will be receiving notifications from this class.
  private OnViewEventListener mListener;

  // Paints used in the view.
  private Paint mBackgroundPaint = new Paint();
  private Paint mSitePaint = new Paint();

  // Game instance that contains the Model data.
  private Game mGame = Game.getInstance();

  // Aerodrome Matrix used to fit the model into the view.
  private Matrix mAerodrome = new Matrix();
  private boolean mIsAerodromeLoaded = false;

  // Map of Aircraft Drawables. They represent the aircraft from the model.
  private Map<Integer, AircraftDrawable> mDrawables = new HashMap<>();
  private List<LandingSiteDrawable> mSiteDrawables = new ArrayList<>();

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
   * Map an (x,y) point into Aerodrome coordinates.
   *
   * @param x The X-coordinate of the point to map.
   * @param y The Y-coordinate of the point to map.
   * @return  The mapped Position in Aerodrome coordinates.
   */
  private Position getAerodromePosition(float x, float y) {
    float xy[] = {x, y};
    Matrix inverse = new Matrix();
    mAerodrome.invert(inverse);
    inverse.mapPoints(xy);
    return new Position(xy[0], xy[1]);
  }

  /**
   * Auxiliary method used to scale the Aerodrome Matrix in order to fit the screen.
   * Note that this method is only called once, as the view's size is constant throughout the game.
   */
  private void prepareAerodrome() {
    float scaleX = getWidth() / (float) (mGame.getAerodrome().getWidth());
    float scaleY = getHeight() / (float) (mGame.getAerodrome().getHeight());
    mAerodrome.postScale(scaleX, scaleY);
    mIsAerodromeLoaded = true;
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

    // Set the Aerodrome Matrix only once.
    if (!mIsAerodromeLoaded) {
      prepareAerodrome();
    }

    // Concatenate the Canvas with the Aerodrome Matrix.
    canvas.concat(mAerodrome);

    // Draw the Aerodrome's Background.
    canvas.drawRect(0, 0, getWidth(), getHeight(), mBackgroundPaint);

    // Draw the Landing Sites.
    for (LandingSiteDrawable site : mSiteDrawables) {
      site.draw(canvas);
    }

    // Draw all Aircraft.
    synchronized (mDrawables.values()) {
      for (AircraftDrawable aircraft : mDrawables.values()) {
        aircraft.draw(canvas);
      }
    }

    canvas.restore();
  }

  @Override
  public void onAircraftPositionChanged() {
    // Synchronize the Drawable's list, so we make sure we update all the positions safely.
    synchronized (mDrawables.values()) {
      for (int id : mGame.getAircraftPositionMap().keySet()) {
        mDrawables.get(id).setPosition(mGame.getAircraftPositionMap().get(id));
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
  public void onLongRunwayCreated(LongRunway longRunway) {
    mSiteDrawables.add(new LongRunwayDrawable(mContext, longRunway));
  }

  @Override
  public void onShortRunwayCreated(ShortRunway shortRunway) {
    mSiteDrawables.add(new ShortRunwayDrawable(mContext, shortRunway));
  }

  @Override
  public void onHelipadCreated(Helipad helipad) {
    mSiteDrawables.add(new HelipadDrawable(mContext, helipad));
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
  public void onAircraftOutsideAerodrome(int id) {
    removeAircraftDrawableById(id);
  }

  @Override
  public void onCrash(int firstId, int secondId) {
    // Crash happened between the aircrafts of the given IDs.
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    switch (event.getActionMasked()) {
      case MotionEvent.ACTION_DOWN:
        // A tap in the screen can do two things: Select an Aircraft or move an already selected one
        // towards the given coordinates.
        if (mListener != null) {
          // Map the coordinates into the Aerodrome.
          Position aerodromePosition = getAerodromePosition(event.getX(), event.getY());
          // As we are going to be reading from the Drawables' list, we need to synchronize it.
          synchronized (mDrawables.values()) {
            // Check if there is an Aircraft at the touch position.
            for (AircraftDrawable drawable : mDrawables.values()) {
              if (drawable.getPosition().distanceTo(aerodromePosition) <=
                  drawable.getRadius() * SELECT_MODIFIER) {
                drawable.select();
                mListener.onAircraftSelected(drawable.getId());
                return true;
              }
              drawable.deselect();
            }
            // At this point, no Aircraft was at the touch position. Notify that position.
            mListener.onAerodromeTapped(aerodromePosition);
          }
        }
        return true;
    }
    return super.onTouchEvent(event);
  }
}
