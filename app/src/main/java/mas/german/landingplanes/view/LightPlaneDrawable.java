package mas.german.landingplanes.view;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import mas.german.landingplanes.Position;
import mas.german.landingplanes.R;
import mas.german.landingplanes.aircrafts.Aircraft;

/**
 * A class that extends from AircraftDrawable and represents a Large Plane.
 */
public class LightPlaneDrawable extends AircraftDrawable {
  private static final String TAG = LightPlaneDrawable.class.getSimpleName();

  LightPlaneDrawable(int id, Position position, float radius) {
    setId(id);
    setPosition(position);
    setRadius(radius);
    setPaintColor(R.color.lightAircraft);
  }

  /**
   * Updates the position of the drawable in order to reflect the Model data.
   * @param aircraft  The Aircraft Model to represent.
   */
  @Override
  protected void updateData(Aircraft aircraft) {
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
    //
  }

  @Override
  public void setColorFilter(ColorFilter colorFilter) {
    //
  }

  @Override
  public int getOpacity() {
    return PixelFormat.UNKNOWN;
  }
}
