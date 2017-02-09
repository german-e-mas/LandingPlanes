package mas.german.landingplanes.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import mas.german.landingplanes.Game;
import mas.german.landingplanes.R;

/**
 * This view represents the field where the landing sites stand and where the aircraft fly.
 */
public class AerodromeView extends ImageView implements Game.Listener {
  private static final String TAG = AerodromeView.class.getSimpleName();

  private Paint mGrassPaint = new Paint();

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
    mGrassPaint.setColor(context.getResources().getColor(R.color.grass));
  }

  @Override
  protected void onDraw(Canvas canvas) {
    canvas.drawRect(getLeft(), getTop(), getRight(), getBottom(), mGrassPaint);

  }

  @Override
  public void onUpdate() {
    Log.e(TAG, "Update!");
  }

  @Override
  public void onNewPlane() {
    Log.e(TAG, "New Plane!");
  }

  @Override
  public void onCrash() {
    Log.e(TAG, "CRASH!!");
  }

  @Override
  public void onGameOver() {
    Log.e(TAG, "Game Over!");
  }

  @Override
  public void onLand() {
    Log.e(TAG, "Plane landed..");
  }
}
