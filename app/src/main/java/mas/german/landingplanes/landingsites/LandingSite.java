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
        // Center and Aperture angles are given between [0, 2*PI), which is the same range as the
        // direction.
        // For ease of calculation in the verification, map the angles from [0, 2*PI) to [-PI, PI)
        // and calculate their difference, which is mapped as well.
        double angleDifference = mapAngle(direction) - mapAngle(mCenterAngle);
        // The difference can fall off the limits. Map it between [-PI, PI)
        angleDifference = mapAngle(angleDifference);
        // The limits take the center as zero. Double precision isn't required.
        float upperLimit = (float) mApertureAngle / 2;
        float lowerLimit = (float) -mApertureAngle / 2;
        // Check if the resulting direction falls between the limits.
        if ((angleDifference <= upperLimit) && (angleDifference >= lowerLimit)) {
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

    /**
     * Auxiliary method to map an angle between [0, 2*PI) to [-PI, PI).
     *
     * @param angle Angle between [0, 2*PI).
     * @return The given angle between [-PI, PI)
     */
    private double mapAngle(double angle) {
        double mappedAngle = angle;
        if (angle < -Math.PI) {
            mappedAngle = angle + 2 * Math.PI;
        }
        if (angle >= Math.PI) {
            mappedAngle = angle - 2 * Math.PI;
        }
        return mappedAngle;
    }
}
