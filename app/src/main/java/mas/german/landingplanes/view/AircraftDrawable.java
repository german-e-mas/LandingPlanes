package mas.german.landingplanes.view;

import android.graphics.Paint;
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
  private double mX;
  private double mY;

  // Radius of the Aircraft
  private float mRadius;

  // Paint to represent the Aircraft
  private Paint mPaint;

  protected void setId(int id) {
    mId = id;
  }

  protected void setPosition(Position position) {
    mX = position.getX();
    mY = position.getY();
  }

  protected void setRadius(float radius) {
    mRadius = radius;
  }

  protected void setPaintColor(int color) {
    mPaint = new Paint();
    mPaint.setColor(color);
  }

  protected Position getPosition() {
    return new Position(mX, mY);
  }

  protected float getRadius() {
    return mRadius;
  }

  protected Paint getPaint() {
    return mPaint;
  }

  // Update the Drawable according to the Model Data.
  protected abstract void updateData(Aircraft aircraft);
}
