package mas.german.landingplanes.view;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
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

  // Position of the Drawable, in Canvas Coordinates.
  private float mX;
  private float mY;

  // Radius of the Aircraft, in Canvas Coordinates.
  private float mRadius;

  // Scale to convert from Aerodrome Coordinates into Canvas Coordinates.
  private float mScale = 1f;

  // Direction which the aircraft is facing, in radians.
  private double mDirection;

  // Representation of the Aircraft.
  private Paint mPaint;
  private Path mPath;

  // Drawable Selection Flag.
  private boolean mSelected = false;

  AircraftDrawable(int id, Position position, float radius, double direction, float scale, int color) {
    mId = id;
    mScale = scale;
    mX = (float) position.getX() * scale;
    mY = (float) position.getY() * scale;
    mRadius = radius * scale;
    mDirection = direction;
    mPaint = new Paint();
    mPaint.setStyle(Paint.Style.FILL);
    mPaint.setColor(color);
    mPath = new Path();
  }

  private void configurePath() {
    mPath.reset();
    mPath.moveTo(mX + mRadius, mY);
    mPath.lineTo(mX - 0.5f * mRadius, mY - mRadius * (float) Math.sin(Math.toRadians(60)));
    mPath.lineTo(mX - 0.25f * mRadius, mY);
    mPath.lineTo(mX - 0.5f * mRadius, mY + mRadius * (float) Math.sin(Math.toRadians(60)));
    mPath.close();
  }

  protected int getId() {
    return mId;
  }

  protected void setPosition(Position position) {
    // Change the direction accordingly.
    float x = (float) position.getX() * mScale;
    float y = (float) position.getY() * mScale;
    mDirection = Math.atan2(y - mY, x - mX);
    // Keep the angle positive.
    if (mDirection < 0) {
      mDirection += 2 * Math.PI;
    }
    mX = x;
    mY = y;
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

  protected void select(boolean state) {
    mSelected = state;
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
    configurePath();
    canvas.rotate((float) Math.toDegrees(mDirection), mX, mY);
    canvas.drawPath(mPath, mPaint);
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
