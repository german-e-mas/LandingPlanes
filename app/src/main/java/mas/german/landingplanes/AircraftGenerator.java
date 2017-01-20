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
        // Starts a new thread if the executor was shut down before.
        if (mExecutor.isShutdown()) {
            mExecutor = Executors.newScheduledThreadPool(1);
        }
        mTime = TIME_MIN_MS + mRandom.nextInt(TIME_DELTA_MS);
        mExecutor.schedule(this, mTime, TimeUnit.MILLISECONDS);
    }

    /**
     * Stops the random aircraft generation task. Scheduled generation tasks are canceled, as we
     * don't want any after calling this method.
     */
    public void stop() {
        mExecutor.shutdownNow();
    }

    /**
     * Generates a random Aircraft.
     */
    private Aircraft generateRandomAircraft() {
        // First calculate the position, as the range of directions also depends on it.
        // The aircraft starts from a side of the Map, and it's direction is set accordingly.
        double x = 0;
        double y = 0;
        double angle = 0;
        switch (mRandom.nextInt(4)) {
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
                // Keep the angle in positive values.
                if (angle < 0) {
                    angle += 360;
                }
                break;
        }
        // The angle generated is in degrees. Convert it to radians.
        angle = Math.toRadians(angle);
        Position randomPosition = new Position(x, y);

        // Calculate and instantiate the type of Aircraft.
        // Speed is set at random according to the type.
        Aircraft randomAircraft = null;
        switch (mRandom.nextInt(3)) {
            case 0:
                randomAircraft = new LargePlane(mRandom.nextInt(LargePlane.MAX_SPEED) +
                    LargePlane.MIN_SPEED, angle, randomPosition);
                break;
            case 1:
                randomAircraft = new LightPlane(mRandom.nextInt(LightPlane.MAX_SPEED) +
                    LightPlane.MIN_SPEED, angle, randomPosition);
                break;
            case 2:
                randomAircraft = new Helicopter(mRandom.nextInt(Helicopter.MAX_SPEED) +
                    Helicopter.MIN_SPEED, angle, randomPosition);
                break;
        }
        return randomAircraft;
    }

    @Override
    public void run() {
        mTime = TIME_MIN_MS + mRandom.nextInt(TIME_DELTA_MS);
        Aircraft randomAircraft = generateRandomAircraft();
        if (randomAircraft != null) {
            mOnAircraftGeneratedListener.onAircraftGenerated(randomAircraft);
        }
        // Re-schedule the current task with the new time.
        mExecutor.schedule(this, mTime, TimeUnit.MILLISECONDS);
    }
}
