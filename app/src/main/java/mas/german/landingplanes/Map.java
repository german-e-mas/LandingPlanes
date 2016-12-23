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
    private final double BOUNDARY_LEFT = 0;
    private final double BOUNDARY_UP = 100;
    private final double BOUNDARY_RIGHT = 100;
    private final double BOUNDARY_DOWN = 0;

    /**
     * Get the unique instance of the Map class.
     *
     * @return Map instance.
     */
    public static Map getInstance() {
        if (sInstance == null) {
            sInstance = new Map();
        }
        return sInstance;
    }

    private Map() {
        //
    }

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