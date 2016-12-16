package mas.german.landingplanes.aircrafts;

import mas.german.landingplanes.landingsites.LandingSite;
import mas.german.landingplanes.Position;

/**
 * Represents an helicopter, which is an Aircraft.
 * It can only land on helipads.
 */
public class Helicopter extends Aircraft {
    private static final String TAG = Helicopter.class.getSimpleName();

    public Helicopter(int speed, double direction, Position pos) {
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
