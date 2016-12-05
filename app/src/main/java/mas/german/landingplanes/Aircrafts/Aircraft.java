package mas.german.landingplanes.Aircrafts;

import android.util.Log;

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

    Aircraft(int speed, double direction, Position pos) {
        mSpeed = speed;
        mDirection = direction;
        mPos = pos;
    }

    public void setSpeed(int speed) {
        mSpeed = speed;
    }

    public void setDirection(double direction) {
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

    /**
     * Updates the aircraft's position according to it's speed and direction.
     */
    public void move() {
        mPos.setX(mPos.getX() + (int) (mSpeed * Math.cos(mDirection)));
        mPos.setY(mPos.getY() + (int) (mSpeed * Math.sin(mDirection)));
    }

    public boolean crashesWith(Aircraft otherAircraft) {
        return ((!this.equals(otherAircraft)) &&
                (otherAircraft.getPos().distanceTo(mPos)) <= (mRadius + otherAircraft.getRadius()));
    }

    public abstract boolean land(LandingSite site);

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getClass().getSimpleName()).append(" Spd=").append(mSpeed).append(" Dir=");
        stringBuilder.append(mDirection).append(" Rad=").append(mRadius).append(" Pos=");
        stringBuilder.append(mPos.toString());
        return stringBuilder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Aircraft aircraft = (Aircraft) o;

        if (mSpeed != aircraft.mSpeed) return false;
        if (Double.compare(aircraft.mDirection, mDirection) != 0) return false;
        if (mRadius != aircraft.mRadius) return false;
        return mPos != null ? mPos.equals(aircraft.mPos) : aircraft.mPos == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = mSpeed;
        temp = Double.doubleToLongBits(mDirection);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + mRadius;
        result = 31 * result + (mPos != null ? mPos.hashCode() : 0);
        return result;
    }
}
