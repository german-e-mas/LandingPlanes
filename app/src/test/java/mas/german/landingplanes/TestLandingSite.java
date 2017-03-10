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

        // Issues occur mostly when the center and aperture range include 0° or 180°.
        // Center angle: 30°. Aperture: 180°. Range: [-60°/300°, 120°].
        center = Math.toRadians(30);
        aperture = Math.toRadians(180);
        testSite = new LongRunway(new Position(0, 0), center, aperture);
        // Verify with Center Angle
        assertTrue(testSite.verifyDirection(center));
        // Verify Limits. If the angle is negative, test it's positive one as well.
        assertTrue(testSite.verifyDirection(Math.toRadians(120)));
        assertTrue(testSite.verifyDirection(Math.toRadians(-60)));
        assertTrue(testSite.verifyDirection(Math.toRadians(300)));
        // Verify outside the range by +/- 10°.
        assertFalse(testSite.verifyDirection(Math.toRadians(130)));
        assertFalse(testSite.verifyDirection(Math.toRadians(-70)));
        assertFalse(testSite.verifyDirection(Math.toRadians(290)));

        // Center angle: 255°. Aperture: 270°. Range: [120°, 390°/30°].
        center = Math.toRadians(255);
        aperture = Math.toRadians(270);
        testSite = new LongRunway(new Position(0, 0), center, aperture);
        // Verify Center Angle
        assertTrue(testSite.verifyDirection(center));
        // Verify Limits. If the angle is greater than 360°, test it's equivalent.
        assertTrue(testSite.verifyDirection(Math.toRadians(390)));
        assertTrue(testSite.verifyDirection(Math.toRadians(30)));
        assertTrue(testSite.verifyDirection(Math.toRadians(120)));
        // Verify outside the range by +/- 30°.
        assertFalse(testSite.verifyDirection(Math.toRadians(60)));
        assertFalse(testSite.verifyDirection(Math.toRadians(90)));

        // A more normal test, where 0° or 180° is not contained within the range.
        // Center angle: 90°. Aperture: 90°. Range: [45°, 135°].
        center = Math.toRadians(90);
        aperture = Math.toRadians(90);
        testSite = new LongRunway(new Position(0, 0), center, aperture);
        // Verify Center Angle
        assertTrue(testSite.verifyDirection(center));
        // Verify Limits.
        assertTrue(testSite.verifyDirection(Math.toRadians(45)));
        assertTrue(testSite.verifyDirection(Math.toRadians(135)));
        // Verify outside the range by +/- 5°.
        assertFalse(testSite.verifyDirection(Math.toRadians(40)));
        assertFalse(testSite.verifyDirection(Math.toRadians(140)));

    }
}
