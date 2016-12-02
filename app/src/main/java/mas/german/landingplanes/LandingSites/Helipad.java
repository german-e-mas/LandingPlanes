package mas.german.landingplanes.LandingSites;

import mas.german.landingplanes.Aircrafts.Helicopter;
import mas.german.landingplanes.Aircrafts.LargePlane;
import mas.german.landingplanes.Aircrafts.LightPlane;

/**
 * Represents a Helipad, which is a Landing Site.
 * Only helicopters can land on it.
 */
public class Helipad extends LandingSite {
    private static final String TAG = Helipad.class.getSimpleName();

    public boolean accept(LargePlane largePlane) {
        // Planes can't land on Helipads.
        return false;
    }

    public boolean accept(LightPlane lightPlane) {
        // Planes can't land on Helipads.
        return false;
    }

    public boolean accept(Helicopter helicopter) {
        // Only Helicopters can land on Helipads.
        return true;
    }
}
