package mas.german.landingplanes.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import mas.german.landingplanes.R;
import mas.german.landingplanes.aircrafts.Aircraft;

/**
 * A class that extends from AircraftDrawable and represents a Large Plane.
 */
public class LargePlaneDrawable extends AircraftDrawable {
  private static final String TAG = LargePlaneDrawable.class.getSimpleName();

  LargePlaneDrawable(Context context, Aircraft aircraft) {
    setId(aircraft.getId());
    setPosition(aircraft.getPosition());
    setRadius(aircraft.getRadius());
    setPaintColor(context.getResources().getColor(R.color.largeAircraft));
  }

  /**
   * Updates the position of the drawable in order to reflect the Model data.
   * @param aircraft  The Aircraft Model to represent.
   */
  @Override
  protected void updatePosition(Aircraft aircraft) {
    setPosition(aircraft.getPosition());
  }

  @Override
  public void draw(Canvas canvas) {
    canvas.save();
    canvas.drawCircle((float) getPosition().getX(), (float) getPosition().getY(), getRadius(),
        getPaint());
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
