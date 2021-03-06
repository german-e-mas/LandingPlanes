package mas.german.landingplanes.landingsites;

import mas.german.landingplanes.aircrafts.Helicopter;
import mas.german.landingplanes.aircrafts.LargePlane;
import mas.german.landingplanes.aircrafts.LightPlane;
import mas.german.landingplanes.Position;

/**
 * Represents a Helipad, which is a Landing Site.
 * Only helicopters can land on it.
 */
public class Helipad extends LandingSite {
    private static final String TAG = Helipad.class.getSimpleName();

    // Helipads can accept helicopters from all angles.
    public Helipad(Position pos) {
        super(pos, 0, 2 * Math.PI);
    }

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
