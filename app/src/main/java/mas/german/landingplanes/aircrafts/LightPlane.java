package mas.german.landingplanes.aircrafts;

import mas.german.landingplanes.Game;
import mas.german.landingplanes.landingsites.LandingSite;
import mas.german.landingplanes.Position;

/**
 * Represents a light plane, which is an Aircraft.
 * It can land on both long and short runways.
 */
public class LightPlane extends Aircraft {
    private static final String TAG = LightPlane.class.getSimpleName();

    // Speed is in Map Units per millisecond.
    public static final double MAX_SPEED = 0.045;
    public static final double MIN_SPEED = 0.025;
    private static final int RADIUS = 3;

    public LightPlane(double speed, double direction, Position pos) {
        super(speed, direction, pos, RADIUS);
    }

    /**
     * The aircraft only lands on certain sites, and also only if it's close enough to it.
     */
    public boolean land(LandingSite site) {
        if ((getPosition().distanceTo(site.getPosition()) <= getRadius()) &&
            (site.verifyDirection(getDirection())) && (site.accept(this))) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void notifyCreation(Game game) {
        game.createdLightPlane(this);
    }
}
