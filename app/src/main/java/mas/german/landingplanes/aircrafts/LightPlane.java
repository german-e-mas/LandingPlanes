package mas.german.landingplanes.aircrafts;

import mas.german.landingplanes.landingsites.LandingSite;
import mas.german.landingplanes.Position;

/**
 * Represents a light plane, which is an Aircraft.
 * It can land on both long and short runways.
 */
public class LightPlane extends Aircraft {
    private static final String TAG = LightPlane.class.getSimpleName();

    public LightPlane(int speed, double direction, Position pos) {
        super(speed, direction, pos);
    }

    /**
     * The aircraft only lands on certain sites, and also only if it's close enough to it.
     */
    public boolean land(LandingSite site) {
        if ((getPosition().distanceTo(site.getPosition()) <= getRadius()) && (site.accept(this))) {
            return true;
        } else {
            return false;
        }
    }
}
