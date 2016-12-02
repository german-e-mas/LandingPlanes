package mas.german.landingplanes.Aircrafts;

import mas.german.landingplanes.LandingSites.LandingSite;
import mas.german.landingplanes.Position;

/**
 * Represents an Aircraft. Specific aircraft types should extend from this class.
 * An Aircraft has speed, direction and size. An Aircraft can land on Landing Sites.
 */
public abstract class Aircraft {
    private static final String TAG = Aircraft.class.getSimpleName();

    private int mSpeed;
    private double mDirection;
    private int mRadius;
    private Position mPos;

    public void setSpeed(int speed) {
        mSpeed = speed;
    }

    public void setDirection(int direction) {
        mDirection = direction;
    }

    public void setRadius(int radius) {
        mRadius = radius;
    }

    public void setPos(Position pos) {
        mPos = pos;
    }

    public int getSpeed() {
        return mSpeed;
    }

    public double getDirection() {
        return mDirection;
    }

    public int getRadius() {
        return mRadius;
    }

    public Position getPos() {
        return mPos;
    }

    public abstract boolean land(LandingSite site);
}
