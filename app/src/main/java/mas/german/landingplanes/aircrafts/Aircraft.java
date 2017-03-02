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
    // Counter of the number of Aircraft instances. Used for assigning the IDs.
    private static int sAircraftCreated = 0;

    // ID of the current Aircraft. Used Integer for simplicity.
    private int mId;
    // Length of the speed vector, in Aerodrome Units per millisecond.
    private double mSpeed;
    // Direction is the angle of the speed vector in radians.
    private double mDirection;
    // Radius of the Aircraft.
    private int mRadius;
    // Position in the Aerodrome.
    private Position mPosition;
    // Selection behaviour.
    private boolean mSelected;

    Aircraft(double speed, double direction, Position position, int radius) {
        sAircraftCreated++;
        mId = sAircraftCreated;
        mSpeed = speed;
        mDirection = direction;
        mPosition = position;
        mRadius = radius;
        mSelected = false;
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

    public double getDirection() {
        return mDirection;
    }

    public void select(boolean state) {
        mSelected = state;
    }

    public boolean isSelected() {
        return mSelected;
    }

    /**
     * Updates the aircraft's position according to it's speed and direction.
     *
     * @param sampleTime    Time span in milliseconds, used to calculate the distance moved.
     */
    public void moveForward(long sampleTime) {
        Position deltaPosition = new Position(mSpeed * sampleTime * Math.cos(mDirection),
            mSpeed * sampleTime * Math.sin(mDirection));
        mPosition.add(deltaPosition);
    }

    /**
     * Modifies the direction towards a position.
     *
     * @param position  The position to point the Aircraft to.
     */
    public void changeDirection(Position position) {
        double u = position.getX() - mPosition.getX();
        double v = position.getY() - mPosition.getY();
        mDirection = Math.atan2(v, u);
        // Aircrafts are generated using angles between 0 and 2*PI radians.
        if (mDirection < 0) {
            mDirection += 2 * Math.PI;
        }
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
