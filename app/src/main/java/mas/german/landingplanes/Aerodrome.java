package mas.german.landingplanes;

import mas.german.landingplanes.aircrafts.Aircraft;

/**
 * Class that sets to the space where the aircraft move and the landing sites stand.
 * Contains methods to determine if a position or aircraft is outside it's boundaries.
 * The start of coordinates is considered the lower left corner of the screen, with the x-axis
 * positive to the right, and the y-axis positive towards the top.
 */
public class Aerodrome {
    private static final String TAG = Aerodrome.class.getSimpleName();

    // Boundaries of the aerodrome.
    private final double mBoundaryLeft;
    private final double mBoundaryTop;
    private final double mBoundaryRight;
    private final double mBoundaryBottom;

    Aerodrome(double left, double top, double right, double bottom) {
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

    public double getWidth() {
        return mBoundaryRight - mBoundaryLeft;
    }

    public double getHeight() {
        return mBoundaryTop - mBoundaryBottom;
    }

    /**
     * Returns whether a position is outside the aerodrome or not.
     * Note: This method is private as it checks only position. A public method is available for
     * checking individual aircraft.
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
     * Returns whether an aircraft is outside the aerodrome or not.
     */
    public boolean isOutOfBounds(Aircraft aircraft) {
        if (isOutOfBounds(aircraft.getPosition())) {
            return true;
        } else {
            return false;
        }
    }
}