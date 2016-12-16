package mas.german.landingplanes.landingsites;

import mas.german.landingplanes.aircrafts.Helicopter;
import mas.german.landingplanes.aircrafts.LargePlane;
import mas.german.landingplanes.aircrafts.LightPlane;
import mas.german.landingplanes.Position;

/**
 * Represents a Landing Site. Specific landing sites should extend from this class.
 * A landing site has a size (width and height) and can accept Aircrafts for landing on them.
 */
public abstract class LandingSite {
    private static final String TAG = LandingSite.class.getSimpleName();

    LandingSite(Position position) {
        mPosition = position;
    }

    private Position mPosition;

    public Position getPosition() {
        return mPosition;
    }

    public abstract boolean accept(LargePlane largePlane);

    public abstract boolean accept(LightPlane lightPlane);

    public abstract boolean accept(Helicopter helicopter);
}
