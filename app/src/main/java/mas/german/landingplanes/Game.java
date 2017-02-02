package mas.german.landingplanes;

import mas.german.landingplanes.aircrafts.*;
import mas.german.landingplanes.landingsites.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Game class contains the logic of all the game. It is a singleton because only one instance should
 * exist.
 *
 * This class does the following tasks:
 * - Generates Landing Sites and store them on a array.
 * - Detects collisions of aircrafts, and finishes the game if they happen.
 * - Detects landings and increases the score.
 * - Checks if an aircraft has an invalid position and takes it off the array.
 */
public class Game implements AircraftGenerator.OnAircraftGenerated {
    private static final String TAG = Game.class.getSimpleName();
    private static final int UPDATE_MS = 200;

    private static Game sInstance = null;

    private ArrayList<Aircraft> mAircrafts;
    private ArrayList<LandingSite> mSites;
    private int mScore;
    private Map mMap;

    private ScheduledExecutorService mExecutor;
    private ScheduledFuture<?> mUpdateTask;

    private AircraftGenerator mGenerator;

    /**
     * Get the unique instance of the Game class.
     *
     * @return Game instance.
     */
    public static Game getInstance() {
        if (sInstance == null) {
            sInstance = new Game();
        }
        return sInstance;
    }

    private Game() {
        // Creates the periodic Tasks.
        mExecutor = Executors.newScheduledThreadPool(1);
        // Containers for all the active aircrafts and landing sites.
        mAircrafts = new ArrayList<>();
        mSites = new ArrayList<>();
        // Other game-related variables.
        mMap = new Map(0,100,100,0);
        mGenerator = new AircraftGenerator(mMap);
        mGenerator.setOnAircraftGeneratedListener(this);
        mGenerator.begin();
        mScore = 0;

        setStartingSites();

        // Periodic task to update the game status.
        mUpdateTask = mExecutor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                // Aircraft iterator for safely handle the removal of aircrafts from the list.
                synchronized (mAircrafts) {
                    Iterator<Aircraft> iterator = mAircrafts.iterator();
                    while (iterator.hasNext()) {
                        Aircraft aircraft = iterator.next();

                        // Move the aircraft.
                        aircraft.moveForward();

                        // Check for any crash.
                        for (Aircraft otherAircraft : mAircrafts) {
                            if (aircraft.crashesWith(otherAircraft)) {
                                gameOver();
                            }
                        }

                        // Check for any landing.
                        for (LandingSite site : mSites) {
                            if (aircraft.land(site)) {
                                mScore++;
                                iterator.remove();
                            }
                        }

                        // Delete aircrafts that are outside the map.
                        if (mMap.isOutOfBounds(aircraft)) {
                            iterator.remove();
                        }
                    }
                }
            }
        }, 0, UPDATE_MS, TimeUnit.MILLISECONDS);
    }

    /**
     * Creates the initial Landing Sites. They are currently hardcoded in the given positions.
     */
    private void setStartingSites() {
        mSites.add(new LongRunway(new Position(50, 75)));
        mSites.add(new ShortRunway(new Position(75, 25)));
        mSites.add(new Helipad(new Position(25, 25)));
    }

    /**
     * Game over procedure.
     */
    private void gameOver() {
        // Stop the Generator.
        mGenerator.stop();
        // Cancel the Update Task.
        mUpdateTask.cancel(true);
    }

    @Override
    public void onAircraftGenerated(Aircraft generatedAircraft) {
        // Synchronize the Aircraft list to prevent access during the operation.
        synchronized (mAircrafts) {
            mAircrafts.add(generatedAircraft);
        }
    }
}
