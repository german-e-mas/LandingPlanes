package mas.german.landingplanes;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

import mas.german.landingplanes.aircrafts.*;
import mas.german.landingplanes.landingsites.*;
import org.junit.Test;

/**
 * Unit Tests related to Aircraft.
 */
public class TestAircraft {
    private static final double ACCEPTED_DELTA = 0.001d;

    /**
     * Test the Aircraft movement. They should move at the correct speed, which is in Aerodrome
     * Units per millisecond. So, with speed 1, at 1 millisecond, they should move 1 unit.
     */
    @Test
    public void testMovement() {
        Aircraft testAircraft;

        // Test moving to the right (0°).
        testAircraft = new LargePlane(1, 0, new Position(0, 0));
        testAircraft.moveForward(1);
        assertEquals(1, testAircraft.getPosition().getX(), ACCEPTED_DELTA);
        assertEquals(0, testAircraft.getPosition().getY(), ACCEPTED_DELTA);

        // Test moving up (90°).
        testAircraft = new LargePlane(1, Math.PI / 2, new Position(0, 0));
        testAircraft.moveForward(1);
        assertEquals(0, testAircraft.getPosition().getX(), ACCEPTED_DELTA);
        assertEquals(1, testAircraft.getPosition().getY(), ACCEPTED_DELTA);

        // Test moving to the left (180°).
        testAircraft = new LargePlane(1, Math.PI, new Position(0, 0));
        testAircraft.moveForward(1);
        assertEquals(-1, testAircraft.getPosition().getX(), ACCEPTED_DELTA);
        assertEquals(0, testAircraft.getPosition().getY(), ACCEPTED_DELTA);

        // Test moving down (270°).
        testAircraft = new LargePlane(1, 3 * Math.PI / 2, new Position(0, 0));
        testAircraft.moveForward(1);
        assertEquals(0, testAircraft.getPosition().getX(), ACCEPTED_DELTA);
        assertEquals(-1, testAircraft.getPosition().getY(), ACCEPTED_DELTA);
    }

    /**
     * Test the Aircraft crash. They should crash if they are close enough, at a distance lower than
     * both radius.
     */
    @Test
    public void testCrash() {
        // Note: Large Planes are being used for this test. Their radius is 5.
        Aircraft testAircraft = new LargePlane(1, 0, new Position(0, 0));
        Aircraft otherAircraft;

        // Same position. Crash.
        otherAircraft = new LargePlane(1, 0, new Position(0, 0));
        assertTrue(testAircraft.crashesWith(otherAircraft));

        // At a distance of both radius. Crash.
        otherAircraft = new LargePlane(1, 0, new Position(10, 0));
        assertTrue(testAircraft.crashesWith(otherAircraft));

        // Farther than both radius. Shouldn't crash.
        otherAircraft = new LargePlane(1, 0, new Position(15, 0));
        assertFalse(testAircraft.crashesWith(otherAircraft));
    }

    /**
     * Test the Aircraft landing to see if they land on their correct landing site.
     */
    @Test
    public void testLandingSiteType() {
        // Test Aircraft and Sites.
        Aircraft testAircraft;
        Position originPosition = new Position(0, 0);
        LandingSite testLongRunway = new LongRunway(originPosition, 0, 0);
        LandingSite testShortRunway = new ShortRunway(originPosition, 0, 0);
        LandingSite testHelipad = new Helipad(originPosition);

        // Test for Large Plane landing. They should only land on Long Runways.
        testAircraft = new LargePlane(1, 0, originPosition);
        assertTrue(testAircraft.land(testLongRunway));
        assertFalse(testAircraft.land(testShortRunway));
        assertFalse(testAircraft.land(testHelipad));

        // Test for Light Plane landing. They should can land on both Long and Short Runways.
        testAircraft = new LightPlane(1, 0, originPosition);
        assertTrue(testAircraft.land(testLongRunway));
        assertTrue(testAircraft.land(testShortRunway));
        assertFalse(testAircraft.land(testHelipad));

        // Test for Helicopter landing. They can only land on Helipads.
        testAircraft = new Helicopter(1, 0, originPosition);
        assertFalse(testAircraft.land(testLongRunway));
        assertFalse(testAircraft.land(testShortRunway));
        assertTrue(testAircraft.land(testHelipad));
    }

    /**
     * Test the Aircraft landing direction. They should land if they enter in the correct angle.
     */
    @Test
    public void testLandingDirection() {
        // The center and aperture angles are set in such way that the aperture of the landing site
        // goes from 0° to 60° (center +/- aperture/2).
        double centerAngle = Math.toRadians(30);
        double apertureAngle = Math.toRadians(60);

        // Angles are tested at the limits and outside them. For the case inside the limits, the
        // center angle is used.
        double testBelowLowerLimit = Math.toRadians(-10);
        double testLowerLimit = Math.toRadians(0);
        double testUpperLimit = Math.toRadians(60);
        double testPastUpperLimit = Math.toRadians(70);

        // Test Aircraft and Sites.
        Aircraft testAircraft;
        Position originPosition = new Position(0, 0);
        LandingSite testLongRunway = new LongRunway(originPosition, centerAngle, apertureAngle);
        LandingSite testShortRunway = new ShortRunway(originPosition, centerAngle, apertureAngle);

        // Test landing with the center angle.
        testAircraft = new LargePlane(1, centerAngle, originPosition);
        assertTrue(testAircraft.land(testLongRunway));
        testAircraft = new LightPlane(1, centerAngle, originPosition);
        assertTrue(testAircraft.land(testLongRunway));
        assertTrue(testAircraft.land(testShortRunway));

        // Test Large Planes on Long Runways with the limit angles.
        testAircraft = new LargePlane(1, testLowerLimit, originPosition);
        assertTrue(testAircraft.land(testLongRunway));
        testAircraft = new LargePlane(1, testUpperLimit, originPosition);
        assertTrue(testAircraft.land(testLongRunway));

        // Test Light Planes on Long and Short Runways with the limit angles.
        testAircraft = new LightPlane(1, testLowerLimit, originPosition);
        assertTrue(testAircraft.land(testLongRunway));
        assertTrue(testAircraft.land(testShortRunway));
        testAircraft = new LightPlane(1, testUpperLimit, originPosition);
        assertTrue(testAircraft.land(testLongRunway));
        assertTrue(testAircraft.land(testShortRunway));

        // Test Large Planes on Long Runways with angles outside the aperture.
        testAircraft = new LargePlane(1, testBelowLowerLimit, originPosition);
        assertFalse(testAircraft.land(testLongRunway));
        testAircraft = new LargePlane(1, testPastUpperLimit, originPosition);
        assertFalse(testAircraft.land(testLongRunway));

        // Test Light Planes on Long and Short Runways with angles outside the aperture.
        testAircraft = new LightPlane(1, testBelowLowerLimit, originPosition);
        assertFalse(testAircraft.land(testLongRunway));
        assertFalse(testAircraft.land(testShortRunway));
        testAircraft = new LightPlane(1, testPastUpperLimit, originPosition);
        assertFalse(testAircraft.land(testLongRunway));
        assertFalse(testAircraft.land(testShortRunway));
    }
}
