package mas.german.landingplanes.landingsites;

import mas.german.landingplanes.aircrafts.Helicopter;
import mas.german.landingplanes.aircrafts.LargePlane;
import mas.german.landingplanes.aircrafts.LightPlane;
import mas.german.landingplanes.Position;

/**
 * Represents a Long Runway, which is a Landing Site.
 * Large and light planes can land on it.
 */
public class LongRunway extends LandingSite {
    private static final String TAG = LongRunway.class.getSimpleName();

    public LongRunway(Position pos) {
        super(pos);
    }

    public boolean accept(LargePlane largePlane) {
        // Planes can land on Large Runways.
        return true;
    }

    public boolean accept(LightPlane lightPlane) {
        // Planes can land on Large Runways.
        return true;
    }

    public boolean accept(Helicopter helicopter) {
        // Helicopters can't land on runways.
        return false;
    }
}
