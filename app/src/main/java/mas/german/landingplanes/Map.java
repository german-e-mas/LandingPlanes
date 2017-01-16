package mas.german.landingplanes;

import mas.german.landingplanes.aircrafts.Aircraft;

/**
 * Singleton Class that sets to the space where the aircrafts move and the landing sites stand.
 * Contains methods to determine if a position or aircraft is outside the current boundaries.
 */
public class Map {
    private static final String TAG = Map.class.getSimpleName();

    private static Map sInstance = null;

    // Boundaries of the map.
    private final double mBoundaryLeft;
    private final double mBoundaryTop;
    private final double mBoundaryRight;
    private final double mBoundaryBottom;

    /**
     * Get the unique instance of the Map class.
     *
     * @return Map instance.
     */
    public static Map getInstance(double left, double top, double right, double bottom) {
        if (sInstance == null) {
            sInstance = new Map(left, top, right, bottom);
        }
        return sInstance;
    }

    private Map(double left, double top, double right, double bottom) {
        mBoundaryLeft = left;
        mBoundaryTop = top;
        mBoundaryRight = right;
        mBoundaryBottom = bottom;
    }

    public double getBoundaryLeft() {
        return mBoundaryLeft;
    }

    public double getBoundaryTop() {
        return mBoundaryTop;
    }

    public double getBoundaryRight() {
        return mBoundaryRight;
    }

    public double getBoundaryBottom() {
        return mBoundaryBottom;
    }

    /**
     * Returns whether a position is outside the map or not.
     * Note: This method is private as it checks only position. A public methods is available for
     * checking individual aircrafts.
     */
    private boolean isOutOfBounds(Position position) {
        double x = position.getX();
        double y = position.getY();

        if ((x < mBoundaryLeft) || (x > mBoundaryRight) || (y < mBoundaryBottom) ||
                (y > mBoundaryTop)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns whether an aircraft is outside the map or not.
     */
    public boolean isOutOfBounds(Aircraft aircraft) {
        if (isOutOfBounds(aircraft.getPosition())) {
            return true;
        } else {
            return false;
        }
    }
}