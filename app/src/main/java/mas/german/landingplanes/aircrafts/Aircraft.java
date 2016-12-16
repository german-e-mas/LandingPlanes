package mas.german.landingplanes.aircrafts;

import mas.german.landingplanes.landingsites.LandingSite;
import mas.german.landingplanes.Position;

/**
 * Represents an Aircraft. Specific aircraft types should extend from this class.
 * An Aircraft has speed, direction and size. An Aircraft can land on Landing Sites.
 */
public abstract class Aircraft {
    private static final String TAG = Aircraft.class.getSimpleName();
    // Counter of the number of Aircrafts instances.
    private static int sAircraftsCreated = 0;

    // ID of the current Aircraft..
    private int mId;
    // Length of the speed vector.
    private int mSpeed;
    // Direction is the angle of the speed vector in radians.
    private double mDirection;
    // Radius of the Aircraft.
    private int mRadius;
    // Position in the Map.
    private Position mPosition;

    Aircraft(int speed, double direction, Position position) {
        sAircraftsCreated++;
        mId = sAircraftsCreated;
        mSpeed = speed;
        mDirection = direction;
        mPosition = position;
    }

    public int getRadius() {
        return mRadius;
    }

    public void setRadius(int radius) {
        mRadius = radius;
    }

    public Position getPosition() {
        return mPosition;
    }

    /**
     * Updates the aircraft's position according to it's speed and direction.
     */
    public void moveForward() {
        Position deltaPosition = new Position(mSpeed * Math.cos(mDirection),
                mSpeed * Math.sin(mDirection));
        mPosition.add(deltaPosition);
    }

    /**
     * Crash with another Aircraft if it's within range.
     */
    public boolean crashesWith(Aircraft otherAircraft) {
        if ((!this.equals(otherAircraft)) && (otherAircraft.mPosition.distanceTo(mPosition)
                <= (mRadius + otherAircraft.mRadius))) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Check if the Aircraft is farther than the position bounds.
     */
    public boolean isOutOfBounds() {
        return mPosition.isOutOfBounds();
    }

    public abstract boolean land(LandingSite site);

    public boolean equals(Aircraft aircraft) {
        if (mId == aircraft.mId) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getClass().getSimpleName()).append(" Id=").append(mId);
        stringBuilder.append(" Spd=").append(mSpeed).append(" Dir=").append(mDirection);
        stringBuilder.append(" Rad=").append(mRadius).append(" Pos=").append(mPosition.toString());
        return stringBuilder.toString();
    }
}
