package mas.german.landingplanes.view;

import android.content.Context;
import android.graphics.Canvas;
import mas.german.landingplanes.R;
import mas.german.landingplanes.landingsites.LongRunway;

/**
 * A class that extends from LongRunwayDrawable and represents a Long Runway.
 */
public class LongRunwayDrawable extends LandingSiteDrawable {
  private static final String TAG = LongRunwayDrawable.class.getSimpleName();
  private static final float LENGTH_MULTIPLIER = 6;

  // Dimensions of the Runway. Width is common to all.
  private float mLength;

  // Facing direction.
  private double mAngle;

  LongRunwayDrawable(Context context, LongRunway runway) {
    setPosition(runway.getPosition());
    setPaintColor(context.getResources().getColor(R.color.landingSite));
    mLength = LENGTH_MULTIPLIER * WIDTH;
    // Drawable faces the opposite direction of the center Angle.
    mAngle = runway.getCenterAngle() - Math.PI;
  }

  @Override
  public void draw(Canvas canvas) {
    canvas.save();
    canvas.translate((float) getPosition().getX(), (float) getPosition().getY());
    canvas.rotate((float) Math.toDegrees(mAngle));
    canvas.drawRect(-mLength, -WIDTH/2, 0, WIDTH/2, getPaint());
    canvas.restore();
  }
}
