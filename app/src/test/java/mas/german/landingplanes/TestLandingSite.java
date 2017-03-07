package mas.german.landingplanes;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

import mas.german.landingplanes.aircrafts.*;
import mas.german.landingplanes.landingsites.*;
import org.junit.Test;

/**
 * Unit Tests related to Landing Sites.
 */
public class TestLandingSite {
    private static final double ACCEPTED_DELTA = 0.001d;

    /**
     * Test if the landing sites accept the aircraft they should.
     */
    @Test
    public void testAcceptance() {
        LandingSite testSite;
        Position originPosition = new Position(0, 0);
        LargePlane testLargePlane = new LargePlane(1, 0, new Position(0, 0));
        LightPlane testLightPlane = new LightPlane(1, 0, new Position(0, 0));
        Helicopter testHelicopter = new Helicopter(1, 0, new Position(0, 0));

        // Test Long Runways.
        testSite = new LongRunway(originPosition, 0, 0);
        assertTrue(testSite.accept(testLargePlane));
        assertTrue(testSite.accept(testLightPlane));
        assertFalse(testSite.accept(testHelicopter));

        // Test Short Runways.
        testSite = new ShortRunway(originPosition, 0, 0);
        assertFalse(testSite.accept(testLargePlane));
        assertTrue(testSite.accept(testLightPlane));
        assertFalse(testSite.accept(testHelicopter));

        // Test Helipad.
        testSite = new Helipad(originPosition);
        assertFalse(testSite.accept(testLargePlane));
        assertFalse(testSite.accept(testLightPlane));
        assertTrue(testSite.accept(testHelicopter));
    }

    /**
     * Test the allowed entrance directions.
     */
    @Test
    public void testDirections() {
        LandingSite testSite;
        double center;
        double aperture;
        double upperLimitAngle;
        double lowerLimitAngle;
        double direction;

        // Center and Aperture angles range from 0° to 360°.
        // Verify all the possible center and aperture angles. In each step they change 10°.
        for (center = 0; center < 2 * Math.PI; center += Math.toRadians(10)) {
            for (aperture = 0; aperture < 2 * Math.PI; aperture += Math.toRadians(10)) {
                // The direction changes only 1°. It ranges from -180° to 180° (which is the range
                // obtained by the atan2 function).
                for (direction = -Math.PI; direction < Math.PI; direction += Math.toRadians(1)) {
                    testSite = new LongRunway(new Position(0, 0), center, aperture);
                    upperLimitAngle = center + aperture / 2;
                    lowerLimitAngle = center - aperture / 2;

                    // To ease calculations, limits should be in the same range as the direction.
                    if (upperLimitAngle > Math.PI) {
                        upperLimitAngle -= 2 * Math.PI;
                    }
                    if (lowerLimitAngle > Math.PI) {
                        lowerLimitAngle -= 2 * Math.PI;
                    }

                    // Test the direction.
                    if ((direction <= upperLimitAngle) && (direction >= lowerLimitAngle)) {
                        assertTrue(testSite.verifyDirection(direction));
                    } else {
                        assertFalse(testSite.verifyDirection(direction));
                    }
                }
            }
        }
    }
}
