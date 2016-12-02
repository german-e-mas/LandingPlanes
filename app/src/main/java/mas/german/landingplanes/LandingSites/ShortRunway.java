package mas.german.landingplanes.LandingSites;

import mas.german.landingplanes.Aircrafts.Helicopter;
import mas.german.landingplanes.Aircrafts.LargePlane;
import mas.german.landingplanes.Aircrafts.LightPlane;

/**
 * Represents a Short Runway, which is a Landing Site.
 * Only light planes can land on it.
 */
public class ShortRunway extends LandingSite {
    private static final String TAG = ShortRunway.class.getSimpleName();

    public boolean accept(LargePlane largePlane) {
        // Large planes can't land on Short Runways.
        return false;
    }

    public boolean accept(LightPlane lightPlane) {
        // Light planes can land safely on a Short Runway.
        return true;
    }

    public boolean accept(Helicopter helicopter) {
        // Helicopters can't land on runways.
        return false;
    }
}
