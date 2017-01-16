package mas.german.landingplanes;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import mas.german.landingplanes.aircrafts.Aircraft;
import mas.german.landingplanes.aircrafts.Helicopter;
import mas.german.landingplanes.aircrafts.LargePlane;
import mas.german.landingplanes.aircrafts.LightPlane;

/**
 * AircraftGenerator class is in charge of the task to create a random Aircraft every random time.
 * Once the aircraft is generated, it's communicates it to the classes implementing it's interface.
 */
public class AircraftGenerator implements Runnable {
    private static final String TAG = AircraftGenerator.class.getSimpleName();
    private final int TIME_MIN_MS = 1000;
    private final int TIME_DELTA_MS = 1000;

    private ScheduledExecutorService mExecutor;

    private Map mMap;
    private Random mRandom;
    private int mTime;

    private OnAircraftGenerated mOnAircraftGeneratedListener;

    /**
     * Interface to be implemented by the classes that need access to the randomly generated
     * aircrafts.
     */
    public interface OnAircraftGenerated {
        void onAircraftGenerated(Aircraft generatedAircraft);
    }

    /**
     * Sets the Listener in the classes that require randomly generated aircrafts.
     */
    public void setOnAircraftGeneratedListener(OnAircraftGenerated listener) {
        mOnAircraftGeneratedListener = listener;
    }

    AircraftGenerator(Map currentMap) {
        mRandom = new Random(System.currentTimeMillis());
        mMap = currentMap;
        // Start a new executor thread.
        mExecutor = Executors.newScheduledThreadPool(1);
    }

    /**
     * Schedules the first task. The task re-schedules itself after some time defined at random.
     */
    public void begin() {
        mTime = TIME_MIN_MS + mRandom.nextInt(TIME_DELTA_MS);
        mExecutor.schedule(this, mTime, TimeUnit.MILLISECONDS);
    }

    /**
     * Generates a random Aircraft.
     */
    private Aircraft generateRandomAircraft() {
        Aircraft randomAircraft;
        Position randomPosition;

        // First calculate the position, as the range of directions also depends on it.
        // The aircraft starts from a side of the Map, and it's direction is set accordingly.
        double x = 0;
        double y = 0;
        double angle = 0;
        switch (3) {
        //switch (mRandom.nextInt(3)) {
            case 0:
                // Starts somewhere in the bottom edge
                x = mRandom.nextDouble()*mMap.getBoundaryRight();
                y = mMap.getBoundaryBottom();
                // 0° < angle < 180°
                angle = mRandom.nextInt(180);
                break;
            case 1:
                // Starts somewhere in the right edge
                x = mMap.getBoundaryRight();
                y = mRandom.nextDouble()*mMap.getBoundaryTop();
                // 90° < angle < 270°
                angle = 90 + mRandom.nextInt(180);
                break;
            case 2:
                // Starts somewhere in the top edge.
                x = mRandom.nextDouble()*mMap.getBoundaryRight();
                y = mMap.getBoundaryTop();
                // 180° < angle < 360°
                angle = 180 + mRandom.nextInt(180);
                break;
            case 3:
                // Starts somewhere in the left edge.
                x = mMap.getBoundaryLeft();
                y = mRandom.nextDouble()*mMap.getBoundaryTop();
                // -90° < angle < 90°
                angle = 90 - mRandom.nextInt(180);
                if (angle < 0) {
                    angle += 360;
                }
                break;
        }
        angle = Math.toRadians(angle);
        randomPosition = new Position(x, y);

        // Calculate and instantiate the type of Aircraft.
        // Speed is set at random according to the type.
        switch (mRandom.nextInt(3)) {
            case 0:
                randomAircraft = new LargePlane(mRandom.nextInt(10) + 10, angle, randomPosition);
                break;
            case 1:
                randomAircraft = new LightPlane(mRandom.nextInt(5) + 10, angle, randomPosition);
                break;
            case 2:
                randomAircraft = new Helicopter(mRandom.nextInt(5) + 5, angle, randomPosition);
                break;
            default:
                randomAircraft = new LargePlane(0, angle, randomPosition);
        }
        return randomAircraft;
    }

    @Override
    public void run() {
        mTime = TIME_MIN_MS + mRandom.nextInt(TIME_DELTA_MS);
        mOnAircraftGeneratedListener.onAircraftGenerated(generateRandomAircraft());
        // Re-schedule the current task with the new time.
        mExecutor.schedule(this, mTime, TimeUnit.MILLISECONDS);
    }
}
