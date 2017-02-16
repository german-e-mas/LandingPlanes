package mas.german.landingplanes.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import mas.german.landingplanes.Game;
import mas.german.landingplanes.R;
import mas.german.landingplanes.aircrafts.Aircraft;

/**
 * This view represents the field where the landing sites stand and where the aircraft fly.
 */
public class AerodromeView extends ImageView implements Game.Listener {
  private static final String TAG = AerodromeView.class.getSimpleName();

  // The background Paint.
  private Paint mBackgroundPaint = new Paint();

  // Game instance that contains the Model data.
  private Game mGame = Game.getInstance();

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
    mBackgroundPaint.setColor(context.getResources().getColor(R.color.grass));
    mGame.setListener(this);
  }

  @Override
  protected void onDraw(Canvas canvas) {
    canvas.drawRect(getLeft(), getTop(), getRight(), getBottom(), mBackgroundPaint);

  }

  @Override
  public void onUpdate() {
    // Update.
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
  }

  @Override
  public void onCrash(int id1, int id2) {
    Log.d(TAG, "CRASH! Aircraft #" + id1 + " hit Aircraft #" + id2);
  }
}
