package mas.german.landingplanes.Aircrafts;

import mas.german.landingplanes.LandingSites.LandingSite;
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

    public boolean land(LandingSite site) {
        return (getPos().distanceTo(site.getPos()) <= getRadius()) && (site.accept(this));
    }
}
