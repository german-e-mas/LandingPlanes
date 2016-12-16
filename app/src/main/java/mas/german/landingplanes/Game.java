package mas.german.landingplanes;

import mas.german.landingplanes.aircrafts.*;
import mas.german.landingplanes.landingsites.*;

import java.util.ArrayList;
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
public class Game {
    private static final String TAG = Game.class.getSimpleName();
    private static final int UPDATE_MS = 200;

    private static Game sInstance = null;

    private ArrayList<Aircraft> mAircrafts;
    private ArrayList<LandingSite> mSites;
    private int mScore;

    private ScheduledExecutorService mExecutor;
    private ScheduledFuture<?> mUpdateTask;

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
        mScore = 0;

        initialTest();

        // Periodic task to update the game status.
        mUpdateTask = mExecutor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                for (Aircraft aircraft : mAircrafts) {
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
                            synchronized (mAircrafts) {
                                mAircrafts.remove(aircraft);
                            }
                        }
                    }

                    // Delete aircrafts that are outside the map.
                    if (aircraft.isOutOfBounds()) {
                        synchronized (mAircrafts) {
                            mAircrafts.remove(aircraft);
                        }
                    }
                }
            }
        }, 0, UPDATE_MS, TimeUnit.MILLISECONDS);
    }

    /**
     * Auxiliar method for testing. Creates a Large Plane at (0,0) in the direction of a long runway
     * When it's close to it, the plane lands.
     */
    private void initialTest() {
        mAircrafts.add(new LargePlane(2, 0, new Position(0,0)));
        mSites.add(new LongRunway(new Position(20,0)));
    }

    /**
     * Game over procedure.
     */
    private void gameOver() {
        // Cancel the Update Task.
        mUpdateTask.cancel(true);
        // Clean the airplanes and sites.
        mAircrafts.clear();
        mSites.clear();
    }
}
