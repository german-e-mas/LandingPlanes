package mas.german.landingplanes;

import android.util.Log;

/**
 * Represents an Aircraft. Specific aircraft types should extend from this class.
 * An Aircraft has speed, direction and size (it is modeled by a circle).
 * An Aircraft can land on Landing Sites.
 */
public abstract class Aircraft {
    private static final String TAG = Aircraft.class.getSimpleName();

    private int mSpeed;
    private double mDirection;
    private int mRadius;

    public void setSpeed(int speed) {
        mSpeed = speed;
    }

    public void setDirection(int direction) {
        mDirection = direction;
    }

    public void setRadius(int radius) {
        mRadius = radius;
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

    public abstract boolean land(LandingSite site);
}
