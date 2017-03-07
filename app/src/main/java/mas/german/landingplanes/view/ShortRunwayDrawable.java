package mas.german.landingplanes.view;

import android.content.Context;
import android.graphics.Canvas;
import mas.german.landingplanes.R;
import mas.german.landingplanes.landingsites.ShortRunway;

/**
 * A class that extends from LandingSiteDrawable and represents a Short Runway.
 */
public class ShortRunwayDrawable extends LandingSiteDrawable {
  private static final String TAG = ShortRunwayDrawable.class.getSimpleName();
  private static final float LENGTH_MULTIPLIER = 4;

  // Dimensions of the Runway. Width is common to all.
  private float mLength;

  // Facing direction.
  private double mAngle;

  ShortRunwayDrawable(Context context, float scale, ShortRunway runway) {
    super(scale, runway.getPosition(), context.getResources().getColor(R.color.landingSite));
    mLength = LENGTH_MULTIPLIER * getWidth();
    // Drawable faces the opposite direction of the center Angle.
    mAngle = runway.getCenterAngle() - Math.PI;
  }

  @Override
  public void draw(Canvas canvas) {
    canvas.save();
    canvas.translate((float) getPosition().getX(), (float) getPosition().getY());
    canvas.rotate((float) Math.toDegrees(mAngle));
    canvas.drawRect(-mLength, -getWidth()/2, 0, getWidth()/2, getPaint());
    canvas.restore();
  }
}
