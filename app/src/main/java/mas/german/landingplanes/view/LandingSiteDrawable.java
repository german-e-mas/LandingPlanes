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
  // Main measurement in Aerodrome Coordinates. This represents the width of runways and radius of
  // helipads.
  protected static final float MAIN_MEASUREMENT = 6f;

  // Entrance Position of the Landing Site, in Canvas Coordinates.
  private float mX;
  private float mY;

  // Paint to represent the Landing Site.
  private Paint mPaint;

  // Scale to convert from Aerodrome Coordinates into Canvas Coordinates.
  private float mScale = 1f;

  LandingSiteDrawable(float scale, Position position, int color) {
    mScale = scale;
    mX = (float) position.getX() * scale;
    mY = (float) position.getY() * scale;
    mPaint = new Paint();
    mPaint.setStyle(Paint.Style.FILL);
    mPaint.setColor(color);
  }

  protected void setPosition(Position position) {
    mX = (float) position.getX() * mScale;
    mY = (float) position.getY() * mScale;
  }

  protected Paint getPaint() {
    return mPaint;
  }

  protected Position getPosition() {
    return new Position(mX, mY);
  }

  /**
   * Get the main measurement of the landing site in Canvas Coordinates.
   */
  protected float getMeasurement() {
    return MAIN_MEASUREMENT * mScale;
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
