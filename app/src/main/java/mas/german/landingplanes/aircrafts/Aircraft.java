package mas.german.landingplanes.aircrafts;

import mas.german.landingplanes.Game;
import mas.german.landingplanes.landingsites.LandingSite;
import mas.german.landingplanes.Position;

/**
 * Represents an Aircraft. Specific aircraft types should extend from this class.
 * An Aircraft has speed, direction and size. An Aircraft can land on Landing Sites.
 */
public abstract class Aircraft {
    private static final String TAG = Aircraft.class.getSimpleName();
    // Counter of the number of Aircrafts instances. Used for assigning the IDs.
    private static int sAircraftsCreated = 0;

    // ID of the current Aircraft. Used Integer for simplicity.
    private int mId;
    // Length of the speed vector.
    private double mSpeed;
    // Direction is the angle of the speed vector in radians.
    private double mDirection;
    // Radius of the Aircraft.
    private int mRadius;
    // Position in the Map.
    private Position mPosition;

    Aircraft(double speed, double direction, Position position, int radius) {
        sAircraftsCreated++;
        mId = sAircraftsCreated;
        mSpeed = speed;
        mDirection = direction;
        mPosition = position;
        mRadius = radius;
    }

    public int getId() {
        return mId;
    }

    public int getRadius() {
        return mRadius;
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

    public abstract boolean land(LandingSite site);

    public abstract void notifyCreation(Game game);

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
        stringBuilder.append(" Spd=").append(mSpeed);
        stringBuilder.append(" Dir=").append(Math.toDegrees(mDirection));
        stringBuilder.append(" Rad=").append(mRadius).append(" Pos=").append(mPosition.toString());
        return stringBuilder.toString();
    }
}
