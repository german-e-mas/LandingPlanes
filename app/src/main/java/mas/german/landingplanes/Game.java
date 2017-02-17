package mas.german.landingplanes;

import java.util.List;
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

    /**
     * Various game-related events that a listener may want to react upon.
     */
    public interface Listener {
        /**
         * Let the listener know the game is finished.
         */
        void onGameOver();

        /**
         * A new Aircraft was created. This encompasses all types of aircrafts.
         *
         * @param aircraft  The aircraft that was generated.
         */
        void onAircraftGenerated(Aircraft aircraft);

        /**
         * A new Large Plane was created.
         *
         * @param largePlane  The large plane that was generated.
         */
        void onLargePlaneGenerated(LargePlane largePlane);

        /**
         * A new Light Plane was created.
         *
         * @param lightPlane  The large plane that was generated.
         */
        void onLightPlaneGenerated(LightPlane lightPlane);

        /**
         * A new Helicopter was created.
         *
         * @param helicopter  The large plane that was generated.
         */
        void onHelicopterGenerated(Helicopter helicopter);

        /**
         * An update cycle was completed. Notify the listeners in order to reflect changes in
         * position of the Aircrafts.
         */
        void onUpdateCycle();

        /**
         * An aircraft has landed.
         *
         * @param id    ID of the Aircraft that landed.
         */
        void onLand(int id);

        /**
         * An aircraft has left the game's map.
         *
         * @param id    ID of the Aircraft that left the map.
         */
        void onAircraftOutsideMap(int id);

        /**
         * A crash happened.
         *
         * @param firstId   ID of the first Aircraft involved in the crash.
         * @param secondId  ID of the second Aircraft involved in the crash.
         */
        void onCrash(int firstId, int secondId);
    }

    public void setListener(Listener listener) {
        mListener = listener;
    }

    private ArrayList<Aircraft> mAircrafts;
    private ArrayList<LandingSite> mSites;
    private int mScore;
    private Map mMap;
    private Listener mListener;

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
                        aircraft.moveForward();

                        // Check for any landing.
                        for (LandingSite site : mSites) {
                            if (aircraft.land(site)) {
                                if (mListener != null) {
                                    mListener.onLand(aircraft.getId());
                                }
                                mScore++;
                                iterator.remove();
                            }
                        }

                        // Delete aircrafts that are outside the map.
                        if (mMap.isOutOfBounds(aircraft)) {
                            if (mListener != null) {
                                mListener.onAircraftOutsideMap(aircraft.getId());
                            }
                            iterator.remove();
                        }

                        // Check for any crash.
                        for (Aircraft otherAircraft : mAircrafts) {
                            if (aircraft.crashesWith(otherAircraft)) {
                                if (mListener != null) {
                                    mListener.onCrash(aircraft.getId(), otherAircraft.getId());
                                }
                                gameOver();
                                return;
                            }
                        }
                    }

                    // An update cycle was finished.
                    if (mListener != null) {
                        mListener.onUpdateCycle();
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
        // Notify the Listener about the event.
        if (mListener != null) {
            mListener.onGameOver();
        }
    }

    /**
     * Return the list of currently existing Aircraft.
     */
    public synchronized List<Aircraft> getAircraft() {
        return mAircrafts;
    }

    public List<LandingSite> getSites() {
        return mSites;
    }

    public Map getMap() {
        return mMap;
    }

    @Override
    public void onAircraftGenerated(Aircraft generatedAircraft) {
        // Synchronize the Aircraft list to prevent access during the operation.
        synchronized (mAircrafts) {
            mAircrafts.add(generatedAircraft);
        }
        // Notify the Listener of the creation of a new Aircraft.
        if (mListener != null) {
            mListener.onAircraftGenerated(generatedAircraft);
        }
        // Make the Aircraft notify itself. It's subtypes will call the corresponding method.
        generatedAircraft.notifyCreation(this);
    }

    /**
     * Notify the listener that a Large Plane was created. This is called from the corresponding
     * subclass of Aircraft, after calling their notifyCreation method.
     */
    public void createdLargePlane(LargePlane largePlane) {
        if (mListener != null) {
            mListener.onLargePlaneGenerated(largePlane);
        }
    }

    /**
     * Notify the listener that a Light Plane was created. This is called from the corresponding
     * subclass of Aircraft, after calling their notifyCreation method.
     */
    public void createdLightPlane(LightPlane lightPlane) {
        if (mListener != null) {
            mListener.onLightPlaneGenerated(lightPlane);
        }
    }

    /**
     * Notify the listener that a Helicopter was created. This is called from the corresponding
     * subclass of Aircraft, after calling their notifyCreation method.
     */
    public void createdHelicopter(Helicopter helicopter) {
        if (mListener != null) {
            mListener.onHelicopterGenerated(helicopter);
        }
    }
}
