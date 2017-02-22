package mas.german.landingplanes.view;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import mas.german.landingplanes.Position;
import mas.german.landingplanes.aircrafts.Aircraft;

/**
 * Abstract class to represent all types of aircraft to be drawn on the View.
 */
public abstract class AircraftDrawable extends Drawable {
  private static final String TAG = AircraftDrawable.class.getSimpleName();

  // ID of the Model Aircraft
  private int mId;

  // Position of the Drawable.
  private float mX;
  private float mY;

  // Radius of the Aircraft
  private float mRadius;

  // Paint to represent the Aircraft
  private Paint mPaint;

  protected void setId(int id) {
    mId = id;
  }

  protected int getId() {
    return mId;
  }

  protected void setPosition(Position position) {
    mX = (float) position.getX();
    mY = (float) position.getY();
  }

  protected Position getPosition() {
    return new Position(mX, mY);
  }

  protected void setRadius(float radius) {
    mRadius = radius;
  }

  protected float getRadius() {
    return mRadius;
  }

  protected void setPaintColor(int color) {
    mPaint = new Paint();
    mPaint.setStyle(Paint.Style.FILL);
    mPaint.setColor(color);
  }

  @Override
  public void draw(Canvas canvas) {
    canvas.save();
    canvas.drawCircle(mX, mY, mRadius, mPaint);
    canvas.restore();
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
