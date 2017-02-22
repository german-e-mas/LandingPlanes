package mas.german.landingplanes.aircrafts;

import mas.german.landingplanes.Game;
import mas.german.landingplanes.landingsites.LandingSite;
import mas.german.landingplanes.Position;

/**
 * Represents an helicopter, which is an Aircraft.
 * It can only land on helipads.
 */
public class Helicopter extends Aircraft {
    private static final String TAG = Helicopter.class.getSimpleName();

    public static final double MAX_SPEED = 4;
    public static final double MIN_SPEED = 2.5;
    private static final int RADIUS = 3;

    public Helicopter(double speed, double direction, Position pos) {
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
        game.createdHelicopter(this);
    }
}
