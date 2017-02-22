package mas.german.landingplanes.view;

import android.content.Context;
import android.graphics.Canvas;
import mas.german.landingplanes.R;
import mas.german.landingplanes.landingsites.Helipad;

/**
 * A class that extends from LandingSiteDrawable and represents a Helipad.
 */
public class HelipadDrawable extends LandingSiteDrawable {
  private static final String TAG = HelipadDrawable.class.getSimpleName();

  HelipadDrawable(Context context, Helipad runway) {
    setPosition(runway.getPosition());
    setPaintColor(context.getResources().getColor(R.color.landingSite));
  }

  @Override
  public void draw(Canvas canvas) {
    canvas.save();
    canvas.drawCircle((float) getPosition().getX(), (float) getPosition().getY(), WIDTH/2,
        getPaint());
    canvas.restore();
  }
}
