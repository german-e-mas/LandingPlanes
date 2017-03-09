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
public class AerodromeView extends ImageView {
  private static final String TAG = AerodromeView.class.getSimpleName();

  /**
   * Aerodrome-related events that need to be communicated to the corresponding listeners.
   */
  public interface OnAerodromeEventListener {
    /**
     * Notify the listeners that a position in the aerodrome was tapped.
     *
     * @param position  The position in Aerodrome Coordinates.
     */
    void onAerodromeTapped(Position position);

    /**
     * Notify the listeners that this view is ready to be used.
     */
    void onAerodromeReady();
  }

  public void setListener(OnAerodromeEventListener listener) {
    mListener = listener;
  }

  // Each plane need Context in order to access the resources and get their colours.
  private Context mContext;

  // The Listener that will be receiving notifications from this class.
  private OnAerodromeEventListener mListener;

  // Paints used in the view.
  private Paint mBackgroundPaint = new Paint();
  private Paint mSitePaint = new Paint();

  // Game instance that contains the Model data.
  private Game mGame = Game.getInstance();

  // Aerodrome Matrix used to fit the model into the view.
  private Matrix mAerodrome = new Matrix();
  private boolean mIsAerodromeLoaded = false;
  private float mScale = 1f;

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
   * Auxiliary method used to scale the Aerodrome Matrix in order to fit the given screen space.
   * Note that this method is only called once, as the view's size is constant throughout the game.
   * This Matrix helps mapping between Aerodrome and Canvas Coordinates.
   */
  public void prepareAerodrome() {
    float scaleX = getWidth() / (float) (mGame.getAerodrome().getWidth());
    float scaleY = getHeight() / (float) (mGame.getAerodrome().getHeight());
    mAerodrome.postScale(scaleX, scaleY);
    mIsAerodromeLoaded = true;
    // Since the Aerodrome is square, either scale can be used. Should the aerodrome be rectangular,
    // the scale would have to be chosen in order to fit the aerodrome in the screen.
    mScale = scaleX;
    if (mListener != null) {
      mListener.onAerodromeReady();
    }
  }

  /**
   * Restores the Aerodrome's view. Clear it's drawables and returns the background to it's original
   * color.
   */
  public void cleanView() {
    mDrawables.clear();
    mSiteDrawables.clear();
    mBackgroundPaint.setColor(getResources().getColor(R.color.grass));
  }

  /**
   * Remove a Drawable from the Collection, given it's ID. This operation is synchronized to keep it
   * thread-safe.
   *
   * @param id  Drawable Aircraft to be removed.
   */
  public void removeAircraftDrawableById(int id) {
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

  /**
   * Update the position of all aircraft.
   */
  public void onAircraftPositionChanged() {
    // Synchronize the Drawable's list, so we make sure we update all the positions safely.
    synchronized (mDrawables.values()) {
      for (int id : mGame.getAircraftPositionMap().keySet()) {
        mDrawables.get(id).setPosition(mGame.getAircraftPositionMap().get(id));
      }
    }
    postInvalidate();
  }

  /**
   * Change the visual state of the given aircraft.
   *
   * @param id      ID of the target aircraft.
   * @param state   Selection state to represent.
   */
  public void onAircraftSelect(int id, boolean state) {
    synchronized (mDrawables.values()) {
      // Select or deselect the aircraft with the matched ID. The rest is deselected. Only one can
      // be selected at a time.
      for (AircraftDrawable aircraftDrawable : mDrawables.values()) {
        if (aircraftDrawable.getId() == id) {
          aircraftDrawable.select(state);
        } else {
          aircraftDrawable.select(false);
        }
      }
    }
    postInvalidate();
  }

  /**
   * Visual effect of the game over.
   */
  public void onGameOver() {
    // Game Over visual effect.
    mBackgroundPaint.setColor(getResources().getColor(R.color.landingSite));
    postInvalidate();
  }

  /**
   * Create the visual representation of the given Long Runway and add it to the list of landing
   * site drawables.
   *
   * @param longRunway  The long runway created.
   */
  public void onLongRunwayCreated(LongRunway longRunway) {
    mSiteDrawables.add(new LongRunwayDrawable(mContext, mScale, longRunway));
  }

  /**
   * Create the visual representation of the given Short Runway and add it to the list of landing
   * site drawables.
   *
   * @param shortRunway  The short runway created.
   */
  public void onShortRunwayCreated(ShortRunway shortRunway) {
    mSiteDrawables.add(new ShortRunwayDrawable(mContext, mScale, shortRunway));
  }

  /**
   * Create the visual representation of the given Helipad and add it to the list of landing
   * site drawables.
   *
   * @param helipad  The helipad created.
   */
  public void onHelipadCreated(Helipad helipad) {
    mSiteDrawables.add(new HelipadDrawable(mContext, mScale, helipad));
  }

  /**
   * Create the visual representation of the given Large Plane and add it to the list of aircraft
   * drawables.
   *
   * @param largePlane  The large plane created.
   */
  public void onLargePlaneGenerated(LargePlane largePlane) {
    synchronized (mDrawables.values()) {
      mDrawables.put(largePlane.getId(), new LargePlaneDrawable(mContext, mScale, largePlane));
    }
    postInvalidate();
  }

  /**
   * Create the visual representation of the given Light Plane and add it to the list of aircraft
   * drawables.
   *
   * @param lightPlane  The light plane created.
   */
  public void onLightPlaneGenerated(LightPlane lightPlane) {
    synchronized (mDrawables.values()) {
      mDrawables.put(lightPlane.getId(), new LightPlaneDrawable(mContext, mScale, lightPlane));
    }
    postInvalidate();
  }

  /**
   * Create the visual representation of the given Helicopter and add it to the list of aircraft
   * drawables.
   *
   * @param helicopter  The helicopter created.
   */
  public void onHelicopterGenerated(Helicopter helicopter) {
    synchronized (mDrawables.values()) {
      mDrawables.put(helicopter.getId(), new HelicopterDrawable(mContext, mScale, helicopter));
    }
    postInvalidate();
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    switch (event.getActionMasked()) {
      case MotionEvent.ACTION_DOWN:
        if (mListener != null) {
          // Map the coordinates into the Aerodrome.
          Position aerodromePosition = getAerodromePosition(event.getX(), event.getY());
          mListener.onAerodromeTapped(aerodromePosition);
        }
        return true;
    }
    return super.onTouchEvent(event);
  }
}
