package mas.german.landingplanes.landingsites;

import mas.german.landingplanes.aircrafts.Helicopter;
import mas.german.landingplanes.aircrafts.LargePlane;
import mas.german.landingplanes.aircrafts.LightPlane;
import mas.german.landingplanes.Position;

/**
 * Represents a Short Runway, which is a Landing Site.
 * Only light planes can land on it.
 */
public class ShortRunway extends LandingSite {
    private static final String TAG = ShortRunway.class.getSimpleName();

    public ShortRunway(Position pos, double centerAngle, double apertureAngle) {
        super(pos, centerAngle, apertureAngle);
    }

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
