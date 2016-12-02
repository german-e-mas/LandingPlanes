package mas.german.landingplanes.LandingSites;

import mas.german.landingplanes.Aircrafts.Helicopter;
import mas.german.landingplanes.Aircrafts.LargePlane;
import mas.german.landingplanes.Aircrafts.LightPlane;

/**
 * Represents a Long Runway, which is a Landing Site.
 * Large and light planes can land on it.
 */
public class LongRunway extends LandingSite {
    private static final String TAG = LongRunway.class.getSimpleName();

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
