package mas.german.landingplanes;

import mas.german.landingplanes.aircrafts.Aircraft;

public class Map {
    private static final String TAG = Position.class.getSimpleName();
    // Boundaries of the map.
    private static final double BOUNDARY_LEFT = 0;
    private static final double BOUNDARY_UP = 100;
    private static final double BOUNDARY_RIGHT = 100;
    private static final double BOUNDARY_DOWN = 0;

    /**
     * Returns whether a position is outside the map or not.
     */
    static boolean isOutOfBounds(Position position) {
        double x = position.getX();
        double y = position.getY();

        if ((x < BOUNDARY_LEFT) || (x > BOUNDARY_RIGHT) || (y < BOUNDARY_DOWN) ||
                (y > BOUNDARY_UP)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns whether an aircraft is outside the map or not.
     */
    static boolean isOutOfBounds(Aircraft aircraft) {
        if (isOutOfBounds(aircraft.getPosition())) {
            return true;
        } else {
            return false;
        }
    }
}