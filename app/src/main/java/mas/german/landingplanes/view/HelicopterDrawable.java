package mas.german.landingplanes.view;

import android.content.Context;
import mas.german.landingplanes.R;
import mas.german.landingplanes.aircrafts.Aircraft;

/**
 * A class that extends from AircraftDrawable and represents a Large Plane.
 */
public class HelicopterDrawable extends AircraftDrawable {
  private static final String TAG = HelicopterDrawable.class.getSimpleName();

  HelicopterDrawable(Context context, float scale, Aircraft aircraft) {
    super(aircraft.getId(), aircraft.getPosition(), aircraft.getRadius(), aircraft.getDirection(),
        scale, context.getResources().getColor(R.color.helicopter));
  }
}
