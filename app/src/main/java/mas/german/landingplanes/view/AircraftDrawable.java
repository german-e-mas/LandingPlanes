package mas.german.landingplanes.view;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import mas.german.landingplanes.Position;

/**
 * Abstract class to represent all types of aircraft to be drawn on the View.
 */
public abstract class AircraftDrawable extends Drawable {
  private static final String TAG = AircraftDrawable.class.getSimpleName();
  private static final float SIZE_MODIFIER = 1.5f;
  private static final int ALPHA_SELECTED = 70;
  private static final int ALPHA_OPAQUE = 255;

  // ID of the Model Aircraft.
  private int mId;

  // Position of the Drawable.
  private float mX;
  private float mY;

  // Radius of the Aircraft.
  private float mRadius;

  // Paint to represent the Aircraft.
  private Paint mPaint;

  // Drawable Selection Flag.
  private boolean mSelected = false;

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

  protected void select() {
    mSelected = true;
  }

  protected void deselect() {
    mSelected = false;
  }

  @Override
  public void draw(Canvas canvas) {
    canvas.save();
    // A bigger, transparent circle is drawn to provide selection feedback.
    if (mSelected) {
      mPaint.setAlpha(ALPHA_SELECTED);
      canvas.drawCircle(mX, mY, mRadius * SIZE_MODIFIER, mPaint);
    }
    mPaint.setAlpha(ALPHA_OPAQUE);
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
