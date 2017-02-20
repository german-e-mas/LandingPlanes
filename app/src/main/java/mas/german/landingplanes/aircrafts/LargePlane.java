package mas.german.landingplanes.aircrafts;

import mas.german.landingplanes.Game;
import mas.german.landingplanes.landingsites.LandingSite;
import mas.german.landingplanes.Position;

/**
 * Represents a large plane, which is an Aircraft.
 * It can only land on long runways.
 */
public class LargePlane extends Aircraft {
    private static final String TAG = LargePlane.class.getSimpleName();

    // Speed is in Map Units per millisecond.
    public static final double MAX_SPEED = 0.05;
    public static final double MIN_SPEED = 0.035;
    private static final int RADIUS = 5;

    public LargePlane(double speed, double direction, Position pos) {
        super(speed, direction, pos, RADIUS);
    }

    /**
     * The aircraft only lands on certain sites, and also only if it's close enough to it.
     */
    public boolean land(LandingSite site) {
        if ((getPosition().distanceTo(site.getPosition()) <= getRadius()) && (site.accept(this))) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void notifyCreation(Game game) {
        game.createdLargePlane(this);
    }
}
