package mas.german.landingplanes.aircrafts;

import mas.german.landingplanes.landingsites.LandingSite;
import mas.german.landingplanes.Position;

/**
 * Represents a large plane, which is an Aircraft.
 * It can only land on long runways.
 */
public class LargePlane extends Aircraft {
    private static final String TAG = LargePlane.class.getSimpleName();

    public LargePlane(int speed, double direction, Position pos) {
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
