package mas.german.landingplanes.view;

import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import mas.german.landingplanes.Position;

/**
 * Abstract class to represent all types of landing sites to be drawn on the View.
 */
public abstract class LandingSiteDrawable extends Drawable {
  private static final String TAG = LandingSiteDrawable.class.getSimpleName();
  // Width measure in Aerodrome Units.
  protected static final float WIDTH = 6f;

  // Entrance Position of the Landing Site.
  private float mX;
  private float mY;

  // Paint to represent the Landing Site.
  private Paint mPaint;

  protected void setPosition(Position position) {
    mX = (float) position.getX();
    mY = (float) position.getY();
  }

  protected Paint getPaint() {
    return mPaint;
  }

  protected Position getPosition() {
    return new Position(mX, mY);
  }

  protected void setPaintColor(int color) {
    mPaint = new Paint();
    mPaint.setStyle(Paint.Style.FILL);
    mPaint.setColor(color);
  }

  @Override
  public void setAlpha(int i) {
    // Currently not implemented, but it must be overriden regardless.
  }

  @Override
  public void setColorFilter(ColorFilter colorFilter) {
    // Currently not implemented, but it must be overriden regardless.
  }

  @Override
  public int getOpacity() {
    // Currently not implemented, but it must be overriden regardless.
    return PixelFormat.UNKNOWN;
  }
}
