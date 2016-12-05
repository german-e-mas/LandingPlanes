package mas.german.landingplanes.Aircrafts;

import mas.german.landingplanes.LandingSites.LandingSite;
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

    public boolean land(LandingSite site) {
        return (getPos().distanceTo(site.getPos()) <= getRadius()) && (site.accept(this));
    }
}
