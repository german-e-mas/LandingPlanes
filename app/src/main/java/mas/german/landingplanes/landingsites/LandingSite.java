package mas.german.landingplanes.landingsites;

import mas.german.landingplanes.aircrafts.Helicopter;
import mas.german.landingplanes.aircrafts.LargePlane;
import mas.german.landingplanes.aircrafts.LightPlane;
import mas.german.landingplanes.Position;

/**
 * Represents a Landing Site. Specific landing sites should extend from this class.
 * A landing site has a size (width and height) and can accept Aircrafts for landing on them.
 */
public abstract class LandingSite {
    private static final String TAG = LandingSite.class.getSimpleName();

    // Entrance position.
    private Position mPosition;

    // Entrance opening angle, in radians. The opening is centerAngle +/- deltaAngle.
    private double mCenterAngle;
    private double mApertureAngle;

    LandingSite(Position position, double centerAngle, double apertureAngle) {
        mPosition = position;
        mCenterAngle = centerAngle;
        mApertureAngle = apertureAngle;
    }

    public Position getPosition() {
        return mPosition;
    }

    /**
     * Verify if a given angle can enter the landing site safely.
     *
     * @param direction The angle in radians to verify.
     */
    public boolean verifyDirection(double direction) {
        double upperLimit = mCenterAngle + mApertureAngle / 2;
        double lowerLimit = mCenterAngle - mApertureAngle / 2;
        // For ease of calculation, map the direction from (0,2*PI) to (-PI, PI)
        if (direction > Math.PI) {
            direction -= 2 * Math.PI;
        }

        if ((direction <= upperLimit) && (direction >= lowerLimit)) {
            return true;
        } else {
            return false;
        }
    }

    public double getCenterAngle() {
        return mCenterAngle;
    }

    public abstract boolean accept(LargePlane largePlane);

    public abstract boolean accept(LightPlane lightPlane);

    public abstract boolean accept(Helicopter helicopter);
}
